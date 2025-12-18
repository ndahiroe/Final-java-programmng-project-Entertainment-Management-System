package dao;

import db.DBConnection;
import model.Review;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public static List<Review> getAll() {
        List<Review> list = new ArrayList<>();
        String sql = "SELECT * FROM reviews ORDER BY ReviewID DESC";
        try(Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql)){
            while(rs.next()){
                Review r = new Review();
                r.setReviewID(rs.getInt("ReviewID"));
                r.setAttribute1(rs.getString("Attribute1"));
                r.setAttribute2(rs.getString("Attribute2"));
                r.setAttribute3(rs.getString("Attribute3"));
                Timestamp ts = rs.getTimestamp("CreatedAt");
                if(ts!=null) r.setCreatedAt(ts.toLocalDateTime());
                r.setUserID(rs.getInt("UserID"));
                r.setSponsorID(rs.getInt("SponsorID"));
                list.add(r);
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return list;
    }

    public static Review findById(int id){
        String sql = "SELECT * FROM reviews WHERE ReviewID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Review r = new Review();
                r.setReviewID(rs.getInt("ReviewID"));
                r.setAttribute1(rs.getString("Attribute1"));
                r.setAttribute2(rs.getString("Attribute2"));
                r.setAttribute3(rs.getString("Attribute3"));
                Timestamp ts = rs.getTimestamp("CreatedAt");
                if(ts!=null) r.setCreatedAt(ts.toLocalDateTime());
                r.setUserID(rs.getInt("UserID"));
                r.setSponsorID(rs.getInt("SponsorID"));
                return r;
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return null;
    }

    public static boolean add(Review r){
        String sql = "INSERT INTO reviews (Attribute1,Attribute2,Attribute3,CreatedAt,UserID,SponsorID) VALUES (?,?,?,?,?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,r.getAttribute1());
            ps.setString(2,r.getAttribute2());
            ps.setString(3,r.getAttribute3());
            ps.setTimestamp(4, r.getCreatedAt()==null?Timestamp.valueOf(LocalDateTime.now()):Timestamp.valueOf(r.getCreatedAt()));
            ps.setInt(5,r.getUserID());
            ps.setInt(6,r.getSponsorID());
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean update(Review r){
        String sql = "UPDATE reviews SET Attribute1=?, Attribute2=?, Attribute3=?, CreatedAt=?, UserID=?, SponsorID=? WHERE ReviewID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1,r.getAttribute1());
            ps.setString(2,r.getAttribute2());
            ps.setString(3,r.getAttribute3());
            ps.setTimestamp(4, r.getCreatedAt()==null?Timestamp.valueOf(LocalDateTime.now()):Timestamp.valueOf(r.getCreatedAt()));
            ps.setInt(5,r.getUserID());
            ps.setInt(6,r.getSponsorID());
            ps.setInt(7,r.getReviewID());
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean delete(int id){
        String sql = "DELETE FROM reviews WHERE ReviewID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1,id);
            return ps.executeUpdate()>0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }
}
