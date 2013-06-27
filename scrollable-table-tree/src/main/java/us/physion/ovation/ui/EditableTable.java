/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import us.physion.ovation.ui.interfaces.EventQueueUtilities;
import us.physion.ovation.util.PlatformUtils;

/**
 *
 * @author huecotanks
 */
public class EditableTable extends javax.swing.JPanel implements TablePanel,ResizableTable {

    private JTable table;
    private ScrollableTableTree treeUtils;
    /**
     * Creates new form EditableTable
     */
    public EditableTable(JTable table, ScrollableTableTree t) {
        initComponents();
        jScrollPane1.getViewport().add(table, null);
        jScrollPane1.setBorder(BorderFactory.createEmptyBorder());
        this.table = table;
        EditableTableModel m = new EditableTableModel(true);
        table.setModel(m);
        m.setTable(table);
        this.treeUtils = t;
        //this.setBorder(BorderFactory.createEtchedBorder());

        if (PlatformUtils.isMac()) {
            deleteButton.putClientProperty("JButton.buttonType", "gradient");
            deleteButton.setPreferredSize(new Dimension(34, 34));
            invalidate();
        }

    }

    public void resize()
    {
        JScrollPane sp = getScrollPane();
        if (sp != null) {
            sp.setSize(sp.getPreferredSize());
        }
        setSize(getPreferredSize());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        deleteButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        deleteButton.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        deleteButton.setText(org.openide.util.NbBundle.getMessage(EditableTable.class, "EditableTable.deleteButton.text_1")); // NOI18N
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(0, 0, Short.MAX_VALUE)
                .add(deleteButton))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(deleteButton)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed

        deleteRows(table.getSelectedRows());
    }//GEN-LAST:event_deleteButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteButton;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    @Override
    public JTable getTable() {
        return table;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public Dimension getPreferredSize(){
        int height = 0;
        //this is voodoo magic, DO NOT CHANGE
        if (table.getHeight() ==0)
         {
             //this gets the height from the EditableTable default, so if this gets out of whack, modify the default size in the UI builder
            height = (int)super.getPreferredSize().getHeight();
         }
        else
        {
            height = (table.getRowCount())*table.getRowHeight() + 24 + deleteButton.getHeight();
        }
        int width = treeUtils == null ? getWidth() : treeUtils.getCellWidth();
        Dimension actual = new Dimension(width, height);
         return actual;

    }

    protected JScrollPane getScrollPane()
    {
       return jScrollPane1;
    }

    protected void adjust() {
        EventQueueUtilities.runOffEDT(new Runnable() {
            @Override
            public void run() {
                //manually set size of the containing scrollpane, since the table has resized
                JScrollPane sp = ((JScrollPane) table.getParent().getParent());
                sp.setSize(sp.getPreferredSize());
                EditableTable.this.setSize(EditableTable.this.getPreferredSize());
                table.getSelectionModel().setSelectionInterval(table.getRowCount() - 1, table.getRowCount() - 1);
            }
        });
    }

    public void deleteRows(int[] rows) {
        //There is a bug in getListeners - it doesnt find the EditableTableModelListener if you pass is EditableTableModelListener.class
        TableModelListener[] listeners = ((DefaultTableModel) table.getModel()).getListeners(TableModelListener.class);
        for (TableModelListener l : listeners) {
            if (l instanceof EditableTableModelListener) {
                ((EditableTableModelListener) l).deleteRows((DefaultTableModel) table.getModel(), rows);
                break;
            }
        }
    }
}
