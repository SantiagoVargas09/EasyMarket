package com.easymarket.utils;

// Genera IDs únicos para usuarios, productos y pedidos.
// Cada vez que se llama a siguiente() devuelve un número mayor.
public class IdGenerator {

    private static int contadorUsuarios = 1;
    private static int contadorProductos = 1;
    private static int contadorPedidos   = 1;

    public static int siguienteUsuario()  { return contadorUsuarios++; }
    public static int siguienteProducto() { return contadorProductos++; }
    public static int siguientePedido()   { return contadorPedidos++; }

    // Estos métodos sincronizan el contador con los IDs que ya existen en los archivos
    public static void sincronizarUsuario(int maxId)  { if (maxId >= contadorUsuarios)  contadorUsuarios  = maxId + 1; }
    public static void sincronizarProducto(int maxId) { if (maxId >= contadorProductos) contadorProductos = maxId + 1; }
    public static void sincronizarPedido(int maxId)   { if (maxId >= contadorPedidos)   contadorPedidos   = maxId + 1; }
}
