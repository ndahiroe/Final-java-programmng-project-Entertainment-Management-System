module EntertainmentManagementSystem {
    requires java.sql;
    requires java.desktop;

    opens ui to java.desktop; // for Swing reflection
    exports ui;
    exports db;
    exports dao;
    exports model;
    exports utils;
}
