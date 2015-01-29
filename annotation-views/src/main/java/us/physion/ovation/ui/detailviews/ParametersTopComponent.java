/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui.detailviews;

import java.util.*;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;
import us.physion.ovation.domain.AnalysisRecord;
import us.physion.ovation.domain.Measurement;
import us.physion.ovation.domain.OvationEntity;
import us.physion.ovation.domain.Resource;
import us.physion.ovation.domain.mixin.ProcedureElement;
import us.physion.ovation.ui.*;
import us.physion.ovation.ui.interfaces.EventQueueUtilities;
import us.physion.ovation.ui.interfaces.IEntityWrapper;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physion.ovation.detailviews//Parameters//EN",
autostore = false)
@TopComponent.Description(preferredID = "ParametersTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE",
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "leftSlidingSide", openAtStartup = true)
@ActionID(category = "Window", id = "us.physion.ovation.detailviews.ParametersTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_ParametersAction",
preferredID = "ParametersTopComponent")
@Messages({
    "CTL_ParametersAction=Parameters",
    "CTL_ParametersTopComponent=Parameters",
    "HINT_ParametersTopComponent=Parameters (protocol, device, analysis, etc.) of the selected entities"
})
public final class ParametersTopComponent extends TopComponent {

    Lookup.Result global;
    private Collection<? extends IEntityWrapper> entities;
    private LookupListener listener = new LookupListener() {

        @Override
        public void resultChanged(LookupEvent le) {

            //TODO: we should have some other Interface for things that can setEntities the tags view
            //then we could get rid of the Library dependancy on the Explorer API
            if (TopComponent.getRegistry().getActivated() instanceof ExplorerManager.Provider)
            {
                update();
            }
        }
    };

    public void update()
    {
        EventQueueUtilities.runOffEDT(new Runnable() {

            public void run() {
                setEntities(global.allInstances());
            }
        });
    }

    public List<TableTreeKey> setEntities(final Collection<? extends IEntityWrapper> entities)
    {
        this.entities = entities;

        Map<String, Map<String, Object>> tables = new HashMap<>();
        for (IEntityWrapper ew : entities)
        {
            OvationEntity eb = ew.getEntity();
            if (eb instanceof AnalysisRecord)
            {
                addParams(tables, "Analysis Parameters", ((AnalysisRecord)eb).getProtocolParameters());
            }
            else if (eb instanceof ProcedureElement)
            {
                addParams(tables, "Device Parameters", ((ProcedureElement)eb).getDeviceParameters());
                addParams(tables, "Protocol Parameters", ((ProcedureElement)eb).getProtocolParameters());
            } else if (eb instanceof Measurement) {
                addParams(tables, "Device Parameters", ((Measurement) eb).getEpoch().getDeviceParameters());
                addParams(tables, "Protocol Parameters", ((Measurement) eb).getEpoch().getProtocolParameters());
            } else if (eb instanceof Resource) {
                addParams(tables, "Analysis Parameters", ((AnalysisRecord) ((Resource) eb).getContainingEntity()).getProtocolParameters());
            }
        }
        List<TableTreeKey> tableKeys = new ArrayList<>();
        for (String key: tables.keySet())
        {
            tableKeys.add(new ParameterSet(key, tables.get(key)));
        }
        ((ScrollableTableTree)tableTreeScrollPane).setKeys(tableKeys);
        return tableKeys;
    }

    public Collection<? extends IEntityWrapper> getEntities()
    {
        return entities;
    }

    public ScrollableTableTree getTableTree()
    {
        return ((ScrollableTableTree)tableTreeScrollPane);
    }

    protected void setTableTree(ScrollableTableTree t)
    {
        tableTreeScrollPane = t;
    }

    public ParametersTopComponent() {
        initComponents();
        setName(Bundle.CTL_ParametersTopComponent());
        setToolTipText(Bundle.HINT_ParametersTopComponent());

        global = Utilities.actionsGlobalContext().lookupResult(IEntityWrapper.class);
        global.addLookupListener(listener);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tableTreeScrollPane = new us.physion.ovation.ui.ScrollableTableTree();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(tableTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 359, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(tableTreeScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 368, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane tableTreeScrollPane;
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    private void addParams(Map<String, Map<String, Object>> tables, String paramName, Map<String, Object> parametersToAdd) {
        Map<String, Object> params = tables.get(paramName);
        if (params == null) {
            params = parametersToAdd;
        } else {
            //create a new map here, because the Ovation jar returns unmodifiable maps
            params = new HashMap<String, Object>();
            params.putAll(tables.get(paramName));
            for (String paramKey : parametersToAdd.keySet() ) {
                if (params.containsKey(paramKey))
                {
                    MultiUserParameter p = new MultiUserParameter(params.get(paramKey));
                    p.add(parametersToAdd.get(paramKey));
                    params.put(paramKey, p);
                }else{
                    params.put(paramKey, parametersToAdd.get(paramKey));
                }
            }
        }
        tables.put(paramName, params);
    }
}
