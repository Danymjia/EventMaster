import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class actualizarAsistentes {
    public JPanel mainActualizarAsistentes;
    private JComboBox<String> asistentesCombobox;
    private JTextField nuevoNombretxt;
    private JTextField nuevoApellidotxt;
    private JTextField nuevoEmailtxt;
    private JComboBox<String> nuevoTipoCombobox;
    private JComboBox<String> nuevoEstadoCombobox;
    private JButton regresarbtn;
    private JButton actualizarbtn;
    private JTextField nuevoTelefonotxt;

    public actualizarAsistentes() {
        nuevoEstadoCombobox.addItem("Registrado");
        nuevoEstadoCombobox.addItem("Cancelado");

        nuevoTipoCombobox.addItem("VIP");
        nuevoTipoCombobox.addItem("General");
        nuevoTipoCombobox.addItem("Invitado");

        cargarAsistentes();

        actualizarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String asistenteSeleccionado = (String) asistentesCombobox.getSelectedItem();
                String nuevoNombre = nuevoNombretxt.getText().trim();
                String nuevoApellido = nuevoApellidotxt.getText().trim();
                String nuevoEmail = nuevoEmailtxt.getText().trim();
                String nuevoTelefono = nuevoTelefonotxt.getText().trim();
                String nuevoTipo = (String) nuevoTipoCombobox.getSelectedItem();
                String nuevoEstado = (String) nuevoEstadoCombobox.getSelectedItem();

                if (asistenteSeleccionado == null || nuevoNombre.isEmpty() || nuevoApellido.isEmpty() || nuevoEmail.isEmpty() || nuevoTelefono.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (nuevoTelefono.length() != 10) {
                    JOptionPane.showMessageDialog(null, "El número de teléfono debe tener 10 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                actualizarAsistente(asistenteSeleccionado, nuevoNombre, nuevoApellido, nuevoEmail, nuevoTelefono, nuevoTipo, nuevoEstado);
            }
        });

        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("Lista de Asistentes Disponibles");
                frame.setContentPane(new listarAsistentes().mainListarAsistentes);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700, 500);
                frame.setPreferredSize(new Dimension(700, 500));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarbtn);
                if (currentFrame != null) {
                    currentFrame.dispose();
                }
            }
        });
    }

    private void cargarAsistentes() {
        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT nombre FROM asistentes");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                asistentesCombobox.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar asistentes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarAsistente(String asistente, String nombre, String apellido, String email, String telefono, String tipo, String estado) {
        String query = "UPDATE asistentes SET nombre = ?, apellido = ?, email = ?, telefono = ?, tipo_asistente = ?, estado_asistencia = ? WHERE nombre = ?";
        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, email);
            pstmt.setString(4, telefono);
            pstmt.setString(5, tipo);
            pstmt.setString(6, estado);
            pstmt.setString(7, asistente);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Asistente actualizado correctamente.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar el asistente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
