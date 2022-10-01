package BalancedPersistentDynamicSet;

import BinarySearchTree.BinarySearchTree;

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

    void printGivenLevel(RedBlackTreeNode<E> root, int level)
    {
        if (root == null) {
            System.out.print("null ");
            return;
        }
        if (level == 1) {
            System.out.print(root.getElement() + " ");
        }
        else if (level > 1) {
            printGivenLevel(root.getLeftChildren(), level - 1);
            printGivenLevel(root.getRightChildren(), level - 1);
        }
    }

    int height(RedBlackTreeNode<E> root)
    {
        if (root == null)
            return 0;
        else {
            /* compute height of each subtree */
            int lheight = height(root.getLeftChildren());
            int rheight = height(root.getRightChildren());

            /* use the larger one */
            if (lheight > rheight)
                return (lheight + 1);
            else
                return (rheight + 1);
        }
    }

    /* function to print level order traversal of tree*/
    public void printLevelOrder()
    {
        int h = height(root);
        int i;
        for (i = 1; i <= h; i++) {
            printGivenLevel(root, i);
            System.out.print(System.lineSeparator());
        }
    }
}
