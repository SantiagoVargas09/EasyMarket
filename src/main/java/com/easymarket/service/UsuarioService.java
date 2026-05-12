package com.easymarket.service;

import com.easymarket.model.Cliente;
import com.easymarket.model.Usuario;
import com.easymarket.repository.UsuarioRepository;
import com.easymarket.utils.IdGenerator;

// Lógica de negocio relacionada con los usuarios.
public class UsuarioService {

    private UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    // Registra un nuevo cliente. Valida que el email no esté duplicado.
    public Cliente registrarCliente(String nombre, String email, String contrasena) {
        if (nombre.isBlank() || email.isBlank() || contrasena.isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("El email no es válido.");
        }
        if (contrasena.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres.");
        }
        if (repo.existeEmail(email)) {
            throw new IllegalArgumentException("Ya existe una cuenta con ese email.");
        }

        Cliente nuevo = new Cliente(IdGenerator.siguienteUsuario(), nombre, email, contrasena);
        repo.agregar(nuevo);
        return nuevo;
    }

    // Intenta hacer login. Devuelve el usuario o null si los datos son incorrectos.
    public Usuario login(String email, String contrasena) {
        if (email.isBlank() || contrasena.isBlank()) {
            throw new IllegalArgumentException("Email y contraseña son obligatorios.");
        }
        return repo.buscarPorLogin(email, contrasena);
    }
}
