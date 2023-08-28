package chess;

public class Vector2D {
	public int row;
	public int col;
	public Vector2D(int row, int col){
		this.row = row;
		this.col = col;
	}
	
	public Vector2D subtract(Vector2D v) {
		return new Vector2D(this.row - v.row, this.col - v.col);
	}
	
	public Vector2D normalized() {
		return new Vector2D(row / Math.max(Math.abs(row), Math.abs(col)), col / Math.max(Math.abs(row), Math.abs(col)));
	}
	public Vector2D add(Vector2D v) {
		return new Vector2D(this.row + v.row, this.col + v.col);
	}
	
	public boolean equals(Vector2D v) {
		return row == v.row && col == v.col;
	}
}
