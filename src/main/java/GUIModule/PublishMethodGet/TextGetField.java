package GUIModule.PublishMethodGet;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class TextGetField extends JTextField {
    public TextGetField(int cols) {
        super(cols);
    }

    public TextGetField(String text, int columns) {
        super(text, columns);
    }



    static class NewDocument extends PlainDocument {

        public void insertString(int offs, String str, AttributeSet a)
                throws BadLocationException {

            if (str == null) {
                return;
            }
            char[] upper = str.trim().toCharArray();//去除空格
            super.insertString(offs, new String(upper), a);
        }
    }
}
