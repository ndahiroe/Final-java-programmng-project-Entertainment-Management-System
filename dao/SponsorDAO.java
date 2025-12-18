package dao;

import db.DBConnection;
import model.Sponsor;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SponsorDAO {

    public static List<Sponsor> getAll() {
        List<Sponsor> list = new ArrayList<>();
        String sql = "SELECT * FROM sponsors ORDER BY SponsorID DESC";
        try(Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql)){
            while(rs.next()){
                Sponsor sp = new Sponsor();
                sp.setSponsorID(rs.getInt("SponsorID"));
                sp.setAttribute1(rs.getString("Attribute1"));
                sp.setAttribute2(rs.getString("Attribute2"));
                sp.setAttribute3(rs.getString("Attribute3"));
                Timestamp ts = rs.getTimestamp("CreatedAt");
                sp.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);
                list.add(sp);
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return list;
    }

    public static Sponsor findById(int id){
        String sql = "SELECT * FROM sponsors WHERE SponsorID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Sponsor sp = new Sponsor();
                sp.setSponsorID(rs.getInt("SponsorID"));
                sp.setAttribute1(rs.getString("Attribute1"));
                sp.setAttribute2(rs.getString("Attribute2"));
                sp.setAttribute3(rs.getString("Attribute3"));
                Timestamp ts = rs.getTimestamp("CreatedAt");
                sp.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);
                return sp;
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return null;
    }

    public static boolean add(Sponsor sp){
        String sql = "INSERT INTO sponsors (Attribute1, Attribute2, Attribute3, CreatedAt) VALUES (?,?,?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, sp.getAttribute1());
            ps.setString(2, sp.getAttribute2());
            ps.setString(3, sp.getAttribute3());
            ps.setTimestamp(4, Timestamp.valueOf(sp.getCreatedAt()));
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean update(Sponsor sp){
        String sql = "UPDATE sponsors SET Attribute1=?, Attribute2=?, Attribute3=?, CreatedAt=? WHERE SponsorID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, sp.getAttribute1());
            ps.setString(2, sp.getAttribute2());
            ps.setString(3, sp.getAttribute3());
            ps.setTimestamp(4, Timestamp.valueOf(sp.getCreatedAt()));
            ps.setInt(5, sp.getSponsorID());
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean delete(int id){
        String sql = "DELETE FROM sponsors WHERE SponsorID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }
}
