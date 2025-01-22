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


    public login() {
        ingresarbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String username = usuariotxt.getText();
                String password = new String(passwordtxt.getPassword());

                if (autenticarLogin(username, password)) {
                    JOptionPane.showMessageDialog(null, "Sesión Iniciada" , "Inicio de Sesióon", JOptionPane.INFORMATION_MESSAGE);

                    JFrame login = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
                    if (login != null) {
                        login.dispose();
                    }

                    JFrame frame = new JFrame("Menú Principal");

                    frame.setContentPane(new menu().menuPanel);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setSize(700,500);
                    frame.setPreferredSize(new Dimension(700,500));
                    frame.pack();
                    frame.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private boolean autenticarLogin(String username, String password) {
        try (Connection conn = conexionBD.getConnection()) {

            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contraseña = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            return rs.next();

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

