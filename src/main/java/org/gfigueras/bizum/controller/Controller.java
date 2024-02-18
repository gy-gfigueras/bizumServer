package org.gfigueras.bizum.controller;

import java.util.List;

import org.gfigueras.bizum.model.database.FactoryDB;
import org.gfigueras.bizum.model.database.IDAODB;
import org.gfigueras.bizum.model.entities.Email;
import org.gfigueras.bizum.model.entities.User;


public class Controller implements IController{
    private IDAODB dao = FactoryDB.getDB(FactoryDB.MODO_MYSQL);

    @Override
    public List<User> getUsers() {
        return dao.getUsers();
    }

    @Override
    public User getUser(String name) {
        return dao.getUser(name);
    }

    @Override
    public User getUser(Email email) {
        return dao.getUser(email);
    }

    @Override
    public boolean isLogin(String username, String password) {
       return dao.isLogin(username, password);
    }

    @Override
    public Integer signUp(String username, String email, String password) {
        return dao.signUp(username, email, password);
    }

    @Override
    public Integer changePassword(String username, String password, String passwordNew) {
        return dao.changePassword(username, password, passwordNew);
    }

    @Override
    public int changeUsername(String username, String password, String newUsername) {
       return dao.changeUsername(username, password, newUsername);
    }

    @Override
    public int deleteUser(String usernameToDelete, String password) {
        return dao.deleteUser(usernameToDelete, password);
    }

    @Override
    public int changePasswordForgotten(String username, String email, String passwordNew) {
       return dao.changePasswordForgotten(username, email, passwordNew);
    }

    @Override
    public String getUser(String username, String password) {
        return dao.getUser(username, password);
    }

    @Override
    public Integer assignPhoneNumber(String username, String password, String nTelefono) {
        return dao.assignPhoneNumber(username, password, nTelefono);
    }

    @Override
    public Integer createAccount(String username, String password, String nTelefono) {
        return dao.createAccount(username, password, nTelefono);
    }

    @Override
    public String getAccount(String username, String password, String phoneNumber) {
        return dao.getAccount(username, password, phoneNumber);
    }

    @Override
    public Integer deposit(String username, String password, String nTelefonoEmisor, String nTelefonoReceptor,
            Double dinero) {
        return dao.deposit(username, password, nTelefonoEmisor, nTelefonoReceptor, dinero);
    }

    @Override
    public String getAccounts(String username, String password) {
        return dao.getAccounts(username, password);
    }

    @Override
    public String getCreditCard(String username, String password, String phoneNumber) {
        return dao.getCreditCard(username, password, phoneNumber);
    }

    
}
