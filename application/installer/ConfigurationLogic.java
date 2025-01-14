/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 1997-2011 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * Contributor(s):
 *
 * The Original Software is NetBeans. The Initial Developer of the Original
 * Software is Sun Microsystems, Inc. Portions Copyright 1997-2011 Sun
 * Microsystems, Inc. All Rights Reserved.
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 */

package org.mycompany;

import java.util.List;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import org.mycompany.wizard.panels.HelloWorldPanel;
import org.mycompany.installer.utils.applications.NetBeansRCPUtils;
import org.netbeans.installer.product.components.ProductConfigurationLogic;
import org.netbeans.installer.product.components.Product;
import org.netbeans.installer.utils.FileUtils;
import org.netbeans.installer.utils.helper.RemovalMode;
import org.netbeans.installer.utils.exceptions.InitializationException;
import org.netbeans.installer.utils.exceptions.InstallationException;
import org.netbeans.installer.utils.exceptions.UninstallationException;
import org.netbeans.installer.utils.progress.Progress;
import org.netbeans.installer.utils.system.shortcut.FileShortcut;
import org.netbeans.installer.utils.system.shortcut.LocationType;
import org.netbeans.installer.utils.system.shortcut.Shortcut;
import org.netbeans.installer.utils.SystemUtils;
import org.netbeans.installer.utils.LogManager;
import org.netbeans.installer.utils.ResourceUtils;
import org.netbeans.installer.utils.StreamUtils;
import org.netbeans.installer.utils.StringUtils;
import org.netbeans.installer.utils.exceptions.NativeException;
import org.netbeans.installer.wizard.Wizard;
import org.netbeans.installer.wizard.components.WizardComponent;

import org.netbeans.installer.utils.system.launchers.LauncherResource;

public class ConfigurationLogic extends ProductConfigurationLogic {

    private List<WizardComponent> wizardComponents;

    // constructor //////////////////////////////////////////////////////////////////
    public ConfigurationLogic() throws InitializationException {
        wizardComponents = Wizard.loadWizardComponents(
                WIZARD_COMPONENTS_URI,
                getClass().getClassLoader());
    }

    public List<WizardComponent> getWizardComponents() {
        return wizardComponents;
    }

    @Override
    public boolean allowModifyMode() {
        return false;
    }
    
    @Override
    public Map<String, Object> getAdditionalSystemIntegrationInfo() {
        Map<String, Object> info = super.getAdditionalSystemIntegrationInfo();
        if (SystemUtils.isWindows()) {
            info.put("DisplayVersion", getString("ovation.version"));
            info.put("Publisher", "Physion LLC");
            info.put("URLInfoAbout", "https://ovation.io/");
            info.put("HelpLink", "http://docs.ovation.io/");
//            info.put("URLUpdateInfo",  "");
//            info.put("Readme",  readme absolute path);
        }
        return info;
    }

