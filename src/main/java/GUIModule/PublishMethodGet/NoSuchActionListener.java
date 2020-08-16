package GUIModule.PublishMethodGet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NoSuchActionListener implements ActionListener {
    private Component component;
    public NoSuchActionListener(Component component) {
        this.component = component;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(component,"还没上线该功能，请期待！","提示",JOptionPane.PLAIN_MESSAGE);
    }
}
