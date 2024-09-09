import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdatePrices extends JFrame {

    private JComboBox<String> criteriaComboBox;
    private JTextField valueTextField;
    private JButton updateButton;

    public UpdatePrices() {
        setTitle("Actualizar Precios de Productos");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel criteriaLabel = new JLabel("Seleccionar criterio:");
        criteriaLabel.setBounds(20, 20, 150, 25);
        add(criteriaLabel);

        String[] criteriaOptions = {"IDTIENDA", "IDEMPLEADO", "IDESTADO"};
        criteriaComboBox = new JComboBox<>(criteriaOptions);
        criteriaComboBox.setBounds(180, 20, 150, 25);
        add(criteriaComboBox);

        JLabel valueLabel = new JLabel("Valor del criterio:");
        valueLabel.setBounds(20, 60, 150, 25);
        add(valueLabel);

        valueTextField = new JTextField();
        valueTextField.setBounds(180, 60, 150, 25);
        add(valueTextField);

        updateButton = new JButton("Actualizar Precios");
        updateButton.setBounds(100, 100, 200, 30);
        add(updateButton);

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCriteria = criteriaComboBox.getSelectedItem().toString();
                int criteriaValue = Integer.parseInt(valueTextField.getText());
                updatePriceByCriteria(selectedCriteria, criteriaValue);
            }
        });
    }

    public static void updatePriceByCriteria(String criterionType, int criterionValue) {
        String query = "UPDATE TICKETSD SET PRECIO = PRECIO + 1 WHERE TICKET IN (" +
                "SELECT FOLIO FROM TICKETSH WHERE " + criterionType + " = ?) " +
                "AND TICKET IN (" +
                "SELECT TICKET FROM TICKETSD GROUP BY TICKET HAVING COUNT(DISTINCT IDPRODUCTO) >= 3)";

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = DatabaseConnection.connect();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            stmt = conn.prepareStatement(query);
            stmt.setInt(1, criterionValue);

            int rowsAffected = stmt.executeUpdate();

            // Commit the transaction after successful execution
            conn.commit();

            JOptionPane.showMessageDialog(null, "Precios actualizados para " + rowsAffected + " productos.");

        } catch (SQLException e) {
            try {
                // Roll back the transaction in case of an error
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackException) {
                rollbackException.printStackTrace();
            }
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar los precios.");

        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit mode
                    conn.close();
                }
            } catch (SQLException closeException) {
                closeException.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdatePrices gui = new UpdatePrices();
            gui.setVisible(true);
        });
    }
}
