package dao;

import db.DBConnection;
import model.User;
import utils.SecurityUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Get all users
    public static List<User> getAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY UserID DESC";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while(rs.next()){
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setEmail(rs.getString("Email"));
                u.setFullName(rs.getString("FullName"));
                u.setRole(rs.getString("Role"));
                list.add(u);
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return list;
    }

    // Find user by ID
    public static User findById(int id){
        String sql = "SELECT * FROM users WHERE UserID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setEmail(rs.getString("Email"));
                u.setFullName(rs.getString("FullName"));
                u.setRole(rs.getString("Role"));
                return u;
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    // Find user by username
    public static User findByUsername(String username){
        String sql = "SELECT * FROM users WHERE Username=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                User u = new User();
                u.setUserID(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setPasswordHash(rs.getString("PasswordHash"));
                u.setEmail(rs.getString("Email"));
                u.setFullName(rs.getString("FullName"));
                u.setRole(rs.getString("Role"));
                return u;
            }
        } catch(SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    // Register a new user
    public static boolean register(String username, String password, String email, String fullName){
        String sql = "INSERT INTO users (Username, PasswordHash, Email, FullName) VALUES (?,?,?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, SecurityUtil.hash(password));
            ps.setString(3, email);
            ps.setString(4, fullName);

            return ps.executeUpdate() > 0;
        } catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    // Update user
    public static boolean update(User u){
        String sql = "UPDATE users SET Email=?, FullName=?, Role=? WHERE UserID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, u.getEmail());
            ps.setString(2, u.getFullName());
            ps.setString(3, u.getRole());
            ps.setInt(4, u.getUserID());

            return ps.executeUpdate() > 0;
        } catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    // Delete user
    public static boolean delete(int id){
        String sql = "DELETE FROM users WHERE UserID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch(SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    // Verify user credentials for login
    public static boolean verifyCredentials(String username, String password){
        User user = findByUsername(username);
        return user != null && SecurityUtil.check(password, user.getPasswordHash());
    }
}
