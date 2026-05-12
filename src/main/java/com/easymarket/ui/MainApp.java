package com.easymarket.ui;

import com.easymarket.model.Administrador;
import com.easymarket.model.Cliente;
import com.easymarket.repository.PedidoRepository;
import com.easymarket.repository.ProductoRepository;
import com.easymarket.repository.UsuarioRepository;
import com.easymarket.service.PedidoService;
import com.easymarket.service.ProductoService;
import com.easymarket.service.UsuarioService;

import javax.swing.*;
import java.awt.*;

// Clase principal: arranca la aplicación y maneja la navegación entre pantallas.
// Usamos CardLayout para intercambiar paneles sin abrir ventanas nuevas.
public class MainApp extends JFrame {

    // Repositorios (acceso a los archivos)
    private UsuarioRepository usuarioRepo;
    private ProductoRepository productoRepo;
    private PedidoRepository pedidoRepo;

    // Servicios (lógica de negocio)
    private UsuarioService usuarioService;
    private ProductoService productoService;
    private PedidoService pedidoService;

    // Panel contenedor con CardLayout
    private JPanel contenedor;
    private CardLayout cardLayout;

    // Nombres de cada "carta" del CardLayout
    private static final String PANTALLA_LOGIN    = "LOGIN";
    private static final String PANTALLA_REGISTRO = "REGISTRO";
    private static final String PANTALLA_CLIENTE  = "CLIENTE";
    private static final String PANTALLA_ADMIN    = "ADMIN";

    public MainApp() {
        inicializarServicios();
        construirVentana();
        mostrarLogin();
    }

    private void inicializarServicios() {
        usuarioRepo  = new UsuarioRepository();
        productoRepo = new ProductoRepository();
        pedidoRepo   = new PedidoRepository();

        usuarioService  = new UsuarioService(usuarioRepo);
        productoService = new ProductoService(productoRepo);
        pedidoService   = new PedidoService(pedidoRepo, productoRepo);
    }

    private void construirVentana() {
        setTitle("EasyMarket");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 520);
        setMinimumSize(new Dimension(650, 450));
        setLocationRelativeTo(null); // centrar en pantalla

        cardLayout = new CardLayout();
        contenedor = new JPanel(cardLayout);
        add(contenedor);
    }

    // Muestra la pantalla de login
    public void mostrarLogin() {
        contenedor.removeAll();
        contenedor.add(new LoginPanel(this, usuarioService), PANTALLA_LOGIN);
        cardLayout.show(contenedor, PANTALLA_LOGIN);
        contenedor.revalidate();
        contenedor.repaint();
    }

    // Muestra la pantalla de registro
    public void mostrarRegistro() {
        contenedor.add(new RegistroPanel(this, usuarioService), PANTALLA_REGISTRO);
        cardLayout.show(contenedor, PANTALLA_REGISTRO);
    }

    // Muestra el panel del cliente después del login
    public void mostrarPanelCliente(Cliente cliente) {
        contenedor.removeAll();
        contenedor.add(
            new ClientePanel(this, cliente, productoService, pedidoService),
            PANTALLA_CLIENTE
        );
        cardLayout.show(contenedor, PANTALLA_CLIENTE);
        contenedor.revalidate();
        contenedor.repaint();
    }

    // Muestra el panel del administrador después del login
    public void mostrarPanelAdmin(Administrador admin) {
        contenedor.removeAll();
        contenedor.add(
            new AdminPanel(this, admin, productoService, pedidoService),
            PANTALLA_ADMIN
        );
        cardLayout.show(contenedor, PANTALLA_ADMIN);
        contenedor.revalidate();
        contenedor.repaint();
    }

    public static void main(String[] args) {
        // Usamos el look and feel del sistema operativo para que se vea más nativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si no funciona, Swing usa su propio estilo por defecto
            System.out.println("No se pudo aplicar el tema del sistema.");
        }

        // Toda la UI de Swing debe ejecutarse en el Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new MainApp().setVisible(true);
        });
    }
}
