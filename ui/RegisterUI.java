package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class RegisterUI extends JFrame {
    private JTextField txtUsername, txtEmail, txtFullName;
    private JPasswordField txtPassword;
    private JButton btnRegister, btnBack;

    public RegisterUI() {
        setTitle("EMS - Register");
        setSize(420, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);
        txtEmail = new JTextField(20);
        txtFullName = new JTextField(20);

        btnRegister = new JButton("Register");
        btnBack = new JButton("Back to Login");

        c.insets = new Insets(6,6,6,6);
        c.gridx = 0; c.gridy = 0; panel.add(new JLabel("Username:"), c);
        c.gridx = 1; c.gridy = 0; panel.add(txtUsername, c);

        c.gridx = 0; c.gridy = 1; panel.add(new JLabel("Password:"), c);
        c.gridx = 1; c.gridy = 1; panel.add(txtPassword, c);

        c.gridx = 0; c.gridy = 2; panel.add(new JLabel("Email:"), c);
        c.gridx = 1; c.gridy = 2; panel.add(txtEmail, c);

        c.gridx = 0; c.gridy = 3; panel.add(new JLabel("Full Name:"), c);
        c.gridx = 1; c.gridy = 3; panel.add(txtFullName, c);

        c.gridx = 0; c.gridy = 4; panel.add(btnRegister, c);
        c.gridx = 1; c.gridy = 4; panel.add(btnBack, c);

        add(panel);

        btnRegister.addActionListener(e -> register());
        btnBack.addActionListener(e -> {
            new LoginUI().setVisible(true);
            this.dispose();
        });
    }

    private void register() {
        String u = txtUsername.getText().trim();
        String p = new String(txtPassword.getPassword());
        String email = txtEmail.getText().trim();
        String full = txtFullName.getText().trim();

        if (u.isEmpty() || p.isEmpty() || email.isEmpty() || full.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please complete all fields.");
            return;
        }

        boolean ok = UserDAO.register(u, p, email, full);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Registration successful. You can login now.");
            new LoginUI().setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Registration failed. Try different username/email.");
        }
    }
}
