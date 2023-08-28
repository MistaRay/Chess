package chess;

import java.awt.image.BufferedImage;

public class Bishop extends Piece{
	public Bishop(int row, int col, int color) {
		super(row, col, color);
		if (this.color == Chess.BLACK) {
			img = spriteSheet.getSubimage(x * 3 + offset * 2, offset, size, size);
		}
		else {
			img = spriteSheet.getSubimage(x * 3 + offset * 2, offset + y, size, size);
		}
		name = "b";
	}
	public boolean isTargeting(Vector2D target, Piece [][] board) {
		boolean result = super.isTargeting(target, board);
		if (result == false) {
			return false;
		}
		Vector2D direction = target.subtract(position);
		if (Math.abs(direction.row) != Math.abs(direction.col)) {
			return false;
		}
		direction = direction.normalized();
		Vector2D tempPos = position;
		while (true) {
			tempPos = tempPos.add(direction);
			if(tempPos.equals(target)) {
				break;
			}
			if (board[tempPos.row][tempPos.col] != null) {
				return false;
			}
		}
		return true;
	}
	public static BufferedImage getImg(int color) {
		if (color == Chess.BLACK) {
			return spriteSheet.getSubimage(x * 3 + offset * 2, offset, size, size);
		}
		else {
			return spriteSheet.getSubimage(x * 3 + offset * 2, offset + y, size, size);
		}
	}
}
