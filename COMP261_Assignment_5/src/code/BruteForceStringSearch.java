package code;

public class BruteForceStringSearch {
	
	private String pattern;
	private String text;
	
	public BruteForceStringSearch(String pattern, String text) {
		this.pattern = pattern;
		this.text = text;
	}
	
	public int search(String pattern, String text) {
		if (pattern.length() < 1) {
			return -1;
		}
		int diffLength = text.length()-pattern.length();
		long startTime = System.nanoTime();
		for (int j = 0; j < diffLength+1; j++) {
			boolean patternFound = true;
			for (int i = 0; i < pattern.length(); i++) {
				if (pattern.charAt(i) != text.charAt(j+i)){
					patternFound = false;
					break;
				}
			}
			if (patternFound) {
				long endTime = System.nanoTime();
				long performTime = endTime-startTime;
				System.out.println("Brute Force Search Time : "+performTime/1000000+"\n");
				return j;
			}
		}
		long endTime = System.nanoTime();
		long performTime = endTime-startTime;
		System.out.println("Brute Force Search Time : "+performTime/1000000+"\n");
		return -1;
	}
}
