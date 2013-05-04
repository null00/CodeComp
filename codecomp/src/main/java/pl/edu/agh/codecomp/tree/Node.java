package pl.edu.agh.codecomp.tree;

import java.util.LinkedList;

public class Node<K,V> {
	private K key;
	private V value;
	private Node<K,V> parent;
	private LinkedList<Node<K,V>> children;

	public Node() {
		parent = null;
		children = new LinkedList<Node<K,V>>();
	}
	
	public Node(K key, V value) {
        this();
        this.key = key;
        this.value = value;
    }

	public Node(Node<K,V> parent) {
		this();
		this.parent = parent;
	}

	public Node(Node<K,V> parent, K key, V value) {
		this(parent);
        this.key = key;
        this.value = value;
	}

	public Node<K,V> getParent() {
		return parent;
	}

	public void setParent(Node<K,V> parent) {
		this.parent = parent;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

	public int getDegree() {
		return children.size();
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public Node<K,V> addChild(Node<K,V> child) {
		child.setParent(this);
		children.add(child);
		return child;
	}

	public Node<K,V> addChild(K key, V value) {
		Node<K,V> child = new Node<K,V>(this, key, value);
		children.add(child);
		return child;
	}

	public Node<K,V> getChild(int i) {
		return children.get(i);
	}

	public Node<K,V> removeChild(int i) {
		return children.remove(i);
	}

	public void removeChildren() {
		children.clear();
	}

	public LinkedList<Node<K,V>> getChildren() {
		return children;
	}

	public Node<K,V> getLeftMostChild() {
		if (children.isEmpty())
			return null;
		return children.get(0);
	}

	public Node<K,V> getRightSibling() {
		if (parent != null) {
			LinkedList<Node<K,V>> parentsChildren = parent.getChildren();
			int pos = parentsChildren.indexOf(this);
			if (pos < (parentsChildren.size() - 1))
				return parentsChildren.get(pos + 1);
		}
		return null;
	}

	public String toString() {
		return key.toString() + ": " + value.toString();
	}
}
