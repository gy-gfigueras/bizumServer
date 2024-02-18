package org.gfigueras.bizum.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.gfigueras.bizum.model.util.Cipher;
import org.gfigueras.bizum.model.util.ByteHexConverter;
import org.gfigueras.bizum.model.entities.Cuenta;
import org.gfigueras.bizum.model.entities.Email;
import org.gfigueras.bizum.model.entities.ServerCodes;
import org.gfigueras.bizum.model.entities.Tarjeta;
import org.gfigueras.bizum.model.entities.User;

public class DAODB implements IDAODB {
    private final static String URL = "jdbc:mysql://gfaws.ckwawk4f9hpm.eu-west-3.rds.amazonaws.com:3306/Gfcuentas";
    private final static String DB_USER = "admin";
    private final static String DB_PASSWORD = "gfmanager";
    private final static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    private Connection connection;

    private void connect() {
        try {
            Class.forName(DB_DRIVER);
            connection = DriverManager.getConnection(URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void disconnect() {
        try {
            this.connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        try {
            connect();
            String query = "SELECT * FROM User";
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                users.add(new User(rs.getString("username"), new Email(rs.getString("email"))));
            }
            return users;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect();
        }
    }

    @Override
    public User getUser(String name) {
        connect();
        String query = "SELECT * FROM User WHERE username = ?";
        String queryPhone = "SELECT * FROM Telefono WHERE id_user = ?";
        User user = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, name);
            ps.executeQuery();
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));
                user.setPassword(rs.getString("password"));
                user.setId(rs.getInt("id"));
                user.setSalt(rs.getString("salt"));
            }

            PreparedStatement psPhone = connection.prepareStatement(queryPhone);
            psPhone.setInt(1, rs.getInt("id"));
            ResultSet rsPhone = psPhone.executeQuery();

