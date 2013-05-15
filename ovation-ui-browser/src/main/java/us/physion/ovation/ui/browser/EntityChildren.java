/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui.browser;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.awt.EventQueue;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;
import javax.swing.SwingUtilities;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ovation.*;
import us.physion.ovation.DataContext;
import us.physion.ovation.DataStoreCoordinator;
import us.physion.ovation.domain.*;
import us.physion.ovation.ui.interfaces.ConnectionProvider;
import us.physion.ovation.ui.browser.EntityWrapper;
import us.physion.ovation.ui.interfaces.EventQueueUtilities;
import us.physion.ovation.ui.interfaces.IEntityWrapper;


/**
 *
 * @author huecotanks
 */
public class EntityChildren extends Children.Keys<EntityWrapper> {

    EntityWrapper parent;
    boolean projectView;
    DataStoreCoordinator dsc;

    EntityChildren(EntityWrapper e, boolean pView, DataStoreCoordinator theDSC) {
        parent = e;
        projectView = pView;
        dsc = theDSC;
        //if its per user, we create 
        if (e instanceof PerUserEntityWrapper)
        {
            setKeys(((PerUserEntityWrapper)e).getChildren());
        }else{
            initKeys();
        }
    }

    private Callable<Children> getChildrenCallable(final EntityWrapper key)
    {
        return new Callable<Children>() {

            @Override
            public Children call() throws Exception {
                return new EntityChildren(key, projectView, dsc);
            }
        };
    }

    @Override
    protected Node[] createNodes(final EntityWrapper key) 
    {
        return new Node[]{EntityWrapperUtilities.createNode(key, Children.createLazy(getChildrenCallable(key)))};
    }

   
    protected void updateWithKeys(final List<EntityWrapper> list)
    {
        EventQueueUtilities.runOnEDT(new Runnable(){

            @Override
            public void run() {
                setKeys(list);
                addNotify();
                refresh();
            }
        });
    }
    
    public void resetNode()
    {
        initKeys();
    }
    
    protected void initKeys()
    {
        EventQueueUtilities.runOffEDT(new Runnable(){

            @Override
            public void run() {
                createKeys();
            }
        });
    }
    
    protected void createKeys() {
        
        if (dsc == null)
            dsc = Lookup.getDefault().lookup(ConnectionProvider.class).getConnection();
        if (dsc == null) {
            return;
        }
        DataContext c = dsc.getContext();
        
        if (parent == null) {
            List<EntityWrapper> list = new LinkedList<EntityWrapper>();
            //case root node: add entityWrapper for each project
            if (projectView) {
                for (Project p : c.getProjects()) {
                    list.add(new EntityWrapper(p));
                }
            } else {
                for (Source s : c.getTopLevelSources()) {
                    list.add(new EntityWrapper(s));
                }
            }
            updateWithKeys(list);

        } else {
            updateWithKeys(createKeysForEntity(c, parent));
        }
    }

    protected List<EntityWrapper> createKeysForEntity(DataContext c, EntityWrapper ew) {

        DataContext context = dsc.getContext();
        List<EntityWrapper> list = new LinkedList<EntityWrapper>();
        Class entityClass = ew.getType();
        if (projectView) {
            if (Project.class.isAssignableFrom(entityClass)) {
                Project entity = (Project) ew.getEntity();
                for (Experiment e : entity.getExperiments())
                {
                    list.add(new EntityWrapper(e));
                }
                String currentUser = c.getAuthenticatedUser().getUsername();

                for (User user : c.getUsers()) {
                    List<EntityWrapper> l = Lists.newArrayList(Iterables.transform(entity.getAnalysisRecords(user),
                            new Function<AnalysisRecord, EntityWrapper>() {

                                @Override
                                public EntityWrapper apply(AnalysisRecord f) {
                                    return new EntityWrapper(f);
                                }
                            }));
                    if (l.size() > 0)
                        list.add(new PerUserEntityWrapper(user.getUsername(), user.getURI().toString(), l));
                    
                }

                return list;
            }
        } else {
            if (Source.class.isAssignableFrom(entityClass)) {
                Source entity = (Source) ew.getEntity();
                for (Source e : entity.getChildrenSources()) {
                    list.add(new EntityWrapper(e));
                }
                for (Epoch e : entity.getEpochs()) {
                    list.add(new EntityWrapper(e));
                }
                return list;
            }
        }
        if (Experiment.class.isAssignableFrom(entityClass)) {
            Experiment entity = (Experiment) ew.getEntity();

            for (EpochGroup eg : entity.getEpochGroups()) {
                list.add(new EntityWrapper(eg));
            }
            return list;
        } else if (EpochGroup.class.isAssignableFrom(entityClass)) {
            EpochGroup entity = (EpochGroup) ew.getEntity();

            for (EpochGroup eg : entity.getEpochGroups()) {
                list.add(new EntityWrapper(eg));
            }
            
            context.beginTransaction();
            try {
                for (Epoch e : entity.getEpochs()) {
                    list.add(new EntityWrapper(e));
                }
            } finally{
                context.commitTransaction();
            }
            return list;
        } else if (Epoch.class.isAssignableFrom(entityClass)) {
            context.beginTransaction();
            try {
                Epoch entity = (Epoch) ew.getEntity();
                for (Measurement m : entity.getMeasurements()) {
                    list.add(new EntityWrapper(m));
                }

                for (User user : c.getUsers()) {
                    List<EntityWrapper> l = Lists.newArrayList(Iterables.transform(entity.getAnalysisRecords(user),
                            new Function<AnalysisRecord, EntityWrapper>() {

                                @Override
                                public EntityWrapper apply(AnalysisRecord f) {
                                    return new EntityWrapper(f);
                                }
                            }));
                    if (l.size() > 0)
                        list.add(new PerUserEntityWrapper(user.getUsername(), user.getURI().toString(), l));    
                }
            } finally {
                context.commitTransaction();
            }
        }
        return list;
    }
}
