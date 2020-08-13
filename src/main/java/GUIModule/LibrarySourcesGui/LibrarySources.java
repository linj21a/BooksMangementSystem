package GUIModule.LibrarySourcesGui;

import DatabasesOperation.DAO_Design.DAOImpl.DAOBorrow;
import DatabasesOperation.DAO_Design.ORM.ORM_Books;
import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import GUIModule.PublishMethodGet.*;
import GUIModule.Reader.ReaderInfo;
import GUIModule.Start.StartGUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static DatabasesOperation.DAO_Design.DAOImpl.DAOBooks.displayBooks;

/**
 * 馆藏图书资源、以及借书也是来这里
 */

public class LibrarySources {
    private ORM_Reader reader;
    private JFrame before_jFrame;//前一个窗体
    public JFrame jFrame;//主窗体、
    private String[][] bookData;//书的信息，
    private JTextField jTextField_find;//查找关键字
    private JTextField jTextField_id;//要借书的id
    private JButton jButton_keyword;//按照关键字查找
    private JButton jButton_borrow;//借书按钮
    private JButton jButton_return;//返回上一层的按钮，搭配before_frame
    private JButton jButton_myInfo;//我的信息，如果reader==null，则直接提示先去注册，然后直接返回首页。
    //private String[] columnNames = {"编号", "书名", "作者", "价格/元", "出版社", "出版日期", "类型", "余量"};//book的数据
    private TableRowSorter<TableModel> sorter;//过滤器，表格JTable

