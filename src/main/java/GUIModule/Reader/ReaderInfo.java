package GUIModule.Reader;

import DatabasesOperation.DAO_Design.DAOImpl.DAOBorrow;
import DatabasesOperation.DAO_Design.DAOImpl.DAOReader;
import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import GUIModule.LibrarySourcesGui.LibrarySources;
import GUIModule.PublishMethodGet.Constant_Size;
import GUIModule.PublishMethodGet.MouseListenerNew;
import GUIModule.PublishMethodGet.RepaintJPanel;
import GUIModule.PublishMethodGet.TextMouseListen;
import GUIModule.ReturnBookGUI.ReturnBooksGui;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * 成功登陆以后，显示读者的基本信息，不含有密码和id；
 * 扩展功能：添加头像设定
 */

public class ReaderInfo {
    private JFrame jFrame;//主界面
    private ORM_Reader reader;
    private JTable jTable_reader;//表格信息
    JButton jButton_borrow;//借书按钮
    JButton jButton_return;//还书按钮
    JButton jButton_changeLifeMotto;//修改格言按钮

    public static void main(String[] args) {
        DAOReader daoReader = new DAOReader();
        ORM_Reader reader = daoReader.readReader("Amy");
        new ReaderInfo(reader);
    }

    public ReaderInfo(ORM_Reader reader) {
        if (reader == null)
            throw new RuntimeException("运行出错，表里面没有reader却有user");
        //先获取信息
        this.reader = reader;
        String name = reader.getName();
        String sex = reader.getSex();
        int borrow_day = reader.getBorrow_day();
        int borrow_num = reader.getBorrow_num();
        String life_motto = reader.getLife_motto();
        //查找该读者所借的书
        Integer[] book_id = new DAOBorrow().findMyBorrow(reader);
        int num = book_id == null ? 0 : book_id.length;


        //开始创建界面
        jFrame = new JFrame();
        jFrame.setTitle("我的信息");
        jFrame.setBounds(Constant_Size.x, Constant_Size.y, Constant_Size.Width, Constant_Size.Height);
        ImageIcon jFame_icon = new ImageIcon("src/main/resources/head.PNG");
        jFrame.setIconImage(jFame_icon.getImage());


        //创建含有背景图片的panel
        JPanel mainJPanel = new RepaintJPanel("src/main/resources/beautiful.jpg");
        mainJPanel.setLayout(new BorderLayout());


        JPanel jPanel1 = new JPanel();//最上面的显示头像，利用了JPanel的流式布局和Frame的边缘布局
        ImageIcon icon;
        if (sex.equals("man")) {
            icon = new ImageIcon("src/main/resources/man.jpg");
        } else icon = new ImageIcon("src/main/resources/woman.jpg");
        jPanel1.add(new JLabel(icon));

        //面板2展示个人信息和
        JPanel jPanel2 = new RepaintJPanel("src/main/resources/dianya.jpg");
        //  JPanel jPanel2 = new JPanel();
        // int hasBorrow = //查询已经借了多少本书
        String[][] data = new String[][]{
                {"姓名", name},
                {"性别", sex},
                {"可借书本数", Integer.toString(borrow_day)},
                {"可借天数", Integer.toString(borrow_num)},
                {"已借数书", Integer.toString(num)},
                {"人生格言", life_motto}
        };
        String[] columnNames = {"选项", "值 "};

        jTable_reader = new JTable(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;//都不可编辑
            }
        };//二维表


