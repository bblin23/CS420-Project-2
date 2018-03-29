/**
 * 
 */
package def;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bolton
 *
 */
public class Genotype implements Comparable<Genotype> {
	private Board board;
	private String strain;
	private List<Tile> queens;
	private int friendlies;
	private double fitness;
	private int size;
	
	public Genotype(Board board){
		this.board = board;
		this.size = board.getSize();
		this.strain = initStrain();
		this.queens = initQueens();
	}

	public Genotype(String strain){
		this.strain = strain;
		this.size = strain.length();
		this.board = initBoard();
		this.queens = initQueens();
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public String getStrain(){
		return this.strain;
	}
	
	public ArrayList<Tile> getQueens(){
		return (ArrayList<Tile>) this.queens;
	}
	
	public int getFriendlies(){
		return this.friendlies;
	}
	
	public double getFitness(){
		return this.fitness;
	}
	
	public void setFriendlies(int friendlies){
		this.friendlies = friendlies;
	}
	
	public void setFitness(double fitness){
		this.fitness = fitness;
	}
	
	private Board initBoard() {
		Board toReturn = new Board(strain.length());
		for(int i = 0; i < size; i++){
			toReturn.getTile(i,Character.getNumericValue(strain.charAt(i))).placeQ();
		}
		return toReturn;
	}

	private String initStrain(){
		StringBuilder toReturn = new StringBuilder("");
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				if(board.getTile(i,j).hasQ())
					toReturn.append(j);
			}
		}
		return toReturn.toString();
	}
	
	private ArrayList<Tile> initQueens() {
		List<Tile> toReturn = new ArrayList<Tile>(board.getSize());
		int x; int y;
		for(int i = 0; i < size; i++){
			x = i;
			y = Character.getNumericValue(strain.charAt(i));
			toReturn.add(board.getTile(x,y));
		}
		return (ArrayList<Tile>) toReturn;
	}
	
	public String toString(){
		return getStrain() + "\t" + friendlies + "\t" + fitness;
	}
	
	@Override
	public int compareTo(Genotype o) {
		return Double.compare(o.fitness, this.fitness);
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
}
