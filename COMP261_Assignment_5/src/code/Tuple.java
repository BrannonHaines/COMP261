package code;

public class Tuple {
	public int offset;
	public int length;
	public Character nextCharacter;

	public Tuple(int offset, int length, char nextCharacter){
		this.offset = offset;
		this.length = length;
		this.nextCharacter = nextCharacter;
	}

	@Override
	public String toString() {
		return "[" + offset + ", " + length + ", " + nextCharacter + "]";
	}
}
