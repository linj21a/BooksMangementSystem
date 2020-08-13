package DatabasesOperation.Test;

import DatabasesOperation.DAO_Design.DAOImpl.DAOBooks;
import DatabasesOperation.DAO_Design.DAOImpl.DAOUser;
import DatabasesOperation.DAO_Design.ORM.ORM_Books;
import GUIModule.PublishMethodGet.Constant_Size;
import GUIModule.PublishMethodGet.RepaintJPanel;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;


/**
 * 测试类，用于测试每个DAO实现类的CRUD是否可以实现。
 * 已经通过测试：books表, user表、Reader表、borrow表
 */

public class TestDAOimpl {
    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setBounds(Constant_Size.x,Constant_Size.y,Constant_Size.Width,Constant_Size.Height);
        JPanel mainJPanel = new RepaintJPanel("src/main/resources/booksLibrary.jpg");
        mainJPanel.setLayout(new BorderLayout());
        //Box vBox = Box.createVerticalBox();
        //  vBox.add(mainJPanel);
        //  vBox.setOpaque(false);
        jFrame.add(mainJPanel);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setVisible(true);

    }
}
