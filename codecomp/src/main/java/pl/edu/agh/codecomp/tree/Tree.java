package pl.edu.agh.codecomp.tree;

class Tree<T> {

    private Node<T> root;

    public Tree() {
        root = null;
    }

    public Tree(Node<T> root) {
        this.root = root;
    }
}
