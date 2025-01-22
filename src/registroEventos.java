import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class registroEventos {
    private JTextField nombreeventotxt;
    private JTextField fechaeventotext;
    private JTextField ubicacioneventotext;
    private JTable registrostable;
    private JTextField descripcioneventotxt;
    public JPanel registroEventosPanel;
    private JLabel nombreeventolbl;
    private JLabel fechaeventolbl;
    private JLabel ubicacioneventolbl;
    private JLabel descripcioneventolbl;
    private JButton guardarButton;
    private JButton listarbtn;
    private JButton guardarbtn;
    private JButton regresarbtn;

    public registroEventos() {

        guardarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre = nombreeventotxt.getText();
                String fecha = fechaeventotext.getText();
                String ubicacion = ubicacioneventotext.getText();
                String descripcion = descripcioneventotxt.getText();

                if (!nombre.isEmpty() && !fecha.isEmpty() && !ubicacion.isEmpty() && !descripcion.isEmpty()) {
                    guardarBD(nombre, fecha, ubicacion, descripcion);

                    nombreeventotxt.setText("");
                    fechaeventotext.setText("");
                    ubicacioneventotext.setText("");
                    descripcioneventotxt.setText("");

                } else {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void guardarBD(String nombre, String fecha, String ubicacion, String descripcion) {
                String query = "INSERT INTO eventos (nombre, fecha, ubicacion, descripcion) VALUES (?, ?, ?, ?)";

                try (Connection conn = conexionBD.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, nombre);
                    pstmt.setString(2, fecha);
                    pstmt.setString(3, ubicacion);
                    pstmt.setString(4, descripcion);
                    pstmt.executeUpdate();

                    JOptionPane.showMessageDialog(null, "Evento guardado correctamente.");

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al guardar el evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        listarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(listarbtn);
                currentFrame.dispose();

                JFrame frame = new JFrame("Listado de Eventos");
                frame.setLayout(new BorderLayout());

                JTable eventosTable = new JTable();
                JScrollPane scrollPane = new JScrollPane(eventosTable);
                frame.add(scrollPane, BorderLayout.CENTER);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

                JButton eliminarBtn = new JButton("Eliminar Evento");
                bottomPanel.add(eliminarBtn);

                JButton regresarBtn = new JButton("Regresar");
                bottomPanel.add(regresarBtn);

                frame.add(bottomPanel, BorderLayout.SOUTH);

                cargarBD(eventosTable);

                frame.setSize(700, 500);
                frame.setPreferredSize(new Dimension(700, 500));
                frame.pack();
                frame.setVisible(true);

                eliminarBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String idEliminar = JOptionPane.showInputDialog(frame, "Ingrese el ID del evento a eliminar:");
                        if (idEliminar != null && !idEliminar.isEmpty()) {
                            eliminarEvento(Integer.parseInt(idEliminar), eventosTable);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Por favor ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                });

                regresarBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.dispose();
                        JFrame newFrame = new JFrame("Gestión de Eventos");
                        newFrame.setContentPane(new registroEventos().registroEventosPanel);
                        newFrame.setSize(700, 500);
                        newFrame.setPreferredSize(new Dimension(700, 500));
                        newFrame.pack();
                        newFrame.setVisible(true);
                    }
                });
            }

            private void cargarBD(JTable eventosTable) {
                String[] columnNames = {"ID", "Nombre", "Fecha", "Ubicación", "Descripción"};
                DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                eventosTable.setModel(tableModel);

                String query = "SELECT * FROM eventos";

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
                    JOptionPane.showMessageDialog(null, "Error al cargar los eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void eliminarEvento(int id, JTable eventosTable) {
                String query = "DELETE FROM eventos WHERE id = ?";

                try (Connection conn = conexionBD.getConnection();
                     PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setInt(1, id);

                    int filasAfectadas = pstmt.executeUpdate();
                    if (filasAfectadas > 0) {
                        JOptionPane.showMessageDialog(null, "Evento eliminado correctamente.");
                        cargarBD(eventosTable);

                    } else {
                        JOptionPane.showMessageDialog(null, "No se encontró un evento con este ID", "Error", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error al eliminar el evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarbtn);
                currentFrame.dispose();

                JFrame frame = new JFrame("Menú Principal");

                frame.setContentPane(new menu().menuPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
