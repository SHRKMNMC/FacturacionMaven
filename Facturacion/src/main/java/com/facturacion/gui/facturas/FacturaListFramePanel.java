package com.facturacion.gui.facturas;

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
                "ID", "Número", "Cliente", "Fecha", "Total (€)"
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

        // NUEVO: botón rectificar
        JButton btnRectificar = new JButton("Rectificar");
        btnRectificar.addActionListener(e -> rectificar());
        south.add(btnRectificar);

        // ❌ Eliminado botón eliminar (ya no se pueden borrar facturas)

        add(south, BorderLayout.SOUTH);

        cargarFacturas();
    }

    private void cargarFacturas() {
        tableModel.setRowCount(0);

        List<Factura> lista = facturaDAO.listarTodos();

        for (Factura f : lista) {
            tableModel.addRow(new Object[]{
                    f.getId(),
                    f.getNumeroFactura(),
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
                    f.getNumeroFactura(),
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

    // ============================
    // NUEVO: RECTIFICAR FACTURA
    // ============================
    private void rectificar() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una factura.");
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);
        Factura original = facturaDAO.buscarPorId(id);

        if (original == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la factura.");
            return;
        }

        // ❌ 1. No permitir rectificar una factura rectificativa
        if (original.isEsRectificativa()) {
            JOptionPane.showMessageDialog(this,
                    "No se puede rectificar una factura rectificativa.",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ❌ 2. No permitir rectificar una factura que ya ha sido rectificada
        if (original.getFacturaRectificada() != null) {
            JOptionPane.showMessageDialog(this,
                    "Esta factura ya ha sido rectificada.\nNo se puede rectificar de nuevo.",
                    "Operación no permitida",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✔ 3. Abrir panel de rectificación
        FacturaRectificativaPanel panel = new FacturaRectificativaPanel(original);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                "Factura rectificativa", true);

        dialog.setContentPane(panel);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        cargarFacturas();
    }

}
