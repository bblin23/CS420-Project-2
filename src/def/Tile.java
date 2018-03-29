/**
 * 
 */
package def;

/**
 * @author Bolton
 *
 */
public class Tile {
	
	private boolean hasQ;
	private int x;
	private int y;
	private int value;
	
	public Tile(){
		hasQ = false;
	}
	
	public Tile(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Tile(Tile copy){
		this.x = copy.x;
		this.y = copy.y;
		this.hasQ = copy.hasQ;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public boolean hasQ(){
		return hasQ;
	}
	
	public void placeQ(){
		this.hasQ = true;
	}
	
	public void removeQ(){
		this.hasQ = false;
	}
	
	public String toString(){
		return "("+x+","+y+") "+value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
