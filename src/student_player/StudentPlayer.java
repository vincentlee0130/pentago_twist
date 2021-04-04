package student_player;

import java.util.ArrayList;
import java.util.Collections;

import boardgame.Move;

import pentago_twist.PentagoPlayer;
import pentago_twist.PentagoBoardState.Piece;
import pentago_twist.PentagoBoardState;
import pentago_twist.PentagoMove;

/** A player file submitted by a student. */
public class StudentPlayer extends PentagoPlayer {

	/**
	 * You must modify this constructor to return your student number. This is
	 * important, because this is what the code that runs the competition uses to
	 * associate you with your agent. The constructor should do nothing else.
	 */
	public StudentPlayer() {
		super("260774232");
	}

	/**
	 * This is the primary method that you need to implement. The ``boardState``
	 * object contains the current state of the game, which your agent must use to
	 * make decisions.
	 */
	public Move chooseMove(PentagoBoardState boardState) {
		// You probably will make separate functions in MyTools.
		// For example, maybe you'll need to load some pre-processed best opening
		// strategies...

		//        MyTools.getSomething();
		Piece myPieceColor;
		if (getColor() == 0) {
			myPieceColor = Piece.WHITE;
		} else {
			myPieceColor = Piece.BLACK;
		}

		System.out.println("I AM PLAYER: " + getColor());

		// Is random the best you can do?
//		        Move myMove = boardState.getRandomMove();

		Move myMove = minimaxDecision(boardState, myPieceColor);

		//        PentagoMove pMove = (PentagoMove) myMove;
		//        boardState.processMove(pMove);
		//        System.out.println("MY SCORE: " + utility(boardState, myPieceColor));

		// Return your move to be processed by the server.
		return myMove;
	}

//	private void miniMax(PentagoBoardState boardState, Piece myPieceColor) {
//		Piece opponentPiece;
//		if (myPieceColor == Piece.BLACK) {
//			opponentPiece = Piece.WHITE;
//		} else {
//			opponentPiece = Piece.BLACK;
//		}
//		
//		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
//	}
	
	public PentagoMove minimaxDecision(PentagoBoardState boardState, Piece myPiece) {
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		ArrayList<Integer> values = new ArrayList<>(legalMoves.size());
		
		for (int i = 0; i < legalMoves.size(); i++) {
			PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
			boardCopy.processMove(legalMoves.get(i));
			values.add(miniMaxValue(boardCopy, myPiece, 0));
		}
		int maxValueIndex = -1;
		int maxValue = Collections.max(values);
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) == maxValue) {
				maxValueIndex = i;
			}
		}
		return legalMoves.get(maxValueIndex);
	}
	
	public int miniMaxValue(PentagoBoardState boardState, Piece myPiece, int depth) {
		if (depth == 1) {
			return utility(boardState, myPiece);
		}
		
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		ArrayList<Integer> values = new ArrayList<>(legalMoves.size());
		
		for (int i = 0; i < legalMoves.size(); i++) {
			PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
			boardCopy.processMove(legalMoves.get(i));
			values.add(miniMaxValue(boardCopy, myPiece, depth+1));
		}
		
		if (depth % 2 == 0) {
			return Collections.max(values);
		} else {
			return Collections.min(values);
		}
	}
	
//	private PentagoMove getOptimalMove(PentagoBoardState boardState, Piece myPieceColor) {
//		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
//		PentagoMove bestMove = legalMoves.get(0);
//		PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
//		boardCopy.processMove(bestMove);
//		int maxScore = utility(boardCopy, myPieceColor);
//		for (PentagoMove m : legalMoves) {
//			boardCopy = (PentagoBoardState) boardState.clone();
//			boardCopy.processMove(m);
//			int moveScore = utility(boardCopy, myPieceColor);
//			if (moveScore > maxScore) {
//				maxScore = moveScore;
//				bestMove = m;
//			}
//		}
//		return bestMove;
//	}

	private int utility(PentagoBoardState boardState, Piece myPiece) {
		Piece opponentPiece;
		if (myPiece == Piece.BLACK) {
			opponentPiece = Piece.WHITE;
		} else {
			opponentPiece = Piece.BLACK;
		}
		
		return getScoreByPiece(boardState, myPiece) - getScoreByPiece(boardState, opponentPiece);
	}
	
	private int getScoreByPiece(PentagoBoardState boardState, Piece pieceColor) {
		int score = 0;
		Piece[][] board = boardState.getBoard();
		for (int y = 0; y < 6; y++) {
			int horizontalStreak = 0;
			for (int x = 0; x < 5; x++) {
				if (board[y][x] == pieceColor && board[y][x+1] == pieceColor) {
					score = horizontalStreak + 1;
					horizontalStreak++;
				} else {
					horizontalStreak = 0;
				}
			}
		}
		
		for (int x = 0; x < 6; x++) {
			int verticalStreak = 0;
			for (int y = 0; y < 5; y++) {
				if (board[y][x] == pieceColor && board[y+1][x] == pieceColor) {
					score = verticalStreak + 1;
					verticalStreak++;
				} else {
					verticalStreak = 0;
				}
			}
		}
		
		for (int i = 0; i < 5; i++) {
			int diagStreak = 0;
			if (board[i][i] == pieceColor && board[i+1][i+1] == pieceColor) {
				score = diagStreak + 1;
				diagStreak++;
			} else {
				diagStreak = 0;
			}
		}
		
		for (int i = 0; i < 5; i++) {
			int diagStreak = 0;
			if (board[5-i][5-i] == pieceColor && board[5-i-1][5-i-1] == pieceColor) {
				score = diagStreak + 1;
				diagStreak++;
			} else {
				diagStreak = 0;
			}
		}
		

		return score;
	}
}

//class Node<T> {
//	public T data;
//	private Node<T> parent;
//	private ArrayList<Node<T>> children;
//
//	public Node(T data) {
//		this.data = data;
//		this.children = new ArrayList<Node<T>>();
//	}
//	
//	public void addParent(Node<T> parent) {
//		this.parent = parent;
//	}
//	
//	public void addChild(Node<T> child) {
//		this.children.add(child);
//	}
//	
//	public void setChildren(ArrayList<Node<T>> children) {
//		this.children = children;
//	}
//	
//}