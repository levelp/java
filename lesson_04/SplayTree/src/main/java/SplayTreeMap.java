/**
 * Расширяющееся дерево (splay tree) является двоичным деревом поиска,
 * в котором поддерживается свойство сбалансированности.
 */
public class SplayTreeMap {
    private Node root;
    private String string;

    public SplayTreeMap() {
        root = null; // every new object make the root null
    }

    // Left child rotate
    private Node leftRotate(Node node) {
        Node newT = node.left;
        node.left = newT.right;
        newT.right = node;
        return newT;
    }

    //Right child rotate
    private Node rightRotate(Node node) {
        Node newT = node.right;
        node.right = newT.left;
        newT.left = node;
        return newT;
    }

    // adjustTree function to perform rotations and zig - zig, zig-zag operations.
    private void adjustTree(int data) {
        boolean flag = true; // check if satisfed the condition or not.
        Node node = root;
        Node parent, grparent, ggparent = null; // Need pointer for the parent and the grandparent

        while (true) {
            if (node == null || data == node.data)
                break;
            else if (node.left != null && data < node.data) {
                // left child data match do simple left child rotate
                if (data == node.left.data) {
                    node = leftRotate(node);
                }
                // zig-zig
                else if (node.left.left != null && data == node.left.left.data) {
                    grparent = node;
                    parent = node.left;
                    leftRotate(grparent);
                    node = leftRotate(parent);
                    flag = true;
                }
                // zig-zag
                else if (node.left.right != null && data == node.left.right.data) {
                    grparent = node; // point to grant parent
                    parent = node.left; // pointer to parent
                    grparent.left = rightRotate(parent);
                    node = leftRotate(grparent);
                    flag = true;
                } else if (data < node.data) {
                    ggparent = node; // point for the great-grandparent.
                    node = node.left;
                }
            } else if (node.right != null && data > node.data) {
                //  right child data match do simple left child rotate
                if (data == node.right.data) {
                    node = rightRotate(node);
                }
                // zig-zig
                else if (node.right.right != null && data == node.right.right.data) {
                    grparent = node;
                    parent = node.right;
                    rightRotate(grparent);
                    node = rightRotate(parent);
                    flag = true;
                }
                // zig-zag
                else if (node.right.left != null && data == node.right.left.data) {
                    grparent = node;
                    parent = node.right;
                    grparent.right = leftRotate(parent);
                    node = rightRotate(grparent);
                    flag = true;
                } else if (data > node.data) {
                    ggparent = node;
                    node = node.right;
                }
            } else if ((node.left == null && data < node.data)
                    || (node.right == null && data > node.data)) {
                data = node.data;
                node = root;
                ggparent = null;
            }

            // Link node and its all parent after zig-zig or zig-zag
            // set root to node.
            if (flag && ggparent != null) {
                if (node.data < ggparent.data)
                    ggparent.left = node;
                else if (node.data > ggparent.data)
                    ggparent.right = node;
                node = root;
                ggparent = null;
                flag = false;
            }
        }
        // The root is now that of the final tree
        root = node;
    }

    // Check if any node exist or not.
    public boolean isNull() {
        return root == null;
    }

    // item need to be added
    public void add(int item) {
        root = add(item, root);
        adjustTree(item);
    }

    // item and root where it need to be added
    private Node add(int item, Node node) {
        if (node == null)
            return new Node(item);
        else {
            if (item < node.data)
                node.left = add(item, node.left);
            else if (item > node.data)
                node.right = add(item, node.right);
            return node;
        }
    }

    // Get the root value
    public Integer getRootValue() {
        if (root != null) {
            return root.data;
        } else {
            return null;
        }
    }

    // Get the current root of the tree.
    public Node getRoot() {
        return root;
    }

    // preorder traversal of the tree.
    void preOrder(Node node) {
        if (node == null)
            return;
        string += node.data + " / ";
        preOrder(node.left);
        preOrder(node.right);
    }

    /**
     * Removes a node just like a search tree does, then rotates the
     * removed node's parent to the top.
     *
     * @param item - item to remove
     * @return - boolean value true or false.
     */
    public boolean remove(int item) {
        if (!isNull()) {
            adjustTree(item);
            if (root != null && root.data == item) {
                if (root.left != null) {
                    Node tmp = root.right;
                    root = root.left;
                    adjustTree(item);
                    root.right = tmp;
                } else
                    root = root.right;
                return true;
            }
        }
        return false;
    }

    //count the leaf in tree.
    public int leafCount(Node node) {
        if (node == null)
            return 0;
        if (node.left == null && node.right == null) {
            return 1;
        } else {
            return leafCount(node.left) + leafCount(node.right);
        }
    }

    // count leaf sum
    public int leafSum(Node node) {
        if (node == null)
            return 0;
        if (node.left == null && node.right == null) {
            return node.data;
        } else {
            return leafSum(node.left) + leafSum(node.right);
        }
    }

    /**
     * Find the item to be found.
     * Rotates the node to the top if found or the last node
     * accessed if not found
     *
     * @param item - item to find
     * @return - boolean
     */
    public boolean find(int item) {
        if (isNull())
            return false;
        adjustTree(item);
        return root.data == item;
    }

    @Override
    public String toString() {
        string = "";
        preOrder(getRoot());
        return string.trim();
    }

    /**
     * Node class represent each node in tree. left , right pointer.
     */

    public class Node {
        public int data;
        public Node left, right;

        public Node(int element) {
            this.data = element;
            left = null;
            right = null;
        }
    }


}