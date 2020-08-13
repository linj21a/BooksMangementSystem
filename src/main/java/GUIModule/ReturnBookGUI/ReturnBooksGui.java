package GUIModule.ReturnBookGUI;

import DatabasesOperation.DAO_Design.DAOImpl.DAOBorrow;
import DatabasesOperation.DAO_Design.ORM.ORM_Books;
import DatabasesOperation.DAO_Design.ORM.ORM_Reader;
import GUIModule.PublishMethodGet.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.List;

import static DatabasesOperation.DAO_Design.DAOImpl.DAOBooks.displayBooks;

/**
 * 还书界面
 */

public class ReturnBooksGui {
    private final JTextField jTextField_id;
    private final DAOBorrow daoBorrows;
    private final JButton jButton_reflash;
    private JTextField jTextField_find;
    private JButton jButton_return;
    private JButton jButton_returnBooks;//还书按钮
    private JFrame mainJFrame;//主窗口
    private JFrame before_jFrame;
    private JButton jButton_keyword;//查找书按钮
    private String[][] bookData;//书的信息，
    private TableRowSorter<TableModel> sorter;//过滤器，表格JTable
    private ORM_Reader reader;
    private DefaultTableModel model;//表模型，用于删除行数据或者列数据或者添加排序使用


    public ReturnBooksGui(ORM_Reader reader, JFrame jFrame,Integer[]ids) {
        this.before_jFrame = jFrame;
        this.reader = reader;
        mainJFrame = new JFrame();
        mainJFrame.setTitle("我的借书状况");
        mainJFrame.setBounds(jFrame.getX(), jFrame.getY(), jFrame.getWidth(), jFrame.getHeight());
        mainJFrame.setIconImage(new ImageIcon("src/main/resources/borrow.jpg").getImage());


        //按钮面板
        jButton_keyword = new JButton();//查找所借书id
        jButton_returnBooks = new JButton();//还书按钮
        jButton_return = new JButton();//返回
        jButton_reflash = new JButton();
        jButton_reflash.setText("刷新");
        jButton_reflash.setForeground(Color.BLUE);

        //设置属性
        jButton_keyword.setText("查找");
        jButton_keyword.setForeground(Color.BLUE);
        jButton_returnBooks.setText("还书");
        jButton_returnBooks.setForeground(Color.BLUE);
        jButton_return.setText("返回上层");
        jButton_return.setForeground(Color.BLUE);

        jTextField_find = new JTextField("关键字(小于10个字)", 12);
        jTextField_find.setForeground(Color.BLUE);
        JLabel jLabel1 = new JLabel("关键字:");
        jLabel1.setForeground(Color.BLUE);

        JPanel jPanel1_2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanel1_2.add(jLabel1);
        jPanel1_2.add(jTextField_find);
        jPanel1_2.add(jButton_keyword);

        JLabel jLabel2 = new JLabel("输入想要还的书id");
        jLabel2.setForeground(Color.BLUE);
        jTextField_id = new JTextField("", 6);
        jPanel1_2.add(jLabel2);//标签信息
        jPanel1_2.add(jTextField_id);//输入还书id
        jPanel1_2.add(jButton_returnBooks);
        // jPanel1_2.setOpaque(false);设置不透明
        jPanel1_2.setVisible(true);
        //--监听动作
        jButtonLister();
        jTextFieldLister();

        //下面是查找的结果：根据关键字或者作者找出的信息
        //先添加二维表，默认显示全部的书籍信息
        List<ORM_Books> listBooks = displayBooks();//默认展示所有的书-------------------------
        daoBorrows = new DAOBorrow();
       // Integer[] ids = daoBorrows.findMyBorrow(reader);//查找reader所借的所有的书的id
             bookData = new String[listBooks.size()][];
        for (int i = 0, k = 0; i < listBooks.size(); i++) {//这里展示了所有的书的信息，而且这个size必然大于ids里面的值
            int id = listBooks.get(i).getId();
            for (Integer integer : ids) {
                if (integer == id) {//所借书的id
                    bookData[k] = ORM_Books.booksToArray(listBooks.get(i));
                    k++;
                }
            }
        }

        //使用过滤器
        String[] columnNames = {"编号", "书名", "作者", "价格/元", "出版社", "出版日期", "类型"};//借书则不展示余量
         model = new DefaultTableModel(bookData, columnNames) {
            public Class<?> getColumnClass(int column) {
                Class<?> returnValue;
                if ((column >0) && (column < getColumnCount())) {
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



        for (int i = 0; i < jTable_book.getColumnCount(); i++) {
            if (i == 1 || i == 2 || i == 4)
                jTable_book.getColumnModel().getColumn(i).setPreferredWidth(Constant_Size.Width / 3);//设置列的宽度
        }
        JScrollPane jScrollPane = new JScrollPane(jTable_book);//添加到滚动栏目里面
        jScrollPane.setVisible(true);
        jScrollPane.setOpaque(false);//------------
//--------------------------------------表结构完成

        JPanel jPanel3 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        jPanel3.add(jButton_return);
        jPanel3.add(jButton_reflash);
        jPanel3.setVisible(true);



        JPanel mainJPanel = new RepaintJPanel("src/main/resources/dianya.jpg");
        mainJPanel.setLayout(new BorderLayout());
        mainJPanel.add(jPanel1_2, BorderLayout.NORTH);
        mainJPanel.add(jScrollPane, BorderLayout.CENTER);
        mainJPanel.add(jPanel3,BorderLayout.SOUTH);
        mainJPanel.setVisible(true);
        mainJFrame.add(mainJPanel);

        mainJFrame.setVisible(true);
        mainJFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

    }

    public void jButtonLister() {//监听按钮，4个按钮：关键字查找，借书，返回，我的信息
        // 借书按钮
        jButton_returnBooks.addMouseListener(new MouseListenerNew(jButton_returnBooks) {
            @Override
            public void mouseClicked(MouseEvent e) {
                int id_int;
                try {
                    id_int = Integer.parseInt(jTextField_id.getText().trim());

                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(mainJFrame, "你输入的id格式有误",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String name = "";
                int row=-1;
                for (int i = 0; i < bookData.length; i++) {
                    if (Integer.toString(id_int).equals(bookData[i][0])) {//说明输入的id正确，则还书
                        name = bookData[i][1];
                        row = i;//保存要移除的行号
                        break;
                    }
                }
                if (name.equals(""))
                    JOptionPane.showMessageDialog(mainJFrame, "你输入的id格式不在范围",
                            "Error", JOptionPane.ERROR_MESSAGE);
                else {
                    int choose = JOptionPane.showConfirmDialog(mainJFrame, "你要还的书是：" + name + "\n请确认是否现在还？", "提示信息",
                            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (choose == JOptionPane.YES_OPTION) {
                        daoBorrows.returnBook(reader, id_int);
                        JOptionPane.showMessageDialog(mainJFrame, "还书成功！", "还书结果",
                                JOptionPane.INFORMATION_MESSAGE);
                        //刷新数据
                        //;;;;;;;;;;
                        model.removeRow(row);

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

        jButton_return.addMouseListener(new MouseListenerNew(jButton_return) {//返回上一层只需要监控before_frame
            @Override
            public void mouseClicked(MouseEvent e) {
                before_jFrame.setVisible(true);
                mainJFrame.dispose();
            }
        });
        jButton_reflash.addMouseListener(new MouseListenerNew(jButton_reflash){
            @Override
            public void mouseClicked(MouseEvent e) {
                sorter.setRowFilter(null);//刷新就是显示全部的数据
            }
        });

    }

    public void jTextFieldLister() {//一点文本框，则清空内容
        jTextField_find.addMouseListener(new TextMouseListen(jTextField_find) {
        });
        jTextField_id.addMouseListener(new TextMouseListen(jTextField_id));
    }
}
