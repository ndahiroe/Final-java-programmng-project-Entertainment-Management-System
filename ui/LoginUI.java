package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    public LoginUI() {
        setTitle("EMS - Login");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        init();
    }

    private void init() {
        // Gradient background panel
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color1 = new Color(72, 209, 204); // turquoise
                Color color2 = new Color(32, 178, 170); // dark turquoise
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        add(backgroundPanel);

        // Rounded login panel with shadow
        JPanel loginPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setPreferredSize(new Dimension(350, 250));
        loginPanel.setLayout(new GridBagLayout());
        loginPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel lblTitle = new JLabel("EMS Login");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(32, 178, 170));
        c.gridx = 0; c.gridy = 0; c.gridwidth = 2; c.anchor = GridBagConstraints.CENTER;
        loginPanel.add(lblTitle, c);

        // Username
        JLabel lblUser = new JLabel("Username:");
        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 30));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 178, 170), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        c.gridwidth = 1; c.gridx = 0; c.gridy = 1; c.anchor = GridBagConstraints.WEST;
        loginPanel.add(lblUser, c);
        c.gridx = 1; c.gridy = 1;
        loginPanel.add(txtUsername, c);

        // Password
        JLabel lblPass = new JLabel("Password:");
        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 30));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(32, 178, 170), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        c.gridx = 0; c.gridy = 2;
        loginPanel.add(lblPass, c);
        c.gridx = 1; c.gridy = 2;
        loginPanel.add(txtPassword, c);

        // Buttons
        btnLogin = new JButton("Login");
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(new Color(32, 178, 170));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(72, 209, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(32, 178, 170));
            }
        });

        btnRegister = new JButton("Register");
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setBackground(new Color(0, 128, 128));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRegister.setBackground(new Color(32, 178, 170));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRegister.setBackground(new Color(0, 128, 128));
            }
        });

        c.gridx = 0; c.gridy = 3;
        loginPanel.add(btnLogin, c);
        c.gridx = 1; c.gridy = 3;
        loginPanel.add(btnRegister, c);

        // Add login panel to background
        backgroundPanel.add(loginPanel);

        // Action listeners
        btnLogin.addActionListener((ActionEvent ae) -> doLogin());
        btnRegister.addActionListener((ActionEvent ae) -> {
            new RegisterUI().setVisible(true);
            this.dispose();
        });
    }

    private void doLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password.");
            return;
        }

        if (UserDAO.verifyCredentials(username, password)) {
            JOptionPane.showMessageDialog(this, "Login successful.");
            SwingUtilities.invokeLater(() -> {
                DashboardUI dash = new DashboardUI(username);
                dash.setVisible(true);
            });
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
