package MEDIATECA2023.Añadir_editar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit_CD extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField artistaField;
    private JTextField generoField;
    private JTextField duracionField;
    private JTextField numeroField;
    private JTextField dispField;
    private ConfirmedCD callback;

    private String originalId;
    private String originalTitulo;
    private String originalArtista;
    private String originalGenero;
    private double originalDuracion;
    private int originalNumero;
    private int originalDisp;


    public Edit_CD(String id, String titulo, String artista, String genero, float duracion, int numero, int disp) {
        setTitle("Editar CD");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        originalId = id;
        originalTitulo = titulo;
        originalArtista = artista;
        originalGenero = genero;
        originalDuracion = duracion;
        originalNumero = numero;
        originalDisp = disp;

        idField = new JTextField(id);
        tituloField = new JTextField(titulo);
        artistaField = new JTextField(artista);
        generoField = new JTextField(genero);
        duracionField = new JTextField(Float.toString(duracion));
        numeroField = new JTextField(Integer.toString(numero));
        dispField = new JTextField(Integer.toString(disp));

        JButton saveButton = new JButton("Guardar Cambios");

        add(idField);
        add(tituloField);
        add(artistaField);
        add(generoField);
        add(duracionField);
        add(numeroField);
        add(dispField);
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCdInDatabase();
            }
        });
    }

    private void updateCdInDatabase() {
        String id = idField.getText();
        String titulo = tituloField.getText();
        String artista = artistaField.getText();
        String genero = generoField.getText();
        double duracion = Double.parseDouble(duracionField.getText());
        int numero = Integer.parseInt(numeroField.getText());
        int disp = Integer.parseInt(dispField.getText());

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String updateSql = "UPDATE cd SET cd_id = ?, titulo = ?, artista = ?, genero = ?, duracion = ?, numero_canciones = ?, cdDisp = ? WHERE cd_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, artista);
            preparedStatement.setString(4, genero);
            preparedStatement.setDouble(5, duracion);
            preparedStatement.setInt(6, numero);
            preparedStatement.setInt(7, disp);
            preparedStatement.setString(8, originalId);

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

    public void setCallback(ConfirmedCD callback) {
        this.callback = callback;
    }

    public interface ConfirmedCD {
        void onConfirm();
    }
}
