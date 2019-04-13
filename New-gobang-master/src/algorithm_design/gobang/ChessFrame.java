package algorithm_design.gobang;

import javax.swing.JFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 * @author kaimo
 *
 */
public class ChessFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	// color value and empty value, player-black, robot-white
	public static int EMPTY = 0, BLACK = 1, WHITE = -1;
	// left-top coordinate
	public static int x = 30, y = 50;
	// set ChessBoard size and cell size
	public static int CHESS_SIZE = 15, CELL_SIZE = 30;
	// matrix to store all chess coordinates
	public static int[][] s = new int[CHESS_SIZE][CHESS_SIZE];
	
	public static HashMap <String, Integer> toScore = new HashMap<>();
	
	// two buffer to solve screen flashes
	private Image iBuffer;  
	private Graphics gBuffer; 
	
	public void init() {
		// initialized all coordinates to zero
		for(int i = 0; i < ChessFrame.CHESS_SIZE; i++) {
			for (int j = 0; j < ChessFrame.CHESS_SIZE; j++) {
				this.s[i][j] = EMPTY;
			}
		}
		// always robot move first
		this.makeMove(7, 7, WHITE);
		// turn to player move
		MouseListener.SWITCH = 1;
	}
	
	/**
	 * draw ChessBoard
	 */
	public void paint(Graphics g) {
		
		// initialize paint
		super.paintComponents(g);
		//Graphics2D g2 = (Graphics2D)g; 
		//g2.setStroke(new BasicStroke(1.5f));

		
		// draw vertical and horizontal line
		for (int i = 0; i < CHESS_SIZE; i++) {
			for (int j = 0; j < CHESS_SIZE; j++) {
				g.drawLine(x + i * CELL_SIZE, y,
						x + i * CELL_SIZE, y + (CHESS_SIZE-1) * CELL_SIZE);
				g.drawLine(x, y + i * CELL_SIZE,
						x + (CHESS_SIZE-1) * CELL_SIZE, y + i * CELL_SIZE);
			}
		}
		
		// draw chess
		for (int i = 0; i < CHESS_SIZE; i++) {
			for (int j = 0; j < CHESS_SIZE; j++) {
				if (s[i][j] == BLACK) {
					// set default painting color to BLACK
					g.setColor(Color.BLACK);
					// draw black chess
					g.fillOval(x + i * CELL_SIZE - CELL_SIZE/2,
							y + j * CELL_SIZE - CELL_SIZE/2,
							CELL_SIZE, CELL_SIZE);
				} else if(s[i][j] == WHITE) {
					// set default painting color to WHITE
					g.setColor(Color.WHITE);
					// draw white chess
					g.fillOval(x + i * CELL_SIZE - CELL_SIZE/2,
							y + j * CELL_SIZE - CELL_SIZE/2,
							CELL_SIZE, CELL_SIZE);
				}
			}
		}
		
	}
	
	@Override
	public void update(Graphics g) {
		 //initialize buffer
	    if(iBuffer==null)  {  
	       iBuffer=createImage(this.getSize().width,this.getSize().height);  
	       gBuffer=iBuffer.getGraphics();  
	    }  
       // set gBuffer the same as graphics we want to draw on the back end
       gBuffer.setColor(getBackground());  
       gBuffer.fillRect(0,0,this.getSize().width,this.getSize().height);  
       // repaint the graphic 
       gBuffer.setColor (getForeground());
       paint(gBuffer);
       //repaint the image
       g.drawImage(iBuffer,0,0,this);  
	}  


	public boolean isEmpty(int x, int y) {
		return ChessFrame.s[x][y] == EMPTY;
	}
	
	public void makeMove(int x, int y, int color) {
		ChessFrame.s[x][y] = color;
	}
	
	public void unMove(int x, int y) {
		ChessFrame.s[x][y] = ChessFrame.EMPTY;
	}

	/**
	 * method to check (x, y) if out of boundary
	 */
	public boolean isLegal(int x, int y) {

		return (x > 0 && y > 0 &&
				x < ChessFrame.CHESS_SIZE &&
				y < ChessFrame.CHESS_SIZE);
	}
	
	
	/**
	 *  count all color chess in +/-(dx, dy) direction line
	 */
	public int countNum(int x, int y, int dx, int dy, int color) {
	
		int count = 1;	// current position itself
		
		// direction #1
		int nextX = x + dx;
		int nextY = y + dy;
		
		while (isLegal(nextX, nextY) &&
				ChessFrame.s[nextX][nextY] == color) {
			count = count + 1;
			nextX = nextX + dx;
			nextY = nextY + dy;
		}
		
		// direction #2
		nextX = x - dx;
		nextY = y - dy;
		while (isLegal(nextX, nextY) &&
				ChessFrame.s[nextX][nextY] == color) {
			count = count + 1;
			nextX = nextX - dx;
			nextY = nextY - dy;
		}
		
		return count;
	}
