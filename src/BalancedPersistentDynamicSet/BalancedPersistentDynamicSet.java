package BalancedPersistentDynamicSet;

import BinarySearchTree.BinarySearchNode;
import BinarySearchTree.BinarySearchTree;

import java.util.HashMap;

public class BalancedPersistentDynamicSet<E extends Comparable<E>> extends BinarySearchTree<E> {

    public HashMap<Integer, RedBlackTree<E>> versionControl;
    public RedBlackTreeNode<E> root;

    public BalancedPersistentDynamicSet() {
        root = null;
    }

    public void add(E element){
        add(new BinarySearchNode<>(element));
    }

    public void add(RedBlackTreeNode<E> newNode){
        if(root == null){
            root = newNode;
        }
    }

    private void addHelp(){

    }
}
