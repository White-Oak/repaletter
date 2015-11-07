package me.oak.imgcolor;

import lombok.RequiredArgsConstructor;

/**
 *
 * @author White Oak
 */
public class BinaryColorTree {

    private Node root;
    private final static int NEAREST_DELTA = 5;

    private int getNearestRec(Node node, int color) {

	int diff = Math.abs(color - node.key);
	if (diff < NEAREST_DELTA) {
	    node.amount--;
	    if (node.amount == 0) {
		remove(node.key);
	    }
	    return node.key;
	}
	return 0;
    }

    private void insert(int key) {
	if (root == null) {
	    root = new Node(key);
	    root.amount++;
	} else {
	    insert(root, key);
	}
    }

    private void insert(Node node, int key) {
	if (key < node.key) {
	    if (node.left != null) {
		insert(node.left, key);
	    } else {
		System.out.println("  Inserted " + key + " to left of "
			+ node.key);
		node.left = new Node(key);
	    }
	} else if (key > node.key) {
	    if (node.right != null) {
		insert(node.right, key);
	    } else {
		System.out.println("  Inserted " + key + " to right of "
			+ node.key);
		node.right = new Node(key);
	    }
	} else {
	    node.amount++;
	}
    }

    private void remove(int key) {
	removeRec(root, key);
    }

    private Node removeRec(Node node, int key) {
	if (node == null) {
	    return null;
	}
	if (key < node.key) {
	    node.left = removeRec(node.left, key);
	} else if (key > node.key) {
	    node.right = removeRec(node.right, key);
	} else {
	    if (node.left == null) {
		return node.right;
	    } else if (node.right == null) {
		return node.left;
	    }

	    Node parent = null;
	    Node temp = node;
	    while (temp.left != null) {
		parent = temp;
		temp = temp.left;
	    }

	    parent.left = removeRec(temp, temp.key);
	    temp.right = node.right;
	    temp.left = node.left;
	    return temp;
	}
	return node;
    }

    @RequiredArgsConstructor static class Node {

	Node left;

	Node right;

	final int key;
	int amount;
    }
}
