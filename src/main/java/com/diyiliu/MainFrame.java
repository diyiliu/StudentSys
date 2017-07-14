package com.diyiliu;

import com.diyiliu.comp.StudentDialog;
import com.diyiliu.tmodel.StudentModel;
import com.diyiliu.util.DbUtil;
import com.diyiliu.util.UIHelper;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;

/**
 * Description: MainFrame
 * Author: DIYILIU
 * Update: 2017-07-13 09:17
 */
public class MainFrame extends JFrame implements ActionListener {


    private JPanel jpHeader, jpFooter;

    private JScrollPane jsp;

    private JTable jTable;

    private JButton jbSearch, jbCreate, jbDelete, jbUpdate;

    private JLabel jlInput;
    private JTextField jtfInput;

    private StudentModel studentModel = new StudentModel();

    public MainFrame() {
        UIHelper.beautify();

        jlInput = new JLabel("请输入姓名");
        jtfInput = new JTextField(20);
        jbSearch = new JButton("查询");

        jbCreate = new JButton("新增");
        jbUpdate = new JButton("修改");
        jbDelete = new JButton("删除");


        jpHeader = new JPanel();
        jpHeader.add(jlInput);
        jpHeader.add(jtfInput);
        jpHeader.add(jbSearch);


        jpFooter = new JPanel();
        jpFooter.add(jbCreate);
        jpFooter.add(jbUpdate);
        jpFooter.add(jbDelete);

        jsp = new JScrollPane();
        jTable = new JTable(studentModel);
        //jTable.setShowGrid(false);
        //jTable.setEnabled(false);

        jsp.setViewportView(jTable);
        //jsp.setBorder(new EmptyBorder(0, 0, 0, 0));

        this.add(jpHeader, BorderLayout.NORTH);
        this.add(jpFooter, BorderLayout.SOUTH);
        this.add(jsp);

        // 添加监听;
        jbSearch.addActionListener(this);
        jbSearch.setActionCommand("search");

        jbCreate.addActionListener(this);
        jbCreate.setActionCommand("create");

        jbUpdate.addActionListener(this);
        jbUpdate.setActionCommand("update");

        jbDelete.addActionListener(this);
        jbDelete.setActionCommand("delete");

        this.setSize(600, 400);
        UIHelper.setCenter(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public static void main(String[] args) {

        new MainFrame();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("create")) {

            new StudentDialog(this, "新增学生");
            studentModel.refresh();
        } else if (e.getActionCommand().equals("search")) {
            String name = jtfInput.getText().trim();
            if (name.length() < 1) {
                studentModel.refresh();
            } else {
                String sql = "select name, sex, age, school, major from student where name like '" + name + "%'";
                studentModel.refresh(sql);
            }
        } else if (e.getActionCommand().equals("update")) {

            int row = jTable.getSelectedRow();

            if (row < 0) {
                JOptionPane.showMessageDialog(this, "请选择一行需要修改的信息!");
                return;
            }

            new StudentDialog(this, "修改信息", studentModel.getRowData(row));
            studentModel.refresh();
        } else if (e.getActionCommand().equals("delete")) {

            int[] rows = jTable.getSelectedRows();

            if (rows.length < 1) {
                JOptionPane.showMessageDialog(this, "请至少选择一行需要删除的信息!");
            }else {
              int result = JOptionPane.showConfirmDialog(this, "确定要删除选中的条信息!", "共" + rows.length + "条", JOptionPane.YES_NO_OPTION);

              if (result == 0){
                  StringBuilder str = new StringBuilder();
                  for (int i: rows){
                      Object[] data = studentModel.getRowData(i);
                      str.append("'").append(data[0]).append("',");
                  }

                  try {
                      String sql = "delete from student where name in (" + str.substring(0, str.length() - 1) + ")";
                      QueryRunner runner = new QueryRunner(DbUtil.getDataSource());
                      runner.update(sql);
                  } catch (SQLException e1) {
                      e1.printStackTrace();
                  }

                  studentModel.refresh();
              }
            }
        }
    }
}