    public LibrarySources(ORM_Reader reader, JFrame before_jFrame) {

        this.reader = reader;
        this.before_jFrame = before_jFrame;
        jFrame = new JFrame();
        //先设置大小再设置图标
        jFrame.setBounds(before_jFrame.getX(), before_jFrame.getY(), before_jFrame.getWidth(),before_jFrame.getHeight());
        jFrame.setIconImage(new ImageIcon("src/main/resources/book.jpg").getImage());


        //标题面板
        JPanel jPanel_title = new RepaintJPanel("src/main/resources/goodtitle.jpg");
        // JPanel jPanel_title = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel jLabel_title = new JLabel("馆藏图书大全");
        jLabel_title.setFont(new Font("楷体", Font.PLAIN, 20));
        jLabel_title.setForeground(Color.BLUE);
        jPanel_title.add(jLabel_title);
        // jPanel_title.setOpaque(false);//------------


        //按钮面板
        jButton_keyword = new JButton();//关键字查找
        jButton_borrow = new JButton();//借书按钮
        jButton_return = new JButton();//返回上一个界面，主页或者个人信息页面
        jButton_myInfo = new JButton();//个人信息页面，需要判断是否是从主页来的是，则不需要创建。

        jButton_keyword.setText("查找");
        jButton_keyword.setForeground(Color.BLUE);

        jButton_borrow.setText("借书");
        jButton_borrow.setForeground(Color.BLUE);

        jButton_myInfo.setText("我的信息");//如果reader==null说明该人没有登陆，直接提示先登陆
        jButton_myInfo.setForeground(Color.BLUE);

        jButton_return.setText("返回上层");
        jButton_return.setForeground(Color.BLUE);


        jTextField_find = new JTextField("关键字(小于10个字)", 12);
        jTextField_find.setForeground(Color.BLUE);

        JPanel jPanel1_1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JPanel jPanel1_2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanel1_1.add(jButton_myInfo);
        jPanel1_1.add(jButton_return);
        jPanel1_1.setOpaque(false);
        jPanel1_1.setVisible(true);
        JLabel jLabel1 = new JLabel("关键字:");
        jLabel1.setForeground(Color.BLUE);
        jPanel1_2.add(jLabel1);
        jPanel1_2.add(jTextField_find);
        jPanel1_2.add(jButton_keyword);

        JLabel jLabel2 = new JLabel("输入要借书的id");
        jLabel2.setForeground(Color.BLUE);
        jTextField_id = new JTextField("", 6);
        jPanel1_2.add(jLabel2);//标签信息
        jPanel1_2.add(jTextField_id);//输入借书id
        jPanel1_2.add(jButton_borrow);
        // jPanel1_2.setOpaque(false);设置不透明
        jPanel1_2.setVisible(true);

        //--监听动作
        jButtonLister();
        jTextFieldLister();

        JPanel jPanel1 = new JPanel();
        jPanel1.setLayout(new BorderLayout());//边缘布局
        jPanel1.add(jPanel1_1, BorderLayout.NORTH);
        jPanel1.add(jPanel1_2, BorderLayout.SOUTH);
        jPanel1.setVisible(true);
        jPanel1.setOpaque(false);//-------


        //下面是查找的结果：根据关键字或者作者找出的信息
        //先添加二维表，默认显示全部的书籍信息
        List<ORM_Books> listBooks = displayBooks();//默认展示所有的书
        bookData = new String[listBooks.size()][];
        for (int i = 0; i < listBooks.size(); i++) {
            ORM_Books books = listBooks.get(i);
            bookData[i] = ORM_Books.booksToArray(books);
        }

        //  jTable_book = new NewJTable(bookData, columnNames); //创建table子类
        //使用过滤器

        TableModel model = new DefaultTableModel(bookData, ORM_Books.columnNames) {
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
        JTable jTable_book = new NewJTable(model);
        // sorter = new TableRowSorter<TableModel>(model);
        sorter = new TableRowSorter<>(model);
        jTable_book.setRowSorter(sorter);


        //
        for (int i = 0; i < jTable_book.getColumnCount(); i++) {
            if (i == 1 || i == 2 || i == 4)
                jTable_book.getColumnModel().getColumn(i).setPreferredWidth(Constant_Size.Width / 3);//设置列的宽度
        }
        JScrollPane jScrollPane = new JScrollPane(jTable_book);//添加到滚动栏目里面
        jScrollPane.setVisible(true);
        jScrollPane.setOpaque(false);//------------

           /* //设置表的模式和排序以及关键字查找
            DefaultTableModel model = (DefaultTableModel) jTable_book.getModel();
            jTable_book.setModel(model);
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
            jTable_book.setRowSorter(sorter);
            //实现过滤，search为正则表达式，该方法支持参数为null和空字符串，因此不用对输入进行校验

            sorter.setRowFilter(RowFilter.regexFilter(jTextField_find.getText().trim()));*/


        Box vBox = Box.createVerticalBox();
        vBox.add(jPanel_title);
        vBox.add(jPanel1);
        vBox.add(jScrollPane);
        vBox.setOpaque(false);//----------------
        JPanel mainJPanel = new RepaintJPanel("src/main/resources/booksLibrary.jpg");
        mainJPanel.setLayout(new BorderLayout());
        mainJPanel.add(vBox);
        mainJPanel.setVisible(true);
        jFrame.add(mainJPanel, BorderLayout.CENTER);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//这里一退出就全部退出
        jFrame.setVisible(true);
    }

    public void jButtonLister() {//监听按钮，4个按钮：关键字查找，借书，返回，我的信息
        // 借书按钮
        jButton_borrow.addMouseListener(new MouseListenerNew(jButton_borrow) {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (reader == null) {
                    JOptionPane.showMessageDialog(jFrame, "你还没有登陆！", "Error", JOptionPane.ERROR_MESSAGE);
                    if (before_jFrame == null) {
                        new StartGUI();
                    } else before_jFrame.setVisible(true);
                    jFrame.dispose();
                } else {
                    int id_int;
                    try {
                        id_int = Integer.parseInt(jTextField_id.getText().trim());

                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(jFrame, "你输入的id格式有误",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (id_int <= bookData.length && id_int > 0) {//大于0且小于等于书的id
                        {
                            String name = bookData[id_int - 1][1];
                            int choose = JOptionPane.showConfirmDialog(jFrame, "你要借的书是：" + name + "\n请确认是否要借？", "提示信息",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

                            if (choose == JOptionPane.YES_OPTION) {
                                if (Integer.parseInt(bookData[id_int - 1][7]) > 0)//能借书
                                {
                                    new DAOBorrow().borrowBook(reader, id_int);//索引和实际的id差1个
                                    //reader.setBorrow_num(reader.getBorrow_num()+1);//更新原来的reader的已经借的书的值
                                    JOptionPane.showMessageDialog(jFrame, "借书成功！", "借书结果",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else JOptionPane.showMessageDialog(jFrame, "对不起，这本书余量为0",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else{
                        JOptionPane.showMessageDialog(jFrame, "你输入的id格式有误或者不在范围",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }

            }
        });
        jButton_keyword.addMouseListener(new MouseListenerNew(jButton_keyword) {//查找按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                String keyword = jTextField_find.getText().trim();

                if (keyword.length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(keyword));
                }//使用过滤器完成
            }
        });
        jButton_myInfo.addMouseListener(new MouseListenerNew(jButton_myInfo) {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (reader == null) {
                    JOptionPane.showMessageDialog(jFrame, "你还没有登陆！", "Error", JOptionPane.ERROR_MESSAGE);
                    if (before_jFrame == null) {
                        new StartGUI();
                    } else before_jFrame.setVisible(true);
                } else {
                    new ReaderInfo(reader);
                    jFrame.dispose();//关闭该资源
                }
            }
        });
        jButton_return.addMouseListener(new MouseListenerNew(jButton_return) {//返回上一层只需要监控before_frame
            @Override
            public void mouseClicked(MouseEvent e) {
                before_jFrame.setVisible(true);
                jFrame.dispose();
            }
        });

    }

    public void jTextFieldLister() {//一点文本框，则清空内容
        jTextField_find.addMouseListener(new TextMouseListen(jTextField_find) {
        });
        jTextField_id.addMouseListener(new TextMouseListen(jTextField_id));
    }
}
