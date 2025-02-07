import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class listarEventos {
    public JPanel mainListarEventos;
    private JTable eventosTable;
    private JButton regresarbtn;
    private JButton actualizarbtn;
    private JButton eliminarbtn;

    public listarEventos() {

        String[] columnNames = {"ID", "Nombre", "Fecha", "Ubicación", "Descripción"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        eventosTable.setModel(tableModel);

        String query = "SELECT id, nombre, fecha, ubicacion, descripcion FROM eventos";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String fecha = rs.getString("fecha");
                String ubicacion = rs.getString("ubicacion");
                String descripcion = rs.getString("descripcion");

                Object[] row = {id, nombre, fecha, ubicacion, descripcion};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los asistentes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame newFrame = new JFrame("Gestión de Eventos");
                newFrame.setContentPane(new registroEventos().registroEventosPanel);
                newFrame.setSize(700, 400);
                newFrame.setPreferredSize(new Dimension(700, 400));
                newFrame.pack();
                newFrame.setLocationRelativeTo(null);
                newFrame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarbtn);
                currentFrame.dispose();

            }
        });

        actualizarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame newFrame = new JFrame("Actualizar Eventos");
                newFrame.setContentPane(new actualizarEventos().mainActualizarEventos);
                newFrame.setSize(700, 400);
                newFrame.setPreferredSize(new Dimension(700, 400));
                newFrame.pack();
                newFrame.setLocationRelativeTo(null);
                newFrame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(actualizarbtn);
                currentFrame.dispose();

            }
        });

        eliminarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String idEvento = JOptionPane.showInputDialog(null, "Introduce el ID del evento a eliminar:");

                if (idEvento != null && !idEvento.isEmpty()) {

                    String query = "DELETE FROM eventos WHERE id = ?";

                    try (Connection conn = conexionBD.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(query)) {

                        pstmt.setInt(1, Integer.parseInt(idEvento));
                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Evento Eliminado Correctamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró un evento con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el evento: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "ID de evento inválido. ","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
}
