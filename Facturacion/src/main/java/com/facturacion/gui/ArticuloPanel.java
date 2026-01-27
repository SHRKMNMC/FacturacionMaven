package com.facturacion.gui;

import com.facturacion.dao.ArticuloDAO;
import com.facturacion.entity.Articulo;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ArticuloPanel extends JPanel {

    private JTextField txtId, txtCodigo, txtNombre, txtFamilia, txtCategoria, txtUnidad;
    private JTextField txtProveedor, txtCodigoBarras, txtImagen, txtFechaAlta;
    private JTextArea txtDescripcion, txtObservaciones;
    private JFormattedTextField txtPrecioCoste, txtPrecioVenta;
    private JComboBox<Integer> cmbIva;
    private JSpinner spStockActual, spStockMinimo;
    private JCheckBox chkActivo;

    private ArticuloDAO articuloDAO = new ArticuloDAO();
    private Articulo articuloEditando = null;

    public ArticuloPanel() {
        setLayout(new BorderLayout(10, 10));
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
    }

    public void cargarArticulo(Articulo a) {
        this.articuloEditando = a;

        txtId.setText(a.getId() != null ? a.getId().toString() : "");
        txtCodigo.setText(a.getCodi());
        txtNombre.setText(a.getNom());
        txtDescripcion.setText(a.getDescripcio());
        txtFamilia.setText(a.getFamilia());
        txtCategoria.setText(a.getCategoria());
        txtUnidad.setText(a.getUnitat());
        txtProveedor.setText(a.getProveedor());
        txtPrecioCoste.setText(a.getPreuCost() != null ? a.getPreuCost().toString() : "");
        txtPrecioVenta.setText(a.getPreuVenda() != null ? a.getPreuVenda().toString() : "");
        cmbIva.setSelectedItem(a.getIvaPercent());
        spStockActual.setValue(a.getStockActual());
        spStockMinimo.setValue(a.getStockMinim());
        txtCodigoBarras.setText(a.getCodiBarres());
        chkActivo.setSelected(a.isActiu());
        txtImagen.setText(a.getImatgePath());
        txtFechaAlta.setText(a.getDataAlta() != null ? a.getDataAlta().toString() : "");
        txtObservaciones.setText(a.getObservacions());
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // Campos ocultos (NO se añaden al panel)
        txtId = new JTextField();
        txtId.setEditable(false);

        txtFechaAlta = new JTextField();
        txtFechaAlta.setEditable(false);

        // Campos visibles
        txtCodigo = new JTextField();
        txtNombre = new JTextField();
        txtDescripcion = new JTextArea(3, 20);
        txtFamilia = new JTextField();
        txtCategoria = new JTextField();
        txtUnidad = new JTextField();
        txtProveedor = new JTextField();
        txtPrecioCoste = new JFormattedTextField();
        txtPrecioVenta = new JFormattedTextField();
        cmbIva = new JComboBox<>(new Integer[]{0, 4, 10, 21});
        spStockActual = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        spStockMinimo = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        txtCodigoBarras = new JTextField();
        chkActivo = new JCheckBox("Activo", true);
        txtImagen = new JTextField();
        txtObservaciones = new JTextArea(3, 20);

        // ===================== COLUMNA 1 =====================
        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Código *"), gbc);
        gbc.gridx = 1;
        p.add(txtCodigo, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Nombre *"), gbc);
        gbc.gridx = 1;
        p.add(txtNombre, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Descripción"), gbc);
        gbc.gridx = 1;
        p.add(new JScrollPane(txtDescripcion), gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Familia *"), gbc);
        gbc.gridx = 1;
        p.add(txtFamilia, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Categoría *"), gbc);
        gbc.gridx = 1;
        p.add(txtCategoria, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Unidad *"), gbc);
        gbc.gridx = 1;
        p.add(txtUnidad, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Proveedor *"), gbc);
        gbc.gridx = 1;
        p.add(txtProveedor, gbc);
        fila++;

        // ===================== COLUMNA 2 =====================
        fila = 0;
        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Precio coste (€) *"), gbc);
        gbc.gridx = 3;
        p.add(txtPrecioCoste, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Precio venta (€) *"), gbc);
        gbc.gridx = 3;
        p.add(txtPrecioVenta, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("IVA (%) *"), gbc);
        gbc.gridx = 3;
        p.add(cmbIva, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Stock actual *"), gbc);
        gbc.gridx = 3;
        p.add(spStockActual, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Stock mínimo *"), gbc);
        gbc.gridx = 3;
        p.add(spStockMinimo, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Código barras"), gbc);
        gbc.gridx = 3;
        p.add(txtCodigoBarras, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Activo"), gbc);
        gbc.gridx = 3;
        p.add(chkActivo, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Imagen (ruta)"), gbc);
        gbc.gridx = 3;
        p.add(txtImagen, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Observaciones"), gbc);
        gbc.gridx = 3;
        p.add(new JScrollPane(txtObservaciones), gbc);
        fila++;

        return p;
    }

    private JPanel crearBotonera() {
        JPanel p = new JPanel();
        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardarArticulo());
        p.add(btnGuardar);
        return p;
    }

    private void guardarArticulo() {
        if (!validarFormulario()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El artículo NO se ha guardado.\nRevisa los datos introducidos.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            Articulo a = (articuloEditando != null) ? articuloEditando : new Articulo();

            a.setCodi(txtCodigo.getText().trim());
            a.setNom(txtNombre.getText().trim());
            a.setDescripcio(txtDescripcion.getText().trim());
            a.setFamilia(txtFamilia.getText().trim());
            a.setCategoria(txtCategoria.getText().trim());
            a.setUnitat(txtUnidad.getText().trim());
            a.setProveedor(txtProveedor.getText().trim());
            a.setPreuCost(new BigDecimal(txtPrecioCoste.getText().trim()));
            a.setPreuVenda(new BigDecimal(txtPrecioVenta.getText().trim()));
            a.setIvaPercent((Integer) cmbIva.getSelectedItem());
            a.setStockActual((Integer) spStockActual.getValue());
            a.setStockMinim((Integer) spStockMinimo.getValue());
            a.setCodiBarres(txtCodigoBarras.getText().trim().isEmpty() ? null : txtCodigoBarras.getText().trim());
            a.setActiu(chkActivo.isSelected());
            a.setImatgePath(txtImagen.getText().trim().isEmpty() ? null : txtImagen.getText().trim());
            a.setObservacions(txtObservaciones.getText().trim().isEmpty() ? null : txtObservaciones.getText().trim());

            articuloDAO.guardar(a);

            JOptionPane.showMessageDialog(this, "Artículo guardado correctamente");

            if (articuloEditando == null) {
                limpiarFormulario();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error de base de datos.\nEl artículo NO se ha guardado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private boolean validarFormulario() {
        if (txtCodigo.getText().trim().isEmpty()
                || txtNombre.getText().trim().isEmpty()
                || txtFamilia.getText().trim().isEmpty()
                || txtCategoria.getText().trim().isEmpty()
                || txtUnidad.getText().trim().isEmpty()
                || txtProveedor.getText().trim().isEmpty()
                || txtPrecioCoste.getText().trim().isEmpty()
                || txtPrecioVenta.getText().trim().isEmpty()) {
            return false;
        }

        try {
            BigDecimal coste = new BigDecimal(txtPrecioCoste.getText().trim());
            BigDecimal venta = new BigDecimal(txtPrecioVenta.getText().trim());
            if (coste.compareTo(BigDecimal.ZERO) < 0) return false;
            if (venta.compareTo(coste) < 0) return false;
        } catch (NumberFormatException e) {
            return false;
        }

        if (!txtCodigoBarras.getText().trim().isEmpty()
                && !Pattern.matches("\\d{13}", txtCodigoBarras.getText().trim())) {
            return false;
        }

        return true;
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtFamilia.setText("");
        txtCategoria.setText("");
        txtUnidad.setText("");
        txtProveedor.setText("");
        txtPrecioCoste.setText("");
        txtPrecioVenta.setText("");
        cmbIva.setSelectedIndex(0);
        spStockActual.setValue(0);
        spStockMinimo.setValue(0);
        txtCodigoBarras.setText("");
        chkActivo.setSelected(true);
        txtImagen.setText("");
        txtFechaAlta.setText("");
        txtObservaciones.setText("");
    }
}
