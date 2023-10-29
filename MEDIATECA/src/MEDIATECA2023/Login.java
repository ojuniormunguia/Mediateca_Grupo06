package MEDIATECA2023;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login {

    private Connection connection = null;
    private JFrame loginFrame;
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public String permissionType;
    public String userId;

    public Login() {
        connection = DatabaseConnection.getConnection();

        loginFrame = new JFrame("Iniciar Sesión");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400, 200);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(3, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Usuario:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Iniciar Sesión");

        mainPanel.add(usernameLabel);
        mainPanel.add(usernameField);
        mainPanel.add(passwordLabel);
        mainPanel.add(passwordField);
        mainPanel.add(new JLabel());
        mainPanel.add(loginButton);

        loginFrame.add(mainPanel, BorderLayout.CENTER);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                validateUser();
            }
        });

        loginFrame.setVisible(true);
    }

    public void validateUser() {
        String user = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        try {
            String adminSQL = "SELECT * FROM administrador WHERE admin_id = ? AND contrasena = ?";
            PreparedStatement adminStatement = connection.prepareStatement(adminSQL);
            adminStatement.setString(1, user);
            adminStatement.setString(2, password);

            ResultSet adminResultSet = adminStatement.executeQuery();

            if (adminResultSet.next()) {
                permissionType = "adm_";
                userId = user;
                new AdminView(permissionType, userId);
                loginFrame.dispose();
            } else {
                String userSQL = "SELECT * FROM socio WHERE socio_id = ? AND contrasena = ?";
                PreparedStatement userStatement = connection.prepareStatement(userSQL);
                userStatement.setString(1, user);
                userStatement.setString(2, password);

                ResultSet userResultSet = userStatement.executeQuery();

                if (userResultSet.next()) {
                    permissionType = "socio_";
                    userId = user;
                    new SocioView(permissionType, userId);
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
        return mainPanel;
    }

    public String getPermissionType() {
        return permissionType;
    }

    public String getUserId() {
        return userId;
    }
}
