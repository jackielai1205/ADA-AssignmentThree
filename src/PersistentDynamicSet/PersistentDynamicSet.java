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
        PersistentDynamicSet<Integer> set = new PersistentDynamicSet<Integer>();
        set.add(9);
        set.add(5);
        set.add(15);
        set.add(7);
        set.add(6);
        set.add(20);
        set.add(13);
        set.getLatestVersionTree().print(System.out);
        set.remove(9);
        set.getLatestVersionTree().print(System.out);
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
        BinarySearchTree<E> previousTree = latestVersionTree;
        BinarySearchTree<E> newTree = new BinarySearchTree<E>();
        if(previousTree.getRoot() == null){
            return;
        }
        if(previousTree.getRoot().getElement() == removeNode.getElement()){
            newTree.setRoot(removeRootNode(previousTree.getRoot()));
        }else{
            newTree.setRoot(duplicateNode(previousTree.getRoot()));
            removeHelper(newTree.getRoot(), previousTree.getRoot(), removeNode, null);
        }
        addNewVersion(newTree);
        latestVersionTree = newTree;
    }

    private BinarySearchNode<E> removeRootNode(BinarySearchNode<E> referenceNode){
        int numberOfChildren = 0;
        if(referenceNode.getRightChildren() != null){
            numberOfChildren++;
        }
        if(referenceNode.getLeftChildren() != null){
            numberOfChildren++;
        }
        switch (numberOfChildren){
            case 0:
                return null;
            case 1:
                if(referenceNode.getRightChildren() != null){
                    return referenceNode.getRightChildren();
                }
                if(referenceNode.getLeftChildren() != null){
                    return referenceNode.getLeftChildren();
                }
            case 2:
                BinarySearchNode<E> newTreeRoot = duplicateNode(referenceNode);
                BinarySearchNode<E> newTreeCurrentNode = newTreeRoot;
                BinarySearchNode<E> referenceTreeCurrentNode = referenceNode;

                if(referenceTreeCurrentNode.getLeftChildren() != null){
                    BinarySearchNode<E> newDuplicateNode = duplicateNode(referenceTreeCurrentNode.getLeftChildren());
                    newTreeCurrentNode.setLeftChildren(newDuplicateNode);
                    newTreeCurrentNode.setRightChildren(referenceTreeCurrentNode.getRightChildren());
                    newTreeCurrentNode = newTreeCurrentNode.getLeftChildren();
                    referenceTreeCurrentNode = referenceTreeCurrentNode.getLeftChildren();

                    while(referenceTreeCurrentNode.getRightChildren().getRightChildren() != null){
                        newDuplicateNode = duplicateNode(referenceTreeCurrentNode.getRightChildren());
                        newTreeCurrentNode.setRightChildren(newDuplicateNode);
                        newTreeCurrentNode.setLeftChildren(referenceTreeCurrentNode.getLeftChildren());
                        newTreeCurrentNode = newTreeCurrentNode.getRightChildren();
                        referenceTreeCurrentNode = referenceTreeCurrentNode.getRightChildren();
                    }
                    BinarySearchNode<E> parentNode = newTreeCurrentNode;
                    BinarySearchNode<E> replaceNode = duplicateNode(referenceTreeCurrentNode.getRightChildren());
                    parentNode.setLeftChildren(referenceTreeCurrentNode.getLeftChildren());
                    parentNode.setRightChildren(referenceTreeCurrentNode.getRightChildren().getLeftChildren());
                    replaceNode.setLeftChildren(newTreeRoot.getLeftChildren());
                    replaceNode.setRightChildren(referenceNode.getRightChildren());
                    return replaceNode;
                }else if(referenceTreeCurrentNode.getRightChildren() != null){
                    return referenceTreeCurrentNode.getRightChildren();
                }
        }
        throw new IllegalStateException();
    }

    private BinarySearchNode<E> removeHelper(BinarySearchNode<E> currentTreeNode, BinarySearchNode<E> currentPreviousTreeNode, BinarySearchNode<E> removeNode, Boolean isLeftRemove){
        int difference = currentPreviousTreeNode.getElement().compareTo(removeNode.getElement());
        if(difference > 0){
            currentTreeNode.setLeftChildren(duplicateNode(currentPreviousTreeNode.getLeftChildren()));
            currentTreeNode.setRightChildren(currentPreviousTreeNode.getRightChildren());
            currentTreeNode.setLeftChildren(removeHelper(currentTreeNode.getLeftChildren(), currentPreviousTreeNode.getLeftChildren(), removeNode, isLeftRemove));
        }else if(difference < 0){
            currentTreeNode.setRightChildren(duplicateNode(currentPreviousTreeNode.getRightChildren()));
            currentTreeNode.setLeftChildren(currentPreviousTreeNode.getLeftChildren());
            currentTreeNode.setRightChildren(removeHelper(currentTreeNode.getRightChildren(), currentPreviousTreeNode.getRightChildren(), removeNode, isLeftRemove));
        }else{
            return removeNode(currentPreviousTreeNode);
        }
        return currentTreeNode;
    }

    private BinarySearchNode<E> removeNode(BinarySearchNode<E> referenceNode){
        int numberOfChildren = 0;
        if(referenceNode.getRightChildren() != null){
            numberOfChildren++;
        }
        if(referenceNode.getLeftChildren() != null){
            numberOfChildren++;
        }
        switch (numberOfChildren){
            case 0:
                return null;
            case 1:
                return (referenceNode.getRightChildren() != null)? referenceNode.getRightChildren() : referenceNode.getLeftChildren();
            case 2:
                BinarySearchNode<E> newTreeRoot = duplicateNode(referenceNode);
                BinarySearchNode<E> newTreeCurrentNode = newTreeRoot;
                BinarySearchNode<E> referenceTreeCurrentNode = referenceNode;
                while(referenceTreeCurrentNode.getRightChildren().getRightChildren() != null){
                    BinarySearchNode<E> newDuplicateNode;
                    newDuplicateNode = duplicateNode(referenceTreeCurrentNode.getRightChildren());
                    newTreeCurrentNode.setRightChildren(newDuplicateNode);
                    newTreeCurrentNode.setLeftChildren(referenceTreeCurrentNode.getLeftChildren());
                    newTreeCurrentNode = newTreeCurrentNode.getRightChildren();
                    referenceTreeCurrentNode = referenceTreeCurrentNode.getRightChildren();
                }
                BinarySearchNode<E> parentNode = newTreeCurrentNode;
                BinarySearchNode<E> replaceNode = duplicateNode(referenceTreeCurrentNode.getRightChildren());
                parentNode.setLeftChildren(referenceTreeCurrentNode.getLeftChildren());
                parentNode.setRightChildren(referenceTreeCurrentNode.getRightChildren().getLeftChildren());
                replaceNode.setLeftChildren(newTreeRoot.getLeftChildren());
                replaceNode.setRightChildren(referenceNode.getRightChildren());
                return replaceNode;
        }
        throw new IllegalStateException();
    }


    public void addNewVersion(BinarySearchTree<E> newTree){
        versionControl.put(versionId, newTree);
        versionId++;
    }

    public BinarySearchNode<E> duplicateNode(BinarySearchNode<E> node){
        if(node == null){
            return null;
        }
        return new BinarySearchNode<E>(node.getElement());
    }

    public BinarySearchTree<E> getVersionTree(int id){
        return versionControl.get(id);
    }

    public BinarySearchTree<E> getLatestVersionTree(){
        return latestVersionTree;
    }


}
