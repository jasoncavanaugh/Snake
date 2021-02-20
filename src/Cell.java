import java.awt.Color;

public class Cell {
	private int row, col;
	private boolean snake;
	private Color color;
	
	public Cell(int col, int row, Color color) {
		this.color = color;
		this.snake = false;
		this.col = col;
		this.row = row;
	}
	
	public boolean isSnake() {
		return snake;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.col;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
