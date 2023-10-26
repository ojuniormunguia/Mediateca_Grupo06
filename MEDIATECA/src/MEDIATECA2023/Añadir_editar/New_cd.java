package MEDIATECA2023.Añadir_editar;

import MEDIATECA2023.Añadir_editar.Edit_CD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class New_cd extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField artistaField;
    private JTextField generoField;
    private JTextField duracionField;
    private JTextField numeroField;
    private JTextField dispField;
    private Edit_CD.ConfirmedCD callback;

    public New_cd() {
        setTitle("Agregar una nueva revista");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        JLabel cd_idLabel = new JLabel("cd_id:");
        idField = new JTextField();
        JLabel tituloLabel = new JLabel("titulo:");
        tituloField = new JTextField();
        JLabel artistaLabel = new JLabel("artista:");
        artistaField = new JTextField();
        JLabel generoLabel = new JLabel("genero:");
        generoField = new JTextField();
        JLabel duracionLabel = new JLabel("duracion:");
        duracionField = new JTextField();
        JLabel numeroLabel = new JLabel("numero:");
        numeroField = new JTextField();
        JLabel cdDispLabel = new JLabel("cdDisp:");
        dispField = new JTextField();


        JButton addButton = new JButton("Agregar revista");

        // Add components to the frame
        add(cd_idLabel);
        add(idField);
        add(tituloLabel);
        add(tituloField);
        add(artistaLabel);
        add(artistaField);
        add(generoLabel);
        add(generoField);
        add(duracionLabel);
        add(duracionField);
        add(numeroLabel);
        add(numeroField);
        add(cdDispLabel);
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
        String cd_id = idField.getText();
        String titulo = tituloField.getText();
        String artista = artistaField.getText();
        String genero = generoField.getText();
        float duracion = Float.parseFloat(duracionField.getText());
        int numero = Integer.parseInt(numeroField.getText());
        int disp = Integer.parseInt(dispField.getText());

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String insertSql = "INSERT INTO cd (cd_id, titulo, artista, genero, duracion, numero_canciones, cdDisp) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, cd_id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, artista);
            preparedStatement.setString(4, genero);
            preparedStatement.setFloat(5, duracion);
            preparedStatement.setInt(6, numero);
            preparedStatement.setInt(7, disp);


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "revista agregada correctamente.");
                if (callback != null) {
                    callback.onConfirm();
                }
                dispose(); // Close the window after adding the CD
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el revista.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }


    }
    public void setCallback(Edit_CD.ConfirmedCD callback) {
        this.callback = callback;
    }

    public interface ConfirmedCD {
        void onConfirm();
    }

}
