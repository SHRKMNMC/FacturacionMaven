package com.facturacion.gui.clientes;

import com.facturacion.entity.Cliente;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ClienteFichaPanel extends JPanel {

    private JTextField txtNombre, txtDni, txtDireccion, txtPoblacion, txtProvincia;
    private JTextField txtCodigoPostal, txtTelefonoFijo, txtTelefonoMovil;
    private JTextField txtEmail, txtWeb, txtMetodoPago, txtLimiteCredito;
    private JTextField txtCuenta, txtActivo;
    private JTextArea txtObservaciones;

    private JLabel lblFoto;

    public ClienteFichaPanel() {
        setLayout(new BorderLayout(10, 10));
        add(crearFormulario(), BorderLayout.CENTER);
    }

    public void cargarCliente(Cliente c) {
        txtNombre.setText(c.getNombre());
        txtDni.setText(c.getDni());
        txtDireccion.setText(c.getDireccion());
        txtPoblacion.setText(c.getPoblacion());
        txtProvincia.setText(c.getProvincia());
        txtCodigoPostal.setText(c.getCodigoPostal());
        txtTelefonoFijo.setText(c.getTelefonoFijo());
        txtTelefonoMovil.setText(c.getTelefonoMovil());
        txtEmail.setText(c.getCorreoElectronico());
        txtWeb.setText(c.getPaginaWeb());
        txtMetodoPago.setText(c.getMetodoPago());
        txtLimiteCredito.setText(c.getLimiteCredito() != null ? c.getLimiteCredito().toString() : "");
        txtCuenta.setText(c.getNumCuentaBancaria());
        txtActivo.setText(c.isActivo() ? "Sí" : "No");
        txtObservaciones.setText(c.getObservaciones());

        cargarImagen(c.getImagenPath());
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        txtNombre = crearCampo();
        txtDni = crearCampo();
        txtDireccion = crearCampo();
        txtPoblacion = crearCampo();
        txtProvincia = crearCampo();
        txtCodigoPostal = crearCampo();
        txtTelefonoFijo = crearCampo();
        txtTelefonoMovil = crearCampo();
        txtEmail = crearCampo();
        txtWeb = crearCampo();
        txtMetodoPago = crearCampo();
        txtLimiteCredito = crearCampo();
        txtCuenta = crearCampo();
        txtActivo = crearCampo();

        txtObservaciones = new JTextArea(6, 20);
        txtObservaciones.setEditable(false);
        txtObservaciones.setLineWrap(true);
        txtObservaciones.setWrapStyleWord(true);

        // FOTO
        lblFoto = new JLabel();
        lblFoto.setPreferredSize(new Dimension(140, 140));
        lblFoto.setOpaque(true);
        lblFoto.setBackground(Color.LIGHT_GRAY);
        lblFoto.setHorizontalAlignment(SwingConstants.CENTER);
        lblFoto.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY));

        int fila = 0;

        // FOTO — columna fija con espacio reservado
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
        añadirCampo(p, gbc, fila++, "Nombre", txtNombre, 0);
        añadirCampo(p, gbc, fila++, "DNI", txtDni, 0);
        añadirCampo(p, gbc, fila++, "Dirección", txtDireccion, 0);
        añadirCampo(p, gbc, fila++, "Población", txtPoblacion, 0);
        añadirCampo(p, gbc, fila++, "Provincia", txtProvincia, 0);
        añadirCampo(p, gbc, fila++, "Código Postal", txtCodigoPostal, 0);
        añadirCampo(p, gbc, fila++, "Teléfono Fijo", txtTelefonoFijo, 0);
        añadirCampo(p, gbc, fila++, "Teléfono Móvil", txtTelefonoMovil, 0);

        // COLUMNA DERECHA
        int filaDer = 0;
        añadirCampo(p, gbc, filaDer++, "Email", txtEmail, 2);
        añadirCampo(p, gbc, filaDer++, "Web", txtWeb, 2);
        añadirCampo(p, gbc, filaDer++, "Método Pago", txtMetodoPago, 2);
        añadirCampo(p, gbc, filaDer++, "Límite Crédito", txtLimiteCredito, 2);
        añadirCampo(p, gbc, filaDer++, "Cuenta Bancaria", txtCuenta, 2);
        añadirCampo(p, gbc, filaDer++, "Activo", txtActivo, 2);

        // OBSERVACIONES
        gbc.gridx = 0;
        gbc.gridy = Math.max(fila, filaDer) + 1;
        gbc.gridwidth = 1;
        p.add(new JLabel("Observaciones:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 4;
        gbc.weighty = 1;
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
