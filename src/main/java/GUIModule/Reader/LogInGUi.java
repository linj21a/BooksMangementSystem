package GUIModule.Reader;

import DatabasesOperation.DAO_Design.DAOImpl.DAOReader;
import DatabasesOperation.DAO_Design.DAOImpl.DAOUser;
import DatabasesOperation.DAO_Design.ORM.ORM_User;
import GUIModule.AdministratorGui.AdminRegister;
import GUIModule.PublishMethodGet.*;
import GUIModule.Register.UserRegister;
import GUIModule.Start.StartGUI;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.Font;
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

        //标签
        JLabel l_name = new JLabel("用户名");
        JLabel l_password = new JLabel("密 码");
        SetMethod.setForeGround(l_password);
        SetMethod.setForeGround(l_name);

        //文本
        jTextField_name = new JTextField(15);
        jTextField_password = new JPasswordField(15);
        SetMethod.setForeGround(jTextField_name);
        SetMethod.setForeGround(jTextField_password);

        //按钮
        jButton_login = new JButton();
        jButton_login.setText("登陆");
        jButton_return = new JButton();
        jButton_return.setText("返回主界面");
        jButton_register = new JButton();
        jButton_register.setText("先注册");
        SetMethod.setForeGround(jButton_return);
        SetMethod.setForeGround(jButton_login);
        SetMethod.setForeGround(jButton_register);

        //面板1，用户名
        JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));//居中对齐
        jPanel1.add(l_name);
        jPanel1.add(jTextField_name);
        //面板2，密码
        JPanel jPanel2 = new JPanel();

        jPanel2.add(l_password);
        jPanel2.add(jTextField_password);

        //面板3，按钮
        JPanel jPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));//居中对齐
        jPanel3.add(jButton_login);
        jPanel3.add(jButton_return);
        jPanel3.add(jButton_register);

        //监听文本框输入和按钮动作
        buttonListener();
        jTextFieldListener();

        //标题面板
        JPanel jPanel0 = new JPanel();
        JLabel title = new JLabel("用户登陆", null, JLabel.CENTER);//设置文本居中
        title.setForeground(Color.BLUE);
        title.setFont(new Font("仿宋", Font.BOLD, 20));
        jPanel0.add(title);

        // 创建一个垂直盒子容器, 把上面 3 个 JPanel 串起来作为内容面板添加到窗口
        Box vBox = Box.createVerticalBox();
        vBox.add(jPanel0);//登陆界面标题
        vBox.add(jPanel1);//1号是用户名
        vBox.add(jPanel2);//2号是密码
        vBox.add(jPanel3);
        jPanel0.setOpaque(false);
        jPanel1.setOpaque(false);
        jPanel2.setOpaque(false);
        jPanel3.setOpaque(false);
        vBox.setOpaque(false);
        JPanel jPanel_main = new RepaintJPanel("src/main/resources/fengJin3.jpg");
        jPanel_main.add(vBox);
        jFrame_login.add(jPanel_main);
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
                else {

                    before_jFrame = new StartGUI().jFrame0;
                }
                new UserRegister(before_jFrame);//用户注册模块
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
                    int[] UseArray = DAOUser.register(name, password);
                    if (UseArray[0] == 0) {
                        //登陆不成功，
                        //弹窗警告！
                        JOptionPane.showMessageDialog(jFrame_login, "用户名或密码错误！", "Error", JOptionPane.ERROR_MESSAGE);
                        //这个方法是阻塞
                    } else if (UseArray[1] != 0) {//是管理员
                        int value = JOptionPane.showConfirmDialog(jFrame_login, "你是管理员账号，是否以管理员身份登陆？", "提示",
                                JOptionPane.YES_NO_OPTION);
                        if (value == JOptionPane.YES_OPTION) {
                            ORM_User user = new ORM_User();
                            user.setId(UseArray[0]);
                            user.setPassword(password);
                            user.setName(name);
                            user.setId(UseArray[1]);
                            jFrame_login.dispose();//先关闭再打开
                            new AdminRegister(jFrame_login, user);
                        }else{
                            //管理员不允许是读者，否则它能操作自己的数据。
                            JOptionPane.showMessageDialog(jFrame_login, "请您换读者账号登陆！", "提示",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        //成功登陆,查找其信息，显示出来
                        // 进入读者的界面信息处
                        //同时退出登陆界面
                        new ReaderInfo(new DAOReader().readReader(name));
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

    private void jTextFieldListener() {
        jTextField_name.addMouseListener(new TextMouseListen(jTextField_name));
        jTextField_password.addMouseListener(new TextMouseListen(jTextField_password));
        jTextField_password.addActionListener(e -> {
            //不允许输入非字母和非英文符号
        });
    }
}
