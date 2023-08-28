package chess;

import java.util.Scanner;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.awt.font.*;

public class Chess extends Frame implements MouseListener, MouseMotionListener{
	public Canvas canvas = new Canvas() {
		public void paint(Graphics g) {
			/*System.out.println(Font.getAllFonts());
			Font font = Font.getFont("Serif");
			   FontRenderContext frc = ((Graphics2D)g).getFontRenderContext();
			   TextLayout layout = new TextLayout("RESET", font, frc);
			   layout.draw((Graphics2D) g, 380, 850);
			   */
			g.setColor(new Color(170, 200, 255));
			g.fillRect(380, 850, 150, 60);
			for (int i = 0; i < 8; i++) {
				for (int j = 0; j < 8; j++) {
					if (selectedPiece != null && selectedPiece.position.row == i && selectedPiece.position.col == j) {
						g.setColor(Color.yellow);
					}
					else if ((i + j)%2 == 0) {
						g.setColor(Color.white);
					}
					else {
						g.setColor(new Color(170, 200, 255));
					}
					if (state != 0) {
						if (state == 1 && board[i][j] == king[turn]) {
							g.setColor(Color.red);
						}
						if (state == 2 && (board[i][j] == king[0] || board[i][j] == king[1])) {
							g.setColor(Color.red);
						}
					}
					g.fillRect(j * 100, i * 100, 100, 100);
					if (board[i][j] != null) {
						g.drawImage(board[i][j].img, j * 100, i * 100, this);
					}
				}
			}
			if (selectedPiece != null) {
				//g.drawImage(selectedPiece.img, mouseLocation.x - 56, mouseLocation.y - 60, this);
			}
			// displaying all available moves when selecting a piece
			if (selectedPiece != null) {
				g.setColor(new Color(175, 175, 175));
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						if (isLegal(selectedPiece.position, new Vector2D(i,j)) != ILLEGAL) {
							if (board[i][j] != null) {
								g.drawOval(j * 100, i * 100, 100, 100);
								g.drawOval(j * 100 + 1, i * 100 + 1, 98, 98);
								g.drawOval(j * 100 + 2, i * 100 + 2, 96, 96);
							}
							else {
								g.fillOval(j * 100 + 30, i * 100 + 30, 40, 40);
							}
						}
					}
				}
			}
			//drawing for promotion
			if (state == 3) {
				if (turn == BLACK) {
					g.setColor(new Color(175, 175, 175));
					g.fillRect(promotionPos.col * 100, promotionPos.row * 100, 100, 400);
					g.drawImage(Queen.getImg(WHITE),  promotionPos.col * 100, promotionPos.row * 100, this);
					g.drawImage(Bishop.getImg(WHITE),  promotionPos.col * 100, promotionPos.row * 100 + 100, this);
					g.drawImage(Knight.getImg(WHITE),  promotionPos.col * 100, promotionPos.row * 100 + 200, this);
					g.drawImage(Rook.getImg(WHITE),  promotionPos.col * 100, promotionPos.row * 100 + 300, this);
				}
			}
		}
	};
	public int state = 0;
	public Point mouseLocation;
	public static final int BLACK = 0;
	public static final int WHITE = 1;
	private int turn = WHITE;
	private Piece [][] board;
	private Piece king [] = new Piece[2]; 
	public static Piece enPassentPawn = null;
	public static final int ILLEGAL = 0;
	public static final int LEGAL = 1;
	public static final int KINGSIDE = 2;
	public static final int QUEENSIDE = 3;
	public static final int PROMOTE = 4;
	public Piece selectedPiece = null;
	public Vector2D promotionPos;
	public Chess() {
		reset();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
		setSize(1000, 1000);
		add(canvas);
		setVisible(true);
		//frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
	}
	
	public void reset() {
		this.board = new Piece[8][8];
		board [0][0] = new Rook(0,0,BLACK);
		board [0][1] = new Knight(0,1,BLACK);
		board [0][2] = new Bishop(0,2,BLACK);
		board [0][3] = new Queen(0,3,BLACK);
		board [0][4] = new King(0,4,BLACK);
		king[BLACK] = board[0][4];
		board [0][5] = new Bishop(0,5,BLACK);
		board [0][6] = new Knight(0,6,BLACK);
		board [0][7] = new Rook(0,7,BLACK);
		for (int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(1, i, BLACK);
			board[6][i] = new Pawn(6, i, WHITE);
		}
		board [7][0] = new Rook(7,0,WHITE);
		board [7][1] = new Knight(7,1,WHITE);
		board [7][2] = new Bishop(7,2,WHITE);
		board [7][3] = new Queen(7,3,WHITE);
		board [7][4] = new King(7,4,WHITE);
		king[WHITE] = board[7][4];
		board [7][5] = new Bishop(7,5,WHITE);
		board [7][6] = new Knight(7,6,WHITE);
		board [7][7] = new Rook(7,7,WHITE);
		turn = WHITE;
		selectedPiece = null;
		state = 0;
	}
	
	/*(public void display() {
		System.out.println("   ABCDEFGH\n");
		//System.out.println("");
		for (int i = 0; i < 8; i++) {
			System.out.print(8 - i + "  ");
			for (int j = 0; j < 8; j++) {
				if (board[i][j] == null) {
					System.out.print(" ");
				}
				else {
					System.out.print(board[i][j]);
				}
			}
			System.out.println("");
		}
	}
	*/
	public int isLegal(Vector2D position, Vector2D target) {
		Piece piece = board[position.row][position.col];
		if (piece == null || turn != piece.color) {
			return ILLEGAL;
		}
		boolean result = piece.isTargeting(target, board);
		if (result == false) {
			return ILLEGAL;
		}
		Piece takenPiece = board[target.row][target.col];
		//checking for castling
		if (piece instanceof King && Math.abs(target.col - position.col) == 2) {
			if (isCheck(piece.color)) {
				return ILLEGAL;
			}
			int direction = -1;
			//checking for kingside castling
			if (target.col - position.col > 0) {
				direction = 1;
			}
			piece.move(piece.position.add(new Vector2D(0, direction)), board);
			if (isCheck(piece.color)) {
				piece.move(piece.position.add(new Vector2D(0, -direction)), board);
				return ILLEGAL;
			}
			piece.move(piece.position.add(new Vector2D(0, direction)), board);
			if (isCheck(piece.color)) {
				piece.move(position, board);
				return ILLEGAL;
			}
			piece.move(position, board);
			if (direction == 1) {
				return KINGSIDE;
			}
			return QUEENSIDE;
		}
		piece.move(target, board);
		result = isCheck(piece.color);
		piece.move(position, board);
		board[target.row][target.col] = takenPiece;
		if (result) {
			return ILLEGAL;
		}
		if (piece instanceof Pawn && (piece.color == WHITE && target.row == 0 || piece.color == BLACK && target.row == 7)) {
			return PROMOTE;
		}
		return LEGAL;
	}
	
	public boolean isCheck(int color) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (board[i][j] != null && color != board[i][j].color && board[i][j].isTargeting(king[color].position, board)) {
					return true;
				}
			}
		}
		return false; 
	}
	
	public int isGameover(int color) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				for (int l = 0; l < 8; l++) {
					for (int p = 0; p < 8; p++) {
						if (isLegal(new Vector2D(i, j), new Vector2D(l, p)) != ILLEGAL) {
							return 0;
						}
					}
				}
			}
		}
		if (isCheck(color) == true) {
			return 1;
		}
		return 2;
	}
	
	/*public Vector2D [] getValidInput(Scanner input) {
		String str;
		while (true) {
			str = input.nextLine();
			if (str.length() == 4 && str.charAt(0) >= 'a' && str.charAt(0) <= 'h' && str.charAt(2) >= 'a' && str.charAt(2) <= 'h' && str.charAt(1) >= '1' && str.charAt(1) <= '8' && str.charAt(3) >= '1' && str.charAt(3) <= '8'){
				break;
			}
			System.out.println("Invalid Input");
		}
		Vector2D [] output = new Vector2D[2];
		output[0] = new Vector2D(8 - str.charAt(1) + '1' - 1, str.charAt(0) - 'a');
		output[1] = new Vector2D(8 - str.charAt(3) + '1' - 1, str.charAt(2) - 'a');
		return output;
	}*/
	
	public void start() {
		repaint();
	}

	public static void main(String[] args) {
		Frame frame = new Frame(); 
		Chess game = new Chess();
		game.start();
	}
	@Override
	public void mousePressed(MouseEvent e) {
		Vector2D p = new Vector2D(e.getPoint().y / 100, e.getPoint().x / 100);
		if (p.row > 7 || p.col > 7) {
			if (e.getPoint().x >= 380 && e.getPoint().x <= 530 && e.getPoint().y >= 850 && e.getPoint().y <= 910) {
				reset();
				canvas.repaint();
			}
			return;
		}
		if (board[p.row][p.col] != null && board[p.row][p.col].color == turn) {
			selectedPiece = board[p.row][p.col];
		}
		else if (selectedPiece != null) {
			int result = isLegal(selectedPiece.position, p);
			if (result != ILLEGAL) {
				board[selectedPiece.position.row][selectedPiece.position.col].move(p, board, true);
				if (result == KINGSIDE) {
					board[selectedPiece.position.row][7].move(new Vector2D(selectedPiece.position.row, 5), board, true);
				}
				else if (result == QUEENSIDE) {
					board[selectedPiece.position.row][0].move(new Vector2D(selectedPiece.position.row, 3), board, true);
				}
				else if (result == PROMOTE) {
					state = 3;
					promotionPos = selectedPiece.position;
					/*Piece piece = new Queen(p.row, p.col, turn);
					if (answer.equals("r")) {
						piece = new Rook(p.row, p.col, turn);
					}
					if (answer.equals("b")) {
						piece = new Bishop(p.row, p.col, turn);
					}
					if (answer.equals("n")) {
						piece = new Knight(p.row, p.col, turn);
					}
					board[piece.position.row][piece.position.col] = piece;*/
				}
				turn += 1;
				turn %= 2;
				int gameOver = isGameover(turn);
				if (gameOver == 1) {
					System.out.println("Checkmate");
					state = 1;
					canvas.repaint();
				}
				if (gameOver == 2) {
					System.out.println("Stalemate");
					state = 2;
					canvas.repaint();
				}
				if (isCheck(turn)) {
					System.out.println("CHECK");
				}
				selectedPiece = null;
			}
			else {
				System.out.println("Illegal Move");
			}
		}
		canvas.repaint();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		mouseLocation = e.getLocationOnScreen();
		//canvas.repaint();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {

	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		//System.out.println(123);
	}

}
