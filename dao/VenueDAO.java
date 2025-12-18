package dao;

import db.DBConnection;
import model.Venue;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueDAO {

    public static List<Venue> getAll() {
        List<Venue> list = new ArrayList<>();
        String sql = "SELECT * FROM venues ORDER BY VenueID DESC";
        try(Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql)){
            while(rs.next()){
                Venue v = new Venue();
                v.setVenueID(rs.getInt("VenueID"));
                v.setName(rs.getString("Name"));
                v.setAddress(rs.getString("Address"));
                v.setCapacity(rs.getInt("Capacity"));
                v.setManager(rs.getString("Manager"));
                v.setContact(rs.getString("Contact"));
                list.add(v);
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return list;
    }

    public static Venue findById(int id){
        String sql = "SELECT * FROM venues WHERE VenueID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Venue v = new Venue();
                v.setVenueID(rs.getInt("VenueID"));
                v.setName(rs.getString("Name"));
                v.setAddress(rs.getString("Address"));
                v.setCapacity(rs.getInt("Capacity"));
                v.setManager(rs.getString("Manager"));
                v.setContact(rs.getString("Contact"));
                return v;
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return null;
    }

    public static boolean add(Venue v){
        String sql = "INSERT INTO venues (Name, Address, Capacity, Manager, Contact) VALUES (?,?,?,?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,v.getName());
            ps.setString(2,v.getAddress());
            ps.setInt(3,v.getCapacity());
            ps.setString(4,v.getManager());
            ps.setString(5,v.getContact());
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean update(Venue v){
        String sql = "UPDATE venues SET Name=?, Address=?, Capacity=?, Manager=?, Contact=? WHERE VenueID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,v.getName());
            ps.setString(2,v.getAddress());
            ps.setInt(3,v.getCapacity());
            ps.setString(4,v.getManager());
            ps.setString(5,v.getContact());
            ps.setInt(6,v.getVenueID());
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean delete(int id){
        String sql = "DELETE FROM venues WHERE VenueID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }
}
