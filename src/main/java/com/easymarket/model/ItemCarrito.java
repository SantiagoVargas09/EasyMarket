package com.easymarket.model;

// Representa un producto dentro del carrito, con su cantidad.
// Por ejemplo: 3 unidades de "Laptop Gamer"
public class ItemCarrito {

    private Producto producto;
    private int cantidad;

    public ItemCarrito(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    // El subtotal es el precio del producto por la cantidad
    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    public Producto getProducto() { return producto; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}
