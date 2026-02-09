package com.facturacion.gui;

import com.facturacion.dao.FacturaDAO;
import com.facturacion.entity.Factura;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class FacturaListFramePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private FacturaDAO facturaDAO = new FacturaDAO();

    private final DecimalFormat df = new DecimalFormat("#0.00");

    public FacturaListFramePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 39, 42));

        df.setRoundingMode(RoundingMode.HALF_UP);

        tableModel = new DefaultTableModel() {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Cliente", "Fecha", "Total (€)"
        });

        table = new JTable(tableModel);
        table.setBackground(new Color(45, 49, 52));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(88, 101, 242));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(35, 39, 42));
        table.getTableHeader().setForeground(Color.WHITE);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel south = new JPanel();
        south.setBackground(new Color(35, 39, 42));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener(e -> cargarFacturas());
        south.add(btnActualizar);

        JButton btnVer = new JButton("Ver factura");
        btnVer.addActionListener(e -> ver());
        south.add(btnVer);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener(e -> eliminar());
        south.add(btnEliminar);

        add(south, BorderLayout.SOUTH);

        cargarFacturas();
    }

    private void cargarFacturas() {
        tableModel.setRowCount(0);

        List<Factura> lista = facturaDAO.listarTodos();

        for (Factura f : lista) {
            tableModel.addRow(new Object[]{
                    f.getId(),
                    f.getCliente().getNombre(),
                    f.getFecha(),
                    df.format(f.getTotalFactura())
            });
        }
    }

    public void mostrarSolo(List<Factura> lista) {
        tableModel.setRowCount(0);

        for (Factura f : lista) {
            tableModel.addRow(new Object[]{
                    f.getId(),
                    f.getCliente().getNombre(),
                    f.getFecha(),
                    df.format(f.getTotalFactura())
            });
        }
    }

    private void ver() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura.");
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);
        Factura f = facturaDAO.buscarPorId(id);

        if (f == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la factura.");
            return;
        }

        FacturaPanel panel = new FacturaPanel();
        panel.cargarFactura(f);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Factura", true);
        dialog.setContentPane(panel);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void eliminar() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura.");
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar la factura con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        facturaDAO.eliminar(id);
        cargarFacturas();
    }
}
