import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.text.*;

/**
 * This class implements the CardGameTable interface. It is used to build a GUI 
 * for the Big Two card game and handle all user actions.
 * 
 * @author Ajayveer Singh
 */

public class BigTwoTable implements CardGameTable {
	/**
	 * A card game associated with this table.
	 */
	private CardGame game;
	
	/**
	 * A boolean array indicating which cards are being selected.
	 */
	private boolean[] selected;
	
	/**
	 * An integer specifying the index of the active player.
	 */
	private int activePlayer;
	
	/**
	 * The main window of the application.
	 */
	private JFrame frame;
	
	/**
	 * A panel for showing the cards of each player and the
	 * cards played on the table.
	 */
	private JPanel bigTwoPanel;
	
	/**
	 * Button for the active player to play the selected cards.
	 */
	private JButton playButton;
	
	/**
	 * Button for the active player to pass their turn to the next player.
	 */
	private JButton passButton;
	
	/**
	 * A text Area for showing the current game status as well as
	 * end of game messages.
	 */
	private JTextArea msgArea;
	
	/**
	 * A text Area for showing the chat.
	 */
	private JTextArea chatArea;
	
	/**
	 * A textField to type your chat message in.
	 */
	private JTextField chatBox;
	
	/**
	 * 2D array storing images for faces of cards.
	 */
	private Image[][] cardImages;
	
	/**
	 * Image for the backs of cards.
	 */
	private Image cardBackImage;
	
	/**
	 * Array storing images of Avatars.
	 */
	private Image[] avatars;
	
	/**
	 * Array storing boolean values specifying whether a player is present or not.
	 */
	private boolean[] present;
	
	/**
	 * Constructor for creating a BigTwoTable.
	 * 
	 * @param game CardGame associated with this table.
	 */
	public BigTwoTable(CardGame game) {
		this.game = game;
		avatars = new Image[4];
		cardImages = new Image[4][13];
		selected = new boolean[13];
		present = new boolean[4];
		
		setActivePlayer(game.getCurrentIdx());
		loadImages();
		makeGUI();
	}
	
