package pl.edu.agh.codecomp.tree;

import java.util.LinkedList;

class Node<T> {
	private T data;
	private Node<T> parent;
	private LinkedList<Node<T>> children;

	public Node() {
		parent = null;
		children = new LinkedList<Node<T>>();
	}

	public Node(Node<T> parent) {
		this();
		this.parent = parent;
	}

	public Node(Node<T> parent, T data) {
		this(parent);
		this.data = data;
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node<T> parent) {
		this.parent = parent;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getDegree() {
		return children.size();
	}

	public boolean isLeaf() {
		return children.isEmpty();
	}

	public Node<T> addChild(Node<T> child) {
		child.setParent(this);
		children.add(child);
		return child;
	}

	public Node<T> addChild(T data) {
		Node<T> child = new Node<T>(this, data);
		children.add(child);
		return child;
	}

	public Node<T> getChild(int i) {
		return children.get(i);
	}

	public Node<T> removeChild(int i) {
		return children.remove(i);
	}

	public void removeChildren() {
		children.clear();
	}

	public LinkedList<Node<T>> getChildren() {
		return children;
	}

	public Node<T> getLeftMostChild() {
		if (children.isEmpty())
			return null;
		return children.get(0);
	}

	public Node<T> getRightSibling() {
		if (parent != null) {
			LinkedList<Node<T>> parentsChildren = parent.getChildren();
			int pos = parentsChildren.indexOf(this);
			if (pos < (parentsChildren.size() - 1))
				return parentsChildren.get(pos + 1);
		}
		return null;
	}

	public String toString() {
		return data.toString();
	}
}
