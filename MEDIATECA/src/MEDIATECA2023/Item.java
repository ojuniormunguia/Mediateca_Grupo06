package MEDIATECA2023;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Item {
    protected JTextField idField;
    protected JTextField tituloField;
    protected JTextField editorialField;
    protected JTextField yearPublicField;
    protected JTextField dispField;
    //Comunes actuales: id, titulo, editorial, yearPublic, disp
    public Item() {
        idField = new JTextField();
        tituloField = new JTextField();
        editorialField = new JTextField();
        yearPublicField = new JTextField();
        dispField = new JTextField();
    }

    public void addComponentsToFrame(JFrame frame) {
        frame.add(idField);
        frame.add(tituloField);
        frame.add(editorialField);
        frame.add(yearPublicField);
        frame.add(dispField);
    }
    //métodos comunes...
}
