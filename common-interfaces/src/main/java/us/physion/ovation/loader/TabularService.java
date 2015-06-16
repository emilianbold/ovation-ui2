/*
 * Copyright (C) 2015 Physion LLC
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
package us.physion.ovation.loader;

import java.io.File;
import java.io.IOException;
import org.openide.util.Lookup;

public abstract class TabularService {

    public static String[][] load(File f) {
        for (TabularService service : Lookup.getDefault().lookupAll(TabularService.class)) {
            try {
                String[][] data = service.read(f);
                if (data != null) {
                    return data;
                }
            } catch (IOException ex) {
                //ignore
            }
        }

        return null;
    }

    public abstract String[][] read(File f) throws IOException;

}
