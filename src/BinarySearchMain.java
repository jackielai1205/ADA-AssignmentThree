public class BinarySearchMain {

    public static void main(String[] args){
//        BinarySearchTree<Integer> tree = new BinarySearchTree<>();
//        tree.add(15);
//        tree.add(18);
//        tree.add(17);
//        tree.add(24);
//        tree.add(30);
//        tree.add(28);
//        tree.add(29);
//        tree.printLevelOrder();
//        tree.remove(30);
//        System.out.println(tree.contain(29));
//        tree.printLevelOrder();
        PersistentDynamicSet<Integer> treeSet = new PersistentDynamicSet<>();
        treeSet.add(15);
        treeSet.add(5);
        treeSet.add(20);
        treeSet.add(10);
        treeSet.add(2);
        treeSet.remove(5);
        treeSet.remove(10);
        treeSet.getLatestVersionTree().printLevelOrder();

    }
}
