package MEDIATECA2023;

import MEDIATECA2023.Añadir_editar.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
public class Main extends JFrame {
    private JPanel contentPanel;
    public String permisos;
    public String identificador;


    public Main(String userId, String permissionType) {
        Connection connection = DatabaseConnection.getConnection();

        Login login = new Login();


        System.out.println(userId + " " + permissionType);
        permisos = permissionType;
        identificador = userId;


    }







    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Connection connection = DatabaseConnection.getConnection();

            Login login = new Login();

            // LOS MANDA A INICIO DE SESION



        });
    }

}