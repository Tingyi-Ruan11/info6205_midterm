import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class MidtermSolution {
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
    
        TreeNode(int x) {
            val = x;
        }
    }
    
    // Problem 1
    // Solution 1
    public static TreeNode upsideDownBinaryTree(TreeNode root) {
        // define pointers
        TreeNode curr = root;
        TreeNode next = null;
        TreeNode temp = null;
        TreeNode prev = null;
        
        while(curr != null) {
            next = curr.left;
            
            // swapping nodes now, need temp to keep the previous right child
            curr.left = temp;
            temp = curr.right;
            curr.right = prev;
            
            prev = curr;
            curr = next;
        }
        return prev;
    }

    // Method to print the tree in pre-order traversal
    public static void preOrder(TreeNode root) {
        if (root != null) {
            System.out.print(root.val + " ");
            preOrder(root.left);
            preOrder(root.right);
        }
   
    }
        

    // Problem 2
    // Solution 2
    static int counter =0;
    public static boolean helper_dfs(TreeNode node) {
        if (node == null) {
            return true;
        }

        boolean left = helper_dfs(node.left);
        boolean right = helper_dfs(node.right);

        // If both the children form uni-value subtrees, compare the value of chidren's node with the node value.
        if (left && right) {
            if (node.left != null && node.left.val != node.val) {
                return false;
            }
            if (node.right != null && node.right.val != node.val) {
                return false;
            }
            counter++;
            return true;
        }
        // Else if any of the child does not form a uni-value subtree, the subtree rooted at node cannot be a uni-value subtree.
        return false;
    }
    
    public static int countUnivalSubtrees(TreeNode root) {
        // make help function can revise this count
        helper_dfs(root);
        return counter;
    }

    // Problem 3
    // Solutin 3
    public static boolean verifyPreorder(int[] preorder) {
        return verifyPreorder(preorder, 0, preorder.length - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    private static boolean verifyPreorder(int[] preorder, int start, int end, int min, int max) {
        // If there are no elements, or it's a single element, it's a valid BST
        if (start > end) {
            return true;
        }

        int root = preorder[start];
        // If the current root does not satisfy min < root < max, it's not a BST
        if (root <= min || root >= max) {
            return false;
        }

        int rightSubtreeStart = start + 1;
        // Find the start of the right subtree
        while (rightSubtreeStart <= end && preorder[rightSubtreeStart] < root) {
            rightSubtreeStart++;
        }

        // Ensure that the right subtree only contains values greater than root
        for (int i = rightSubtreeStart; i <= end; i++) {
            if (preorder[i] <= root) {
                return false;
            }
        }

        // Recursively verify the left and right subtrees
        return verifyPreorder(preorder, start + 1, rightSubtreeStart - 1, min, root) &&
                verifyPreorder(preorder, rightSubtreeStart, end, root, max);
    }

    // Problem 4
    // Solution 4
    // Define the directions the ball can roll
    static int[][] DIR = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; //up, down, right, left
    public static int shortestDistance(int[][] maze, int[] start, int[] destination) {
        int m = maze.length, n = maze[0].length;

        //check if it's possible to stay at destination to improve time efficiency
        boolean[] hitsWall = new boolean[4]; //up, down, right, left
        boolean hasSpace = false;
        for (int i = 0; i < 4; i++) {
            hitsWall[i] = hitWall(destination[0] + DIR[i][0], destination[1] + DIR[i][1], maze) != 1;
            if (!hitsWall[i]) {
                hasSpace = true;
            }
        }
        if (!hasSpace // if the destination are fully surrounded by walls in four directions
            || (hitsWall[0] && hitsWall[1] && !hitsWall[2] && !hitsWall[3]) // or up and down are surrounded by wall but both left and right are empty spaces
            || (!hitsWall[0] && !hitsWall[1] && hitsWall[2] && hitsWall[3])) { // or left and right are surrounded by wall but both up and down are empty spaces
            return -1; // it would be impossible to stay at destination
        }
        
        PriorityQueue<int[]> minHeap = new PriorityQueue<>((a, b)->(a[2] - b[2]));
        Integer[][] distance = new Integer[m][n];
        minHeap.offer(new int[]{start[0], start[1], 0});
        distance[start[0]][start[1]] = 0;

        while (!minHeap.isEmpty()) {
            int[] vals = minHeap.poll();
            int r = vals[0], c = vals[1], d = vals[2];
            if (r == destination[0] && c == destination[1]) {
                return d;
            }
            if (distance[r][c] != null) {
                distance[r][c] = d;
            }
            //explore
            for (int[] dir : DIR) {
                int newD = 0, newR = r + dir[0], newC = c + dir[1];
                //keep expanding until hit the wall or boundary
                while (hitWall(newR, newC, maze) == 1) {
                    newR += dir[0];
                    newC += dir[1];
                    newD++;
                }

                if (newD > 0) { //if any movement is made
                    newR -= dir[0];
                    newC -= dir[1];
                    if (distance[newR][newC] == null || newD + d < distance[newR][newC]) {
                        //if found a short path to a empty space, add it to the priority queue 
                        distance[newR][newC] = newD + d;
                        minHeap.offer(new int[]{newR, newC, newD + d});
                    }
                }
                
            }
        }
        return -1;
    }

    private static int hitWall(int r, int c, int[][] maze) {
        if (r >= 0 && r < maze.length && c >= 0 && c < maze[0].length) {
            if (maze[r][c] == 1) {
                return 0; //hits the wall
            } else {
                return 1; //is an empty space
            }
        }
        return -1; //hits boundary
    }





    public static void main(String[] args) {
        // Test for problem 1
        // Constructing the binary tree
        // Given tree:
        //      1
        //     / \
        //    2   3
        //   / \
        //  4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        // Flipping the binary tree
        TreeNode newRoot = upsideDownBinaryTree(root);

        // Print the result, should be the new root and its children
        System.out.println("Test for problem 1:");
        System.out.print("new tree: ");
        preOrder(newRoot);
        System.out.println();

        // Test for problem 2
        TreeNode root2 = new TreeNode(5);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(5);
        root2.left.right = new TreeNode(5);
        root2.right.right = new TreeNode(5);
        System.out.println("Test for problem 2:");
        System.out.println("the number of unival subtrees: "+countUnivalSubtrees(root2));



        // Test for problem 3
        int[] preorder1 = {5, 2, 1, 3, 6};
        int[] preorder2 = {5, 2, 6, 1, 3};
        System.out.println("Test for problem 3:");
        System.out.println("is BST："+ verifyPreorder(preorder1)); // Output: true
        System.out.println("is BST："+ verifyPreorder(preorder2)); // Output: false

        // Test for problem 4
        int[][] maze1 = {
            {0, 0, 1, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 1, 0},
            {1, 1, 0, 1, 1},
            {0, 0, 0, 0, 0}
        };
        int[] start1 = {0, 4};
        int[] destination1 = {4, 4};
        System.out.println("Test for problem 4:");
        System.out.println("the shortest distance: "+shortestDistance(maze1, start1, destination1));//should be 12

        int[][] maze2 = {
            {0, 0, 1, 0, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0},
            {1, 1, 0, 1, 1}
        };
        int[] start2 = {0, 4};
        int[] destination2 = {3, 2};
        System.out.println("the shortest distance: "+ shortestDistance(maze2, start2, destination2));//should be -1
    }

}


