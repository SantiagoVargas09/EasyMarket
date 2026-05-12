package com.easymarket.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Clase de utilidad para leer y escribir archivos .txt.
// Todos los métodos son estáticos para poder usarlos desde cualquier parte.
public class FileManager {

    // Lee todas las líneas de un archivo y las devuelve en una lista
    public static List<String> leerArchivo(String ruta) {
        List<String> lineas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                // Ignoramos líneas vacías
                if (!linea.trim().isEmpty()) {
                    lineas.add(linea.trim());
                }
            }
        } catch (IOException e) {
            // Si el archivo no existe todavía, simplemente devolvemos lista vacía
            System.out.println("Archivo no encontrado, se creará al guardar: " + ruta);
        }
        return lineas;
    }

    // Sobreescribe el archivo completo con las líneas que le pasamos
    public static void escribirArchivo(String ruta, List<String> lineas) {
        try {
            // Creamos las carpetas si no existen
            File archivo = new File(ruta);
            archivo.getParentFile().mkdirs();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, false))) {
                for (String linea : lineas) {
                    writer.write(linea);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Error al escribir el archivo: " + ruta + " -> " + e.getMessage());
        }
    }

    // Agrega una sola línea al final del archivo sin borrar lo que ya había
    public static void agregarLinea(String ruta, String linea) {
        try {
            File archivo = new File(ruta);
            archivo.getParentFile().mkdirs();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivo, true))) {
                writer.write(linea);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al agregar línea: " + e.getMessage());
        }
    }
}