            while (rsPhone.next()) {
                user.addNumeroTelefono(rsPhone.getString("nTelefono"));
            }
            rs.close();
            rsPhone.close();
            return user;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect();
        }
    }

    @Override
    public User getUser(Email email) {
        connect();
        String query = "SELECT * FROM User WHERE username = ?";
        User user = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, email.getEmail());
            ps.executeQuery();

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));
                user.setPassword(rs.getString("password"));
                user.setId(rs.getInt("id"));
                user.setSalt(rs.getString("salt"));
                return user;
            } else
                return null;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect();
        }
    }

    @Override
    public boolean isLogin(String username, String password) {
        connect();
        String query = "SELECT * FROM User WHERE username = ?";
        User user = null;
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(rs.getString("username"), new Email(rs.getString("email")));
                user.setSalt(rs.getString("salt"));
                return Cipher.verifyPassword(password, ByteHexConverter.hexToBytes(user.getSalt()),
                        ByteHexConverter.hexToBytes(rs.getString("password")));
            } else
                return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }

    }

    @Override
    public Integer signUp(String username, String email, String password) {
        connect();
        byte[] salt = Cipher.generateSalt();
        String query = "INSERT INTO User (username, email, password, salt) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, ByteHexConverter.bytesToHex(Cipher.hashPassword(password, salt)));
            ps.setString(4, ByteHexConverter.bytesToHex(salt));
            ps.executeUpdate();
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    @Override
    public int changeUsername(String username, String password, String newUsername) {
        String query = "UPDATE User SET username = ? WHERE username = ?";
        try {
            if (isLogin(username, password)) {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, newUsername);
                ps.setString(2, username);
                ps.executeUpdate();
                return ServerCodes.SUCCESS.toInt();
            } else {
                return ServerCodes.INVALID_LOGIN.toInt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    @Override
    public int deleteUser(String usernameToDelete, String passwordtoDelete) {
        String query = "DELETE FROM User WHERE username = ?";
        try {
            if (isLogin(usernameToDelete, passwordtoDelete)) {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, usernameToDelete);
                ps.executeUpdate();
                return ServerCodes.SUCCESS.toInt();
            } else {
                return ServerCodes.INVALID_LOGIN.toInt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    @Override
    public Integer changePasswordForgotten(String username, String email, String passwordNew) {
        String query = "UPDATE User SET password = ?, salt = ? WHERE username = ? AND email = ?";
        try {
            byte[] salt = Cipher.generateSalt();
            connect();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, ByteHexConverter.bytesToHex(Cipher.hashPassword(passwordNew, salt)));
            ps.setString(2, ByteHexConverter.bytesToHex(salt));
            ps.setString(3, username);
            ps.setString(4, email);

            if (ps.executeUpdate() == 0) {
                return ServerCodes.INVALID_LOGIN.toInt();
            }
            return ServerCodes.SUCCESS.toInt();
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    @Override
    public Integer changePassword(String username, String password, String passwordNew) {
        String query = "UPDATE User SET password = ?, salt = ? WHERE username = ?";
        try {
            if (isLogin(username, password)) {
                connect();
                byte[] salt = Cipher.generateSalt();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, ByteHexConverter.bytesToHex(Cipher.hashPassword(passwordNew, salt)));
                ps.setString(2, ByteHexConverter.bytesToHex(salt));
                ps.setString(3, username);
                ps.executeUpdate();
                return ServerCodes.SUCCESS.toInt();
            } else {
                return ServerCodes.INVALID_LOGIN.toInt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    @Override
    public String getUser(String username, String password) {
        if (isLogin(username, password)) {
            return getUser(username).toString();
        } else {
            return "Invalid login";
        }
    }

    @Override
    public Integer assignPhoneNumber(String username, String password, String nTelefono) {
        String query = "INSERT INTO Telefono (nTelefono, id_user) VALUES (?, ?)";
        try {
            if (isLogin(username, password)) {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setString(1, nTelefono);
                ps.setInt(2, getUser(username).getId());
                ps.executeUpdate();
                return ServerCodes.SUCCESS.toInt();
            } else {
                return ServerCodes.INVALID_LOGIN.toInt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    @Override
    public String getAccount(String username, String password, String phoneNumber) {
        String query = "SELECT Account.accountNumber, Account.money, Account.nTelefono FROM Account, Telefono, User WHERE Account.nTelefono = Telefono.nTelefono AND Telefono.id_user = User.id AND User.id = ? AND Telefono.nTelefono = ? ";
        try {
            if (isLogin(username, password)) {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, getUser(username).getId());
                ps.setString(2, phoneNumber);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Cuenta(rs.getString("accountNumber"), rs.getDouble("money"), rs.getString("nTelefono"))
                            .toString();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect();
        }
    }

    @Override
    public Integer createAccount(String username, String password, String nTelefono) {
        String query = "INSERT INTO Account (accountNumber, money, nTelefono) VALUES (?, ?, ?)";
        String queryId = "SELECT * FROM Telefono WHERE nTelefono = ? AND id_user = ?";

        if (isLogin(username, password)) {
            connect();
            try {

                PreparedStatement psId = connection.prepareStatement(queryId);
                psId.setString(1, nTelefono);
                psId.setInt(2, getUser(username).getId());
                connect();
                ResultSet rs = psId.executeQuery();

                if (rs.next()) {
                    PreparedStatement ps = connection.prepareStatement(query);
                    ps.setString(1, Cipher.generateAccountNumber());
                    ps.setDouble(2, 0.0);
                    ps.setString(3, nTelefono);
                    ps.executeUpdate();
                    return ServerCodes.SUCCESS.toInt();

                } else {
                    return 99;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return ServerCodes.DATABASE_ERROR.toInt();
            } finally {
                disconnect();
            }
        } else {
            return ServerCodes.INVALID_LOGIN.toInt();
        }
    }

    @Override
    public Integer deposit(String username, String password, String nTelefonoEmisor, String nTelefonoReceptor,
            Double dinero) {
        String queryEmisor = "UPDATE Account SET money = money - ? WHERE nTelefono = ?";
        String queryReceptor = "UPDATE Account SET money = money + ? WHERE nTelefono = ?";

        try {
            if (isLogin(username, password)) {
                connect();
                PreparedStatement psEmisor = connection.prepareStatement(queryEmisor);
                PreparedStatement psReceptor = connection.prepareStatement(queryReceptor);

                psEmisor.setDouble(1, dinero);
                psEmisor.setString(2, nTelefonoEmisor);

                psReceptor.setDouble(1, dinero);
                psReceptor.setString(2, nTelefonoReceptor);

                if(telefonoExists(nTelefonoReceptor) && telefonoExists(nTelefonoEmisor)){
                   if(haveMoney(nTelefonoEmisor, dinero)){
                    connect();
                    psEmisor.executeUpdate();
                    psReceptor.executeUpdate();
                   }else{
                        
                    }
                }else{
                    return ServerCodes.INVALID_PHONE.toInt();
                }
                return ServerCodes.SUCCESS.toInt();
            } else {
                return ServerCodes.INVALID_LOGIN.toInt();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toInt();
        } finally {
            disconnect();
        }
    }

    public boolean haveMoney(String nTelefonoEmisor, double dinero){
        String query = "SELECT money FROM Account WHERE nTelefono = ?";
        try {
            connect();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, nTelefonoEmisor);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                if(rs.getDouble("money") >= dinero){
                    return true;
                }else{
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }
        return false;
    }

    public boolean telefonoExists(String telefono){
        String query = "SELECT * FROM Telefono WHERE nTelefono = ?";
        try {
            connect();
            PreparedStatement ps = connection.prepareStatement(query);
            ps.setString(1, telefono);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            disconnect();
        }
    }

    @Override
    public String getAccounts(String username, String password) {
        String query = "SELECT Account.accountNumber, Account.nTelefono FROM Account, Telefono, User WHERE Account.nTelefono = Telefono.nTelefono AND Telefono.id_user = User.id AND User.id = ?";
        List<Cuenta> cuentas = new ArrayList<Cuenta>();

        try {
            if (isLogin(username, password)) {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, getUser(username).getId());
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    cuentas.add(new Cuenta(rs.getString("accountNumber"), 0.0, rs.getString("nTelefono")));
                }

                String jString = "";
                for (Cuenta cuenta : cuentas) {
                    jString += cuenta.toJsonWithoutMoney() + ",";
                }
                return String.format("{\"Cuentas\":[%s]}", jString.substring(0, jString.length() - 1));
            } else {
                return ServerCodes.INVALID_LOGIN.toString();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ServerCodes.DATABASE_ERROR.toString();
        } finally {
            disconnect();
        }
    }


    @Override
    public String getCreditCard(String username, String password, String phoneNumber) {
        String query = "SELECT cardNumber, expirationDate, cvv, name, Card.account_number FROM Card, Account, Telefono, User WHERE Card.account_number = Account.accountNumber AND Account.nTelefono = Telefono.nTelefono AND Telefono.id_user = User.id AND User.id = ? AND Account.accountNumber = ?";
        if (isLogin(username, password)) {
            String cuenta = getAccountObject(username, password, phoneNumber).getnCuenta();
            try {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, getUser(username).getId());
                connect();
                ps.setString(2, cuenta );
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Tarjeta(rs.getString("cardNumber"), rs.getString("name"), rs.getInt("cvv"),
                            rs.getString("expirationDate"), rs.getString("account_number")).toString();
                } else {
                    return ServerCodes.INVALID_LOGIN.toString();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return ServerCodes.DATABASE_ERROR.toString();
            } finally {
                disconnect(); 
            }
        }
        return null;
    }

    @Override
    public Cuenta getAccountObject(String username, String password,String phoneNumber) {
        String query = "SELECT Account.accountNumber, Account.money, Account.nTelefono FROM Account, Telefono, User WHERE Account.nTelefono = Telefono.nTelefono AND Telefono.id_user = User.id AND User.id = ? AND Telefono.nTelefono = ? ";
        try {
            if (isLogin(username, password)) {
                connect();
                PreparedStatement ps = connection.prepareStatement(query);
                ps.setInt(1, getUser(username).getId());
                ps.setString(2, phoneNumber);

                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new Cuenta(rs.getString("accountNumber"), rs.getDouble("money"), rs.getString("nTelefono"));
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            disconnect();
        }
    }


}
