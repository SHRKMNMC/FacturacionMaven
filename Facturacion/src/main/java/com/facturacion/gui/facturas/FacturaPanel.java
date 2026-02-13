package com.facturacion.gui.facturas;

import com.facturacion.entity.Factura;
import com.facturacion.entity.FacturaLinea;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class FacturaPanel extends JPanel {

    private JTextField txtNumero, txtCliente, txtFecha, txtTotal, txtRectifica;
    private JTable table;
    private DefaultTableModel tableModel;

    public FacturaPanel() {
        setLayout(new BorderLayout(10, 10));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
    }

    public void cargarFactura(Factura f) {

        // Número de factura (incluye sufijo R si es rectificativa)
        txtNumero.setText(f.getNumeroFactura());

        txtCliente.setText(f.getCliente().getNombre());
        txtFecha.setText(f.getFecha().toString());
        txtTotal.setText(f.getTotalFactura().toString());

        // Si es rectificativa, mostrar a quién rectifica
        if (f.isEsRectificativa() && f.getFacturaRectificada() != null) {
            txtRectifica.setText(f.getFacturaRectificada().getNumeroFactura());
        } else {
            txtRectifica.setText("—");
        }

        tableModel.setRowCount(0);

        for (FacturaLinea l : f.getLineas()) {

            BigDecimal precio = l.getPrecioUnitario();
            BigDecimal total = precio.multiply(BigDecimal.valueOf(l.getCantidad()));

            tableModel.addRow(new Object[]{
                    l.getArticulo().getNom(),
                    l.getCantidad(),
                    precio,
                    total,
                    l.getIvaPercent()
            });
        }
    }

    private JPanel crearCabecera() {
        JPanel p = new JPanel(new GridLayout(5, 2, 5, 5));

        txtNumero = new JTextField(); txtNumero.setEditable(false);
        txtCliente = new JTextField(); txtCliente.setEditable(false);
        txtFecha = new JTextField(); txtFecha.setEditable(false);
        txtTotal = new JTextField(); txtTotal.setEditable(false);
        txtRectifica = new JTextField(); txtRectifica.setEditable(false);

        p.add(new JLabel("Número factura:"));
        p.add(txtNumero);

        p.add(new JLabel("Cliente:"));
        p.add(txtCliente);

        p.add(new JLabel("Fecha:"));
        p.add(txtFecha);

        p.add(new JLabel("Total factura:"));
        p.add(txtTotal);

        p.add(new JLabel("Rectifica a:"));
        p.add(txtRectifica);

        return p;
    }

    private JScrollPane crearTabla() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        tableModel.setColumnIdentifiers(new Object[]{
                "Artículo", "Cantidad", "Precio unitario", "Total", "IVA"
        });

        table = new JTable(tableModel);

        return new JScrollPane(table);
    }
}