    @Override
    public void install(Progress progress) throws InstallationException {
        final Product product = getProduct();
        final File installLocation = product.getInstallationLocation();
        //final FilesList filesList = product.getInstalledFiles();

        if (SystemUtils.isMacOS()) {
            File f = new File(installLocation, ICON_MACOSX);
            if(!f.exists()) {
                try {
                FileUtils.writeFile(f,
                        ResourceUtils.getResource(ICON_MACOSX_RESOURCE,
                        getClass().getClassLoader()));
                getProduct().getInstalledFiles().add(f);
                } catch (IOException e) {
                    LogManager.log(
                                "... cannot handle icns icon " + f, e); // NOI18N
                }
            }
        }



        if (Boolean.parseBoolean(getProperty(HelloWorldPanel.CREATE_DESKTOP_SHORTCUT_PROPERTY))) {
            LogManager.logIndent(
                    "creating the desktop shortcut for the application"); // NOI18N
            if (!SystemUtils.isMacOS()) {
                try {
                    progress.setDetail(getString("CL.install.desktop")); // NOI18N

                    if (SystemUtils.isCurrentUserAdmin()) {
                        LogManager.log(
                                "... current user is an administrator " + // NOI18N
                                "-- creating the shortcut for all users"); // NOI18N

                        SystemUtils.createShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.ALL_USERS_DESKTOP);

                        product.setProperty(
                                DESKTOP_SHORTCUT_LOCATION_PROPERTY,
                                ALL_USERS_PROPERTY_VALUE);
                    } else {
                        LogManager.log(
                                "... current user is an ordinary user " + // NOI18N
                                "-- creating the shortcut for the current " + // NOI18N
                                "user only"); // NOI18N

                        SystemUtils.createShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.CURRENT_USER_DESKTOP);

                        getProduct().setProperty(
                                DESKTOP_SHORTCUT_LOCATION_PROPERTY,
                                CURRENT_USER_PROPERTY_VALUE);
                    }
                } catch (NativeException e) {
                    LogManager.unindent();

                    LogManager.log(
                            getString("CL.install.error.desktop"), // NOI18N
                            e);
                }
            } else {
                LogManager.log(
                        "... skipping this step as we're on Mac OS"); // NOI18N
            }
        }
        LogManager.logUnindent(
                "... done"); // NOI18N

        /////////////////////////////////////////////
        // create start menu shortcut
        if (Boolean.parseBoolean(getProperty(HelloWorldPanel.CREATE_START_MENU_SHORTCUT_PROPERTY))) {
            LogManager.logIndent(
                    "creating the start menu shortcut for the application"); // NOI18N
            try {
                progress.setDetail(getString("CL.install.start.menu")); // NOI18N

                if (SystemUtils.isCurrentUserAdmin()) {
                    LogManager.log(
                            "... current user is an administrator " + // NOI18N
                            "-- creating the shortcut for all users"); // NOI18N

                    SystemUtils.createShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.ALL_USERS_START_MENU);

                    getProduct().setProperty(
                            START_MENU_SHORTCUT_LOCATION_PROPERTY,
                            ALL_USERS_PROPERTY_VALUE);
                } else {
                    LogManager.log(
                            "... current user is an ordinary user " + // NOI18N
                            "-- creating the shortcut for the current " + // NOI18N
                            "user only"); // NOI18N

                    SystemUtils.createShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.CURRENT_USER_START_MENU);

                    getProduct().setProperty(
                            START_MENU_SHORTCUT_LOCATION_PROPERTY,
                            CURRENT_USER_PROPERTY_VALUE);
                }
            } catch (NativeException e) {
                LogManager.log(
                        getString("CL.install.error.start.menu"), // NOI18N
                        e);
            }
            LogManager.logUnindent(
                    "... done"); // NOI18N
        }

        File javaHome = new File(System.getProperty("java.home")); //NOI18N
        File target = new File(installLocation, "jre"); //NOI18N
        try {
            FileUtils.copyFile(javaHome, target, true);
        } catch (IOException e) {
            throw new InstallationException("Cannot copy JRE", e);
        }

        //make exe
        File binDir = new File(target, "bin");
        for (File file : binDir.listFiles()) {
            try {
                file.setExecutable(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //add JRE to conf file
        File etc = new File(installLocation, "etc"); //NOI18N
        File[] etcFiles = etc.listFiles();

        if (etcFiles == null) {
            throw new InstallationException("Cannot find configuration file to add JRE");
        }

        boolean found = false;
        for (File conf : etcFiles) {
            if (!conf.getName().endsWith(".conf")) {
                continue;
            }
            try {
                FileUtils.appendFile(conf, "\njdkhome=\"jre\"\n");
                found = true;
            } catch (IOException e) {
                throw new InstallationException("Cannot write to configuration file " + conf, e);
            }
        }
        if (!found) {
            throw new InstallationException("Cannot find configuration file to add JRE");
        }

        //uninstaller
        SystemUtils.getNativeUtils().addUninstallerJVM(new LauncherResource(false, target));
    }

    @Override
    public void uninstall(Progress progress) throws UninstallationException {
        final Product product = getProduct();
        final File installLocation = product.getInstallationLocation();

        //NetBeansUtils.warnNetbeansRunning(installLocation);
        /////////////////////////////////////////////////////////////////////////////
        if (Boolean.parseBoolean(getProperty(HelloWorldPanel.CREATE_START_MENU_SHORTCUT_PROPERTY))) {
            try {
                progress.setDetail(getString("CL.uninstall.start.menu")); // NOI18N

                final String shortcutLocation =
                        getProduct().getProperty(START_MENU_SHORTCUT_LOCATION_PROPERTY);

                if ((shortcutLocation == null)
                        || shortcutLocation.equals(CURRENT_USER_PROPERTY_VALUE)) {
                    SystemUtils.removeShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.CURRENT_USER_START_MENU,
                            true);
                } else {
                    SystemUtils.removeShortcut(
                            getStartMenuShortcut(installLocation),
                            LocationType.ALL_USERS_START_MENU,
                            true);
                }
            } catch (NativeException e) {
                LogManager.log(
                        getString("CL.uninstall.error.start.menu"), // NOI18N
                        e);
            }
        }

        /////////////////////////////////////////////////////////////////////////////
        if (Boolean.parseBoolean(getProperty(HelloWorldPanel.CREATE_DESKTOP_SHORTCUT_PROPERTY))) {
            if (!SystemUtils.isMacOS()) {
                try {
                    progress.setDetail(getString("CL.uninstall.desktop")); // NOI18N

                    final String shortcutLocation = getProduct().getProperty(
                            DESKTOP_SHORTCUT_LOCATION_PROPERTY);

                    if ((shortcutLocation == null)
                            || shortcutLocation.equals(CURRENT_USER_PROPERTY_VALUE)) {
                        SystemUtils.removeShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.CURRENT_USER_DESKTOP,
                                false);
                    } else {
                        SystemUtils.removeShortcut(
                                getDesktopShortcut(installLocation),
                                LocationType.ALL_USERS_DESKTOP,
                                false);
                    }
                } catch (NativeException e) {
                    LogManager.log(
                            getString("CL.uninstall.error.desktop"), // NOI18N
                            e);
                }
            }
        }


        if (Boolean.getBoolean("remove.app.userdir")) {
            try {
                progress.setDetail(getString("CL.uninstall.remove.userdir")); // NOI18N
                LogManager.logIndent("Removing application`s userdir... ");
                File userDir = NetBeansRCPUtils.getApplicationUserDirFile(installLocation);
                LogManager.log("... application userdir location : " + userDir);
                if (FileUtils.exists(userDir) && FileUtils.canWrite(userDir)) {
                    FileUtils.deleteFile(userDir, true);
                    FileUtils.deleteEmptyParents(userDir);
                }
                LogManager.log("... application userdir totally removed");
            } catch (IOException e) {
                LogManager.log("Can`t remove application userdir", e);
            } finally {
                LogManager.unindent();
            }
        }

        /////////////////////////////////////////////////////////////////////////////
        //remove cluster/update files
        /*
        try {
        progress.setDetail(getString("CL.uninstall.update.files")); // NOI18N
        for(String cluster : CLUSTERS) {
        File updateDir = new File(installLocation, cluster + File.separator + "update");
        if ( updateDir.exists()) {
        FileUtils.deleteFile(updateDir, true);
        }
        }
        } catch (IOException e) {
        LogManager.log(
        getString("CL.uninstall.error.update.files"), // NOI18N
        e);
        }
         */
        /////////////////////////////////////////////////////////////////////////////
        File jre = new File(installLocation, "jre"); //NOI18N
        if (jre.exists()) {
            try {
                for (File file : FileUtils.listFiles(jre).toList()) {
                    FileUtils.deleteOnExit(file);
                }
                FileUtils.deleteOnExit(installLocation);
            } catch (IOException e) {
                //ignore
            }
        }
        progress.setPercentage(Progress.COMPLETE);
    }

    @Override
    public String getExecutable() {
        if (SystemUtils.isWindows()) {
            return EXECUTABLE_WINDOWS;
        } else {
            return EXECUTABLE_UNIX;
        }
    }

    @Override
    public String getIcon() {
        if (SystemUtils.isWindows()) {
            return ICON_WINDOWS;
        } else if (SystemUtils.isMacOS()) {
            return ICON_MACOSX;
        } else {
            return ICON_UNIX;
        }
    }

    public RemovalMode getRemovalMode() {
        return RemovalMode.LIST;
    }

    @Override
    public boolean registerInSystem() {
        return true;
    }

    @Override
    public boolean requireLegalArtifactSaving() {
        return false;
    }

    @Override
    public boolean requireDotAppForMacOs() {
        return true;
    }

    @Override
    public boolean wrapForMacOs() {
        return true;
    }



    private Shortcut getDesktopShortcut(final File directory) {
        return getShortcut(
                getStrings("CL.desktop.shortcut.name"), // NOI18N
                getStrings("CL.desktop.shortcut.description"), // NOI18N
                getString("CL.desktop.shortcut.path"), // NOI18N
                directory);
    }

    private Shortcut getStartMenuShortcut(final File directory) {
        if (SystemUtils.isMacOS()) {
            return getShortcut(
                    getStrings("CL.start.menu.shortcut.name.macosx"), // NOI18N
                    getStrings("CL.start.menu.shortcut.description"), // NOI18N
                    getString("CL.start.menu.shortcut.path"), // NOI18N
                    directory);
        } else {
            return getShortcut(
                    getStrings("CL.start.menu.shortcut.name"), // NOI18N
                    getStrings("CL.start.menu.shortcut.description"), // NOI18N
                    getString("CL.start.menu.shortcut.path"), // NOI18N
                    directory);
        }
    }

    private Shortcut getShortcut(
            final Map<Locale, String> names,
            final Map<Locale, String> descriptions,
            final String relativePath,
            final File location) {
        final File icon;
        final File executable;

        if (SystemUtils.isWindows()) {
            icon = new File(location, ICON_WINDOWS);
        } else if (SystemUtils.isMacOS()) {
            icon = new File(location, ICON_MACOSX);
        } else {
            icon = new File(location, ICON_UNIX);
            LogManager.log("... icon file: " + icon);
            if(!FileUtils.exists(icon)) {
                LogManager.log("... icon file does not exist: " + icon);
                InputStream is = null;
                is = ResourceUtils.getResource(ICON_UNIX_RESOURCE, this.getClass().getClassLoader());
                if(is!=null) {
                    FileOutputStream fos =null;
                    try {
                        fos = new FileOutputStream(icon);
                        StreamUtils.transferData(is, fos);
                        is.close();
                        fos.close();
                        getProduct().getInstalledFiles().add(icon);
                    } catch (IOException e) {
                        LogManager.log(e);
                    } finally {
                        if(fos!=null) {
                            try {
                                fos.close();
                            } catch (IOException e) {
                            }
                        }
                    }
                }
            }
        }

        if (SystemUtils.isWindows()) {
            executable = new File(location, EXECUTABLE_WINDOWS);
        } else {
            executable = new File(location, EXECUTABLE_UNIX);
        }
        final String name = names.get(new Locale(StringUtils.EMPTY_STRING));
        final FileShortcut shortcut = new FileShortcut(name, executable);
        shortcut.setNames(names);
        shortcut.setDescriptions(descriptions);
        shortcut.setCategories(SHORTCUT_CATEGORIES);
        shortcut.setFileName(SHORTCUT_FILENAME);
        shortcut.setIcon(icon);
        shortcut.setRelativePath(relativePath);
        shortcut.setWorkingDirectory(location);
        shortcut.setModifyPath(true);

        return shortcut;
    }
    public static final String SHORTCUT_FILENAME =
            ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".desktop"; // NOI18N
    public static final String[] SHORTCUT_CATEGORIES = new String[]{
        "Application"
    };
    public static final String BIN_SUBDIR =
            "bin/";
    public static final String EXECUTABLE_WINDOWS =
            BIN_SUBDIR
            + ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".exe"; // NOI18N
    public static final String EXECUTABLE_UNIX =
            BIN_SUBDIR
            + ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name"); // NOI18N
    public static final String ICON_WINDOWS =
            EXECUTABLE_WINDOWS;
    public static final String ICON_UNIX =
            ResourceUtils.getString(ConfigurationLogic.class,
            "CL.unix.icon.name"); // NOI18N
    public static final String ICON_UNIX_RESOURCE =
            ResourceUtils.getString(ConfigurationLogic.class,
            "CL.unix.icon.resource"); // NOI18N
    public static final String ICON_MACOSX =
            ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".icns"; // NOI18N
    public static final String ICON_MACOSX_RESOURCE =
            "org/mycompany/" + ResourceUtils.getString(ConfigurationLogic.class, "CL.app.name") + ".icns"; // NOI18N
    public static final String WIZARD_COMPONENTS_URI =
            "resource:" + // NOI18N
            "org/mycompany/wizard.xml"; // NOI18N
    private static final String DESKTOP_SHORTCUT_LOCATION_PROPERTY =
            "desktop.shortcut.location"; // NOI18N
    private static final String START_MENU_SHORTCUT_LOCATION_PROPERTY =
            "start.menu.shortcut.location"; // NOI18N
    private static final String ALL_USERS_PROPERTY_VALUE =
            "all.users"; // NOI18N
    private static final String CURRENT_USER_PROPERTY_VALUE =
            "current.user"; // NOI18N
}

