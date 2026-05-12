package com.easymarket.repository;

import com.easymarket.model.Administrador;
import com.easymarket.model.Cliente;
import com.easymarket.model.Usuario;
import com.easymarket.utils.FileManager;
import com.easymarket.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// Se encarga de guardar y cargar los usuarios desde el archivo usuarios.txt
public class UsuarioRepository {

    private static final String RUTA = "data/usuarios.txt";
    private static final String SEP  = "|";

    private List<Usuario> usuarios;

    public UsuarioRepository() {
        usuarios = new ArrayList<>();
        cargarDesdeArchivo();
    }

    public void agregar(Usuario usuario) {
        usuarios.add(usuario);
        guardarTodos();
    }

    public Usuario buscarPorLogin(String email, String contrasena) {
        for (Usuario u : usuarios) {
            if (u.verificarLogin(email, contrasena)) {
                return u;
            }
        }
        return null;
    }

    public boolean existeEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public List<Usuario> getTodos() {
        return usuarios;
    }

    // Formato: id|tipo|nombre|email|contrasena
    private void guardarTodos() {
        List<String> lineas = new ArrayList<>();
        for (Usuario u : usuarios) {
            lineas.add(u.getId() + SEP + u.getTipo() + SEP + u.getNombre()
                    + SEP + u.getEmail() + SEP + u.getContrasena());
        }
        FileManager.escribirArchivo(RUTA, lineas);
    }

    private void cargarDesdeArchivo() {
        List<String> lineas = FileManager.leerArchivo(RUTA);
        for (String linea : lineas) {
            try {
                String[] partes = linea.split("\\|", 5);
                if (partes.length < 5) continue;

                int id        = Integer.parseInt(partes[0].trim());
                String tipo   = partes[1].trim();
                String nombre = partes[2].trim();
                String email  = partes[3].trim();
                String clave  = partes[4].trim();

                Usuario u;
                if (tipo.equals("Administrador")) {
                    u = new Administrador(id, nombre, email, clave);
                } else {
                    u = new Cliente(id, nombre, email, clave);
                }
                usuarios.add(u);
                IdGenerator.sincronizarUsuario(id);

            } catch (Exception e) {
                System.out.println("Error al leer usuario (línea ignorada): " + linea);
            }
        }

        // Si no hay ningún usuario todavía, creamos el admin por defecto
        if (usuarios.isEmpty()) {
            Administrador admin = new Administrador(
                    IdGenerator.siguienteUsuario(),
                    "Admin",
                    "admin@easymarket.com",
                    "admin123"
            );
            agregar(admin);
        }
    }
}
