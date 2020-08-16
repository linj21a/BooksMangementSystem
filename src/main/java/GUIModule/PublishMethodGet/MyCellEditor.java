package GUIModule.PublishMethodGet;

import javax.swing.*;

/**
 * 自动义单元格编辑器，不允许空格
 */
public  class MyCellEditor extends DefaultCellEditor {
    private JFrame jFrame;
    public MyCellEditor(JTextField textField, JFrame jFrame) {
        super(textField);
        this.jFrame = jFrame;
    }

   /* public MyCellEditor(JCheckBox checkBox) {
        super(checkBox);
    }

    public MyCellEditor(JComboBox comboBox) {
        super(comboBox);
    }*/
    @Override
    public boolean stopCellEditing() {
        // 获取当前单元格的编辑器组件
       // Component comp = getComponent();

        // 获取当前单元格编辑器输入的值
        Object obj = getCellEditorValue();

        // 如果当前单元格编辑器输入的值不是数字，则返回 false（表示数据非法，）
        if (obj == null || obj.toString().equals("")) {
            JOptionPane.showMessageDialog(jFrame, "不允许输入空值", "警告！", JOptionPane.ERROR_MESSAGE);
            return false;//不允许设置，无法保存,不调用super.stopCellEdition
        }

        // 合法数据交给父类处理
       return super.stopCellEditing();
    }
}

