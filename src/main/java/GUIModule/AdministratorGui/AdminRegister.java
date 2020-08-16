package GUIModule.AdministratorGui;

import DatabasesOperation.DAO_Design.ORM.ORM_User;
import GUIModule.LibrarySourcesGui.LibrarySources;
import GUIModule.PublishMethodGet.Constant_Size;
import GUIModule.PublishMethodGet.NoSuchActionListener;
import GUIModule.PublishMethodGet.RepaintJPanel;
import GUIModule.Reader.LogInGUi;
import GUIModule.Start.StartGUI;

import javax.swing.*;
import java.awt.*;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * 管理员登陆模块
 */
public class AdminRegister {
    private JFrame mainJFrame;
    JMenuItem bookItem1;
 /*   JMenuItem bookItem2;*/
    JMenuItem bookItem3;
   /* JMenuItem bookItem4;*/
    JMenuItem bookItem5;

    JMenuItem userItem1;
    JMenuItem userItem2;
    JMenuItem userItem3;
    JMenuItem userItem4;
    JMenuItem userItem5;

    JMenuItem systemItem1;
    JMenuItem systemItem2;
    JMenuItem systemItem3;
    JMenuItem systemItem4;
    JMenuItem systemItem5;
    JMenuItem systemItem6;
    JMenuItem systemItem7;

    JMenuItem exitItem1;
    JMenuItem exitItem2;
    JMenuItem exitItem3;
    JMenuItem exitItem4;


   /* public static void main(String[] args) {
        new AdminRegister(null, new ORM_User());
    }*/

    /**
     * 构造管理员登陆以后的界面
     *
     * @param jFrame 传入的主界面值
     */
    public AdminRegister(JFrame jFrame, ORM_User user) {

        this.mainJFrame = new JFrame("图书馆管理");
        if (jFrame == null) {
            mainJFrame.setBounds(Constant_Size.x, Constant_Size.y, Constant_Size.Width, Constant_Size.Height);
        } else mainJFrame.setBounds(jFrame.getX(), jFrame.getY(), jFrame.getWidth(), jFrame.getHeight());
        //JPanel jPanel1 = new JPanelOpaque(new FlowLayout(FlowLayout.CENTER));
        // public JPanel(LayoutManager layout) {
        //        this(layout, true);，默认使用缓存区，使得已经加载过的页面变快
        //    }

        JPanel jPanel3 = new RepaintJPanel("src/main/resources/Adminbei.JPG");
        jPanel3.setLayout(new BoxLayout(jPanel3, BoxLayout.Y_AXIS));
        jPanel3.setVisible(true);


        JMenuBar jMenuBar = new JMenuBar();//创建菜单栏
        jMenuBar.setForeground(Color.BLUE);

        //第一个菜单——用户管理（用户列表，修改用户、删除用户、禁止用户借书、生成用户罚单）
        JMenu jMenu = new JMenu("用户管理");
        //第二个菜单——图书管理（更新图书信息、删除图书（设置余量为0）、 导入图书，批量导入图书）
        JMenu jMenu1 = new JMenu("图书管理");
        //第三个菜单——系统管理（修改用户名和密码，数据库系统重启，数据库系统退出，数据库备份，数据库日志记录，暂停用户登陆、清空数据库）
        JMenu jMenu2 = new JMenu("系统管理");
        //第四个菜单——
        JMenu jMenu3 = new JMenu("退出管理");

        //设置菜单1
        userItem1 = new JMenuItem("用户列表");
        userItem2 = new JMenuItem("修改用户");
        userItem3 = new JMenuItem("删除用户");
        userItem4 = new JMenuItem("禁止用户借书");
        userItem5 = new JMenuItem("生成用户罚单");
        jMenu.add(userItem1);
        jMenu.add(userItem2);
        jMenu.add(userItem3);
        jMenu.add(userItem4);
        jMenu.add(userItem5);

        //设置菜单2
        bookItem1 = new JMenuItem("更新图书信息");
     /*   bookItem2 = new JMenuItem("删除图书");*/
        bookItem3 = new JMenuItem("导入图书");
     /*   bookItem4 = new JMenuItem("批量导入图书");*/
        bookItem5 = new JMenuItem("清空图书馆");
        jMenu1.add(bookItem1);
     /*   jMenu1.add(bookItem2);*/
        jMenu1.add(bookItem3);
      /*  jMenu1.add(bookItem4);*/
        jMenu1.add(bookItem5);

        //设置菜单3
        systemItem1 = new JMenuItem("修改管理员账号");
        systemItem6 = new JMenuItem("暂停用户登陆");
        systemItem2 = new JMenuItem("数据库系统重启");
        systemItem3 = new JMenuItem("数据库系统退出");
        systemItem4 = new JMenuItem("数据库备份");
        systemItem5 = new JMenuItem("导出数据库日志");
        systemItem7 = new JMenuItem("清空数据库");
        jMenu2.add(systemItem1);
        jMenu2.add(systemItem6);
        jMenu2.add(systemItem2);
        jMenu2.add(systemItem3);
        jMenu2.add(systemItem4);
        jMenu2.add(systemItem5);
        jMenu2.add(systemItem7);


        //设置菜单4
        exitItem1 = new JMenuItem("回到图书管");
        exitItem2 = new JMenuItem("退出程序 ");
        exitItem3 = new JMenuItem("回到登陆界面");
        exitItem4 = new JMenuItem("回到主界面 ");

        jMenu3.add(exitItem1);
        jMenu3.add(exitItem2);
        jMenu3.add(exitItem3);
        jMenu3.add(exitItem4);


        //添加菜单到菜单栏
        jMenuBar.add(jMenu);
        jMenuBar.add(jMenu1);
        jMenuBar.add(jMenu2);
        jMenuBar.add(jMenu3);
        listenerItem();
        mainJFrame.setJMenuBar(jMenuBar);

        mainJFrame.setLocationRelativeTo(null);
        jPanel3.setOpaque(false);
        mainJFrame.add(jPanel3);
        mainJFrame.setVisible(true);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showText(user);
    }

