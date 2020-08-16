package GUIModule.PublishMethodGet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * insertString方法可以控制显示的字符，也可以控制传入的字符
 */

public class TextGetField extends JTextField {
    public TextGetField(int cols) {
        super(cols);
    }

    public TextGetField(String text, int columns) {
        super(text, columns);
    }



    static class NewDocument extends PlainDocument {
        /**
         *
         * @param offs 起始位置
         * @param str 文本域的字符集合
         * @param a 属性集
         * @throws BadLocationException 可能抛出的异常
         */
        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {

            if (str == null) {
                return;
            }
            char[] upper = str.trim().toCharArray();//去除空格
            //可以进行其他的操作
            super.insertString(offs, new String(upper), a);
        }
    }
}
