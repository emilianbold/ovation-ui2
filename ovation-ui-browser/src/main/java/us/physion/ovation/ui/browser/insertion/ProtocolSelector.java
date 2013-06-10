/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui.browser.insertion;

import com.google.common.collect.Lists;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.openide.util.ChangeSupport;
import org.openide.util.Lookup;
import us.physion.ovation.DataContext;
import us.physion.ovation.DataStoreCoordinator;
import us.physion.ovation.domain.Protocol;
import us.physion.ovation.exceptions.OvationException;
import us.physion.ovation.ui.browser.EntityWrapper;
import us.physion.ovation.ui.interfaces.ConnectionProvider;

/**
 *
 * @author jackie
 */
public class ProtocolSelector extends javax.swing.JPanel {

    DefaultListModel listModel;
    
    ChangeSupport cs;
    private DataContext context;
    
    List<Protocol> protocols;
    
    Map<String, String> newProtocols;
    Map<UUID, String> editedProtocols;
    boolean noneSelectable;
    boolean allowEditExistingProtocols;
    
    @Override
    public String getName() {
        return "Select an existing Protocol, or create your own";
    }
    
    public ProtocolSelector(ChangeSupport cs, boolean noneSelectable, boolean allowEditExistingProtocols)
    {
        this(cs, Lookup.getDefault().lookup(ConnectionProvider.class).getDefaultContext(), noneSelectable, allowEditExistingProtocols);
    }

    /**
     * Creates new form ProtocolSelector
     */
    public ProtocolSelector(ChangeSupport cs, DataContext ctx, boolean noneSelectable, boolean allowEdit) {
        this.noneSelectable = noneSelectable;
        this.allowEditExistingProtocols = allowEdit;
        this.cs = cs;
        this.context = ctx;
        initComponents();
        jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jSplitPane1.setDividerLocation(170);

        resetProtocols();
        
        jList1.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                Protocol selected = getProtocol();
                if (selected == null)
                {
                    String name = (String)listModel.get(lse.getLastIndex());
                    if (newProtocols.containsKey(name))
                    {
                        jTextArea1.setText(newProtocols.get(name));
                        jTextArea1.setEditable(true);
                    }else{
                        //this happens when <none> is selected
                        jTextArea1.setText("");
                        jTextArea1.setEditable(false);
                    }
                }else{
                    jTextArea1.setText(selected.getProtocolDocument());
                    jTextArea1.setEditable(allowEditExistingProtocols && 
                            context.getAuthenticatedUser().equals(selected.getOwner()));
                }
            }
        });
        
        addProtocolButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                String protocolName = jTextField1.getText();
                if (protocolName == null || protocolName.isEmpty())
                    return;
                addToNewProtocolList(protocolName);
                jTextField1.setText("");
                //TODO: uncomment
                //ProtocolSelector.this.cs.fireChange();
            }
        });
        
        jTextArea1.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent ke) {
            }

            @Override
            public void keyReleased(KeyEvent ke) {
                if (jTextArea1.isEditable()) {
                    Protocol selected = getProtocol();
                    if (selected == null) {
                        String name = (String) listModel.get(jList1.getSelectedIndex());
                        if (newProtocols.containsKey(name)) {
                            newProtocols.put(name, jTextArea1.getText());
                        }
                    } else {
                        editedProtocols.put(selected.getUuid(), jTextArea1.getText());
                    }
                    ProtocolSelector.this.cs.fireChange();
                }
            }
        });
    }
    
    public void resetProtocols()
    {
        newProtocols = new HashMap<String, String>();
        editedProtocols = new HashMap<UUID, String>();
        DefaultListModel newModel = new DefaultListModel();
        
        protocols = Lists.newArrayList(context.getProtocols());
        for (Protocol p : protocols)
        {
            newModel.addElement(p.getName());
        }
        if (noneSelectable)
            newModel.addElement("<none>");

        jList1.setModel(newModel);
        listModel = newModel;

    }
    
    public Protocol getProtocol()
    {
        int selected = jList1.getSelectedIndex();
        if (selected >= 0 && selected < protocols.size())
            return protocols.get(selected);
        return null;
    }
    
    protected void addToNewProtocolList(String name)
    {
        listModel.add(listModel.size() -1, name);
        newProtocols.put(name, "");
    }
    
    public Map<String, String> getNewProtocols()
    {
        return newProtocols;
    }
    
    public Map<UUID, String> getEditedProtocols()
    {
        return editedProtocols;
    }
    
    public String getProtocolName()
    {
        int selected = jList1.getSelectedIndex();
        if (selected >= 0 && selected < listModel.getSize()-1)
            return (String)listModel.get(selected);
        return "";
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        addProtocolButton = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jSplitPane1.setRightComponent(jScrollPane2);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(ProtocolSelector.class, "ProtocolSelector.jLabel1.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(ProtocolSelector.class, "ProtocolSelector.jTextField1.text")); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        addProtocolButton.setText(org.openide.util.NbBundle.getMessage(ProtocolSelector.class, "ProtocolSelector.addProtocolButton.text")); // NOI18N

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jSplitPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 707, Short.MAX_VALUE)
            .add(layout.createSequentialGroup()
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jTextField1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(addProtocolButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jSplitPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 342, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 31, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(addProtocolButton)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addProtocolButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
