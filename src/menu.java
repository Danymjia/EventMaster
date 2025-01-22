import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class menu {
    public JPanel menuPanel;
    private JButton irButton;
    private JButton irButton1;
    private JButton irButton2;

    public menu() {
        irButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(irButton);
                currentFrame.dispose();

                JFrame frame = new JFrame("Pantalla de Gestion de Eventos");

                frame.setContentPane(new registroEventos().registroEventosPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
