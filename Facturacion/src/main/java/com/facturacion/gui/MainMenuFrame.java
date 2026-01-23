package com.facturacion.gui;

import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    private JPanel mainPanel; // panel que cambia de vista
    private CardLayout cardLayout;

    public MainMenuFrame() {
        setTitle("Sistema de Facturación");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Look & feel oscuro
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Panel principal del menú
        mainPanel.add(crearMenuCentral(), "menuPrincipal");

        // Submenú Clientes
        mainPanel.add(crearMenuClientes(), "menuClientes");

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
        JButton btnFacturas = crearBoton("FACTURAS", new Color(100, 100, 100));
        btnFacturas.setEnabled(false);
        JButton btnSalir = crearBoton("SALIR", new Color(114, 137, 218));

        // acción: abrir submenú clientes
        btnClientes.addActionListener(e -> cardLayout.show(mainPanel, "menuClientes"));
        btnSalir.addActionListener(e -> System.exit(0));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(btnClientes, gbc);

        gbc.gridy = 1;
        panel.add(btnFacturas, gbc);

        gbc.gridy = 2;
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
        JButton btnListado = crearBoton("Listado Clientes", new Color(88, 101, 242));
        JButton btnVolver = crearBoton("Volver al Menú", new Color(114, 137, 218));

        // panel para mostrar contenido dinámico
        JPanel contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new JPanel(), "vacio"); // panel vacío por defecto
        contentPanel.add(new ClientePanel(), "nuevoCliente");
        contentPanel.add(new ClienteListFramePanel(), "listadoClientes"); // JTable adaptado

        // acciones de botones
        btnNuevoCliente.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "nuevoCliente"));
        btnListado.addActionListener(e -> ((CardLayout) contentPanel.getLayout()).show(contentPanel, "listadoClientes"));
        btnVolver.addActionListener(e -> cardLayout.show(mainPanel, "menuPrincipal"));

        gbc.gridy = 0;
        botones.add(btnNuevoCliente, gbc);
        gbc.gridy = 1;
        botones.add(btnListado, gbc);
        gbc.gridy = 2;
        botones.add(btnVolver, gbc);

        panel.add(botones, BorderLayout.WEST);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
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
