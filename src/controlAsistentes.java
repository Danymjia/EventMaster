import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class controlAsistentes {
    public JPanel controlAsistentesPanel;
    private JTextField nombretxt;
    private JTextField apellidotxt;
    private JButton registrarbtn;
    private JButton regresarbtn;
    private JTextField emailtxt;
    private JTextField telefonotxt;
    private JComboBox<String> eventoComboBox;
    private JComboBox<String> estadoAsistenciaComboBox;
    private JComboBox<String> tipoAsistenciaCombobox;
    private JButton listarbtn;

    public controlAsistentes() {

        estadoAsistenciaComboBox.addItem("Registrado");
        estadoAsistenciaComboBox.addItem("Cancelado");

        tipoAsistenciaCombobox.addItem("VIP");
        tipoAsistenciaCombobox.addItem("General");
        tipoAsistenciaCombobox.addItem("Invitado");

        cargarEventos();

        registrarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombretxt.getText().trim();
                String apellido = apellidotxt.getText().trim();
                String email = emailtxt.getText().trim();
                String telefono = telefonotxt.getText().trim();
                String tipoAsistente = (String) tipoAsistenciaCombobox.getSelectedItem();
                String estadoAsistencia = (String) estadoAsistenciaComboBox.getSelectedItem();
                int eventoId = obtenerEventoId();

                if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty() || eventoId == -1) {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (telefono.length() != 10) {
                    JOptionPane.showMessageDialog(null, "El número de teléfono debe tener 10 dígitos.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                guardarAsistentes(nombre, apellido, email, telefono, eventoId, tipoAsistente, estadoAsistencia);
                nombretxt.setText("");
                apellidotxt.setText("");
                emailtxt.setText("");
                telefonotxt.setText("");
            }

            private void guardarAsistentes(String nombre, String apellido, String email, String telefono, int eventoId, String tipoAsistente, String estadoAsistencia) {
                String query = "INSERT INTO asistentes (nombre, apellido, email, telefono, evento_id, tipo_asistente, estado_asistencia) VALUES (?, ?, ?, ?, ?, ?, ?)";

                try (Connection conn = conexionBD.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, apellido);
                    pstmt.setString(3, email);
                    pstmt.setString(4, telefono);
                    pstmt.setInt(5, eventoId);
                    pstmt.setString(6, tipoAsistente);
                    pstmt.setString(7, estadoAsistencia);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Asistente registrado correctamente.");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al registrar el asistente: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("Menú Principal");
                frame.setContentPane(new menu().menuPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600, 300);
                frame.setPreferredSize(new Dimension(600, 300));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarbtn);
                currentFrame.dispose();
            }
        });

        listarbtn.addActionListener(new ActionListener() {
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

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(listarbtn);
                currentFrame.dispose();
            }
        });
    }

    private void cargarEventos() {
        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT nombre FROM eventos");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                eventoComboBox.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int obtenerEventoId() {
        String eventoSeleccionado = (String) eventoComboBox.getSelectedItem();
        if (eventoSeleccionado == null) return -1;

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT id FROM eventos WHERE nombre = ?")) {
            pstmt.setString(1, eventoSeleccionado);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener el ID del evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return -1;
    }
}
