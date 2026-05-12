package com.easymarket.service;

import com.easymarket.model.*;
import com.easymarket.repository.PedidoRepository;
import com.easymarket.repository.ProductoRepository;
import com.easymarket.utils.IdGenerator;

import java.util.ArrayList;
import java.util.List;

// Lógica para crear pedidos.
public class PedidoService {

    private PedidoRepository pedidoRepo;
    private ProductoRepository productoRepo;

    public PedidoService(PedidoRepository pedidoRepo, ProductoRepository productoRepo) {
        this.pedidoRepo = pedidoRepo;
        this.productoRepo = productoRepo;
    }

    // Confirma la compra: verifica stock, lo reduce y crea el pedido.
    // Devuelve el Pedido creado para que la UI pueda mostrar el resumen.
    public Pedido confirmarPedido(Cliente cliente, Pago pago) {
        Carrito carrito = cliente.getCarrito();

        if (carrito.estaVacio()) {
            throw new IllegalStateException("El carrito está vacío.");
        }

        // Paso 1: verificar que todos los productos tienen stock suficiente
        // ANTES de modificar cualquier cosa (si falla uno no queremos tocar los demás)
        for (ItemCarrito item : carrito.getItems()) {
            Producto enRepo = productoRepo.buscarPorId(item.getProducto().getId());
            if (enRepo == null) {
                throw new IllegalStateException(
                    "El producto \"" + item.getProducto().getNombre() + "\" ya no existe en el sistema."
                );
            }
            if (!enRepo.hayStock(item.getCantidad())) {
                throw new IllegalStateException(
                    "Stock insuficiente para \"" + enRepo.getNombre() + "\". "
                    + "Disponible: " + enRepo.getStock()
                    + ", solicitado: " + item.getCantidad()
                );
            }
        }

        // Paso 2: guardamos el total ANTES de vaciar el carrito
        double totalFinal = carrito.calcularTotal();

        // Paso 3: copiamos los items del carrito para incluirlos en el pedido
        List<ItemCarrito> itemsDelPedido = new ArrayList<>(carrito.getItems());

        // Paso 4: reducir el stock de cada producto y persistir el cambio
        for (ItemCarrito item : carrito.getItems()) {
            Producto enRepo = productoRepo.buscarPorId(item.getProducto().getId());
            enRepo.reducirStock(item.getCantidad());
            productoRepo.actualizar(enRepo);
        }

        // Paso 5: crear el pedido con el total que calculamos antes
        Pedido pedido = new Pedido(
                IdGenerator.siguientePedido(),
                cliente.getId(),
                itemsDelPedido,
                totalFinal,
                pago.getNombreMetodo()
        );

        pedidoRepo.agregar(pedido);
        cliente.agregarPedido(pedido);

        // Paso 6: vaciar el carrito después de confirmar todo
        carrito.vaciar();

        return pedido;
    }

    public List<Pedido> obtenerPedidosDeCliente(int clienteId) {
        return pedidoRepo.buscarPorCliente(clienteId);
    }

    public List<Pedido> obtenerTodos() {
        return pedidoRepo.getTodos();
    }
}
