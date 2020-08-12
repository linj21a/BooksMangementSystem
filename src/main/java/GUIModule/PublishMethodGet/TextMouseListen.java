package GUIModule.PublishMethodGet;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 将文本按钮的方法抽取
 */
public class TextMouseListen extends MouseAdapter {
    private JTextField jTextField;
    public TextMouseListen(JTextField jTextField) {
        this.jTextField = jTextField;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        int c = e.getButton();
        if (c == MouseEvent.BUTTON1) {
            // 左键按下
            this.jTextField.setText("");
        }
    }
}
