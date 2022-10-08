package BinarySearchTree;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> {

    private BinarySearchNode<E> root;

    public BinarySearchTree() {
        root = null;
    }

    public static void main(String[] args){
        BinarySearchTree<String> tree = new BinarySearchTree<String>();
        tree.add("dog");
        tree.add("cat");
        tree.add("owl");
        tree.print(System.out);
    }

    public BinarySearchNode<E> getRoot() {
        return root;
    }

    public void setRoot(BinarySearchNode<E> root) {
        this.root = root;
    }

    //Add function
    public void add(E element){
        this.add(new BinarySearchNode<>(element));
    }

    public void add(BinarySearchNode<E> newNode){
        if(root == null){
            root = newNode;
            return;
        }
        List<BinarySearchNode<E>> visitedNodes = new ArrayList<>();
        addRecursionHelper(newNode, root, visitedNodes);
    }

    private void addRecursionHelper(BinarySearchNode<E> newNode, BinarySearchNode<E> currentNode, List<BinarySearchNode<E>> visitedNodes){
        visitedNodes.add(currentNode);
        int difference = currentNode.getElement().compareTo(newNode.getElement());
        if(difference > 0){
            if(currentNode.getLeftChildren() != null){
                addRecursionHelper(newNode, currentNode.getLeftChildren(), visitedNodes);
            }else{
                currentNode.setLeftChildren(newNode);
            }
        }else if(difference < 0){
            if(currentNode.getRightChildren() != null){
                addRecursionHelper(newNode, currentNode.getRightChildren(), visitedNodes);
            }else{
                currentNode.setRightChildren(newNode);
            }
        }
    }

    //Remove function
    public void remove(E element){
        this.remove(new BinarySearchNode<>(element));
    }

    public void remove(BinarySearchNode<E> removeNode){
        if(root == null){
            return;
        }
        List<BinarySearchNode<E>> visitedNodes = new ArrayList<>();
        removeHelper(removeNode, root, null, visitedNodes);
    }

    private BinarySearchNode<E> removeHelper(BinarySearchNode<E> removeNode, BinarySearchNode<E> root, Boolean isLeftRemove, List<BinarySearchNode<E>> visitedNodes){
        if(root == null){
            return null;
        }
        visitedNodes.add(root);
        int difference = root.getElement().compareTo(removeNode.getElement());
        if(difference > 0){
            if(isLeftRemove == null){
                isLeftRemove = true;
            }
            root.setLeftChildren(removeHelper(removeNode, root.getLeftChildren(), isLeftRemove, visitedNodes));
        }else if(difference < 0){
            if(isLeftRemove == null){
                isLeftRemove = false;
            }
            root.setRightChildren(removeHelper(removeNode, root.getRightChildren() ,isLeftRemove, visitedNodes));
        }else{
            //difference == 0
            if(isLeftRemove == null || isLeftRemove){
                return leftRemoveNode(root);
            }else{
                return rightRemoveNode(root);
            }
        }
        return root;
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

    public boolean contain(E element){
        return contain(new BinarySearchNode<>(element));
    }

    public boolean contain(BinarySearchNode<E> checkNode){

        if(checkNode == null || root == null){
            return false;
        }
        return containHelper(checkNode, root);
    }

    private boolean containHelper(BinarySearchNode<E> checkNode, BinarySearchNode<E> root){
        BinarySearchNode<E> currentNode = root;
        int difference = currentNode.getElement().compareTo(checkNode.getElement());
        if(difference > 0){
            return containHelper(checkNode, root.getLeftChildren());
        }else if(difference < 0){
            return containHelper(checkNode, root.getRightChildren());
        }else {
            return true;
        }
    }

    //Print function refer to https://www.baeldung.com/java-print-binary-tree-diagram
    public void traverseNodes(StringBuilder sb, String padding, String pointer, BinarySearchNode<E> node,
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

    public String traversePreOrder(BinarySearchNode<E> root) {

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
        os.println(traversePreOrder(root));
    }
}
