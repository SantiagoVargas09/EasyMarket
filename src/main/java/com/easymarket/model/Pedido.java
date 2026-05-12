package com.easymarket.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

// Un pedido es la compra confirmada del cliente.
// Guarda todo: los productos, el total, el método de pago y la fecha.
public class Pedido {

    private int id;
    private int clienteId;
    private List<ItemCarrito> items;
    private double total;
    private String metodoPago;
    private String estado;
    private String fecha;

    public Pedido(int id, int clienteId, List<ItemCarrito> items, double total, String metodoPago) {
        this.id = id;
        this.clienteId = clienteId;
        this.items = items;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estado = "CONFIRMADO";
        // Guardamos la fecha y hora en que se hizo el pedido
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.fecha = LocalDateTime.now().format(formato);
    }

    // Constructor para cargar pedidos desde archivo
    public Pedido(int id, int clienteId, double total, String metodoPago, String estado, String fecha) {
        this.id = id;
        this.clienteId = clienteId;
        this.total = total;
        this.metodoPago = metodoPago;
        this.estado = estado;
        this.fecha = fecha;
    }

    // Getters y setters
    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public List<ItemCarrito> getItems() { return items; }
    public double getTotal() { return total; }
    public String getMetodoPago() { return metodoPago; }
    public String getEstado() { return estado; }
    public String getFecha() { return fecha; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Pedido #" + id + " | " + fecha + " | $" + String.format("%.2f", total) + " | " + estado;
    }
}
