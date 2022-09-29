package PersistentDynamicSet;

import BinarySearchTree.*;

import java.util.HashMap;

public class PersistentDynamicSet<E extends Comparable<E>> {

    private final HashMap<Integer, BinarySearchTree<E>> versionControl;
    private BinarySearchTree<E> latestVersionTree;
    private int versionId;

    public PersistentDynamicSet() {
        versionControl = new HashMap<>();
        latestVersionTree = new BinarySearchTree<E>();
        versionId = 1;
    }

    public static void main(String[] args){
        PersistentDynamicSet<String> set = new PersistentDynamicSet<String>();
        set.add("dog");
        set.add("cat");
        set.add("owl");
        set.getVersionTree(1).printLevelOrder();
        set.getLatestVersionTree().printLevelOrder();
    }

    //Add
    public void add(E element){
        add(new BinarySearchNode<>(element));
    }

    public void add(BinarySearchNode<E> newNode){
        BinarySearchTree<E> newTree = new BinarySearchTree<E>();
        BinarySearchTree<E> previousTree = latestVersionTree;
        if(previousTree.getRoot() == null){
            newTree.setRoot(newNode);
        }else{
            newTree.setRoot(duplicateNode(previousTree.getRoot()));
            addHelper(newTree.getRoot(), previousTree.getRoot(), newNode);
        }
        addNewVersion(newTree);
        latestVersionTree = newTree;
    }

    private void addHelper(BinarySearchNode<E> currentTreeNode, BinarySearchNode<E> currentPreviousTreeNode, BinarySearchNode<E> newNode){
        int difference = currentPreviousTreeNode.getElement().compareTo(newNode.getElement());
        if(difference > 0){
            currentTreeNode.setRightChildren(currentPreviousTreeNode.getRightChildren());
            if(currentPreviousTreeNode.getLeftChildren() == null){
                currentTreeNode.setLeftChildren(newNode);
            }else{
                currentTreeNode.setLeftChildren(duplicateNode(currentPreviousTreeNode.getLeftChildren()));
                addHelper(currentTreeNode.getLeftChildren(), currentPreviousTreeNode.getLeftChildren(), newNode);
            }
        }else if(difference < 0){
            currentTreeNode.setLeftChildren(currentPreviousTreeNode.getLeftChildren());
            if(currentPreviousTreeNode.getRightChildren() == null){
                currentTreeNode.setRightChildren(newNode);
            }else{
                currentTreeNode.setRightChildren(duplicateNode(currentPreviousTreeNode.getRightChildren()));
                addHelper(currentTreeNode.getRightChildren(), currentPreviousTreeNode.getRightChildren(), newNode);
            }
        }
    }

    //Remove
    public void remove(E element){
        remove(new BinarySearchNode<>(element));
    }

    public void remove(BinarySearchNode<E> removeNode){
        BinarySearchTree<E> previousTreeNode = latestVersionTree;
        BinarySearchTree<E> newTree = new BinarySearchTree<E>();
        if(previousTreeNode.getRoot() == null){
            return;
        }
        newTree.setRoot(duplicateNode(previousTreeNode.getRoot()));
        removeHelper(newTree.getRoot(), previousTreeNode.getRoot(), removeNode, null);
        addNewVersion(newTree);
        latestVersionTree = newTree;
    }

    private BinarySearchNode<E> removeHelper(BinarySearchNode<E> currentTreeNode, BinarySearchNode<E> currentPreviousTreeNode, BinarySearchNode<E> removeNode, Boolean isLeftRemove){
        int difference = currentPreviousTreeNode.getElement().compareTo(removeNode.getElement());
        if(difference > 0){
            if(isLeftRemove != null){
                isLeftRemove = true;
            }
            currentTreeNode.setLeftChildren(duplicateNode(currentPreviousTreeNode.getLeftChildren()));
            currentTreeNode.setRightChildren(currentPreviousTreeNode.getRightChildren());
            currentTreeNode.setLeftChildren(removeHelper(currentTreeNode.getLeftChildren(), currentPreviousTreeNode.getLeftChildren(), removeNode, isLeftRemove));
        }else if(difference < 0){
            if(isLeftRemove != null){
                isLeftRemove = false;
            }
            currentTreeNode.setRightChildren(duplicateNode(currentPreviousTreeNode.getRightChildren()));
            currentTreeNode.setLeftChildren(currentPreviousTreeNode.getLeftChildren());
            currentTreeNode.setRightChildren(removeHelper(currentTreeNode.getRightChildren(), currentPreviousTreeNode.getRightChildren(), removeNode, isLeftRemove));
        }else{
            if(isLeftRemove == null || isLeftRemove){
                return leftRemoveNode(currentPreviousTreeNode);
            }else{
                return rightRemoveNode(currentPreviousTreeNode);
            }
        }
        return currentTreeNode;
    }

    private BinarySearchNode<E> leftRemoveNode(BinarySearchNode<E> removeNode){
        BinarySearchNode<E> currentNode = removeNode;
        BinarySearchNode<E> parentNode = currentNode;
        if(currentNode.getLeftChildren() != null){
            currentNode = currentNode.getLeftChildren();
        }else{
            return currentNode.getRightChildren();
        }
        while(currentNode.getRightChildren() != null){
            parentNode = currentNode;
            currentNode = currentNode.getRightChildren();
        }
        if(currentNode.getLeftChildren() != null){
            parentNode.setRightChildren(currentNode.getLeftChildren());
            currentNode.setLeftChildren(removeNode.getLeftChildren());
        }
        currentNode.setRightChildren(removeNode.getRightChildren());
        removeNode.setRightChildren(null);
        removeNode.setLeftChildren(null);
        return currentNode;
    }

    private BinarySearchNode<E> rightRemoveNode(BinarySearchNode<E> removeNode){
        BinarySearchNode<E> currentNode = removeNode;
        BinarySearchNode<E> parentNode = currentNode;
        if(currentNode.getRightChildren() != null){
            currentNode = currentNode.getRightChildren();
        }else{
            return currentNode.getLeftChildren();
        }
        while(currentNode.getLeftChildren() != null){
            parentNode = currentNode;
            currentNode = currentNode.getLeftChildren();
        }
        if(currentNode.getRightChildren() != null){
            parentNode.setLeftChildren(currentNode.getRightChildren());
            currentNode.setRightChildren(removeNode.getLeftChildren());
        }
        currentNode.setLeftChildren(removeNode.getLeftChildren());
        removeNode.setRightChildren(null);
        removeNode.setLeftChildren(null);
        return currentNode;
    }

    public void addNewVersion(BinarySearchTree<E> newTree){
        versionControl.put(versionId, newTree);
        versionId++;
    }

    public BinarySearchNode<E> duplicateNode(BinarySearchNode<E> node){
        return new BinarySearchNode<E>(node.getElement());
    }

    public BinarySearchTree<E> getVersionTree(int id){
        return versionControl.get(id);
    }

    public BinarySearchTree<E> getLatestVersionTree(){
        return latestVersionTree;
    }
}
