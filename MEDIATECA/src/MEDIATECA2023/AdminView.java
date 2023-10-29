package MEDIATECA2023;

import MEDIATECA2023.Añadir_editar.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import java.util.Map;
import java.util.HashMap;

public class AdminView extends JFrame {
    private JPanel contentPanel;
    public String permisos;
    public String identificador;


    public AdminView(String permissionType, String userId) {
        JFrame frame = new JFrame("MEDIATECA 2023");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        permisos = permissionType;
        identificador = userId;

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu verMenu = new JMenu("Ver todos");
        menuBar.add(verMenu);

        JMenuItem cdTodo = new JMenuItem("CDs");
        verMenu.add(cdTodo);

        JMenuItem dvdTodo = new JMenuItem("DVDs");
        verMenu.add(dvdTodo);

        JMenuItem revistaTodo = new JMenuItem("Revistas");
        verMenu.add(revistaTodo);

        JMenuItem libroTodo = new JMenuItem("Libros");
        verMenu.add(libroTodo);

        JMenu addMenu = new JMenu("Agregar");
        menuBar.add(addMenu);

        JMenuItem cdAdd = new JMenuItem("CD");
        addMenu.add(cdAdd);

        JMenuItem dvdAdd = new JMenuItem("DVD");
        addMenu.add(dvdAdd);

        JMenuItem revistaAdd = new JMenuItem("Revista");
        addMenu.add(revistaAdd);

        JMenuItem libroAdd = new JMenuItem("Libro");
        addMenu.add(libroAdd);

        JMenu dispMenu = new JMenu("Disponibles");
        menuBar.add(dispMenu);

        JMenuItem cdDisponibles = new JMenuItem("CDs");
        dispMenu.add(cdDisponibles);

        JMenuItem dvdDisponibles = new JMenuItem("DVDs");
        dispMenu.add(dvdDisponibles);

        JMenuItem revistaDisponibles = new JMenuItem("Revistas");
        dispMenu.add(revistaDisponibles);

        JMenuItem libroDisponibles = new JMenuItem("Libros");
        dispMenu.add(libroDisponibles);

        JMenu rentedMenu = new JMenu("Rentados");
        menuBar.add(rentedMenu);

        JMenuItem cdRentados = new JMenuItem("CDs");
        rentedMenu.add(cdRentados);

        JMenuItem dvdRentados = new JMenuItem("DVDs");
        rentedMenu.add(dvdRentados);

        JMenuItem revistaRentados = new JMenuItem("Revistas");
        rentedMenu.add(revistaRentados);

        JMenuItem libroRentados = new JMenuItem("Libros");
        rentedMenu.add(libroRentados);

        contentPanel = new JPanel();

        contentPanel.setLayout(new BorderLayout());
        frame.add(contentPanel, BorderLayout.CENTER);
        //al seleccionar todos los cds
        cdTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTable("cd");
            }
        });

        dvdTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTable("dvds");
            }
        });

        libroTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTable("libro");
            }
        });

        revistaTodo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayTable("revistas");
            }
        });

        //al seleccionar agregar cd

        cdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                New_cd newCdWindow = new New_cd();
                newCdWindow.setCallback(() -> {
                    displayTable("cd");
                });
                newCdWindow.setVisible(true);
            }
        });

        dvdAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                New_dvd newDvdWindow = new New_dvd();
                newDvdWindow.setCallback(() -> {
                    displayTable("dvds");
                });
                newDvdWindow.setVisible(true);
            }
        });

        libroAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                New_libro newLibroWindow = new New_libro();
                newLibroWindow.setCallback(() -> {
                    displayTable("libro");
                });
                newLibroWindow.setVisible(true);
            }
        });

        revistaAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                New_revista newRevistaWindow = new New_revista();
                newRevistaWindow.setCallback(() -> {
                    displayTable("revistas");
                });
                newRevistaWindow.setVisible(true);
            }
        });

        //al seleccionar disponibles

        cdDisponibles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableTable("cd");
            }
        });

        dvdDisponibles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableTable("dvds");
            }
        });

        libroDisponibles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableTable("libro");
            }
        });

        revistaDisponibles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAvailableTable("revistas");
            }
        });

        //al seleccionar rentados

        cdRentados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRentedTable("cd");
            }
        });

        dvdRentados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRentedTable("dvds");
            }
        });

        libroRentados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRentedTable("libro");
            }
        });

        revistaRentados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRentedTable("revistas");
            }
        });



        frame.setVisible(true);
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

    private JTextField searchField;
    private JButton searchButton;
    private TableRowSorter<DefaultTableModel> rowSorter;

    //MUESTRA TABLA CON TODO EL CONTENIDO
    private void displayTable(String tableName) {
        try {
            Connection connection = DatabaseConnection.getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);

            JFrame frame = new JFrame("Lista de " + tableName);
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            rowSorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowSorter);

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

                    switch (tableName){
                        case "cd":
                            selectedRow = table.getSelectedRow();

                            String cd_id = (String) table.getValueAt(selectedRow, 0);
                            String cd_titulo = (String) table.getValueAt(selectedRow, 1);
                            String cd_artista = (String) table.getValueAt(selectedRow, 2);
                            String cd_genero = (String) table.getValueAt(selectedRow, 3);
                            Double cd_duracion = (Double) table.getValueAt(selectedRow, 4);
                            int cd_numero = (int) table.getValueAt(selectedRow, 5);
                            int cdDisp = (int) table.getValueAt(selectedRow, 6);

                            Edit_CD editCdWindow = new Edit_CD(cd_id, cd_titulo, cd_artista, cd_genero, cd_duracion, cd_numero, cdDisp);
                            editCdWindow.setCallback(() -> {
                                displayTable("cd");
                            });
                            editCdWindow.setVisible(true);
                            break;
                        case "dvds":
                            selectedRow = table.getSelectedRow();

                            String dvd_id = (String) table.getValueAt(selectedRow, 0);
                            String dvd_titulo = (String) table.getValueAt(selectedRow, 1);
                            String dvd_director = (String) table.getValueAt(selectedRow, 2);
                            double dvd_duracion = (Double) table.getValueAt(selectedRow, 3);
                            String dvd_genero = (String) table.getValueAt(selectedRow, 4);
                            int dvdDisp = (int) table.getValueAt(selectedRow, 5);

                            Edit_DVD editDvdWindow = new Edit_DVD(dvd_id, dvd_titulo, dvd_director, dvd_duracion, dvd_genero, dvdDisp);
                            editDvdWindow.setCallbackdvd(() -> {
                                displayTable("dvds");
                            });
                            editDvdWindow.setVisible(true);
                            break;
                        case "libro":
                            selectedRow = table.getSelectedRow();

                            String libro_id = (String) table.getValueAt(selectedRow, 0);
                            String libro_titulo = (String) table.getValueAt(selectedRow, 1);
                            String libro_autor = (String) table.getValueAt(selectedRow, 2);
                            int libro_pagina = (int) table.getValueAt(selectedRow, 3);
                            String libro_editorial = (String) table.getValueAt(selectedRow, 4);
                            String libro_isbn = (String) table.getValueAt(selectedRow, 5);
                            String libro_yearPublic = (String) table.getValueAt(selectedRow, 6);
                            int librosDisp = (int) table.getValueAt(selectedRow, 7);

                            Edit_Libro editLibroWindow = new Edit_Libro(libro_id, libro_titulo, libro_autor, libro_pagina, libro_editorial, libro_isbn, libro_yearPublic, librosDisp);
                            editLibroWindow.setCallbacklibro(() -> {
                                displayTable("libro");
                            });
                            editLibroWindow.setVisible(true);
                            break;
                        case "revistas":
                            selectedRow = table.getSelectedRow();

                            String revistas_id = (String) table.getValueAt(selectedRow, 0);
                            String revistas_titulo = (String) table.getValueAt(selectedRow, 1);
                            String revistas_editorial = (String) table.getValueAt(selectedRow, 2);
                            String revistas_periocidad = (String) table.getValueAt(selectedRow, 3);
                            String revistas_yearPublic = (String) table.getValueAt(selectedRow, 4);
                            int revistasDisp = (int) table.getValueAt(selectedRow, 5);
                            Edit_Revista editRevistaWindow = new Edit_Revista(revistas_id, revistas_titulo, revistas_editorial, revistas_periocidad, revistas_yearPublic, revistasDisp);
                            editRevistaWindow.setCallback(() -> {
                                displayTable("revistas");
                            });
                            editRevistaWindow.setVisible(true);
                            break;
                        default:
                            JOptionPane.showMessageDialog(frame, "La tabla seleccionada no es válida.");
                            break;
                    }
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
        searchField = new JTextField();
        searchButton = new JButton("Buscar");
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });

    }
    private void filterTable() {
        if (rowSorter != null) {
            RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchField.getText(), 0, 1, 2, 3);
            rowSorter.setRowFilter(rowFilter);
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
            String joined = mapper.getJoin(tableName);
            String userKey;
            if (permisos.equals("adm_")){
                userKey = "admin_id";
            }
            else{
                userKey = "socio_id";
            }
            String rentedItemsQuery = "SELECT " + primaryKey + " FROM " + permisos + joined + " WHERE " + userKey + " = '" + identificador + "'";
            String availableItemsQuery = "SELECT * FROM " + tableName + " WHERE " + dispColumn + " > 0 AND " + primaryKey + " NOT IN (" + rentedItemsQuery + ")";
            ResultSet resultSet = statement.executeQuery(availableItemsQuery);


            JFrame frame = new JFrame("Lista de " + tableName);
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            rowSorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowSorter);

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
                            updateStatement.executeUpdate("INSERT INTO " + permisos + joined + " (" + userKey + ", " + primaryKey + ") VALUES ('" + identificador + "', '" + id + "')");
                            System.out.println("INSERT INTO " + permisos + joined + " (" + userKey + ", " + primaryKey + ") VALUES ('" + identificador + "', '" + id + "')");
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
        searchField = new JTextField();
        searchButton = new JButton("Buscar");
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
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
            if (permisos.equals("adm_")){
                userKey = "admin_id";
            }
            else{
                userKey = "socio_id";
            }


            //Seleccionar todo lo rentado por el usuario
            String test = "SELECT " + tableName + ".* FROM " + tableName + " INNER JOIN " + permisos + joined + " ON " + tableName + "." + primaryKey + " = " + permisos + joined + "." + primaryKey + " WHERE " + permisos + joined + "." + userKey + " = '" + identificador + "'";
            System.out.println(test);
            ResultSet resultSet = statement.executeQuery("SELECT " + tableName + ".* FROM " + tableName + " INNER JOIN " + permisos + joined + " ON " + tableName + "." + primaryKey + " = " + permisos + joined + "." + primaryKey + " WHERE " + permisos + joined + "." + userKey + " = '" + identificador + "'");

            JFrame frame = new JFrame("Lista de " + tableName);
            JTable table = new JTable(buildTableModel(resultSet));
            JScrollPane scrollPane = new JScrollPane(table);
            rowSorter = new TableRowSorter<>((DefaultTableModel) table.getModel());
            table.setRowSorter(rowSorter);

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
                    if (disp > -1) {
                        try {
                            //INSERT INTO adm_cd (admin_id, cd_id) VALUES (1, 1);
                            String sqlQuery = "DELETE FROM " + permisos + joined + " WHERE " + userKey + " = " + identificador + " AND " + primaryKey + " = " + id;
                            //System.out.println("SQL Query: " + sqlQuery);
                            Statement updateStatement = connection.createStatement();
                            updateStatement.executeUpdate("UPDATE " + tableName + " SET " + dispColumn + " = " + (disp + 1) + " WHERE " + primaryKey + " = '" + id + "'");
                            updateStatement.executeUpdate("DELETE FROM " + permisos + joined + " WHERE " + userKey + " = '" + identificador + "' AND " + primaryKey + " = '" + id + "'");
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
        searchField = new JTextField();
        searchButton = new JButton("Buscar");
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);
        contentPanel.add(searchPanel, BorderLayout.NORTH);
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }


}
