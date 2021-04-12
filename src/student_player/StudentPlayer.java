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
	
//	private int alpha = Integer.MIN_VALUE;
//	private int beta = Integer.MAX_VALUE;
	
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

//		System.out.println("I AM PLAYER: " + getColor());

		Move myMove = minimaxDecision(boardState, myPieceColor);

		//        PentagoMove pMove = (PentagoMove) myMove;
		//        boardState.processMove(pMove);
		//        System.out.println("MY SCORE: " + utility(boardState, myPieceColor));

		// Return your move to be processed by the server.
		return myMove;
	}
	
	public PentagoMove minimaxDecision(PentagoBoardState boardState, Piece myPiece) {
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		ArrayList<Integer> values = new ArrayList<>(legalMoves.size());
		
//		int alpha = Integer.MIN_VALUE;
//		int beta = Integer.MAX_VALUE;
		
		for (PentagoMove m : legalMoves) {
			PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
			boardCopy.processMove(m);
			int value = minimaxValue(boardCopy, myPiece, 0);
//			int value = minValue(boardCopy,myPiece, 0, alpha, beta);
//			if (value > alpha) {
//				alpha = value;
//			}
			values.add(value);
		}
		int max = Collections.max(values);
		int index = -1;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) == max) {
				index = i;
			}
		}
		return legalMoves.get(index);
	}
	
	int minimaxValue(PentagoBoardState boardState, Piece myPiece, int depth) {
		if (depth == 0) {
			return utility(boardState, myPiece);
		}
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		ArrayList<Integer> values = new ArrayList<>();
		
		for (PentagoMove m : legalMoves) {
			PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
			boardCopy.processMove(m);
			values.add(minimaxValue(boardCopy, myPiece, depth+1));
		}
		
		if (depth % 2 == 0) {
			return Collections.min(values);
		} else {
			return Collections.max(values);
		}
	}
	
	int maxValue(PentagoBoardState boardState, Piece myPiece, int depth, int alpha, int beta) {
		if (depth == 1) {
			return utility(boardState, myPiece);
		}
		
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		
		for (PentagoMove m : legalMoves) {
			PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
			boardCopy.processMove(m);
			alpha = Integer.max(alpha, minValue(boardCopy, myPiece, depth+1, alpha, beta));
			if (alpha >= beta) {
				return beta;
			}
		}
		return alpha;
	}
	
	int minValue(PentagoBoardState boardState, Piece myPiece, int depth, int alpha, int beta) {
		if (depth == 1) {
			return utility(boardState, myPiece);
		}
		
		ArrayList<PentagoMove> legalMoves = boardState.getAllLegalMoves();
		
		for (PentagoMove m : legalMoves) {
			PentagoBoardState boardCopy = (PentagoBoardState) boardState.clone();
			boardCopy.processMove(m);
			beta = Integer.min(beta, maxValue(boardCopy, myPiece, depth+1, alpha, beta));
			if (alpha >= beta) {
				return alpha;
			}
		}
		return beta;
	}
	
	private int utility(PentagoBoardState boardState, Piece myPiece) {
		Piece opponentPiece;
		if (myPiece == Piece.BLACK) {
			opponentPiece = Piece.WHITE;
		} else {
			opponentPiece = Piece.BLACK;
		}
		
//		return getScoreByPiece(boardState, myPiece) - getScoreByPiece(boardState, opponentPiece);
		return getScore(boardState, myPiece, opponentPiece);
	}
	
	private int getScore(PentagoBoardState boardState, Piece myPiece, Piece opponentPiece) {
		int score = 0;
		Piece[][] board = boardState.getBoard();

		for (int y = 0; y < 6; y++) {
			int streak = 0;
			int opponentStreak = 0;
			for (int x = 0; x < 5; x++) {
				if (board[y][x] == myPiece && board[y][x+1] == myPiece) {
					score = score + streak + 1;
					streak++;
					if (streak >= 4) {
						score = score + 1000;
					}
				} else {
					streak = 0;
				}
				if (board[y][x] == opponentPiece && board[y][x+1] == opponentPiece) {
					score = score - opponentStreak - 1;
					opponentStreak++;
					if (opponentStreak >= 4) {
						score = score - 1000;
					}
				} else {
					opponentStreak = 0;
				}
			}
		}

		for (int x = 0; x < 6; x++) {
			int streak = 0;
			int opponentStreak = 0;
			for (int y = 0; y < 5; y++) {
				if (board[y][x] == myPiece && board[y+1][x] == myPiece) {
					score = score + streak + 1;
					streak++;
					if (streak >= 4) {
						score = score + 1000;
					}
				} else {
					streak = 0;
				}
				if (board[y][x] == opponentPiece && board[y+1][x] == opponentPiece) {
					score = score - opponentStreak - 1;
					opponentStreak++;
					if (opponentStreak >= 4) {
						score = score - 1000;
					}
				} else {
					opponentStreak = 0;
				}
			}
		}
		
		// Check diagonal of the shape: /
		for (int k = 0; k <= 2 * (6 - 1); ++k) {
			int streak = 0;
			int opponentStreak = 0;
		    for (int y = 0; y < 6; ++y) {
		        int x = k - y;
		        if (x < 0 || x >= 6) {
		            // Coordinates are out of bounds; skip.
		        } else {
		        	if (board[y][x] == myPiece) {
		        		if (streak > 0) {
		        			score = score + streak + 1;
		        		}
		        		streak++;
		        		if (streak > 4) {
							score = score + 1000;
						}
		        	} else {
		        		streak = 0;
		        	}
		        	if (board[y][x] == opponentPiece) {
		        		if (opponentStreak > 0) {
		        			score = score - opponentStreak - 1;
		        		}
		        		opponentStreak++;
		        		if (opponentStreak > 4) {
							score = score - 1000;
						}
		        	} else {
		        		opponentStreak = 0;
		        	}
		        }
		    }
		}

		// Check diagonal of the shape: \
		for (int k = -5; k < 6; k++) {
			int streak = 0;
			int opponentStreak = 0;
			for (int y = 0; y < 6; y++) {
				int x = y - k;
				if (x < 0 || x >= 6) {
		            // Coordinates are out of bounds; skip.
		        } else {
		        	if (board[y][x] == myPiece) {
		        		if (streak > 0) {
		        			score = score + streak + 1;
		        		}
		        		streak++;
		        		if (streak > 4) {
							score = score + 1000;
						}
		        	} else {
		        		streak = 0;
		        	}
		        	if (board[y][x] == opponentPiece) {
		        		if (opponentStreak > 0) {
		        			score = score - opponentStreak - 1;
		        		}
		        		opponentStreak++;
		        		if (opponentStreak > 4) {
							score = score - 1000;
						}
		        	} else {
		        		opponentStreak = 0;
		        	}
		        }
			}
		}
	return score;
	}
		
	
}
//	private int getScoreByPiece(PentagoBoardState boardState, Piece pieceColor) {
//		int score = 0;
//		Piece[][] board = boardState.getBoard();
//		
//		
//		for (int y = 0; y < 6; y++) {
//			int horizontalStreak = 0;
//			for (int x = 0; x < 5; x++) {
//				if (board[y][x] == pieceColor && board[y][x+1] == pieceColor) {
//					score = score + horizontalStreak + 1;
//					horizontalStreak++;
////					if (horizontalStreak == 3 && board[y][x+1] == Piece.EMPTY) {
////						score = score + 500;
////					}
////					if (horizontalStreak == 4) {
////						score = score + 1000;
////					}
//				} else {
//					horizontalStreak = 0;
//				}
//			}
//		}
//		
//		for (int y = 5; y > 0; y--) {
//			int horizontalStreak = 0;
//			for (int x = 5; x > 1; x--) {
//				if (board[y][x] == pieceColor && board[y][x-1] == pieceColor) {
//					score = score + horizontalStreak + 1;
//					horizontalStreak++;
////					if (horizontalStreak == 3 && board[y][x-1] == Piece.EMPTY) {
////						score = score + 500;
////					}
////					if (horizontalStreak == 4) {
////						score = score + 1000;
////					}
//				} else {
//					horizontalStreak = 0;
//				}
//			}
//		}
//		
//		for (int x = 0; x < 6; x++) {
//			int verticalStreak = 0;
//			for (int y = 0; y < 5; y++) {
//				if (board[y][x] == pieceColor && board[y+1][x] == pieceColor) {
//					score = score + verticalStreak + 1;
//					verticalStreak++;
////					if (verticalStreak == 3 && board[y+1][x] == Piece.EMPTY) {
////						score = score + 500;
////					}
//					if (verticalStreak == 4) {
//						score = score + 1000;
//					}
//				} else {
//					verticalStreak = 0;
//				}
//			}
//		}
//		
//		for (int x = 5; x > 0; x--) {
//			int verticalStreak = 0;
//			for (int y = 5; y > 1; y--) {
//				if (board[y][x] == pieceColor && board[y-1][x] == pieceColor) {
//					score = score + verticalStreak + 1;
//					verticalStreak++;
////					if (verticalStreak == 3 && board[y-1][x] == Piece.EMPTY) {
////						score = score + 500;
////					}
//					if (verticalStreak == 4) {
//						score = score + 1000;
//					}
//				} else {
//					verticalStreak = 0;
//				}
//			}
//		}
//		
////		int diagStreak = 0;
////		for (int i = 0; i < 5; i++) {
////			if (board[i][i] == pieceColor && board[i+1][i+1] == pieceColor) {
////				score = diagStreak + 1;
////				diagStreak++;
////				if (diagStreak == 4) {
////					score = score + 1000;
////				}
////			} else {
////				diagStreak = 0;
////			}
////		}
////		
////		int diagStreak2 = 0;
////		for (int i = 0; i < 5; i++) {
////			if (board[5-i][5-i] == pieceColor && board[5-i-1][5-i-1] == pieceColor) {
////				score = diagStreak2 + 1;
////				diagStreak++;
////				if (diagStreak2 == 4) {
////					score = score + 1000;
////				}
////			} else {
////				diagStreak2 = 0;
////			}
////		}
//		
//
//		return score;
//	}


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