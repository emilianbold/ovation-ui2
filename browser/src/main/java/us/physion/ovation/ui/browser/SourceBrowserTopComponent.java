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
package us.physion.ovation.ui.browser;

import java.awt.BorderLayout;
import javax.swing.Action;
import javax.swing.ActionMap;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import us.physion.ovation.domain.Source;
import us.physion.ovation.ui.interfaces.EntityColors;
import us.physion.ovation.ui.interfaces.TreeViewProvider;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physion.ovation.ui.browser//SourceBrowser//EN",
        autostore = false)
@TopComponent.Description(preferredID = "SourceBrowserTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "us.physion.ovation.ui.browser.SourceBrowserTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_SourceBrowserAction",
        preferredID = "SourceBrowserTopComponent")
@Messages({
    "CTL_SourceBrowserAction=Sources Navigator",
    "CTL_SourceBrowserTopComponent=Sources",
    "HINT_SourceBrowserTopComponent=Browse Sources in your Ovation database",
    "HINT_SourceBrowser_NewSource_Button=Add a new Source"
})
public final class SourceBrowserTopComponent extends TopComponent implements ExplorerManager.Provider, TreeViewProvider {

    private final ExplorerManager em = new ExplorerManager();
    private final BeanTreeView view;

    public SourceBrowserTopComponent() {
        setLayout(new BorderLayout());
        FilteredTreeViewPanel panel = new FilteredTreeViewPanel(
                "us.physion.ovation.ui.browser.insertion.NewSourceAction",
                Bundle.HINT_SourceBrowser_NewSource_Button());
        view = panel.getTreeView();
        add(panel, BorderLayout.CENTER);

        setName(Bundle.CTL_SourceBrowserTopComponent());
        setToolTipText(Bundle.HINT_SourceBrowserTopComponent());

        associateLookup(ExplorerUtils.createLookup(em, getActionMap()));

        BrowserUtilities.initBrowser(em, NavigatorType.SOURCE);

        ActionMap actionMap = this.getActionMap();
        actionMap.put("copy-to-clipboard", (Action) new BrowserCopyAction());

        String html = "<html><font color=\"" + EntityColors.getEntityColorHex(Source.class) + "\">" + Bundle.CTL_SourceBrowserTopComponent() + "</font></html>";
        setHtmlDisplayName(html);
    }

    @Override
    public Object getTreeView() {
        return view;
    }

    @Override
    public Action[] getActions() {
        return ActionUtils.appendToArray(new Action[]{new ResettableAction(this), null}, super.getActions());
    }

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
    }

    void readProperties(java.util.Properties p) {
        //String version = p.getProperty("version");
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
}
