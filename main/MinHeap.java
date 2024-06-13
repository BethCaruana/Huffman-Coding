import java.lang.Math;

public class MinHeap {

    // array of Nodes
    private Node[] Heap;
    // keeps track of current size
    public int currentSize;
    // keeps track of size of the array
    private int size;

    public MinHeap(int size){
        this.size = size;
        this.currentSize = 0;
        Heap = new Node[size];
    }

    private int parentIndex(int i){
        return (int) Math.ceil((i-1)/2);
    }

    private int leftChild(int i){
        return (i * 2) + 1;
    }

    private int rightChild(int i){
        return (i * 2) + 2;
    }

    private boolean isLeaf(int i){
        // if both "children indicies" are beyond the size of the array its a leaf node
        if(rightChild(i) > this.currentSize && leftChild(i) > this.currentSize){
            return true;
        }
        //child nodes within the size of the array
        return false;
    }

    public void insert(Node node){
        // array is full
        if(this.currentSize >= this.size){
            return;
        }
        //add new node to end of array
        Heap[this.currentSize] = node;
        int current = this.currentSize;

        // while the parent frequency is higher, swap with current node until smallest frequency ends at the top
        while (current > 0 && Heap[current].getFrequency() < Heap[parentIndex(current)].getFrequency()){
            swap(current, parentIndex(current));
            current = parentIndex(current);
        }
        //increment the front
        this.currentSize++;
    }

    public Node extractMin(){
        // pop root/first element
        Node popped = Heap[0];
        this.currentSize--;
        //assign last element to the front
        Heap[0] = Heap[this.currentSize];
        //clear last spot
        Heap[this.currentSize] = null;
        // maintain heap order
        Heapify(0);
        return popped;
    }

    private void Heapify(int i){
        // if not a leaf
        if(!isLeaf(i)){
            //check if node has both children
            if(this.currentSize > rightChild(i)){
                if(this.currentSize > leftChild(i)){
                    // if current index frequency is larger than either of its children
                    if(Heap[i].getFrequency() > Heap[leftChild(i)].getFrequency() || Heap[i].getFrequency() > Heap[rightChild(i)].getFrequency()){
                        // if left node is the smallest
                        if(Heap[leftChild(i)].getFrequency() < Heap[rightChild(i)].getFrequency()){
                            //swap with current node and heapify the left node
                            swap(i, leftChild(i));
                            Heapify(leftChild(i));
                        }
                        //right node is smaller
                        else{
                            swap(i, rightChild(i));
                            Heapify(rightChild(i));
                        }
                    }
                }
                // right child but no left child
                else{
                    if(Heap[i].getFrequency() > Heap[rightChild(i)].getFrequency()){
                        swap(i, rightChild(i));
                        Heapify(rightChild(i));
                    }
                }
            }
            // left child but no right child
            else if(this.currentSize > leftChild(i)){
                if(Heap[i].getFrequency() > Heap[leftChild(i)].getFrequency()){
                    swap(i, leftChild(i));
                    Heapify(leftChild(i));
                }
            }
        }
    }

    // calls heapify on all elements with leaves
    public void buildHeap(){
        //start at the last non-leaf node in the tree
        for (int i = ((this.currentSize - 1 )/ 2); i>=0; i--){
            Heapify(i);
        }
    }

    // swaps nodes
    private void swap(int x, int y){
        Node temp = Heap[x];
        Heap[x] = Heap[y];
        Heap[y] = temp;
    }

}
