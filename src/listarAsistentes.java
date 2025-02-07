import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class listarAsistentes {
    public JPanel mainListarAsistentes;
    private JButton regresarbtn;
    private JButton actualizarbtn;
    private JButton eliminarbtn;
    private JTable asistentesTable;

    public listarAsistentes() {

        String[] columnNames = {"ID", "Nombre", "Apellido", "E-mail", "Telefono","Evento ID","Fecha", "Tipo Asistente", "Estado Asistencia"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        asistentesTable.setModel(tableModel);

        String query = "SELECT * FROM asistentes";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_asistente");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String email = rs.getString("email");
                String telefono = rs.getString("telefono");
                String id_evento = rs.getString("evento_id");
                String fecha = rs.getString("fecha_registro");
                String estado = rs.getString("estado_asistencia");
                String tipo = rs.getString("tipo_asistente");

                Object[] row = {id, nombre, apellido, email, telefono, id_evento, fecha, estado, tipo};
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame newFrame = new JFrame("Gestión de Asistentes");
                newFrame.setContentPane(new controlAsistentes().controlAsistentesPanel);
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

                JFrame newFrame = new JFrame("Actualizar Asistentes");
                newFrame.setContentPane(new actualizarAsistentes().mainActualizarAsistentes);
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

                String idAsistente = JOptionPane.showInputDialog(null, "Introduzca el ID del Asistente a eliminar:");

                if (idAsistente != null && !idAsistente.isEmpty()) {

                    String query = "DELETE FROM asistentes WHERE id_asistente = ?";

                    try (Connection conn = conexionBD.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(query)) {

                        pstmt.setInt(1, Integer.parseInt(idAsistente));
                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Asistente Eliminado Correctamente");
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró un asistente con ese ID", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al eliminar el Asistente: " + ex.getMessage(),"Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "ID de Asistente inválido. ","Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }


}
