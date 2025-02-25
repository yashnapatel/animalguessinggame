import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Node {
    // Instance variables for the node's value and its left/right children
    private String value;
    private Node left;
    private Node right;

    // Constructor to initialize a node with a value and given left/right children
    public Node(String value, Node left, Node right){
        this.value = value;
        this.left = left;
        this.right = right;
    }

    // Constructor to initialize a leaf node with just a value
    public Node(String value){
        this.value = value;
        left = null;
        right = null;
    }

    // Getter for the node's value
    public String getValue(){
        return value;
    }

    // Getter for the left child node
    public Node getLeft(){
        return left;
    }

    // Getter for the right child node
    public Node getRight(){
        return right;
    }

    // Setter to update the left child node
    public void setLeft(Node l){
        left = l;
    }

    // Setter to update the right child node
    public void setRight(Node r){
        right = r;
    }

    // Setter to update the node's value
    public void setValue(String value){
        this.value = value;
    }

    /**
     * Method to write the tree's values to a file ("f.txt") using pre-order traversal.
     */
    public void makeFile(){
        ArrayList<String> vals = new ArrayList<String>();
        // Populate the list with node values using helper method
        makeFileHelper(vals);
        try {
            FileWriter writer = new FileWriter("f.txt"); 
            // Write each value (or "null") on a new line in the file
            for(String str: vals) {
                writer.write(str + System.lineSeparator());
            }
            writer.close();
        }
        catch(IOException ioe){
            // Exception is caught but not handled specifically
        }
    }

    /**
     * Helper method for makeFile() to add node values to the list.
     * Uses pre-order traversal: node, left, then right.
     * Adds "null" if a child does not exist.
     */
    public void makeFileHelper(ArrayList<String> vals){
        vals.add(value);
        // If left child exists, recursively add its values; else add "null"
        if(left != null){
            left.makeFileHelper(vals);
        }
        else {
            vals.add("null");
        }
        // If right child exists, recursively add its values; else add "null"
        if(right != null){
            right.makeFileHelper(vals);
        }
        else {
            vals.add("null");
        }
    }

    /*
     * Static method to rebuild the tree from the file "f.txt".
     * Reads the file and builds the tree using a helper method.
     */
    public static Node buildTree() {
        ArrayList<String> vals = new ArrayList<String>();
        try {
            File file = new File("f.txt");
            Scanner in = new Scanner(file);
            // Read each line of the file into the list
            while (in.hasNextLine()){
                vals.add(in.nextLine());
            }
            in.close();
        }
        catch(IOException ioe){
            // Exception is caught but not handled specifically
        }
        
        // Use an integer array to keep track of the current index while building the tree recursively
        int[] current = {0};
        return buildTreeHelper(vals, current);
    }

    /*
     * Helper method for buildTree() to recursively reconstruct the tree.
     */
    private static Node buildTreeHelper(ArrayList<String> vals, int[] current) {
        // If index is out of bounds or the value is "null", return null to indicate no node.
        if (current[0] >= vals.size() || vals.get(current[0]).equals("null")) {
            current[0]++;
            return null;
        }

        // Create a new node with the current value
        String value = vals.get(current[0]);
        Node n = new Node(value);
        current[0]++;

        // Recursively build the left and right subtrees
        n.setLeft(buildTreeHelper(vals, current));
        n.setRight(buildTreeHelper(vals, current));
        return n;
    }

    /*
     * Starts the animal guessing game by rebuilding the tree from file and prompting the user to play.
     */
    public static void guessingGame(){
        Node n = Node.buildTree();
        System.out.println("Get ready to play the animal guessing game!");
        try {
            Thread.sleep(1000); // Pause for 1 second
        } catch(Exception e) {
            // Exception is caught but not handled specifically
        }
        // Begin the guessing process starting from the root of the tree
        guess(n, n);
    }

    /*
     * Recursive method that runs the guessing game.
     * It navigates the tree based on user input, makes guesses, and updates the tree if needed.
    */
    public static Node guess(Node n, Node original){
        Scanner input = new Scanner(System.in);
        String in = "";
        String in2 = "";
        String in3 = "";
        
        // If at a leaf node, make a guess
        if(n.getLeft() == null || n.getRight() == null){
            System.out.println("I guess that your animal is a " + n.getValue() + ". Am I correct?");
            in = input.nextLine();
            // If the guess is correct, declare victory and ask if the user wants to play again
            if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
                System.out.println("I win!");
                try {
                    Thread.sleep(1000); // Pause for 1 second
                } catch(Exception e) {}
                System.out.println("Would you like to play again?");
                in = input.nextLine();
                if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
                    System.out.println("Okay, let's go again!");
                    try {
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                    guessingGame(); // Restart game
                }
                else{
                    System.out.println("Okay, see you later!");
                    System.exit(0);
                }              
            }
            // If the guess is wrong, update the tree with the correct animal and a new question
            else{
                System.out.println("Dang it! I lose.");
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {}
                System.out.println("Please enter the animal you were thinking.");
                in3 = input.nextLine();
                System.out.println("Please enter a yes/no question that has opposite answers for the guessed animal and the animal you are thinking of.");
                in2 = input.nextLine();
                System.out.println("Is the answer to this question yes or no for your animal?");
                in = input.nextLine();
                // Depending on the answer for the new animal, update the left/right children accordingly
                if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
                    n.setLeft(new Node(in3));
                    n.setRight(new Node(n.getValue()));
                    n.setValue(in2);
                    original.makeFile(); // Save updated tree to file
                    System.out.println("Your animal is now in my database!");
                    try {
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                    System.out.println("Would you like to play again?");
                    in = input.nextLine();
                    if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
                        System.out.println("Okay, let's go again!");
                        try {
                            Thread.sleep(1000);
                        } catch(Exception e) {}
                        guessingGame();
                    }
                    else{
                        System.out.println("Okay, see you later!");
                        System.exit(0);
                    } 
                }
                else{
                    n.setLeft(new Node(n.getValue()));
                    n.setRight(new Node(in3));
                    n.setValue(in2);
                    original.makeFile(); // Save updated tree to file
                    System.out.println("Your animal is now in my database!");
                    try {
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                    System.out.println("Would you like to play again?");
                    in = input.nextLine();
                    if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
                        System.out.println("Okay, let's go again!");
                        try {
                            Thread.sleep(1000);
                        } catch(Exception e) {}
                        guessingGame();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {}
                if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
                    System.out.println("Okay, let's go again!");
                    try {
                        Thread.sleep(1000);
                    } catch(Exception e) {}
                    guessingGame();
                }
                else{
                    System.out.println("Okay, see you later!");
                    System.exit(0);
                }              
            }
            input.close();
            return null;
        }

        // If not at a leaf, ask a question stored in the current node
        System.out.println(n.getValue());
        try {
            Thread.sleep(500); // Pause briefly to allow the user to read the question
        } catch(Exception e) {}
        in = input.nextLine();
        // Based on the user's response, traverse left or right in the tree
        if(in.contains("y") || in.contains("Y") || in.contains("yes") || in.contains("Yes") || in.contains("YES")){
            guess(n.getLeft(), original);
        }
        else{
            guess(n.getRight(), original);
        }

        input.close();
        return n;
    }
}