
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class App {
    private static OrderQueue orderQueue = new OrderQueue();
    private static DefaultTableModel tableModel;

    public static void main(String[] args) {
        loadOrders();

        // Create the frame
        JFrame frame = new JFrame("Online Bookstore");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        // Create a CardLayout for switching between panels
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create the homepage panel
        JPanel homePanel = createHomePage(cardLayout, mainPanel);
        homePanel.setBackground(Color.decode("#fe775c"));

        // Create the place order panel
        JPanel placeOrderPanel = createPlaceOrderPage(cardLayout, mainPanel);
        placeOrderPanel.setBackground(Color.decode("#fe775c"));

        // Create the all orders panel
        JPanel allOrdersPanel = createAllOrdersPage(cardLayout, mainPanel);
        allOrdersPanel.setBackground(Color.decode("#fe775c"));

        // Create the search order panel
        JPanel searchOrderPanel = createSearchOrderPage(cardLayout, mainPanel);
        searchOrderPanel.setBackground(Color.decode("#fe775c"));

        // Add panels to the main panel
        mainPanel.add(homePanel, "Home");
        mainPanel.add(placeOrderPanel, "PlaceOrder");
        mainPanel.add(allOrdersPanel, "AllOrders");
        mainPanel.add(searchOrderPanel, "SearchOrder");

        // Add the main panel to the frame
        frame.getContentPane().add(mainPanel);

        // Show the frame
        frame.setVisible(true);

        // Show the homepage initially
        cardLayout.show(mainPanel, "Home");

        // Save orders on exit
        Runtime.getRuntime().addShutdownHook(new Thread(App::saveOrders));
    }

    private static JPanel createHomePage(CardLayout cardLayout, JPanel mainPanel) {
        // Create the homepage panel
        JPanel homePanel = new JPanel(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("Home page", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        buttonPanel.setBackground(Color.decode("#fe775c"));

        // Create buttons
        JButton placeOrderButton = new JButton("Place order");
        JButton allOrdersButton = new JButton("All orders");
        JButton searchOrderButton = new JButton("Search order");

        // Add buttons to the button panel
        buttonPanel.add(placeOrderButton);
        buttonPanel.add(allOrdersButton);
        buttonPanel.add(searchOrderButton);

        // Add components to the homepage panel
        homePanel.add(titleLabel, BorderLayout.NORTH);
        homePanel.add(buttonPanel, BorderLayout.CENTER);

        // Place order button action
        placeOrderButton.addActionListener(e -> cardLayout.show(mainPanel, "PlaceOrder"));

        // All orders button action
        allOrdersButton.addActionListener(e -> cardLayout.show(mainPanel, "AllOrders"));

        // Search order button action
        searchOrderButton.addActionListener(e -> cardLayout.show(mainPanel, "SearchOrder"));

        return homePanel;
    }

    private static JPanel createPlaceOrderPage(CardLayout cardLayout, JPanel mainPanel) {
        // Create place order panel
        JPanel placeOrderPanel = new JPanel(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("Place your order", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Create form panel using GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.decode("#fe775c"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Create text fields
        JTextField nameField = new JTextField(15);
        JTextField addressField = new JTextField(15);
        JTextField bookAndQuantitiesField = new JTextField(15);
        JLabel errorMessageLabel = new JLabel("");
        errorMessageLabel.setForeground(Color.RED);

        // Add form components
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Address:"), gbc);

        gbc.gridx = 1;
        formPanel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Book and Quantities:"), gbc);

        gbc.gridx = 1;
        formPanel.add(bookAndQuantitiesField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(errorMessageLabel, gbc);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonsPanel.setBackground(Color.decode("#fe775c"));

        JButton placeOrderButton = new JButton("Place order!");
        JButton backButton = new JButton("BACK");
        backButton.setForeground(Color.RED);

        buttonsPanel.add(backButton);
        buttonsPanel.add(placeOrderButton);

        // Add components to the place order panel
        placeOrderPanel.add(titleLabel, BorderLayout.NORTH);
        placeOrderPanel.add(formPanel, BorderLayout.CENTER);
        placeOrderPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Back button action
        backButton.addActionListener(e -> {
            errorMessageLabel.setText("");
            cardLayout.show(mainPanel, "Home");
        });

        // Place order button action
        placeOrderButton.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String bookAndQuantities = bookAndQuantitiesField.getText();

            if (isValidBookAndQuantities(bookAndQuantities)) {
                // Split and sort book and quantities
                String[] bookQuantitiesArray = bookAndQuantities.split("\\s*;\\s*");
                bookQuantitiesArray = SortAlgorithms.insertionSort(bookQuantitiesArray);

                // Join the sorted array back to a single string
                String sortedBookAndQuantities = String.join("; ", bookQuantitiesArray);

                Order order = new Order(name, address, sortedBookAndQuantities);
                orderQueue.addOrder(order);

                // Update the table model
                tableModel.addRow(new Object[]{
                        order.getId(),
                        order.getName(),
                        order.getAddress(),
                        order.getBookAndQuantities(),
                        "Delete"
                });

                JOptionPane.showMessageDialog(placeOrderPanel, "Order placed successfully!");

                // Clear text fields
                nameField.setText("");
                addressField.setText("");
                bookAndQuantitiesField.setText("");
                errorMessageLabel.setText("");
            } else {
                errorMessageLabel.setText("Please follow the format \"[Name]-[number];[Name]-[number]\"");
            }
        });

        return placeOrderPanel;
    }

    private static JPanel createAllOrdersPage(CardLayout cardLayout, JPanel mainPanel) {
        // Create all orders panel
        JPanel allOrdersPanel = new JPanel(new BorderLayout());

        // Create title label
        JLabel titleLabel = new JLabel("All orders", SwingConstants.LEFT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 18));

        // Create table model and table
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
        };
        JTable table = new JTable(tableModel);
        tableModel.addColumn("ID");
        tableModel.addColumn("Name");
        tableModel.addColumn("Address");
        tableModel.addColumn("Book and Quantities");
        tableModel.addColumn("Actions");

        // Populate table with orders
        orderQueue.getAllOrders().forEach(order -> {
            tableModel.addRow(new Object[]{
                    order.getId(),
                    order.getName(),
                    order.getAddress(),
                    order.getBookAndQuantities(),
                    "Delete"
            });
        });

        // Add delete button functionality
        table.getColumn("Actions").setCellRenderer(new ButtonRenderer());
        table.getColumn("Actions").setCellEditor(new ButtonEditor(new JCheckBox()));

        // Create scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the all orders panel
        allOrdersPanel.add(titleLabel, BorderLayout.NORTH);
        allOrdersPanel.add(scrollPane, BorderLayout.CENTER);

        // Create back button
        JButton backButton = new JButton("BACK");
        backButton.setForeground(Color.RED);
        allOrdersPanel.add(backButton, BorderLayout.SOUTH);

        // Back button action
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));

        return allOrdersPanel;
    }

    private static JPanel createSearchOrderPage(CardLayout cardLayout, JPanel mainPanel) {
        return new SearchOrder(orderQueue, tableModel, cardLayout, mainPanel).getPanel();
    }

    private static void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("orders.dat"))) {
            oos.writeObject(new ArrayList<>(orderQueue.getAllOrders()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("orders.dat"))) {
            List<Order> orders = (List<Order>) ois.readObject();
            orders.forEach(orderQueue::addOrder);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static boolean isValidBookAndQuantities(String input) {
        String regex = "^([a-zA-Z0-9 ]+-\\d+)(;[a-zA-Z0-9 ]+-\\d+)*$";
        return Pattern.matches(regex, input);
    }

    static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Delete" : value.toString());
            return this;
        }
    }

    static class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "Delete" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(e -> {
                if (isPushed) {
                    int modelRow = table.convertRowIndexToModel(row);
                    int orderId = (int) tableModel.getValueAt(modelRow, 0);
                    orderQueue.removeOrderById(orderId);
                    tableModel.removeRow(modelRow);
                }
                fireEditingStopped();
            });
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            isPushed = false;
            return label;
        }
    }
}