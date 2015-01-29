package us.physion.ovation.ui.editor;

import java.awt.Color;
import us.physion.ovation.ui.actions.OpenInNativeAppAction;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import us.physion.ovation.domain.Resource;

/**
 *
 * @author jackie
 */
public class DefaultDataPanel extends JPanel{

    Resource d;
    JLabel elementName;
    JLabel messageLabel;

    public DefaultDataPanel(Resource data) {
        d = data;
        this.setLayout(new GridBagLayout());
        this.setBackground(Color.white);
        GridBagConstraints c = new GridBagConstraints();
        elementName = new JLabel(data.getName());
        messageLabel = new JLabel("Cannot display data of type '" + data.getDataContentType() + "'");
        JButton openButton = new JButton(new OpenInNativeAppAction(d));

        c.insets = new Insets(10, 10, 10, 10);
        c.anchor = GridBagConstraints.NORTH;
        c.weighty = 0.0;
        c.gridy = 0;
        add(elementName, c);
        c.gridy = 1;
        add(messageLabel, c);
        c.gridy = 2;
        c.weighty = 1.0;
        add(openButton, c);
    }
}
