package us.physion.ovation.ui.browser;

import java.util.Comparator;
import us.physion.ovation.domain.AnalysisRecord;
import us.physion.ovation.domain.Folder;
import us.physion.ovation.domain.Measurement;
import us.physion.ovation.domain.OvationEntity;
import us.physion.ovation.domain.Project;
import us.physion.ovation.domain.Protocol;
import us.physion.ovation.domain.Resource;
import us.physion.ovation.domain.Revision;
import us.physion.ovation.domain.Source;
import us.physion.ovation.domain.User;
import us.physion.ovation.domain.mixin.TimelineElement;

/**
 * Comparator for Ovation entities. Compares start time for time line elements,
 * or display name for all others.
 *
 * @param <T> entity type T
 */
public class EntityComparator<T extends EntityWrapper> implements Comparator<T> {

    @Override
    public int compare(T o1, T o2) {
        //put the empty one last!
        if (o1 == EntityWrapper.EMPTY) {
            if (o2 == EntityWrapper.EMPTY) {
                return 0;
            } else {
                return 1;
            }
        } else if (o2 == EntityWrapper.EMPTY) {
            return -1;
        }

        final OvationEntity entity1 = o1.getEntity(true);
        final OvationEntity entity2 = o2.getEntity(true);

        if (entity1 instanceof Project && entity2 instanceof Project) {
            return ((Project) entity1).getName().toLowerCase().compareTo(((Project) entity2).getName().toLowerCase());
        }

        if (entity1 instanceof Source && entity2 instanceof Source) {
            return ((Source) entity1).getLabel().toLowerCase().compareTo(((Source) entity2).getLabel().toLowerCase());
        }

        if (entity1 instanceof Protocol && entity2 instanceof Protocol) {
            return ((Protocol) entity1).getName().toLowerCase().compareTo(((Protocol) entity2).getName().toLowerCase());
        }

        if (entity1 instanceof Folder && entity2 instanceof Folder) {
            return ((Folder) entity1).getLabel().compareTo(((Folder) entity2).getLabel());
        }

        if (entity1 instanceof Folder && !(entity2 instanceof Folder)) {
            return -1; //Folders compare before structural entities
        }

        if (entity2 instanceof Folder && !(entity1 instanceof Folder)) {
            return 1; //Folders compare before structural entities
        }

        if (entity1 instanceof TimelineElement && entity2 instanceof TimelineElement) {
            TimelineElement t1 = (TimelineElement) entity1;
            TimelineElement t2 = (TimelineElement) entity2;

            return t1.getStart().compareTo(t2.getStart());
        }

        if (entity1 instanceof User && !(entity2 instanceof User)) {
            return 1; //User entries compare after non-analysis records
        }

        if (entity1 instanceof AnalysisRecord && entity2 instanceof AnalysisRecord) {
            return ((AnalysisRecord) entity1).getName().compareTo(((AnalysisRecord) entity2).getName());
        }

        if (entity1 instanceof Measurement && entity2 instanceof Measurement) {
            Measurement m1 = (Measurement) entity1;
            Measurement m2 = (Measurement) entity2;

            return m1.getLabel().compareTo(m2.getLabel());
//            if (m1.getEpoch().equals(m2.getEpoch())) {
//                return m1.getName().toLowerCase().compareTo(m2.getName().toLowerCase());
//            }
//
//            return ((Measurement) entity1).getEpoch().getStart().compareTo(((Measurement) entity2).getEpoch().getStart());
        }
        
        if(entity1 instanceof Resource && entity2 instanceof Resource) {
            Resource r1 = (Resource)entity1;
            Resource r2 = (Resource)entity2;
            
            return r1.getLabel().compareTo(r2.getLabel());
        }

        if (entity1 instanceof Revision && entity2 instanceof Revision) {
            Revision r1 = (Revision) entity1;
            Revision r2 = (Revision) entity2;

            if (r1.getVersion() != null && r2.getVersion() != null) {
                return r1.getVersion().compareTo(r2.getVersion());
            }
        }

        return entity1.getURI().compareTo(entity2.getURI());
    }

}
