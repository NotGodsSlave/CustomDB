import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.text.TableView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TableForm {
    public JTable tableHolder;
    public JList<String> tableList;
    public JButton addColumnButton;
    public JButton deleteRowButton;
    public JButton addRowButton;
    public JButton deleteColumnButton;
    public JButton deleteTableButton;
    public JButton addTableButton;
    public JPanel tablePanel;
    public JLabel DBName;
    public JButton goBackButton;
    public JButton saveButton;
    private JButton sortButton;
    private Database currentDB;
    private Table currentTable;

    public Database getCurrentDB() {
        return currentDB;
    }

    public void setCurrentDB(Database currentDB) {
        this.currentDB = currentDB;
        DBName.setText(currentDB.name);
        tableList.setListData(currentDB.tables.keySet().toArray(new String[0]));
    }

    public Table getCurrentTable() {
        return currentTable;
    }

    public void setCurrentTable(Table currentTable) {
        this.currentTable = currentTable;
    }

    public void updateModel() {
        tableHolder.setModel(new CustomTableModel());
        tableHolder.getModel().addTableModelListener(
                new TableModelListener() {
                    @Override
                    public void tableChanged(TableModelEvent tableModelEvent) {
                        if (tableModelEvent.getType() == TableModelEvent.UPDATE) {
                            int row = tableModelEvent.getFirstRow();
                            int column = tableModelEvent.getColumn();
                            if (row >= 0 && row < currentTable.rows.size() && column >= 0 && column < currentTable.columns.size()) {
                                if (currentTable.updateCellContent(row, column, tableHolder.getValueAt(row, column))) {
                                    currentDB.tables.put(currentTable.name, currentTable);
                                } else {
                                    tableHolder.getModel().setValueAt(null, row, column);
                                    //tableHolder.setValueAt(null,row,column);
                                }
                            }
                        }
                    }
                }
        );
    }

    public TableForm() {
        updateModel();
        addTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = JOptionPane.showInputDialog(
                        tablePanel,
                        "Enter the table name, please",
                        "New Table",
                        JOptionPane.PLAIN_MESSAGE);
                if (name != null && name.length() > 0) {
                    currentDB.addTable(name);
                }
                else {
                    JOptionPane.showMessageDialog(tablePanel, "Couldn't create a table");
                }
                if (currentDB.tables != null)
                    tableList.setListData(currentDB.tables.keySet().toArray(new String[0]));
                else
                    tableList.setListData(new String[]{});
            }
        });
        deleteTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = JOptionPane.showInputDialog(
                        tablePanel,
                        "Enter the table name, please",
                        "Deleting a Table",
                        JOptionPane.PLAIN_MESSAGE);
                if (name != null && name.length() > 0 && currentDB.tables.containsKey(name)) {
                    currentDB.removeTable(name);
                }
                else {
                    JOptionPane.showMessageDialog(tablePanel, "Couldn't delete the table");
                }
                if (currentDB.tables != null)
                    tableList.setListData(currentDB.tables.keySet().toArray(new String[0]));
                else
                    tableList.setListData(new String[]{});
            }
        });
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                App.saveDB(currentDB);
            }
        });
        tableList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    currentTable = currentDB.tables.get(tableList.getSelectedValue());
                    if (currentTable != null) {
                        updateModel();
                        CustomTableModel tableModel = (CustomTableModel) tableHolder.getModel();
                        for (Column col : currentTable.columns) {
                            tableModel.addColumn(col.name);
                        }
                        for (Row r : currentTable.rows) {
                            ArrayList<Object> content = new ArrayList<>();
                            for (Cell c : r.cells) {
                                content.add(c.getContent());
                            }
                            tableModel.addRow(content.toArray());
                        }
                    }
                }
            }
        });
        addColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentTable != null) {
                    JTextField nameField = new JTextField(15);
                    JComboBox<String> typeField = new JComboBox<>(new String[]{"Integer", "Real", "Character", "String", "Money"});
                    JPanel responsePanel = new JPanel();
                    responsePanel.add(new JLabel("Column name:"));
                    responsePanel.add(nameField);
                    responsePanel.add(new JLabel("Column type"));
                    responsePanel.add(typeField);
                    String name, type;
                    int resp = JOptionPane.showConfirmDialog(tablePanel, responsePanel, "Adding a Column", JOptionPane.OK_CANCEL_OPTION);
                    if (resp == JOptionPane.OK_OPTION) {
                        name = nameField.getText();
                        type = typeField.getSelectedItem().toString();
                        CustomTableModel tableModel = (CustomTableModel) tableHolder.getModel();
                        if (name != null && name.length() > 0) {
                            tableModel.addColumn(name);
                            currentTable.addColumn(name, type);
                            currentDB.tables.put(currentTable.name, currentTable);
                        } else {
                            JOptionPane.showMessageDialog(tablePanel, "Couldn't add the column");
                        }
                    }
                }
            }
        });
        deleteColumnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentTable != null) {
                    String num = JOptionPane.showInputDialog(
                            tablePanel,
                            "Enter the column number, please",
                            "Deleting a Column",
                            JOptionPane.PLAIN_MESSAGE);
                    try {
                        int i = Integer.parseInt(num);
                        if (i >= 0 && i < currentTable.columns.size()) {
                            CustomTableModel tableModel = (CustomTableModel) tableHolder.getModel();
                            tableModel.removeColumn(i);
                            currentTable.removeColumn(i);
                            currentDB.tables.put(currentTable.name, currentTable);
                        } else {
                            JOptionPane.showMessageDialog(tablePanel, "Incorrect number");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(tablePanel, "Incorrect number");
                    }
                }
            }
        });
        addRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentTable != null) {
                    CustomTableModel tableModel = (CustomTableModel) tableHolder.getModel();
                    tableModel.addRow(new Object[]{});
                    currentTable.addRow();
                    currentDB.tables.put(currentTable.name, currentTable);
                }
            }
        });
        deleteRowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentTable != null) {
                    String num = JOptionPane.showInputDialog(
                            tablePanel,
                            "Enter the row number, please",
                            "Deleting a Row",
                            JOptionPane.PLAIN_MESSAGE);
                    try {
                        int i = Integer.parseInt(num);
                        if (i >= 0 && i < currentTable.rows.size()) {
                            CustomTableModel tableModel = (CustomTableModel) tableHolder.getModel();
                            tableModel.removeRow(i);
                            currentTable.removeRow(i);
                            currentDB.tables.put(currentTable.name, currentTable);
                        } else {
                            JOptionPane.showMessageDialog(tablePanel, "Incorrect number");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(tablePanel, "Incorrect number");
                    }
                }
            }
        });
        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (currentTable != null) {
                    String num = JOptionPane.showInputDialog(
                            tablePanel,
                            "Enter the number of the column you want to sort by",
                            "Sorting options",
                            JOptionPane.PLAIN_MESSAGE);
                    try {
                        int i = Integer.parseInt(num);
                        if (i >= 0 && i < currentTable.columns.size()) {
                            CustomTableModel tableModel = (CustomTableModel) tableHolder.getModel();
                            tableModel.setRowCount(0);
                            currentTable.sortByValue(i);
                            for (Row r : currentTable.rows) {
                                ArrayList<Object> content = new ArrayList<>();
                                for (Cell c : r.cells) {
                                    content.add(c.getContent());
                                }
                                tableModel.addRow(content.toArray());
                            }
                            currentDB.tables.put(currentTable.name, currentTable);
                        } else {
                            JOptionPane.showMessageDialog(tablePanel, "Incorrect number");
                        }
                    } catch (NumberFormatException e) {
                        JOptionPane.showMessageDialog(tablePanel, "Incorrect number");
                    }
                }
            }
        });
    }
}
