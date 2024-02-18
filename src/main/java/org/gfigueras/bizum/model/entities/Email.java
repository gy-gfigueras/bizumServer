package org.gfigueras.bizum.model.entities;

import java.util.StringTokenizer;

public class Email {
    String email;
    String username;
    String servicio;

    public Email(String email){
        this.email = email;
        procesarEmail();
    }

    private void procesarEmail(){
        StringTokenizer st = new StringTokenizer(email, "@");
        setUsername(st.nextToken());
        setServicio(st.nextToken());
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setServicio(String servicio) {
        this.servicio = servicio;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getServicio() {
        return servicio;
    }

    @Override
    public String toString(){
        return this.toJson();
    }

    public String toJson(){
        return String.format("{\"name\": \"%s\", \"servicio\": \"%s\"}", username, servicio);
    }
}
