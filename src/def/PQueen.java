/**
 * 
 */
package def;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * @author Bolton
 *
 */
public class PQueen {
	
	class Coords{
		int x; int y;
		public Coords(int x, int y){
			this.x = x;
			this.y = y;
		}
	}
	
	private Board board;
	private List<Coords> queens;
	private Map<Tile, ArrayList<Tile>> map;
	private PriorityQueue<Node> boardQ;
	private double trials;
	private double success;
	private long totalTime;
	private long avgTime;
	private boolean debug;
	
	public PQueen(){
		trials = 0;
		success = 0;
		map = new HashMap<Tile, ArrayList<Tile>>();
		queens = new ArrayList<Coords>();
		debug = false;
	}
	
	public PQueen(int n){
		trials = 0;
		success = 0;
		debug = false;
		makeBoard(n);	
	}
	
	public void makeBoard(int n){
		this.board = new Board(n);
		map = new HashMap<Tile, ArrayList<Tile>>(n);
		queens = new ArrayList<Coords>(n);
	}
	
	public Board getBoard(){
		return this.board;
	}
	
	public String getRep(){
		return board.toString();
	}
	
	public void genRandConfig(){
		Random rand = new Random();
		int randx;
		int randy;
		for(int i = 0; i < board.getSize(); i++){
			randx = Math.abs(rand.nextInt()%board.getSize());
			randy = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(randx,randy))
				placeQ(randx, randy);
			else
				i--;
		}
	}
	
	public void genSemiRandConfig(){
		Random rand = new Random();
		int randy;
		for(int i = 0; i < board.getSize(); i++){
			randy = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(i,randy))
				placeQ(i, randy);
			else
				i--;
		}
	}
	
	public void genSemiRandConfig(int size){
		makeBoard(size);
		Random rand = new Random();
		int randy;
		for(int i = 0; i < board.getSize(); i++){
			randy = Math.abs(rand.nextInt()%board.getSize());
			if(!checkQ(i,randy))
				placeQ(i, randy);
			else
				i--;
		}
	}
	
	
	public void fixBoard(){
		//TODO Make a random config into a semi random config (make sure only one
		//queen for each row)
	
	}
	
	public void nTrials(int n, int size, int mode, int additional1, 
			double additional2, int debug){
		long startTime;
		long endTime;
		long time;
		totalTime = 0;
		avgTime = 0;
		trials = 0;
		success = 0;
		if(debug == 1)
			this.debug = true;
		else if (debug == 0)
			this.debug = false;
		if(board == null){
			makeBoard(size);
			genSemiRandConfig();
		}
		for(int i = 0; i < n; i++){
			switch(mode){
			case 1:
				startTime = System.currentTimeMillis();
				steepestHillClimbing();
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			case 2:
				startTime = System.currentTimeMillis();
				minConflicts(additional1);
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			case 3:
				startTime = System.currentTimeMillis();
				geneticAlgorithm(additional1,size,additional2);
				endTime = System.currentTimeMillis();
				time = endTime - startTime;
				totalTime += time;
				break;
			default:
				continue;
			}
			avgTime = totalTime/n;
			board = new Board(size);
			queens.clear();
			map.clear();
			if(boardQ != null)
				boardQ.clear();
			genSemiRandConfig();
		}
	}
	
	public String getReport(){
		String toReturn;
		toReturn = "Trials: "+(int)trials+"\nSuccess Rate: "+(double)(success/trials)*100+"%";
		return toReturn;
	}
	
	private void replaceQ(Tile variable, Tile value) {
		removeQ(variable);
		placeQ(value);
	}

	
	public boolean checkQ(int x, int y){
		return board.getTile(x, y).hasQ();
	}

	public void placeQ(int x, int y){
		if(board.getTile(x, y).hasQ())
			return;
		else{
			board.getTile(x, y).placeQ();
			queens.add(x, new Coords(x,y));
		}
	}
	
	private void placeQ(Tile value) {
		placeQ(value.getX(), value.getY());
	}
	
	public void placeQ(Node current, int x, int y){
		if(current.board.getTile(x, y).hasQ())
			return;
		else{
			current.board.getTile(x, y).placeQ();
			current.queens.add(x, new Coords(x,y));
		}
	}
	
	public void removeQ(int x, int y){
		if(board.getTile(x, y).hasQ()){
			board.getTile(x, y).removeQ();
			queens.remove(x);
			return;
		}
		else
			return;
	}
	
	private void removeQ(int x) {
		@SuppressWarnings("unused")
		Coords coords;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(x, i).hasQ()){
				coords = new Coords(x,i);
				board.getTile(x, i).removeQ();
				queens.remove(x);
				return;
			}
		}
	}
	
	private void removeQ(Tile variable) {
		removeQ(variable.getX());
	}

	
	public int getAllAttackers(){
		int allAttackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			for(int j = 0; j < board.getSize(); j++){
				if(board.getTile(i,j).hasQ())
					allAttackers += getAttackers(i,j);
			}
		}
		map.clear();
		return allAttackers;
	}
	
	private int getAttackers(int x, int y){
		int toReturn = 0;
		toReturn += getColAtkers(x, y);
		toReturn += getRowAtkers(x, y);
		toReturn += getDiagAtkers(x, y);
		board.getTile(x,y).setValue(toReturn);
		return toReturn;
	}
	
	private int getDiagAtkers(int x, int y) {
		int i = x; int j = y;
		int attackers = 0;
		while(i < board.getSize() && j < board.getSize()){ //downright
			if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i++;
			j++;
		}
		i = x; j = y;
		while(i < board.getSize() && j >= 0){ //downleft
			if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i++;
			j--;
		}
		i = x; j = y;
		while(i >= 0 && j < board.getSize()){ //upright
		if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
			attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i--;
			j++;
		}
		i = x; j = y;
		while(i >= 0 && j >= 0){ //upleft
			if(board.getTile(i, j).hasQ() && (i != x && j != y) && !counted(board.getTile(x, y), board.getTile(i, j)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i, j));
			i--;
			j--;
		}
		return attackers;
	}

	private int getRowAtkers(int x, int y) {
		int attackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(x, i).hasQ() && i != y && !counted(board.getTile(x, y), board.getTile(x,i)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(x,i));
		}
		return attackers;
	}

	private int getColAtkers(int x, int y) {
		int attackers = 0;
		for(int i = 0; i < board.getSize(); i++){
			if(board.getTile(i, y).hasQ() && i != x && !counted(board.getTile(x, y), board.getTile(i,y)))
				attackers = incAttackers(attackers, board.getTile(x, y), board.getTile(i,y));
		}
		return attackers;
	}
	
	private int incAttackers(int count, Tile uno, Tile dos){
		count++;
		if(map.get(uno) != null && map.get(dos) != null){
			if(!map.get(uno).contains(dos) && !map.get(dos).contains(uno)){
				map.get(uno).add(dos);
				map.get(dos).add(uno);
			}
		}else if(map.get(uno) == null && map.get(dos) != null){
			map.put(uno, new ArrayList<Tile>());
			map.get(uno).add(dos);
			map.get(dos).add(uno);
		}else if(map.get(dos) == null && map.get(uno) != null){
			map.put(dos, new ArrayList<Tile>());
			map.get(uno).add(dos);
			map.get(dos).add(uno);
		}else{
			map.put(uno, new ArrayList<Tile>());
			map.put(dos, new ArrayList<Tile>());
			map.get(uno).add(dos);
			map.get(dos).add(uno);
		}
		return count;
	}
	
	private boolean counted(Tile uno, Tile dos){
		if(map.isEmpty())
			return false;
		else if(map.containsKey(uno))
			if(map.get(uno).contains(dos))
				return true;
			else
				return false;
		else
			return false;
	}
	
	public Node steepestHillClimbing(){
		Node current;
		Node neighbor;
		current = new Node(board, getAllAttackers(), (ArrayList<Coords>) queens);
		boardQ = new PriorityQueue<Node>();
		while(true){
			neighbor = genNeighbor(current);
			if(neighbor.value >= current.value){
				trials++;
				if(current.value == 0){
					success++;
					if(debug)
						System.out.println(current.board.toString());
				}
				return current;
			}
			current = neighbor;
		}
	}
	
	private Node genNeighbor(Node current) {
		@SuppressWarnings("unused")
		Coords coords;
		Node copy = new Node(current);
		for(int i = 0; i < copy.board.getSize(); i++){
			for(int j = 0; j < copy.board.getSize(); j++){
				coords = new Coords(i,j);
				if(!copy.board.getTile(i, j).hasQ()){
					this.board = copy.board;			//focus on board
					this.queens = copy.queens;			//get list of queens for that board
					removeQ(i);							//remove queen in row i
					placeQ(i,j);						//place queen in coord i,j
					copy.setValue(getAllAttackers());	//get new value
					boardQ.add(copy);					//enqueue copy node
					//System.out.println(copy.toString());
					copy = new Node(current);			//make new copy
				}
			}
		}
		Node best = boardQ.poll();
		return best;
	}
	
	
	public Node minConflicts(int maxSteps){
		trials++;
		Node current;
		Tile variable;
		Tile value;
		boolean conflicted = false;
		Random rand = new Random();
		current = new Node(board, getAllAttackers(), (ArrayList<Coords>) queens);
		for(int i = 0; i < maxSteps; i++){
			if(current.value == 0){
				success++;
				if(debug)
					System.out.println(board.toString());
				return current;
			}
			do{
				variable = board.getTile(queens.get(Math.abs(rand.nextInt()%queens.size())));
				if(getThreats(variable) > 0)
					conflicted = true;
				else
					conflicted = false;
			}while(!conflicted);
			value = minimize(variable);
			replaceQ(variable, value);
			variable = value;
			current.update(board, getAllAttackers(), (ArrayList<Coords>) queens);
			conflicted = false;
		}
		return current;
	}
	
	
	private Tile minimize(Tile variable) {
		List<Tile> minimums = new ArrayList<Tile>();
		int min = getAttackers(variable.getX(),variable.getY());
		Random rand = new Random();
		Tile focus;
		for(int i = 0; i < board.getSize(); i++){
			focus = board.getTile(variable.getX(), i);
			if(i == variable.getY()){
				continue;
			}else if(getThreats(focus) < min){
				min = getThreats(focus);
				minimums = new ArrayList<Tile>();
				minimums.add(focus);
			} else if (getThreats(focus) == min){
				minimums.add(focus);
			}
		}
		if(!minimums.isEmpty())
			return minimums.get(Math.abs(rand.nextInt()%minimums.size()));
		else
			return variable;
	}

	private int getThreats(Tile variable) {
		int x; int y; int toReturn = 0;
		x = variable.getX();
		y = variable.getY();
		toReturn += getColAtkers(x,y);
		toReturn += getDiagAtkers(x,y);
		variable.setValue(toReturn);
		map.clear();
		return toReturn;
	}
	
	public Genotype geneticAlgorithm(int popSize, int boardSize, double mutation){
		List<Genotype> population;
		population = initPop(popSize, boardSize);
		population.sort(null);
		Genotype x; Genotype y; Genotype child;
		boolean satisfied = false;
		Random rand = new Random();
		double mutafactor;
		while(!satisfied){
			List<Genotype> newPopulation = new ArrayList<Genotype>(popSize);
			for(int i = 0; i < popSize; i++){
				x = randomSelect(population);
				y = randomSelect(population);
				child = reproduce(x,y);
				mutafactor = Math.abs(rand.nextDouble()%1);
				if(mutafactor < mutation)
					child = mutate(child);
				newPopulation.add(child);
				newPopulation.sort(null);
			}
			population = newPopulation;
			setFitnessValues((ArrayList<Genotype>) population);
			population.sort(null);
			if(population.get(0).getFriendlies() == (boardSize*(boardSize-1))/2)
				satisfied = true;
		}
		if(!debug)
			System.out.println(population.get(0).toString());
		if(debug)
			System.out.println(population.get(0).getBoard().toString());
		return population.get(0);
	}
	
