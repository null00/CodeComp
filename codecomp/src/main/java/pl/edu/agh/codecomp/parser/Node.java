package pl.edu.agh.codecomp.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Node implements Iterable<Node> {
	private Node parent;
	private NodeType type;
	private String data;
	private int lineNum;

	protected List<Node> children;

	/**
	 * Creates a new node with the given parent node and node type. It also
	 * takes the line number the node was on in the source file as an argument
	 * to enable easier debugging of scripts.
	 * 
	 * @param type
	 *            the type of node
	 * @param parent
	 *            the parent node
	 * @param lineNum
	 *            the line number
	 */
	public Node(NodeType type, Node parent, int lineNum) {
		this(type, parent, lineNum, null);
	}

	/**
	 * Creates a new node instance with the given parent node, node type and
	 * data string. It also takes the line number the node was on in the source
	 * file as an argument to enable easier debugging of scripts.
	 * 
	 * @param type
	 *            the type of node
	 * @param parent
	 *            the parent node
	 * @param lineNum
	 *            the line number
	 * @param data
	 *            the data of the node
	 */
	public Node(NodeType type, Node parent, int lineNum, String data) {
		this.parent = parent;
		this.type = type;
		this.lineNum = lineNum;
		this.data = data;
		children = new ArrayList<>();
	}

	/**
	 * Get the parent node of this node
	 * 
	 * @return the parent node
	 */
	private Node getParent() {
		return parent;
	}

	/**
	 * Get the node at the given index
	 * 
	 * @param index
	 *            the index
	 * @return the node at the given nodex
	 */
	public Node getChild(int index) {
		return children.get(index);
	}

	/**
	 * Sets the node at the given index
	 * 
	 * @param index
	 *            the index
	 * @param n
	 *            the node
	 */
	public void setChild(int index, Node n) {
		children.set(index, n);
	}

	/**
	 * Adds a new node at the end of the node list
	 * 
	 * @param n
	 *            the node to be added
	 */
	public void addChild(Node n) {
		children.add(n);
	}

	/**
	 * Add a collection of child nodes to this node
	 * 
	 * @param nodes
	 *            the child nodes to be
	 */
	public void addChildren(Collection<Node> nodes) {
		children.addAll(nodes);
	}

	/**
	 * Removes the node at the given index
	 * 
	 * @param index
	 */
	public void removeChild(int index) {
		children.remove(index);
	}

	/**
	 * How many child nodes this node has
	 * 
	 * @return the amount of child nodes of this node
	 */
	public int childCount() {
		return children.size();
	}

	/**
	 * Return the type of node
	 * 
	 * @return what type this node is
	 */
	public NodeType getType() {
		return type;
	}

	/**
	 * Return the data contained in the node, or null if none exists
	 * 
	 * @return the node data
	 */
	public String getData() {
		return data;
	}

	/**
	 * Return the line number of the node
	 * 
	 * @return the line number
	 */
	public int getLineNumber() {
		return lineNum;
	}

	/**
	 * Returns an iterator over the nodes
	 * 
	 * @return an iterator over the nodes
	 */
	@Override
	public Iterator<Node> iterator() {
		return children.iterator();
	}

	/**
	 * Creates a new root node
	 * 
	 * @return the new root node
	 */
	public static Node newRootNode() {
		return new Node(NodeType.ROOT, null, 0, null);
	}
}
