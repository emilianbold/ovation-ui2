package us.physion.ovation.ui.browser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.ToolTipManager;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;
import org.openide.windows.TopComponent;
import us.physion.ovation.domain.Project;
import us.physion.ovation.ui.interfaces.ConnectionListener;
import us.physion.ovation.ui.interfaces.ConnectionProvider;
import us.physion.ovation.ui.interfaces.EntityColors;
import us.physion.ovation.ui.interfaces.TreeViewProvider;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//us.physion.ovation.ui.browser//ProjectBrowser//EN",
        autostore = false)
@TopComponent.Description(preferredID = "ProjectBrowserTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "us.physion.ovation.ui.browser.ProjectBrowserTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_BrowserAction",
        preferredID = "ProjectBrowserTopComponent")
@Messages({
    "CTL_BrowserAction=Projects Navigator",
    "CTL_ProjectBrowserTopComponent=Projects",
    "HINT_BrowserTopComponent=Browse Projects in your Ovation database",
    "HINT_ProjectBrowser_NewProject_Button_FirstRun=Click here to add a new Project",
    "HINT_ProjectBrowser_NewProject_Button=Add a new Project"
})
public final class ProjectBrowserTopComponent extends TopComponent implements ExplorerManager.Provider, TreeViewProvider {

    private Lookup lookup;
    private final ExplorerManager explorerManager = new ExplorerManager();
    private final BeanTreeView view;
    private final FilteredTreeViewPanel treeViewPanel;

    private static final String SHOW_FIRST_RUN_TIP = "show_first_run_tip";

    public ProjectBrowserTopComponent() {
        final Preferences prefs = NbPreferences.forModule(ProjectBrowserTopComponent.class);

        setLayout(new BorderLayout());
        treeViewPanel = new FilteredTreeViewPanel(
                "us.physion.ovation.ui.browser.insertion.NewProjectAction",
                Bundle.HINT_ProjectBrowser_NewProject_Button());

        view = treeViewPanel.getTreeView();
        view.setBackground(Color.white);
        add(treeViewPanel, BorderLayout.CENTER);

        setName(Bundle.CTL_ProjectBrowserTopComponent());
        setToolTipText(Bundle.HINT_BrowserTopComponent());

        lookup = ExplorerUtils.createLookup(explorerManager, getActionMap());
        associateLookup(lookup);
        BrowserUtilities.initBrowser(explorerManager, NavigatorType.PROJECT);

        ActionMap actionMap = this.getActionMap();
        actionMap.put("copy-to-clipboard", (Action) new BrowserCopyAction());

        String html = "<html><font color=\"" + EntityColors.getEntityColorHex(Project.class) + "\">" + Bundle.CTL_ProjectBrowserTopComponent() + "</font></html>";
        setHtmlDisplayName(html);

        ConnectionProvider cp = Lookup.getDefault().lookup(ConnectionProvider.class);
        cp.addConnectionListener(new ConnectionListener(new Runnable() {

            @Override
            public void run() {
                if (prefs.getBoolean(SHOW_FIRST_RUN_TIP, true)) {
                    final JButton btn = treeViewPanel.getNewRootEntityButton();

                    final Rectangle buttonBounds = btn.getBounds();

                    ToolTipManager.sharedInstance().mouseMoved(
                            new MouseEvent(btn, 0, 0, 0,
                                    buttonBounds.width,
                                    0, // X-Y of the mouse for the tool tip
                                    0, true));

                    prefs.putBoolean(SHOW_FIRST_RUN_TIP, false);

                }
            }
        }));
    }

    private String colorToHex(Color color) {
        return String.format("#%02x%02x%02x",
                color.getRed(),
                color.getGreen(),
                color.getBlue());
    }

    @Override
    public Action[] getActions() {
        return ActionUtils.appendToArray(new Action[]{new ResettableAction(this), null}, super.getActions());
    }

    @Override
    public Object getTreeView() {
        return view;
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void componentOpened() {
        //BrowserUtilities.createTreeComponent(em, true);
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
        return explorerManager;
    }
}
