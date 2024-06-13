import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

public class Compress{
    Path inputFilePath;
    Path outputFilePath;
    protected static final String INPUT_FILE_EXT = ".txt";
    protected static final String OUTPUT_FILE_EXT = ".hzip";

    public static void main(String[] args) {
        if (args != null && args.length != 0) {
            String inputFile = args[0];
            System.out.println("Starting to Compress");
            double startTime = System.currentTimeMillis();
            try {
                Compress c = new Compress(inputFile);
                c.writeCompressedFile();
                System.out.println(
                        "Compress file completed in " + (System.currentTimeMillis() - startTime) / 1000 + " seconds");
            } catch (FileNotFoundException e) {
                System.out.println("Error while compressing file: " + e);
            }
        } else {
            System.out.println("No input file specified");
        }
    }

    /**
     * Compress' constructor
     *
     * @param inputFile
     * @throws FileNotFoundException
     */
    public Compress(String inputFile) throws FileNotFoundException {
        //validate input file exists (i.e., has been found)
        inputFilePath = Path.of(inputFile);
        if (!Files.exists(inputFilePath)) {
            throw new FileNotFoundException(inputFilePath.toString());
        }

        //create simple binary output file name (i.e. not using any external dependency here)
        String fileName = inputFilePath.getFileName().toString();
        fileName = fileName.substring(0, fileName.lastIndexOf(INPUT_FILE_EXT));
        outputFilePath = Path.of(fileName + OUTPUT_FILE_EXT);
    }

    /**
     * Call HuffmanCoding object to encode and write compressed file.
     */
    public void writeCompressedFile(){
        //perform huffman encoding and write out compressed file
        HuffmanCoding huffmanCoding = new HuffmanCoding(this.inputFilePath);

        try {
            huffmanCoding.encode(this.inputFilePath, this.outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


