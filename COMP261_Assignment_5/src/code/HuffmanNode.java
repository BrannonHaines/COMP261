package code;

public class HuffmanNode implements Comparable<HuffmanNode>{
	
	public char character;
    public int freq;
    public HuffmanNode parent;
    public HuffmanNode leftChild;
    public HuffmanNode rightChild;
    public String encoding = "";
    
    /**
     * Constructor for Parent HuffmanNode
     * @param leftChild
     * @param rightChild
     * @author Brannon Haines
     */
    public HuffmanNode(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }
    
    /**
     * Constructor for Leaf HuffmanNode
     * @param character
     * @param freq
     * @author Brannon Haines
     */
    public HuffmanNode(char character, int freq) {
        this.character = character;
        this.freq = freq;
    }
    
    // Getters
    public char getCharacter() {
    	return character;
    }
    
    public HuffmanNode getLeftChild() {
    	return leftChild;
    }
    
    public HuffmanNode getRightChild() {
    	return rightChild;
    }
    
    public String getEncoding() {
        return encoding;
    }
    
    // Setters
    public void setParent(HuffmanNode parent) {
    	this.parent = parent;
    }
    
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
    
    public void setFreq(HuffmanNode leftChild, HuffmanNode rightChild) {
        freq = leftChild.freq + rightChild.freq;
    }
    
    @Override
    public int compareTo(HuffmanNode other) {
        return freq - other.freq;
    }

    /**@Override
    public String toString() {
        return "HuffmanCodingNode{character =" + character +", freq=" + freq +", coding='" + encoding + '\'' +'}';
    }*/
}
