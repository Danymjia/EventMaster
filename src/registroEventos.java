import javax.swing.*;
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
    private JComboBox<String> estadoAsistenciaComboBox;
    private JComboBox<String> tipoAsistenciaCombobox;
    private JComboBox<String> eventoComboBox;

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

                JFrame frame = new JFrame("Lista de Eventos Disponibles");
                frame.setContentPane(new listarEventos().mainListarEventos);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(listarbtn);
                currentFrame.dispose();
            }
        });



        regresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("Menú Principal");

                frame.setContentPane(new menu().menuPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(600,300);
                frame.setPreferredSize(new Dimension(600,300));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarbtn);
                currentFrame.dispose();

            }
        });
    }
}
