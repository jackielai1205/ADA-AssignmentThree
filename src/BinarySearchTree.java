import java.util.ArrayList;
import java.util.List;

public class BinarySearchTree<E extends Comparable<E>> {

    private BinarySearchNode<E> root;

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
        System.out.println(visitedNodes);
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
        System.out.println(visitedNodes);
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

//    private boolean containHelper(BinarySearchNode<E> checkNode, BinarySearchNode<E> root){
//        BinarySearchNode<E> currentNode = root;
//        int difference = checkNode.getElement().compareTo(currentNode.getElement());
//        if(difference )
//    }

    /* Print nodes at a given level */
    void printGivenLevel(BinarySearchNode root, int level)
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

    int height(BinarySearchNode root)
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
    void printLevelOrder()
    {
        int h = height(root);
        int i;
        for (i = 1; i <= h; i++) {
            printGivenLevel(root, i);
            System.out.print(System.lineSeparator());
        }
    }
}
