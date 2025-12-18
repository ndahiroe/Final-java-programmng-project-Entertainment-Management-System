package ui;

import javax.swing.*;

public class MainLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}
