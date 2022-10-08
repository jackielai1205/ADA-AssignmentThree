package BalancedPersistentDynamicSet;

import BinarySearchTree.BinarySearchTree;

import java.io.PrintStream;

public class RedBlackTree<E extends Comparable<E>> {

    private RedBlackTreeNode<E> root;

    public RedBlackTree() {
        root = null;
    }

    public RedBlackTreeNode<E> getRoot() {
        return root;
    }

    public void setRoot(RedBlackTreeNode<E> root) {
        root.setParent(null);
        this.root = root;
    }

    //Print function refer to https://www.baeldung.com/java-print-binary-tree-diagram
    public void traverseNodes(StringBuilder sb, String padding, String pointer, RedBlackTreeNode<E> node,
                              boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.getElement());

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) {
                paddingBuilder.append("│  ");
            } else {
                paddingBuilder.append("   ");
            }

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "└──";
            String pointerLeft = (node.getRightChildren() != null) ? "├──" : "└──";

            traverseNodes(sb, paddingForBoth, pointerLeft, node.getLeftChildren(), node.getRightChildren() != null);
            traverseNodes(sb, paddingForBoth, pointerRight, node.getRightChildren(), false);
        }
    }

    public String traversePreOrder(RedBlackTreeNode<E> root) {

        if (root == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(root.getElement());

        String pointerRight = "└──";
        String pointerLeft = (root.getRightChildren() != null) ? "├──" : "└──";

        traverseNodes(sb, "", pointerLeft, root.getLeftChildren(), root.getRightChildren() != null);
        traverseNodes(sb, "", pointerRight, root.getRightChildren(), false);

        return sb.toString();
    }

    public void print(PrintStream os) {
        os.print(traversePreOrder(root));
    }

}
