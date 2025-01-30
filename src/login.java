import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class login {
    public JPanel mainPanel;
    private JTextField usuariotxt;
    private JPasswordField passwordtxt;
    private JButton ingresarbtn;
    private JLabel usuariolbl;
    private JLabel passwordlbl;
    private JComboBox<String> comboBoxLogin;

    public login() {

        comboBoxLogin.addItem("Administrador");
        comboBoxLogin.addItem("Usuario");



        ingresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usuariotxt.getText();
                String password = new String(passwordtxt.getPassword());
                String rol = (String) comboBoxLogin.getSelectedItem();

                if (autenticarLogin(username, password, rol)) {
                    JOptionPane.showMessageDialog(null, "Bienvenido " + rol, "Inicio de Sesión", JOptionPane.INFORMATION_MESSAGE);

                    JFrame loginFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    if (loginFrame != null) {
                        loginFrame.dispose();
                    }

                    if ("Administrador".equals(rol)) {

                        JFrame frame = new JFrame("Menú Principal");
                        frame.setContentPane(new menu().menuPanel);
                        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        frame.setSize(700, 500);
                        frame.setPreferredSize(new Dimension(700, 500));
                        frame.pack();
                        frame.setVisible(true);

                    } else {

                        JFrame usuarioFrame = new JFrame("Usuario");
                        String eventId = JOptionPane.showInputDialog(null, "Ingrese el ID del evento:", "Buscar Evento", JOptionPane.QUESTION_MESSAGE);
                        if (eventId != null && !eventId.trim().isEmpty()) {
                            mostrarDetallesEvento(eventId, usuarioFrame);
                        }
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Usuario, contraseña o rol incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void mostrarDetallesEvento(String eventId, JFrame frame) {
                try (Connection conn = conexionBD.getConnection()) {
                    String sql = "SELECT nombre, fecha, ubicacion, descripcion FROM eventos WHERE id = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, eventId);
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        String nombre = rs.getString("nombre");
                        String fecha = rs.getString("fecha");
                        String ubicacion = rs.getString("ubicacion");
                        String descripcion = rs.getString("descripcion");

                        JTextArea textArea = new JTextArea(5, 30);
                        textArea.setText("Nombre: " + nombre + "\nFecha: " + fecha + "\nUbicación: " + ubicacion + "\nDescripción: " + descripcion);
                        textArea.setEditable(false);

                        JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Detalles del Evento", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "No se encontró el evento con ID: " + eventId, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error al recuperar los datos del evento", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean autenticarLogin(String username, String password, String rol) {
        try (Connection conn = conexionBD.getConnection()) {
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ? AND rol = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, rol);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
