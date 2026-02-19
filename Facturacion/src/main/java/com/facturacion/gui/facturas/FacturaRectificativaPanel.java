package com.facturacion.gui.facturas;

import com.facturacion.dao.FacturaDAO;
import com.facturacion.entity.Factura;

import javax.swing.*;
import java.awt.*;

public class FacturaRectificativaPanel extends JPanel {

    private final Factura facturaOriginal;
    private final FacturaDAO facturaDAO = new FacturaDAO();

    public FacturaRectificativaPanel(Factura facturaOriginal) {
        this.facturaOriginal = facturaOriginal;

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(35, 39, 42));

        add(crearCabecera(), BorderLayout.NORTH);
        add(crearContenido(), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
    }

    private JPanel crearCabecera() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(45, 49, 52));
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titulo = new JLabel("Crear factura rectificativa");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));

        p.add(titulo, BorderLayout.WEST);
        return p;
    }

    private JPanel crearContenido() {
        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.setBackground(new Color(35, 39, 42));

        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(35, 39, 42));
        p.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JLabel lbl1 = crearLabel("Factura original:");
        JLabel lbl2 = crearLabel("Cliente:");
        JLabel lbl3 = crearLabel("Fecha:");
        JLabel lbl4 = crearLabel("Total:");
        JLabel lbl5 = crearLabel("Tipo de rectificación:");

        JLabel v1 = crearValor(facturaOriginal.getNumeroFactura());
        JLabel v2 = crearValor(facturaOriginal.getCliente().getNombre());
        JLabel v3 = crearValor(facturaOriginal.getFecha().toString());
        JLabel v4 = crearValor(facturaOriginal.getTotalFactura() + " €");
        JLabel v5 = crearValor("Rectificación total (todas las líneas en negativo)");

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(lbl1, gbc);
        gbc.gridx = 1;
        p.add(v1, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(lbl2, gbc);
        gbc.gridx = 1;
        p.add(v2, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(lbl3, gbc);
        gbc.gridx = 1;
        p.add(v3, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(lbl4, gbc);
        gbc.gridx = 1;
        p.add(v4, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(lbl5, gbc);
        gbc.gridx = 1;
        p.add(v5, gbc);

        // 🔥 CLAVE: alineación a la izquierda
        contenedor.add(p, BorderLayout.WEST);

        return contenedor;
    }

    private JLabel crearLabel(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        return l;
    }

    private JLabel crearValor(String txt) {
        JLabel l = new JLabel(txt);
        l.setForeground(Color.WHITE);
        l.setFont(new Font("SansSerif", Font.PLAIN, 14));

        l.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(88, 101, 242)),
                BorderFactory.createEmptyBorder(2, 6, 2, 6)
        ));

        return l;
    }

    private JPanel crearBotonera() {
        JPanel p = new JPanel();
        p.setBackground(new Color(35, 39, 42));

        JButton btnCrear = new JButton("Generar rectificativa");
        JButton btnCancelar = new JButton("Cancelar");

        btnCrear.addActionListener(e -> crearRectificativa());
        btnCancelar.addActionListener(e -> cerrar());

        p.add(btnCrear);
        p.add(btnCancelar);

        return p;
    }

    private void crearRectificativa() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Se generará una factura rectificativa TOTAL.\n" +
                        "Esto anulará completamente la factura original.\n\n" +
                        "¿Deseas continuar?",
                "Confirmar rectificación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        Factura rect = facturaDAO.crearRectificativa(facturaOriginal);

        if (rect == null) {
            JOptionPane.showMessageDialog(this,
                    "Error al crear la factura rectificativa.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Factura rectificativa creada correctamente.\nNúmero: " +
                        rect.getNumeroFactura(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

        cerrar();
    }

    private void cerrar() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }
}
