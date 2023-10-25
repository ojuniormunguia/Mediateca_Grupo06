package MEDIATECA2023;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class Main extends JFrame {
    private JPanel contentPanel;
    private DefaultTableModel tableModel;
    private JTable dataTable;

    public Main() {
        setTitle("Mediateca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        JButton menuItem1 = new JButton("TODOS");
        JButton menuItem2 = new JButton("DISPONIBLES");
        JButton menuItem3 = new JButton("AGREGAR");

        menuPanel.add(menuItem1);
        menuPanel.add(menuItem2);
        menuPanel.add(menuItem3);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        // Default content
        JLabel contentLabel = new JLabel("Elija el contenido a mostrar");
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(contentLabel, BorderLayout.CENTER);

        // LISTENERS
        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add a JPanel submenu for 4 buttons
                JPanel subMenuPanel = new JPanel();
                subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));

                JButton cdButton = new JButton("CD");
                JButton dvdButton = new JButton("DVD");
                JButton bookButton = new JButton("BOOK");
                JButton magazineButton = new JButton("MAGAZINE");

                subMenuPanel.add(cdButton);
                subMenuPanel.add(dvdButton);
                subMenuPanel.add(bookButton);
                subMenuPanel.add(magazineButton);

                cdButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        displayTable("cd");
                    }
                });

                dvdButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        displayTable("dvds");
                    }
                });

                bookButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        displayTable("libro");
                    }
                });

                magazineButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        displayTable("revistas");
                    }
                });

                setContentPanel(subMenuPanel);
            }
        });

        menuItem2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel newContentLabel = new JLabel("ITEMS DISPONIBLES");
                newContentLabel.setHorizontalAlignment(JLabel.CENTER);
                setContentPanel(newContentLabel);
            }
        });

        menuItem3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JLabel newContentLabel = new JLabel("AGREGAR");
                newContentLabel.setHorizontalAlignment(JLabel.CENTER);
                setContentPanel(newContentLabel);
            }
        });

        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    // Convert ResultSet to DefaultTableModel
    public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        // Get the column names
        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // Get the data
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) {
            Vector<Object> row = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                row.add(resultSet.getObject(columnIndex));
            }
            data.add(row);
        }

        return new DefaultTableModel(data, columnNames);
    }

    private void setContentPanel(Component newContent) {
        contentPanel.removeAll();
        contentPanel.add(newContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void displayTable(String tableName) {
        // Display the table for the given table name
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            // Create a JTable and set it as the content
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void displayPlaceholderLabel(String label) {
        contentPanel.removeAll();
        JLabel placeholderLabel = new JLabel("Placeholder for " + label);
        placeholderLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(placeholderLabel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }
}