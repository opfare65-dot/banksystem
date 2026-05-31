import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

/**
 * About screen with project and team details.
 */
public class AboutUsPanel extends JPanel {
    // Reference to the main app controller used for screen navigation.
    private final BankManagementSystem bankingApp;
    // Bottom button used to return to login screen.
    private JButton backToLoginButton;

    // Creates About panel with a link to main app controller.
    public AboutUsPanel(BankManagementSystem app) {
        // Store parent app reference.
        this.bankingApp = app;
        // Build all labels and layout regions.
        buildLayout();
        // Attach button listeners.
        setupListeners();
    }

    // Builds the visual layout and static content.
    private void buildLayout() {
        // Use border layout for top-level arrangement.
        setLayout(new BorderLayout(10, 10));
        // Add padding around panel edges.
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        // Apply light-blue background.
        setBackground(new Color(240, 248, 255));

        // Main content column container.
        JPanel contentPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        // Keep content panel background transparent.
        contentPanel.setOpaque(false);

        // Large title label.
        JLabel titleLabel = new JLabel("KAAFI BANK MANAGEMENT SYSTEM");
        // Use bold large font for emphasis.
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        // Center title text horizontally.
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // Use dark navy text color.
        titleLabel.setForeground(new Color(25, 25, 112));

        // Subtitle label for project context.
        JLabel subtitleLabel = new JLabel("Group-4 Bank Management");
        // Use italic medium font style.
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        // Center subtitle text horizontally.
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Visual separator above member list.
        JSeparator topSeparator = new JSeparator();
        // Visual separator below member list.
        JSeparator bottomSeparator = new JSeparator();

        // Label heading for team member section.
        JLabel membersTitleLabel = new JLabel("Group Members:");
        // Make section heading bold.
        membersTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Multiline area holding static member names.
        JTextArea membersListArea = new JTextArea(5, 30);
        // Keep list read-only.
        membersListArea.setEditable(false);
        // Use monospaced font to align IDs.
        membersListArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        // Fill member names and IDs.
        membersListArea.setText(
            "  1. Abdiwasa Mahammed    - 1701996\n"
                + "  2. Abdi Osman           - 1701992\n"
                + "  3. Farhan Hassan        - 1702097\n"
                + "  4. Abdukerim Redwan     - 1701997\n"
                + "  5. Abdurehman Alemar    - 1702298"
        );
        // Blend area into parent panel background.
        membersListArea.setOpaque(false);

        // University info label.
        JLabel universityLabel = new JLabel("University: Dire Dawa University");
        // Course info label.
        JLabel courseLabel = new JLabel("Course: Object-Oriented Programming");
        

        // Create return button.
        backToLoginButton = new JButton("Back to Login");

        // Add title to content panel.
        contentPanel.add(titleLabel);
        // Add subtitle to content panel.
        contentPanel.add(subtitleLabel);
        // Add top separator line.
        contentPanel.add(topSeparator);
        // Add section title label.
        contentPanel.add(membersTitleLabel);
        // Add member list text area.
        contentPanel.add(membersListArea);
        // Add bottom separator line.
        contentPanel.add(bottomSeparator);
        // Add university line.
        contentPanel.add(universityLabel);
        // Add course line.
        contentPanel.add(courseLabel);
        // Add instructor line.
        contentPanel.add(instructorLabel);

        // Place content in center region.
        add(contentPanel, BorderLayout.CENTER);
        // Place back button in bottom region.
        add(backToLoginButton, BorderLayout.SOUTH);
    }

    // Attaches click events for panel controls.
    private void setupListeners() {
        // Navigate back to login when button is clicked.
        backToLoginButton.addActionListener(event -> bankingApp.showPanel("LOGIN"));
    }
}
