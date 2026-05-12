package com.easymarket.model;

// El administrador puede gestionar productos y ver todos los pedidos.
// También hereda de Usuario, igual que Cliente.
public class Administrador extends Usuario {

    public Administrador(int id, String nombre, String email, String contrasena) {
        super(id, nombre, email, contrasena);
    }

    @Override
    public String getTipo() {
        return "Administrador";
    }
}
