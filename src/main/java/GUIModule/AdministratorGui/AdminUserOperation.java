package GUIModule.AdministratorGui;

import DatabasesOperation.DAO_Design.DAOImpl.DAOReader;
import DatabasesOperation.DAO_Design.DAOImpl.DAOUser;
import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import DatabasesOperation.DAO_Design.ORM.ORM_User;
import GUIModule.PublishMethodGet.MouseListenerNew;
import GUIModule.PublishMethodGet.NewJTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

public class AdminUserOperation {
    private static JFrame jFrame;

    public static void UpdateUser(JFrame mainJFrame) {
        //修改用户模块
        //先弹窗
        jFrame = new JFrame("修改用户");
        jFrame.setBounds(mainJFrame.getX() + 200, mainJFrame.getY() + 200, mainJFrame.getWidth() / 2, mainJFrame.getHeight() / 4);
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel jLabel = new JLabel("修改的用户名字");
        jPanel.add(jLabel);
        JTextField jTextField = new JTextField("", 15);
        jPanel.add(jTextField);
        JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel jLabel1 = new JLabel(" 新 用 户 名    ");
        JLabel jLabel2 = new JLabel(" 新  密  码      ");
        JTextField jTextField1 = new JTextField("", 15);
        JTextField jTextField2 = new JTextField("", 15);

        jPanel1.add(jLabel1);
        jPanel1.add(jTextField1);
        JPanel jPanel2 = new JPanel();

        jPanel2.add(jLabel2);
        jPanel2.add(jTextField2);

        JPanel jPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton jButton = new JButton("修改");
        jButton.addMouseListener(new MouseListenerNew(jButton) {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = jTextField.getText().trim();
                String new_name = jTextField1.getText().trim() ;
                String new_password = jTextField2.getText().trim();
                if(name.equals("")||new_name.equals("")||new_password.equals("")){
                    JOptionPane.showMessageDialog(mainJFrame,"文本框输入为空！请检查！","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    DAOUser DAOuser = new DAOUser();
                    ORM_User orm_user = DAOuser.findUser(name);
                    if (orm_user == null) {
                        JOptionPane.showMessageDialog(mainJFrame, "找不到用户名为：\n" + name, "警告",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        String password = orm_user.getPassword();
                        System.out.print("id "+orm_user.getId());
                        //这里的换行符可以换为系统自带的，实现跨平台；
                        int value = JOptionPane.showConfirmDialog(mainJFrame,
                                "你要修改的用户信息如下：\n原用户名：" + name + "\n原密码：" + password +
                                "\n新用户名："+new_name+"\n新密码"+new_password+"\n是否要修改？", "警告", JOptionPane.YES_NO_OPTION);
                        if (value == JOptionPane.YES_OPTION) {
                                if ((DAOuser.updateUser(orm_user, new_name, new_password))!= 0) {
                                    //修改了读者表，对应的reader表的name也要改
                                    ORM_Reader reader = new DAOReader().readReader(name);
                                    System.out.println(reader.getId());
                                    new DAOReader().updateReaderName(reader,new_name);
                                    JOptionPane.showMessageDialog(mainJFrame, "修改成功！", "提示"
                                            , JOptionPane.PLAIN_MESSAGE);
                                    jFrame.dispose();
                                } else JOptionPane.showMessageDialog(mainJFrame, "修改失败，请重试", "提示"
                                        , JOptionPane.PLAIN_MESSAGE);
                            }
                        }
                    }
                }
        });
        jPanel3.add(jButton);
        Box vbox = Box.createVerticalBox();
        vbox.add(jPanel);
        vbox.add(jPanel1);
        vbox.add(jPanel2);
        vbox.add(jPanel3);
        jFrame.setContentPane(vbox);
        jFrame.setVisible(true);
    }

    public static void deleteUser(JFrame mainJFrame) {
        jFrame = new JFrame("删除用户");
        jFrame.setBounds(mainJFrame.getX() + 200, mainJFrame.getY() + 200, mainJFrame.getWidth() / 2, mainJFrame.getHeight() / 4);
        JPanel jPanel = new JPanel();
        JLabel jLabel = new JLabel("要删除的用户名:");
        jPanel.add(jLabel);
        JTextField jTextField = new JTextField("", 10);
        JButton jButton = new JButton("删除");
        jButton.addMouseListener(new MouseListenerNew(jButton) {
            @Override
            public void mouseClicked(MouseEvent e) {
                String name = jTextField.getText().trim();
                if(name.equals("")){
                    JOptionPane.showMessageDialog(mainJFrame,"文本框输入为空！请检查！","错误",JOptionPane.ERROR_MESSAGE);
                }else {
                    DAOUser DAOuser = new DAOUser();
                    ORM_User orm_user = DAOuser.findUser(name);
                    if (orm_user == null) {
                        JOptionPane.showMessageDialog(mainJFrame, "找不到用户名为：\n" + name, "警告",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        String password = orm_user.getPassword();
                        ORM_Reader reader = new ORM_Reader();
                        reader.setName(orm_user.getName());
                        reader.setId(orm_user.getId());
                        int value = JOptionPane.showConfirmDialog(mainJFrame, "你要删除的用户为：\n" + name + "\n密码" + password +
                                "\n是否要删除？", "警告", JOptionPane.YES_NO_OPTION);
                        if (value == JOptionPane.YES_OPTION) {
                            //纪要删除user也要删除reader
                            if (DAOuser.deleteUser(orm_user) != 0 && (new DAOReader().deleteReader(reader)!=0)){
                                    JOptionPane.showMessageDialog(mainJFrame, "删除成功", "提示"
                                        , JOptionPane.PLAIN_MESSAGE);
                            }
                            else JOptionPane.showMessageDialog(mainJFrame, "删除失败，该用户还有书没还！", "提示"
                                    , JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }

            }

        });
        jPanel.add(jTextField);
        JPanel jPanel1 = new JPanel();
        jPanel1.add(new JLabel("请谨慎操作避免删除错读者"));
        jPanel.add(jButton);
        jFrame.add(jPanel, BorderLayout.NORTH);
        jFrame.add(jPanel1, BorderLayout.CENTER);
        jFrame.setVisible(true);
        //输入id

    }

    public static void ListReader(JFrame mainJFrame) {
        List<ORM_User> list = DAOUser.readUser();//只展示读者
        String[] columnNames = new String[]{"编号", "用户名", "密码","已借书数目","人生格言"};
        String[][] userData = new String[list.size()][columnNames.length];
        DAOReader daoreader = new DAOReader();
        for (int i = 0; i < userData.length; i++) {

            ORM_User user = list.get(i);
            ORM_Reader reader = daoreader.readReader(user.getName());
            userData[i][0] = Integer.toString(user.getId());
            userData[i][1] = reader.getName();
            userData[i][2] = user.getPassword();
            userData[i][3] = Integer.toString(reader.getHasBorrow());
            userData[i][4] = reader.getLife_motto();
        }
        TableModel model = new DefaultTableModel(userData, columnNames) {
            public Class<?> getColumnClass(int column) {
                Class<?> returnValue;
                if ((column >= 0) && (column < getColumnCount())) {
                    returnValue = getValueAt(0, column).getClass();
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }
        };
        //书籍信息二维表显示
        JTable jTable_user = new NewJTable(model);
        // sorter = new TableRowSorter<TableModel>(model);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        jTable_user.setRowSorter(sorter);
        JScrollPane jScrollPane = new JScrollPane(jTable_user);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//垂直滚动栏

        JFrame jFrame = new JFrame("读者信息列表");
        jFrame.setBounds(mainJFrame.getX()+200,mainJFrame.getY()+200,mainJFrame.getWidth(),mainJFrame.getHeight()/2);
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel jLabel = new JLabel("读者列表（不许编辑）");
        jPanel.add(jLabel);
        jFrame.add(jPanel);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);
    }
}
