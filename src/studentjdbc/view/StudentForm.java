/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentjdbc.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import studentjdbc.dao.StudentDAO;
import studentjdbc.entity.Student;

/**
 *
 * @author PC
 */
public class StudentForm extends JFrame {
    private JTable table;
    
    private DefaultTableModel model;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    
    public StudentForm() throws SQLException {
        setTitle("Quản lý sinh viên");
        setSize(900,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnAdd = new JButton("Thêm");
        btnUpdate = new JButton("Cập nhật");
        btnDelete = new JButton("Xóa");
        btnReset = new JButton("Reset");
        
        btnPanel.add(btnAdd);
        btnPanel.add(btnUpdate);
        btnPanel.add(btnDelete);
        btnPanel.add(btnReset);
                
        
        model = new DefaultTableModel();
        model.setColumnIdentifiers(new String[]{"Mã sinh viên", "Họ và tên", "Lớp", "GPA"});
        table = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        setLayout(new BorderLayout());
        add(btnPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        scrollPane.setPreferredSize(new Dimension(800,300));

        btnAdd.addActionListener(e -> openAddStudentDialog("Thêm Sinh Viên"));
        btnUpdate.addActionListener(e -> openUpdateStudentDialog("Cập Nhật Sinh Viên"));
        btnDelete.addActionListener(e -> openDeleteStudentDialog("Xóa sinh viên"));
        displayStudents();
    }

    public JTable getTable() {
        return table;
    }
    
    private void displayStudents() throws SQLException {
        model.setRowCount(0);
        List<Student> students = StudentDAO.findAll();
        for(Student student : students){
            model.addRow(new Object[]{student.getId(), student.getFullname(), student.getClassname(), student.getGpa()});
        }
    }
      
    public void openAddStudentDialog(String title) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(500,200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5,2,10,10));
        
        JTextField dialogId = new JTextField();
        JTextField dialogName = new JTextField();
        JTextField dialogClassname = new JTextField();
        JTextField dialogGpa = new JTextField();
        
        dialog.add(new JLabel("Mã sinh viên:"));
        dialog.add(dialogId);
        dialog.add(new JLabel("Họ và tên:"));
        dialog.add(dialogName);
        dialog.add(new JLabel("Lớp:"));
        dialog.add(dialogClassname);
        dialog.add(new JLabel("GPA:"));
        dialog.add(dialogGpa);
        
        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        dialog.add(btnSave);
        dialog.add(btnCancel);
        
        btnSave.addActionListener(e -> {
            String id = dialogId.getText().trim();
            String fullname = dialogName.getText().trim();
            String classname = dialogClassname.getText().trim();
            String gpaText = dialogGpa.getText().trim();
            
            if (id.isEmpty() || fullname.isEmpty() || classname.isEmpty() || gpaText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                double gpa = Double.parseDouble(gpaText);
                Student student = new Student();
                student.setId(id);
                student.setFullname(fullname);
                student.setClassname(classname);
                student.setGpa(gpa);
                if(StudentDAO.addStudent(student)) {
                    JOptionPane.showMessageDialog(dialog, "Thêm thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    displayStudents();
                    dialog.dispose();
                }
            } catch(NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "GPA phải là 1 số hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        btnCancel.addActionListener(e -> dialog.dispose());
        
        dialog.setVisible(true);
    }
    
    public void openUpdateStudentDialog(String title) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để chỉnh sửa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String id = model.getValueAt(selectedRow, 0).toString();
        String fullname = model.getValueAt(selectedRow, 1).toString();
        String classname = model.getValueAt(selectedRow, 2).toString();
        String gpa = model.getValueAt(selectedRow, 3).toString();
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(5, 2, 10, 10));
        JTextField dialogId = new JTextField(id);
        dialogId.setEditable(false);
        JTextField dialogName = new JTextField(fullname);
        JTextField dialogClassname = new JTextField(classname);
        JTextField dialogGpa = new JTextField(gpa);
        
        dialog.add(new JLabel("Mã sinh viên:"));
        dialog.add(dialogId);
        dialog.add(new JLabel("Họ và tên:"));
        dialog.add(dialogName);
        dialog.add(new JLabel("Lớp:"));
        dialog.add(dialogClassname);
        dialog.add(new JLabel("GPA:"));
        dialog.add(dialogGpa);

        JButton btnSave = new JButton("Lưu");
        JButton btnCancel = new JButton("Hủy");
        dialog.add(btnSave);
        dialog.add(btnCancel);

        btnSave.addActionListener(e -> {
            String fullnameText = dialogName.getText().trim();
            String classnameText = dialogClassname.getText().trim();
            String gpaText = dialogGpa.getText().trim();

            if (fullnameText.isEmpty() || classnameText.isEmpty() || gpaText.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Vui lòng điền đầy đủ thông tin.", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                double gpaValue = Double.parseDouble(gpaText);
                Student student = new Student();
                student.setId(id);
                student.setFullname(fullnameText);
                student.setClassname(classnameText);
                student.setGpa(gpaValue);

                if (StudentDAO.updateStudent(student)) {
                    JOptionPane.showMessageDialog(dialog, "Cập nhật thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    displayStudents();
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "GPA phải là một số hợp lệ.", "Lỗi nhập liệu", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                Logger.getLogger(StudentForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.setVisible(true);
    }
    
    public void openDeleteStudentDialog(String title) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một sinh viên để xóa.", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this,"Bạn có chắc chắn muốn xóa sinh viên này?",title,
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (confirm == JOptionPane.YES_OPTION) {
            String id = (String) table.getValueAt(selectedRow, 0);
            StudentDAO studentDAO = new StudentDAO();
            boolean isDeleted = studentDAO.deleteStudent(id);
            if(isDeleted) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Sinh viên đã được xóa thành công.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa sinh viên. Vui lòng thử lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
            
    }
}
