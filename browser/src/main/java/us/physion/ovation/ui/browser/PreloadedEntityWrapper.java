package us.physion.ovation.ui.browser;

import com.google.common.collect.Lists;
import java.util.List;
import us.physion.ovation.domain.OvationEntity;
import us.physion.ovation.exceptions.OvationException;

/**
 *
 * @author jackie
 */
public class PreloadedEntityWrapper extends EntityWrapper{

    public PreloadedEntityWrapper(OvationEntity e)
    {
        super(e);
        children = Lists.newArrayList();
    }
    private final List<EntityWrapper> children;

    public void addChildren(List<OvationEntity> entityChain, OvationEntity last)
    {
        PreloadedEntityWrapper current = this;
        for (OvationEntity entity : entityChain)
        {
            EntityWrapper child = current.findChild(entity);
            if (child == null)
            {
                child = new PreloadedEntityWrapper(entity);
                current.addChild(child);
            }
            if (child instanceof PreloadedEntityWrapper)
            {
                current = (PreloadedEntityWrapper)child;
            }else{
                throw new OvationException("Cannot insert entity '" +  last.getURI() + "' into '" + current.getURI());
            }
        }
        current.addChild(new EntityWrapper(last));//add the last child as a non-preloaded entity wrapper
    }

    public void addChild(EntityWrapper ew)
    {
        children.add(ew);
    }

    private EntityWrapper findChild(OvationEntity entity) {
        for (EntityWrapper child : children)
        {
            if (child.getURI().equals(entity.getURI()))
            {
                return child;
            }
        }
        return null;
    }

    public List<EntityWrapper> getChildren()
    {
        return children;
    }
}
