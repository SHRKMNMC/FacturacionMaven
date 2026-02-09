package com.facturacion.gui;

import com.facturacion.dao.*;
import com.facturacion.entity.*;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainMenuFrame extends JFrame {

    private JPanel mainPanel;
    private CardLayout cardLayout;

    public MainMenuFrame() {
        setTitle("Sistema de Facturación");
        setSize(1200, 700);
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
        mainPanel.add(crearMenuAlbaranes(), "menuAlbaranes");
        mainPanel.add(crearMenuFacturas(), "menuFacturas");

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
        JButton btnAlbaranes = crearBoton("ALBARANES", new Color(88, 101, 242));
        JButton btnFacturas = crearBoton("FACTURAS", new Color(88, 101, 242));
        JButton btnSalir = crearBoton("SALIR", new Color(114, 137, 218));

        btnClientes.addActionListener(e -> cardLayout.show(mainPanel, "menuClientes"));
        btnArticulos.addActionListener(e -> cardLayout.show(mainPanel, "menuArticulos"));
        btnAlbaranes.addActionListener(e -> cardLayout.show(mainPanel, "menuAlbaranes"));
        btnFacturas.addActionListener(e -> cardLayout.show(mainPanel, "menuFacturas"));
        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(btnClientes, gbc);
        gbc.gridy = 1; panel.add(btnArticulos, gbc);
        gbc.gridy = 2; panel.add(btnAlbaranes, gbc);
        gbc.gridy = 3; panel.add(btnFacturas, gbc);
        gbc.gridy = 4; panel.add(btnSalir, gbc);

        return panel;
    }

    // =================== MENU CLIENTES ===================
    private JPanel crearMenuClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 39, 42));

        JPanel botones = crearPanelBotones();
        JPanel contentPanel = new JPanel(new CardLayout());

        contentPanel.add(new JPanel(), "vacio");
        contentPanel.add(new ClientePanel(), "nuevoCliente");
        contentPanel.add(new ClienteListFramePanel(), "listadoClientes");

        añadirBoton(botones, "Nuevo Cliente", () ->
                mostrar(contentPanel, "nuevoCliente"));

        añadirBoton(botones, "Buscar por ID", () -> buscarClientePorId(contentPanel));
        añadirBoton(botones, "Buscar por Nombre", () -> buscarClientePorNombre(contentPanel));

        añadirBoton(botones, "Listado Clientes", () -> {
            ClienteListFramePanel listado = obtenerPanel(contentPanel, ClienteListFramePanel.class);
            listado.mostrarTodos();
            mostrar(contentPanel, "listadoClientes");
        });

        añadirBoton(botones, "Volver al Menú", () ->
                cardLayout.show(mainPanel, "menuPrincipal"));

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    // =================== MENU ARTÍCULOS ===================
    private JPanel crearMenuArticulos() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 39, 42));

        JPanel botones = crearPanelBotones();
        JPanel contentPanel = new JPanel(new CardLayout());

        contentPanel.add(new JPanel(), "vacio");
        contentPanel.add(new ArticuloPanel(), "nuevoArticulo");
        contentPanel.add(new ArticuloListFramePanel(), "listadoArticulos");

        añadirBoton(botones, "Nuevo Artículo", () ->
                mostrar(contentPanel, "nuevoArticulo"));

        añadirBoton(botones, "Buscar por ID", () -> buscarArticuloPorId(contentPanel));
        añadirBoton(botones, "Buscar por Nombre", () -> buscarArticuloPorNombre(contentPanel));

        añadirBoton(botones, "Listado Artículos", () ->
                mostrar(contentPanel, "listadoArticulos"));

        añadirBoton(botones, "Volver al Menú", () ->
                cardLayout.show(mainPanel, "menuPrincipal"));

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    // =================== MENU ALBARANES ===================
    private JPanel crearMenuAlbaranes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 39, 42));

        JPanel botones = crearPanelBotones();
        JPanel contentPanel = new JPanel(new CardLayout());

        contentPanel.add(new JPanel(), "vacio");
        contentPanel.add(new AlbaranPanel(), "nuevoAlbaran");
        contentPanel.add(new AlbaranListFramePanel(), "listadoAlbaranes");

        añadirBoton(botones, "Nuevo Albarán", () ->
                mostrar(contentPanel, "nuevoAlbaran"));

        añadirBoton(botones, "Buscar por ID", () -> buscarAlbaranPorId(contentPanel));
        añadirBoton(botones, "Buscar por Cliente", () -> buscarAlbaranPorCliente(contentPanel));

        añadirBoton(botones, "Listado Albaranes", () ->
                mostrar(contentPanel, "listadoAlbaranes"));

        añadirBoton(botones, "Volver al Menú", () ->
                cardLayout.show(mainPanel, "menuPrincipal"));

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    // =================== MENU FACTURAS ===================
    private JPanel crearMenuFacturas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(35, 39, 42));

        JPanel botones = crearPanelBotones();
        JPanel contentPanel = new JPanel(new CardLayout());

        contentPanel.add(new JPanel(), "vacio");
        contentPanel.add(new FacturaListFramePanel(), "listadoFacturas");

        añadirBoton(botones, "Buscar por ID", () -> buscarFacturaPorId(contentPanel));
        añadirBoton(botones, "Buscar por Cliente", () -> buscarFacturaPorCliente(contentPanel));

        añadirBoton(botones, "Listado Facturas", () ->
                mostrar(contentPanel, "listadoFacturas"));

        añadirBoton(botones, "Volver al Menú", () ->
                cardLayout.show(mainPanel, "menuPrincipal"));

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    // =================== BÚSQUEDAS (CORREGIDAS) ===================

    private <T> T obtenerPanel(JPanel contentPanel, Class<T> tipo) {
        for (Component c : contentPanel.getComponents()) {
            if (tipo.isInstance(c)) return tipo.cast(c);
        }
        return null;
    }

    private void buscarClientePorId(JPanel contentPanel) {
        String input = JOptionPane.showInputDialog(this, "ID cliente:");
        if (input == null) return;

        try {
            Cliente c = new ClienteDAO().buscarPorId(Integer.parseInt(input));
            if (c == null) {
                JOptionPane.showMessageDialog(this, "No existe.");
                return;
            }

            ClienteListFramePanel listado = obtenerPanel(contentPanel, ClienteListFramePanel.class);
            listado.mostrarSolo(List.of(c));

            mostrar(contentPanel, "listadoClientes");
            cardLayout.show(mainPanel, "menuClientes");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void buscarClientePorNombre(JPanel contentPanel) {
        String nombre = JOptionPane.showInputDialog(this, "Nombre cliente:");
        if (nombre == null) return;

        List<Cliente> lista = new ClienteDAO().buscarPorNombre(nombre);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No encontrado.");
            return;
        }

        ClienteListFramePanel listado = obtenerPanel(contentPanel, ClienteListFramePanel.class);
        listado.mostrarSolo(lista);

        mostrar(contentPanel, "listadoClientes");
        cardLayout.show(mainPanel, "menuClientes");
    }

    private void buscarArticuloPorId(JPanel contentPanel) {
        String input = JOptionPane.showInputDialog(this, "ID artículo:");
        if (input == null) return;

        try {
            Articulo a = new ArticuloDAO().buscarPorId(Integer.parseInt(input));
            if (a == null) {
                JOptionPane.showMessageDialog(this, "No existe.");
                return;
            }

            ArticuloListFramePanel listado = obtenerPanel(contentPanel, ArticuloListFramePanel.class);
            listado.mostrarSolo(List.of(a));

            mostrar(contentPanel, "listadoArticulos");
            cardLayout.show(mainPanel, "menuArticulos");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void buscarArticuloPorNombre(JPanel contentPanel) {
        String nombre = JOptionPane.showInputDialog(this, "Nombre artículo:");
        if (nombre == null) return;

        List<Articulo> lista = new ArticuloDAO().buscarPorNombre(nombre);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No encontrado.");
            return;
        }

        ArticuloListFramePanel listado = obtenerPanel(contentPanel, ArticuloListFramePanel.class);
        listado.mostrarSolo(lista);

        mostrar(contentPanel, "listadoArticulos");
        cardLayout.show(mainPanel, "menuArticulos");
    }

    private void buscarAlbaranPorId(JPanel contentPanel) {
        String input = JOptionPane.showInputDialog(this, "ID albarán:");
        if (input == null) return;

        try {
            Albaran a = new AlbaranDAO().buscarPorId(Integer.parseInt(input));
            if (a == null) {
                JOptionPane.showMessageDialog(this, "No existe.");
                return;
            }

            AlbaranListFramePanel listado = obtenerPanel(contentPanel, AlbaranListFramePanel.class);
            listado.mostrarSolo(List.of(a));

            mostrar(contentPanel, "listadoAlbaranes");
            cardLayout.show(mainPanel, "menuAlbaranes");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void buscarAlbaranPorCliente(JPanel contentPanel) {
        String nombre = JOptionPane.showInputDialog(this, "Nombre cliente:");
        if (nombre == null) return;

        List<Albaran> lista = new AlbaranDAO().buscarPorNombreCliente(nombre);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No encontrado.");
            return;
        }

        AlbaranListFramePanel listado = obtenerPanel(contentPanel, AlbaranListFramePanel.class);
        listado.mostrarSolo(lista);

        mostrar(contentPanel, "listadoAlbaranes");
        cardLayout.show(mainPanel, "menuAlbaranes");
    }

    private void buscarFacturaPorId(JPanel contentPanel) {
        String input = JOptionPane.showInputDialog(this, "ID factura:");
        if (input == null) return;

        try {
            Factura f = new FacturaDAO().buscarPorId(Integer.parseInt(input));
            if (f == null) {
                JOptionPane.showMessageDialog(this, "No existe.");
                return;
            }

            FacturaListFramePanel listado = obtenerPanel(contentPanel, FacturaListFramePanel.class);
            listado.mostrarSolo(List.of(f));

            mostrar(contentPanel, "listadoFacturas");
            cardLayout.show(mainPanel, "menuFacturas");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ID inválido.");
        }
    }

    private void buscarFacturaPorCliente(JPanel contentPanel) {
        String nombre = JOptionPane.showInputDialog(this, "Nombre cliente:");
        if (nombre == null) return;

        List<Factura> lista = new FacturaDAO().buscarPorNombreCliente(nombre);
        if (lista.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No encontrado.");
            return;
        }

        FacturaListFramePanel listado = obtenerPanel(contentPanel, FacturaListFramePanel.class);
        listado.mostrarSolo(lista);

        mostrar(contentPanel, "listadoFacturas");
        cardLayout.show(mainPanel, "menuFacturas");
    }

    // =================== UTILIDADES ===================
    private JPanel crearPanelBotones() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(35, 39, 42));
        return p;
    }

    private void añadirBoton(JPanel panel, String texto, Runnable action) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = panel.getComponentCount();

        JButton btn = crearBoton(texto, new Color(88, 101, 242));
        btn.addActionListener(e -> action.run());
        panel.add(btn, gbc);
    }

    private void mostrar(JPanel contentPanel, String card) {
        ((CardLayout) contentPanel.getLayout()).show(contentPanel, card);
    }

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
