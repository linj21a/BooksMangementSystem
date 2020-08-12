package GUIModule.Start;

import GUIModule.Factory.GUIFactory;
import GUIModule.PublishMethodGet.Constant_Size;
import GUIModule.PublishMethodGet.MouseListenerNew;
import GUIModule.Reader.LogInGUi;
import GUIModule.Register.UserRegister;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 图书管理系统的启动界面。
 */
public class StartGUI {
    JButton jButton_Admin;
    JButton jButton_Reader;
    JButton jButton_Books;
    JButton jButton_register;
    private static JFrame jFrame0;

    public static void main(String[] args) {
        new StartGUI();
    }

    public StartGUI() {
        Init();
    }

    private void Init() {
        if (jFrame0 == null)
            synchronized (StartGUI.class) {
                if (jFrame0 == null) {
                    //主窗体
                    ImageIcon backImage = new ImageIcon("src/main/resources/book.jpg");

                    jFrame0 = new JFrame();
                    jFrame0.setTitle("银河图书管理系统");//默认边缘布局
                    jFrame0.setBounds(Constant_Size.x, Constant_Size.y, Constant_Size.Width, Constant_Size.Height);
                    jFrame0.setVisible(true);


                    //设置窗体的背景图片
                    jFrame0.setIconImage(backImage.getImage());
                    //----------------------------
                    //布局格式化
                    //主面板
                    JPanel jPanel = new JPanel();
                    jPanel.setLayout(new BorderLayout());

                    JLabel jLabel0 = new JLabel("银河图书管理系统欢迎你！");
                    jLabel0.setFont(new java.awt.Font("", Font.BOLD, 26));
                    jLabel0.setForeground(Color.BLUE);
                    //面板2号，存放label
                    JPanel jPanel2 = new JPanel();
                    jPanel2.add(jLabel0, BorderLayout.NORTH);
                    //面板1号，存放按钮
                    JPanel jPanel1 = new JPanel();//默认流式布局
                    jPanel1.setOpaque(false);
                    jPanel1.setVisible(true);
                    setButton(jPanel1);

                    //面板3，注册
                    JLabel l4 = new JLabel("用户注册");
                    l4.setForeground(Color.BLUE);
                    jButton_register = new JButton();
                    jButton_register.add(l4);
                    jButton_register.setVisible(true);
                    jPanel1.add(jButton_register);
                 /*   ImageIcon jImage = new ImageIcon("src/main/resources/dog.jpg");
                    jImage.setImage(jImage.getImage().getScaledInstance(jFrame0.getWidth(), jFrame0.getHeight(),
                            Image.SCALE_AREA_AVERAGING));*/
                  // JPanel jPanel3 = new ImageJPanel(jImage, jButton_register);
                    //JPanel jPanel3 = new JPanel();
                   // jPanel3.add(jButton_register);
                  //  jPanel3.setVisible(true);

                    //主面板存放面板1、2
                    jPanel.add(jPanel2, BorderLayout.NORTH);
                    jPanel.add(jPanel1, BorderLayout.CENTER);
                  //  jPanel.add(jPanel3, BorderLayout.SOUTH);
                    //将主面板添加到窗体
                    jFrame0.add(jPanel, BorderLayout.NORTH);

                    //使用自定义的子类，可以添加背景图片的子类。
                    JPanel imageJPanel = new ImageJPanel(backImage, jFrame0);
                    jFrame0.add(imageJPanel, BorderLayout.SOUTH);

                    //--------------------------------


                    jFrame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    jFrame0.setVisible(true);
                    buttonListen();
                }else
                    jFrame0.setVisible(true);

            }
        else
            jFrame0.setVisible(true);


    }

    private void buttonListen() {
        jButton_register.addMouseListener(new MouseListenerNew(jButton_register) {//注册按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                    UserRegister userRegister = GUIFactory.getUserRegister(jFrame0);
                    userRegister.jFrame1_register.setVisible(true);
            }
        });
        jButton_Books.addMouseListener(new MouseListenerNew(jButton_Books));//查看馆藏资源
        jButton_Admin.addMouseListener(new MouseListenerNew(jButton_Admin));//管理员登陆
        jButton_Reader.addMouseListener(new MouseListenerNew(jButton_Reader) {//读者登陆按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                jFrame0.setVisible(false);
                new LogInGUi(jFrame0);
            }
        });
    }

    private void setButton(JPanel jPanel) {
        jButton_Admin = new JButton();
        JLabel l1 = new JLabel("管理员登陆");
        l1.setForeground(Color.BLUE);
        jButton_Admin.add(l1);
        jButton_Admin.setVisible(true);
        jButton_Reader = new JButton();
        JLabel l2 = new JLabel("读者登陆");
        l2.setForeground(Color.BLUE);
        jButton_Reader.add(l2);
        jButton_Reader.setVisible(true);
        jButton_Books = new JButton();
        JLabel l3 = new JLabel("馆藏图书资源");
        l3.setForeground(Color.BLUE);
        jButton_Books.add(l3);
        jButton_Books.setVisible(true);


        jPanel.add(jButton_Admin);
        jPanel.add(jButton_Reader);
        jPanel.add(jButton_Books);
    }

}
