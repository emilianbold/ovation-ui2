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

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.function.Supplier;
import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import org.jdesktop.swingx.JXHyperlink;
import org.openide.util.NbBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.physion.ovation.DataContext;
import us.physion.ovation.domain.Folder;
import us.physion.ovation.domain.FolderContainer;
import us.physion.ovation.domain.Protocol;
import us.physion.ovation.ui.browser.BrowserUtilities;
import us.physion.ovation.ui.interfaces.EntityColors;
import us.physion.ovation.ui.interfaces.IEntityNode;
import us.physion.ovation.ui.reveal.api.RevealNode;

/**
 *
 * @author barry
 */
@NbBundle.Messages({
    "NewFolderHyperlink.text=+New Folder"
})
abstract class AbstractContainerVisualizationPanel extends javax.swing.JLayeredPane {

    public static final String PROP_PROTOCOLS = "protocols";
    public static final String PROP_PROTOCOL = "protocol";

    final IEntityNode node;

    private final DataContext context;

    public AbstractContainerVisualizationPanel(IEntityNode entityNode) {
        node = entityNode;
        context = entityNode.getEntity().getDataContext();

        this.setOpaque(true);
    }

    protected void setEntityBorder(JComponent panel) {
        panel.setBorder(new LineBorder(EntityColors.getEntityColor(getNode().getEntity().getClass()), 2, true));
    }

    Logger logger = LoggerFactory.getLogger(AbstractContainerVisualizationPanel.class);

    public List<Protocol> getProtocols() {
        List<Protocol> result = Lists.newLinkedList(context.getProtocols());
        result = Lists.newLinkedList(Iterables.filter(result, p -> {
            try {
                p.getName();
                return false;
            } catch (NullPointerException npe) {
                return true;
            }
        }));
        
        return result;
    }

    protected DataContext getContext() {
        return context;
    }

    protected IEntityNode getNode() {
        return node;
    }

    protected Protocol addProtocol()
    {
        List<Protocol> current = getProtocols();
        Protocol p = getContext().insertProtocol(Bundle.CTL_NewProtocolName(), "");
        firePropertyChange("protocols", current, getProtocols());
        return p;
    }
    
    protected Folder addFolder(FolderContainer parent, boolean reveal) {
        final Folder folder = parent.addFolder(Bundle.Default_Folder_Label());
        if (reveal) {
            node.refresh();
            RevealNode.forEntity(getRevealTopComponentId(), folder);
        }
        return folder;
    }
    
    protected String getRevealTopComponentId() {
        return BrowserUtilities.PROJECT_BROWSER_ID;
    }
    
    protected JPanel createActionBar(Supplier<FolderContainer> folderContainer, Action... actions) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.X_AXIS));
        if (folderContainer != null) {
            JXHyperlink newFolderHyperlink = new org.jdesktop.swingx.JXHyperlink();
            org.openide.awt.Mnemonics.setLocalizedText(newFolderHyperlink, Bundle.NewFolderHyperlink_text());
            p.add(newFolderHyperlink);
            p.add(Box.createHorizontalStrut(10));

            newFolderHyperlink.addActionListener(e -> addFolder(folderContainer.get(), true));
        }
        for(Action action : actions) {
            JXHyperlink link = new JXHyperlink(action);
            p.add(link);
            p.add(Box.createHorizontalStrut(10));
        }
        return p;
    }
    
    protected JPanel createActionBar() {
        return createActionBar(null);
    }

    static class ProtocolCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list,
                final Object value,
                int index,
                boolean isSelected,
                boolean cellHasFocus) {

            return super.getListCellRendererComponent(list, new Object() {

                @Override
                public String toString() {
                    if (value instanceof Protocol) {
                        Protocol p = (Protocol) value;
                        try {
                            return p.getName(); //TODO getVersion()
                        } catch (NullPointerException npe) {
                            new Exception("NPE on protocol " + p.getUuid()).printStackTrace(System.out);
                            return "NPE " + p.getUuid();
                        }
                    }
                    return value.toString();
                }

            }, index, isSelected, cellHasFocus);
        }
    }

    public List<String> getAvailableZoneIDs() {
        return Lists.newArrayList(DatePickers.getTimeZoneIDs());
    }

    public static class WindowMoveAdapter extends MouseAdapter {

        private boolean dragging = false;
        private int prevX = -1;
        private int prevY = -1;

        public WindowMoveAdapter() {
            super();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if (SwingUtilities.isLeftMouseButton(e)) {
                dragging = true;
            }
            prevX = e.getXOnScreen();
            prevY = e.getYOnScreen();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (prevX != -1 && prevY != -1 && dragging) {
                Window w = SwingUtilities.getWindowAncestor(e.getComponent());
                if (w != null && w.isShowing()) {
                    Rectangle rect = w.getBounds();
                    w.setBounds(rect.x + (e.getXOnScreen() - prevX),
                            rect.y + (e.getYOnScreen() - prevY), rect.width, rect.height);
                }
            }
            prevX = e.getXOnScreen();
            prevY = e.getYOnScreen();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            dragging = false;
        }
    }
}
