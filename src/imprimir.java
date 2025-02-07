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
                            pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("nombre"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                            pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("fecha"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                            pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("ubicacion"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                            pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("descripcion"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                        }

                        document.add(pdfTable);

                        document.close();
                        JOptionPane.showMessageDialog(null, "PDF generado exitosamente en: " + fileToSave.getAbsolutePath());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al generar el PDF.", "Error",JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha seleccionado una ruta.", "Error",JOptionPane.ERROR_MESSAGE);
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

                        document.add(new Paragraph("Historial de Asistentes Registrados", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 13, BaseColor.BLACK)));
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

                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("id_asistente"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("nombre"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("apellido"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("email"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("telefono"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("evento_id"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("fecha_registro"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("estado_asistencia"), FontFactory.getFont(FontFactory.HELVETICA, 9))));
                                pdfTable.addCell(new PdfPCell(new Phrase(rs.getString("tipo_asistente"), FontFactory.getFont(FontFactory.HELVETICA, 9))));

                        }

                        document.add(pdfTable);

                        document.close();
                        JOptionPane.showMessageDialog(null, "PDF generado exitosamente en: " + fileToSave.getAbsolutePath());

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Error al generar el PDF.","Error",JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No se ha seleccionado una ruta.", "Error",JOptionPane.ERROR_MESSAGE);
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
                frame.setSize(600, 300);
                frame.setPreferredSize(new Dimension(600, 300));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });
    }
}
