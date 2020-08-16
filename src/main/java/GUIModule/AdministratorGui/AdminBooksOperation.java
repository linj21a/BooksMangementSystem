package GUIModule.AdministratorGui;


import DatabasesOperation.DAO_Design.DAOImpl.DAOBooks;
import DatabasesOperation.DAO_Design.ORM.ORM_Books;
import GUIModule.PublishMethodGet.*;

import javax.swing.*;

import javax.swing.event.TableModelEvent;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static DatabasesOperation.DAO_Design.DAOImpl.DAOBooks.displayBooks;

/**
 * 管理界面对图书资源的操作封装
 */
public class AdminBooksOperation {
    /**
     * 实现删除图书、批量删除图书、更新图书、批量更新图书
     *
     * @param mainJFrame 调用该类的窗体
     * @param s          传入的字符，即要查看的图书名，进行更新。
     * @return 返回构建窗体，更新图书的界面
     */
    public static JFrame updateBook(JFrame mainJFrame, String s) {
        //要更新的图书名
        //下面是查找的结果：根据关键字或者作者找出的信息
        //先添加二维表
        List<ORM_Books> listBooks = displayBooks(s);//默认展示所有的书,这里加了s进行过滤
        //获取修改记录的数组
        List<String> alter_row = new LinkedList<>();
        String[][] bookData = new String[listBooks.size()][];
        for (int i = 0; i < listBooks.size(); i++) {
            ORM_Books books = listBooks.get(i);
            bookData[i] = ORM_Books.booksToArray(books);

        }

        //使用过滤器

        DefaultTableModel model = new DefaultTableModel(bookData, ORM_Books.columnNames) {
            public Class<?> getColumnClass(int column) {
                Class<?> returnValue;
                if (getRowCount() == 0) {
                    returnValue = "".getClass();
                } else if ((column >= 0) && (column < getColumnCount() && getRowCount() > 0)) {
                    returnValue = getValueAt(0, column).getClass();//---------------
                } else {
                    returnValue = Object.class;
                }
                return returnValue;
            }

        };
        //书籍信息二维表显示
        JTable jTable_book = new JTable(model) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column != 0;//id一行不允许编辑
            }
        };//允许编辑
        /*
         * 上面的继承 AbstractTableModel 实现自定义表格模型，功能并不完整，还有很多需要自己
         * 去实现（例如更新数据，通知UI更新，列名称获取等），建议使用 DefaultTableModel 类，
         * 该类对 TableModel 做了较为完善的实现，支持自动更新数据处理，支持UI自动更新，列名称
         * 处理，添加/移除行列等。无特殊要求不需要重写方法，直接使用即可，如下两行代码即可:
         */
        // DefaultTableModel tableModel = new DefaultTableModel(rowData, columnNames);
        // JTable table = new JTable(tableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(model);
        jTable_book.setRowSorter(sorter);
        for (int i = 0; i < jTable_book.getColumnCount(); i++) {
            if (i == 1 || i == 2 || i == 4)
                jTable_book.getColumnModel().getColumn(i).setPreferredWidth(Constant_Size.Width / 3);//设置列的宽度
        }
        // 创建单元格编辑器，使用文本框作为编辑组件
        MyCellEditor cellEditor = new MyCellEditor
                (new JTextField("", jTable_book.getColumnModel().getColumn(2).getWidth()), mainJFrame);

        // 遍历表格中所有数字列，并设置列单元格的编辑器
        for (int i = 1; i < ORM_Books.columnNames.length; i++) {
            // 根据 列名 获取 表格列
            TableColumn tableColumn = jTable_book.getColumn(ORM_Books.columnNames[i]);
            // 设置 表格列 的 单元格编辑器
            tableColumn.setCellEditor(cellEditor);
        }
        JScrollPane jScrollPane = new JScrollPane(jTable_book);//添加到滚动栏目里面
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);//垂直滚动栏
        jScrollPane.setVisible(true);
        jScrollPane.setOpaque(false);//------------
        JPanel jPanel3 = new JPanel();
        jPanel3.add(new JLabel("提示：请直接在表格编辑信息完成更新图书操作。点击更新按钮确认更新到数据库,不许插入。" +
                "\n按住ctrl选择行，进行删除操作，点击删除按钮批量删除"));

        JFrame jFrame = new JFrame();
        jFrame.setTitle("更新图书");
        jFrame.setBounds(mainJFrame.getX(), mainJFrame.getY() + 200, mainJFrame.getWidth(), mainJFrame.getHeight() / 2);

        //按钮面板
        JButton jButton_keyword = new JButton("查找");//关键字查找
        jButton_keyword.setForeground(Color.BLUE);
        JTextField jTextField_find = new JTextField("关键字(小于10个字)", 12);
        jTextField_find.setForeground(Color.BLUE);

        JButton jButton_confirm = new JButton("确认更新");
        jButton_confirm.setForeground(Color.BLUE);
        JButton jButton_delete = new JButton("确认删除");
        jButton_delete.setForeground(Color.BLUE);
        JPanel jPanel = new JPanel();
        jPanel.add(new JLabel("关键字"));
        jPanel.add(jTextField_find);
        jPanel.add(jButton_keyword);
        jPanel.add(jButton_confirm);
        jPanel.add(jButton_delete);
        //查找按钮监控
        jButton_keyword.addMouseListener(new MouseListenerNew(jButton_keyword) {//查找按钮
            @Override
            public void mouseClicked(MouseEvent e) {
                String keyword = jTextField_find.getText().trim();

                if (keyword.length() == 0) {
                    sorter.setRowFilter(RowFilter.regexFilter(s));
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter(keyword));
                }//使用过滤器完成
            }
        });

        //文本框监听
        jTextField_find.addMouseListener(new TextMouseListen(jTextField_find) {
        });

        ///表格监听，编辑表格得到的数据将保存到实际的数据库，保存前会发出提示
        jTable_book.getModel().addTableModelListener(e -> {

            int type = e.getType();
            if (type == TableModelEvent.UPDATE)//更新操作触发
            {
                int row_first = jTable_book.getEditingRow(); //获取改变的行，默认每改变一个格子则弹出一次窗口
                int col = jTable_book.getEditingColumn();//获取改变的列
                String newInformation = jTable_book.getValueAt(row_first, col).toString();//获取新行的值,视觉行
                String id = jTable_book.getValueAt(row_first, 0).toString();//获取id

                alter_row.add(id);//获取id值
                for (int i = 0; i < bookData.length; i++) {
                    if (bookData[i][0].equals(id)) {
                        bookData[i][col] = newInformation;//先缓存到数据bookdata里面
                        break;
                    }
                }
            }
        });

        jButton_confirm.addMouseListener(new MouseListenerNew(jButton_confirm) {
            @Override
            public void mouseClicked(MouseEvent e) {
                while (jTable_book.isEditing()) {//注意这个操作，当在编辑行时，如果没有保存数据，点击button，没有下面的代码二无法实现更新
                    jTable_book.getCellEditor(jTable_book.getEditingRow(), jTable_book.getEditingColumn()).stopCellEditing();
                }

                //确认更新操作，会提示真的更新所有修改到数据库？
                int value = JOptionPane.showConfirmDialog(jFrame, "所有的修改会保存到对应的数据库，是否继续？", "提示", JOptionPane.YES_NO_OPTION);
                if (value == JOptionPane.YES_OPTION) {
                    DAOBooks daoBooks = new DAOBooks();
                    List<ORM_Books> list = new ArrayList<>();
                    JOptionPane.showMessageDialog(jFrame, "修改中...,请稍等", "提示", JOptionPane.INFORMATION_MESSAGE);
                    for (int i = 0; i < alter_row.size(); i++) {
                        String s = alter_row.remove(i);//获取被修改的书的id
                        //  System.out.println("移除的id____"+s);
                        for (String[] bookDatum : bookData) {
                            if (bookDatum[0].equals(s)) {//找到该id
                                ORM_Books books = new ORM_Books();
                                books.setId(Integer.parseInt(s));//设置id，根据id来修改的
                                books.setName(bookDatum[1]);
                                books.setAuthor(bookDatum[2]);
                                books.setPrice(Float.parseFloat(bookDatum[3].trim()));
                                books.setPublisher(bookDatum[4]);
                                books.setPublish(Date.valueOf(bookDatum[5]));
                                books.setType(bookDatum[6]);
                                books.setSurples(Integer.parseInt(bookDatum[7].trim()));
                                //alter_row.remove(i);//修改了，则需要在记录数组中移除
                                list.add(books);
                            }
                        }
                    }
                    if (list.size() == 1)//一个的时候不需要
                        daoBooks.updateBooks(list.get(0));
                    else daoBooks.updateBooksBatch(list);//批量更新

                    JOptionPane.showMessageDialog(jFrame, "修改成功！", "提示", JOptionPane.INFORMATION_MESSAGE);

                }
            }


        });
        jButton_delete.addMouseListener(new MouseListenerNew(jButton_delete) {
            @Override
            public void mouseClicked(MouseEvent e) {
                while (jTable_book.isEditing()) {//注意这个操作，当在编辑行时，如果没有保存数据，点击button，没有下面的代码二无法实现更新
                    jTable_book.getCellEditor(jTable_book.getEditingRow(), jTable_book.getEditingColumn()).stopCellEditing();
                }
                int numLength = jTable_book.getSelectedRows().length;//获取选择的行数
                if (numLength > 0) {
                    int value = JOptionPane.showConfirmDialog(jFrame, "真的要删除选择图书？",
                            "提示", JOptionPane.YES_NO_OPTION);
                    if (value == JOptionPane.YES_OPTION) {
                        DAOBooks daoBooks = new DAOBooks();
                        for (int i = 0; i < numLength; i++) {
                            int row = jTable_book.getSelectedRow();//因为一行一行的删除会改变行索引，导致我们无法
                            //通过直接导入选择的所有行进行 一行行删除实现
                            int id = Integer.parseInt(jTable_book.getValueAt(row, 0).toString());
                            daoBooks.deleteBooks(id);
                            model.removeRow(row);//移除行后行号变了，所以对应的res[i]又变了。
                        }
                    }
                } else JOptionPane.showMessageDialog(jFrame, "你还没有选择要删除的行数据！", "警告", JOptionPane.ERROR_MESSAGE);

            }
        });

        jFrame.add(jPanel, BorderLayout.NORTH);
        jFrame.add(jScrollPane, BorderLayout.CENTER);
        jFrame.add(jPanel3, BorderLayout.SOUTH);
        jFrame.setVisible(true);
        return jFrame;
    }

    /**
     * 添加图书操作，可以添加一本书或者多本书，导入方式分别是手动录入、文件录入。
     */
    public static void addBook(JFrame mainJFrame) {
        int value = JOptionPane.showConfirmDialog(mainJFrame,
                "Yes:手动录入\nNO:批量录入\nCancel:取消录入\n请选择...", "提示", JOptionPane.YES_NO_CANCEL_OPTION);
        if (value == JOptionPane.YES_OPTION)//手动录入
        {
            JFrame jFrame = new JFrame("导入图书");
            jFrame.setBounds(mainJFrame.getX() + 200, mainJFrame.getY() + 200, mainJFrame.getWidth(), mainJFrame.getHeight() / 2);
            JPanel jPanel1 = new JPanel();
            jPanel1.add(new JLabel("添加图书信息录入列表"));
            //---------------------------
            JPanel jPanel2_1 = new JPanel();
            JLabel name = new JLabel("书 名：");
            JTextField jTextField_name = new JTextField("", 15);
            jPanel2_1.add(name);
            jPanel2_1.add(jTextField_name);

            JPanel jPanel2_2 = new JPanel();
            JLabel author = new JLabel("作 者：");
            JTextField jTextField_author = new JTextField("", 15);
            jPanel2_2.add(author);
            jPanel2_2.add(jTextField_author);

            JPanel jPanel2_4 = new JPanel();
            JLabel publisher = new JLabel("出版社：");
            JTextField jTextField_publisher = new JTextField("", 15);
            jPanel2_4.add(publisher);
            jPanel2_4.add(jTextField_publisher);

            JPanel jPanel2_5 = new JPanel();
            JLabel publish = new JLabel("出版日期：");
            JTextField jTextField_publish = new JTextField("", 15);
            jPanel2_5.add(publish);
            jPanel2_5.add(jTextField_publish);

            JPanel jPanel2_6 = new JPanel();
            JLabel surples = new JLabel("书本数量：");
            JTextField jTextField_surples = new JTextField("", 5);
            jPanel2_6.add(surples);
            jPanel2_6.add(jTextField_surples);
            //JPanel jPanel2_3 = new JPanel();
            JLabel type = new JLabel("类别：");
            String[] type_combobox = new String[]{"科技杂志", "文学诗歌", "小说类", "散文", "编程技术", "生活技巧", "其他类"};

            JComboBox<String> jComboBox_type = new JComboBox<>(type_combobox);
            jPanel2_6.add(type);
            jPanel2_6.add(jComboBox_type);

            JPanel jPanel2_7 = new JPanel();
            JLabel price = new JLabel("书本价格：");
            JTextField jTextField_price = new JTextField("", 10);
            jPanel2_7.add(price);
            jPanel2_7.add(jTextField_price);
            //--------------------------

            JButton jButton = new JButton("保存录入");
            jButton.addMouseListener(new MouseListenerNew(jButton));
            JPanel jPanel0 = new JPanel(new FlowLayout(FlowLayout.CENTER));//图书界面录入信息
            jPanel0.add(jButton);


            Box vBox = Box.createVerticalBox();
            vBox.add(jPanel2_1);
            vBox.add(jPanel2_2);
            vBox.add(jPanel2_4);
            vBox.add(jPanel2_5);
            vBox.add(jPanel2_6);
            vBox.add(jPanel2_7);

            //添加文本监控，鼠标一点击则设置为null
            jTextField_author.addMouseListener(new TextMouseListen(jTextField_author));
            jTextField_name.addMouseListener(new TextMouseListen(jTextField_name));
            jTextField_price.addMouseListener(new TextMouseListen(jTextField_price));
            jTextField_publish.addMouseListener(new TextMouseListen(jTextField_publish));
            jTextField_publisher.addMouseListener(new TextMouseListen(jTextField_publisher));
            jTextField_surples.addMouseListener(new TextMouseListen(jTextField_surples));

            //给按钮设置监控
            jButton.addMouseListener(new MouseListenerNew(jButton) {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //书名和书本数量不允许为空
                    String name = jTextField_name.getText().trim();
                    String surples = jTextField_surples.getText().trim();
                    if (name.equals("") || surples.equals("")) {
                        JOptionPane.showMessageDialog(jFrame, "书名或者书本数量不允许为空！", "警告", JOptionPane.ERROR_MESSAGE);
                    } else {
                        int sur;//剩余量
                        float pric; //价格
                        Date publish;//出版日期
                        String publisher = jTextField_publisher.getText().trim();//出版商
                        String type = Objects.requireNonNull(jComboBox_type.getSelectedItem()).toString();//类别

                        try {
                            sur = Integer.parseInt(surples);
                            pric = Float.parseFloat(jTextField_price.getText().trim());
                            publish = Date.valueOf(jTextField_publish.getText().trim());

                        } catch (NumberFormatException ignored) {
                            JOptionPane.showMessageDialog(jFrame, "输入的书本的参数格式可能有误！请检查输入的参数值类型", "警告", JOptionPane.ERROR_MESSAGE);
                            return;
                            //如果无法转化则使用默认值
                        }//如果能出来

                        ORM_Books books = new ORM_Books();
                        books.setName(name);
                        books.setSurples(sur);
                        books.setPrice(pric);
                        books.setPublisher(publisher);
                        books.setPublish(publish);
                        books.setType(type);

                        books.setId(DAOBooks.maxId() + 1);//当前书的最大id加1

                        new DAOBooks().addBooks(books);//添加书
                        JOptionPane.showMessageDialog(jFrame, "添加成功！", "提示", JOptionPane.PLAIN_MESSAGE);
                    }

                }
            });

            jFrame.add(jPanel1, BorderLayout.NORTH);//题目
            jFrame.add(vBox, BorderLayout.CENTER);//录入信息面板
            jFrame.add(jPanel0, BorderLayout.SOUTH);
            jFrame.setVisible(true);

        } else if (value == JOptionPane.NO_OPTION)//批量录入
        {
            JFrame jFrame = new JFrame("批量录入文件");
            jFrame.setBounds(mainJFrame.getX()+200,mainJFrame.getY()+200,mainJFrame.getWidth()/2,mainJFrame.getHeight()/2);
            JPanel jPanel = new JPanel();
            JButton jButton = new JButton("选择文件");
            JFileChooser jFileChooser = new JFileChooser();

            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//只能选文件
            jFileChooser.setMultiSelectionEnabled(false);//不允许多选
          /*  jFileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    //只接受txt文件
                    return f.getName().contains(".txt");
                }

                @Override
                public String getDescription() {
                    return "";
                }
            });//设置文件过滤*/
            //设置文件选择器的默认路径
            jFileChooser.setCurrentDirectory(new java.io.File("E:\\"));//默认打开路径
            //设置窗口标题
            jFileChooser.setDialogTitle("选择要导入的文件，必须符合格式（txt）");
            jFileChooser.setFont(new java.awt.Font("新宋体", Font.PLAIN, 14));
            //  int select = jFileChooser.showOpenDialog(jButton);//将打开按钮邦到button
            jButton.addActionListener(e -> {
                //Handle open button action.
                    int select = jFileChooser.showOpenDialog(jFrame);

                    if (select == JFileChooser.APPROVE_OPTION) {
                        File file = jFileChooser.getSelectedFile();
                        //对该文件进行判断是否可以添加到图书管
                        int res = JOptionPane.showConfirmDialog(jFrame,"请确认文件格式是否正确！不正确会导致异常！",
                                "提示",JOptionPane.YES_NO_OPTION);
                        if(res==JOptionPane.YES_OPTION){
                            try {
                                new DAOBooks().addBooksByFile(file);
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(jFrame,ex.getMessage(),"导入错误",JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                            JOptionPane.showMessageDialog(jFrame,"导入成功！","图书导入",JOptionPane.INFORMATION_MESSAGE);
                        }
                }
            });
            jPanel.add(jButton);
            jFrame.add(jPanel);
            jFrame.setVisible(true);
        }


    }
}
