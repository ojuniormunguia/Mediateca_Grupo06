package MEDIATECA2023.Añadir_editar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class New_libro extends JFrame {
    private JTextField idField;
    private JTextField tituloField;
    private JTextField autorField;
    private JTextField paginaField;
    private JTextField editorialField;
    private JTextField isbnField;
    private JTextField yearPublicField;
    private JTextField librosDispField;
    private New_libro.ConfirmedLibro callback;
    public New_libro() {
        setTitle("Agregar una nueva revista");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        JLabel libro_idLabel = new JLabel("id:");
        idField = new JTextField();
        JLabel tituloLabel = new JLabel("titulo:");
        tituloField = new JTextField();
        JLabel autorLabel = new JLabel("autor:");
        autorField = new JTextField();
        JLabel paginaLabel = new JLabel("paginas:");
        paginaField = new JTextField();
        JLabel editorialLabel = new JLabel("editorial:");
        editorialField = new JTextField();
        JLabel isbnLabel = new JLabel("isbn:");
        isbnField = new JTextField();
        JLabel yearPublicLabel = new JLabel("yearPublic:");
        yearPublicField = new JTextField();
        JLabel librosDispLabel = new JLabel("librosDisp:");
        librosDispField = new JTextField();



        JButton addButton = new JButton("Agregar revista");

        // Add components to the frame
        add(libro_idLabel);
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
        String autor = autorField.getText();
        String pagina = paginaField.getText();
        String editorial = editorialField.getText();
        String isbn = isbnField.getText();
        String yearPublic = yearPublicField.getText();
        int librosDisp = Integer.parseInt(librosDispField.getText());


        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619"
            );

            String insertSql = "INSERT INTO libro (libro_id,titulo,autor,paginas,editorial,isbn,yearPublic,librosDisp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSql);
            preparedStatement.setString(1, libro_id);
            preparedStatement.setString(2, titulo);
            preparedStatement.setString(3, autor);
            preparedStatement.setString(4, pagina);
            preparedStatement.setString(5, editorial);
            preparedStatement.setString(6, isbn);
            preparedStatement.setString(7, yearPublic);
            preparedStatement.setInt(8, librosDisp);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "revista agregada correctamente.");
                if (callback != null) {
                    callback.onConfirm();
                }
                dispose(); // Close the window after adding the libro
            } else {
                JOptionPane.showMessageDialog(this, "Error al agregar el revista.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.");
        }


    }
    public void setCallback(New_libro.ConfirmedLibro callback) {
        this.callback = callback;
    }

    public interface ConfirmedLibro {
        void onConfirm();
    }

}
