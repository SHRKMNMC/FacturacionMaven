package com.facturacion.gui;

import com.facturacion.dao.ClienteDAO;
import com.facturacion.entity.Cliente;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class ClientePanel extends JPanel {

    private JTextField txtNombre, txtDni, txtDireccion, txtPoblacion;
    private JTextField txtCodigoPostal, txtTelefonoFijo, txtTelefonoMovil;
    private JTextField txtEmail, txtWeb, txtCuenta, txtImagen;
    private JTextArea txtObservaciones;
    private JFormattedTextField txtLimiteCredito;
    private JComboBox<String> cmbProvincia, cmbMetodoPago;
    private JCheckBox chkActivo;
    private ClienteDAO clienteDAO = new ClienteDAO();

    // 🔥 NUEVO: cliente en edición
    private Cliente clienteEditando = null;

    public ClientePanel() {
        setLayout(new BorderLayout(10, 10));
        add(crearFormulario(), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
    }

    // 🔥 NUEVO: cargar datos en modo edición
    public void cargarCliente(Cliente c) {
        this.clienteEditando = c;

        txtNombre.setText(c.getNombre());
        txtDni.setText(c.getDni());
        txtDireccion.setText(c.getDireccion());
        txtPoblacion.setText(c.getPoblacion());
        cmbProvincia.setSelectedItem(c.getProvincia());
        txtCodigoPostal.setText(c.getCodigoPostal());
        txtTelefonoFijo.setText(c.getTelefonoFijo());
        txtTelefonoMovil.setText(c.getTelefonoMovil());
        txtEmail.setText(c.getCorreoElectronico());
        txtWeb.setText(c.getPaginaWeb());
        cmbMetodoPago.setSelectedItem(c.getMetodoPago());
        txtLimiteCredito.setText(c.getLimiteCredito() != null ? c.getLimiteCredito().toString() : "");
        txtCuenta.setText(c.getNumCuentaBancaria());
        txtImagen.setText(c.getImagenPath());
        txtObservaciones.setText(c.getObservaciones());
        chkActivo.setSelected(c.isActivo());
    }

    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int fila = 0;

        // ===================== COLUMNA 1 =====================
        txtNombre = new JTextField();
        txtDni = new JTextField();
        txtDireccion = new JTextField();
        txtPoblacion = new JTextField();
        txtCodigoPostal = new JTextField();
        chkActivo = new JCheckBox("Activo", true);
        cmbProvincia = new JComboBox<>(PROVINCIAS);
        cmbProvincia.setSelectedIndex(-1);

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Nombre *"), gbc);
        gbc.gridx = 1;
        p.add(txtNombre, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("DNI *"), gbc);
        gbc.gridx = 1;
        p.add(txtDni, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Dirección *"), gbc);
        gbc.gridx = 1;
        p.add(txtDireccion, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Población *"), gbc);
        gbc.gridx = 1;
        p.add(txtPoblacion, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Provincia *"), gbc);
        gbc.gridx = 1;
        p.add(cmbProvincia, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Código Postal"), gbc);
        gbc.gridx = 1;
        p.add(txtCodigoPostal, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Activo"), gbc);
        gbc.gridx = 1;
        p.add(chkActivo, gbc);
        fila++;

        // ===================== COLUMNA 2 =====================
        txtTelefonoFijo = new JTextField();
        txtTelefonoMovil = new JTextField();
        txtEmail = new JTextField();
        txtWeb = new JTextField();
        cmbMetodoPago = new JComboBox<>(new String[]{"contado", "credito"});
        txtLimiteCredito = new JFormattedTextField();
        txtCuenta = new JTextField();
        txtImagen = new JTextField();
        txtObservaciones = new JTextArea(5, 20);

        fila = 0;
        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Teléfono fijo"), gbc);
        gbc.gridx = 3;
        p.add(txtTelefonoFijo, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Teléfono móvil"), gbc);
        gbc.gridx = 3;
        p.add(txtTelefonoMovil, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Correo electrónico"), gbc);
        gbc.gridx = 3;
        p.add(txtEmail, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Página web"), gbc);
        gbc.gridx = 3;
        p.add(txtWeb, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Método de pago"), gbc);
        gbc.gridx = 3;
        p.add(cmbMetodoPago, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Límite de crédito"), gbc);
        gbc.gridx = 3;
        p.add(txtLimiteCredito, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Cuenta bancaria"), gbc);
        gbc.gridx = 3;
        p.add(txtCuenta, gbc);
        fila++;

        gbc.gridx = 2; gbc.gridy = fila;
        p.add(new JLabel("Ruta imagen"), gbc);
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
        btnGuardar.addActionListener(e -> guardarCliente());
        p.add(btnGuardar);
        return p;
    }

    // ===================== GUARDAR =====================
    private void guardarCliente() {

        if (!validarFormulario()) {
            JOptionPane.showMessageDialog(
                    this,
                    "El cliente NO se ha guardado.\nRevisa los datos introducidos.",
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            // 🔥 Si estamos editando, usamos el mismo objeto
            Cliente c = (clienteEditando != null) ? clienteEditando : new Cliente();

            c.setNombre(txtNombre.getText().trim());
            c.setDni(txtDni.getText().trim());
            c.setDireccion(txtDireccion.getText().trim());
            c.setPoblacion(txtPoblacion.getText().trim());
            c.setProvincia(cmbProvincia.getSelectedItem().toString());
            c.setCodigoPostal(txtCodigoPostal.getText().trim());
            c.setTelefonoFijo(txtTelefonoFijo.getText().trim());
            c.setTelefonoMovil(txtTelefonoMovil.getText().trim());
            c.setCorreoElectronico(txtEmail.getText().trim());
            c.setPaginaWeb(txtWeb.getText().trim());
            c.setMetodoPago(cmbMetodoPago.getSelectedItem().toString());
            c.setNumCuentaBancaria(txtCuenta.getText().trim());
            c.setObservaciones(txtObservaciones.getText().trim());
            c.setImagenPath(txtImagen.getText().trim());
            c.setActivo(chkActivo.isSelected());

            if (!txtLimiteCredito.getText().trim().isEmpty()) {
                c.setLimiteCredito(new BigDecimal(txtLimiteCredito.getText()));
            }

            clienteDAO.guardar(c);

            JOptionPane.showMessageDialog(this, "Cliente guardado correctamente");

            if (clienteEditando == null) {
                limpiarFormulario();
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error de base de datos.\nEl cliente NO se ha guardado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private boolean validarFormulario() {

        if (txtNombre.getText().trim().isEmpty()
                || txtDni.getText().trim().isEmpty()
                || txtDireccion.getText().trim().isEmpty()
                || txtPoblacion.getText().trim().isEmpty()
                || cmbProvincia.getSelectedIndex() == -1) {
            return false;
        }

        if (!txtCodigoPostal.getText().trim().isEmpty()
                && !txtCodigoPostal.getText().matches("\\d{5}")) {
            return false;
        }

        if (!validarTelefono(txtTelefonoFijo.getText())
                || !validarTelefono(txtTelefonoMovil.getText())) {
            return false;
        }

        if (!txtEmail.getText().trim().isEmpty()
                && !Pattern.matches("^[^@]+@[^@]+\\.[^@]+$", txtEmail.getText())) {
            return false;
        }

        try {
            if (!txtLimiteCredito.getText().trim().isEmpty()) {
                BigDecimal lim = new BigDecimal(txtLimiteCredito.getText());
                if (lim.compareTo(BigDecimal.ZERO) < 0
                        || lim.compareTo(new BigDecimal("1000000")) > 0) {
                    return false;
                }
            }
        } catch (NumberFormatException e) {
            return false;
        }

        return true;
    }

    private boolean validarTelefono(String t) {
        return t.trim().isEmpty() || t.matches("\\d{9}");
    }

    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDni.setText("");
        txtDireccion.setText("");
        txtPoblacion.setText("");
        txtCodigoPostal.setText("");
        txtTelefonoFijo.setText("");
        txtTelefonoMovil.setText("");
        txtEmail.setText("");
        txtWeb.setText("");
        txtCuenta.setText("");
        txtImagen.setText("");
        txtObservaciones.setText("");
        txtLimiteCredito.setText("");
        chkActivo.setSelected(true);
        cmbProvincia.setSelectedIndex(-1);
        cmbMetodoPago.setSelectedIndex(0);
    }

    private static final String[] PROVINCIAS = {
            "Álava","Albacete","Alicante","Almería","Asturias","Ávila",
            "Badajoz","Barcelona","Burgos","Cáceres","Cádiz","Cantabria",
            "Castellón","Ciudad Real","Córdoba","Cuenca","Girona","Granada",
            "Guadalajara","Guipúzcoa","Huelva","Huesca","Illes Balears","Jaén",
            "La Coruña","La Rioja","Las Palmas","León","Lleida","Lugo",
            "Madrid","Málaga","Murcia","Navarra","Ourense","Palencia",
            "Pontevedra","Salamanca","Santa Cruz de Tenerife","Segovia",
            "Sevilla","Soria","Tarragona","Teruel","Toledo","Valencia",
            "Valladolid","Vizcaya","Zamora","Zaragoza"
    };
}
