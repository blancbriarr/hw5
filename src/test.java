import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

class Node {
    public int val;
    public Node left, right, parent;
    public boolean black;
    public Node(int new_val) {
        this.val = new_val;
        this.left = RBTree.nil;
        this.right = RBTree.nil;
        this.parent = RBTree.nil;
        black = true;
    }
}

class RBTree {
    public Node root;
    static public Node nil = new Node(0);
    RBTree() {
        root = nil;
    }
    static int miss = 0, inserted = 0, deleted = 0;
    static int getTotal(RBTree root) {
        return inserted - deleted;
    }

    public void insert(RBTree tree, Node n) {
        Node y = tree.nil;
        Node x = tree.root;
        while (x != tree.nil) {
            y = x;
            if (n.val < x.val) {
                x = x.left;
            }
            else x = x.right;
        }
        n.parent = y;

        if(y == tree.nil)
            tree.root = n;
        else if (n.val < y.val)
            y.left = n;
        else y.right = n;
        n.left = tree.nil;
        n.right = tree.nil;
        n.black = false;
        RB_Insert_Fixup(tree, n);
    }

    public void RB_Insert_Fixup(RBTree T, Node n) {
        while (n.parent.black == false) {
            if (n.parent == n.parent.parent.left) {
                Node y = n.parent.parent.right;
                if(y.black == false){
                    n.parent.black = true;
                    y.black = true;
                    n.parent.parent.black = false;
                    n = n.parent.parent;
                }
                else {
                    if (n == n.parent.right){
                        n = n.parent;
                        left_rotate(T,n);
                    }

                    n.parent.black = true;
                    n.parent.parent.black = false;
                    right_rotate(T,n.parent.parent);
                }
            }
            else {
                Node y = n.parent.parent.left;
                if(y.black == false) {
                    n.parent.black = true;
                    y.black = true;
                    n.parent.parent.black = false;
                    n = n.parent.parent;
                }
                else {
                    if (n == n.parent.left){
                        n = n.parent;
                        right_rotate(T,n);
                    }
                    n.parent.black = true;
                    n.parent.parent.black = false;
                    left_rotate(T,n.parent.parent);
                }
            }
            T.root.black = true;
        }
    }

    public void Transplant(RBTree T, Node u, Node v){
        if (u.parent == nil)
            T.root = v;
        else if (u == u.parent.left)
            u.parent.left = v;
        else u.parent.right = v;
        if (v != nil)
            v.parent = u.parent;
    }
    public Node Tree_Minimum(Node x) {
        while (x.left != nil) {
            x = x.left;
        }
        return x;
    }
    public void delete(RBTree T, Node z) {
        Node y = z;
        Node x;
        boolean y_original_color = y.black;
        if(z.left == T.nil) {
            x = z.right;
            Transplant(T, z, z.right);
        }
        else if (z.right == T.nil) {
            x = z.left;
            Transplant(T, z, z.left);
        }
        else {
            y = Tree_Minimum(z.right);
            y_original_color = y.black;
            x = y.right;
            if (y.parent != z) {
                Transplant(T,y,y.right);
                y.right = z.right;
                y.right.parent = y;
            }
            else
                x.parent = y;
            Transplant(T,z,y);
            y.left = z.left;
            y.left.parent = y;
            y.black = z.black;
        }
        if (y_original_color == true)
            RB_Delete_Fixup(T, x);
    }

