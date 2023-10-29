package MEDIATECA2023.Añadir_editar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Edit_Libro extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField autorField;
    private JTextField paginaField;
    private JTextField editorialField;
    private JTextField isbnField;
    private JTextField yearPublicField;
    private JTextField librosDispField;
    private New_libro.ConfirmedLibro callback;

    private String originalId;
    private String originalTitulo;
    private String originalAutor;
    private int originalPagina;
    private String originalEditorial;
    private String originalIsbn;
    private String originalYearPublic;
    private int originalLibrosDisp;

    public Edit_Libro(String id, String titulo, String autor, int pagina, String editorial, String isbn, String yearPublic, int librosDisp) {
        setTitle("Editar libro");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(12, 2));

        JLabel cd_idLabel = new JLabel("id:");
        originalId = id;
        JLabel tituloLabel = new JLabel("titulo:");
        originalTitulo = titulo;
        JLabel autorLabel = new JLabel("autor:");
        originalAutor = autor;
        JLabel paginaLabel = new JLabel("paginas:");
        originalPagina = pagina;
        JLabel editorialLabel = new JLabel("editorial:");
        originalEditorial = editorial;
        JLabel isbnLabel = new JLabel("isbn:");
        originalIsbn = isbn;
        JLabel yearPublicLabel = new JLabel("yearPublic:");
        originalYearPublic = yearPublic;
        JLabel librosDispLabel = new JLabel("libros disponibles:");
        originalLibrosDisp = librosDisp;


        idField = new JTextField(id);
        tituloField = new JTextField(titulo);
        autorField = new JTextField(autor);
        paginaField = new JTextField(Integer.toString(pagina));
        editorialField = new JTextField(editorial);
        isbnField = new JTextField(isbn);
        yearPublicField = new JTextField(yearPublic);
        librosDispField = new JTextField(Integer.toString(librosDisp));

        JButton saveButton = new JButton("Guardar Cambios");

        add(cd_idLabel);
        add(idField);
        add(tituloLabel);
        add(tituloField);
        add(autorLabel);
        add(autorField);
        add(paginaLabel);
        add(paginaField);
        add(editorialLabel);
        add(editorialField);
        add(isbnLabel);
        add(isbnField);
        add(yearPublicLabel);
        add(yearPublicField);
        add(librosDispLabel);
        add(librosDispField);
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateLibroInDatabase();
            }
        });
    }

    private void updateLibroInDatabase() {
        String id = idField.getText();
        String titulo = tituloField.getText();
        String autor = autorField.getText();
        int pagina = Integer.parseInt(paginaField.getText());
        String editorial = editorialField.getText();
        String isbn = isbnField.getText();
        int yearPublic = Integer.parseInt(yearPublicField.getText());
        int librosDisp = Integer.parseInt(librosDispField.getText());

        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String updateSql = "UPDATE libro SET libro_id = ?, titulo = ?, autor = ?, paginas = ?, editorial = ?, isbn = ?, yearPublic = ?, librosDisp = ? WHERE libro_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateSql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, autor);
            preparedStatement.setInt(4, pagina);
            preparedStatement.setString(5, editorial);
            preparedStatement.setString(6, isbn);
            preparedStatement.setInt(7, yearPublic);
            preparedStatement.setInt(8, librosDisp);
            preparedStatement.setString(9, originalId);

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

    public void setCallbacklibro(New_libro.ConfirmedLibro callback) {
        this.callback = callback;
    }

    public interface ConfirmedLibro {
        void onConfirm();
    }
}
