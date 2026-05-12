package com.easymarket.model;

// Clase base para todos los usuarios del sistema.
// Es abstracta porque no tiene sentido crear un "Usuario" genérico,
// siempre va a ser un Cliente o un Administrador.
public abstract class Usuario {

    private int id;
    private String nombre;
    private String email;
    private String contrasena;

    public Usuario(int id, String nombre, String email, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.contrasena = contrasena;
    }

    // Verifica si el email y la contraseña coinciden con los del usuario
    public boolean verificarLogin(String email, String contrasena) {
        return this.email.equals(email) && this.contrasena.equals(contrasena);
    }

    // Cada subclase dice qué tipo de usuario es
    public abstract String getTipo();

    // Getters y setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    @Override
    public String toString() {
        return nombre + " (" + email + ")";
    }
}
