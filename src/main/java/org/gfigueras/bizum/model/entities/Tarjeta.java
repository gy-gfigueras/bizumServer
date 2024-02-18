package org.gfigueras.bizum.model.entities;

public class Tarjeta {

    private String nTarjeta;
    private String titular;
    private String caducidad;
    private int cvv;
    private String cuenta;

    public Tarjeta(String nTarjeta, String titular, int cvv,String caducidad, String cuenta) {
        this.nTarjeta = nTarjeta;
        this.titular = titular;
        this.caducidad = caducidad;
        this.cvv = cvv;
        this.cuenta = cuenta;
    }

    public String getnTarjeta() {
        return nTarjeta;
    }

    public void setnTarjeta(String nTarjeta) {
        this.nTarjeta = nTarjeta;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getCaducidad() {
        return caducidad;
    }

    public void setCaducidad(String caducidad) {
        this.caducidad = caducidad;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    @Override
    public String toString() {
        return this.toJSON();
    }

    public String toJSON() {
        String jsonFormat = "{\"nTarjeta\": %s, \"titular\": \"%s\", \"caducidad\": \"%s\", \"cvv\": %d, \"cuenta\": \"%s\"}";
        return String.format(jsonFormat, nTarjeta, titular, caducidad, cvv, cuenta);

    }

}
