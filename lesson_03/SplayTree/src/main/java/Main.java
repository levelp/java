
public class Main {
    public static void main(String[] args) {
        SplayTreeMap impl = new SplayTreeMap();
        impl.add(1);
        impl.add(5);
        impl.add(7);
        impl.add(9);
        impl.add(8);
        impl.add(4);


        System.out.println();
        System.out.println("Preorder after insert: " + impl);

        //System.out.println("Leave counts :" + impl.leafCount(impl.getRoot(12)));
        //System.out.println("Leave counts :" + impl.leafSum(impl.getRoot(24)));

	/*System.out.println("Remove:"+impl.remove(5));
    System.out.println("After delete preorder:"+impl);

	System.out.println("Node found or not :"+impl.find(2));
	System.out.println("After search preorder:"+impl);

	System.out.println("Node found or not :"+impl.find(3)); // node note in tree
	System.out.println("After search preorder:"+impl);*/

    }
}
