package com.easymarket.ui;

import com.easymarket.model.Administrador;
import com.easymarket.model.Cliente;
import com.easymarket.model.Usuario;
import com.easymarket.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

// Pantalla de inicio de sesión.
public class LoginPanel extends JPanel {

    private MainApp app;
    private UsuarioService usuarioService;

    private JTextField txtEmail;
    private JPasswordField txtContrasena;

    public LoginPanel(MainApp app, UsuarioService usuarioService) {
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

        JLabel lblTitulo = new JLabel("EasyMarket", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(33, 150, 83));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblTitulo, gbc);

        JLabel lblSubtitulo = new JLabel("Inicia sesión en tu cuenta", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1;
        add(lblSubtitulo, gbc);

        gbc.gridy = 2; gbc.gridwidth = 1;
        add(new JLabel("Email:"), gbc);
        txtEmail = new JTextField(20);
        gbc.gridx = 1;
        add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Contraseña:"), gbc);
        txtContrasena = new JPasswordField(20);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setBackground(new Color(33, 150, 83));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setFocusPainted(false);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnLogin, gbc);

        JButton btnRegistrar = new JButton("¿No tienes cuenta? Regístrate");
        btnRegistrar.setBorderPainted(false);
        btnRegistrar.setContentAreaFilled(false);
        btnRegistrar.setForeground(new Color(33, 150, 83));
        gbc.gridy = 5;
        add(btnRegistrar, gbc);

        btnLogin.addActionListener(e -> intentarLogin());
        txtContrasena.addActionListener(e -> intentarLogin());
        btnRegistrar.addActionListener(e -> app.mostrarRegistro());
    }

    private void intentarLogin() {
        String email = txtEmail.getText().trim();
        String clave = new String(txtContrasena.getPassword());

        try {
            Usuario usuario = usuarioService.login(email, clave);

            if (usuario == null) {
                JOptionPane.showMessageDialog(this,
                        "Email o contraseña incorrectos.",
                        "Error de login", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Usamos getTipo() en vez de instanceof para evitar problemas
            // de compilación con versiones viejas de NetBeans
            if (usuario.getTipo().equals("Administrador")) {
                app.mostrarPanelAdmin((Administrador) usuario);
            } else {
                app.mostrarPanelCliente((Cliente) usuario);
            }

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Campos vacíos", JOptionPane.WARNING_MESSAGE);
        }
    }
}
