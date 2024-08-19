package Classes;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

import Helper.CustomDialogResult;
import Views.RoundedButton;

public class BlackJack {

    private class Card {
        String value, type;

        Card(String value, String type) {
            this.type = type;
            this.value = value;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) { // AJQK
                return value.equals("A") ? 11 : 10;
            }
            return Integer.parseInt(value); // 2-10
        }

        public boolean isAce() {
            return value.equals("A");
        }

        public String getImagePath() {
            return "/Cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random();

    // Dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    // Player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // Window
    int boardWidth = 600;
    int boardHeight = boardWidth;

    int cardWidth = 110; // ratio should be 1/1.4
    int cardHeight = 154;

    JFrame jFrame = new JFrame("Black Jack");
    
    JPanel mJPanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                // Draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("/Cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                // Draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5) * i, 20, cardWidth, cardHeight, null);
                }

                // Draw player's hand
                for (int i = 0; i < playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5) * i, 320, cardWidth, cardHeight, null);
                }

                // Check if game is over
                if (!stayButton.isEnabled()) {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    

                    String message = getResultMessage();
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);

                    // Schedule the post-game handling
                    Timer timer = new Timer(1500, e -> handlePostGame());
                    timer.setRepeats(false);
                    timer.start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private String getResultMessage() {
            if (playerSum > 21) {
                return "You Lose!";
            } else if (dealerSum > 21) {
                return "You Win!";
            } else if (playerSum == dealerSum) {
                return "Tie!";
            } else if (playerSum > dealerSum) {
                return "You Win!";
            } else {
                return "You Lose!";
            }
        }

        private void handlePostGame() {
           // int choice = JOptionPane.showConfirmDialog(this, "Do you want to play again?", "Game Over", JOptionPane.YES_NO_OPTION);
           int choice = showCustomConfirmDialog(jFrame, "Do you want to play again?", "Game Over");
            if (choice == JOptionPane.YES_OPTION) {
                // Reset game state and re-render the panel
                startGame();
                mJPanel.repaint(); // Repaint to show the updated game state
                stayButton.setEnabled(true); // Enable the Stay button for new game
                hiButton.setEnabled(true); // Enable the Hit button for new game
            } else if (choice == JOptionPane.NO_OPTION) {
                System.exit(0); // Exit the application
            }
        }

        private int showCustomConfirmDialog(JFrame jFrame, String message, String title) {
        // Create a custom dialog
        JDialog dialog = new JDialog(jFrame, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(jFrame);
    
        // Add message label
        JLabel messageLabel = new JLabel(message, SwingConstants.CENTER);
        dialog.add(messageLabel, BorderLayout.CENTER);
    
        // Create panel for buttons
        JPanel buttonPanel = new JPanel();
        RoundedButton yesButton = new RoundedButton("Yes",Color.decode("#B7ADFF"), Color.decode("#c690ff"), 20);
        RoundedButton noButton = new RoundedButton("No", Color.decode("#B7ADFF"), Color.decode("#c690ff"), 20);
        
        // set the cursor to change when button is in focus (when clicking the button or hovering over it)
        yesButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // Add action listeners
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                // Store result as YES_OPTION
                CustomDialogResult.setResult(JOptionPane.YES_OPTION);
            }
        });

         // set the cursor to change when button is in focus (when clicking the button or hovering over it)
        noButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
                // Store result as NO_OPTION
                CustomDialogResult.setResult(JOptionPane.NO_OPTION);
            }
        });
    
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
    
        // Show dialog
        dialog.setVisible(true);
    
        // Return the result
        return CustomDialogResult.getResult();
        }
    };

    JPanel button1 = new JPanel();

    RoundedButton hiButton = new RoundedButton("Hit",Color.decode("#9e5dff"), Color.decode("#6c169c"), 20);
    RoundedButton stayButton = new RoundedButton("Stay", Color.decode("#c690ff"), Color.decode("#6c169c"), 20);
    
 
    public BlackJack() {
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/Assets/cards.png"));
        startGame();
        jFrame.setIconImage(imageIcon.getImage());
        jFrame.setVisible(true);
        jFrame.setSize(boardWidth, boardHeight);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mJPanel.setLayout(new BorderLayout());
        mJPanel.setBackground(Color.decode("#B7ADFF"));
        jFrame.add(mJPanel);


        hiButton.setFont(new Font("Arial", Font.BOLD, 20)); // Example of setting custom font size and style
        hiButton.setForeground(Color.WHITE); // Example of setting text color
        button1.add(hiButton);
        stayButton.setFont(new Font("Arial", Font.BOLD, 20)); // Example of setting custom font size and style
        stayButton.setForeground(Color.WHITE); // Example of setting text color
        button1.add(stayButton);

        jFrame.add(button1, BorderLayout.SOUTH);


         // set the cursor to change when button is in focus (when clicking the button or hovering over it)
        hiButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hiButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size() - 1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) { // A + 2 + J --> 1 + 2 + J
                    hiButton.setEnabled(false);
                }
                mJPanel.repaint();
            }
        });


        // set the cursor to change when button is in focus (when clicking the button or hovering over it)
        stayButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hiButton.setEnabled(false);
                stayButton.setEnabled(false);

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                mJPanel.repaint();
            }
        });

        mJPanel.repaint();
    }

    private void startGame() {
        
        buildDeck();
        shuffleDeck();

        // Dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size() - 1);
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size() - 1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);


        // Player
        playerHand = new ArrayList<Card>();
        playerSum = 0;
        playerAceCount = 0;

        for (int i = 0; i < 2; i++) {
            card = deck.remove(deck.size() - 1);
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

    }

    private void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (String type : types) {
            for (String value : values) {
                Card card = new Card(value, type);
                deck.add(card);
            }
        }
        System.out.println(deck);
    }

    private void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currentCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currentCard);
        }
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount --;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount --;
        }
        return dealerSum;
    }
}
