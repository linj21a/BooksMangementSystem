package GUIModule.PublishMethodGet;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.Color;

/**
 * 创建一个JTable子类，实现不可编辑和不可选择列；文字颜色为蓝色
 */
public class NewJTable extends JTable {
   /* public NewJTable(Object[][] rowData, Object[] columnNames) {
        super(rowData, columnNames);
        this.setForeground(Color.BLUE);
    }*/

    public NewJTable(TableModel dm) {
        super(dm);
        this.setForeground(Color.BLUE);
    }

    @Override
    public boolean isColumnSelected(int column) {
        return false;//不可选择列,不可多选
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;//不可编辑
}
}
