package ui;

import dao.*;
import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public class DashboardUI extends JFrame {

    private String currentUser;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public DashboardUI(String username) {
        this.currentUser = username;
        setTitle("EMS - Dashboard (" + username + ")");
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel mainPanel = new JPanel(new java.awt.BorderLayout());
        
        JPanel topPanel = new JPanel();
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(e -> {
            dispose();
            new LoginUI().setVisible(true);
        });
        topPanel.add(btnLogout);
        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);
        
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Events", createEventsPanel());
        tabbedPane.addTab("Users", createUsersPanel());
        tabbedPane.addTab("Venues", createVenuesPanel());
        tabbedPane.addTab("Sponsors", createSponsorsPanel());
        tabbedPane.addTab("Reviews", createReviewsPanel());
        tabbedPane.addTab("Venue-Sponsor", createVenueSponsorPanel());

        mainPanel.add(tabbedPane, java.awt.BorderLayout.CENTER);
        add(mainPanel);
    }

    // ================= EVENTS TAB =================
    private JPanel createEventsPanel() {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        DefaultTableModel model = new DefaultTableModel(
            new Object[]{"EventID","Reference","Description","Date","Status","Remarks","UserID"}, 0) {
            @Override public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Event");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(btnPanel, java.awt.BorderLayout.SOUTH);

        // Refresh table
        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Event> events = EventDAO.getAll();
            for(Event ev : events){
                Date dt = ev.getDate() == null ? null : java.util.Date.from(ev.getDate().atZone(java.time.ZoneId.systemDefault()).toInstant());
                String dateStr = dt==null?"":dateFormat.format(dt);
                model.addRow(new Object[]{
                    ev.getEventID(),
                    ev.getReferenceID(),
                    ev.getDescription(),
                    dateStr,
                    ev.getStatus(),
                    ev.getRemarks(),
                    ev.getUserID()
                });
            }
        });

        // Add Event
        btnAdd.addActionListener(e -> {
            JTextField txtRef = new JTextField();
            JTextField txtDesc = new JTextField();
            JTextField txtDate = new JTextField(); // Manual date input
            JTextField txtStatus = new JTextField();
            JTextField txtRemarks = new JTextField();
            JTextField txtUserID = new JTextField();
            Object[] msg = {
                "ReferenceID:", txtRef,
                "Description:", txtDesc,
                "Date (yyyy-MM-dd HH:mm):", txtDate,
                "Status:", txtStatus,
                "Remarks:", txtRemarks,
                "UserID:", txtUserID
            };
            if(JOptionPane.showConfirmDialog(this,msg,"Add Event",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                Event ev = new Event();
                ev.setReferenceID(txtRef.getText());
                ev.setDescription(txtDesc.getText());
                try {
                    ev.setDate(LocalDateTime.parse(txtDate.getText(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                } catch(Exception ex) {
                    ev.setDate(null);
                }
                ev.setStatus(txtStatus.getText());
                ev.setRemarks(txtRemarks.getText());
                try { ev.setUserID(Integer.parseInt(txtUserID.getText())); } catch(NumberFormatException ex) { ev.setUserID(0); }
                if(EventDAO.add(ev)) { JOptionPane.showMessageDialog(this,"Event added."); btnRefresh.doClick(); }
                else JOptionPane.showMessageDialog(this,"Add failed.");
            }
        });

        // Edit Event
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                int eventId = (int) model.getValueAt(row,0);
                Event ev = EventDAO.findById(eventId);
                if(ev != null) {
                    JTextField txtRef = new JTextField(ev.getReferenceID());
                    JTextField txtDesc = new JTextField(ev.getDescription());
                    String dateStr = ev.getDate() == null ? "" : ev.getDate().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                    JTextField txtDate = new JTextField(dateStr);
                    JTextField txtStatus = new JTextField(ev.getStatus());
                    JTextField txtRemarks = new JTextField(ev.getRemarks());
                    JTextField txtUserID = new JTextField(""+ev.getUserID());
                    Object[] msg = {
                        "ReferenceID:", txtRef,
                        "Description:", txtDesc,
                        "Date (yyyy-MM-dd HH:mm):", txtDate,
                        "Status:", txtStatus,
                        "Remarks:", txtRemarks,
                        "UserID:", txtUserID
                    };
                    if(JOptionPane.showConfirmDialog(this,msg,"Edit Event",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                        ev.setReferenceID(txtRef.getText());
                        ev.setDescription(txtDesc.getText());
                        try {
                            ev.setDate(LocalDateTime.parse(txtDate.getText(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        } catch(Exception ex) {
                            ev.setDate(null);
                        }
                        ev.setStatus(txtStatus.getText());
                        ev.setRemarks(txtRemarks.getText());
                        try { ev.setUserID(Integer.parseInt(txtUserID.getText())); } catch(NumberFormatException ex) { ev.setUserID(0); }
                        if(EventDAO.update(ev)) { JOptionPane.showMessageDialog(this,"Updated."); btnRefresh.doClick(); }
                        else JOptionPane.showMessageDialog(this,"Update failed.");
                    }
                }
            } else JOptionPane.showMessageDialog(this,"Select an event first.");
        });

        // Delete Event
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row >= 0) {
                int eventId = (int) model.getValueAt(row,0);
                int confirm = JOptionPane.showConfirmDialog(this,
                    "Delete EventID " + eventId + "?",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION);
                if(confirm == JOptionPane.YES_OPTION){
                    if(EventDAO.delete(eventId)) { JOptionPane.showMessageDialog(this,"Deleted."); btnRefresh.doClick(); }
                    else JOptionPane.showMessageDialog(this,"Delete failed.");
                }
            } else JOptionPane.showMessageDialog(this,"Select an event first.");
        });

        btnRefresh.doClick();
        return panel;
    }

    // ================= USERS TAB =================
    private JPanel createUsersPanel() {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"UserID","Username","Email","FullName","Role"},0){
            @Override public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add User");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(btnPanel, java.awt.BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            List<User> users = UserDAO.getAll();
            for(User u : users){
                model.addRow(new Object[]{u.getUserID(), u.getUsername(), u.getEmail(), u.getFullName(), u.getRole()});
            }
        });

        btnAdd.addActionListener(e -> {
            JTextField txtUser = new JTextField();
            JTextField txtEmail = new JTextField();
            JTextField txtFull = new JTextField();
            JTextField txtRole = new JTextField();
            JPasswordField txtPass = new JPasswordField();
            Object[] msg = {"Username:",txtUser,"Email:",txtEmail,"Full Name:",txtFull,"Role:",txtRole,"Password:",txtPass};
            if(JOptionPane.showConfirmDialog(this,msg,"Add User",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                if(UserDAO.register(txtUser.getText(), new String(txtPass.getPassword()), txtEmail.getText(), txtFull.getText())){
                    JOptionPane.showMessageDialog(this,"User added.");
                    btnRefresh.doClick();
                } else JOptionPane.showMessageDialog(this,"Add failed.");
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int userId = (int) model.getValueAt(row,0);
                User u = UserDAO.findById(userId);
                if(u!=null){
                    JTextField txtEmail = new JTextField(u.getEmail());
                    JTextField txtFull = new JTextField(u.getFullName());
                    JTextField txtRole = new JTextField(u.getRole());
                    Object[] msg = {"Email:",txtEmail,"Full Name:",txtFull,"Role:",txtRole};
                    if(JOptionPane.showConfirmDialog(this,msg,"Edit User",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                        u.setEmail(txtEmail.getText());
                        u.setFullName(txtFull.getText());
                        u.setRole(txtRole.getText());
                        if(UserDAO.update(u)){ JOptionPane.showMessageDialog(this,"Updated."); btnRefresh.doClick(); }
                        else JOptionPane.showMessageDialog(this,"Update failed.");
                    }
                }
            } else JOptionPane.showMessageDialog(this,"Select a user first.");
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int userId = (int) model.getValueAt(row,0);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete user ID " + userId + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );
                if(confirm==JOptionPane.YES_OPTION){
                    if(UserDAO.delete(userId)){ JOptionPane.showMessageDialog(this,"Deleted."); btnRefresh.doClick(); }
                    else JOptionPane.showMessageDialog(this,"Delete failed.");
                }
            } else JOptionPane.showMessageDialog(this,"Select a user first.");
        });

        btnRefresh.doClick();
        return panel;
    }

    // ================= VENUES TAB =================
    private JPanel createVenuesPanel() {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"VenueID","Name","Address","Capacity","Manager","Contact"},0){
            @Override public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Venue");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(btnPanel, java.awt.BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Venue> list = VenueDAO.getAll();
            for(Venue v : list){
                model.addRow(new Object[]{v.getVenueID(),v.getName(),v.getAddress(),v.getCapacity(),v.getManager(),v.getContact()});
            }
        });

        btnAdd.addActionListener(e -> {
            JTextField name = new JTextField();
            JTextField addr = new JTextField();
            JTextField cap = new JTextField();
            JTextField man = new JTextField();
            JTextField cont = new JTextField();
            Object[] msg = {"Name:",name,"Address:",addr,"Capacity:",cap,"Manager:",man,"Contact:",cont};
            if(JOptionPane.showConfirmDialog(this,msg,"Add Venue",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                Venue v = new Venue();
                v.setName(name.getText()); v.setAddress(addr.getText());
                v.setCapacity(Integer.parseInt(cap.getText())); v.setManager(man.getText()); v.setContact(cont.getText());
                if(VenueDAO.add(v)){ JOptionPane.showMessageDialog(this,"Added."); btnRefresh.doClick(); }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int) model.getValueAt(row,0);
                Venue v = VenueDAO.findById(id);
                if(v!=null){
                    JTextField name = new JTextField(v.getName());
                    JTextField addr = new JTextField(v.getAddress());
                    JTextField cap = new JTextField(""+v.getCapacity());
                    JTextField man = new JTextField(v.getManager());
                    JTextField cont = new JTextField(v.getContact());
                    Object[] msg = {"Name:",name,"Address:",addr,"Capacity:",cap,"Manager:",man,"Contact:",cont};
                    if(JOptionPane.showConfirmDialog(this,msg,"Edit Venue",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                        v.setName(name.getText()); v.setAddress(addr.getText());
                        v.setCapacity(Integer.parseInt(cap.getText())); v.setManager(man.getText()); v.setContact(cont.getText());
                        if(VenueDAO.update(v)){ JOptionPane.showMessageDialog(this,"Updated."); btnRefresh.doClick(); }
                    }
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int) model.getValueAt(row,0);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete venue ID " + id + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );
                if(confirm==JOptionPane.YES_OPTION){
                    if(VenueDAO.delete(id)){ JOptionPane.showMessageDialog(this,"Deleted."); btnRefresh.doClick(); }
                }
            }
        });

        btnRefresh.doClick();
        return panel;
    }

    // ================= SPONSORS TAB =================
    private JPanel createSponsorsPanel() {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"SponsorID","Attribute1","Attribute2","Attribute3","CreatedAt"},0){
            @Override public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Sponsor");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(btnPanel, java.awt.BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Sponsor> list = SponsorDAO.getAll();
            for(Sponsor s:list){
                Date d = s.getCreatedAt()==null?null: java.sql.Timestamp.valueOf(s.getCreatedAt());
                String ds = d==null?"":dateFormat.format(d);
                model.addRow(new Object[]{s.getSponsorID(),s.getAttribute1(),s.getAttribute2(),s.getAttribute3(),ds});
            }
        });

        btnAdd.addActionListener(e -> {
            JTextField a1 = new JTextField(); JTextField a2 = new JTextField(); JTextField a3 = new JTextField();
            Object[] msg = {"Attribute1:",a1,"Attribute2:",a2,"Attribute3:",a3};
            if(JOptionPane.showConfirmDialog(this,msg,"Add Sponsor",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                Sponsor s = new Sponsor();
                s.setAttribute1(a1.getText()); s.setAttribute2(a2.getText()); s.setAttribute3(a3.getText());
                s.setCreatedAt(LocalDateTime.now());
                if(SponsorDAO.add(s)){ JOptionPane.showMessageDialog(this,"Added."); btnRefresh.doClick(); }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int) model.getValueAt(row,0);
                Sponsor s = SponsorDAO.findById(id);
                if(s!=null){
                    JTextField a1 = new JTextField(s.getAttribute1());
                    JTextField a2 = new JTextField(s.getAttribute2());
                    JTextField a3 = new JTextField(s.getAttribute3());
                    Object[] msg = {"Attribute1:",a1,"Attribute2:",a2,"Attribute3:",a3};
                    if(JOptionPane.showConfirmDialog(this,msg,"Edit Sponsor",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                        s.setAttribute1(a1.getText()); s.setAttribute2(a2.getText()); s.setAttribute3(a3.getText());
                        s.setCreatedAt(LocalDateTime.now());
                        if(SponsorDAO.update(s)){ JOptionPane.showMessageDialog(this,"Updated."); btnRefresh.doClick(); }
                    }
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int) model.getValueAt(row,0);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete sponsor ID " + id + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );
                if(confirm==JOptionPane.YES_OPTION){
                    if(SponsorDAO.delete(id)){ JOptionPane.showMessageDialog(this,"Deleted."); btnRefresh.doClick(); }
                }
            }
        });

        btnRefresh.doClick();
        return panel;
    }

    // ================= REVIEWS TAB =================
    private JPanel createReviewsPanel() {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"ReviewID","Attr1","Attr2","Attr3","CreatedAt","UserID","SponsorID"},0){
            @Override public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Review");
        JButton btnEdit = new JButton("Edit Selected");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd); btnPanel.add(btnEdit); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(btnPanel, java.awt.BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            List<Review> list = ReviewDAO.getAll();
            for(Review r:list){
                Date d = r.getCreatedAt()==null?null:java.sql.Timestamp.valueOf(r.getCreatedAt());
                String ds = d==null?"":dateFormat.format(d);
                model.addRow(new Object[]{r.getReviewID(),r.getAttribute1(),r.getAttribute2(),r.getAttribute3(),ds,r.getUserID(),r.getSponsorID()});
            }
        });

        btnAdd.addActionListener(e -> {
            JTextField a1 = new JTextField(); JTextField a2 = new JTextField(); JTextField a3 = new JTextField();
            JTextField uid = new JTextField(); JTextField sid = new JTextField();
            Object[] msg = {"Attr1:",a1,"Attr2:",a2,"Attr3:",a3,"UserID:",uid,"SponsorID:",sid};
            if(JOptionPane.showConfirmDialog(this,msg,"Add Review",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                Review r = new Review();
                r.setAttribute1(a1.getText()); r.setAttribute2(a2.getText()); r.setAttribute3(a3.getText());
                r.setUserID(Integer.parseInt(uid.getText())); r.setSponsorID(Integer.parseInt(sid.getText()));
                r.setCreatedAt(LocalDateTime.now());
                if(ReviewDAO.add(r)){ JOptionPane.showMessageDialog(this,"Added."); btnRefresh.doClick(); }
            }
        });

        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int) model.getValueAt(row,0);
                Review r = ReviewDAO.findById(id);
                if(r!=null){
                    JTextField a1 = new JTextField(r.getAttribute1()); JTextField a2 = new JTextField(r.getAttribute2());
                    JTextField a3 = new JTextField(r.getAttribute3()); JTextField uid = new JTextField(""+r.getUserID());
                    JTextField sid = new JTextField(""+r.getSponsorID());
                    Object[] msg = {"Attr1:",a1,"Attr2:",a2,"Attr3:",a3,"UserID:",uid,"SponsorID:",sid};
                    if(JOptionPane.showConfirmDialog(this,msg,"Edit Review",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                        r.setAttribute1(a1.getText()); r.setAttribute2(a2.getText()); r.setAttribute3(a3.getText());
                        r.setUserID(Integer.parseInt(uid.getText())); r.setSponsorID(Integer.parseInt(sid.getText()));
                        r.setCreatedAt(LocalDateTime.now());
                        if(ReviewDAO.update(r)){ JOptionPane.showMessageDialog(this,"Updated."); btnRefresh.doClick(); }
                    }
                }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if(row>=0){
                int id = (int) model.getValueAt(row,0);
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete review ID " + id + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );
                if(confirm==JOptionPane.YES_OPTION){
                    if(ReviewDAO.delete(id)){ JOptionPane.showMessageDialog(this,"Deleted."); btnRefresh.doClick(); }
                }
            }
        });

        btnRefresh.doClick();
        return panel;
    }

    // ================= VENUE-SPONSOR TAB =================
    private JPanel createVenueSponsorPanel() {
        JPanel panel = new JPanel(new java.awt.BorderLayout());
        DefaultTableModel model = new DefaultTableModel(new Object[]{"VenueID","SponsorID"},0){
            @Override public boolean isCellEditable(int row,int col){ return false; }
        };
        JTable table = new JTable(model);
        panel.add(new JScrollPane(table), java.awt.BorderLayout.CENTER);

        JPanel btnPanel = new JPanel();
        JButton btnAdd = new JButton("Add Relation");
        JButton btnDelete = new JButton("Delete Selected");
        JButton btnRefresh = new JButton("Refresh");
        btnPanel.add(btnAdd); btnPanel.add(btnDelete); btnPanel.add(btnRefresh);
        panel.add(btnPanel, java.awt.BorderLayout.SOUTH);

        btnRefresh.addActionListener(e -> {
            model.setRowCount(0);
            List<VenueSponsor> list = VenueSponsorDAO.getAll();
            for(VenueSponsor vs:list){
                model.addRow(new Object[]{vs.getVenueID(),vs.getSponsorID()});
            }
        });

        btnAdd.addActionListener(e -> {
            JTextField vid = new JTextField(); JTextField sid = new JTextField();
            Object[] msg = {"VenueID:",vid,"SponsorID:",sid};
            if(JOptionPane.showConfirmDialog(this,msg,"Add Relation",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                VenueSponsor vs = new VenueSponsor();
                vs.setVenueID(Integer.parseInt(vid.getText()));
                vs.setSponsorID(Integer.parseInt(sid.getText()));
                if(VenueSponsorDAO.add(vs)){ JOptionPane.showMessageDialog(this,"Added."); btnRefresh.doClick(); }
            }
        });

        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();

            if (row >= 0) {
                int vid = (int) model.getValueAt(row, 0);
                int sid = (int) model.getValueAt(row, 1);

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Delete relation VenueID " + vid + " SponsorID " + sid + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = VenueSponsorDAO.delete(vid, sid);
                    if (deleted) {
                        JOptionPane.showMessageDialog(this, "Deleted successfully.");
                        btnRefresh.doClick();
                    } else {
                        JOptionPane.showMessageDialog(this, "Deletion failed!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnRefresh.doClick();
        return panel;
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new DashboardUI("Admin").setVisible(true));
    }
}