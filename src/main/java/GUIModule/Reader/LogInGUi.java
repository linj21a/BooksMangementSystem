package GUIModule.Reader;

import DatabasesOperation.DAO_Design.DAOImpl.DAOUser;
import GUIModule.Factory.GUIFactory;
import GUIModule.PublishMethodGet.Constant_Size;
import GUIModule.PublishMethodGet.MouseListenerNew;
import GUIModule.PublishMethodGet.SetMethod;
import GUIModule.PublishMethodGet.TextMouseListen;
import GUIModule.Register.UserRegister;
import GUIModule.Start.StartGUI;

import javax.swing.*;
import java.awt.event.MouseEvent;

/**
 * 登陆界面,
 * 凭借用户名和密码来登陆，显示自己的读者界面，功能：
 * 1、选择查看基本信息
 * 2、查看所借书的情况
 * 3、还书
 * 4、搜索书本进行借书
 * 5、馆藏资源中借书
 */

public class LogInGUi {
    private JButton jButton_login = null;
    private JButton jButton_return = null;
    private JTextField jTextField_name = null;
    private JTextField jTextField_password = null;
    private JButton jButton_register = null;
    private JFrame jFrame_login = null;
    private JFrame before_jFrame = null;//上一个窗体，仅限制了返回主界面用

    public LogInGUi() {
        jFrame_login = new JFrame();
        jFrame_login.setTitle("银河图书管理系统");//默认边缘布局
        jFrame_login.setBounds(Constant_Size.x, Constant_Size.y, Constant_Size.Width, Constant_Size.Height);


        jTextField_name = new JTextField("用户名", 10);
        jTextField_password = new JTextField("密码", 15);
        SetMethod.setForeGround(jTextField_name);
        SetMethod.setForeGround(jTextField_password);
        jButton_login = new JButton();
        jButton_login.setText("登陆");
        SetMethod.setForeGround(jButton_login);

        jButton_return = new JButton();
        jButton_return.setText("返回主界面");
        SetMethod.setForeGround(jButton_return);
        jButton_register = new JButton();
        jButton_register.setText("先注册");
        JPanel jPanel1 = new JPanel();
        jPanel1.add(jTextField_name);
        jPanel1.add(jTextField_password);
        JPanel jPanel2 = new JPanel();
        jPanel2.add(jButton_login);
        jPanel2.add(jButton_return);
        jPanel2.add(jButton_register);
        buttonListener();
        jTextFieldListener();

        JPanel jPanel_login = new JPanel();

        jPanel_login.add(jPanel1);
        jPanel_login.add(jPanel2);
        jFrame_login.add(jPanel_login);
        jFrame_login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame_login.setVisible(true);

    }

    public LogInGUi(JFrame before_jFrame) {
        this.before_jFrame = before_jFrame;
        new LogInGUi();
    }

    private void buttonListener() {
        jButton_register.addMouseListener(new MouseListenerNew(jButton_register) {//先注册按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jFrame_login.setVisible(false);
                if (before_jFrame != null)
                    before_jFrame.setVisible(true);//主界面显示
                else
                    new StartGUI();
                UserRegister userRegister = GUIFactory.getUserRegister(before_jFrame);
                userRegister.jFrame1_register.setVisible(true);
            }
        });

        jButton_login.addMouseListener(new MouseListenerNew(jButton_login) {//登陆按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = jTextField_name.getText().trim();
                String password = jTextField_password.getText().trim();
                if (password.equals("") || name.equals("")) {
                    //弹窗警告！
                    JOptionPane.showMessageDialog(jFrame_login, "用户名或者密码为空！", "Error", JOptionPane.ERROR_MESSAGE);
                    //这个方法是阻塞
                } else {//查询数据库，是否有对应的账号密码，进行校验
                    if (DAOUser.register(name, password) == 0) {
                        //登陆不成功，
                        //弹窗警告！
                        JOptionPane.showMessageDialog(jFrame_login, "用户名或密码错误！", "Error", JOptionPane.ERROR_MESSAGE);
                        //这个方法是阻塞
                    } else {
                        //成功登陆
                        // 进入读者的界面信息处
                        //同时退出登陆界面
                        jFrame_login.dispose();
                        /////
                    }

                }

            }
        });
        jButton_return.addMouseListener(new MouseListenerNew(jButton_return) {//返回到主界面
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jFrame_login.dispose();
                //jFrame_login.setVisible(false);//隐藏登陆界面，不要用dispose，因为其包含了before_jFrame资源，也会关闭
                if (before_jFrame != null)
                    before_jFrame.setVisible(true);//主界面显示
                else
                    new StartGUI();
            }
        });

    }
    private void jTextFieldListener(){
        jTextField_name.addMouseListener(new TextMouseListen(jTextField_name));
        jTextField_password.addMouseListener(new TextMouseListen(jTextField_password));
    }
}
