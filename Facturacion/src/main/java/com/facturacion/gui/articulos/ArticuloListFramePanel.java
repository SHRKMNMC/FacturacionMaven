package com.facturacion.gui.articulos;

import com.facturacion.dao.ArticuloDAO;
import com.facturacion.entity.Articulo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ArticuloListFramePanel extends JPanel {

    private JTable table;
    private DefaultTableModel tableModel;
    private ArticuloDAO articuloDAO = new ArticuloDAO();

    public ArticuloListFramePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(35, 39, 42));

        tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(new Object[]{
                "ID", "Código", "Nombre", "Família", "Categoria",
                "Unidad", "Proveedor", "Precio coste", "Precio venta",
                "IVA", "Stock act.", "Stock mín.", "Activo"
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

        //BOTONES

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setBackground(new Color(88, 101, 242));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnActualizar.addActionListener(e -> cargarArticulos());
        southPanel.add(btnActualizar);

        JButton btnEliminar = new JButton("Eliminar");
        btnEliminar.setBackground(new Color(242, 63, 63));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEliminar.addActionListener(e -> eliminarArticuloSeleccionado());
        southPanel.add(btnEliminar);

        JButton btnEditar = new JButton("Editar");
        btnEditar.setBackground(new Color(52, 152, 219));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnEditar.addActionListener(e -> editarArticuloSeleccionado());
        southPanel.add(btnEditar);

        JButton btnVer = new JButton("Ver");
        btnVer.setBackground(new Color(46, 204, 113));
        btnVer.setForeground(Color.WHITE);
        btnVer.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnVer.addActionListener(e -> verArticuloSeleccionado());
        southPanel.add(btnVer);

        add(southPanel, BorderLayout.SOUTH);

        cargarArticulos();
    }

    private void cargarArticulos() {
        tableModel.setRowCount(0);
        List<Articulo> articulos = articuloDAO.listarTodos();

        for (Articulo a : articulos) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getCodi(),
                    a.getNom(),
                    a.getFamilia(),
                    a.getCategoria(),
                    a.getUnitat(),
                    a.getProveedor(),
                    a.getPreuCost() != null ? a.getPreuCost() : BigDecimal.ZERO,
                    a.getPreuVenda() != null ? a.getPreuVenda() : BigDecimal.ZERO,
                    a.getIvaPercent(),
                    a.getStockActual(),
                    a.getStockMinim(),
                    a.isActiu() ? "Sí" : "No"
            });
        }

        ajustarAnchoColumnas();
    }

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

    private void eliminarArticuloSeleccionado() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un artículo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que deseas eliminar el artículo con ID " + id + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            articuloDAO.eliminar(id);
            JOptionPane.showMessageDialog(this, "Artículo eliminado correctamente.");
            cargarArticulos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al eliminar el artículo.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void editarArticuloSeleccionado() {
        int fila = table.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un artículo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);
        Articulo a = articuloDAO.buscarPorId(id);

        if (a == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el artículo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArticuloPanel panelEdicion = new ArticuloPanel();
        panelEdicion.cargarArticulo(a);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Editar Artículo", true);
        dialog.setContentPane(panelEdicion);
        dialog.setSize(900, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        cargarArticulos();
    }

    private void verArticuloSeleccionado() {
        int fila = table.getSelectedRow();

        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un artículo.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(fila, 0);

        Articulo a = articuloDAO.buscarPorId(id);

        if (a == null) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar el artículo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArticuloFichaPanel ficha = new ArticuloFichaPanel();
        ficha.cargarArticulo(a);

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ficha del Artículo", true);
        dialog.setContentPane(ficha);
        dialog.setSize(750, 650);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void mostrarSolo(List<Articulo> lista) {
        tableModel.setRowCount(0);

        for (Articulo a : lista) {
            tableModel.addRow(new Object[]{
                    a.getId(),
                    a.getCodi(),
                    a.getNom(),
                    a.getFamilia(),
                    a.getCategoria(),
                    a.getUnitat(),
                    a.getProveedor(),
                    a.getPreuCost(),
                    a.getPreuVenda(),
                    a.getIvaPercent(),
                    a.getStockActual(),
                    a.getStockMinim(),
                    a.isActiu() ? "Sí" : "No"
            });
        }

        ajustarAnchoColumnas();
    }
}
