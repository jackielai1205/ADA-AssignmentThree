package BalancedPersistentDynamicSet;

import BinarySearchTree.BinarySearchNode;

public class RedBlackTreeNode<E extends Comparable<E>> extends BinarySearchNode<E> {

    private Color color;

    public RedBlackTreeNode(E element) {
        super(element);
        this.color = Color.Black;
    }

    public Color getColor() {
        return color;
    }

    public void toggleColor() {
        this.color = (this.color == Color.Red) ? Color.Black : Color.Red;
    }

    enum Color{
        Red,
        Black
    }
}
