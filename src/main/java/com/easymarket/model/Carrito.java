package com.easymarket.model;

import java.util.ArrayList;
import java.util.List;

// El carrito guarda los productos que el cliente quiere comprar.
public class Carrito {

    private List<ItemCarrito> items;

    public Carrito() {
        this.items = new ArrayList<>();
    }

    // Agrega un producto al carrito.
    // Si ya está, suma la cantidad en lugar de duplicarlo.
    public void agregarProducto(Producto producto, int cantidad) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        items.add(new ItemCarrito(producto, cantidad));
    }

    // Quita un producto del carrito usando su ID
    public void eliminarProducto(int productoId) {
        items.removeIf(item -> item.getProducto().getId() == productoId);
    }

    // Suma todos los subtotales y devuelve el total
    public double calcularTotal() {
        double total = 0;
        for (ItemCarrito item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public void vaciar() {
        items.clear();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public List<ItemCarrito> getItems() { return items; }
}
