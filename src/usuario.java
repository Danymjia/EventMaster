import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class usuario {
    private JLabel titulotxt;
    private JTextField buscartxt;
    private JButton regresarbtn;
    private JButton buscarbtn;
    private JTable eventosTable;
    public JPanel mainUsuario;

    public usuario() {

        String[] columnNames = {"ID", "Nombre", "Fecha", "Ubicación", "Descripción"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        eventosTable.setModel(tableModel);

        buscarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = buscartxt.getText().trim();

                if (!id.isEmpty()) {
                    try (Connection conn = conexionBD.getConnection()) {
                        String query = "SELECT id, nombre, fecha, ubicacion, descripcion FROM eventos WHERE id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(query);
                        pstmt.setInt(1, Integer.parseInt(id)); // Convertir a entero el ID

                        ResultSet rs = pstmt.executeQuery();

                        tableModel.setRowCount(0);

                        if (rs.next()) {
                            int eventId = rs.getInt("id");
                            String nombre = rs.getString("nombre");
                            String fecha = rs.getString("fecha");
                            String ubicacion = rs.getString("ubicacion");
                            String descripcion = rs.getString("descripcion");

                            Object[] row = {eventId, nombre, fecha, ubicacion, descripcion};
                            tableModel.addRow(row);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontraron eventos con ese ID", "Resultado de la búsqueda", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "Error al consultar el evento: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor ingrese un ID para buscar", "Campo vacío", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("EventMaster");
                frame.setContentPane(new login().mainPanel);
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
    }
}

