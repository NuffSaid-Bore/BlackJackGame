package Views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class RoundedButton extends JButton {
    private Color backgroundColor;
    private Color borderColor;
    private int borderRadius;

    public RoundedButton(String text, Color backgroundColor, Color borderColor, int borderRadius) {
        super(text);
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderRadius = borderRadius;

        setContentAreaFilled(false); // Avoid default filling
        setFocusPainted(false); // Avoid focus painting
        setOpaque(false); // Allow custom painting
        setBorderPainted(false); // Disable default border painting
        setPreferredSize(new Dimension(90, 35)); // Custom size
        setBackground(backgroundColor);
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Paint button background
            g2d.setColor(backgroundColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), borderRadius, borderRadius);

            // Paint button border
            g2d.setColor(borderColor);
            g2d.setStroke(new BasicStroke((float) 1.5)); // Adjust the border thickness as needed
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, borderRadius, borderRadius);

            // Paint button text
            super.paintComponent(g2d);
        } finally {
            g2d.dispose();
        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0); // No extra space
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(90, 35); // Adjust size as needed
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
        repaint(); // Repaint the button to show the updated border color
    }
    
}