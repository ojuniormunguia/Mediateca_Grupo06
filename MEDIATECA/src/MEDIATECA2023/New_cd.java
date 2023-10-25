package MEDIATECA2023;

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


    public New_cd() {
        setTitle("Agregar un nuevo CD");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        JLabel titleLabel = new JLabel("cd_id:");
        idField = new JTextField();
        JLabel artistLabel = new JLabel("titulo:");
        tituloField = new JTextField();
        JLabel yearLabel = new JLabel("artista:");
        artistaField = new JTextField();
        JLabel genreLabel = new JLabel("genero:");
        generoField = new JTextField();
        JLabel durationLabel = new JLabel("duracion:");
        duracionField = new JTextField();
        JLabel songsLabel = new JLabel("numero_canciones:");
        numeroField = new JTextField();
        JLabel cdDispLabel = new JLabel("cdDisp:");
        dispField = new JTextField();

        JButton addButton = new JButton("Agregar CD");

        // Add components to the frame
        add(idField);
        add(tituloField);
        add(artistaField);
        add(generoField);
        add(duracionField);
        add(numeroField);
        add(dispField);
        add(addButton);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCdToDatabase();
            }
        });
    }

    private void addCdToDatabase() {
        String id = idField.getText();
        String titulo = tituloField.getText();
        String artista = artistaField.getText();
        String genero = generoField.getText();
        Double duracion = Double.parseDouble(duracionField.getText());
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
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, artista);
            preparedStatement.setString(4, genero);
            preparedStatement.setDouble(5, duracion);
            preparedStatement.setInt(6, numero);
            preparedStatement.setInt(7, disp);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "CD agregado correctamente.");
                dispose(); // Close the window after adding the CD
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el CD.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new New_cd().setVisible(true);
        });
    }
}
