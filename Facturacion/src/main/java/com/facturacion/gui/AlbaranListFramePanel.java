package com.facturacion.gui;

import com.facturacion.dao.AlbaranDAO;
import com.facturacion.dao.FacturaDAO;
import com.facturacion.entity.Albaran;
import com.facturacion.entity.Factura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AlbaranListFramePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private AlbaranDAO albaranDAO = new AlbaranDAO();
    private FacturaDAO facturaDAO = new FacturaDAO();

    public AlbaranListFramePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 39, 42));

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Cliente", "Fecha", "Líneas", "Convertido"
        });

        table = new JTable(tableModel) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        table.setBackground(new Color(45, 49, 52));
        table.setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setBackground(new Color(35, 39, 42));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarAlbaranes());
        south.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminar());
        south.add(btnEliminar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.addActionListener(e -> editar());
        south.add(btnEditar);

        JButton btnConvertir = new JButton("Convertir a factura");
        btnConvertir.addActionListener(e -> convertir());
        south.add(btnConvertir);

        add(south, BorderLayout.SOUTH);

        cargarAlbaranes();
    }

    private void cargarAlbaranes() {
        tableModel.setRowCount(0);

        List<Albaran> lista = albaranDAO.listarTodos();

        for (Albaran a : lista) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getCliente().getNombre(),
                    a.getFecha(),
                    a.getLineas().size(),
                    a.isConvertidoFactura() ? "Sí" : "No"
            });
        }
    }

    public void mostrarSolo(List<Albaran> lista) {
        tableModel.setRowCount(0);

        for (Albaran a : lista) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getCliente().getNombre(),
                    a.getFecha(),
                    a.getLineas().size(),
                    a.isConvertidoFactura() ? "Sí" : "No"
            });
        }
    }

    private void eliminar() {
        int fila = table.getSelectedRow();
        if (fila == -1) return;

        int id = (int) tableModel.getValueAt(fila, 0);

        albaranDAO.eliminar(id);
        cargarAlbaranes();
    }

    private void editar() {
        int fila = table.getSelectedRow();
        if (fila == -1) return;

        int id = (int) tableModel.getValueAt(fila, 0);
        Albaran a = albaranDAO.buscarPorId(id);

        AlbaranPanel panel = new AlbaranPanel();
        panel.cargarAlbaran(a);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Albarán", true);
        dialog.setContentPane(panel);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        cargarAlbaranes();
    }

    private void convertir() {
        int fila = table.getSelectedRow();
        if (fila == -1) return;

        int id = (int) tableModel.getValueAt(fila, 0);
        Albaran a = albaranDAO.buscarPorId(id);

        if (a.isConvertidoFactura()) {
            JOptionPane.showMessageDialog(this, "Este albarán ya está convertido.");
            return;
        }

        Factura f = facturaDAO.crearDesdeAlbaran(a);

        JOptionPane.showMessageDialog(this, "Factura creada con ID: " + f.getId());

        cargarAlbaranes();
    }
}
