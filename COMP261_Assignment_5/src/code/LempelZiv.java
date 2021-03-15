package code;

import java.util.ArrayList;

/**
 * A new instance of LempelZiv is created for every run.
 */
public class LempelZiv {
	
	ArrayList<Tuple> tupleList = new ArrayList<>();
	private static final int WINDOW_SIZE = 100;
	
	/**
	 * Take uncompressed input as a text string, compress it, and return it as a
	 * text string.
	 */
	public String compress(String input) {
		StringBuilder outputResult = new StringBuilder();
		int cursor = 0;
		while(cursor < input.length()){
			int stepAhead = 0;
			int lastMatch = -1;
			while(true){
				int start;
				if(cursor<WINDOW_SIZE) { 
					start= 0;
				}
				else {
					start = cursor - WINDOW_SIZE;
				}
				String str = input.substring(start, cursor);
				String patt = input.substring(cursor, cursor + stepAhead);
				int match = str.indexOf(patt);

				if(cursor + stepAhead >= input.length()){
					match = -1;
				}

				if(match > -1){
					lastMatch = match;
					stepAhead++;
				}
				
				else{
					int offset;
					if(lastMatch>-1) {
						offset = str.length() - lastMatch;
					}
					else {
						offset = 0;
					}
					Character nextCharacter = input.charAt(cursor +stepAhead - 1);
					Tuple t = new Tuple(offset, stepAhead - 1, nextCharacter);
					tupleList.add(t);
					outputResult.append(t.toString());
					cursor = cursor + stepAhead;
					break;
				}
			}
		}
		return outputResult.toString();
	}

	/**
	 * Take compressed input as a text string, decompress it, and return it as a
	 * text string.
	 */
	public String decompress(String compressed) {
		StringBuilder outputResult = new StringBuilder();
		int cursor = 0;
		for(Tuple t: tupleList){
			if(t.length == 0 && t.offset == 0){
				outputResult.append(t.nextCharacter);
				cursor++;
			}
			else{
				String str = outputResult.substring(cursor - t.offset, cursor - t.offset + t.length);
				outputResult.append(str);
				cursor = cursor + t.length;

				if (t.nextCharacter != null)	{
					outputResult.append(t.nextCharacter);
					cursor++;
				}
			}
		}
		return outputResult.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't want to. It is called on every run and its return
	 * value is displayed on-screen. You can use this to print out any relevant
	 * information from your compression.
	 */
	public String getInformation() {
		return "";
	}

}