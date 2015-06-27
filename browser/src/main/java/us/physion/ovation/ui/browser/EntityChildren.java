package us.physion.ovation.ui.browser;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;
import us.physion.ovation.ui.interfaces.EventQueueUtilities;

@Messages({
    "# {0} - parent display name",
    "Loading_Entity_Children=Loading data for {0}",
    "Loading_Epochs=Loading epochs",
    "Loading_Epochs_Done=Done loading epochs"
})
public class EntityChildren extends Children.Keys<EntityWrapper> {

    public EntityChildren(List<EntityWrapper> children) {
        updateWithKeys(children == null ? Lists.<EntityWrapper>newArrayList() : children);
    }

    @Override
    protected Node[] createNodes(final EntityWrapper key) {
        return new Node[]{
            EntityWrapperUtilities.createNode(key,
            new EntityChildrenChildFactory(key)
            )};
    }

    protected final ListenableFuture<Void> updateWithKeys(final List<EntityWrapper> list) {
        return EventQueueUtilities.runOnEDT(() -> {
            setKeys(list);
        });
    }
}
