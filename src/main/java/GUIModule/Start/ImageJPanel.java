package GUIModule.Start;

import javax.swing.*;
import java.awt.*;

public class ImageJPanel extends JPanel {
    public ImageJPanel(ImageIcon imageIcon, JFrame jFrame) {
        imageIcon.setImage(imageIcon.getImage().getScaledInstance(jFrame.getWidth(),
                jFrame.getHeight(), Image.SCALE_AREA_AVERAGING));//设置图片大小跟随面板大小
        drawLayout(imageIcon);
    }

    /* public ImageJPanel(ImageIcon icon,JButton jButton){
         drawLayout(icon,jButton);
     }*/
/*    private  void drawLayout(ImageIcon imageIcon,JButton jButton){
        this.setLayout(new BorderLayout());
        JLabel background = new JLabel(imageIcon);
        background.setBounds(this.getX(),this.getY(),imageIcon.getIconWidth(),imageIcon.getIconHeight());
        this.add(background,BorderLayout.CENTER);
      *//*  if(jButton!=null){
            JPanel jPanel = new JPanel(new BorderLayout());
            jPanel.setOpaque(false);
           // jPanel.add(jButton);
            //background.add(jButton);
            this.add(jPanel,BorderLayout.CENTER);
            jButton.setOpaque(true);

           // background.add(jButton);*//*

        }else{//第二张图片图书管，需要设置对底层透明
            this.setOpaque(false);//设置对底层组件透明
        }

        this.setVisible(true);
    }*/
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
