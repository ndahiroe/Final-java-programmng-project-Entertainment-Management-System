package dao;

import db.DBConnection;
import model.VenueSponsor;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenueSponsorDAO {

    public static List<VenueSponsor> getAll() {
        List<VenueSponsor> list = new ArrayList<>();
        String sql = "SELECT * FROM venue_sponsor ORDER BY VenueID, SponsorID";
        try(Connection c = DBConnection.getConnection();
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(sql)){
            while(rs.next()){
                VenueSponsor vs = new VenueSponsor();
                vs.setVenueID(rs.getInt("VenueID"));
                vs.setSponsorID(rs.getInt("SponsorID"));
                list.add(vs);
            }
        } catch(SQLException ex){ ex.printStackTrace(); }
        return list;
    }

    public static boolean add(VenueSponsor vs){
        String sql = "INSERT INTO venue_sponsor (VenueID, SponsorID) VALUES (?,?)";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, vs.getVenueID());
            ps.setInt(2, vs.getSponsorID());
            return ps.executeUpdate() > 0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }

    public static boolean delete(int venueId, int sponsorId){
        String sql = "DELETE FROM venue_sponsor WHERE VenueID=? AND SponsorID=?";
        try(Connection c = DBConnection.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, venueId);
            ps.setInt(2, sponsorId);
            return ps.executeUpdate() > 0;
        } catch(SQLException ex){ ex.printStackTrace(); return false; }
    }
}
