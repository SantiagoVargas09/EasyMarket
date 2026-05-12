package com.easymarket.ui;

import com.easymarket.model.Administrador;
import com.easymarket.model.Pedido;
import com.easymarket.service.PedidoService;
import com.easymarket.service.ProductoService;
import com.easymarket.model.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// Panel del administrador.
// Tiene dos pestañas: gestión de productos y ver todos los pedidos.
public class AdminPanel extends JPanel {

    private MainApp app;
    private Administrador admin;
    private ProductoService productoService;
    private PedidoService pedidoService;

    private JTable tablaProductos;
    private DefaultTableModel modeloProductos;
    private JTable tablaPedidos;
    private DefaultTableModel modeloPedidos;

    public AdminPanel(MainApp app, Administrador admin,
                      ProductoService productoService, PedidoService pedidoService) {
        this.app = app;
        this.admin = admin;
        this.productoService = productoService;
        this.pedidoService = pedidoService;
        construirUI();
        cargarProductos();
    }

    private void construirUI() {
        setLayout(new BorderLayout());

        // Barra superior
        JPanel barraTop = new JPanel(new BorderLayout());
        barraTop.setBackground(new Color(44, 62, 80));
        barraTop.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        JLabel lblTitulo = new JLabel("Panel Administrador — " + admin.getNombre());
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 15));
        barraTop.add(lblTitulo, BorderLayout.WEST);

        JButton btnSalir = new JButton("Cerrar sesión");
        btnSalir.setFocusPainted(false);
        btnSalir.addActionListener(e -> app.mostrarLogin());
        barraTop.add(btnSalir, BorderLayout.EAST);

        add(barraTop, BorderLayout.NORTH);

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("📦 Productos", crearPestanaProductos());
        pestanas.addTab("📋 Pedidos", crearPestanaPedidos());

        pestanas.addChangeListener(e -> {
            if (pestanas.getSelectedIndex() == 1) cargarPedidos();
        });

        add(pestanas, BorderLayout.CENTER);
    }

    // ─── Pestaña Productos ────────────────────────────────────────────────────

    private JPanel crearPestanaProductos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"ID", "Nombre", "Descripción", "Precio", "Stock"};
        modeloProductos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaProductos = new JTable(modeloProductos);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaProductos.getColumnModel().getColumn(0).setMaxWidth(40);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Botones CRUD
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JButton btnAgregar  = new JButton("Agregar");
        JButton btnEditar   = new JButton("Editar");
        JButton btnEliminar = new JButton("Eliminar");

        btnAgregar.setBackground(new Color(33, 150, 83));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);

        botones.add(btnAgregar);
        botones.add(btnEditar);
        botones.add(btnEliminar);
        panel.add(botones, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> mostrarFormProducto(null));
        btnEditar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
            int id = (int) modeloProductos.getValueAt(fila, 0);
            mostrarFormProducto(productoService.buscarPorId(id));
        });
        btnEliminar.addActionListener(e -> {
            int fila = tablaProductos.getSelectedRow();
            if (fila < 0) { JOptionPane.showMessageDialog(this, "Selecciona un producto."); return; }
            int id = (int) modeloProductos.getValueAt(fila, 0);
            int conf = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que quieres eliminar este producto?",
                    "Confirmar", JOptionPane.YES_NO_OPTION);
            if (conf == JOptionPane.YES_OPTION) {
                productoService.eliminarProducto(id);
                cargarProductos();
            }
        });

        return panel;
    }

    // Formulario para agregar o editar un producto.
    // Si se pasa un producto existente, rellena los campos con sus datos.
    private void mostrarFormProducto(Producto existente) {
        boolean esNuevo = (existente == null);
        String titulo = esNuevo ? "Agregar producto" : "Editar producto";

        JTextField txtNombre = new JTextField(esNuevo ? "" : existente.getNombre(), 20);
        JTextField txtDesc   = new JTextField(esNuevo ? "" : existente.getDescripcion(), 20);
        JTextField txtPrecio = new JTextField(esNuevo ? "" : String.valueOf(existente.getPrecio()), 10);
        JTextField txtStock  = new JTextField(esNuevo ? "" : String.valueOf(existente.getStock()), 8);

        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        form.add(new JLabel("Nombre:")); form.add(txtNombre);
        form.add(new JLabel("Descripción:")); form.add(txtDesc);
        form.add(new JLabel("Precio:")); form.add(txtPrecio);
        form.add(new JLabel("Stock:")); form.add(txtStock);

        int res = JOptionPane.showConfirmDialog(this, form, titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (res != JOptionPane.OK_OPTION) return;

        try {
            String nombre = txtNombre.getText().trim();
            String desc   = txtDesc.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock     = Integer.parseInt(txtStock.getText().trim());

            if (esNuevo) {
                productoService.crearProducto(nombre, desc, precio, stock);
            } else {
                productoService.actualizarProducto(existente.getId(), nombre, desc, precio, stock);
            }
            cargarProductos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Precio y stock deben ser números válidos.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ─── Pestaña Pedidos ──────────────────────────────────────────────────────

    private JPanel crearPestanaPedidos() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] cols = {"#", "Cliente ID", "Fecha", "Total", "Método", "Estado"};
        modeloPedidos = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaPedidos = new JTable(modeloPedidos);
        tablaPedidos.getColumnModel().getColumn(0).setMaxWidth(40);
        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);

        return panel;
    }

    // ─── Carga de datos ───────────────────────────────────────────────────────

    private void cargarProductos() {
        modeloProductos.setRowCount(0);
        for (Producto p : productoService.listarTodos()) {
            modeloProductos.addRow(new Object[]{
                p.getId(), p.getNombre(), p.getDescripcion(),
                "$" + String.format("%.2f", p.getPrecio()), p.getStock()
            });
        }
    }

    private void cargarPedidos() {
        modeloPedidos.setRowCount(0);
        for (Pedido p : pedidoService.obtenerTodos()) {
            modeloPedidos.addRow(new Object[]{
                p.getId(), p.getClienteId(), p.getFecha(),
                "$" + String.format("%.2f", p.getTotal()),
                p.getMetodoPago(), p.getEstado()
            });
        }
    }
}
