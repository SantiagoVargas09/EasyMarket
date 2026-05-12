package com.easymarket.service;

import com.easymarket.model.Producto;
import com.easymarket.repository.ProductoRepository;
import com.easymarket.utils.IdGenerator;

import java.util.List;

// Lógica de negocio para los productos.
public class ProductoService {

    private ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public Producto crearProducto(String nombre, String descripcion, double precio, int stock) {
        if (nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }

        Producto nuevo = new Producto(IdGenerator.siguienteProducto(), nombre, descripcion, precio, stock);
        repo.agregar(nuevo);
        return nuevo;
    }

    public void actualizarProducto(int id, String nombre, String descripcion, double precio, int stock) {
        Producto p = repo.buscarPorId(id);
        if (p == null) {
            throw new IllegalArgumentException("No se encontró el producto con ID " + id);
        }
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        repo.actualizar(p);
    }

    public void eliminarProducto(int id) {
        repo.eliminar(id);
    }

    public List<Producto> listarTodos() {
        return repo.getTodos();
    }

    public Producto buscarPorId(int id) {
        return repo.buscarPorId(id);
    }
}
