/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentjdbc.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import studentjdbc.entity.Student;

/**
 *
 * @author PC
 */
public class StudentDAO {
    private static String url = "jdbc:mysql://127.0.0.1:3306/student_jdbc?user=root";
    private static String username = "root";
    private static String password = "MYSQL";
    
    private static Connection connection;
    private static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public static List<Student> findAll() throws SQLException {
        List<Student> students = new ArrayList<>();
        try (Connection connections = getConnection()){
            ResultSet rs = connections.createStatement().executeQuery("SELECT * FROM student");
            while(rs.next()) {
                Student student = new Student();
                student.setId(rs.getString("id"));
                student.setFullname(rs.getString("fullname"));
                student.setClassname(rs.getString("classname"));
                student.setGpa(rs.getDouble("gpa"));
                students.add(student);
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
        return students;
    }
    
    public static Boolean addStudent(Student student) {
        String insertQuery = "INSERT INTO student(id, fullname, classname, gpa) VALUES (?, ?, ?, ?)";
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(insertQuery)) {
            ps.setString(1, student.getId());
            ps.setString(2, student.getFullname());
            ps.setString(3, student.getClassname());
            ps.setDouble(4, student.getGpa());
            ps.executeUpdate();
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public static Boolean updateStudent(Student student) {
        String updateQuery = "UPDATE student SET fullname=?, classname=?, gpa=? WHERE id=?";
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(updateQuery)) {
            ps.setString(1, student.getFullname());
            ps.setString(2, student.getClassname());
            ps.setDouble(3, student.getGpa());
            ps.setString(4, student.getId());
            ps.executeUpdate();
            return true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    
    public static Boolean deleteStudent(String id) {
        String deleteQuery = "DELETE FROM Student WHERE id=?";
        connection = getConnection();
        try (PreparedStatement ps = connection.prepareStatement(deleteQuery)) {
            ps.setString(1, id);
            ps.executeUpdate();
            return true;
        } catch(Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }
}
