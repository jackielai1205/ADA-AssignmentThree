package BalancedPersistentDynamicSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BalancedPersistentDynamicSet<E extends Comparable<E>> {

    private final HashMap<Integer, RedBlackTree<E>> versionControl;
    private RedBlackTree<E> latestVersionTree;
    private int count;
    private RedBlackTree<E> currentTree;

    public BalancedPersistentDynamicSet() {
        versionControl = new HashMap<>();
        latestVersionTree = new RedBlackTree<E>();
        currentTree = new RedBlackTree<E>();
        count = 1;
    }

    public static void main(String[] args){
        BalancedPersistentDynamicSet<Integer> balancedPersistentDynamicSet = new BalancedPersistentDynamicSet<Integer>();
        balancedPersistentDynamicSet.add(20);
        balancedPersistentDynamicSet.add(10);
        balancedPersistentDynamicSet.add(30);
        balancedPersistentDynamicSet.add(27);
        balancedPersistentDynamicSet.add(40);
        balancedPersistentDynamicSet.add(45);
        balancedPersistentDynamicSet.add(25);
        balancedPersistentDynamicSet.add(21);
//        balancedPersistentDynamicSet.add(16);
//        balancedPersistentDynamicSet.getVersionTree(1).printLevelOrder();
        balancedPersistentDynamicSet.getLatestVersionTree().printLevelOrder();
    }

    public void add(E element){
        add(new RedBlackTreeNode<>(element));
    }

    public void add(RedBlackTreeNode<E> newNode){
        RedBlackTree<E> newTree = new RedBlackTree<E>();
        currentTree = newTree;
        List<RedBlackTreeNode<E>> visitedNodes = new ArrayList<>();
        if(latestVersionTree.getRoot() == null){
            newTree.setRoot(newNode);
            newNode.setRoot(true);
        }else{
            RedBlackTreeNode<E> newRootNode = new RedBlackTreeNode<E>(latestVersionTree.getRoot().getElement(), NodeColor.Black);
            newTree.setRoot(newRootNode);
            newRootNode.setRoot(true);
            newNode.setNodeColor(NodeColor.Red);
            addHelper(newTree.getRoot(), latestVersionTree.getRoot(), newNode, visitedNodes);
        }
        addNewVersion(newTree);
        latestVersionTree = newTree;
    }

    private void addHelper(RedBlackTreeNode<E> currentTreeNode, RedBlackTreeNode<E> currentPreviousTreeNode, RedBlackTreeNode<E> newNode, List<RedBlackTreeNode<E>> visitedNodes){
        visitedNodes.add(currentTreeNode);
        int difference = currentPreviousTreeNode.getElement().compareTo(newNode.getElement());
        if(difference > 0){
            currentTreeNode.setRightChildren(currentPreviousTreeNode.getRightChildren());
            if(currentPreviousTreeNode.getLeftChildren().getElement() == null){
                currentTreeNode.setLeftChildren(newNode);
                newNode.setNodeColor(NodeColor.Red);
                checkBalance(visitedNodes, newNode);
            }else{
                currentTreeNode.setLeftChildren(duplicateNode(currentPreviousTreeNode.getLeftChildren()));
                addHelper(currentTreeNode.getLeftChildren(), currentPreviousTreeNode.getLeftChildren(), newNode, visitedNodes);
            }
        }else if(difference < 0){
            currentTreeNode.setLeftChildren(currentPreviousTreeNode.getLeftChildren());
            if(currentPreviousTreeNode.getRightChildren().getElement() == null){
                currentTreeNode.setRightChildren(newNode);
                newNode.setNodeColor(NodeColor.Red);
                checkBalance(visitedNodes, newNode);
            }else{
                currentTreeNode.setRightChildren(duplicateNode(currentPreviousTreeNode.getRightChildren()));
                addHelper(currentTreeNode.getRightChildren(), currentPreviousTreeNode.getRightChildren(), newNode, visitedNodes);
            }
        }
    }

    public void checkBalance(List<RedBlackTreeNode<E>> visitedNodes, RedBlackTreeNode<E> currentNode){
        if(visitedNodes.isEmpty()){
            return;
        }
        if(visitedNodes.get(visitedNodes.size() - 1).getColor() == NodeColor.Red && currentNode.getColor() == NodeColor.Red){
            balanceTree(visitedNodes, currentNode);
        }else{
            checkBalance(visitedNodes.subList(0, visitedNodes.size() - 1), visitedNodes.get(visitedNodes.size() - 1));
        }
    }

    public void balanceTree(List<RedBlackTreeNode<E>> visitedNodes, RedBlackTreeNode<E> currentNode){
        int grandparentNodeIndex = visitedNodes.size() - 2;
        if(grandparentNodeIndex < 0){
            return;
        }
        RedBlackTreeNode<E> uncleNode = new RedBlackTreeNode<E>();
        RedBlackTreeNode<E> grandparentNode = visitedNodes.get(grandparentNodeIndex);
        if(currentNode.getParent().getRightChildren() == currentNode){
            uncleNode = grandparentNode.getLeftChildren();
        }else if(currentNode.getParent().getLeftChildren() == currentNode){
            uncleNode = grandparentNode.getRightChildren();
        }
        switch (uncleNode.getColor()) {
            case Black -> {
                if (currentNode.getParent().getRightChildren() == currentNode) {
                    leftRotation(visitedNodes, currentNode);
                } else if (currentNode.getParent().getLeftChildren() == currentNode) {
                    rightRotation(visitedNodes, currentNode);
                }
                if (visitedNodes.size() - 3 >= 0) {
                    checkBalance(visitedNodes.subList(0, visitedNodes.size() - 3), visitedNodes.get(visitedNodes.size() - 3));
                }
            }
            case Red -> {
                reprintColor(visitedNodes, currentNode);
                visitedNodes.get(0).setNodeColor(NodeColor.Black);
                checkBalance(visitedNodes.subList(0, visitedNodes.size() - 1), visitedNodes.get(visitedNodes.size() - 1));
            }
        }
    }

    private void rightRotation(List<RedBlackTreeNode<E>> visitedNodes, RedBlackTreeNode<E> currentNode){
        RedBlackTreeNode<E> grandparentNode = visitedNodes.get(visitedNodes.size() - 2);
        RedBlackTreeNode<E> parentNode = visitedNodes.get(visitedNodes.size() - 1);
        grandparentNode.setLeftChildren(parentNode.getRightChildren());
        parentNode.setRightChildren(grandparentNode);
        parentNode.setLeftChildren(currentNode);
        if((visitedNodes.size() - 3) >= 0){
            RedBlackTreeNode<E> grandGrandParentNode = visitedNodes.get(visitedNodes.size() - 3);
            grandGrandParentNode.setLeftChildren(parentNode);
        }else{
            currentTree.setRoot(parentNode);
        }
        grandparentNode.setNodeColor(NodeColor.Red);
        parentNode.setNodeColor(NodeColor.Black);
        currentNode.setNodeColor(NodeColor.Red);
    }

    private void leftRotation(List<RedBlackTreeNode<E>> visitedNodes, RedBlackTreeNode<E> currentNode){
        RedBlackTreeNode<E> grandparentNode = visitedNodes.get(visitedNodes.size() - 2);
        RedBlackTreeNode<E> parentNode = visitedNodes.get(visitedNodes.size() - 1);
        grandparentNode.setRightChildren(parentNode.getLeftChildren());
        parentNode.setLeftChildren(grandparentNode);
        parentNode.setRightChildren(currentNode);
        if((visitedNodes.size() - 3) >= 0){
            RedBlackTreeNode<E> grandGrandParentNode = visitedNodes.get(visitedNodes.size() - 3);
            grandGrandParentNode.setRightChildren(parentNode);
        }else{
            currentTree.setRoot(parentNode);
        }
        grandparentNode.setNodeColor(NodeColor.Red);
        parentNode.setNodeColor(NodeColor.Black);
        currentNode.setNodeColor(NodeColor.Red);
    }

    private void reprintColor(List<RedBlackTreeNode<E>> visitedNodes, RedBlackTreeNode<E> currentNode){
        if(visitedNodes.size() == 1){
            return;
        }
        RedBlackTreeNode<E> grandparentNode = visitedNodes.get(visitedNodes.size() - 2);
        RedBlackTreeNode<E> parentNode = visitedNodes.get(visitedNodes.size() - 1);
        if(currentNode.getColor() == NodeColor.Red && parentNode.getColor() == NodeColor.Red){
            parentNode.setNodeColor(NodeColor.Black);
            RedBlackTreeNode<E> otherChildrenNode = new RedBlackTreeNode<E>();
            if(grandparentNode.getLeftChildren() == parentNode){
                otherChildrenNode = grandparentNode.getRightChildren();
            }else if(grandparentNode.getRightChildren() == parentNode){
                otherChildrenNode = grandparentNode.getLeftChildren();
            }
            otherChildrenNode.setNodeColor(NodeColor.Black);
            grandparentNode.setNodeColor(NodeColor.Red);
        }
    }


    private void addNewVersion(RedBlackTree<E> newTree){
        versionControl.put(count, newTree);
        count++;
    }

    public RedBlackTreeNode<E> duplicateNode(RedBlackTreeNode<E> node){
        return new RedBlackTreeNode<E>(node.getElement(), node.getColor());
    }

    public RedBlackTree<E> getVersionTree(int id){
        return versionControl.get(id);
    }

    public RedBlackTree<E> getLatestVersionTree(){
        return latestVersionTree;
    }
}
