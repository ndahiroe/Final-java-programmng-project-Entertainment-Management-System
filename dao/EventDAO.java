package dao;

import db.DBConnection;
import model.Event;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventDAO {

    // Get all events
    public static List<Event> getAll() {
        List<Event> list = new ArrayList<>();
        String sql = "SELECT * FROM events ORDER BY EventID DESC";
        try (Connection c = DBConnection.getConnection();
             Statement s = c.createStatement();
             ResultSet rs = s.executeQuery(sql)) {

            while (rs.next()) {
                Event ev = new Event();
                ev.setEventID(rs.getInt("EventID"));
                ev.setReferenceID(rs.getString("ReferenceID"));
                ev.setDescription(rs.getString("Description"));
                Timestamp ts = rs.getTimestamp("Date");
                ev.setDate(ts != null ? ts.toLocalDateTime() : null);
                ev.setStatus(rs.getString("Status"));
                ev.setRemarks(rs.getString("Remarks"));
                ev.setUserID(rs.getInt("UserID"));
                list.add(ev);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Find by ID
    public static Event findById(int eventID) {
        String sql = "SELECT * FROM events WHERE EventID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, eventID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Event ev = new Event();
                ev.setEventID(rs.getInt("EventID"));
                ev.setReferenceID(rs.getString("ReferenceID"));
                ev.setDescription(rs.getString("Description"));
                Timestamp ts = rs.getTimestamp("Date");
                ev.setDate(ts != null ? ts.toLocalDateTime() : null);
                ev.setStatus(rs.getString("Status"));
                ev.setRemarks(rs.getString("Remarks"));
                ev.setUserID(rs.getInt("UserID"));
                return ev;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Add event
    public static boolean add(Event ev) {
        String sql = "INSERT INTO events(ReferenceID, Description, Date, Status, Remarks, UserID) VALUES(?,?,?,?,?,?)";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ev.getReferenceID());
            ps.setString(2, ev.getDescription());
            ps.setTimestamp(3, ev.getDate() != null ? Timestamp.valueOf(ev.getDate()) : null);
            ps.setString(4, ev.getStatus());
            ps.setString(5, ev.getRemarks());
            ps.setInt(6, ev.getUserID());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Update event
    public static boolean update(Event ev) {
        String sql = "UPDATE events SET ReferenceID=?, Description=?, Date=?, Status=?, Remarks=?, UserID=? WHERE EventID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, ev.getReferenceID());
            ps.setString(2, ev.getDescription());
            ps.setTimestamp(3, ev.getDate() != null ? Timestamp.valueOf(ev.getDate()) : null);
            ps.setString(4, ev.getStatus());
            ps.setString(5, ev.getRemarks());
            ps.setInt(6, ev.getUserID());
            ps.setInt(7, ev.getEventID());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Delete event
    public static boolean delete(int eventID) {
        String sql = "DELETE FROM events WHERE EventID=?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, eventID);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