    public void RB_Delete_Fixup(RBTree T, Node x) {
        Node w;
        while ((x != T.root)&&(x.black == true)) {
            if (x == x.parent.left) {
                w = x.parent.right;
                if (w.black == false) {
                    w.black = true;
                    x.parent.black = false;
                    left_rotate(T, x.parent);
                    w = x.parent.right;
                }
                if ((w.left.black == true) && (w.right.black == true)) {
                    w.black = false;
                    x = x.parent;
                } else {
                    if (w.right.black == true) {
                        w.left.black = true;
                        w.black = false;
                        right_rotate(T, w);
                        w = x.parent.right;
                    }
                    w.black = x.parent.black;
                    x.parent.black = true;
                    w.right.black = true;
                    left_rotate(T, x.parent);
                    x = T.root;
                }

            }
            else {
                w = x.parent.left;
                if (w.black == false) {
                    w.black = true;
                    x.parent.black = false;
                    right_rotate(T, x.parent);
                    w = x.parent.left;
                }
                if ((w.left.black == true) && (w.left.black == true)) {
                    w.black = false;
                    x = x.parent;
                } else {
                    if (w.left.black == true) {
                        w.right.black = true;
                        w.black = false;
                        left_rotate(T, w);
                        w = x.parent.left;
                    }
                    w.black = x.parent.black;
                    x.parent.black = true;
                    w.left.black = true;
                    right_rotate(T, x.parent);
                    x = T.root;
                }
            }

        }
    }

    public Node search (Node tree, int val) {
        if (tree == null)
            return null;
        else if (val == tree.val)
            return tree;
        else if (val < tree.val)
            return search(tree.left,val);
        else
            return search(tree.right,val);
    }
    void print(Node tree, int level) {
        if (tree.right != nil)
            print(tree.right, level + 1);
        for(int i = 0; i < level; i++)
            System.out.print("    ");
        System.out.println(tree.val);
        if (tree.left != nil)
            print(tree.left, level + 1);
    }
    void inorder(Node tree) {
        if (tree == null)
            return;
        else {
            inorder(tree.left);
            System.out.print(" " + tree.val);
            inorder(tree.right);
        }
    }

    public void inorder_iter() {
        if (root == null)
            return;
        Stack stack = new Stack();
        Node node = root;
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
        while (!stack.is_empty()) {
            node = stack.pop();
            System.out.print(node.val + " ");
            if (node.right != null) {
                node = node.right;
                while (node != null) {
                    stack.push(node);
                    node = node.left;
                }
            }
        }
    }

    void left_rotate(RBTree T, Node x) {
        if (x.right == nil)
            return;
        Node y = x.right;
        x.right = y.left;
        if(y.left != T.nil)
            y.left.parent = x;
        y.parent = x.parent;
        if(x.parent == T.nil)
            T.root = y;
        else if (x == x.parent.left)
            x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    void right_rotate(RBTree T, Node y) {
        if (y.left == nil)
            return;
        Node x = y.left;
        y.left = x.right;
        if(x.right != T.nil)
            x.right.parent = y;
        x.parent = y.parent;
        if(y.parent == T.nil)
            T.root = x;
        else if (y == y.parent.right)
            y.parent.right = x;
        else y.parent.left = x;
        x.right = y;
        y.parent = x;
    }
    public int GetBlackNode(Node root) {
        if(root == nil) {
            return 0;
        }
        else {
            return 1;
        }
    }
    public int GetBlackHeight() {
        return 0;
    }
}

class Stack {
    ArrayList stk = new ArrayList<Node>();
    int top = 0;
    void push(Node a) {
        stk.add(a);
    }
    Node pop() {
        return (Node)stk.remove(stk.size() - 1);
    }
    boolean is_empty() {
        return top == 0;
    }
}





public class test {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        RBTree rb = new RBTree();
        while (true) {
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            int num = Integer.parseInt(line);
            System.out.println(num);
            if (num > 0) {
                rb.insert(rb, new Node(num));
            } else if (num < 0) {
                if (rb.search(rb.root, -num) != null) {
                    rb.delete(rb, rb.search(rb.root, -num));
                } else
                    break;
            }
            br.close();
            System.out.println("filename= ");
            System.out.println("total= " + rb.getTotal(rb));
            System.out.println("insert= " + rb.inserted);
            System.out.println("deleted= " + rb.deleted);
            System.out.println("miss= " + rb.miss);
            System.out.println("nb= ");
            System.out.println("bh= ");
            System.out.println("R");
            System.out.println("B");
        }
    }
}