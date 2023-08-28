package chess;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Piece {
	public String name;
	public int color;
	public Vector2D position;
	public boolean hasMoved;
	public BufferedImage img;
	public static BufferedImage spriteSheet;
	public static int x = 161;
	public static int y = 145;
	public static int size = 100;
	public static int offset = 20;
	public Piece(int row, int col, int color){
		try {
		    spriteSheet = ImageIO.read(new File("chess.png"));
		} catch (IOException e) {
		}
		this.position = new Vector2D(row, col);
		this.color = color;
		this.hasMoved = false;
	}
	public static BufferedImage getImg(int color) {
		return null;
	}
	public boolean isTargeting(Vector2D target, Piece [][] board) {
		if (target.row >= 8 || target.col >= 8 || target.row < 0 || target.col < 0) {
			return false;
		}
		if (board[target.row][target.col] != null && board[target.row][target.col].color == color) {
			return false;
		}
		return true;
	}
	public void move(Vector2D target, Piece [][] board) {
		board[target.row][target.col] = this;
		board[position.row][position.col] = null;
		position = target;
	}
	public void move(Vector2D target, Piece [][] board, boolean actualMove) {
		move(target, board);
		Chess.enPassentPawn = null;
		hasMoved = true;
	}
	public String toString() {
		if (color == Chess.WHITE) {
			return name.toUpperCase();
		}
		return name;
	}
}
