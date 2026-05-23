import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * AboutUsPanel displays project information.
 * Shows team members, project title, and university information.
 */
public class AboutUsPanel extends JPanel {
    private BankManagementSystem app;
    
    public AboutUsPanel(BankManagementSystem app) {
        this.app = app;
        setupLayout();
        setupListeners();
    }

    /**
     * Sets up the panel layout.
     */
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        setBackground(new Color(240, 248, 255));
        
        JPanel centerPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        centerPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("KAAFI BANK MANAGEMENT SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(new Color(25, 25, 112));
        
        JLabel subtitleLabel = new JLabel("Group-4 Bank Management");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JSeparator separator1 = new JSeparator();
        JSeparator separator2 = new JSeparator();
        
        JLabel teamLabel = new JLabel("Group Members:");
        teamLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JTextArea teamMembersArea = new JTextArea(5, 30);
        teamMembersArea.setEditable(false);
        teamMembersArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
teamMembersArea.setText("  1. Abdiwasa mahammed    - 1701996\n" +
                                "  2. Abdi osman           - 1701992\n" +
                                "  3. Farhan hassan        - 1702097\n" +
                                "  4. Abdukerim Redwan     - 1701997\n" +
                                "  5. Abdukerim Alemar     - DDU1702298");
        teamMembersArea.setOpaque(false);
        
        JLabel universityLabel = new JLabel("University: Dire Dawa University");
        JLabel courseLabel = new JLabel("Course: Object-Oriented Programming (CS201)");
        JLabel instructorLabel = new JLabel("Instructor:");
        
        JButton backButton = new JButton("Back to Login");
        
        centerPanel.add(titleLabel);
        centerPanel.add(subtitleLabel);
        centerPanel.add(separator1);
        centerPanel.add(teamLabel);
        centerPanel.add(teamMembersArea);
        centerPanel.add(separator2);
        centerPanel.add(universityLabel);
        centerPanel.add(courseLabel);
        centerPanel.add(instructorLabel);
        
        add(centerPanel, BorderLayout.CENTER);
        add(backButton, BorderLayout.SOUTH);
    }

    /**
     * Sets up event listeners.
     */
    private void setupListeners() {
        for (Component comp : getComponents()) {
            if (comp instanceof JButton) {
                ((JButton) comp).addActionListener(e -> app.showPanel("LOGIN"));
            }
        }
    }
}