//	private void printPopulation(ArrayList<Genotype> pop, int popSize) {
//		for(int i = 0; i < popSize; i++){
//			System.out.println(pop.get(i).getStrain() +"\t"+ pop.get(i).getFitness() + "\t" + pop.get(i).getFriendlies());
//		}
//		System.out.println();
//	}

	private Genotype mutate(Genotype child) {
		Random rand = new Random();
		int chemicalX = Math.abs(rand.nextInt()%child.getSize());
		int placement = Math.abs(rand.nextInt()%child.getSize());
		Genotype toReturn;
		StringBuilder concoction = new StringBuilder(child.getStrain());
		concoction.replace(placement, placement+1, Integer.toString(chemicalX));
		toReturn = new Genotype(concoction.toString());
		toReturn.setFriendlies(evalFriendlies(toReturn.getBoard(),toReturn.getQueens()));
		return toReturn;
	}

	private Genotype reproduce(Genotype x, Genotype y) {
		int n = x.getStrain().length();
		Random rand = new Random();
		int c = Math.abs(rand.nextInt()%n);
		Genotype toReturn;
		StringBuilder splice = new StringBuilder("");
		splice.append(x.getStrain(),0,c);
		splice.append(y.getStrain(),c,n);
		toReturn = new Genotype(splice.toString());
		toReturn.setFriendlies(evalFriendlies(toReturn.getBoard(),toReturn.getQueens()));
		return toReturn;
	}

	private Genotype randomSelect(List<Genotype> population) {
		double sum = 0;
		double[] accFF = new double[population.size()];
		Random rand = new Random();
		double divine;
		int choice = -1;
		setFitnessValues((ArrayList<Genotype>) population);
		population.sort(null);
		for(int i = 0; i < population.size(); i++){
			sum += population.get(i).getFitness();
			accFF[i] = sum;
		}
		divine = rand.nextDouble()%1;
		for(int i = 0; i < accFF.length; i++){
			if(divine < accFF[i]){
				choice = i;
				break;
			}
		}
		return population.get(choice);
	}

	private ArrayList<Genotype> initPop(int popSize, int boardSize){
		List<Genotype> population = new ArrayList<Genotype>(popSize);
		for(int i = 0; i < popSize; i++){
			makeBoard(boardSize);
			genSemiRandConfig();
			Genotype add = new Genotype(board);
			add.setFriendlies(evalFriendlies(add.getBoard(), add.getQueens()));
			population.add(add);
		}
		setFitnessValues((ArrayList<Genotype>) population);
		return (ArrayList<Genotype>) population;
	}
	
	
	private void setFitnessValues(ArrayList<Genotype> population) {
		int sum = 0;
		for(Genotype geno : population){
			sum += geno.getFriendlies();
		}
		for(Genotype geno : population){
			geno.setFitness((double) geno.getFriendlies()/sum);
		}
		
	}

	private int evalFriendlies(Board givenBoard, ArrayList<Tile> givenList){
		map.clear();
		int friendlies = 0;
		for(int i = 0; i < givenList.size(); i++){
			for(int j = i+1; j < givenList.size(); j++){
				if(!isHostile(givenList.get(i),givenList.get(j),givenBoard.getSize()))
					friendlies++;
			}
		}
		return friendlies;
	}

	private boolean isHostile(Tile tile, Tile tile2, int size) {
		int x; int y;
		x = tile.getX();
		y = tile.getY();
		while(x > 0 | y > 0){ //go up left
			x--;
			y--;
		}
		while(x < size+1 | y < size+1){ //iterate down right
			if(tile2.getX() == x && tile2.getY() == y)
				return true;
			x++;
			y++;
		}
		x = tile.getX();
		y = tile.getY();
		while(x < size+1 | y > 0){ //go down left
			x++;
			y--;
		}
		while(x > 0 | y < size+1){//iterate up right
			if(tile2.getX() == x && tile2.getY() == y)
				return true;
			x--;
			y++;
		}
		x = tile.getX();
		y = tile.getY();
		for(int i = 0; i < size; i++){
			if(tile2.getX() == x && tile2.getY() == i)
				return true;
		}
		for(int i = 0; i < size; i++){
			if(tile2.getX() == i && tile2.getY() == y)
				return true;
		}
		return false;
	}

	public long getTime() {
		return this.avgTime;
	}
	
}
