package BalancedPersistentDynamicSet;

import BinarySearchTree.BinarySearchTree;

public class RedBlackTree<E extends Comparable<E>> extends BinarySearchTree<E> {

    public RedBlackTreeNode<E> root;

    public RedBlackTree() {
        root = null;
    }
}
