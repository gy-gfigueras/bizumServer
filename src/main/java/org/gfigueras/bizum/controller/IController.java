package org.gfigueras.bizum.controller;

import java.util.List;

import org.gfigueras.bizum.model.entities.Email;
import org.gfigueras.bizum.model.entities.User;

/**
 * Interfaz de IController
 * @author Guillermo Figueras Jiménez <a href="https://github.com/GFigueras03">(GFIGUERAS)</a>
 */
public interface IController {

    List<User> getUsers();

    User getUser(String name);

    User getUser(Email email);

    boolean isLogin(String username, String password);

    Integer signUp(String username, String email, String password);

    Integer changePassword(String username, String password, String passwordNew);

    int changeUsername(String username, String password, String newUsername);

    int deleteUser(String usernameToDelete, String password);

    int changePasswordForgotten(String username, String email, String passwordNew);

    String getUser(String username, String password);

    Integer assignPhoneNumber(String username, String password, String nTelefono);

    String getAccount(String username, String password, String phoneNumber);

    Integer createAccount(String username, String password, String nTelefono);

    Integer deposit(String username, String password, String nTelefonoEmisor, String nTelefonoReceptor, Double dinero);

    String getAccounts(String username, String password);

    String getCreditCard(String username, String password, String phoneNumber);

}

