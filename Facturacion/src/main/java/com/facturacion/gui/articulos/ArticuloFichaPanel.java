package com.facturacion.gui.articulos;

import com.facturacion.entity.Articulo;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ArticuloFichaPanel extends JPanel {

    private JTextField txtCodigo, txtNombre, txtFamilia, txtCategoria, txtUnidad;
    private JTextField txtProveedor, txtPrecioCoste, txtPrecioVenta, txtIva;
    private JTextField txtStockActual, txtStockMinimo, txtCodigoBarras, txtActivo;
    private JTextArea txtDescripcion, txtObservaciones;
    private JLabel lblFoto;

    public ArticuloFichaPanel() {
        setLayout(new BorderLayout(10, 10));
        add(crearFormulario(), BorderLayout.CENTER);
    }

    public void cargarArticulo(Articulo a) {
        txtCodigo.setText(a.getCodi());
        txtNombre.setText(a.getNom());
        txtFamilia.setText(a.getFamilia());
        txtCategoria.setText(a.getCategoria());
        txtUnidad.setText(a.getUnitat());
        txtProveedor.setText(a.getProveedor());
        txtPrecioCoste.setText(a.getPreuCost() != null ? a.getPreuCost().toString() : "");
        txtPrecioVenta.setText(a.getPreuVenda() != null ? a.getPreuVenda().toString() : "");
        txtIva.setText(a.getIvaPercent() != null ? a.getIvaPercent().toString() : "");
        txtStockActual.setText(a.getStockActual() != null ? a.getStockActual().toString() : "");
        txtStockMinimo.setText(a.getStockMinim() != null ? a.getStockMinim().toString() : "");
        txtCodigoBarras.setText(a.getCodiBarres());
        txtActivo.setText(a.isActiu() ? "Sí" : "No");
        txtDescripcion.setText(a.getDescripcio());
        txtObservaciones.setText(a.getObservacions());

        cargarImagen(a.getImatgePath());
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtCodigo = crearCampo();
        txtNombre = crearCampo();
        txtFamilia = crearCampo();
        txtCategoria = crearCampo();
        txtUnidad = crearCampo();
        txtProveedor = crearCampo();
        txtPrecioCoste = crearCampo();
        txtPrecioVenta = crearCampo();
        txtIva = crearCampo();
        txtStockActual = crearCampo();
        txtStockMinimo = crearCampo();
        txtCodigoBarras = crearCampo();
        txtActivo = crearCampo();

        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setEditable(false);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);

        txtObservaciones = new JTextArea(4, 20);
        txtObservaciones.setEditable(false);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(140, 140));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.LIGHT_GRAY);
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        int fila = 0;

        // FOTO
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 5;
        gbc.weightx = 0.2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.NORTH;
        p.add(lblFoto, gbc);

        gbc.gridheight = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // COLUMNA IZQUIERDA
        añadirCampo(p, gbc, fila++, "Código", txtCodigo, 0);
        añadirCampo(p, gbc, fila++, "Nombre", txtNombre, 0);
        añadirCampo(p, gbc, fila++, "Familia", txtFamilia, 0);
        añadirCampo(p, gbc, fila++, "Categoría", txtCategoria, 0);
        añadirCampo(p, gbc, fila++, "Unidad", txtUnidad, 0);
        añadirCampo(p, gbc, fila++, "Proveedor", txtProveedor, 0);

        // COLUMNA DERECHA
        int filaDer = 0;
        añadirCampo(p, gbc, filaDer++, "Precio Coste", txtPrecioCoste, 2);
        añadirCampo(p, gbc, filaDer++, "Precio Venta", txtPrecioVenta, 2);
        añadirCampo(p, gbc, filaDer++, "IVA", txtIva, 2);
        añadirCampo(p, gbc, filaDer++, "Stock Actual", txtStockActual, 2);
        añadirCampo(p, gbc, filaDer++, "Stock Mínimo", txtStockMinimo, 2);
        añadirCampo(p, gbc, filaDer++, "Código Barras", txtCodigoBarras, 2);
        añadirCampo(p, gbc, filaDer++, "Activo", txtActivo, 2);

        // DESCRIPCIÓN
        gbc.gridx = 0;
        gbc.gridy = Math.max(fila, filaDer) + 1;
        p.add(new JLabel("Descripción:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        p.add(new JScrollPane(txtDescripcion), gbc);

        // OBSERVACIONES
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(new JLabel("Observaciones:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        p.add(new JScrollPane(txtObservaciones), gbc);

        return p;
    }

    private JTextField crearCampo() {
        JTextField t = new JTextField();
        t.setEditable(false);
        t.setPreferredSize(new Dimension(250, 28));
        return t;
    }

    private void añadirCampo(JPanel p, GridBagConstraints gbc, int fila, String label, JComponent campo, int col) {
        gbc.gridx = col;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(new JLabel(label + ":"), gbc);

        gbc.gridx = col + 1;
        gbc.gridwidth = 1;
        p.add(campo, gbc);
    }

    private void cargarImagen(String path) {
        lblFoto.setIcon(null);

        if (path == null || path.trim().isEmpty()) return;

        File f = new File(path);
        if (!f.exists() || !f.isFile()) return;

        try {
            ImageIcon icon = new ImageIcon(path);
            Image img = icon.getImage().getScaledInstance(
                    140, 140, Image.SCALE_SMOOTH
            );
            lblFoto.setIcon(new ImageIcon(img));
        } catch (Exception ignored) {}
    }
}
