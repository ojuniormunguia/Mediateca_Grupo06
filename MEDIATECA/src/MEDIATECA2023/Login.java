package MEDIATECA2023;

import MEDIATECA2023.Añadir_editar.Edit_CD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {

    private Connection connection = null;
    private JPanel main;
    private JTextField textField1;
    private JButton button1;
    private JPasswordField passwordField1;

    public String permissionType;
    public String userId;


    public Login() {
        Connection connection = DatabaseConnection.getConnection();

        // ABRE UNA NUEVA VENTANA
        initializeUI();
    }



    private void initializeUI() {
        Connection connection = DatabaseConnection.getConnection();

        this.connection = connection;
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateUser();
            }
        });
    }


    public void validateUser() {
        String user = textField1.getText();
        char[] passwordChars = passwordField1.getPassword();
        String password = new String(passwordChars);

        try {
            // BUSCA SI ES UN ADMIN
            String adminSQL = "SELECT * FROM administrador WHERE admin_id = ? AND contrasena = ?";
            PreparedStatement adminStatement = connection.prepareStatement(adminSQL);
            adminStatement.setString(1, user);
            adminStatement.setString(2, password);

            ResultSet adminResultSet = adminStatement.executeQuery();

            if (adminResultSet.next()) {
                //LO ENVIA DONDE ADMIN
                permissionType = "adm_";
                userId = user;
                System.out.println("Login: " + permissionType + userId);

                new AdminView(permissionType, userId);
                JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(main);
                loginFrame.dispose();
            } else {
                // SI NO ES ADMIN, BUSCA SI ES SOCIO
                String userSQL = "SELECT * FROM socio WHERE socio_id = ? AND contrasena = ?";
                PreparedStatement userStatement = connection.prepareStatement(userSQL);
                userStatement.setString(1, user);
                userStatement.setString(2, password);

                ResultSet userResultSet = userStatement.executeQuery();

                if (userResultSet.next()) {
                    //LO ENVIA DONDE SOCIO
                    permissionType = "socio_";
                    userId = user;
                    System.out.println("Login: " + permissionType + userId);
                    new Main(userId, permissionType);
                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(main);
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos");

                }

                userResultSet.close();
                userStatement.close();
            }

            adminResultSet.close();
            adminStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }


    public Component main() {
        System.out.println("Login: " + permissionType + userId);
        return main;
    }


    //send permissionType and userId to Main
    public String getPermissionType() {
        return permissionType;
    }

    public String getUserId() {
        return userId;
    }
    //save permissionType and userId data from login to Main


}
