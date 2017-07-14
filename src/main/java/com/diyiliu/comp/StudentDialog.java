package com.diyiliu.comp;

import com.diyiliu.util.DbUtil;
import com.diyiliu.util.UIHelper;
import org.apache.commons.dbutils.QueryRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Description: StudentDialog
 * Author: DIYILIU
 * Update: 2017-07-13 09:53
 */
public class StudentDialog extends JDialog implements ActionListener {

    private JPanel jpContent, jpButton;

    private JLabel jlName, jlSex, jlAge, jlSchool, jlMajor;

    private JTextField jtfName, jtfAge, jtfSchool, jtfMajor;

    private JComboBox jcbSex;

    private JButton jbSave, jbCancel;

    // 默认为插入
    private int operate = 0;

    public StudentDialog(Frame owner, String title) {
        super(owner, title, true);
        buildComponent();
        this.setVisible(true);
    }

    public StudentDialog(Frame owner, String title, Object[] data) {
        super(owner, title, true);
        buildComponent();

        // update
        this.operate = 1;
        jtfName.setText(String.valueOf(data[0]));
        jtfName.setEnabled(false);

        jcbSex.setSelectedItem(String.valueOf(data[1]));
        jtfAge.setText(String.valueOf(data[2]));
        jtfSchool.setText(String.valueOf(data[3]));
        jtfMajor.setText(String.valueOf(data[4]));

        this.setVisible(true);
    }

    public void buildComponent() {
        jlName = new JLabel("姓名");
        jlSex = new JLabel("性别");
        jlAge = new JLabel("年龄");
        jlSchool = new JLabel("学校");
        jlMajor = new JLabel("专业");

        jtfName = new JTextField();
        jcbSex = new JComboBox(new String[]{"男", "女"});
        jtfAge = new JTextField();
        jtfSchool = new JTextField();
        jtfMajor = new JTextField();

        jbSave = new JButton("保存");
        jbCancel = new JButton("取消");

        jpContent = new JPanel();
        GroupLayout layout = new GroupLayout(jpContent);
        jpContent.setLayout(layout);

        //自动设定组件、组之间的间隙
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        // LEADING -- 左对齐    BASELINE -- 底部对齐  CENTER -- 中心对齐

        // 水平分组
        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(jlName)
                .addComponent(jlSex)
                .addComponent(jlAge)
                .addComponent(jlSchool)
                .addComponent(jlMajor));
        hGroup.addGroup(layout.createParallelGroup()
                .addComponent(jtfName)
                .addComponent(jcbSex)
                .addComponent(jtfAge)
                .addComponent(jtfSchool)
                .addComponent(jtfMajor));
        layout.setHorizontalGroup(hGroup);

        // 垂直分组
        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(jlName).addComponent(jtfName));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(jlSex).addComponent(jcbSex));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(jlAge).addComponent(jtfAge));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(jlSchool).addComponent(jtfSchool));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                addComponent(jlMajor).addComponent(jtfMajor));
        layout.setVerticalGroup(vGroup);

        // 添加监听
        jbSave.addActionListener(this);
        jbSave.setActionCommand("save");

        jbCancel.addActionListener(this);
        jbCancel.setActionCommand("cancel");

        jpButton = new JPanel();
        jpButton.add(jbSave);
        jpButton.add(jbCancel);

        this.add(jpContent);
        this.add(jpButton, BorderLayout.SOUTH);

        this.setSize(280, 220);
        UIHelper.setCenter(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("cancel")) {

            this.dispose();
        } else if (e.getActionCommand().equals("save")) {

            String name = jtfName.getText().trim();
            String sex = jcbSex.getSelectedItem().toString();
            String age = jtfAge.getText().trim();
            String school = jtfSchool.getText().trim();
            String major = jtfMajor.getText().trim();

            String sql;
            Object[] params;
            if (operate == 0) {
                sql = "insert into student(name, sex, age, school, major) values (?, ?, ?, ?, ?)";
                params = new Object[]{name, sex, age, school, major};
            } else {
                sql = "update student set sex=?, age=?, school=?, major=? where name=?";
                params = new Object[]{sex, age, school, major, name};
            }

            try {
                QueryRunner runner = new QueryRunner(DbUtil.getDataSource());
                int n = runner.update(sql, params);
                if (n > 0) {
                    this.dispose();
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }
}
