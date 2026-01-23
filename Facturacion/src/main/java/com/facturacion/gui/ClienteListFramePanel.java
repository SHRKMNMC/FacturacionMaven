package com.facturacion.gui;

import com.facturacion.dao.ClienteDAO;
import com.facturacion.entity.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
                return false; // tabla solo lectura
            }
        };
        table.setFillsViewportHeight(true);
        table.setBackground(new Color(45, 49, 52));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(new Color(88, 101, 242));
        table.setSelectionForeground(Color.WHITE);
        table.getTableHeader().setBackground(new Color(35, 39, 42));
        table.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Botón actualizar
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(88, 101, 242));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnActualizar.addActionListener(e -> cargarClientes());

        JPanel southPanel = new JPanel();
        southPanel.setBackground(new Color(35, 39, 42));
        southPanel.add(btnActualizar);

        add(southPanel, BorderLayout.SOUTH);

        cargarClientes();
    }

    private void cargarClientes() {
        tableModel.setRowCount(0); // limpiar tabla
        List<Cliente> clientes = clienteDAO.listarTodos(); // necesitas añadir este método al DAO
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
    }
}
