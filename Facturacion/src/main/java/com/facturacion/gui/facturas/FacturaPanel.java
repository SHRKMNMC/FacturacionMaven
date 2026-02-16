package com.facturacion.gui.facturas;

import com.facturacion.entity.Factura;
import com.facturacion.entity.FacturaLinea;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class FacturaPanel extends JPanel {

    private JTextField txtNumero, txtCliente, txtFecha, txtRectifica;
    private JTextField txtBase, txtIva, txtTotal;
    private JTable table;
    private DefaultTableModel tableModel;

    public FacturaPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 39, 42));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearPie(), BorderLayout.SOUTH);
    }

    public void cargarFactura(Factura f) {

        txtNumero.setText(f.getNumeroFactura());
        txtCliente.setText(f.getCliente().getNombre());
        txtFecha.setText(f.getFecha().toString());

        if (f.isEsRectificativa() && f.getFacturaRectificada() != null) {
            txtRectifica.setText(f.getFacturaRectificada().getNumeroFactura());
        } else {
            txtRectifica.setText("—");
        }

        txtBase.setText(f.getTotalBase().toString());
        txtIva.setText(f.getTotalIva().toString());
        txtTotal.setText(f.getTotalFactura().toString());

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
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(35, 39, 42));

        JPanel datos = new JPanel(new GridBagLayout());
        datos.setBackground(new Color(45, 49, 52));
        datos.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(88, 101, 242)),
                "Datos de la factura",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                Color.WHITE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 10, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        txtNumero = crearCampo();
        txtCliente = crearCampo();
        txtFecha = crearCampo();
        txtRectifica = crearCampo();

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        datos.add(crearLabel("Número factura:"), gbc);
        gbc.gridx = 1;
        datos.add(txtNumero, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        datos.add(crearLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        datos.add(txtCliente, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        datos.add(crearLabel("Fecha:"), gbc);
        gbc.gridx = 1;
        datos.add(txtFecha, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        datos.add(crearLabel("Rectifica a:"), gbc);
        gbc.gridx = 1;
        datos.add(txtRectifica, gbc);

        contenedor.add(datos, BorderLayout.CENTER);

        return contenedor;
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
        table.setBackground(new Color(45, 49, 52));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(88, 101, 242));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(35, 39, 42));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scroll = new JScrollPane(table);
        scroll.getViewport().setBackground(new Color(35, 39, 42));
        return scroll;
    }

    private JPanel crearPie() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(35, 39, 42));
        p.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(88, 101, 242)),
                "Resumen de importes",
                TitledBorder.RIGHT,
                TitledBorder.TOP,
                new Font("SansSerif", Font.BOLD, 12),
                Color.WHITE
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 10, 4, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // 🔥 Igual que arriba: NO expandir horizontalmente
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        txtBase = crearCampo();
        txtIva = crearCampo();
        txtTotal = crearCampo();

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(crearLabel("Base imponible:"), gbc);
        gbc.gridx = 1;
        p.add(txtBase, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(crearLabel("IVA:"), gbc);
        gbc.gridx = 1;
        p.add(txtIva, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(crearLabel("Total factura:"), gbc);
        gbc.gridx = 1;
        p.add(txtTotal, gbc);

        return p;
    }

    private JLabel crearLabel(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.PLAIN, 13));
        return l;
    }

    private JTextField crearCampo() {
        JTextField t = new JTextField();
        t.setEditable(false);
        t.setBackground(new Color(30, 33, 36));
        t.setForeground(Color.WHITE);

        // 🔥 Ajuste clave: padding mínimo + borde fino
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(88, 101, 242)),
                BorderFactory.createEmptyBorder(1, 4, 1, 4)
        ));

        return t;
    }
}
