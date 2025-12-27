import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class NumberGuess extends JFrame {

    private int randomNumber;
    private int attemptsLeft;
    private int startRange;
    private int endRange;

    private JTextField guessField;
    private JLabel hintLabel;
    private JLabel attemptsLabel;
    private JLabel rangeLabel;

    private DefaultTableModel tableModel;

    public NumberGuess() {
        setTitle("Number Guessing Game");
        setSize(700, 450);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Number Guessing Game", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        topPanel.add(title);

        rangeLabel = new JLabel("Range: -", SwingConstants.CENTER);
        rangeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        topPanel.add(rangeLabel);

        add(topPanel, BorderLayout.NORTH);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridLayout(6, 1, 10, 10));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        hintLabel = new JLabel("Enter your guess:", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leftPanel.add(hintLabel);

        guessField = new JTextField();
        guessField.setHorizontalAlignment(JTextField.CENTER);
        guessField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        leftPanel.add(guessField);

        JButton guessButton = new RoundedButton("Submit Guess");
        leftPanel.add(guessButton);

        attemptsLabel = new JLabel("Attempts left: 0", SwingConstants.CENTER);
        attemptsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        leftPanel.add(attemptsLabel);

        JButton resetButton = new RoundedButton("Reset Game");
        leftPanel.add(resetButton);

        tableModel = new DefaultTableModel(new Object[]{"Guess", "Result"}, 0);
        JTable table = new JTable(tableModel);
        table.setRowHeight(22);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(350, 300));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, scrollPane);
        splitPane.setResizeWeight(0.35);
        splitPane.setDividerSize(4);
        add(splitPane, BorderLayout.CENTER);

        startNewGame();

        guessButton.addActionListener(e -> checkGuess());
        resetButton.addActionListener(e -> startNewGame());

        setVisible(true);
    }

    private void startNewGame() {
        try {
            startRange = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter start of range:"));
            endRange = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter end of range:"));

            if (endRange <= startRange) {
                JOptionPane.showMessageDialog(this, "Invalid range. Defaulting to 1 - 100.");
                startRange = 1;
                endRange = 100;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Defaulting to 1 - 100.");
            startRange = 1;
            endRange = 100;
        }

        int rangeSize = endRange - startRange + 1;
        attemptsLeft = (int)(Math.log(rangeSize) / Math.log(2)) + 2;

        randomNumber = new Random().nextInt(rangeSize) + startRange;

        hintLabel.setText("Guess (" + startRange + " - " + endRange + "):");
        attemptsLabel.setText("Attempts left: " + attemptsLeft);
        rangeLabel.setText("Range: " + startRange + " to " + endRange);

        guessField.setText("");
        tableModel.setRowCount(0);
    }

    private void checkGuess() {
        try {
            int guess = Integer.parseInt(guessField.getText());
            attemptsLeft--;

            if (guess == randomNumber) {
                tableModel.addRow(new Object[]{guess, "Correct"});
                JOptionPane.showMessageDialog(this, "Correct. The number was " + randomNumber);
                startNewGame();
                return;
            }
            else if (guess > randomNumber) {
                tableModel.addRow(new Object[]{guess, "Too High"});
                hintLabel.setText("Too high. Try again:");
            }
            else {
                tableModel.addRow(new Object[]{guess, "Too Low"});
                hintLabel.setText("Too low. Try again:");
            }

            attemptsLabel.setText("Attempts left: " + attemptsLeft);

            if (attemptsLeft == 0) {
                tableModel.addRow(new Object[]{"-", "Out of attempts"});
                JOptionPane.showMessageDialog(this, "Out of attempts. The number was " + randomNumber);
                startNewGame();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter a valid number.");
        }

        guessField.setText("");
    }

    class RoundedButton extends JButton {
        RoundedButton(String text) {
            super(text);
            setFocusPainted(false);
            setFont(new Font("Segoe UI", Font.BOLD, 15));
            setBackground(new Color(52, 152, 219));
            setForeground(Color.WHITE);
            setUI(new RoundedButtonUI());
        }
    }

    class RoundedButtonUI extends BasicButtonUI {
        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JButton b = (JButton) c;
            b.setOpaque(false);
            b.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        }

        @Override
        public void paint(Graphics g, JComponent c) {
            JButton b = (JButton) c;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(b.getBackground());
            g2.fillRoundRect(0, 0, b.getWidth(), b.getHeight(), 20, 20);

            g2.setColor(b.getForeground());
            FontMetrics fm = g2.getFontMetrics();
            int x = (b.getWidth() - fm.stringWidth(b.getText())) / 2;
            int y = (b.getHeight() + fm.getAscent()) / 2 - 3;
            g2.drawString(b.getText(), x, y);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        new NumberGuess();
    }
}
