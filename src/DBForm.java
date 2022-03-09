import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class DBForm {
    private JPanel DBMenu;
    private JButton newDatabaseButton;
    private JList<String> DBList;
    private JButton deleteDatabaseButton;
    private Database currentDB;
    TableForm tf;

    public DBForm() {
        if (App.getDBList() != null)
            DBList.setListData(App.getDBList());

        newDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = JOptionPane.showInputDialog(
                        DBMenu,
                        "Enter the database name, please",
                        "New Database",
                        JOptionPane.PLAIN_MESSAGE);
                if (name != null && name.length() > 0)
                    App.createDB(name);
                else
                    JOptionPane.showMessageDialog(DBMenu, "Couldn't create the database!");
                if (App.getDBList() != null)
                    DBList.setListData(App.getDBList());
                else
                    DBList.setListData(new String[]{});
            }
        });

        deleteDatabaseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String name = JOptionPane.showInputDialog(
                        DBMenu,
                        "Enter the database name, please",
                        "Deleting a Database",
                        JOptionPane.PLAIN_MESSAGE);
                if (!(name != null && name.length() > 0 && App.deleteDB(name)))
                    JOptionPane.showMessageDialog(DBMenu, "Couldn't delete the database!");
                if (App.getDBList() != null)
                    DBList.setListData(App.getDBList());
                else
                    DBList.setListData(new String[]{});
            }
        });
    }

    public void createForm() {
        tf = new TableForm();
        JFrame frame = new JFrame("Database GUI");
        JPanel panel1 = DBMenu;
        JPanel panel2 = tf.tablePanel;

        JPanel containerPanel = new JPanel();
        CardLayout cl = new CardLayout();
        containerPanel.setLayout(cl);

        containerPanel.add(panel1, "1");
        containerPanel.add(panel2, "2");
        cl.show(containerPanel,"1");

        DBList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent listSelectionEvent) {
                if (!listSelectionEvent.getValueIsAdjusting()) {
                    tf.setCurrentDB(App.exportDB(DBList.getSelectedValue()));
                    cl.show(containerPanel, "2");
                }
            }
        });

        tf.goBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                cl.show(containerPanel, "1");
                tf.setCurrentTable(null);
                tf.tableHolder.setModel(new CustomTableModel());
            }
        });

        frame.setContentPane(containerPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Insets insets = frame.getInsets();
        frame.setSize(800+insets.left+insets.right, 600+insets.top+insets.bottom);
        frame.setVisible(true);
    }
}
