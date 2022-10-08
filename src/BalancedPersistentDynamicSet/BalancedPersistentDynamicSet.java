package BalancedPersistentDynamicSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

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
        balancedPersistentDynamicSet.add(1);
        balancedPersistentDynamicSet.add(11);
        balancedPersistentDynamicSet.add(0);
        balancedPersistentDynamicSet.add(30);
        balancedPersistentDynamicSet.add(27);
        balancedPersistentDynamicSet.getLatestVersionTree().print(System.out);
        balancedPersistentDynamicSet.add(29);
        balancedPersistentDynamicSet.getLatestVersionTree().print(System.out);
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
        if(grandparentNode.getRightChildren() == currentNode.getParent()){
            uncleNode = grandparentNode.getLeftChildren();
        }else if(grandparentNode.getLeftChildren() == currentNode.getParent()){
            uncleNode = grandparentNode.getRightChildren();
        }
        switch (uncleNode.getColor()) {
            case Black -> {
                if (currentNode.getParent().getRightChildren() == currentNode) {
                    leftRotation(currentNode);
                } else if (currentNode.getParent().getLeftChildren() == currentNode) {
                    rightRotation(currentNode);
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

    private void rightRotation(RedBlackTreeNode<E> currentNode){
        RedBlackTreeNode<E> grandparentNode = duplicateNode(currentNode.getParent().getParent());
        RedBlackTreeNode<E> parentNode = duplicateNode(currentNode.getParent());
        grandparentNode.setLeftChildren(parentNode.getRightChildren());
        parentNode.setRightChildren(grandparentNode);
        parentNode.setLeftChildren(duplicateNode(currentNode));
        if(!grandparentNode.isRoot()){
            RedBlackTreeNode<E> grandGrandParentNode = grandparentNode.getParent();
            grandGrandParentNode.setLeftChildren(duplicateNode(parentNode));
        }else{
            currentTree.setRoot(parentNode);
        }
        grandparentNode.setNodeColor(NodeColor.Red);
        parentNode.setNodeColor(NodeColor.Black);
        currentNode.setNodeColor(NodeColor.Red);
    }

    private void leftRotation(RedBlackTreeNode<E> currentNode){
        RedBlackTreeNode<E> grandparentNode = duplicateNode(currentNode.getParent().getParent());
        RedBlackTreeNode<E> parentNode = duplicateNode(currentNode.getParent());
        grandparentNode.setRightChildren(parentNode.getLeftChildren());
        parentNode.setLeftChildren(grandparentNode);
        parentNode.setRightChildren(duplicateNode(currentNode));
        if(!grandparentNode.isRoot()){
            RedBlackTreeNode<E> grandGrandParentNode = grandparentNode.getParent();
            grandGrandParentNode.setRightChildren(duplicateNode(parentNode));
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
                otherChildrenNode = duplicateNode(grandparentNode.getRightChildren());
                grandparentNode.setRightChildren(otherChildrenNode);
            }else if(grandparentNode.getRightChildren() == parentNode){
                otherChildrenNode = duplicateNode(grandparentNode.getLeftChildren());
                grandparentNode.setLeftChildren(otherChildrenNode);
            }
            otherChildrenNode.setNodeColor(NodeColor.Black);
            grandparentNode.setNodeColor(NodeColor.Red);
        }
    }

    public void remove(E element){
        remove(new RedBlackTreeNode<>(element));
    }

    public void remove(RedBlackTreeNode<E> removeNode){
        RedBlackTree<E> newTree = new RedBlackTree<E>();
        RedBlackTree<E> previousTree = latestVersionTree;
        RedBlackTreeNode<E> previousTreeRoot = previousTree.getRoot();
        currentTree = newTree;
        if(previousTree.getRoot() == null){
            throw new NoSuchElementException("No such element");
        }else{
            newTree.setRoot(duplicateNode(previousTreeRoot));
        }
        if(previousTreeRoot.getElement() == removeNode.getElement()){
            int numberOfChildren = checkChildren(previousTree.getRoot());
            switch(numberOfChildren){
                case 0:
                    newTree.setRoot(new RedBlackTreeNode<E>());
                    break;
                case 1:
                    if(previousTreeRoot.getRightChildren().getElement() != null){
                        newTree.setRoot(previousTreeRoot.getRightChildren());
                    }else{
                        newTree.setRoot(previousTreeRoot.getLeftChildren());
                    }
                    break;
                case 2:
                    RedBlackTreeNode<E> visitedNode = duplicateNode(previousTreeRoot.getRightChildren());
                    RedBlackTreeNode<E> replaceNode = traverseNode(visitedNode);
                    replaceNode.getParent().setLeftChildren(duplicateNode(replaceNode.getRightChildren()));
                    replaceNode.setLeftChildren(duplicateNode(previousTreeRoot.getLeftChildren()));
                    replaceNode.setRightChildren(visitedNode);
                    newTree.setRoot(replaceNode);
                    break;
            }
        }else{
            newTree.setRoot(duplicateNode(previousTree.getRoot()));
            removeHelper(currentTree.getRoot(), previousTree.getRoot(), removeNode);
        }
        this.addNewVersion(newTree);
    }

    private void removeHelper(RedBlackTreeNode<E> root, RedBlackTreeNode<E> previousRoot, RedBlackTreeNode<E> removeNode) {
        int difference = root.getElement().compareTo(removeNode.getElement());
        if(difference > 0){
            root.setRightChildren(previousRoot.getRightChildren());
            RedBlackTreeNode<E> newNode = duplicateNode(previousRoot.getLeftChildren());
            root.setLeftChildren(newNode);
            newNode.setParent(root);
            removeHelper(root.getLeftChildren(), previousRoot.getLeftChildren(), removeNode);
        }else if(difference < 0){
            root.setLeftChildren(previousRoot.getLeftChildren());
            RedBlackTreeNode<E> newNode = duplicateNode(previousRoot.getRightChildren());
            root.setRightChildren(newNode);
            newNode.setParent(root);
            removeHelper(root.getRightChildren(), previousRoot.getRightChildren(), removeNode);
        }else {
            deleteNode(root, previousRoot);
        }
    }

    private int checkChildren(RedBlackTreeNode<E> removeNode){
        int number = 0;
        if(removeNode.getLeftChildren().getElement() != null){
            number++;
        }
        if(removeNode.getRightChildren().getElement() != null){
            number++;
        }
        return number;
    }

    private void deleteNode(RedBlackTreeNode<E> removeNode, RedBlackTreeNode<E> previousRoot){
        int childrenOfPreviousRoot = checkChildren(previousRoot);
        RedBlackTreeNode<E> parentNode = removeNode.getParent();
        switch (childrenOfPreviousRoot) {
            case 0 -> {
                if (parentNode.getRightChildren() == previousRoot) {
                    parentNode.setRightChildren(new RedBlackTreeNode<E>());
                } else {
                    parentNode.setLeftChildren(new RedBlackTreeNode<E>());
                }
            }
            case 1 -> {
                RedBlackTreeNode<E> childrenNode;
                if (previousRoot.getRightChildren().getElement() != null) {
                    childrenNode = previousRoot.getRightChildren();
                } else {
                    childrenNode = previousRoot.getLeftChildren();
                }
                if (parentNode.getRightChildren().getElement() == previousRoot.getElement()) {
                    parentNode.setRightChildren(childrenNode);
                } else {
                    parentNode.setLeftChildren(childrenNode);
                }
            }
            case 2 -> {
                RedBlackTreeNode<E> replaceNode = traverseNode(duplicateNode(previousRoot));
                System.out.println("");
                replaceNode.setLeftChildren(replaceNode.getRightChildren());
                replaceNode.setRightChildren(removeNode.getRightChildren());
                if(parentNode.getRightChildren() == removeNode){
                    parentNode.setRightChildren(replaceNode);
                }else{
                    parentNode.setLeftChildren(replaceNode);
                }
            }
        }
    }

    private RedBlackTreeNode<E> traverseNode(RedBlackTreeNode<E> root){
        if(root.getLeftChildren().getElement() == null){
            RedBlackTreeNode<E> returnNode = duplicateNode(root);
            returnNode.setParent(root.getParent());
            return returnNode;
        }
        RedBlackTreeNode<E> travelNode = duplicateNode(root.getLeftChildren());
        travelNode.setParent(root);
        return traverseNode(travelNode);
    }

    private void addNewVersion(RedBlackTree<E> newTree){
        versionControl.put(count, newTree);
        count++;
    }

    public RedBlackTreeNode<E> duplicateNode(RedBlackTreeNode<E> node){
        RedBlackTreeNode<E> newNode = new RedBlackTreeNode<E>(node.getElement(), node.getColor());
        if(node.getLeftChildren() != null){
            newNode.setLeftChildren(node.getLeftChildren());
        }
        if(node.getRightChildren() != null) {
            newNode.setRightChildren(node.getRightChildren());
        }
        return newNode;
    }

    public RedBlackTree<E> getVersionTree(int id){
        return versionControl.get(id);
    }

    public RedBlackTree<E> getLatestVersionTree(){
        return latestVersionTree;
    }
}
