/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui.browser.insertion;

import java.awt.FileDialog;
import static java.awt.FileDialog.LOAD;
import java.awt.Frame;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import static javax.swing.JComponent.TOOL_TIP_TEXT_KEY;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.google.common.collect.Maps;
import org.apache.commons.io.FilenameUtils;
import org.openide.util.ChangeSupport;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.windows.WindowManager;
import us.physion.ovation.exceptions.OvationException;

/**
 *
 * @author huecotanks
 */
public class MeasurementPanel extends javax.swing.JPanel {

    private File file;
    private ChangeSupport cs;
    /**
     * Creates new form MeasurementPanel
     */
    public MeasurementPanel(ChangeSupport cs) {
        initComponents();
        this.cs = cs;
    }

    public void setFile(File f)
    {
        final Map<String,String> customContentTypes = Maps.newHashMap();
        customContentTypes.put("doc", "application/msword");
        customContentTypes.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        customContentTypes.put("xls", "application/vnd.ms-excel");
        customContentTypes.put("xlsx",  "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        customContentTypes.put("ppt", "application/vnd.ms-powerpoint");
        customContentTypes.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");

        customContentTypes.put("csv", "text/csv");

        file = f;
        jTextField1.setText(f.getName());
        jTextField2.setText(f.getName());
        String contentType = URLConnection.guessContentTypeFromName(f.getName());
        if (contentType == null) {
            final String extension = FilenameUtils.getExtension(f.getName());
            if (customContentTypes.containsKey(extension)) {
                contentType = customContentTypes.get(extension);
            } else {
                contentType = "application/octet-stream"; // fallback to binary
            }
        }
        jTextField3.setText(contentType);
        cs.fireChange();
    }

    public URL getFile()
    {
        if (file == null)
            return null;
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException ex) {
            return null;
        }
    }

    public String getContentType()
    {
        return jTextField3.getText();
    }

    public String getMeasurementName()
    {
        return jTextField2.getText();
    }

    public String getName()
    {
        return "Choose DataElement File";
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jLabel1.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jLabel2.text")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jLabel3.text")); // NOI18N

        jTextField1.setText(org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jTextField1.text")); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField1KeyTyped(evt);
            }
        });

        jTextField2.setText(org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jTextField2.text")); // NOI18N
        jTextField2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField2KeyTyped(evt);
            }
        });

        jTextField3.setText(org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jTextField3.text")); // NOI18N
        jTextField3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jTextField3KeyTyped(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(jButton1, org.openide.util.NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jLabel3)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jTextField1)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 224, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jTextField2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 35, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jButton1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(jTextField2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(jTextField3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(0, 190, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFrame mainFrame = (JFrame) WindowManager.getDefault().getMainWindow();

        FileDialog chooser = new FileDialog(mainFrame,
                NbBundle.getMessage(MeasurementPanel.class, "MeasurementPanel.fileDialog.title"),
                LOAD);

        //chooser.setMultipleMode(false); Java 1.7 only
        chooser.setVisible(true);
        String filename = chooser.getFile();
        if (filename != null) {
            setFile(new File(chooser.getDirectory(), filename));
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField1KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1KeyTyped

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        String fileName = jTextField1.getText();
        if (fileName.isEmpty())
        {
            setFile(null);
        }
        else{
            setFile(new File(fileName));
        }
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jTextField2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField2KeyTyped
        cs.fireChange();
    }//GEN-LAST:event_jTextField2KeyTyped

    private void jTextField3KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextField3KeyTyped
        cs.fireChange();
    }//GEN-LAST:event_jTextField3KeyTyped

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
