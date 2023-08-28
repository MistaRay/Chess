package chess;

import java.awt.image.BufferedImage;

public class Knight extends Piece{
	public Knight(int row, int col, int color) {
		super(row, col, color);
		img = getImg(this.color);
		name = "n";
	}
	public boolean isTargeting(Vector2D target, Piece [][] board) {
		boolean result = super.isTargeting(target, board);
		if (result == false) {
			return false;
		}
		Vector2D direction = target.subtract(position);
		if (Math.abs(direction.row) == 2 && Math.abs(direction.col) == 1 || Math.abs(direction.row) == 1 && Math.abs(direction.col) == 2) {
			return true;
		}
		return false;
	}
	public static BufferedImage getImg(int color) {
		if (color == Chess.BLACK) {
			return spriteSheet.getSubimage(x * 4 + offset * 2 + 8, offset, size, size);
		}
		else {
			return spriteSheet.getSubimage(x * 4 + offset * 2 + 8, offset + y, size, size);
		}
	}
}
