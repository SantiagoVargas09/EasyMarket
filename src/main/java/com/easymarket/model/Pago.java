package com.easymarket.model;

// Clase abstracta que representa un método de pago.
// Usamos abstracción porque el proceso de pago cambia según el método
// (efectivo, tarjeta, etc.) pero el pedido no necesita saber cuál es.
public abstract class Pago {

    private double monto;

    public Pago(double monto) {
        this.monto = monto;
    }

    // Cada subclase implementa cómo se procesa ese tipo de pago
    public abstract String procesarPago();

    // Devuelve el nombre del método para mostrarlo en pantalla
    public abstract String getNombreMetodo();

    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
}
