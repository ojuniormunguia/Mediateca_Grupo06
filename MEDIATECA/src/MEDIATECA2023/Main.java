package MEDIATECA2023;

import MEDIATECA2023.Añadir_editar.Edit_CD;
import MEDIATECA2023.Añadir_editar.New_cd;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Map;
import java.util.Vector;
import java.util.HashMap;
public class Main extends JFrame {
    private JPanel contentPanel;
    private DefaultTableModel tableModel;
    private JTable dataTable;

    public Main() {
        // Colores (temporalmente)
        Color primaryColor = new Color(34, 139, 34);
        Color secondaryColor = new Color(0, 102, 204);



        setTitle("Mediateca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        //botones
        JButton menuItem1 = createStyledButton("TODOS", primaryColor, Color.white);
        JButton menuItem2 = createStyledButton("DISPONIBLES", primaryColor, Color.white);
        JButton menuItem3 = createStyledButton("AGREGAR", primaryColor, Color.white);

        menuPanel.add(menuItem1);
        menuPanel.add(menuItem2);
        menuPanel.add(menuItem3);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Default content
        JLabel contentLabel = new JLabel("Elija el contenido a mostrar");
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(contentLabel, BorderLayout.CENTER);


        menuItem1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel subMenuPanel = new JPanel();
                subMenuPanel.setLayout(new BoxLayout(subMenuPanel, BoxLayout.Y_AXIS));

                JButton cdButton = createStyledButton("CD", secondaryColor, Color.white);
                JButton dvdButton = createStyledButton("DVD", secondaryColor, Color.white);
                JButton bookButton = createStyledButton("BOOK", secondaryColor, Color.white);
                JButton magazineButton = createStyledButton("MAGAZINE", secondaryColor, Color.white);

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
                JPanel subMenuPanel2 = new JPanel();
                subMenuPanel2.setLayout(new BoxLayout(subMenuPanel2, BoxLayout.Y_AXIS));

                JButton cdButton = createStyledButton("CD", secondaryColor, Color.white);
                JButton dvdButton = createStyledButton("DVD", secondaryColor, Color.white);
                JButton bookButton = createStyledButton("BOOK", secondaryColor, Color.white);
                JButton magazineButton = createStyledButton("MAGAZINE", secondaryColor, Color.white);

                subMenuPanel2.add(cdButton);
                subMenuPanel2.add(dvdButton);
                subMenuPanel2.add(bookButton);
                subMenuPanel2.add(magazineButton);

                cdButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        New_cd newCdWindow = new New_cd();
                        newCdWindow.setCallback(() -> {
                            displayTable("cd");
                        });
                        newCdWindow.setVisible(true);
                    }
                });
                setContentPanel(subMenuPanel2);
            }
        });

        add(menuPanel, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    public static DefaultTableModel buildTableModel(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();

        int columnCount = metaData.getColumnCount();
        Vector<String> columnNames = new Vector<>();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

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



        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/mediateca",
                    "root",
                    "Danibanani2619");
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            JFrame frame = new JFrame("Lista de " + tableName);
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            Color secondaryColor = new Color(255, 165, 0);

            JButton updateButton = createStyledButton("Editar", secondaryColor, Color.white);
            JButton deleteButton = createStyledButton("Borrar", secondaryColor, Color.white);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(updateButton);
            buttonPanel.add(deleteButton);

            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            contentPanel.revalidate();
            contentPanel.repaint();

            updateButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String id = (String) table.getValueAt(selectedRow, 0);
                    String titulo = (String) table.getValueAt(selectedRow, 1);
                    String artista = (String) table.getValueAt(selectedRow, 2);
                    String genero = (String) table.getValueAt(selectedRow, 3);
                    float duracion = (float) table.getValueAt(selectedRow, 4);
                    int numero = (int) table.getValueAt(selectedRow, 5);
                    int cdDisp = (int) table.getValueAt(selectedRow, 6);

                    Edit_CD editCdWindow = new Edit_CD(id, titulo, artista, genero, duracion, numero, cdDisp);
                    editCdWindow.setCallback(() -> {
                        displayTable("cd");
                    });
                    editCdWindow.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(frame, "Elija un item para editar");
                }
            });
            deleteButton.addActionListener(e -> {
                // Encontrar la llave primaria de cada tabla
                Map<String, String> tableNameToPrimaryKey = new HashMap<>();
                tableNameToPrimaryKey.put("cd", "cd_id");
                tableNameToPrimaryKey.put("dvds", "dvd_id");
                tableNameToPrimaryKey.put("revistas", "revistas_id");
                tableNameToPrimaryKey.put("libro", "libro_id");

                int selectedRow = table.getSelectedRow();
                DefaultTableModel model = (DefaultTableModel) table.getModel();

                if (selectedRow >= 0 && selectedRow < model.getRowCount()) {
                    String id = (String) model.getValueAt(selectedRow, 0);
                    try {
                        Statement statement1 = connection.createStatement();
                        String primaryKey = tableNameToPrimaryKey.get(tableName);


                        if (primaryKey != null){
                            statement1.executeUpdate("DELETE FROM " + tableName + " WHERE " + primaryKey + " = '" + id + "'");
                            // REFRESCA LA PÁGINA
                            DefaultTableModel newModel = buildTableModel(statement1.executeQuery("SELECT * FROM " + tableName));
                            table.setModel(newModel);
                            JOptionPane.showMessageDialog(frame, "Item borrado exitosamente.");}
                            //FIN DE REFRESCAR LA PÁGINA

                        else {
                            JOptionPane.showMessageDialog(frame, "La tabla seleccionada no es válida.");
                        }

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Elija un item para borrar");
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main();
        });
    }
}
