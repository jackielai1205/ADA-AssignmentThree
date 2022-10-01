package BalancedPersistentDynamicSet;

public class RedBlackTreeNode<E extends Comparable<E>> {

    private NodeColor nodeColor;
    private boolean isRoot;
    private final E element;
    private RedBlackTreeNode<E> leftChildren;
    private RedBlackTreeNode<E> rightChildren;
    private RedBlackTreeNode<E> parent;

    public RedBlackTreeNode(E element) {
        this.nodeColor = NodeColor.Black;
        this.element = element;
        this.leftChildren = new RedBlackTreeNode<E>();
        this.rightChildren = new RedBlackTreeNode<E>();
        this.parent = null;
    }

    public RedBlackTreeNode(E element, NodeColor nodeColor){
        this.nodeColor = nodeColor;
        this.element = element;
        this.leftChildren = new RedBlackTreeNode<E>();
        this.rightChildren = new RedBlackTreeNode<E>();
        this.isRoot = false;
    }

    public RedBlackTreeNode(){
        this.element = null;
        this.nodeColor = NodeColor.Black;
    }

    public NodeColor getColor() {
        return nodeColor;
    }

    public void setNodeColor(NodeColor nodeColor) {
        this.nodeColor = nodeColor;
    }

    public NodeColor oppositeColor(){
        return (this.nodeColor == NodeColor.Black)? NodeColor.Red : NodeColor.Black;
    }

    public RedBlackTreeNode<E> getLeftChildren() {
        return leftChildren;
    }

    public void setLeftChildren(RedBlackTreeNode<E> leftChildren) {
        leftChildren.parent = this;
        this.leftChildren = leftChildren;
    }

    public RedBlackTreeNode<E> getRightChildren() {
        return rightChildren;
    }

    public void setRightChildren(RedBlackTreeNode<E> rightChildren) {
        rightChildren.parent = this;
        this.rightChildren = rightChildren;
    }

    public E getElement() {
        return element;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public RedBlackTreeNode<E> getParent() {
        return parent;
    }

    public void setParent(RedBlackTreeNode<E> parent) {
        this.parent = parent;
    }

    public String toString(){
        return this.element == null ? "null" : this.element.toString();
    }
}
