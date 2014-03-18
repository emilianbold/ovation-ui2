/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui.browser.insertion;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.util.*;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.openide.util.ChangeSupport;
import static us.physion.ovation.ui.browser.insertion.DatePickers.zonedDate;
import us.physion.ovation.ui.interfaces.DateTimePicker;

public final class InsertEpochGroupVisualPanel2 extends JPanel{

    private ChangeSupport cs;
    private String label;
    private DateTime start;
    private DateTimePicker startPicker;
    private String[] availableIDs;
    /**
     * Creates new form InsertEpochGroupVisualPanel2
     */
    public InsertEpochGroupVisualPanel2(ChangeSupport cs) {
        initComponents();

        this.cs = cs;
        label = "";
        startPicker = DatePickers.createDateTimePicker();
        startPicker.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                if ("date".equals(propertyChangeEvent.getPropertyName())) {
                    startDateTimeChanged();
                }
            }
        });

        jComboBox1.setSelectedItem(DatePickers.getID(startPicker));
        startTimePane.setViewportView(startPicker);
        start = null;
    }

    protected void startDateTimeChanged() {
        start = zonedDate(startPicker, jComboBox1);
        setStart(start);
    }
    @Override
    public String getName() {
        return "Insert Epoch Group";
    }

    String getLabel() {
        return label;
    }

    DateTime getStart() {
        return start;
    }

    protected void setLabel(String l)
    {
        boolean fireChange = true;
        if (label.isEmpty() == l.isEmpty())
            fireChange = false;
        label = l;

        if (fireChange)
            cs.fireChange();
    }
    protected void setStart(DateTime t)
    {
        start = t;
        cs.fireChange();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        labelTextField = new javax.swing.JTextField();
        startTimePane = new javax.swing.JScrollPane();
        jComboBox1 = new javax.swing.JComboBox();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(InsertEpochGroupVisualPanel2.class, "InsertEpochGroupVisualPanel2.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(InsertEpochGroupVisualPanel2.class, "InsertEpochGroupVisualPanel2.jLabel2.text")); // NOI18N

        labelTextField.setText(org.openide.util.NbBundle.getMessage(InsertEpochGroupVisualPanel2.class, "InsertEpochGroupVisualPanel2.labelTextField.text")); // NOI18N
        labelTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                labelTextFieldActionPerformed(evt);
            }
        });
        labelTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                labelTextFieldKeyReleased(evt);
            }
        });

        startTimePane.setBackground(new java.awt.Color(204, 204, 204));
        startTimePane.setBorder(null);
        startTimePane.setPreferredSize(new java.awt.Dimension(200, 30));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(DatePickers.getTimeZoneIDs()));
        jComboBox1.setMaximumSize(new java.awt.Dimension(300, 32767));
        jComboBox1.setPreferredSize(new java.awt.Dimension(180, 30));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(startTimePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComboBox1, 0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(labelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(labelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(7, 7, 7)
                            .addComponent(jLabel2)
                            .addContainerGap(186, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jComboBox1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap(178, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(startTimePane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(179, Short.MAX_VALUE))))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void labelTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_labelTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_labelTextFieldActionPerformed

    private void labelTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_labelTextFieldKeyReleased
        setLabel(labelTextField.getText());
    }//GEN-LAST:event_labelTextFieldKeyReleased

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        setStart(new DateTime(startPicker.getDate(),  DateTimeZone.forID(((String)jComboBox1.getSelectedItem()))));
    }//GEN-LAST:event_jComboBox1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField labelTextField;
    private javax.swing.JScrollPane startTimePane;
    // End of variables declaration//GEN-END:variables
}
