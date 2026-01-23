package com.facturacion.gui;

import javax.swing.*;

public class ClienteFrame extends JFrame {

    public ClienteFrame() {
        setTitle("Gestión de Clientes");
        setSize(1000, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        add(new ClientePanel());
    }
}
