package com.diyiliu.tmodel;

import com.diyiliu.util.DbUtil;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.List;

/**
 * Description: StudentModel
 * Author: DIYILIU
 * Update: 2017-07-14 10:08
 */
public class StudentModel extends AbstractTableModel {

    private final String[] columnNames = new String[]{"姓名", "性别", "年龄", "学校", "专业"};
    private List data;

    private final String sql = "select name, sex, age, school, major from student";

    public StudentModel() {

        refresh(sql);
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return ((Object[]) data.get(rowIndex))[columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }



    public void refresh() {
        try {
            QueryRunner runner = new QueryRunner(DbUtil.getDataSource());
            data = runner.query(sql, new ArrayListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.fireTableDataChanged();
    }

    public void refresh(String sql) {
        try {
            QueryRunner runner = new QueryRunner(DbUtil.getDataSource());
            data = runner.query(sql, new ArrayListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        this.fireTableDataChanged();
    }

    public Object[] getRowData(int rowIndex){

        return (Object[]) data.get(rowIndex);
    }
}
