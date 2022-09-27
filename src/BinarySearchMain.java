public class BinarySearchMain {

    public static void main(String[] args){
        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
        tree.add(15);
        tree.add(18);
        tree.add(17);
        tree.add(24);
        tree.add(30);
        tree.add(28);
        tree.add(29);
        tree.printLevelOrder();
        tree.remove(30);
        tree.printLevelOrder();
    }
}
