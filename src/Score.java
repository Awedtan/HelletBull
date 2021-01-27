public class Score implements Comparable<Score> {
	// Class for player high scores
	
	String name;
	int score;
	
	public Score(String line) {
		// Creates score from a string (name, score)
		
		String[] arr = line.split(",");
		name = arr[0].trim();
		score = Integer.parseInt(arr[1].trim());
	}
	
	public String toString() {
		
		return name + ": " + score;
	}
	
	@Override
	public int compareTo(Score o) {
		// Sorts highest to lowest
		
		return o.score - score;
	}
}
