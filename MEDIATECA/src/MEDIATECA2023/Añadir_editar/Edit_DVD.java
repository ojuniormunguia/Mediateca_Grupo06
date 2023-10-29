package MEDIATECA2023.Añadir_editar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit_DVD extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField directorField;
    private JTextField duracionField;
    private JTextField generoField;
    private JTextField dvdDispField;
    private New_dvd.ConfirmedDVD callback;

    private String originalId;
    private String originalTitulo;
    private String originalDirector;
    private double originalDuracion;
    private String originalGenero;
    private int originalDvdDisp;

    public Edit_DVD(String id, String titulo, String director, double duracion, String genero, int dvdDisp) {
        setTitle("Editar CD");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        originalId = id;
        originalTitulo = titulo;
        originalDirector = director;
        originalDuracion = duracion;
        originalGenero = genero;
        originalDvdDisp = dvdDisp;

        JLabel dvd_idLabel = new JLabel("id:");
        idField = new JTextField(id);
        JLabel tituloLabel = new JLabel("titulo:");
        tituloField = new JTextField(titulo);
        JLabel directorLabel = new JLabel("director:");
        directorField = new JTextField(director);
        JLabel duracionLabel = new JLabel("duracion:");
        duracionField = new JTextField(Double.toString(duracion));
        JLabel generoLabel = new JLabel("genero:");
        generoField = new JTextField(genero);
        JLabel dvdDispLabel = new JLabel("dvds disponibles:");
        dvdDispField = new JTextField(Integer.toString(dvdDisp));


        JButton saveButton = new JButton("Guardar Cambios");

        add(dvd_idLabel);
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
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDvdInDatabase();
            }
        });
    }

    private void updateDvdInDatabase() {
        String id = idField.getText();
        String titulo = tituloField.getText();
        String director = directorField.getText();
        double duracion = Double.parseDouble(duracionField.getText());
        String genero = generoField.getText();
        int dvdDisp = Integer.parseInt(dvdDispField.getText());

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String updateSql = "UPDATE dvds SET dvd_id = ?, titulo = ?, director = ?, genero = ?, dvdDisp = ? WHERE dvd_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, director);
            preparedStatement.setString(4, genero);
            preparedStatement.setInt(5, dvdDisp);
            preparedStatement.setString(6, originalId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Cambios guardados correctamente.");
                if (callback != null) {
                    callback.onConfirm();
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar los cambios.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }
    }

    public void setCallbackdvd(New_dvd.ConfirmedDVD callback) {
        this.callback = callback;
    }

}
