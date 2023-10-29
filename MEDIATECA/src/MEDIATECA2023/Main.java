package MEDIATECA2023;

import MEDIATECA2023.Añadir_editar.*;
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
    public String permisos;
    public String identificador;


    public Main(String userId, String permissionType) {
        Connection connection = DatabaseConnection.getConnection();

        Login login = new Login();


        System.out.println(userId + " " + permissionType);
        permisos = permissionType;
        identificador = userId;

        Color primaryColor = new Color(34, 139, 34);
        Color secondaryColor = new Color(0, 102, 204);



        setTitle("Mediateca");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        //botones
        JButton todos = createStyledButton("TODOS", primaryColor, Color.white);
        JButton disponibles = createStyledButton("DISPONIBLES", primaryColor, Color.white);
        JButton agregar = createStyledButton("AGREGAR", primaryColor, Color.white);
        JButton rentado = createStyledButton("RENTADO", primaryColor, Color.white);

        menuPanel.add(todos);
        menuPanel.add(disponibles);
        menuPanel.add(agregar);
        menuPanel.add(rentado);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        // Contenido por defecto
        JLabel contentLabel = new JLabel("Elija el contenido a mostrar");
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPanel.add(contentLabel, BorderLayout.CENTER);


        todos.addActionListener(new ActionListener() {
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

        //PRESIONA "DISPONIBLES"
        disponibles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableTable("cd");

            }
        });
        //PRESIONA AGREGAR
        agregar.addActionListener(new ActionListener() {
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

                //boton de agregar cd
                cdButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        New_cd newCdWindow = new New_cd();
                        newCdWindow.setCallback(() -> {
                            displayTable("cd");
                        });
                        newCdWindow.setVisible(true);
                    }
                });

                //boton de agregar dvd
                magazineButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        New_revista newRevistaWindow = new New_revista();
                        newRevistaWindow.setCallback(() -> {
                            displayTable("revistas");
                        });
                        newRevistaWindow.setVisible(true);
                    }
                });

                //boton de agregar libro
                bookButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        New_libro newLibroWindow = new New_libro();
                        newLibroWindow.setCallback(() -> {
                            displayTable("libro");
                        });
                        newLibroWindow.setVisible(true);
                    }
                });

                //boton de agregar dvd
                dvdButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        New_dvd newDvdWindow = new New_dvd();
                        newDvdWindow.setCallback(() -> {
                            displayTable("dvds");
                        });
                        newDvdWindow.setVisible(true);
                    }
                });
                setContentPanel(subMenuPanel2);
            }
        });

        //RENTADOS
        rentado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRentedTable("cd");

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
    //MUESTRA TABLA CON TODO EL CONTENIDO
    private void displayTable(String tableName) {
        try {
            Connection connection = DatabaseConnection.getConnection();

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


    //MUESTRA TABLA CON EL CONTENIDO DISPONIBLE



    private void displayAvailableTable(String tableName) {
        ColectorDeColumnas mapper = new ColectorDeColumnas();

        try {
            Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            String dispColumn = mapper.getDispColumn(tableName);
            String primaryKey = mapper.getPrimaryKey(tableName);
            String userKey;
            if (permisos == "adm_"){
                userKey = "admin_id";
            }
            else{
                userKey = "socio_id";
            }
            String rentedItemsQuery = "SELECT " + primaryKey + " FROM " + permisos + tableName + " WHERE " + userKey + " = '" + identificador + "'";
            String availableItemsQuery = "SELECT * FROM " + tableName + " WHERE " + dispColumn + " > 0 AND " + primaryKey + " NOT IN (" + rentedItemsQuery + ")";
            ResultSet resultSet = statement.executeQuery(availableItemsQuery);


            JFrame frame = new JFrame("Lista de " + tableName);
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            Color secondaryColor = new Color(255, 165, 0);

            JButton rentButton = createStyledButton("Rentar", secondaryColor, Color.white);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(rentButton);

            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            contentPanel.revalidate();
            contentPanel.repaint();



            rentButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();

                int lastColumnIndex = table.getColumnCount() - 1;

                if (selectedRow != -1) {
                    String id = (String) table.getValueAt(selectedRow, 0);
                    int disp = (int) table.getValueAt(selectedRow,lastColumnIndex);
                    if (disp > 0) {
                        try {
                            String sqlQuery = "UPDATE " + tableName + " SET " + dispColumn + " = " + (disp - 1) + " WHERE " + primaryKey + " = '" + id + "'";
                            System.out.println("SQL Query: " + sqlQuery);
                            Statement updateStatement = connection.createStatement();
                            updateStatement.executeUpdate("UPDATE " + tableName + " SET " + dispColumn + " = " + (disp - 1) + " WHERE " + primaryKey + " = '" + id + "'");
                            updateStatement.executeUpdate("INSERT INTO " + permisos + tableName + " (" + userKey + ", " + primaryKey + ") VALUES ('" + identificador + "', '" + id + "')");
                            System.out.println("INSERT INTO " + permisos + tableName + " (" + userKey + ", " + primaryKey + ") VALUES ('" + identificador + "', '" + id + "')");
                            displayAvailableTable(tableName);




                            updateStatement.executeUpdate(sqlQuery);
                            JOptionPane.showMessageDialog(frame, "Elemento rentado exitosamente.");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "No hay elementos disponibles para rentar.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Elija un elemento para rentar.");
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    //TABLA DE RENTADOS
    private void displayRentedTable(String tableName) {
        Connection connection = DatabaseConnection.getConnection();

        Login login = new Login();
        ColectorDeColumnas mapper = new ColectorDeColumnas();
        try {
            Statement statement = connection.createStatement();
            //encontrar Disp y PrimaryKey
            String dispColumn = mapper.getDispColumn(tableName);
            String primaryKey = mapper.getPrimaryKey(tableName);
            String joined = mapper.getJoin(tableName);
            String userKey;
            if (permisos == "adm_"){
                userKey = "admin_id";
            }
            else{
                userKey = "socio_id";
            }


            //Seleccionar todo lo rentado por el usuario
            String test = "SELECT " + tableName + ".* FROM " + tableName + " INNER JOIN " + permisos + joined + " ON " + tableName + "." + primaryKey + " = " + permisos + tableName + "." + primaryKey + " WHERE " + permisos + joined + "." + userKey + " = '" + identificador + "'";
            System.out.println(test);
            ResultSet resultSet = statement.executeQuery("SELECT " + tableName + ".* FROM " + tableName + " INNER JOIN " + permisos + joined + " ON " + tableName + "." + primaryKey + " = " + permisos + joined + "." + primaryKey + " WHERE " + permisos + joined + "." + userKey + " = '" + identificador + "'");

            JFrame frame = new JFrame("Lista de " + tableName);
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            contentPanel.removeAll();
            contentPanel.add(scrollPane, BorderLayout.CENTER);

            Color secondaryColor = new Color(255, 165, 0);

            JButton returnButton = createStyledButton("Devolver", secondaryColor, Color.white);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(returnButton);

            contentPanel.add(buttonPanel, BorderLayout.SOUTH);

            contentPanel.revalidate();
            contentPanel.repaint();



            returnButton.addActionListener(e -> {
                int selectedRow = table.getSelectedRow();

                int lastColumnIndex = table.getColumnCount() - 1;

                if (selectedRow != -1) {
                    String id = (String) table.getValueAt(selectedRow, 0);
                    int disp = (int) table.getValueAt(selectedRow,lastColumnIndex);
                    if (disp > 0) {
                        try {
                            //INSERT INTO adm_cd (admin_id, cd_id) VALUES (1, 1);
                            String sqlQuery = "DELETE FROM " + permisos + tableName + " WHERE " + userKey + " = " + identificador + " AND " + primaryKey + " = " + id;
                            //System.out.println("SQL Query: " + sqlQuery);
                            Statement updateStatement = connection.createStatement();
                            updateStatement.executeUpdate("UPDATE " + tableName + " SET " + dispColumn + " = " + (disp + 1) + " WHERE " + primaryKey + " = '" + id + "'");
                            updateStatement.executeUpdate("DELETE FROM " + permisos + tableName + " WHERE " + userKey + " = '" + identificador + "' AND " + primaryKey + " = '" + id + "'");
                            displayRentedTable(tableName);

                            JOptionPane.showMessageDialog(frame, "Elemento regresado exitosamente.");
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "No hay elementos disponibles para devolver.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Elija un elemento para devolver.");
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            Connection connection = DatabaseConnection.getConnection();

            Login login = new Login();

            // LOS MANDA A INICIO DE SESION

            JFrame frame = new JFrame("Login");

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


            frame.add(login.main());

            frame.setSize(400, 200);
            frame.setVisible(true);
        });
    }

}