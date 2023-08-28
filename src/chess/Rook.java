package chess;

import java.awt.image.BufferedImage;

public class Rook extends Piece{
	public Rook(int row, int col, int color) {
		super(row, col, color);
		img = getImg(this.color);
		name = "r";
	}
	public boolean isTargeting(Vector2D target, Piece [][] board) {
		boolean result = super.isTargeting(target, board);
		if (result == false) {
			return false;
		}
		if (target.row != position.row && target.col != position.col) {
			return false;
		}
		Vector2D direction = target.subtract(position).normalized();
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
			return spriteSheet.getSubimage(x * 2 + offset * 2 - 4, offset, size, size);
		}
		else {
			return spriteSheet.getSubimage(x * 2 + offset * 2 - 4, offset + y, size, size);
		}
	}
}