        jTable_reader.setRowHeight(50);// 设置表格行宽
        jTable_reader.setTableHeader(new JTableHeader());
        jTable_reader.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);// 以下设置表格列宽
        for (int i = 0; i < jTable_reader.getColumnCount(); i++) {
            jTable_reader.getColumnModel().getColumn(i).setPreferredWidth(Constant_Size.Width / 3);//设置列的宽度
        }
        jTable_reader.setFont(new Font("楷体", Font.PLAIN, 18));
        jTable_reader.setForeground(Color.BLUE);//文字蓝色

        jTable_reader.setAutoCreateColumnsFromModel(true);  //使用默认的表模型，使得

        //给表格添加监听器，监听人生格言这一行
        //jTable_readerListen();
            /*
              String life_motto_old = jTable_reader.getValueAt(5,1).toString();
              jTable_reader.getModel().addTableModelListener(e -> {//添加table的监控事件，
                if(e.getColumn()==1&&e.getLastRow()==5){//人生格言被修改
                //对应的数据库也要修改
                String life_motto = jTable_reader.getValueAt(5,1).toString();
                if(life_motto.equals("")||life_motto.length()>20||life_motto.equals(life_motto_old)){
                    //弹窗警告！
                    life_motto = life_motto_old;//将内容恢复原来的样子
                    JOptionPane.showMessageDialog(jFrame, "输入为空或者内容过长！", "Error", JOptionPane.ERROR_MESSAGE);
                    //这个方法是阻塞
                }else{
                    reader.setLife_motto(life_motto);
                    new DAOReader().updateLifeMotto(reader);
                    JOptionPane.showMessageDialog(jFrame, "修改格言成功！", "修改结果", JOptionPane.INFORMATION_MESSAGE);

                }
                jTable_reader.setValueAt(life_motto_old,5,1);
            }
        });
             */


        jPanel2.add(jTable_reader);


        //面板3添加功能按钮
        JPanel jPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));//流式居中
        jButton_borrow = new JButton();//南部功能面板
        jButton_borrow.setText("借书");
        jButton_borrow.setForeground(Color.BLUE);
        jButton_return = new JButton();//南部功能面板
        jButton_return.setText("还书");
        jButton_return.setForeground(Color.BLUE);
        jButton_changeLifeMotto = new JButton("修改人生格言");
        jButton_changeLifeMotto.setForeground(Color.BLUE);
        jPanel3.add(jButton_borrow);
        jPanel3.add(jButton_return);
        jPanel3.add(jButton_changeLifeMotto);
        jButtonLister();


        //设置透明和可见
        jPanel1.setOpaque(false);
        jPanel2.setOpaque(false);
        jPanel3.setOpaque(false);
        jPanel1.setVisible(true);
        jPanel2.setVisible(true);
        jPanel3.setVisible(true);
        //
        Box box = Box.createVerticalBox();
        box.add(jPanel1);//头像部分
        box.add(jPanel2);//中间部分的面板，个人信息
        box.add(jPanel3);//南部功能面板
        box.setOpaque(false);
        mainJPanel.add(box);
        jFrame.add(mainJPanel);

        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//这里一关闭，则全部关闭


    }

    private void jButtonLister() {//监听按钮
        jButton_changeLifeMotto.addMouseListener(new MouseListenerNew(jButton_changeLifeMotto) {//修改人生格言的按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                createGuiOfMotton();
            }
        });
        jButton_borrow.addMouseListener(new MouseListenerNew(jButton_borrow) {//借书按钮的监听
            @Override
            public void mouseClicked(MouseEvent e) {
                // jFrame.setVisible(false);//隐藏我的信息界面
                if(reader==null){
                    System.out.println("??");
                }
                new LibrarySources(reader, jFrame);//进入馆藏图书资源界面，同时将界面的信息传入
               // jFrame.setVisible(false);
                jFrame.dispose();


            }
        });
        jButton_return.addMouseListener(new MouseListenerNew(jButton_return) {//还书按钮的监听
            @Override
            public void mouseClicked(MouseEvent e) {
              //  List<ORM_Books> listBooks = displayBooks();//默认展示所有的书-------------------------
                DAOBorrow daoBorrows = new DAOBorrow();
                Integer[] ids = daoBorrows.findMyBorrow(reader);//查找reader所借的所有的书的id

                if (ids == null || ids.length == 0) {
                    JOptionPane.showMessageDialog(jFrame, "你还没有借书记录！", "提示", JOptionPane.PLAIN_MESSAGE);
                }else {
                    new ReturnBooksGui(reader,jFrame,ids);//进入还书界面，同时将界面的信息传入
                    jFrame.setVisible(false);//隐藏我的信息界面
                }


            }
        });
    }

    private void createGuiOfMotton() {
        JFrame jFrame1 = new JFrame();
        jFrame1.setBounds(jFrame.getX() + 200, jTable_reader.getY() + 200, jTable_reader.getWidth(), jTable_reader.getHeight());
        jFrame1.setTitle("修改格言");
        jFrame1.setVisible(true);
        JLabel jLabel = new JLabel("新格言");
        jLabel.setForeground(Color.BLUE);
        JTextField jTextField = new JTextField(20);
        jTextField.setText("");
        jTextField.addMouseListener(new TextMouseListen(jTextField));
        //面板放文本框和标签
        JPanel jPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanel.add(jLabel);
        jPanel.add(jTextField);
        JButton jButton = new JButton();
        jButton.setText("确认修改");
        JButton jButton1 = new JButton();
        jButton1.setText("取消修改");

        //面板2放2个按钮
        JPanel jPanel1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanel1.add(jButton);
        jPanel1.add(jButton1);//确认修改按钮
        jButton.addMouseListener(new MouseListenerNew(jButton) {
            @Override
            public void mouseClicked(MouseEvent e) {
                String life_motto_old = jTable_reader.getValueAt(5, 1).toString();
                String life_motto_new = jTextField.getText().trim();
                if (life_motto_new.equals("") || life_motto_new.length() > 20) {
                    //弹窗警告！
                    life_motto_new = life_motto_old;//将内容恢复原来的样子
                    JOptionPane.showMessageDialog(jFrame, "输入为空或者内容过长！", "Error", JOptionPane.ERROR_MESSAGE);
                    //这个方法是阻塞
                } else {
                    reader.setLife_motto(life_motto_new);
                    new DAOReader().updateLifeMotto(reader);
                    JOptionPane.showMessageDialog(jTextField, "修改格言成功！", "修改结果", JOptionPane.INFORMATION_MESSAGE);
                    jFrame1.dispose();//成功则关闭该窗口
                }
                jTable_reader.setValueAt(life_motto_new, 5, 1);
            }
        });
        jButton1.addMouseListener(new MouseListenerNew(jButton1) {
            @Override
            public void mouseClicked(MouseEvent e) {
                jFrame1.dispose();
            }
        });


        jFrame1.add(jPanel, BorderLayout.NORTH);
        jFrame1.add(jPanel1, BorderLayout.SOUTH);
        jFrame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame1.setVisible(true);
    }
}
