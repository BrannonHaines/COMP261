package code;

import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

public class HuffmanTree {
	
	public HuffmanNode root;
	
    public HuffmanTree() {
    	
    }
    
    public Queue<HuffmanNode> makeTree(Map<Character, Integer> freqMap) {
        Queue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
		for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
			queue.offer(new HuffmanNode(entry.getKey(), entry.getValue()));
		}
		while(queue.size()>1){
			HuffmanNode leftChild = queue.poll();
			HuffmanNode rightChild = queue.poll();
			HuffmanNode parent = new HuffmanNode(leftChild, rightChild);
			leftChild.setParent(parent);
			rightChild.setParent(parent);
			parent.setFreq(leftChild, rightChild);
            queue.offer(parent);
		}
		root = queue.peek();
        return queue;
    }
    
    public HuffmanNode getRoot() {
    	return root;
    }
}
