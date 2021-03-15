package code;
/**
 * A new KMP instance is created for every substring search performed. Both the
 * pattern and the text are passed to the constructor and the search method. You
 * could, for example, use the constructor to create the match table and the
 * search method to perform the search itself.
 */
public class KMP {
	
	private int[] matchTable;
	private int patternLength;
	private int textLength;

	public KMP(String pattern, String text) {
		patternLength = pattern.length();
		textLength = text.length();
		matchTable = new int[patternLength];
		matchTable[0] = -1;
		matchTable[1] = 0;
		int matchTablePos = 2;
		int patternPos = 0;
		while(matchTablePos < patternLength) {
			if (pattern.charAt(matchTablePos-1) == pattern.charAt(patternPos)) {
				matchTable[matchTablePos] = patternPos+1;
				matchTablePos++;
				patternPos++;
			}
			else if (patternPos > 0) {
				patternPos = matchTable[patternPos];
			}
			else {
				matchTable[matchTablePos] = 0;
				matchTablePos++;
			}
		}
	}

	/**
	 * Perform KMP substring search on the given text with the given pattern.
	 * 
	 * This should return the starting index of the first substring match if it
	 * exists, or -1 if it doesn't.
	 */
	public int search(String pattern, String text) {
		long startTime = System.nanoTime();
		int k = 0;
		int i = 0;
		while((k + i) < textLength) {
			if (pattern.charAt(i) == text.charAt(k + i)) {
				i++;
				if (i == patternLength) {
					long endTime = System.nanoTime();
					long performTime = endTime-startTime;
					System.out.println("KMP Search Time : "+performTime/1000000+"\n");
					return k;
				}
			}
			else if (matchTable[i] == -1) {
				i = 0;
				k = k+i+1;
			}
			else {
				k = k+i-matchTable[i];
				i = matchTable[i];
			}
		}
		long endTime = System.nanoTime();
		long performTime = endTime-startTime;
		System.out.println("KMP Search Time : "+performTime/1000000+"\n");
		return -1;
	}
}
