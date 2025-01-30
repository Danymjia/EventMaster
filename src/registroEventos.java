import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class registroEventos {
    private JTextField nombreeventotxt;
    private JTextField fechaeventotxt;
    private JTextField ubicacioneventotxt;
    private JTextField descripcioneventotxt;
    public JPanel registroEventosPanel;
    private JLabel nombreeventolbl;
    private JLabel fechaeventolbl;
    private JLabel ubicacioneventolbl;
    private JLabel descripcioneventolbl;
    private JButton listarbtn;
    private JButton guardarbtn;
    private JButton regresarbtn;
    private JLabel titulotxt;

    public registroEventos() {

        guardarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nombre = nombreeventotxt.getText();
                String fecha = fechaeventotxt.getText();
                String ubicacion = ubicacioneventotxt.getText();
                String descripcion = descripcioneventotxt.getText();

                if (!nombre.isEmpty() && !fecha.isEmpty() && !ubicacion.isEmpty() && !descripcion.isEmpty()) {
                    guardarEvento(nombre, fecha, ubicacion, descripcion);

                    nombreeventotxt.setText("");
                    fechaeventotxt.setText("");
                    ubicacioneventotxt.setText("");
                    descripcioneventotxt.setText("");

                } else {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void guardarEvento(String nombre, String fecha, String ubicacion, String descripcion) {
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

                JButton regresarBtn = new JButton("Regresar");
                bottomPanel.add(regresarBtn);

                JButton eliminarBtn = new JButton("Eliminar Evento");
                bottomPanel.add(eliminarBtn);

                JButton actualizarBtn = new JButton("Actualizar Evento");
                bottomPanel.add(actualizarBtn);

                frame.add(bottomPanel, BorderLayout.SOUTH);

                cargarBD(eventosTable);

                frame.setSize(700, 500);
                frame.setPreferredSize(new Dimension(700, 500));
                frame.pack();
                frame.setVisible(true);

                regresarBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JFrame newFrame = new JFrame("Gestión de Eventos");
                        newFrame.setContentPane(new registroEventos().registroEventosPanel);
                        newFrame.setSize(700, 500);
                        newFrame.setPreferredSize(new Dimension(700, 500));
                        newFrame.pack();
                        newFrame.setVisible(true);

                        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarBtn);
                        currentFrame.dispose();
                    }
                });

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

                actualizarBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String idActualizar = JOptionPane.showInputDialog(frame, "Ingrese el ID del evento a Actualizar:");
                        if (idActualizar != null && !idActualizar.isEmpty()) {
                            int idEvento = Integer.parseInt(idActualizar);
                            actualizarEvento(idEvento, eventosTable);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Por favor ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
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

            private void actualizarEvento(int id, JTable eventosTable) {
                JTable frame =new JTable();

                String nuevoNombre = JOptionPane.showInputDialog(frame, "Ingrese el nuevo nombre del evento:");
                String nuevaFecha = JOptionPane.showInputDialog(frame, "Ingrese la nueva fecha del evento (formato YYYY-MM-DD):");
                String nuevaUbicacion = JOptionPane.showInputDialog(frame, "Ingrese la nueva ubicación del evento:");
                String nuevaDescripcion = JOptionPane.showInputDialog(frame, "Ingrese la nueva descripción del evento:");

                if (nuevoNombre != null && !nuevoNombre.isEmpty() &&
                        nuevaFecha != null && !nuevaFecha.isEmpty() &&
                        nuevaUbicacion != null && !nuevaUbicacion.isEmpty() &&
                        nuevaDescripcion != null && !nuevaDescripcion.isEmpty()) {

                    String query = "UPDATE eventos SET nombre = ?, fecha = ?, ubicacion = ?, descripcion = ? WHERE id = ?";

                    try (Connection conn = conexionBD.getConnection();
                         PreparedStatement pstmt = conn.prepareStatement(query)) {

                        pstmt.setString(1, nuevoNombre);
                        pstmt.setString(2, nuevaFecha);
                        pstmt.setString(3, nuevaUbicacion);
                        pstmt.setString(4, nuevaDescripcion);
                        pstmt.setInt(5, id);

                        int filasAfectadas = pstmt.executeUpdate();
                        if (filasAfectadas > 0) {
                            JOptionPane.showMessageDialog(null, "Evento actualizado correctamente.");
                            cargarBD(eventosTable);
                        } else {
                            JOptionPane.showMessageDialog(null, "No se encontró un evento con este ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (SQLException e) {
                        JOptionPane.showMessageDialog(null, "Error al actualizar el evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
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
