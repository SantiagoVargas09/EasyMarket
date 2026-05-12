package com.easymarket.model;

// Pago en efectivo: el más sencillo.
public class PagoEfectivo extends Pago {

    public PagoEfectivo(double monto) {
        super(monto);
    }

    @Override
    public String procesarPago() {
        return "Pago en efectivo de $" + String.format("%.2f", getMonto()) + " registrado.";
    }

    @Override
    public String getNombreMetodo() {
        return "Efectivo";
    }
}
