package MEDIATECA2023.Añadir_editar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit_Revista extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField editorField;
    private JTextField periocidadField;
    private JTextField yearPublicField;
    private JTextField revistasDispField;
    private New_revista.ConfirmedRevista callback;

    private String originalId;
    private String originalTitulo;
    private String originalEditorial;
    private String originalPeriocidad;
    private String originalYearPublic;
    private int originalRevistasDisp;



    public Edit_Revista(String id, String titulo, String editorial, String periocidad, String yearPublic, int revistasDisp) {
        setTitle("Editar revista");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(8, 1));

        originalId = id;
        originalTitulo = titulo;
        originalEditorial = editorial;
        originalPeriocidad = periocidad;
        originalYearPublic = yearPublic;
        originalRevistasDisp = revistasDisp;

        JLabel cd_idLabel = new JLabel("id:");
        idField = new JTextField(id);
        JLabel tituloLabel = new JLabel("titulo:");
        tituloField = new JTextField(titulo);
        JLabel editorialLabel = new JLabel("editorial:");
        editorField = new JTextField(editorial);
        JLabel periocidadLabel = new JLabel("periocidad:");
        periocidadField = new JTextField(periocidad);
        JLabel yearPublicLabel = new JLabel("yearPublic:");
        yearPublicField = new JTextField(yearPublic);
        JLabel revistasDispLabel = new JLabel("revistas disponibles:");
        revistasDispField = new JTextField(Integer.toString(revistasDisp));

        JButton saveButton = new JButton("Guardar Cambios");

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
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateCdInDatabase();
            }
        });
    }

    private void updateCdInDatabase() {
        String revistas_id = idField.getText();
        String titulo = tituloField.getText();
        String editorial = editorField.getText();
        String periocidad = periocidadField.getText();
        String yearPublic = yearPublicField.getText();
        String revistasDisp = revistasDispField.getText();

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String updateSql = "UPDATE revistas SET revistas_id = ?, titulo = ?, editorial = ?, periocidad = ?, yearPublic = ?, revistasDisp = ? WHERE revistas_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, revistas_id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, editorial);
            preparedStatement.setString(4, periocidad);
            preparedStatement.setString(5, yearPublic);
            preparedStatement.setString(6, revistasDisp);
            preparedStatement.setString(7, originalId);

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

    public void setCallback(New_revista.ConfirmedRevista callback) {
        this.callback = callback;
    }

    public interface ConfirmedRevista {
        void onConfirm();
    }
}
