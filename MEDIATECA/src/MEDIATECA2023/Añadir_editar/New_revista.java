package MEDIATECA2023.Añadir_editar;

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
    private JTextField editorField;
    private JTextField periocidadField;
    private JTextField yearPublicField;
    private JTextField revistasDispField;
    private New_revista.ConfirmedRevista callback;
    public New_revista() {
        setTitle("Agregar una nueva revista");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        JLabel cd_idLabel = new JLabel("id:");
        idField = new JTextField();
        JLabel tituloLabel = new JLabel("titulo:");
        tituloField = new JTextField();
        JLabel editorialLabel = new JLabel("editorial:");
        editorField = new JTextField();
        JLabel periocidadLabel = new JLabel("periocidad:");
        periocidadField = new JTextField();
        JLabel yearPublicLabel = new JLabel("yearPublic:");
        yearPublicField = new JTextField();
        JLabel revistasDispLabel = new JLabel("revistasDisp:");
        revistasDispField = new JTextField();


        JButton addButton = new JButton("Agregar revista");

        // Add components to the frame
        add(cd_idLabel);
        add(idField);
        add(tituloLabel);
        add(tituloField);
        add(editorialLabel);
        add(editorField);
        add(periocidadLabel);
        add(periocidadField);
        add(yearPublicLabel);
        add(yearPublicField);
        add(revistasDispLabel);
        add(revistasDispField);
        add(addButton);


        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addRevistaToDatabase();
            }
        });
    }
    private void addRevistaToDatabase() {
        String revistas_id = idField.getText();
        String titulo = tituloField.getText();
        String editorial = editorField.getText();
        String periocidad = periocidadField.getText();
        String yearPublic = yearPublicField.getText();
        Integer revistasDisp = Integer.parseInt(revistasDispField.getText());

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String insertSql = "INSERT INTO revistas (revistas_id, titulo, editorial, periocidad, yearPublic, revistasDisp) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, revistas_id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, editorial);
            preparedStatement.setString(4, periocidad);
            preparedStatement.setString(5, yearPublic);
            preparedStatement.setInt(6, revistasDisp);


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
    public void setCallback(New_revista.ConfirmedRevista callback) {
        this.callback = callback;
    }

    public interface ConfirmedRevista {
        void onConfirm();
    }

}
