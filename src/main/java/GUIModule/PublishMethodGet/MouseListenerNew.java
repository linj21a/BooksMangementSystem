package GUIModule.PublishMethodGet;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 抽取公共方法变成一个子类
 */

public class MouseListenerNew extends MouseAdapter {
    private  JButton jButton;
    public MouseListenerNew(JButton jButton) {
        this.jButton = jButton;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {//进入这个按钮区域，变大一点点
        jButton.setSize(jButton.getWidth() + 4, jButton.getHeight() + 4);
    }

    @Override
    public void mouseExited(MouseEvent e) {//离开按钮区域，变回来
        jButton.setSize(jButton.getWidth() - 4, jButton.getHeight() - 4);
    }
}
