package com.easymarket.ui;

import com.easymarket.model.Cliente;
import com.easymarket.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

// Pantalla de registro para nuevos clientes.
public class RegistroPanel extends JPanel {

    private MainApp app;
    private UsuarioService usuarioService;

    private JTextField txtNombre;
    private JTextField txtEmail;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmar;

    public RegistroPanel(MainApp app, UsuarioService usuarioService) {
        this.app = app;
        this.usuarioService = usuarioService;
        construirUI();
    }

    private void construirUI() {
        setLayout(new GridBagLayout());
        setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Crear cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(33, 150, 83));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        gbc.gridwidth = 1;

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Nombre:"), gbc);
        txtNombre = new JTextField(20);
        gbc.gridx = 1; add(txtNombre, gbc);

        gbc.gridx = 0; gbc.gridy = 2; add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1; add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3; add(new JLabel("Contraseña:"), gbc);
        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1; add(txtContrasena, gbc);

        gbc.gridx = 0; gbc.gridy = 4; add(new JLabel("Confirmar:"), gbc);
        txtConfirmar = new JPasswordField(20);
        gbc.gridx = 1; add(txtConfirmar, gbc);

        JButton btnRegistrar = new JButton("Crear cuenta");
        btnRegistrar.setBackground(new Color(33, 150, 83));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 13));
        btnRegistrar.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(btnRegistrar, gbc);

        JButton btnVolver = new JButton("← Volver al login");
        btnVolver.setBorderPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setForeground(new Color(33, 150, 83));
        gbc.gridy = 6;
        add(btnVolver, gbc);

        btnRegistrar.addActionListener(e -> intentarRegistro());
        btnVolver.addActionListener(e -> app.mostrarLogin());
    }

    private void intentarRegistro() {
        String nombre = txtNombre.getText().trim();
        String email  = txtEmail.getText().trim();
        String clave  = new String(txtContrasena.getPassword());
        String conf   = new String(txtConfirmar.getPassword());

        if (!clave.equals(conf)) {
            JOptionPane.showMessageDialog(this,
                    "Las contraseñas no coinciden.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Cliente cliente = usuarioService.registrarCliente(nombre, email, clave);
            JOptionPane.showMessageDialog(this,
                    "¡Cuenta creada exitosamente!\nYa puedes iniciar sesión.",
                    "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            app.mostrarLogin();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error en el registro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
