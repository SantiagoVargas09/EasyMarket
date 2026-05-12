package com.easymarket.repository;

import com.easymarket.model.Producto;
import com.easymarket.utils.FileManager;
import com.easymarket.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// Guarda y carga los productos desde productos.txt
public class ProductoRepository {

    private static final String RUTA = "data/productos.txt";
    // Usamos | como separador para evitar conflictos con comas en nombres/descripciones
    private static final String SEP  = "|";

    private List<Producto> productos;

    public ProductoRepository() {
        productos = new ArrayList<>();
        cargarDesdeArchivo();

        // Si el archivo estaba vacío (primera vez que se ejecuta),
        // cargamos unos productos de ejemplo para que la tienda no aparezca vacía
        if (productos.isEmpty()) {
            cargarProductosEjemplo();
        }
    }

    public void agregar(Producto producto) {
        productos.add(producto);
        guardarTodos();
    }

    public void actualizar(Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getId() == productoActualizado.getId()) {
                productos.set(i, productoActualizado);
                break;
            }
        }
        guardarTodos();
    }

    public void eliminar(int id) {
        productos.removeIf(p -> p.getId() == id);
        guardarTodos();
    }

    public Producto buscarPorId(int id) {
        for (Producto p : productos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public List<Producto> getTodos() {
        return productos;
    }

    // Formato de cada línea: id|nombre|descripcion|precio|stock
    private void guardarTodos() {
        List<String> lineas = new ArrayList<>();
        for (Producto p : productos) {
            lineas.add(p.getId() + SEP + p.getNombre() + SEP
                    + p.getDescripcion() + SEP + p.getPrecio() + SEP + p.getStock());
        }
        FileManager.escribirArchivo(RUTA, lineas);
    }

    private void cargarDesdeArchivo() {
        List<String> lineas = FileManager.leerArchivo(RUTA);
        for (String linea : lineas) {
            try {
                String[] partes = linea.split("\\|", 5);
                if (partes.length < 5) continue;

                int id             = Integer.parseInt(partes[0].trim());
                String nombre      = partes[1].trim();
                String descripcion = partes[2].trim();
                double precio      = Double.parseDouble(partes[3].trim());
                int stock          = Integer.parseInt(partes[4].trim());

                productos.add(new Producto(id, nombre, descripcion, precio, stock));
                IdGenerator.sincronizarProducto(id);

            } catch (Exception e) {
                System.out.println("Error al leer producto (línea ignorada): " + linea);
            }
        }
    }

    // Productos de ejemplo para la primera ejecución.
    // El admin puede editarlos o borrarlos desde su panel.
    private void cargarProductosEjemplo() {
        Producto[] ejemplos = {
            new Producto(IdGenerator.siguienteProducto(), "Laptop Gamer",
                    "Procesador i7 16GB RAM 512GB SSD", 3500000, 5),
            new Producto(IdGenerator.siguienteProducto(), "Mouse Inalambrico",
                    "Mouse ergonomico 2.4GHz bateria recargable", 85000, 20),
            new Producto(IdGenerator.siguienteProducto(), "Teclado Mecanico",
                    "Teclado RGB switches blue retroiluminado", 210000, 12),
            new Producto(IdGenerator.siguienteProducto(), "Monitor 24 pulgadas",
                    "Full HD 144Hz panel IPS HDMI y DisplayPort", 780000, 8),
            new Producto(IdGenerator.siguienteProducto(), "Auriculares Bluetooth",
                    "Cancelacion de ruido activa 30h bateria", 320000, 15)
        };

        for (Producto p : ejemplos) {
            productos.add(p);
        }
        // Guardamos los ejemplos en el archivo para que persistan
        guardarTodos();
        System.out.println("Productos de ejemplo creados correctamente.");
    }
}
