
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.stream.Collectors;

public class SearchOrder {
    private JPanel searchOrderPanel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField bookAndQuantitiesField;
    private JTable resultTable;
    private DefaultTableModel tableModel;

    public SearchOrder(OrderQueue orderQueue, DefaultTableModel mainTableModel, CardLayout cardLayout, JPanel mainPanel) {
        searchOrderPanel = new JPanel(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("Search your order", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Create form panel using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.decode("#fe775c"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create text fields
        idField = new JTextField(15);
        nameField = new JTextField(15);
        addressField = new JTextField(15);
        bookAndQuantitiesField = new JTextField(15);

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        formPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Book and Quantities:"), gbc);

        gbc.gridx = 1;
        formPanel.add(bookAndQuantitiesField, gbc);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton searchOrderButton = new JButton("Search order!");
        JButton backButton = new JButton("BACK");
        backButton.setForeground(Color.RED);

        buttonsPanel.add(backButton);
        buttonsPanel.add(searchOrderButton);

        // Add components to the search order panel
        searchOrderPanel.add(titleLabel, BorderLayout.NORTH);
        searchOrderPanel.add(formPanel, BorderLayout.WEST);
        searchOrderPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Create table for displaying search results
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Book and Quantities");

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(resultTable);
        searchOrderPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button action
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        // Search order button action
        searchOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrders(orderQueue);
            }
        });
    }

    public JPanel getPanel() {
        return searchOrderPanel;
    }

    private void searchOrders(OrderQueue orderQueue) {
        String idText = idField.getText().trim();
        String nameText = nameField.getText().trim();
        String addressText = addressField.getText().trim();
        String bookAndQuantitiesText = bookAndQuantitiesField.getText().trim();

        List<Order> orders = orderQueue.getAllOrders();

        List<Order> matchingOrders = orders.stream().filter(order -> 
            (!idText.isEmpty() && String.valueOf(order.getId()).contains(idText)) ||
            (!nameText.isEmpty() && order.getName().contains(nameText)) ||
            (!addressText.isEmpty() && order.getAddress().contains(addressText)) ||
            (!bookAndQuantitiesText.isEmpty() && order.getBookAndQuantities().contains(bookAndQuantitiesText))
        ).collect(Collectors.toList());

        // Clear the table
        tableModel.setRowCount(0);

        // Populate table with matching orders
        for (Order order : matchingOrders) {
            tableModel.addRow(new Object[]{
                order.getId(),
                order.getName(),
                order.getAddress(),
                order.getBookAndQuantities()
            });
        }
    }
}