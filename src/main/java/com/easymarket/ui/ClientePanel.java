package com.easymarket.ui;

import com.easymarket.model.*;
import com.easymarket.service.PedidoService;
import com.easymarket.service.ProductoService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

// Panel principal del cliente. Tiene tres pestañas:
// 1. Tienda    → ver productos y agregarlos al carrito
// 2. Carrito   → ver lo que tiene, quitar cosas y confirmar la compra
// 3. Mis Pedidos → historial de compras anteriores
public class ClientePanel extends JPanel {

    private MainApp app;
    private Cliente cliente;
    private ProductoService productoService;
    private PedidoService pedidoService;

    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JTable tablaCarrito;
    private DefaultTableModel modeloCarrito;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;

    private JLabel lblTotal;

    public ClientePanel(MainApp app, Cliente cliente,
                        ProductoService productoService, PedidoService pedidoService) {
        this.app = app;
        this.cliente = cliente;
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        construirUI();
        cargarProductos();
    }

    private void construirUI() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));

        // Barra superior
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(33, 150, 83));
        barraTop.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel lblBienvenida = new JLabel("Hola, " + cliente.getNombre() + "  |  EasyMarket");
        lblBienvenida.setForeground(Color.WHITE);
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 15));
        barraTop.add(lblBienvenida, BorderLayout.WEST);

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> app.mostrarLogin());
        barraTop.add(btnSalir, BorderLayout.EAST);

        add(barraTop, BorderLayout.NORTH);

        // Pestañas principales
        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Tienda", crearPestanaTienda());
        pestanas.addTab("Carrito", crearPestanaCarrito());
        pestanas.addTab("Mis Pedidos", crearPestanaPedidos());

        // Actualizamos los datos al cambiar de pestaña
        pestanas.addChangeListener(e -> {
            int tab = pestanas.getSelectedIndex();
            if (tab == 0) cargarProductos();   // refrescamos stock en tiempo real
            if (tab == 1) cargarCarrito();
            if (tab == 2) cargarPedidos();
        });

        add(pestanas, BorderLayout.CENTER);
    }

    // ─── Pestaña Tienda ───────────────────────────────────────────────────────

    private JPanel crearPestanaTienda() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columnas = {"ID", "Producto", "Descripción", "Precio", "Stock"};
        modeloProductos = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.setRowHeight(22);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(40);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Controles de cantidad y botón agregar
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInferior.add(new JLabel("Cantidad:"));
        JSpinner spCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 99, 1));
        panelInferior.add(spCantidad);

        JButton btnAgregar = new JButton("Agregar al carrito");
        btnAgregar.setBackground(new Color(33, 150, 83));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        panelInferior.add(btnAgregar);

        panel.add(panelInferior, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Primero selecciona un producto de la lista.");
                return;
            }

            int id       = (int) modeloProductos.getValueAt(fila, 0);
            int cantidad = (int) spCantidad.getValue();

            // Siempre buscamos el producto en el repositorio para tener el stock actual
            Producto p = productoService.buscarPorId(id);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Ese producto ya no está disponible.");
                return;
            }
            if (!p.hayStock(cantidad)) {
                JOptionPane.showMessageDialog(this,
                        "Stock insuficiente. Solo hay " + p.getStock() + " unidades.",
                        "Sin stock", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Agregamos al carrito el objeto del repositorio (fuente de verdad)
            cliente.getCarrito().agregarProducto(p, cantidad);
            JOptionPane.showMessageDialog(this,
                    cantidad + " x \"" + p.getNombre() + "\" agregado al carrito.");
        });

        return panel;
    }

    // ─── Pestaña Carrito ──────────────────────────────────────────────────────

    private JPanel crearPestanaCarrito() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"Producto", "Precio unit.", "Cantidad", "Subtotal"};
        modeloCarrito = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaCarrito = new JTable(modeloCarrito);
        tablaCarrito.setRowHeight(22);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        panelInferior.add(lblTotal, BorderLayout.WEST);

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton btnEliminar = new JButton("Quitar seleccionado");
        btnEliminar.addActionListener(e -> {
            int fila = tablaCarrito.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Selecciona un producto del carrito.");
                return;
            }
            // El nombre está en la primera columna; lo usamos para encontrar el item
            String nombre = (String) modeloCarrito.getValueAt(fila, 0);
            for (ItemCarrito item : cliente.getCarrito().getItems()) {
                if (item.getProducto().getNombre().equals(nombre)) {
                    cliente.getCarrito().eliminarProducto(item.getProducto().getId());
                    break;
                }
            }
            cargarCarrito();
        });

        JButton btnComprar = new JButton("Confirmar compra");
        btnComprar.setBackground(new Color(33, 150, 83));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.addActionListener(e -> mostrarDialogoPago());

        botones.add(btnEliminar);
        botones.add(btnComprar);
        panelInferior.add(botones, BorderLayout.EAST);
        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    // ─── Pestaña Pedidos ──────────────────────────────────────────────────────

    private JPanel crearPestanaPedidos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"#", "Fecha", "Total", "Método de pago", "Estado"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.setRowHeight(22);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(40);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        return panel;
    }

    // ─── Métodos de carga ─────────────────────────────────────────────────────

    private void cargarProductos() {
        modeloProductos.setRowCount(0);
        for (Producto p : productoService.listarTodos()) {
            modeloProductos.addRow(new Object[]{
                p.getId(),
                p.getNombre(),
                p.getDescripcion(),
                "$" + String.format("%.2f", p.getPrecio()),
                p.getStock()
            });
        }
    }

    private void cargarCarrito() {
        modeloCarrito.setRowCount(0);
        for (ItemCarrito item : cliente.getCarrito().getItems()) {
            modeloCarrito.addRow(new Object[]{
                item.getProducto().getNombre(),
                "$" + String.format("%.2f", item.getProducto().getPrecio()),
                item.getCantidad(),
                "$" + String.format("%.2f", item.getSubtotal())
            });
        }
        lblTotal.setText("Total: $" + String.format("%.2f", cliente.getCarrito().calcularTotal()));
    }

    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);
        List<Pedido> lista = pedidoService.obtenerPedidosDeCliente(cliente.getId());
        for (Pedido p : lista) {
            modeloPedidos.addRow(new Object[]{
                p.getId(),
                p.getFecha(),
                "$" + String.format("%.2f", p.getTotal()),
                p.getMetodoPago(),
                p.getEstado()
            });
        }
    }

    // ─── Flujo de pago ────────────────────────────────────────────────────────

    private void mostrarDialogoPago() {
        if (cliente.getCarrito().estaVacio()) {
            JOptionPane.showMessageDialog(this,
                    "El carrito está vacío. Agrega productos antes de comprar.");
            return;
        }

        // Calculamos el total AQUÍ, antes de que PedidoService vacíe el carrito
        double totalAPagar = cliente.getCarrito().calcularTotal();

        String[] opciones = {"Efectivo", "Tarjeta"};
        int eleccion = JOptionPane.showOptionDialog(
                this,
                "Total a pagar: $" + String.format("%.2f", totalAPagar)
                        + "\n\nSelecciona el método de pago:",
                "Confirmar compra",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, opciones, opciones[0]
        );

        // El usuario cerró el diálogo sin elegir
        if (eleccion < 0) return;

        Pago pago;
        if (eleccion == 0) {
            pago = new PagoEfectivo(totalAPagar);
        } else {
            String digitos = JOptionPane.showInputDialog(this,
                    "Ingresa los últimos 4 dígitos de tu tarjeta:");
            // Si canceló el campo de dígitos, no hacemos nada
            if (digitos == null || digitos.isBlank()) return;
            pago = new PagoTarjeta(totalAPagar, digitos.trim());
        }

        try {
            // confirmarPedido vacía el carrito internamente, pero ya guardamos el total arriba
            Pedido pedido = pedidoService.confirmarPedido(cliente, pago);

            // Mostramos el resumen con el total del pedido (no del carrito ya vacío)
            JOptionPane.showMessageDialog(this,
                    "Compra confirmada.\n"
                    + pago.procesarPago()
                    + "\nPedido #" + pedido.getId()
                    + " — " + pedido.getFecha(),
                    "¡Gracias por tu compra!",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refrescamos tienda y carrito para mostrar el stock actualizado
            cargarProductos();
            cargarCarrito();

        } catch (IllegalStateException ex) {
            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "No se pudo completar la compra",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
