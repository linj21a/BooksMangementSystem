package GUIModule.Start;

import GUIModule.LibrarySourcesGui.LibrarySources;
import GUIModule.PublishMethodGet.Constant_Size;
import GUIModule.PublishMethodGet.MouseListenerNew;
import GUIModule.PublishMethodGet.RepaintJPanel;
import GUIModule.Reader.LogInGUi;
import GUIModule.Register.UserRegister;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;

/**
 * 图书管理系统的启动界面。
 */
public class StartGUI {
    private JButton jButton_Admin;
    private JButton jButton_Reader;
    private JButton jButton_Books;
    private JButton jButton_register;
    public JFrame jFrame0;

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


                    //设置窗体的背景图片
                    jFrame0.setIconImage(backImage.getImage());
                    //----------------------------
                    //布局格式化
                    //主面板
                    JPanel jPanel = new RepaintJPanel("src/main/resources/book.jpg");
                    jPanel.setLayout(new BorderLayout());

                   // JLabel jLabel0 = new JLabel("银河图书管理系统欢迎你！");
                    JLabel jLabel0 = new JLabel("银河图书管理系统欢迎你！");
                    jLabel0.setFont(new java.awt.Font("楷体", java.awt.Font.BOLD, 26));
                    jLabel0.setForeground(Color.YELLOW);//文字颜色
                    //面板2号，存放label
                    JPanel jPanel2 = new RepaintJPanel("src/main/resources/boader.JPG");
                    jPanel2.add(jLabel0);
                    jPanel2.setOpaque(false);
                    //面板1号，存放按钮
                    JPanel jPanel1 = new JPanel();//默认流式布局
                    jPanel1.setOpaque(false);
                    jPanel1.setVisible(true);
                    setButton(jPanel1);


                    jButton_register = new JButton();
                    jButton_register.setText("用户注册  ");
                    jButton_register.setForeground(Color.BLUE);
                    jButton_register.setVisible(true);
                    jPanel1.add(jButton_register);
                    buttonListen();

                    //主面板存放面板1、2
                    jPanel.add(jPanel2, BorderLayout.NORTH);
                    jPanel.add(jPanel1, BorderLayout.CENTER);

                    //将主面板添加到窗体
                    jFrame0.add(jPanel, BorderLayout.CENTER);



                    jFrame0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                }
                jFrame0.setVisible(true);

            }
        else
            jFrame0.setVisible(true);


    }

    private void buttonListen() {
        jButton_register.addMouseListener(new MouseListenerNew(jButton_register) {//注册按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                //UserRegister userRegister = GUIFactory.getUserRegister(jFrame0);
               // userRegister.jFrame1_register.setVisible(true);
                new UserRegister(jFrame0);
            }
        });
        jButton_Books.addMouseListener(new MouseListenerNew(jButton_Books) {
            @Override
            public void mouseClicked(MouseEvent e) {
                new LibrarySources(null, jFrame0);
                jFrame0.setVisible(false);
            }
        });//查看馆藏资源
        jButton_Admin.addMouseListener(new MouseListenerNew(jButton_Admin){
            @Override
            public void mouseClicked(MouseEvent e) {
                jFrame0.setVisible(false);
                new LogInGUi(jFrame0);
            }
        });//管理员登陆
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
        jButton_Admin.setText("管理员登陆 ");
        jButton_Admin.setForeground(Color.BLUE);
        jButton_Admin.setVisible(true);

        jButton_Reader = new JButton();
        jButton_Reader.setText("读者登陆  ");
        jButton_Reader.setForeground(Color.BLUE);
        jButton_Reader.setVisible(true);

        jButton_Books = new JButton();
        jButton_Books.setText("馆藏图书资源");
        jButton_Books.setForeground(Color.BLUE);
        jButton_Books.setVisible(true);


        jPanel.add(jButton_Admin);
        jPanel.add(jButton_Reader);
        jPanel.add(jButton_Books);
    }

}
