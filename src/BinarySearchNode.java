public class BinarySearchNode<E extends Comparable<E>> {

    private final E element;
    private BinarySearchNode<E> leftChildren;
    private BinarySearchNode<E> rightChildren;

    public BinarySearchNode(E element) {
        this.element = element;
        leftChildren = null;
        rightChildren = null;
    }

    public E getElement() {
        return element;
    }

    public BinarySearchNode<E> getLeftChildren() {
        return leftChildren;
    }

    public void setLeftChildren(BinarySearchNode<E> leftChildren) {
        this.leftChildren = leftChildren;
    }

    public BinarySearchNode<E> getRightChildren() {
        return rightChildren;
    }

    public void setRightChildren(BinarySearchNode<E> rightChildren) {
        this.rightChildren = rightChildren;
    }

    public String toString(){
        return this.getElement().toString();
    }
}
