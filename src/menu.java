import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

public class menu {
    public JPanel menuPanel;
    private JButton eventosbtn;
    private JButton asistentesbtn;
    private JButton salirbtn;
    private JButton irButton;

    public menu() {
        eventosbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(eventosbtn);
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

        asistentesbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame frame = new JFrame("Pantalla de Control de Asistentes");

                frame.setContentPane(new controlAsistentes().controlAsistentesPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setVisible(true);

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(asistentesbtn);
                currentFrame.dispose();
            }
        });
        salirbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(salirbtn);
                currentFrame.dispose();

                JFrame frame = new JFrame("EventMaster");
                frame.setContentPane(new login().mainPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setVisible(true);
            }
        });
        irButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(irButton);
                currentFrame.dispose();

                JFrame frame = new JFrame("Imprimir Datos");
                frame.setContentPane(new imprimir().mainImprimir);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700,500);
                frame.setPreferredSize(new Dimension(700,500));
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
