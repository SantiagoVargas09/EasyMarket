package com.easymarket.model;

// Pago con tarjeta: guarda los últimos 4 dígitos para mostrarlo al usuario.
public class PagoTarjeta extends Pago {

    private String ultimosDigitos;

    public PagoTarjeta(double monto, String ultimosDigitos) {
        super(monto);
        this.ultimosDigitos = ultimosDigitos;
    }

    @Override
    public String procesarPago() {
        return "Pago con tarjeta ****" + ultimosDigitos
                + " de $" + String.format("%.2f", getMonto()) + " aprobado.";
    }

    @Override
    public String getNombreMetodo() {
        return "Tarjeta (****" + ultimosDigitos + ")";
    }

    public String getUltimosDigitos() { return ultimosDigitos; }
}
