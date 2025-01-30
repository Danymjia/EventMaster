import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        JFrame frame = new JFrame("EventMaster");
        frame.setContentPane(new login().mainPanel);
        //frame.setContentPane(new imprimir().mainImprimir);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700,500);
        frame.setPreferredSize(new Dimension(700,500));
        frame.pack();
        frame.setVisible(true);
    }
}