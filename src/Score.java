public class Score implements Comparable<Score> {
	
	String name;
	int score;
	
	public Score(String line) {
		
		String[] arr = line.split(",");
		name = arr[0].trim();
		score = Integer.parseInt(arr[1].trim());
	}
	
	public String toString() {
		
		return name + ": " + score;
	}
	
	@Override
	public int compareTo(Score o) {
		return o.score - score;
	}
}
