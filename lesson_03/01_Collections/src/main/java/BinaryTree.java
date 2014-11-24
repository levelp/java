import javafx.scene.Parent;

/**
 * Бинарное дерево поиска
 * Левый наследник всегда меньше правого
 */
public class BinaryTree<T> {

    /**
     * Узел дерева
     */
    class TreeNode <T> {
        public T value;
        public TreeNode left;
        public TreeNode right;
    }

    private TreeNode root = null;

    public void add(T value) {

        if (root == null) {
            root = new TreeNode();
            root.value = value;
        }else {
            TreeNode tempTreeNode = root;
            TreeNode parentNode = null;
            while (tempTreeNode != null) {
                if (tempTreeNode.value.equals(value)) {
                    System.out.println("Значение уже существует");
                    return;
                }else {
                    parentNode = tempTreeNode;
                    if (tempTreeNode.value.hashCode() < value.hashCode()) {
                        if (tempTreeNode.right != null) {
                            tempTreeNode = tempTreeNode.right;
                            if (tempTreeNode.value == null) {
                                break;
                            } else {
                                continue;
                            }
                        } else {
                            tempTreeNode.right = new TreeNode();
                            tempTreeNode = tempTreeNode.right;
                            break;
                        }
                    }
                    if (tempTreeNode.value.hashCode() > value.hashCode()) {
                        if (tempTreeNode.left != null) {
                            tempTreeNode = tempTreeNode.left;
                            if (tempTreeNode.value == null) {
                                break;
                            } else {
                                continue;
                            }
                        } else {
                            tempTreeNode.left = new TreeNode();
                            tempTreeNode = tempTreeNode.left;
                            break;
                        }

                    }
                }
            }
            TreeNode newTreeNode = new TreeNode();
            if (parentNode == null) {
                root = newTreeNode;
                root.value = value;
            }
            else {
                tempTreeNode = newTreeNode;
                tempTreeNode.value = value;
                if (parentNode.value.hashCode() > value.hashCode())
                    parentNode.left = newTreeNode;
                if (parentNode.value.hashCode() < value.hashCode())
                    parentNode.right = newTreeNode;
            }
//            root = tempTreeNode;
        }
    }

    public boolean find (T value) {
        if (root == null) {
            System.out.println("Дерево пустое");
            return false;
        }else {
            TreeNode tempTreeNode = root;
            while (tempTreeNode != null) {
                if (tempTreeNode.value.equals(value)) {
                    System.out.println("Значение найдено");
                    return true;
                }else {
                    if (tempTreeNode.value.hashCode() < value.hashCode()) {
                        if (tempTreeNode.right != null) {
                            tempTreeNode = tempTreeNode.right;
                            continue;
                        } else {
                            System.out.println("Значениея не существует");
                            return false;
                        }
                    }
                    if (tempTreeNode.value.hashCode() > value.hashCode()) {
                        if (tempTreeNode.left != null) {
                            tempTreeNode = tempTreeNode.left;
                            continue;
                        } else {
                            System.out.println("Значениея не существует");
                            return false;
                        }

                    }
                }
            }
        }
        return false;
    }

    // TODO: реализовать
}