/**
//	public int reckon(int color) {
//	int dx[] = {1, 0, 1, 1};
//	int dy[] = {0, 1, 1, -1};
//	int score = 0;
//	
//	// traversal each coordinate
//	for (int x = 0; x < ChessFrame.CHESS_SIZE; x++) {
//		for (int y = 0; y < ChessFrame.CHESS_SIZE; y++) {
//			
//			// skip the opponent's color
//			if (ChessFrame.s[x][y] != color) { continue; }
//			// a variable to store the count of continuous chess
//			int recordPad[][] = new int[2][100];				
//			
//			// traversal each direction lines
//			for (int i = 0; i < 4; i++) {
//				// flag to store if dead
//				int flag1 = 0, flag2 = 0;
//				// current count
//				int count = 1;	
//				//    direction #1    //
//				int nextX = x + dx[i];	// get next (x,y)
//				int nextY = y + dy[i];	// get next (x,y)
//				// next (x, y) is legal and move on in same direction
//				while (isLegal(nextX, nextY) &&
//						ChessFrame.s[nextX][nextY] == color) {
//					count = count + 1;
//					nextX = nextX + dx[i];
//					nextY = nextY + dy[i];
//				}
//				if (isLegal(nextX, nextY) &&
//						ChessFrame.s[nextX][nextY] == ChessFrame.EMPTY) {
//					flag1 = 1;
//				}
//				//    direction #2    //
//				nextX = x - dx[i];
//				nextY = y - dy[i];
//				while (isLegal(nextX, nextY) &&
//						ChessFrame.s[nextX][nextY] == color) {
//					count = count + 1;
//					nextX = nextX - dx[i];
//					nextY = nextY - dy[i];
//				}
//				if (isLegal(nextX, nextY) &&
//						ChessFrame.s[nextX][nextY] == ChessFrame.EMPTY) {
//					flag2 = 1;
//				}
//				// i.e. at least one flag=1, alive
//				if (flag1 + flag2 > 0) {
//					// if +/- direction promising - dead
//					// if + & - direction promising - alive
//					++recordPad[flag1 + flag2 - 1][count];
//				}
//			}
//			
//			if (recordPad[0][5] > 0 || recordPad[1][5] > 0) {
//				score = Math.max(score, 100000);
//			}else if (recordPad[1][4] > 0||
//					recordPad[0][4] > 1||
//					recordPad[0][4] > 0 && recordPad[1][3] > 0) {
//				score = Math.max(score, 10000);
//			}else if (recordPad[1][3] > 1) {
//				score = Math.max(score, 5000);
//			}else if (recordPad[1][3] > 0 && recordPad[0][3] > 0) {
//				score = Math.max(score, 1000);
//			}else if (recordPad[0][4] > 0) {
//				score = Math.max(score, 500);
//			}else if (recordPad[1][3] > 0) {
//				score = Math.max(score, 200);
//			}else if (recordPad[0][3] > 0) {
//				score = Math.max(score, 100);
//			}else if (recordPad[1][2] > 1) {
//				score = Math.max(score, 50);
//			}else if (recordPad[1][2] > 0) {
//				score = Math.max(score, 10);
//			}else if (recordPad[0][2] > 0) {
//				score = Math.max(score, 5);
//			}else {
//				score = Math.max(score, 1);
//			}
//			
//			
//		}
//	}
//	
//	return score;
//}
**/

	public int reckon(int color) {
		int dx[] = { 1, 0, 1, 1 };
		int dy[] = { 0, 1, 1, -1 };
		int score = 0;
		
		toScore.put("a____", 1);
		// 眠二
		toScore.put("aa___", 100);
		toScore.put("a_a__", 100);
		toScore.put("___aa", 100);
		toScore.put("__a_a", 100);
		toScore.put("a__a_", 100);
		toScore.put("_a__a", 100);
		toScore.put("a___a", 100);

		// 活二"_aa___"
		toScore.put("__aa__", 500);
		toScore.put("_a_a_", 500);
		toScore.put("_a__a_", 500);
		toScore.put("_aa__", 500);
		toScore.put("__aa_", 500);

		// 眠三
		toScore.put("a_a_a", 1000);
		toScore.put("aa__a", 1000);
		toScore.put("_aa_a", 1000);
		toScore.put("a_aa_", 1000);
		toScore.put("_a_aa", 1000);
		toScore.put("aa_a_", 1000);
		toScore.put("aaa__", 1000);

		// 跳活三
		toScore.put("_aa_a_", 9000);
		toScore.put("_a_aa_", 9000);

		// 活三 
		toScore.put("_aaa_", 10000);

		// 冲四
		toScore.put("a_aaa", 15000);
		toScore.put("aaa_a", 15000);
		toScore.put("_aaaa", 15000);
		toScore.put("aaaa_", 15000);
		toScore.put("aa_aa", 15000);

		// 活四
		toScore.put("_aaaa_", 1000000);

		// 连五
		toScore.put("aaaaa", 10000000);

		// traversal each coordinate
		for (int x = 0; x < ChessFrame.CHESS_SIZE; x++) {
			for (int y = 0; y < ChessFrame.CHESS_SIZE; y++) {

				// skip the opponent's color
				if (ChessFrame.s[x][y] != color) {
					continue;
				}
				// a variable to store the count of continuous chess
				int recordPad[][] = new int[2][100];
			
				// traversal each direction lines
				for (int i = 0; i < 4; i++) {
					  StringBuilder input = new StringBuilder();
						String str1 = "a";
						String str2 = "";

					int count = 1;
					// direction #1 //
					int nextX = x + dx[i]; // get next (x,y)
					int nextY = y + dy[i]; // get next (x,y)
				
					while (isLegal(nextX, nextY) && ChessFrame.s[nextX][nextY] != -color && count < 5) {
						// 判断6个棋，用str标记棋型，a为子，_为空
						if (ChessFrame.s[nextX][nextY] == color) {
							str1 += "a";
						} else {
							str1 += "_";
						}
						nextX = nextX + dx[i];
						nextY = nextY + dy[i];
						count++;
					}
                    count = 1;
					nextX = x - dx[i];
					nextY = y - dy[i];
					while (isLegal(nextX, nextY) && ChessFrame.s[nextX][nextY] != -color && count < 5) {
						// 判断6个棋，用str标记棋型，a为子，_为空
						if (ChessFrame.s[nextX][nextY] == color) {
							str2 += "a";
						} else {
							str2 += "_";
						}
						nextX = nextX - dx[i];
						nextY = nextY - dy[i];
						count++;
					}
                input.append(str2);
                input = input.reverse();
                input.append(str1);
                for(String key : toScore.keySet()){
    				if (input.toString().contains(key) ) {
    					//System.out.println("input = "+input.toString());
    					score = Math.max(toScore.get(key), score);
    				}
    				
				}

				}
				
				

			}
		}
		//System.out.println("score= "+score);
		return score;
	}


	public boolean isEnd(int x, int y, int color) {
		
		int dx[] = {1, 0, 1, 1};
		int dy[] = {0, 1, 1, -1};
		
		for (int i = 0; i < 4; i++) {
			int count = countNum(x, y, dx[i], dy[i], color);
			if (count >= 5) {
				return true;
			}
		}
		return false;
	}









}

