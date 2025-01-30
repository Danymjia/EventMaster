import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class imprimir {
    public JPanel mainImprimir;
    private JButton imprimirEventos;
    private JButton imprimirAsistentes;
    private JButton regresarButton;

    public imprimir() {
        imprimirEventos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar PDF");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                fileChooser.setSelectedFile(new java.io.File("Eventos-EventMaster.pdf"));

                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    java.io.File fileToSave = fileChooser.getSelectedFile();

                    try (Connection conn = conexionBD.getConnection();) {
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                        document.open();

                        document.add(new Paragraph("Historial de Eventos Registrados", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK)));
                        document.add(new Paragraph(" "));

                        String sql = "SELECT nombre, fecha, ubicacion, descripcion FROM eventos";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        ResultSet rs = pstmt.executeQuery();

                        PdfPTable pdfTable = new PdfPTable(4);
                        pdfTable.setWidthPercentage(100);
                        pdfTable.setSpacingBefore(10f);

                        pdfTable.addCell(new PdfPCell(new Phrase("Nombre", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Fecha", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Ubicación", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Descripción", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));

                        while (rs.next()) {
                            pdfTable.addCell(rs.getString("nombre"));
                            pdfTable.addCell(rs.getString("fecha"));
                            pdfTable.addCell(rs.getString("ubicacion"));
                            pdfTable.addCell(rs.getString("descripcion"));
                        }

                        document.add(pdfTable);

                        document.close();
                        JOptionPane.showMessageDialog(null, "PDF generado exitosamente en: " + fileToSave.getAbsolutePath());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al generar el PDF.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Operación cancelada.");
                }

            }
        });

        imprimirAsistentes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Guardar PDF");
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

                fileChooser.setSelectedFile(new java.io.File("Asistentes-EventMaster.pdf"));

                int userSelection = fileChooser.showSaveDialog(null);

                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    java.io.File fileToSave = fileChooser.getSelectedFile();

                    try (Connection conn = conexionBD.getConnection();) {
                        Document document = new Document();
                        PdfWriter.getInstance(document, new FileOutputStream(fileToSave));
                        document.open();

                        document.add(new Paragraph("Historial de Asistentes Registrados", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16, BaseColor.BLACK)));
                        document.add(new Paragraph(" "));

                        String sql = "SELECT id_asistente, nombre, apellido, email, telefono, evento_id, fecha_registro, estado_asistencia, tipo_asistente FROM asistentes";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        ResultSet rs = pstmt.executeQuery();

                        PdfPTable pdfTable = new PdfPTable(9);
                        pdfTable.setWidthPercentage(100);
                        pdfTable.setSpacingBefore(10f);

                        pdfTable.addCell(new PdfPCell(new Phrase("ID", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Nombre", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Apellido", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("E-mail", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Telefono", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("ID de Evento", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Fecha", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Estado", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));
                        pdfTable.addCell(new PdfPCell(new Phrase("Tipo", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9))));

                        while (rs.next()) {
                            pdfTable.addCell(rs.getString("id_asistente"));
                            pdfTable.addCell(rs.getString("nombre"));
                            pdfTable.addCell(rs.getString("apellido"));
                            pdfTable.addCell(rs.getString("email"));
                            pdfTable.addCell(rs.getString("telefono"));
                            pdfTable.addCell(rs.getString("evento_id"));
                            pdfTable.addCell(rs.getString("fecha_registro"));
                            pdfTable.addCell(rs.getString("estado_asistencia"));
                            pdfTable.addCell(rs.getString("tipo_asistente"));
                        }

                        document.add(pdfTable);

                        document.close();
                        JOptionPane.showMessageDialog(null, "PDF generado exitosamente en: " + fileToSave.getAbsolutePath());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al generar el PDF.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Operación cancelada.");
                }
            }
        });

        regresarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(regresarButton);
                currentFrame.dispose();

                JFrame frame = new JFrame("Menú Principal");
                frame.setContentPane(new menu().menuPanel);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(700, 500);
                frame.setPreferredSize(new Dimension(700, 500));
                frame.pack();
                frame.setVisible(true);

            }
        });
    }
}
