package code;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
public class HuffmanCoding {
	
	private Map<Character, Integer> freqMap;
	private Map<Character, String> encodingMap;
	private Queue<HuffmanNode> tree;
	private HuffmanNode root;
	
	/**
	 * This would be a good place to compute and store the tree.
	 */
	public HuffmanCoding(String text) {
		Map<Character, Integer> table = new HashMap<Character, Integer>();
		for (int index = 0; index < text.length(); index++) {
			char character = text.charAt(index);
			if (table.keySet().contains(character)) {
				table.put(character, table.get(character) + 1);
			}
			else {
				table.put(character, 1);
			}
		}
		this.freqMap = table;
		HuffmanTree huffmanTree = new HuffmanTree();
		this.root = huffmanTree.getRoot();
		this.tree = huffmanTree.makeTree(freqMap);
		this.encodingMap = createEncodingMap();
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		StringBuilder encodedText = new StringBuilder();
		for(int i = 0; i < text.length(); i++){
			char character = text.charAt(i);
			encodedText.append(encodingMap.get(character));
		}
		return encodedText.toString();
	}
	
	/**
	 * Creates the encoding map
	 * 
	 * @author Brannon Haines
	 */
	public Map<Character,String> createEncodingMap(){
		Map<Character, String> encodingMap = new HashMap<Character, String>();
		Stack<HuffmanNode> nodeStack = new Stack<HuffmanNode>();
		nodeStack.push(root);
		while (!nodeStack.isEmpty()) {
			HuffmanNode topNode = nodeStack.pop();
			HuffmanNode leftChild = topNode.getLeftChild();
			HuffmanNode rightChild = topNode.getRightChild();
			if (leftChild != null) {
				leftChild.setEncoding(topNode.getEncoding() + "0");
				nodeStack.push(leftChild);
			}
			if (rightChild != null){
				rightChild.setEncoding(topNode.getEncoding()+ "1");
				nodeStack.push(rightChild);
			} 
			else {
				encodingMap.put(topNode.getCharacter(), topNode.getEncoding());
			}
		}
		return encodingMap;
	}
	
	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		int i = 0;
		HuffmanNode root = this.root;
		HuffmanNode cursor = this.root;
		char[] characterArray = encoded.toCharArray();
		StringBuilder decodedText = new StringBuilder();
		while (i < characterArray.length) {
			char character = characterArray[i];
			if (character == '0') {
				cursor = cursor.getLeftChild();
				if (cursor.getLeftChild() == null ) {
					decodedText.append(cursor.getCharacter());
					cursor = root;
				}
			}
			else if (character == '1') {
				cursor = cursor.getRightChild();
				if(cursor.getRightChild() == null){
					decodedText.append(cursor.getCharacter());
					cursor = root;
				}
			}
			i++;
		}
		return decodedText.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		return "";
 	}
}
