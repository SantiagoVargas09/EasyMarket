package com.easymarket.model;

// Representa un producto que se vende en la tienda.
public class Producto {

    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, String descripcion, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
    }

    // Reduce el stock cuando se hace una compra
    public void reducirStock(int cantidad) {
        if (cantidad > stock) {
            throw new IllegalArgumentException("No hay suficiente stock para: " + nombre);
        }
        this.stock -= cantidad;
    }

    // Aumenta el stock cuando el admin agrega más unidades
    public void aumentarStock(int cantidad) {
        this.stock += cantidad;
    }

    public boolean hayStock(int cantidad) {
        return this.stock >= cantidad;
    }

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", precio) + " (Stock: " + stock + ")";
    }
}
