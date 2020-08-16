package GUIModule.PublishMethodGet;

import javax.swing.*;
import java.awt.*;

public class JPanelOpaque extends JPanel {
    public JPanelOpaque() {
        this.setOpaque(false);//透明
        this.setVisible(true);//可见
    }

    public JPanelOpaque(LayoutManager layout) {
        super(layout);
        this.setOpaque(false);//透明
        this.setVisible(true);//可见
    }
}
