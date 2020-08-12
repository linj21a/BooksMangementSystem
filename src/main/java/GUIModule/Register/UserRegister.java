package GUIModule.Register;

import DatabasesOperation.DAO_Design.DAOImpl.DAOReader;
import DatabasesOperation.DAO_Design.DAOImpl.DAOUser;
import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import DatabasesOperation.DAO_Design.ORM.ORM_User;
import GUIModule.PublishMethodGet.MouseListenerNew;
import GUIModule.PublishMethodGet.SetMethod;
import GUIModule.PublishMethodGet.TextMouseListen;
import GUIModule.Reader.LogInGUi;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Objects;

/**
 * 用户注册模块，用于监控用户是否点击注册
 */

public class UserRegister {
    public JFrame jFrame1_register = null;//主窗体
    private JTextField jTextField_name = null;//用户名框
    private JTextField jTextField__password = null;//密码框,只支持字母
    private JComboBox<String> jComboBox_sex = null;//选择下拉框

    private JButton jButton_save = null;//确认注册按钮
    private JButton jButton_exit = null;

    private JFrame before_jFrame;

    public UserRegister(JFrame jFrame) {
        registerGui(jFrame);
    }

    private void registerGui(JFrame jFrame) {

        if (jFrame == null)
            throw new RuntimeException("错误调用！");
        if (jFrame1_register == null) {
            before_jFrame = jFrame;//将调用者的窗体存起来
            jFrame1_register = new JFrame();
            jFrame1_register.setTitle("用户注册");
            jFrame1_register.setBounds(jFrame.getX() + jFrame.getWidth() / 4, jFrame.getY() + jFrame.getHeight() / 4, jFrame.getWidth() / 2, jFrame.getHeight() / 4);
            jFrame1_register.setVisible(true);


            jTextField_name = new JTextField("用户名(长度0-10)", 10);//使用自定义的单行文本框
            SetMethod.setForeGround(jTextField_name);//设置为蓝色前景


            jTextField__password = new JTextField("密码(长度0~15)", 15);
            SetMethod.setForeGround(jTextField__password);
            textListener();


            jComboBox_sex = new JComboBox<>(new String[]{"woman", "man"});//文本选择框，只能选择woman或者man
            SetMethod.setForeGround(jComboBox_sex);


            //性别标签
            JLabel jLabel = new JLabel("性别");
            SetMethod.setForeGround(jLabel);


            //面板1
            JPanel jPanel = new JPanel();
            jPanel.add(jTextField_name);
            jPanel.add(jTextField__password);
            jPanel.add(jLabel);
            jPanel.add(jComboBox_sex);
            jPanel.setVisible(true);

            //面板2
            JPanel jPanel1 = new JPanel();
            jButton_save = new JButton();
            jButton_save.setText("确认注册");
            SetMethod.setForeGround(jButton_save);

            jButton_exit = new JButton();
            jButton_exit.setText("取消注册");
            SetMethod.setForeGround(jButton_exit);

            buttonListener();
            jPanel1.add(jButton_save);
            jPanel1.add(jButton_exit);
            jFrame1_register.add(jPanel, BorderLayout.NORTH);

            jFrame1_register.add(jPanel1, BorderLayout.SOUTH);
        } else jFrame1_register.setVisible(true);


    }

    /**
     * 给每个按钮添加监控事件，一旦点击了，则发生。。
     */
    private void buttonListener() {
        jButton_save.addMouseListener(new MouseListenerNew(jButton_save) {
            @Override
            public void mouseClicked(MouseEvent e) {
                //点击则将文本框内的内容插入到user表
                String name = jTextField_name.getText().trim();//去除空格
                String password = jTextField__password.getText().trim();
                String sex = Objects.requireNonNull(jComboBox_sex.getSelectedItem()).toString();
                if (name.equals("用户名(长度0-10)") || password.equals("") || name.equals("")) {
                    //弹窗警告！
                    JOptionPane.showMessageDialog(jFrame1_register, "用户名或者密码为空！", "Error", JOptionPane.ERROR_MESSAGE);
                    //这个方法是阻塞
                } else {
                    //能出来说明内容输入正确，可以执行注册程序。
                    ORM_User user = new ORM_User(name, password);
                    user.setId(DAOUser.getMaxId() + 1);//获取最大的id，赋值给这个user
                    DAOUser daoUser = new DAOUser();
                    ORM_Reader reader = new ORM_Reader();
                    if (daoUser.findUser(name) != null) {
                        //弹窗警告！
                        JOptionPane.showMessageDialog(jFrame1_register, "用户名已经存在！", "Error", JOptionPane.ERROR_MESSAGE);
                        //这个方法是阻塞
                    } else {
                        //创建了一个user，则 可以插入一个reader
                        DAOReader daoReader = new DAOReader();
                        reader.setSex(sex);
                        reader.setName(name);
                        reader.setId(user.getId());
                        while (!daoUser.addUser(user) || !daoReader.addReader(reader)) {
                            //插入不成功，说明id重复了。
                            user.setId(user.getId() + 1);
                            reader.setId(user.getId());
                        }//插入成功
                        JOptionPane.showMessageDialog(jFrame1_register, "注册成功！", "注册结果", JOptionPane.INFORMATION_MESSAGE);
                       // jFrame1_register.setVisible(false);隐藏不可见
                        jFrame1_register.dispose();//关闭部分资源

                        //注册成功则跳转到登陆界面：
                        before_jFrame.setVisible(false);
                        new LogInGUi();
                    }
                }
            }
        });


        //取消注册按钮的监听
        jButton_exit.addMouseListener(new MouseListenerNew(jButton_exit) {
            @Override
            public void mouseClicked(MouseEvent e) {
                //点击则取消注册
                jFrame1_register.setVisible(false);//直接将整个面板不显示可见
                jTextField__password.setText("密码(长度0~15)");
                jTextField_name.setText("用户名(长度0-10)");
            }
        });

    }

    /**
     * 实现了对文本框的监听，一点击输入就重置文本为空
     */
    private void textListener() {
        jTextField_name.addMouseListener(new TextMouseListen(jTextField_name));
        jTextField__password.addMouseListener(new TextMouseListen(jTextField__password));
    }


}
