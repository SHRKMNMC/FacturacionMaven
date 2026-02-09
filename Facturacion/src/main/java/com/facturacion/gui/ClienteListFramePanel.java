package com.facturacion.gui;

import com.facturacion.dao.ClienteDAO;
import com.facturacion.entity.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ClienteListFramePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private ClienteDAO clienteDAO = new ClienteDAO();

    public ClienteListFramePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 39, 42));

        // Modelo de tabla
        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Nombre", "DNI", "Dirección", "Población",
                "Provincia", "CP", "Tel Fijo", "Tel Móvil",
                "Email", "Web", "Método Pago", "Límite Crédito",
                "Cuenta", "Activo"
        });

        table = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table.setFillsViewportHeight(true);
        table.setBackground(new Color(45, 49, 52));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(88, 101, 242));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(35, 39, 42));
        table.getTableHeader().setForeground(Color.WHITE);

        // Tooltip dinámico
        table.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseMoved(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());

                if (row > -1 && col > -1) {
                    Object value = table.getValueAt(row, col);
                    table.setToolTipText(value != null ? value.toString() : null);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Panel inferior con botones
        JPanel southPanel = new JPanel();
        southPanel.setBackground(new Color(35, 39, 42));

        // Botón actualizar
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(88, 101, 242));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnActualizar.addActionListener(e -> cargarClientes());
        southPanel.add(btnActualizar);

        // Botón eliminar
        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(242, 63, 63));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());
        southPanel.add(btnEliminar);

        // Botón editar
        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEditar.addActionListener(e -> editarClienteSeleccionado());
        southPanel.add(btnEditar);

        // ⭐ NUEVO: Botón ver ficha
        JButton btnVer = new JButton("Ver");
        btnVer.setBackground(new Color(46, 204, 113));
        btnVer.setForeground(Color.WHITE);
        btnVer.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVer.addActionListener(e -> verClienteSeleccionado());
        southPanel.add(btnVer);

        add(southPanel, BorderLayout.SOUTH);

        cargarClientes();
    }

    // ===================== CARGAR CLIENTES =====================
    private void cargarClientes() {
        tableModel.setRowCount(0);
        List<Cliente> clientes = clienteDAO.listarTodos();

        for (Cliente c : clientes) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getDni(),
                    c.getDireccion(),
                    c.getPoblacion(),
                    c.getProvincia(),
                    c.getCodigoPostal(),
                    c.getTelefonoFijo(),
                    c.getTelefonoMovil(),
                    c.getCorreoElectronico(),
                    c.getPaginaWeb(),
                    c.getMetodoPago(),
                    c.getLimiteCredito() != null ? c.getLimiteCredito() : BigDecimal.ZERO,
                    c.getNumCuentaBancaria(),
                    c.isActivo() ? "Sí" : "No"
            });
        }

        ajustarAnchoColumnas();
    }

    // ===================== AJUSTAR ANCHO COLUMNAS =====================
    private void ajustarAnchoColumnas() {
        for (int col = 0; col < table.getColumnCount(); col++) {
            int maxWidth = 80;

            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width + 10, maxWidth);
            }

            table.getColumnModel().getColumn(col).setPreferredWidth(maxWidth);
        }
    }

    // ===================== ELIMINAR CLIENTE =====================
    private void eliminarClienteSeleccionado() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCliente = (int) tableModel.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar el cliente con ID " + idCliente + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            clienteDAO.eliminar(idCliente);
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.");
            cargarClientes();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    // ===================== EDITAR CLIENTE =====================
    private void editarClienteSeleccionado() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCliente = (int) tableModel.getValueAt(fila, 0);

        Cliente c = clienteDAO.buscarPorId(idCliente);

        if (c == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClientePanel panelEdicion = new ClientePanel();
        panelEdicion.cargarCliente(c);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Cliente", true);
        dialog.setContentPane(panelEdicion);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        cargarClientes();
    }

    // ===================== VER CLIENTE (NUEVO) =====================
    private void verClienteSeleccionado() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un cliente.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idCliente = (int) tableModel.getValueAt(fila, 0);

        Cliente c = clienteDAO.buscarPorId(idCliente);

        if (c == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el cliente.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ClienteFichaPanel ficha = new ClienteFichaPanel();
        ficha.cargarCliente(c);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ficha del Cliente", true);
        dialog.setContentPane(ficha);
        dialog.setSize(750, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void mostrarSolo(List<Cliente> lista) {
        tableModel.setRowCount(0);

        for (Cliente c : lista) {
            tableModel.addRow(new Object[]{
                    c.getId(),
                    c.getNombre(),
                    c.getDni(),
                    c.getDireccion(),
                    c.getPoblacion(),
                    c.getProvincia(),
                    c.getCodigoPostal(),
                    c.getTelefonoFijo(),
                    c.getTelefonoMovil(),
                    c.getCorreoElectronico(),
                    c.getPaginaWeb(),
                    c.getMetodoPago(),
                    c.getLimiteCredito(),
                    c.getNumCuentaBancaria(),
                    c.isActivo() ? "Sí" : "No"
            });
        }

        ajustarAnchoColumnas();
    }

    public void mostrarTodos() {
        cargarClientes();
    }
}
