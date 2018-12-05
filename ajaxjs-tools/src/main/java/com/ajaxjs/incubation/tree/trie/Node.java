package com.ajaxjs.incubation.tree.trie;

import java.util.LinkedList;

/**
 * http://orchome.com/979
 * 
 * @author Frank Cheung
 *
 */
public class Node {
	char content; // the character in the node
	boolean isEnd; // whether the end of the words
	int count; // the number of words sharing this character
	LinkedList<Node> childList; // the child list

	public Node(char c) {
		childList = new LinkedList<>();
		isEnd = false;
		content = c;
		count = 0;
	}

	public Node subNode(char c) {
		if (childList != null) {
			for (Node eachChild : childList) {
				if (eachChild.content == c) {
					return eachChild;
				}
			}
		}
		
		return null;
	}
}
