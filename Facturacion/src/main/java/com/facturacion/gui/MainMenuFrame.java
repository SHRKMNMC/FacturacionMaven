package com.facturacion.gui;

import com.facturacion.dao.ArticuloDAO;
import com.facturacion.dao.ClienteDAO;
import com.facturacion.entity.Articulo;
import com.facturacion.entity.Cliente;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainMenuFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainMenuFrame() {
        setTitle("Sistema de Facturación");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(crearMenuCentral(), "menuPrincipal");
        mainPanel.add(crearMenuClientes(), "menuClientes");
        mainPanel.add(crearMenuArticulos(), "menuArticulos");

        getContentPane().add(mainPanel);

        cardLayout.show(mainPanel, "menuPrincipal");
    }

    // =================== MENU PRINCIPAL ===================
    private JPanel crearMenuCentral() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(35, 39, 42));
        panel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnClientes = crearBoton("CLIENTES", new Color(88, 101, 242));
        JButton btnArticulos = crearBoton("ARTÍCULOS", new Color(88, 101, 242));
        JButton btnFacturas = crearBoton("FACTURAS", new Color(100, 100, 100));
        btnFacturas.setEnabled(false);
        JButton btnSalir = crearBoton("SALIR", new Color(114, 137, 218));

        btnClientes.addActionListener(e -> cardLayout.show(mainPanel, "menuClientes"));
        btnArticulos.addActionListener(e -> cardLayout.show(mainPanel, "menuArticulos"));
        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnClientes, gbc);

        gbc.gridy = 1;
        panel.add(btnArticulos, gbc);

        gbc.gridy = 2;
        panel.add(btnFacturas, gbc);

        gbc.gridy = 3;
        panel.add(btnSalir, gbc);

        return panel;
    }

    // =================== MENU CLIENTES ===================
    private JPanel crearMenuClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 39, 42));

        JPanel botones = new JPanel(new GridBagLayout());
        botones.setBackground(new Color(35, 39, 42));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnNuevoCliente = crearBoton("Nuevo Cliente", new Color(88, 101, 242));
        JButton btnBuscarId = crearBoton("Buscar por ID", new Color(88, 101, 242));
        JButton btnBuscarNombre = crearBoton("Buscar por Nombre", new Color(88, 101, 242));
        JButton btnListado = crearBoton("Listado Clientes", new Color(88, 101, 242));
        JButton btnVolver = crearBoton("Volver al Menú", new Color(114, 137, 218));

        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new JPanel(), "vacio");
        contentPanel.add(new ClientePanel(), "nuevoCliente");
        contentPanel.add(new ClienteListFramePanel(), "listadoClientes");

        btnNuevoCliente.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "nuevoCliente"));
        btnBuscarId.addActionListener(e -> buscarClientePorId());
        btnBuscarNombre.addActionListener(e -> buscarClientePorNombre());
        btnListado.addActionListener(e -> {
            ClienteListFramePanel listado =
                    (ClienteListFramePanel) contentPanel.getComponent(2);

            listado.mostrarTodos();

            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoClientes");
        });

        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "menuPrincipal"));

        gbc.gridy = 0; botones.add(btnNuevoCliente, gbc);
        gbc.gridy = 1; botones.add(btnBuscarId, gbc);
        gbc.gridy = 2; botones.add(btnBuscarNombre, gbc);
        gbc.gridy = 3; botones.add(btnListado, gbc);
        gbc.gridy = 4; botones.add(btnVolver, gbc);

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // =================== MENU ARTÍCULOS ===================
    private JPanel crearMenuArticulos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 39, 42));

        JPanel botones = new JPanel(new GridBagLayout());
        botones.setBackground(new Color(35, 39, 42));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton btnNuevoArticulo = crearBoton("Nuevo Artículo", new Color(88, 101, 242));
        JButton btnBuscarId = crearBoton("Buscar por ID", new Color(88, 101, 242));
        JButton btnBuscarNombre = crearBoton("Buscar por Nombre", new Color(88, 101, 242));
        JButton btnListadoArticulos = crearBoton("Listado Artículos", new Color(88, 101, 242));
        JButton btnVolver = crearBoton("Volver al Menú", new Color(114, 137, 218));

        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new JPanel(), "vacio");
        contentPanel.add(new ArticuloPanel(), "nuevoArticulo");
        contentPanel.add(new ArticuloListFramePanel(), "listadoArticulos");

        btnNuevoArticulo.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "nuevoArticulo"));
        btnBuscarId.addActionListener(e -> buscarArticuloPorId());
        btnBuscarNombre.addActionListener(e -> buscarArticuloPorNombre());
        btnListadoArticulos.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoArticulos"));
        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "menuPrincipal"));

        gbc.gridy = 0; botones.add(btnNuevoArticulo, gbc);
        gbc.gridy = 1; botones.add(btnBuscarId, gbc);
        gbc.gridy = 2; botones.add(btnBuscarNombre, gbc);
        gbc.gridy = 3; botones.add(btnListadoArticulos, gbc);
        gbc.gridy = 4; botones.add(btnVolver, gbc);

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // =================== MÉTODOS DE BÚSQUEDA ===================

    private void buscarClientePorId() {
        String input = JOptionPane.showInputDialog(this, "Introduce el ID del cliente:");

        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            ClienteDAO dao = new ClienteDAO();
            Cliente c = dao.buscarPorId(id);

            if (c == null) {
                JOptionPane.showMessageDialog(this, "No existe un cliente con ese ID.");
                return;
            }

            // Obtener el panel de listado ya existente
            JPanel menuClientes = (JPanel) mainPanel.getComponent(1);
            JPanel contentPanel = (JPanel) menuClientes.getComponent(1);
            ClienteListFramePanel listado =
                    (ClienteListFramePanel) contentPanel.getComponent(2);

            listado.mostrarSolo(List.of(c));

            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoClientes");
            cardLayout.show(mainPanel, "menuClientes");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }



    private void buscarClientePorNombre() {
        String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre del cliente:");

        if (nombre == null || nombre.trim().isEmpty()) return;

        ClienteDAO dao = new ClienteDAO();
        List<Cliente> lista = dao.buscarPorNombre(nombre.trim());

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron clientes con ese nombre.");
            return;
        }

        JPanel menuClientes = (JPanel) mainPanel.getComponent(1);
        JPanel contentPanel = (JPanel) menuClientes.getComponent(1);
        ClienteListFramePanel listado =
                (ClienteListFramePanel) contentPanel.getComponent(2);

        listado.mostrarSolo(lista);

        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoClientes");
        cardLayout.show(mainPanel, "menuClientes");
    }


    private void buscarArticuloPorId() {
        String input = JOptionPane.showInputDialog(this, "Introduce el ID del artículo:");

        if (input == null || input.trim().isEmpty()) return;

        try {
            int id = Integer.parseInt(input.trim());
            ArticuloDAO dao = new ArticuloDAO();
            Articulo a = dao.buscarPorId(id);

            if (a == null) {
                JOptionPane.showMessageDialog(this, "No existe un artículo con ese ID.");
                return;
            }

            JPanel menuArticulos = (JPanel) mainPanel.getComponent(2);
            JPanel contentPanel = (JPanel) menuArticulos.getComponent(1);
            ArticuloListFramePanel listado =
                    (ArticuloListFramePanel) contentPanel.getComponent(2);

            listado.mostrarSolo(List.of(a));

            ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoArticulos");
            cardLayout.show(mainPanel, "menuArticulos");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }


    private void buscarArticuloPorNombre() {
        String nombre = JOptionPane.showInputDialog(this, "Introduce el nombre del artículo:");

        if (nombre == null || nombre.trim().isEmpty()) return;

        ArticuloDAO dao = new ArticuloDAO();
        List<Articulo> lista = dao.buscarPorNombre(nombre.trim());

        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron artículos con ese nombre.");
            return;
        }

        JPanel menuArticulos = (JPanel) mainPanel.getComponent(2);
        JPanel contentPanel = (JPanel) menuArticulos.getComponent(1);
        ArticuloListFramePanel listado =
                (ArticuloListFramePanel) contentPanel.getComponent(2);

        listado.mostrarSolo(lista);

        ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoArticulos");
        cardLayout.show(mainPanel, "menuArticulos");
    }



    // =================== BOTÓN ESTILO ===================
    private JButton crearBoton(String texto, Color colorBase) {
        JButton btn = new JButton(texto) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? colorBase.darker() :
                        getModel().isRollover() ? colorBase.brighter() : colorBase);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
                super.paintComponent(g);
            }

            @Override
            public void setContentAreaFilled(boolean b) {
                super.setContentAreaFilled(false);
            }
        };
        btn.setPreferredSize(new Dimension(250, 60));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenuFrame().setVisible(true));
    }
}
