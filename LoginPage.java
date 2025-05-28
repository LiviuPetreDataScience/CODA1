java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPage extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JLabel messageLabel;
    private UserManager userManager;

    public LoginPage() {
        super("Login Page");
        userManager = new UserManager();
        initializeUI();
    }

    private void initializeUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 220);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Username:");
        userLabel.setBounds(30, 30, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(120, 30, 180, 25);
        panel.add(usernameField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setBounds(30, 70, 80, 25);
        panel.add(passLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(120, 70, 180, 25);
        panel.add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(30, 120, 120, 30);
        panel.add(loginButton);

        registerButton = new JButton("Register");
        registerButton.setBounds(180, 120, 120, 30);
        panel.add(registerButton);

        messageLabel = new JLabel("");
        messageLabel.setBounds(30, 160, 270, 25);
        messageLabel.setForeground(Color.RED);
        panel.add(messageLabel);

        add(panel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleRegister();
            }
        });
    }

    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (userManager.authenticate(username, password)) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText("Login successful!");
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Invalid username or password.");
        }
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (userManager.registerUser(username, password)) {
            messageLabel.setForeground(new Color(0, 128, 0));
            messageLabel.setText("Registration successful! You can now log in.");
        } else {
            messageLabel.setForeground(Color.RED);
            messageLabel.setText("Registration failed. User may already exist or fields are empty.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage loginPage = new LoginPage();
            loginPage.setVisible(true);
        });
    }
}