	/**
	 * Loads the Avatar and card images into the arrays/image variables.
	 */
	private void loadImages() {
		// Fill avatars
		for(int i = 0; i < 4; i++) {
			avatars[i] = new ImageIcon("src/avatars/Avatar_" + i + ".png").getImage();
		}
		
		// Back of card Image
		cardBackImage = new ImageIcon("src/cards/b.gif").getImage();
		
		// Front of card Images
		String[] suits = {"d","c","h","s"};
		char[] ranks = {'a','2','3','4','5','6','7','8','9','t','j','q','k'};
		
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 13; j++) {
				cardImages[i][j] = new ImageIcon("src/cards/" + ranks[j] + suits[i] + ".gif").getImage();
			}
		}
	}
	
	/**
	 * Method to set the basic layout of the GUI.
	 */
	private void makeGUI() {
		// Make JFrame. Use GridLayout for equal width between cards, and message area
		frame = new JFrame("Big Two");
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1,2));
		
		// Adding a Menu Bar to frame
		JMenuBar menuBar = new JMenuBar();
		JMenu gameMenu = new JMenu("Game");
		JMenuItem restartMenuItem = new JMenuItem("Restart");
		JMenuItem quitMenuItem = new JMenuItem("Quit");
		JMenuItem connectMenuItem = new JMenuItem("Connect");
		
		restartMenuItem.addActionListener(new RestartMenuItemListener());
		quitMenuItem.addActionListener(new QuitMenuItemListener());
		connectMenuItem.addActionListener(new ConnectMenuItemListener());
		
		//gameMenu.add(restartMenuItem);
		gameMenu.add(quitMenuItem);
		gameMenu.add(connectMenuItem);
		menuBar.add(gameMenu);
		frame.setJMenuBar(menuBar);
		
		// Add cards panel to frame
		JPanel cardsAndButtons = new JPanel();
		cardsAndButtons.setLayout(new BorderLayout());
		frame.add(cardsAndButtons);
		
		// bigTwoPanel is for cards.
		bigTwoPanel = new BigTwoPanel(); // Change to BigTwoPanel
		cardsAndButtons.add(bigTwoPanel, BorderLayout.CENTER);
		
		// Creating panel where buttons will go
		JPanel buttonsPanel = new JPanel();
		cardsAndButtons.add(buttonsPanel, BorderLayout.SOUTH);
		
		// Adding play button to panel
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		playButton.setMargin(new Insets(0, 20, 0, 20));
		buttonsPanel.add(playButton);
		
		// Adding pass button to panel
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		passButton.setMargin(new Insets(0, 20, 0, 20));
		buttonsPanel.add(passButton);
		
		// Making panel where message and chat will go
		JPanel messageAndChat = new JPanel();
		messageAndChat.setLayout(new BorderLayout());
		
		// Inner panel
		JPanel innerMandC = new JPanel();
		innerMandC.setLayout(new GridLayout(2,1));
		
		// Add message area to panel
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		innerMandC.add(msgArea);
		
		// Adding scroll for message area
		JScrollPane scroll = new JScrollPane(msgArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		innerMandC.add(scroll);
		
		// Automatic scroll
		DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// Add chat area to panel
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		innerMandC.add(chatArea);
		
		// Adding scroll for message area
		JScrollPane scrollChat = new JScrollPane(chatArea,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		innerMandC.add(scrollChat);
		
		// Automatic scroll
		DefaultCaret caretChat = (DefaultCaret) chatArea.getCaret();
		caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		// Add inner to messageAndChat
		messageAndChat.add(innerMandC, BorderLayout.CENTER);
		
		// Input panel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BorderLayout());
		inputPanel.setBackground(new Color(50,50,50));
		
		// Input area
		chatBox = new EnterTextField(chatArea.getSize().width);
		inputPanel.add(chatBox);
		
		// Input Button
		JButton send = new JButton("Send");
		send.setPreferredSize(new Dimension(70,35));
		send.addActionListener(new SendMessageListener());
		inputPanel.add(send, BorderLayout.EAST);
		
		// Add input panel to messageAndChat
		messageAndChat.add(inputPanel, BorderLayout.SOUTH);
		
		// Add messageAndChat panel to frame
		frame.add(messageAndChat);
		frame.pack();
		
		frame.setExtendedState(frame.MAXIMIZED_BOTH);
		frame.setResizable(false);
		frame.setVisible(true);
	}
	
	/**
	 * Setter method for the instance variable activePlayer.
	 * 
	 * @param activePlayer (int) value specifying the index of the current player.
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}
	
	
	/**
	 * Getter method to get the array of indices of cards selected.
	 * 
	 * @return int[] specifying the indices of the selected cards.
	 */
	public int[] getSelected() {
		int size = 0;
		
		// Find number of cards selected (size)
		for(boolean cardSelected : selected) {
			if(cardSelected) size++;
		}
		
		if (size == 0) return null;
		
		int[] selectedCards = new int[size];
		int index = 0;
		
		for(int i = 0; i < selected.length; i++) {
			if(selected[i]) {
				selectedCards[index] = i;
				index++;
			}
		}
		
		return selectedCards;
	}
	
	/**
	 * Resets the list of selected cards. Makes all elements false.
	 */
	public void resetSelected() {
		Arrays.fill(this.selected, false);
	}
	
	/**
	 * Method to repaint the GUI.
	 */
	public void repaint() {
		frame.repaint();
	}
	
	/**
	 * Method to print a message to the message area of the GUI.
	 * 
	 * @param msg String specifying the message to be printed.
	 */
	public void printMsg(String msg) {
		msgArea.append(msg + "\n");
	}
	
	/**
	 * Method to print a message to the chat area.
	 * 
	 * @param msg String specifying the chat message by the player.
	 */
	public void chatMsg(String msg) {
		chatArea.append(msg + "\n");
	}
	
	/**
	 * Method to set the present value of a player with index i
	 * @param i Index of player whose present value is being altered
	 * @param p Present value of player.
	 */
	public void isPresent(int i, boolean p) {
		present[i] = p;
	}
	
	/**
	 * Method that clears the message area of the GUI.
	 */
	public void clearMsgArea() {
		msgArea.setText(null);
	}
	
	/**
	 * Method to reset the GUI.
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	/**
	 * Method enables user interaction.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}
	
	/**
	 * Method disables user interaction.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}
	
	/**
	 * Inner class that extends the JPanel class and implements the
	 * MouseListener interface.
	 * <p>
	 * Draws the card game table. Handles mouseclick events.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener{
		
		private int nameX = 10;
		private int nameY = 20;
		private int fontHeight = 5;
		private int cardWidth = 73;
		private int cardHeight = 97;
		private int gapBetweenPlayers = 5;
		private int gapBetweenCardsAndAvatar = 70;
		private int playerHeight = cardHeight + fontHeight + nameY + gapBetweenPlayers;
		private int raiseValue = 20;
		private int cardCoveredWidth = 20;
		
		/**
		 * Constructor for BigTwoPanel. Adds mouse event listener.
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		/**
		 * Overriding paintComponent method inherited from JPanel to
		 * draw the card game table.
		 * <p>
		 * Draws the Player names, Avatars, and cards.
		 * 
		 * @param g Graphics object that the system provides.
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			this.setBackground(new Color(0,100,0));
			
			Graphics2D g2d = (Graphics2D) g;
			// Green Background color
			this.setBackground(new Color(0,150,0));
			
			// Print name of Players and show avatar
			for (int i = 0; i < 4; i++) {
				if(!present[i]) continue;
				if (i == activePlayer) {
					g2d.setColor(Color.BLUE);
				} else {
					g2d.setColor(Color.WHITE);
				}
				g2d.drawString(game.getPlayerList().get(i).getName(), nameX, nameY + playerHeight * i);
				g2d.drawImage(avatars[i], nameX, (nameY + fontHeight) + playerHeight * i, this);
				
			}
			
			// Draw cards
			for(int i = 0; i < 4; i++) {
				// Draw the Line
				g2d.setColor(Color.WHITE);
				g2d.drawLine(0, playerHeight * (i+1), this.getWidth(), playerHeight * (i+1));
				if(!present[i]) g2d.drawString("Waiting for player...", nameX, nameY+playerHeight*i);
				try {
					CardGamePlayer currentPlayer = game.getPlayerList().get(i);
					int numOfCards = currentPlayer.getNumOfCards();
					for(int j = 0; j < numOfCards; j++) {
						
						int suit = currentPlayer.getCardsInHand().getCard(j).getSuit();
						int rank = currentPlayer.getCardsInHand().getCard(j).getRank();
						
						if(i == activePlayer) {
							int cardY = nameY + fontHeight + playerHeight * i;
							if(selected[j]) cardY -= raiseValue;
							g2d.drawImage(cardImages[suit][rank], nameX + gapBetweenCardsAndAvatar + cardWidth + cardCoveredWidth * j, cardY, this);
						} else {
							g2d.drawImage(cardBackImage, nameX + gapBetweenCardsAndAvatar + cardWidth + cardCoveredWidth * j, nameY + fontHeight + playerHeight * i, this);
						}
					}
				} catch (Exception e) {
					// do nothing
				}
				
			}
			
			if (game.endOfGame()) {
				boolean didGameEndNicely = ((BigTwoClient)game).getGameEndedNicely();
				if(didGameEndNicely) {
					ArrayList<CardGamePlayer> playerListEOG = game.getPlayerList();
					int winner = -1;
					for(int i = 0; i < playerListEOG.size(); i++) {
						if(playerListEOG.get(i).getNumOfCards() == 0) winner = i;
					}
					g2d.setColor(Color.WHITE);
					g2d.drawString(playerListEOG.get(winner).getName() + " has won the Game!", nameX, nameY + playerHeight * 4);
					
					g2d.drawImage(avatars[winner], nameX, nameY + fontHeight + playerHeight * 4 + 5, this);
					int count = 1;
					for(int i = 0; i < playerListEOG.size(); i++) {
						if(i != winner) {
							g2d.drawString(playerListEOG.get(i).getName() + " has " + playerListEOG.get(i).getNumOfCards() + " cards.", nameX + cardWidth + gapBetweenCardsAndAvatar + cardCoveredWidth * 5, nameY + playerHeight * 4 + 30 * count);
							count++;
						}
					}
				} else {
					g2d.drawString("The game ended unexpectedly. Here's a sword: cxx(============>", nameX, nameY + playerHeight * 4);
				}
				
			} else {
				if(game != null && game.getHandsOnTable().isEmpty()) {
					g2d.setColor(Color.WHITE);
					g2d.drawString("No hands have been played.", nameX, nameY + playerHeight * 4);
				} else {
					g2d.setColor(Color.WHITE);
					Hand lastHand = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
					String lastHandPlayerName = lastHand.getPlayer().getName();
					g2d.drawString("Played by " + lastHandPlayerName, nameX, nameY + playerHeight * 4);
					for(int i = 0; i < lastHand.size(); i++) {
						Card currentCard = lastHand.getCard(i);
						int suit = currentCard.getSuit();
						int rank = currentCard.getRank();
						g2d.drawImage(cardImages[suit][rank], nameX + cardCoveredWidth * i, nameY + fontHeight + playerHeight * 4 + 5, this);
					}
				}
			}
			repaint();
		}
		
		/**
		 * Method to get the index of the card clicked (if any) for the current player.
		 * @param x X-Coordinate of the mouse click.
		 * @param y Y-Coordinate of the mouse click.
		 * @return (int) Index of card clicked. -1 if no card clicked.
		 */
		private int cardClicked(int x, int y) {
			int numOfCards = game.getPlayerList().get(activePlayer).getNumOfCards();
			
			for(int i = numOfCards - 1; i >= 0; i--) {
				int cardXStart = nameX + gapBetweenCardsAndAvatar + cardWidth + cardCoveredWidth * i;
				int cardXEnd = cardXStart + cardWidth;
				
				int cardYStart = nameY + fontHeight + playerHeight * activePlayer;
				if(selected[i]) cardYStart -= raiseValue;
				
				int cardYEnd = cardYStart + cardHeight;
				
				if(x >= cardXStart && x <= cardXEnd && y >= cardYStart && y <= cardYEnd) {
					return i;
				}
			}
			return -1;
		}
		
		/**
		 * Method handles all mouse click events.
		 * <p>
		 * Overrides the mouseClicked method of JPanel.
		 * 
		 * @param e MouseEvent object used to get information about the mouse click.
		 */
		public void mouseClicked(MouseEvent e) {
			int indexCardClicked = cardClicked(e.getX(), e.getY());
			if(indexCardClicked != -1) {
				if(selected[indexCardClicked]) selected[indexCardClicked] = false;
				else selected[indexCardClicked] = true;
			}
			repaint();
		}
		
		/**
		 * Not used/Implemented.
		 * 
		 * @param e MouseEvent object. Has information about mouseclick.
		 */
		public void mouseExited(MouseEvent e) {}
		
		/**
		 * Not used/Implemented.
		 * 
		 * @param e MouseEvent object. Has information about mouseclick.
		 */
		public void mouseReleased(MouseEvent e) {}
		
		/**
		 * Not used/Implemented.
		 * 
		 * @param e MouseEvent object. Has information about mouseclick.
		 */
		public void mouseEntered(MouseEvent e) {}
		
		/**
		 * Not used/Implemented.
		 * 
		 * @param e MouseEvent object. Has information about mouseclick.
		 */
		public void mousePressed(MouseEvent e) {}
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * <p>
	 * Handles button-click events for "Play" Button.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class PlayButtonListener implements ActionListener {
		
		/**
		 * Overrides method from ActionListener Interface.
		 * <p>
		 * Used to make a move when "Play" button is clicked.
		 * 
		 * @param e ActionEvent object provided by system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			if(getSelected() == null) {
				printMsg("Illegal Move.");
			} else {
				game.makeMove(activePlayer, getSelected());
			}
			repaint();
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * <p>
	 * Handles button-click events for "Pass" Button.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class PassButtonListener implements ActionListener {
		
		/**
		 * Overrides method from ActionListener Interface.
		 * <p>
		 * Used to pass a move when "Pass" button is clicked.
		 * 
		 * @param e ActionEvent object provided by system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			resetSelected();
			game.makeMove(activePlayer, null);
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * <p>
	 * Handles button-click events for  "Send" Button.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class SendMessageListener implements ActionListener {
		
		/**
		 * Overrides method from ActionListener Interface.
		 * <p>
		 * Used to send a chat message when "Send" button is clicked.
		 * 
		 * @param e ActionListener object provided by system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			BigTwoClient client = (BigTwoClient) game;
			String inputValue = chatBox.getText();
			if(inputValue != null && !inputValue.trim().isEmpty()) {
				CardGameMessage message = new CardGameMessage(CardGameMessage.MSG, -1, inputValue);
				client.sendMessage(message);
			}
			chatBox.setText("");
			chatBox.requestFocus();
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * <p>
	 * Handles click events for "Restart" Menu Item.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class RestartMenuItemListener implements ActionListener {
		
		/**
		 * Method that restarts the game.
		 * 
		 * @param e ActionEvent object passed by system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			BigTwoDeck bigTwoDeck = new BigTwoDeck();
			bigTwoDeck.shuffle();
			reset();
			game.start(bigTwoDeck);
		}
	}
	
	/**
	 * An inner class that implements the ActionListener interface.
	 * <p>
	 * Handles click events for "Quit" Menu Item.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class QuitMenuItemListener implements ActionListener {
		
		/**
		 * Ends/Quits game.
		 * 
		 * @param e ActionEvent object passed by the system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
	
	/**
	 * Ann inner class taht implements the ActionListener interface.
	 * <p>
	 * Handles click events for "Connect" Menu Item.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class ConnectMenuItemListener implements ActionListener {
		/**
		 * Tries to connect to the game server (IP: 127.0.0.1, Port: 2396).
		 * @param e ActionEvent object passed by the system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			((BigTwoClient)game).makeConnection();
		}
	}
	
	/**
	 * An inner class that implements a JTextField with
	 * ActionListener to allow input by pressing Enter.
	 * 
	 * @author Ajayveer Singh
	 *
	 */
	class EnterTextField extends JTextField implements ActionListener {
		public EnterTextField(int len) {
			super(len);
			addActionListener(this);
		}
		
		/**
		 * Overrides method from ActionListener Interface.
		 * <p>
		 * Used to send a chat message when "Enter" key is clicked.
		 * 
		 * @param e ActionListener object provided by system. Not used.
		 */
		public void actionPerformed(ActionEvent e) {
			BigTwoClient client = (BigTwoClient) game;
			String inputValue = chatBox.getText();
			if(inputValue != null && !inputValue.trim().isEmpty()) {
				CardGameMessage message = new CardGameMessage(CardGameMessage.MSG, -1, inputValue);
				client.sendMessage(message);
			}
			chatBox.setText("");
			chatBox.requestFocus();
		}
	}
}
