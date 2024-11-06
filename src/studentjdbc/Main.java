/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package studentjdbc;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import studentjdbc.view.StudentForm;

/**
 *
 * @author PC
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentForm form = null;
            try {
                form = new StudentForm();
            } catch (SQLException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
            form.setVisible(true);
        });
    }
}
