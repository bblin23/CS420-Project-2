/**
 * 
 */
package def;

/**
 * @author Bolton
 *
 */
public class Board {

	private Tile[][] board;
	private int SIZE;
	
	public Board(int n){
		this.board = new Tile[n][n];
		this.SIZE = n;
		initBoard();
	}
	
	public Board(Board copy){
		this.SIZE = copy.getSize();
		this.board = copyBoard(copy);
	}
	
	private Tile[][] copyBoard(Board copy) {
		// TODO Auto-generated method stub
		Tile[][] toReturn = new Tile[SIZE][SIZE];
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				toReturn[i][j] = new Tile(copy.board[i][j]);
			}
		}
		return toReturn;
	}

	private void initBoard() {
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				board[i][j] = new Tile(i,j);
			}
		}
	}

	public Tile getTile(int x, int y){
		return board[x][y];
	}
	
	public Tile getTile(PQueen.Coords coords){
		return board[coords.x][coords.y];
	}
	
	public int getSize(){
		return this.SIZE;
	}
	
	public String toString(){
		StringBuilder toReturn = new StringBuilder("");
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(getTile(i,j).hasQ())
					toReturn.append("[Q]");
				else
					toReturn.append("[_]");
			}
			toReturn.append("\n");
		}
		return toReturn.toString();
	}

}
