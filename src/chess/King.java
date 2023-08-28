package chess;

public class King extends Piece{
	public King(int row, int col, int color) {
		super(row, col, color);
		if (this.color == Chess.BLACK) {
			img = spriteSheet.getSubimage(x * 0 + offset, offset, size, size);
		}
		else {
			img = spriteSheet.getSubimage(x * 0 + offset, offset + y, size, size);
		}
		name = "k";
	}
	public boolean isTargeting(Vector2D target, Piece [][] board) {
		boolean result = super.isTargeting(target, board);
		if (result == false) {
			return false;
		}
		Vector2D direction = target.subtract(position);
		// checking for castling
		if (direction.row == 0 && Math.abs(direction.col) == 2) {
			if (this.hasMoved) {
				return false;
			}
			//king-side castle
			if (direction.col > 1){
				//checking for rook
				if (board[this.position.row][7] == null || board[this.position.row][7].hasMoved) {
					return false;
				}
				//checking for empty spaces
				if (board[this.position.row][5] != null || board[this.position.row][6] != null) {
					return false;
				}
			}
			//queen-side castle
			if (direction.col < 1){
				//checking for rook
				if (board[this.position.row][0] == null || board[this.position.row][0].hasMoved) {
					return false;
				}
				//checking for empty spaces
				if (board[this.position.row][3] != null || board[this.position.row][2] != null || board[this.position.row][1] != null) {
					return false;
				}
			}
			return true;
		}
		if (Math.abs(direction.row) != Math.abs(direction.col) && target.row != position.row && target.col != position.col) {
			return false;
		}
		if (Math.abs(direction.row) > 1 || Math.abs(direction.col) > 1) {
			return false;
		}
		return true;
	}
}
