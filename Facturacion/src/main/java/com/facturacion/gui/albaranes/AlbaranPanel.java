package com.facturacion.gui.albaranes;

import com.facturacion.dao.AlbaranDAO;
import com.facturacion.dao.ArticuloDAO;
import com.facturacion.dao.ClienteDAO;
import com.facturacion.entity.Albaran;
import com.facturacion.entity.AlbaranLinea;
import com.facturacion.entity.Articulo;
import com.facturacion.entity.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;

public class AlbaranPanel extends JPanel {

    private JComboBox<Cliente> cmbCliente;
    private JComboBox<Articulo> cmbArticulo;
    private JSpinner spCantidad;
    private JFormattedTextField txtPrecioUnitario;
    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnGuardar;
    private JButton btnEliminarLinea;
    private JButton btnAddLinea;

    private Albaran albaranEditando = null;

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ArticuloDAO articuloDAO = new ArticuloDAO();
    private final AlbaranDAO albaranDAO = new AlbaranDAO();

    public AlbaranPanel() {
        setLayout(new BorderLayout(10, 10));

        add(crearFormulario(), BorderLayout.NORTH);
        add(crearTabla(), BorderLayout.CENTER);
        add(crearBotonera(), BorderLayout.SOUTH);
    }

    // ============================
    // CARGAR ALBARÁN EN MODO EDICIÓN
    // ============================
    public void cargarAlbaran(Albaran a) {
        this.albaranEditando = a;

        cmbCliente.setSelectedItem(a.getCliente());

        tableModel.setRowCount(0);
        for (AlbaranLinea l : a.getLineas()) {
            BigDecimal total = l.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(l.getCantidad()));
            tableModel.addRow(new Object[]{
                    l.getArticulo(),
                    l.getCantidad(),
                    l.getPrecioUnitario(),
                    total,
                    l.getIvaPercent()
            });
        }

        // 🔒 BLOQUEAR EDICIÓN SI YA ESTÁ FACTURADO
        if (a.estaBloqueado()) {
            bloquearEdicion();
        }
    }

    // ============================
    // FORMULARIO SUPERIOR
    // ============================
    private JPanel crearFormulario() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbCliente = new JComboBox<>(clienteDAO.listarTodos().toArray(new Cliente[0]));
        cmbArticulo = new JComboBox<>(articuloDAO.listarTodos().toArray(new Articulo[0]));
        cmbArticulo.addActionListener(e -> actualizarPrecioUnitario());

        spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 9999, 1));

        txtPrecioUnitario = new JFormattedTextField();
        txtPrecioUnitario.setColumns(8);
        txtPrecioUnitario.setEditable(false);

        int fila = 0;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Cliente *"), gbc);
        gbc.gridx = 1;
        p.add(cmbCliente, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Artículo *"), gbc);
        gbc.gridx = 1;
        p.add(cmbArticulo, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Cantidad *"), gbc);
        gbc.gridx = 1;
        p.add(spCantidad, gbc);
        fila++;

        gbc.gridx = 0; gbc.gridy = fila;
        p.add(new JLabel("Precio unitario (€)"), gbc);
        gbc.gridx = 1;
        p.add(txtPrecioUnitario, gbc);
        fila++;

        btnAddLinea = new JButton("Añadir línea");
        btnAddLinea.addActionListener(e -> agregarLinea());

        gbc.gridx = 1; gbc.gridy = fila;
        p.add(btnAddLinea, gbc);

        actualizarPrecioUnitario();

        return p;
    }

    // ============================
    // TABLA DE LÍNEAS
    // ============================
    private JScrollPane crearTabla() {
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(new Object[]{
                "Artículo", "Cantidad", "Precio unitario", "Total", "IVA"
        });

        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    // ============================
    // BOTONERA INFERIOR
    // ============================
    private JPanel crearBotonera() {
        JPanel p = new JPanel();

        btnGuardar = new JButton("Guardar albarán");
        btnGuardar.addActionListener(e -> guardarAlbaran());
        p.add(btnGuardar);

        btnEliminarLinea = new JButton("Eliminar línea");
        btnEliminarLinea.addActionListener(e -> eliminarLinea());
        p.add(btnEliminarLinea);

        return p;
    }

    // ============================
    // AÑADIR LÍNEA
    // ============================
    private void agregarLinea() {
        Articulo art = (Articulo) cmbArticulo.getSelectedItem();
        if (art == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un artículo.");
            return;
        }

        int cantidad = (Integer) spCantidad.getValue();
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que 0.");
            return;
        }

        BigDecimal precioUnitario = art.getPreuVenda() != null
                ? art.getPreuVenda()
                : BigDecimal.ZERO;

        BigDecimal total = precioUnitario.multiply(BigDecimal.valueOf(cantidad));

        tableModel.addRow(new Object[]{
                art,
                cantidad,
                precioUnitario,
                total,
                art.getIvaPercent()
        });
    }

    private void actualizarPrecioUnitario() {
        Articulo art = (Articulo) cmbArticulo.getSelectedItem();
        if (art != null && art.getPreuVenda() != null) {
            txtPrecioUnitario.setText(art.getPreuVenda().toString());
        } else {
            txtPrecioUnitario.setText("");
        }
    }

    // ============================
    // ELIMINAR LÍNEA
    // ============================
    private void eliminarLinea() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona una línea para eliminar.");
            return;
        }

        tableModel.removeRow(fila);
    }

    // ============================
    // GUARDAR ALBARÁN
    // ============================
    private void guardarAlbaran() {
        if (albaranEditando != null && albaranEditando.estaBloqueado()) {
            JOptionPane.showMessageDialog(this,
                    "Este albarán ya está convertido en factura.\nNo se puede modificar.");
            return;
        }

        if (cmbCliente.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.");
            return;
        }
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Añade al menos una línea.");
            return;
        }

        try {
            Albaran a = (albaranEditando != null) ? albaranEditando : new Albaran();

            a.setCliente((Cliente) cmbCliente.getSelectedItem());
            a.getLineas().clear();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                AlbaranLinea l = new AlbaranLinea();
                l.setArticulo((Articulo) tableModel.getValueAt(i, 0));
                l.setCantidad((Integer) tableModel.getValueAt(i, 1));
                l.setPrecioUnitario((BigDecimal) tableModel.getValueAt(i, 2));
                l.setIvaPercent((Integer) tableModel.getValueAt(i, 4));

                a.addLinea(l);
            }

            albaranDAO.guardar(a);

            JOptionPane.showMessageDialog(this, "Albarán guardado correctamente.");

            if (albaranEditando == null) {
                tableModel.setRowCount(0);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el albarán.");
            ex.printStackTrace();
        }
    }

    // ============================
    // BLOQUEAR EDICIÓN
    // ============================
    private void bloquearEdicion() {
        cmbCliente.setEnabled(false);
        cmbArticulo.setEnabled(false);
        spCantidad.setEnabled(false);
        txtPrecioUnitario.setEnabled(false);
        table.setEnabled(false);

        btnAddLinea.setEnabled(false);
        btnEliminarLinea.setEnabled(false);
        btnGuardar.setEnabled(false);

        JOptionPane.showMessageDialog(
                this,
                "Este albarán ya ha sido convertido en factura.\nNo se puede modificar.",
                "Albarán bloqueado",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
