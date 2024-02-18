package org.gfigueras.bizum.model.entities;

import java.util.ArrayList;
import java.util.List;

public class User {
    private Integer id;
    private String username;
    private Email email;
    private String password;
    private String salt;
    private List<String> numerosTelefono = new ArrayList<String>();
    
    public User(String username, Email email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getNumeroTelefono() {
        return numerosTelefono;
    }

    public void addNumeroTelefono(String numeroTelefono) {
        this.numerosTelefono.add(numeroTelefono);
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override 
    public String toString(){
        return this.toJson();
    }

    public String toJson() {
        StringBuilder numerosTelefonoJson = new StringBuilder("[");
        if (numerosTelefono != null && !numerosTelefono.isEmpty()) {
            for (String numero : numerosTelefono) {
                numerosTelefonoJson.append(String.format("\"%s\", ", numero));
            }
            numerosTelefonoJson.setLength(numerosTelefonoJson.length() - 2);
        }
        numerosTelefonoJson.append("]");

        return String.format("{\"id\": \"%d\", \"username\": \"%s\", \"email\": %s, \"numerosTelefono\": %s}", id, username, email.toJson(), numerosTelefonoJson.toString());
    }
}
