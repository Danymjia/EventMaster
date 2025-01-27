import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        cargarEventos();
        cargarEstadosAsistencia();
        cargarTiposAsistencia();

        registrarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombretxt.getText();
                String apellido = apellidotxt.getText();
                String email = emailtxt.getText();
                String telefono = telefonotxt.getText();
                String tipoAsistente = (String) tipoAsistenciaCombobox.getSelectedItem();
                String estadoAsistencia = (String) estadoAsistenciaComboBox.getSelectedItem();
                int eventoId = obtenerEventoId();

                if (!nombre.isEmpty() && !apellido.isEmpty() && !email.isEmpty() && !telefono.isEmpty() && eventoId != -1) {
                    guardarAsistentes(nombre, apellido, email, telefono, eventoId, tipoAsistente, estadoAsistencia);
                    nombretxt.setText("");
                    apellidotxt.setText("");
                    emailtxt.setText("");
                    telefonotxt.setText("");
                } else {
                    JOptionPane.showMessageDialog(null, "Por favor complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
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
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        listarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(listarbtn);
                currentFrame.dispose();

                JFrame frame = new JFrame("Listado de Asistentes");
                frame.setLayout(new BorderLayout());

                JTable eventosTable = new JTable();
                JScrollPane scrollPane = new JScrollPane(eventosTable);
                frame.add(scrollPane, BorderLayout.CENTER);

                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

                JButton regresarBtn = new JButton("Regresar");
                bottomPanel.add(regresarBtn);

                JButton eliminarBtn = new JButton("Eliminar Asistente");
                bottomPanel.add(eliminarBtn);

                JButton actualizarBtn = new JButton("Actualizar Asistente");
                bottomPanel.add(actualizarBtn);

                frame.add(bottomPanel, BorderLayout.SOUTH);

                cargarAsistentes(eventosTable);

                frame.setSize(700, 500);
                frame.setPreferredSize(new Dimension(700, 500));
                frame.pack();
                frame.setVisible(true);

                regresarBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        JFrame newFrame = new JFrame("Gestión de Asistentes");
                        newFrame.setContentPane(new controlAsistentes().controlAsistentesPanel);
                        newFrame.setSize(700, 500);
                        newFrame.setPreferredSize(new Dimension(700, 500));
                        newFrame.pack();
                        newFrame.setVisible(true);

                        JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarBtn);
                        currentFrame.dispose();
                    }
                });
            }


            private void cargarAsistentes(JTable eventosTable) {
                String[] columnNames = {"ID", "Nombre", "Apellido", "E-mail", "Telefono","Evento ID","Fecha", "Tipo Asistente", "Estado Asistencia"};
                DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
                eventosTable.setModel(tableModel);

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
            }
        });

    }

    private void cargarEventos() {
        String query = "SELECT id, nombre FROM eventos";

        try (Connection conn = conexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            eventoComboBox.removeAllItems();
            while (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                eventoComboBox.addItem(id + " - " + nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar eventos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarEstadosAsistencia() {
        estadoAsistenciaComboBox.addItem("Registrado");
        estadoAsistenciaComboBox.addItem("Asistió");
        estadoAsistenciaComboBox.addItem("No asistió");
    }

    private void cargarTiposAsistencia() {
        tipoAsistenciaCombobox.addItem("VIP");
        tipoAsistenciaCombobox.addItem("General");
        tipoAsistenciaCombobox.addItem("Invitado");
    }

    private int obtenerEventoId() {
        String seleccion = (String) eventoComboBox.getSelectedItem();
        if (seleccion != null && seleccion.contains(" - ")) {
            try {
                return Integer.parseInt(seleccion.split(" - ")[0]);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error al obtener ID del evento.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return -1;
    }
}
