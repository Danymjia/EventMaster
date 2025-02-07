import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class actualizarEventos {
    public JPanel mainActualizarEventos;
    private JButton actualizarButton;
    private JTextField nuevoNombretxt;
    private JTextField nuevaFechatxt;
    private JTextField nuevaUbicaciontxt;
    private JTextField nuevaDescripciontxt;
    private JComboBox <String> eventoCombobox;
    private JButton regresarbtn;


    private int eventoSeleccionadoID = -1;

    public actualizarEventos() {
        cargarEventos();

        eventoCombobox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String eleccion = (String) eventoCombobox.getSelectedItem();
                if (eleccion == null) return;

                String query = "SELECT id, nombre, fecha, ubicacion, descripcion FROM eventos WHERE nombre = ?";

                try (Connection conn = conexionBD.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, eleccion);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        eventoSeleccionadoID = rs.getInt("id");
                        nuevoNombretxt.setText(rs.getString("nombre"));
                        nuevaFechatxt.setText(rs.getString("fecha"));
                        nuevaUbicaciontxt.setText(rs.getString("ubicacion"));
                        nuevaDescripciontxt.setText(rs.getString("descripcion"));
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al cargar datos del evento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        actualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (eventoSeleccionadoID == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione un evento antes de actualizar", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String query = "UPDATE eventos SET nombre=?, fecha=?, ubicacion=?, descripcion=? WHERE id=?";

                try (Connection conn = conexionBD.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, nuevoNombretxt.getText());
                    pstmt.setString(2, nuevaFechatxt.getText());
                    pstmt.setString(3, nuevaUbicaciontxt.getText());
                    pstmt.setString(4, nuevaDescripciontxt.getText());
                    pstmt.setInt(5, eventoSeleccionadoID);

                    int filasActualizadas = pstmt.executeUpdate();
                    if (filasActualizadas > 0) {
                        JOptionPane.showMessageDialog(null, "Evento actualizado correctamente");
                        cargarEventos();
                    } else {
                        JOptionPane.showMessageDialog(null, "No se pudo actualizar el evento");
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Error al actualizar evento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("Lista de Eventos Disponibles");
                frame.setContentPane(new listarEventos().mainListarEventos);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarbtn);
                currentFrame.dispose();


            }
        });
    }

    private void cargarEventos() {
        String query = "SELECT id, nombre FROM eventos";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            eventoCombobox.removeAllItems();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");

                eventoCombobox.addItem(nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}