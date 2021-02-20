import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class Board extends JComponent {
	private static final long serialVersionUID = 1L;

	private static int CELL_DIMENSION = 40;
	private static int SPEED = 125;
	private static int GROWTH_INCREMENT = 2;
	private static boolean RAISED_CELL = true;

	private static Color BOARD_COLOR = Color.CYAN;
	private static Color SNAKE_COLOR = Color.BLUE;
	private static Color FOOD_COLOR = Color.PINK;
	private static Color WALL_COLOR = Color.DARK_GRAY;

	private Cell[][] board;
	private Cell head, food;
	private boolean over = false, needNewFood = false;
	private Timer timer;
	private int curDir = 0, changeDir = 0, snakeSize = 1, snakeCount = 1;
	private Queue<Cell> snakeQueue;

	public Board(int row, int col) {
		this.board = new Cell[col + 2][row + 2];
		this.snakeQueue = new LinkedList<Cell>();

		super.setPreferredSize(new 
				Dimension(board.length * CELL_DIMENSION, board[0].length * CELL_DIMENSION));
		for (int c = 0; c < board.length; c++) {
			for (int r = 0; r < board[c].length; r++) {
				if (c == 0 || c == col + 1 || r == 0 || r == row + 1) {
					this.board[c][r] = new Cell(c, r, WALL_COLOR);
				} else {
					this.board[c][r] = new Cell(c, r, BOARD_COLOR);
				}
			}
		}
		this.board[1][1].setColor(SNAKE_COLOR);
		this.head = board[1][1];
		this.snakeQueue.offer(head);
		insertFood();

		super.addKeyListener(new Keyboard());
		super.setFocusable(true);
		ActionListener animator = new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (!over) {
					nextAnimationStep();	//updated to take in a parameter. here it will be null
					changeDir = 0;
					if (needNewFood) insertFood();
					repaint();
				} else {
					timer.stop();
					String[] options = {"Play again!", "Quit"};

					int output = JOptionPane.showOptionDialog(null, "Your size is " + snakeSize, 
							"", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
					if (output == 0) {
						reset();
					} else if (output == 1) {
						System.exit(0);
					}
				}
			}
		};
		timer = new Timer(SPEED, animator);
		timer.start();
	}
	
	private void reset() {
		this.over = false;
		for (int c = 0; c < board.length; c++) {
			for (int r = 0; r < board[c].length; r++) {
				if (c == 0 || c == board.length - 1 || r == 0 || r == board[0].length - 1) {
					this.board[c][r] = new Cell(c, r, WALL_COLOR);
				} else {
					this.board[c][r] = new Cell(c, r, BOARD_COLOR);
				}
			}
		}
		snakeQueue.clear();
		this.board[1][1].setColor(SNAKE_COLOR);
		this.head = board[1][1];
		this.snakeQueue.offer(head);
		this.snakeCount = 1;
		this.snakeSize = 1;
		curDir = 0; 
		changeDir = 0; 
		snakeSize = 1; 
		snakeCount = 1;
		insertFood();
		timer.start();
	}
	
	private void insertFood() {
		do {
			this.food = 
					board[(int)(Math.random()*(board.length - 2) + 1)]
							[(int)(Math.random()*(board[0].length - 2) + 1)];
		} while (food.getColor() == SNAKE_COLOR);
		this.food.setColor(FOOD_COLOR);
		this.needNewFood = false;
	}
	
	private void nextAnimationStep() {
		Cell tail = snakeQueue.peek();
		if (tail != null) {
			if (snakeCount == snakeSize) {
				tail.setColor(BOARD_COLOR);
				snakeQueue.poll();
			} else snakeCount++; 
		}
		int c = 0, r = 0;

		switch (curDir) {
			case 0: // rightwards
				if (changeDir == 1) 
					curDir = 1;
				else if (changeDir == -1) 
					curDir = 3;
				
				r = head.getRow() + changeDir;
				c = head.getColumn();
				if (changeDir == 0) 
					c++;
				break;
			case 1: // downwards
				if (changeDir == -1) 
					curDir = 2;
				else if (changeDir == 1) 
					curDir = 0;

				c = head.getColumn() + changeDir;
				r = head.getRow();
				if (changeDir == 0) 
					r++;
				
				break;
			case 2: //leftwards
				if (changeDir == -1) 
					curDir = 3;
				else if (changeDir == 1)
					curDir = 1;
				
				c = head.getColumn();
				r = head.getRow() + changeDir;
				if (changeDir == 0) 
					c--;
				break;
			case 3: //upwards
				if (changeDir == -1) 
					curDir = 2;
				else if (changeDir == 1) 
					curDir = 0;

				c = head.getColumn() + changeDir;
				r = head.getRow();
				if (changeDir == 0) 
					r--;
				break;
		}

		if (r == 0 || r == board[0].length - 1 || c == board.length - 1 || c == 0) 
			over = true;
		if (board[c][r] == food) {
			needNewFood = true;
			snakeSize += GROWTH_INCREMENT;
		}
		if (board[c][r].getColor() == SNAKE_COLOR) over = true;
		board[c][r].setColor(SNAKE_COLOR);
		head = board[c][r];
		snakeQueue.offer(head);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, super.getWidth(), super.getHeight());
		/* Drawing the grid */
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				g.setColor(board[row][col].getColor());
				g.fill3DRect(row * CELL_DIMENSION, col * CELL_DIMENSION,
							 CELL_DIMENSION, CELL_DIMENSION, RAISED_CELL);
			}
		}
	}
	
	private class Keyboard implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {}

		@Override
		public void keyPressed(KeyEvent e) {
		    switch(e.getKeyCode()) { 
		        case KeyEvent.VK_UP:
		        	if (curDir == 0 || curDir == 2) 
		        		changeDir = -1;
		            break;
		        case KeyEvent.VK_DOWN:
		        	if (curDir == 0 || curDir == 2) 
		        		changeDir = 1;
		            break;
		        case KeyEvent.VK_LEFT:
		        	if (curDir == 1 || curDir == 3) 
		        		changeDir = -1;
		            break;
		        case KeyEvent.VK_RIGHT :		        	
		        	if (curDir== 1 || curDir == 3) 
		        		changeDir = 1;
		            break;
		        default:
		        	break;
		    }
		    repaint();
		}

		@Override
		public void keyReleased(KeyEvent e) {}
	}
}
