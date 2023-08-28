package chess;

import java.awt.image.BufferedImage;

public class Pawn extends Piece{
	public Pawn(int row, int col, int color){
		super(row, col, color);
		img = getImg(this.color);
		name = "p";
	}
	public boolean isTargeting(Vector2D target, Piece [][] board) {
		boolean result = super.isTargeting(target, board);
		if (result == false) {
			return false;
		}
		Vector2D direction = target.subtract(position);
		//moving fowards
		if (direction.col == 0 && (direction.row > 0 && color == Chess.BLACK || direction.row < 0 && color == Chess.WHITE)) {
			//checking if there is a piece infront of the pawn
			if (color == Chess.BLACK && board[position.row + 1][position.col] != null || color == Chess.WHITE && board[position.row - 1][position.col] != null) {
				return false;
			}
			//checking if the pawn is moving 1 space foward
			if (Math.abs(direction.row) == 1) {
				return true;
			}
			//checking if the pawn moved before going two spaces
			if (Math.abs(direction.row) == 2 && (color == Chess.BLACK && position.row == 1 && board[position.row + 2][position.col] == null || color == Chess.WHITE && position.row == 6 && board[position.row - 2][position.col] == null)) {
				return true;
			}
			return false;
		}
		//moving diagonally
		if (Math.abs(direction.col) == 1 && (direction.row == 1 && color == Chess.BLACK || direction.row == -1 && color == Chess.WHITE)) {
			//checking if the pawn is taking a piece
			if (board[target.row][target.col] != null && this.color != board[target.row][target.col].color) {
				return true;
			}
			if (Chess.enPassentPawn != null && Chess.enPassentPawn.color != this.color && Chess.enPassentPawn.position.row == this.position.row && Chess.enPassentPawn.position.col - this.position.col == direction.col) {
				return true;
			}
		} 
		return false;
	}
	public void move(Vector2D target, Piece [][] board, boolean actualMove) {
		//This is an En-Passent move
		if ((target.row != this.position.row && target.col != this.position.col) && board[target.row][target.col] == null) {
			//Take the En-Passent pawn
			board[Chess.enPassentPawn.position.row][Chess.enPassentPawn.position.col] = null;
		}
		boolean isEnPassent = Math.abs(this.position.row - target.row) == 2;
		super.move(target, board);
		if (isEnPassent) {
			Chess.enPassentPawn = this;
		}
	}
	public static BufferedImage getImg(int color) {
		if (color == Chess.BLACK) {
			return spriteSheet.getSubimage(x * 5 + offset * 2 + 15, offset, size, size);
		}
		else {
			return spriteSheet.getSubimage(x * 5 + offset * 2 + 15, offset + y, size, size);
		}
	}
}
