package org.gfigueras.bizum.view;

import org.springframework.web.bind.annotation.RestController;

import org.gfigueras.bizum.controller.Controller;
import org.gfigueras.bizum.controller.IController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Routing {

    private IController controller = new Controller();

    @GetMapping("/login")
    public Boolean login(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return controller.isLogin(username, password);
    }

    @GetMapping("/signup")
    public Integer signUp(
            @RequestParam(value = "username", defaultValue = "") String name,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return controller.signUp(name, email, password);
    }

    @GetMapping("/changeusername")
    public Integer changeUsername(
            @RequestParam(value = "username", defaultValue = "") String name,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "newusername", defaultValue = "") String newUsername) {
        return controller.changeUsername(name, password, newUsername);
    }

    @GetMapping("/deleteuser")
    public Integer deleteUser(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return controller.deleteUser(username, password);
    }

    @GetMapping("/changepassword")
    public Integer changePassword(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "newpassword", defaultValue = "") String newPassword) {
        return controller.changePassword(username, password, newPassword);
    }

    @GetMapping("/changepasswordforgotten")
    public Integer changePasswordForgotten(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "email", defaultValue = "") String email,
            @RequestParam(value = "newpassword", defaultValue = "") String newPassword) {
        return controller.changePasswordForgotten(username, email, newPassword);
    }

    @GetMapping("/getusersession")
    public String getUserSesion(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return controller.getUser(username, password).toString();
    }

    @GetMapping("/assignphonenumber")
    public Integer assignPhoneNumber(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "telefono", defaultValue = "") String telefono) {
        return controller.assignPhoneNumber(username, password, telefono);
    }

    @GetMapping("/createaccount")
    public Integer createAccount(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "telefono", defaultValue = "") String telefono) {
        return controller.createAccount(username, password, telefono);
    }

    @GetMapping("/getaccount")
    public String getAccount(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "telefono", defaultValue = "") String telefono) {
        return controller.getAccount(username, password, telefono);
    }

    @GetMapping("/deposit")
    public Integer deposit(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "telefonoemisor", defaultValue = "") String telefonoEmisor,
            @RequestParam(value = "telefonoreceptor", defaultValue = "") String telefonoReceptor,
            @RequestParam(value = "dinero", defaultValue = "") Double dinero) {
        return controller.deposit(username, password, telefonoEmisor, telefonoReceptor, dinero);
    }

    @GetMapping("/getaccounts")
    public String getAccounts(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password) {
        return controller.getAccounts(username, password);
    }

    @GetMapping("/getcreditcard")
    public String getCreditCard(
            @RequestParam(value = "username", defaultValue = "") String username,
            @RequestParam(value = "password", defaultValue = "") String password,
            @RequestParam(value = "telefono", defaultValue = "") String telefono) {
        return controller.getCreditCard(username, password, telefono);
    }
}
