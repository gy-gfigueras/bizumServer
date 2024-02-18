package org.gfigueras.bizum.model.entities;

import java.util.Locale;

public class Cuenta {
    
    private String nCuenta;
    private Double dinero;
    private String nTelefono;
   
    public Cuenta(String nCuenta, Double dinero, String nTelefono) {
        this.nCuenta = nCuenta;
        this.dinero = dinero;
        this.nTelefono = nTelefono;
    }

    public String getnCuenta() {
        return nCuenta;
    }

    public void setnCuenta(String nCuenta) {
        this.nCuenta = nCuenta;
    }
    
    public Double getDinero() {
        return dinero;
    }

    public void setDinero(Double dinero) {
        this.dinero = dinero;
    }

     
    public String getnTelefono() {
        return nTelefono;
    }
    public void setnTelefono(String nTelefono) {
        this.nTelefono = nTelefono;
    }

    @Override 
    public String toString(){
        return this.toJson();
    }

    public String toJson(){
        return String.format(Locale.US,"{ \"nCuenta\": \"%s\", \"dinero\": %.2f, \"nTelefono\": \"%s\" }", nCuenta, dinero, nTelefono);
    }
    public String toJsonWithoutMoney() {
        return String.format("{\"nCuenta\": \"%s\", \"nTelefono\": \"%s\"}", nCuenta, nTelefono);
    }
    
    public String getCuentas(){
        return "";
    }
    
}
