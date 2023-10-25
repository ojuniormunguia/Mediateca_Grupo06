package MEDIATECA2023;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class New_revista extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField editorialField;
    private JTextField periocidadField;
    private JTextField yearPublicField;
    private JTextField dispField;

    public New_revista() {
        setTitle("Agregar una nueva revista");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(6, 1));

        JLabel titleLabel = new JLabel("revista_id:");
        idField = new JTextField();
        JLabel artistLabel = new JLabel("titulo:");
        tituloField = new JTextField();
        JLabel yearLabel = new JLabel("editorial:");
        editorialField = new JTextField();
        JLabel genreLabel = new JLabel("periocidad:");
        periocidadField = new JTextField();
        JLabel durationLabel = new JLabel("yearPublic:");
        yearPublicField = new JTextField();
        JLabel cdDispLabel = new JLabel("revistaDisp:");
        dispField = new JTextField();

        JButton addButton = new JButton("Agregar revista");

        // Add components to the frame
        add(idField);
        add(tituloField);
        add(editorialField);
        add(periocidadField);
        add(yearPublicField);
        add(dispField);
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRevistaToDatabase();
            }
        });
    }
    private void addRevistaToDatabase() {
        String id = idField.getText();
        String titulo = tituloField.getText();
        String editorial = editorialField.getText();
        String periocidad = periocidadField.getText();
        String yearPublic = yearPublicField.getText();
        int disp = Integer.parseInt(dispField.getText());

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String insertSql = "INSERT INTO revistas (revista_id, titulo, editorial, periocidad, yearPublic, revistasDisp) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, editorial);
            preparedStatement.setString(4, periocidad);
            preparedStatement.setString(5, yearPublic);
            preparedStatement.setInt(6, disp);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "revista agregada correctamente.");
                dispose(); // Close the window after adding the CD
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el revista.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }


    }

}
