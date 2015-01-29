/*
 * Copyright (C) 2014 Physion LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package us.physion.ovation.ui.editor;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import org.openide.util.NbBundle.Messages;
import us.physion.ovation.domain.Source;
import us.physion.ovation.ui.browser.BrowserUtilities;
import us.physion.ovation.ui.interfaces.IEntityNode;
import us.physion.ovation.ui.reveal.api.RevealNode;

/**
 *
 * @author barry
 */
@Messages({
    "Source_Default_Label=New Source",
    "Source_Default_Identifier="
})
public class SourceVisualizationPanel extends AbstractContainerVisualizationPanel {

    final Source source;
    final IEntityNode node;
    /**
     * Creates new form SourceVisualizationPanel
     */
    public SourceVisualizationPanel(IEntityNode src) {
        super(src);

        source = src.getEntity(Source.class);
        node = src;

        initComponents();

        setEntityBorder(this);

        addChildButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Source child = getSource().insertSource(Bundle.Source_Default_Label(), Bundle.Source_Default_Identifier());

                getNode().refresh();

                RevealNode.forEntity(BrowserUtilities.SOURCE_BROWSER_ID, child);
            }
        });

        labelTextField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                labelTextField.transferFocus();
            }
        });

        labelTextField.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                //pass
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (getSource().getIdentifier() == null || getSource().getIdentifier().equals("")) {
                    List<Source> parents = Lists.newArrayList(getSource().getParentSources());
                    if (!parents.isEmpty()) {
                        List<String> parentIds = Lists.transform(parents, new Function<Source, String>() {

                            @Override
                            public String apply(Source input) {
                                return input == null ? null : input.getIdentifier();
                            }
                        });

                        String parentIdPrefix = Joiner.on("-").join(parentIds);

                        getSource().setIdentifier(parentIdPrefix + "-" + getSource().getLabel());
                    }
                }
            }
        });

        node.getEntityWrapper().addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                identifierTextField.setText(getSource().getIdentifier());
                labelTextField.setText(getSource().getLabel());
            }
        });
    }

    public Source getSource() {
        return source;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        titleLabel = new javax.swing.JLabel();
        labelTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        identifierTextField = new javax.swing.JTextField();
        addChildButton = new javax.swing.JButton();

        setBackground(javax.swing.UIManager.getDefaults().getColor("EditorPane.background"));

        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(titleLabel, org.openide.util.NbBundle.getMessage(SourceVisualizationPanel.class, "SourceVisualizationPanel.titleLabel.text")); // NOI18N

        labelTextField.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        labelTextField.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${source.label}"), labelTextField, org.jdesktop.beansbinding.BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST"));
        bindingGroup.addBinding(binding);

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(SourceVisualizationPanel.class, "SourceVisualizationPanel.jLabel1.text")); // NOI18N

        identifierTextField.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${source.identifier}"), identifierTextField, org.jdesktop.beansbinding.BeanProperty.create("text_ON_ACTION_OR_FOCUS_LOST"));
        bindingGroup.addBinding(binding);

        org.openide.awt.Mnemonics.setLocalizedText(addChildButton, org.openide.util.NbBundle.getMessage(SourceVisualizationPanel.class, "SourceVisualizationPanel.addChildButton.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titleLabel)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addChildButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(identifierTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE)
                    .addComponent(labelTextField))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(labelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(identifierTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addChildButton)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        bindingGroup.bind();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addChildButton;
    private javax.swing.JTextField identifierTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JTextField labelTextField;
    private javax.swing.JLabel titleLabel;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables
}
