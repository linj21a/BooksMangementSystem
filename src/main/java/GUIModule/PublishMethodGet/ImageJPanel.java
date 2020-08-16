package GUIModule.PublishMethodGet;

import javax.swing.*;
import java.awt.*;

/**
 * 通过设置label和透明度来达到设置图片的目的，不实在。
 */
public class ImageJPanel extends JPanel {//鸡肋
    public ImageJPanel(ImageIcon imageIcon, JFrame jFrame) {
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(jFrame.getWidth(),
                jFrame.getHeight(), Image.SCALE_AREA_AVERAGING));//设置图片大小跟随面板大小
        drawLayout(imageIcon);
    }

    private void drawLayout(ImageIcon imageIcon) {
        this.setLayout(new BorderLayout());
        JLabel background = new JLabel(imageIcon);
        background.setBounds(this.getX(), this.getY(), imageIcon.getIconWidth(), imageIcon.getIconHeight());
        this.add(background, BorderLayout.CENTER);
        //第二张图片图书管，需要设置对底层透明
        this.setOpaque(false);//设置对底层组件透明
        this.setVisible(true);
    }


}
