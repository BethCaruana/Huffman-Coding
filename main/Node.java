
public class Node{
    Character letter;
    Integer frequency;
    Node left;
    Node right;

    public Node(){
        this.letter = null;
        this.frequency = 0;
        this.left = null;
        this.right = null;
    }

    public Node(Integer frequency){
        this();
        this.frequency = frequency;
    }

    public Node(Character letter, Integer frequency){
        this.letter = letter;
        this.frequency = frequency;
        this.left = null;
        this.right = null;
    }

    public void setLetter(Character letter){
        this.letter = letter;
    }

    public void setFrequency(Integer frequency){
        this.frequency = frequency;
    }

    public void setLeft(Node left){
        this.left = left;
    }

    public void setRight(Node right){
        this.right = right;
    }

    public Character getLetter(){
        return this.letter;
    }

    public Integer getFrequency(){
        return this.frequency;
    }

    public Node getLeft(){
        return this.left;
    }

    public Node getRight(){
        return this.right;
    }

    public boolean isLeaf() {
        return (this.left == null && this.right == null);
    }

    public boolean equals(Node node1, Node node2){
        if (node1 == node2) {return true;}

        if (node1 == null || !node1.equals(node2)) {return false;}

        return equals(node1.left, node2.left) && equals(node1.right, node2.right);
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("(").append(this.letter).append(", ").append(this.frequency).append(")");
        if (this.left != null) { sb.append(", left child: ").append(this.left.toString()); }
        if (this.right != null) { sb.append(", right child:").append(this.right.toString()); }
        return  sb.toString();
    }
}