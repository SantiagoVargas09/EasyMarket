package com.easymarket.repository;

import com.easymarket.model.Pedido;
import com.easymarket.utils.FileManager;
import com.easymarket.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// Guarda y carga los pedidos desde pedidos.txt
public class PedidoRepository {

    private static final String RUTA = "data/pedidos.txt";
    private static final String SEP  = "|";

    private List<Pedido> pedidos;

    public PedidoRepository() {
        pedidos = new ArrayList<>();
        cargarDesdeArchivo();
    }

    public void agregar(Pedido pedido) {
        pedidos.add(pedido);
        guardarTodos();
    }

    public List<Pedido> buscarPorCliente(int clienteId) {
        List<Pedido> resultado = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getClienteId() == clienteId) {
                resultado.add(p);
            }
        }
        return resultado;
    }

    public List<Pedido> getTodos() {
        return pedidos;
    }

    // Formato: id|clienteId|total|metodoPago|estado|fecha
    private void guardarTodos() {
        List<String> lineas = new ArrayList<>();
        for (Pedido p : pedidos) {
            lineas.add(p.getId() + SEP + p.getClienteId() + SEP
                    + p.getTotal() + SEP + p.getMetodoPago() + SEP
                    + p.getEstado() + SEP + p.getFecha());
        }
        FileManager.escribirArchivo(RUTA, lineas);
    }

    private void cargarDesdeArchivo() {
        List<String> lineas = FileManager.leerArchivo(RUTA);
        for (String linea : lineas) {
            try {
                String[] partes = linea.split("\\|", 6);
                if (partes.length < 6) continue;

                int id        = Integer.parseInt(partes[0].trim());
                int clienteId = Integer.parseInt(partes[1].trim());
                double total  = Double.parseDouble(partes[2].trim());
                String metodo = partes[3].trim();
                String estado = partes[4].trim();
                String fecha  = partes[5].trim();

                pedidos.add(new Pedido(id, clienteId, total, metodo, estado, fecha));
                IdGenerator.sincronizarPedido(id);

            } catch (Exception e) {
                System.out.println("Error al leer pedido (línea ignorada): " + linea);
            }
        }
    }
}
