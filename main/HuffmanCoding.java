import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCoding {
    Map<Character, Integer> charFreqMap;
    Map<Character, String> huffmanCodes;
    Node huffmanTree;

    protected static final String MAGIC_WORD = "hfm@n";

    /**
     * No-arg constructor used by Decompress/Decode and testing processes.
     */
    public HuffmanCoding(){
        this.huffmanCodes = new HashMap<>();
    }

    /**
     * Constructor used by Compress/Encode processes.
     *
     * @param inputFilePath path to file we're reading to encode
     */
    public HuffmanCoding(Path inputFilePath) {
        //create character frequency map from input file
        this.fillFrequencyMap(inputFilePath);
        //build huffman tree
        this.buildHuffmanTree(charFreqMap);
        //generate and store the huffman codes for this tree
        this.generateHuffmanCodes();
    }

    /**
     * Method to read the file and fill the char frequencies in the hashtable
     *
     * @param inputFilePath
     * @return Map<Character, Integer> charFreqMap
     */
    public Map<Character, Integer> fillFrequencyMap(Path inputFilePath){
        this.charFreqMap = new HashMap<>();
        try (BufferedReader fileReader = Files.newBufferedReader(inputFilePath)){
            int character;
            while((character = fileReader.read()) != -1){
                char charValue = (char) character;
                if(this.charFreqMap.containsKey(charValue)){
                    this.increment(charValue);
                }
                else{
                    this.newLetter(charValue);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return charFreqMap;
    }

    //helper method to add a new letter and frequency to the hashmap
    private void newLetter(Character letter){
        //System.out.println(letter);
        this.charFreqMap.put(letter, 1);
    }

    // helper method to increment frequency of letter in freq map
    private void increment(Character letter){
        Integer frequency = this.charFreqMap.get(letter);
        this.charFreqMap.put(letter, ++frequency);
    }

    /**
     * Build heap from character frequency map, then apply Huffman algo
     * @param charFreqMap Map<Character, Integer>
     * @return root Node
     */
    private Node buildHuffmanTree(Map<Character, Integer> charFreqMap) {
        MinHeap minHeap = new MinHeap(charFreqMap.size());
        for (Map.Entry<Character, Integer> entry : charFreqMap.entrySet()){
            //create new Node
            Node n = new Node(entry.getKey(), entry.getValue());
            minHeap.insert(n);
        }
        minHeap.buildHeap();
        //assign root node of huffman tree
        this.huffmanTree = this.huffman(minHeap);
        return huffmanTree;
    }

    /**
     * the Huffman coding algorithm implemented.
     * @param heap MinHeap
     * @return root Node
     */
    public Node huffman(MinHeap heap){
        //go through each element in the min heap
        while (heap.currentSize > 1) {
            // Allocate new node
            Node n = new Node();

            // Extract the two nodes with the lowest frequencies
            Node leftNode = heap.extractMin();
            Node rightNode = heap.extractMin();

            // Set the left and right children of the new node
            n.setLeft(leftNode);
            n.setRight(rightNode);

            // Calculate the frequency of the new node
            int frequency = leftNode.getFrequency() + rightNode.getFrequency();
            n.setFrequency(frequency);

            // Insert the new node back into the heap
            heap.insert(n);
        }

        // After the loop, there's only one node left in the heap, which is the root of the Huffman tree
        return heap.extractMin();
    }

    /**
     * Generate the Huffman Codes map to apply to
     * @return
     */
    private Map<Character, String> generateHuffmanCodes() {
        this.huffmanCodes = new HashMap<>();
        //start at the root then use preorder traversal to build huffman codes
        this.codeGenHelper(this.huffmanTree, "");
        return huffmanCodes;
    }

    private void codeGenHelper(Node node, String code){
        if (node.isLeaf()){
            this.huffmanCodes.put(node.letter, code);
            return;
        }
        codeGenHelper(node.left, code + '0');
        codeGenHelper(node.right, code + '1' );
    }

    /**
     * Method to apply Huffman encoding the input file and output encoded binary file
     * @param inputFilePath
     * @param outputFilePath
     * @return void
     * @throws IOException
     */
    public void encode(Path inputFilePath, Path outputFilePath) throws IOException {

        //use try with resources to manage the closing of streams
        try (BufferedOutputStream bufferedOutputStream =
                     new BufferedOutputStream(Files.newOutputStream(outputFilePath));
             ObjectOutputStream objectOutStream
                     = new ObjectOutputStream(bufferedOutputStream)){

            //write huffman 'magic word' & codes to output file
            objectOutStream.writeObject(MAGIC_WORD);

            //serialize huffman code map to output file
            objectOutStream.writeObject(this.huffmanCodes);

            //perform the actual huffman encoding
            this.applyHuffmanCodes(bufferedOutputStream, inputFilePath);
        }
    }

    /**
     * Method to apply stored Huffman codes during encode.
     *
     * @param bufferedOutputStream the open output stream to encoded file
     * @param inputFilePath the path to the input file we'll be applying Huffman encoding to
     */
    private void applyHuffmanCodes(BufferedOutputStream bufferedOutputStream, Path inputFilePath){
        //read in input file
        try (BufferedReader fileReader = Files.newBufferedReader(inputFilePath)){
            int character;

            while((character = fileReader.read()) != -1){
                char charValue = (char) character;
                var encodedChar = this.huffmanCodes.get(charValue).getBytes();
                bufferedOutputStream.write(encodedChar);
            }
            bufferedOutputStream.flush();
        } catch (IOException e) {
            System.out.println("Error opening input file reader: " + e);
        }
    }

    /**
     * Method to use Huffman codes to decode the binary input file and output txt file.
     * @param inputFilePath
     * @param outputFilePath
     * @throws IOException
     */
    public void decode(Path inputFilePath, Path outputFilePath) throws IOException {
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(Files.newInputStream(inputFilePath));
             ObjectInputStream objectInStream = new ObjectInputStream(bufferedInputStream);
             BufferedWriter writer = Files.newBufferedWriter(outputFilePath)) {
    
            try {
                String magic = (String) objectInStream.readObject();
                if (magic.equals(MAGIC_WORD)) {
                    @SuppressWarnings("unchecked")
                    Map<Character, String>  huffmanCodes = (Map<Character, String>) objectInStream.readObject();
                    this.huffmanCodes = huffmanCodes;

                    StringBuilder codeBuilder = new StringBuilder();
                    int number;
                    while ((number = bufferedInputStream.read()) != -1) {
                        char character = (char) number;
                        codeBuilder.append(character);
                        String code = codeBuilder.toString();
                        Character letter = undoHuffmanCodes(code);
                        if (letter != null) {
                            writer.write(letter);
                            codeBuilder.setLength(0); // reset codeBuilder
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace(); // Handle the exception or rethrow it as needed
            } catch (EOFException e) {
                System.out.println("reached end of file");
            }
        }
    }

    /**
     * Helper method to check if code exists in the map or not.
     * @param code Huffman code to check
     * @return Character or null
     */
    public Character undoHuffmanCodes(String code){
        //if code exists in map return the key value
        for(Map.Entry<Character, String> entry : this.huffmanCodes.entrySet()){
            if(entry.getValue().equals(code)){
                return entry.getKey();
            }
        }
        return null;
    }

    public void printInOrder(Node root){
        if(root == null){
            return;
        }
        System.out.println(root.getLetter() + " " + root.getFrequency());
        printInOrder(root.getLeft());
        printInOrder(root.getRight());
    }
}
