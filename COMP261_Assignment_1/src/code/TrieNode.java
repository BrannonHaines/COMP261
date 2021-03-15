package code;

import java.util.List;
import java.util.Map;

public class TrieNode {
	
	//Fields
	private char ch;
	private List<Stop> stops;
	private Map<Character, TrieNode> children;
	private TrieNode parent;
	
	/**
	 * Constructor for the root of the Trie
	 */
	public TrieNode() {
		
	}
	
	/**
	 * Constructor for child TrieNode
	 */
	public TrieNode(char ch, TrieNode parent) {
		this.ch = ch;
		this.parent = parent;
	}
	
	public void add(char[] word, Stop stop) {
		for (char c : word) {
			if (!children.containsKey(c)) {
				TrieNode child = new TrieNode(c, this);
				children.put(c, child);
			}
		}
	}
}
