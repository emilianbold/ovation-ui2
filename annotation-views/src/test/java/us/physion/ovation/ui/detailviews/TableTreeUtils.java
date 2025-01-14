/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package us.physion.ovation.ui.detailviews;

import java.util.HashSet;
import java.util.Set;
import us.physion.ovation.ui.TableTreeKey;

/**
 *
 * @author huecotanks
 */
public class TableTreeUtils {
    static boolean setsEqual(Set s1, Set s2) {
        if (s1.size() != s2.size())
            return false;
        for (Object t1 : s1)
        {
            for (Object t2 : s2)
            {
                if (t1.equals(t2))
                {
                    s2.remove(t2);
                    break;
                }
            }    
        }
        return s2.isEmpty();
    }
    
    public static Set<Tuple> getTuplesByKey(String key, Set<Tuple> props)
    {
        Set<Tuple> result = new HashSet<Tuple>();
        for (Tuple p : props)
        {
            if (p.getKey().equals(key))
            {
                result.add(p);
            }
        }
        return result;
    }

    public static Set<Tuple> getTuples(TableTreeKey k) {
        Set<Tuple> properties = new HashSet<Tuple>();
        if (k instanceof UserPropertySet)
        {
            Object[][] data = ((UserPropertySet) k).getData();
            for (int i = 0; i < data.length; ++i) {
                properties.add(new Tuple((String) data[i][0], data[i][1]));
            }
        } else if (k instanceof ParameterSet) {
            Object[][] data = ((ParameterSet) k).getData();
            for (int i = 0; i < data.length; ++i) {
                properties.add(new Tuple((String) data[i][0], data[i][1]));
            }
        }
        return properties;
    }
}
