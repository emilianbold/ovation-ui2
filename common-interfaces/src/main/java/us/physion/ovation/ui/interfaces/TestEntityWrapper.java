package us.physion.ovation.ui.interfaces;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.net.URI;
import java.util.Collections;
import java.util.List;
import us.physion.ovation.DataContext;
import us.physion.ovation.domain.OvationEntity;
import us.physion.ovation.domain.*;

/**
 *
 * @author huecotanks
 */
public class TestEntityWrapper implements IEntityWrapper{

    DataContext ctx;
    String uri;
    String displayName;
    Class type;
    public TestEntityWrapper(DataContext ctx, OvationEntity e)
    {
        this.ctx = ctx;
        uri = e.getURI().toString();
        type = e.getClass();
        displayName = inferDisplayName(e);
    }
    @Override
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public OvationEntity getEntity() {
        return ctx.getObjectWithURI(uri);
    }

    @Override
    public OvationEntity getEntity(boolean includeTrash) {
        return ctx.getObjectWithURI(uri, includeTrash);
    }

    @Override
    public Class getType() {
        return type;
    }

    @Override
    public String getURI() {
        return uri;
    }

    //TODO: call this on some static method in our iterfaces jar
    protected String inferDisplayName(OvationEntity e)
    {
        Class type = e.getClass();
        if (type.isAssignableFrom(Source.class))
        {
            return ((Source)e).getLabel();
        }
        else if (type.isAssignableFrom(Project.class))
        {
            return ((Project)e).getName();
        }else if (type.isAssignableFrom(Experiment.class))
        {
            return ((Experiment)e).getStart().toString("MM/dd/yyyy-hh:mm:ss");
        }
        else if (type.isAssignableFrom(EpochGroup.class))
        {
            return ((EpochGroup)e).getLabel();
        }
        else if (type.isAssignableFrom(Epoch.class))
        {
            return ((Epoch)e).getStart().toString("MM/dd/yyyy-hh:mm:ss");
        }
        else if (type.isAssignableFrom(Measurement.class))
        {
            return ((Measurement)e).getName();
        }
        else if (type.isAssignableFrom(AnalysisRecord.class))
        {
            return ((AnalysisRecord)e).getName();
        }
        return "<no name>";
    }

    @Override
    public <T extends OvationEntity> T getEntity(Class<T> clazz) {
        return (T)getEntity();
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public boolean canRename() {
        return false;
    }

    @Override
    public void setName(String s) {
        throw new IllegalStateException("Cannot rename");
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Color getDisplayColor() {
        return Color.black;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {

    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {

    }

}
