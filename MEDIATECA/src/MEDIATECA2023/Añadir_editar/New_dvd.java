package MEDIATECA2023.Añadir_editar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class New_dvd extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField directorField;
    private JTextField duracionField;
    private JTextField generoField;
    private JTextField dvdDispField;
    private New_dvd.ConfirmedDVD callback;
    public New_dvd() {
        setTitle("Agregar una nueva revista");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        JLabel libro_idLabel = new JLabel("id:");
        idField = new JTextField();
        JLabel tituloLabel = new JLabel("titulo:");
        tituloField = new JTextField();
        JLabel directorLabel = new JLabel("director:");
        directorField = new JTextField();
        JLabel duracionLabel = new JLabel("duracion:");
        duracionField = new JTextField();
        JLabel generoLabel = new JLabel("genero:");
        generoField = new JTextField();
        JLabel dvdDispLabel = new JLabel("dvdDisp:");
        dvdDispField = new JTextField();


        JButton addButton = new JButton("Agregar revista");

        // Add components to the frame
        add(libro_idLabel);
        add(idField);
        add(tituloLabel);
        add(tituloField);
        add(directorLabel);
        add(directorField);
        add(duracionLabel);
        add(duracionField);
        add(generoLabel);
        add(generoField);
        add(dvdDispLabel);
        add(dvdDispField);
        add(addButton);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRevistaToDatabase();
            }
        });
    }
    private void addRevistaToDatabase() {
        String libro_id = idField.getText();
        String titulo = tituloField.getText();
        String director = directorField.getText();
        Float duracion = Float.parseFloat(duracionField.getText());
        String genero = generoField.getText();
        Integer dvdDisp = Integer.parseInt(dvdDispField.getText());


        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String insertSql = "INSERT INTO dvds (dvd_id,titulo,director,duracion,genero,dvdDisp) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, libro_id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, director);
            preparedStatement.setFloat(4, duracion);
            preparedStatement.setString(5, genero);
            preparedStatement.setInt(6, dvdDisp);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "DVD agregado correctamente.");
                if (callback != null) {
                    callback.onConfirm();
                }
                dispose(); // Close the window after adding the libro
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el DVD.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }


    }
    public void setCallback(New_dvd.ConfirmedDVD callback) {
        this.callback = callback;
    }

    public interface ConfirmedDVD {
        void onConfirm();
    }

}
