package com.easymarket.model;

import java.util.ArrayList;
import java.util.List;

// El cliente es el usuario que puede comprar productos.
// Hereda de Usuario y agrega el carrito y el historial de pedidos.
public class Cliente extends Usuario {

    private Carrito carrito;
    private List<Pedido> historialPedidos;

    public Cliente(int id, String nombre, String email, String contrasena) {
        super(id, nombre, email, contrasena);
        this.carrito = new Carrito();
        this.historialPedidos = new ArrayList<>();
    }

    // El cliente siempre es de tipo "Cliente"
    @Override
    public String getTipo() {
        return "Cliente";
    }

    public void agregarPedido(Pedido pedido) {
        historialPedidos.add(pedido);
    }

    public Carrito getCarrito() { return carrito; }

    public List<Pedido> getHistorialPedidos() { return historialPedidos; }
}
