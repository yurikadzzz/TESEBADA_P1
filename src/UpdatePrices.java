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
        
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, criterionValue);
            int rowsAffected = stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Precios actualizados para " + rowsAffected + " productos.");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al actualizar los precios.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UpdatePrices gui = new UpdatePrices();
            gui.setVisible(true);
        });
    }
}