    private void showText(ORM_User user) {//进入管理员则问候
        String tinytext;
        int time = Integer.parseInt(new SimpleDateFormat("HH").format(new Date()));
        //判断上下午：
        while (true) {
            if (time >= 0 && time <= 6) {
                tinytext = "凌晨";
                break;

            } else if (time > 6 && time <= 12) {
                tinytext = "上午";
                break;
            }
            if (time == 13) {
                tinytext = "中午";
                break;
            }
            if (time > 13 && time <= 18) {
                tinytext = "下午";
                break;
            }
            if (time > 18 && time <= 24) {
                tinytext = "晚上";
                break;
            }
        }
        String text = "亲爱的管理员 " + user.getName() + " " + tinytext + "好!";
        JOptionPane.showMessageDialog(mainJFrame, text, "问候语", JOptionPane.PLAIN_MESSAGE);
    }

    private void listenerItem() {//选项监听,函数式接口可以用lambda表达式替代。

        //用户列表
        userItem1.addActionListener(e -> {
            AdminUserOperation.ListReader(mainJFrame);
            //二维表结构显示
        });
        //修改用户
        userItem2.addActionListener(e -> AdminUserOperation.UpdateUser(mainJFrame));
        //删除用户
        userItem3.addActionListener(e -> AdminUserOperation.deleteUser(mainJFrame));
        //禁止用户借书
        userItem4.addActionListener(new NoSuchActionListener(mainJFrame));
        //生成用户罚单
        userItem5.addActionListener(new NoSuchActionListener(mainJFrame));

        final JFrame[] newJFrame = {null};
        //-BOOK-----------------------------------------------------------------
        //更新图书信息
        bookItem1.addActionListener(e -> {
            //先将图书的值列出来 //判断图书是否存在
            String s = JOptionPane.showInputDialog("请输入要更新的图书的书名.不输入表示查看所有书籍");

            if(s!=null){//点击取消得到null
                if (s.equals(""))
                    s = null;
                if (newJFrame[0] != null) {
                    newJFrame[0].dispose();
                }
                newJFrame[0] = AdminBooksOperation.updateBook(mainJFrame, s);
            }



        });
        //导入图书
        bookItem3.addActionListener(e->AdminBooksOperation.addBook(mainJFrame));
       /* //批量导入图书
        bookItem4.addActionListener(e -> {

        });*/
        //清空图书馆
        bookItem5.addActionListener(new NoSuchActionListener(mainJFrame));
        //系统-----------------------------------------------------
        systemItem1.addActionListener(new NoSuchActionListener(mainJFrame));//修改管理员账号
        systemItem2.addActionListener(new NoSuchActionListener(mainJFrame));//暂停用户登陆
        systemItem3.addActionListener(new NoSuchActionListener(mainJFrame));//数据库系统重启
        systemItem4.addActionListener(new NoSuchActionListener(mainJFrame));//数据库系统退出
        systemItem5.addActionListener(new NoSuchActionListener(mainJFrame));//数据库备份
        systemItem6.addActionListener(new NoSuchActionListener(mainJFrame));//导出数据库日志
        systemItem7.addActionListener(new NoSuchActionListener(mainJFrame));//清空数据库


        //退出管理
        //回到图书管
        exitItem1.addActionListener(e -> {
            mainJFrame.dispose();
            new LibrarySources(null, mainJFrame);
        });
        //退出程序
        exitItem2.addActionListener(e -> {
            System.exit(0);//虚拟机退出
        });
        //回到登陆界面
        exitItem3.addActionListener(e -> {
            mainJFrame.dispose();
            new LogInGUi();
        });
        //回到主界面
        exitItem4.addActionListener(e -> {
            mainJFrame.dispose();
            new StartGUI();
        });


    }
}
