package org.gfigueras.bizum.model.database;

import java.util.List;

import org.gfigueras.bizum.model.entities.Cuenta;
import org.gfigueras.bizum.model.entities.Email;
import org.gfigueras.bizum.model.entities.User;

public interface IDAODB {

    List<User> getUsers();

    User getUser(String name);

    User getUser(Email email);

    boolean isLogin(String username, String password);

    Integer signUp(String username, String email, String password);

    Integer changePassword(String username, String password, String passwordNew);

    int changeUsername(String username, String password, String newUsername);

    int deleteUser(String usernameToDelete, String password);

    Integer changePasswordForgotten(String username, String email, String passwordNew);

    String getUser(String username, String password);

    Integer assignPhoneNumber(String username, String password, String nTelefono);

    String getAccount(String username, String password, String phoneNumber);

    Integer deposit(String username, String password, String nTelefonoEmisor, String nTelefonoReceptor, Double dinero);
 
    Integer createAccount(String username, String password, String nTelefono);

    String getAccounts(String username, String password);

    String getCreditCard(String username, String password, String phoneNumber);
    
    Cuenta getAccountObject(String username, String password, String phoneNumber);


}
