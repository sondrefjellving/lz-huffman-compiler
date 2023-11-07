import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Compiler {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("No filename");
            return;
        }

        String algorithm = "LZ77";

        String fileName = args[0];
        String operation = args.length > 1 ? args[1] : "compress";
        String decompressIntoFile = args.length == 3 ? args[2] : "";
        String compressedFileName = getCompressedFileName(fileName);
        try {
            if ("compress".equalsIgnoreCase(operation)) {
                String fileContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
                compress(algorithm, fileContent, compressedFileName);
                System.out.println("Compressed file was saved to: " + compressedFileName);
            } else if ("decompress".equalsIgnoreCase(operation)) {
                decompress(fileName, decompressIntoFile);
                System.out.println("Result of decompression was saved to: " + decompressIntoFile);
            } else {
                System.out.println("Invalid operation. Please use 'compress' or 'decompress'.");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage() + " can't be found.");
        }
    }


    public static void compress(String algorithm, String fileContent, String compressedFileName) {
        HuffmanNumberConfig numberConfig = new HuffmanNumberConfig(LempelZiv.compressToString(algorithm, fileContent));
        String freqTable = numberConfig.getFrequencyTable();
        String huffmanCodes = numberConfig.huffmanCode();

        BitString huffmanCodesBits = new BitString(huffmanCodes);
        try {
            FileOutputStream outFile = new FileOutputStream(compressedFileName);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(outFile);
            DataOutputStream output = new DataOutputStream(bufferedOutput);

            FileHandler.writeFreqTableToFile(freqTable, output);
            FileHandler.writeHuffmanCodeLengthToFile(huffmanCodes.length(), output);
            FileHandler.writeBytesToFile(output, huffmanCodesBits.getBytes());
            output.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void decompress(String decompressedFileName, String decompressIntoFile) {
        try {
            FileInputStream inFile = new FileInputStream(decompressedFileName);
            BufferedInputStream bufferedInput = new BufferedInputStream(inFile);
            DataInputStream input = new DataInputStream(bufferedInput);

            char[] freqTable = FileHandler.readFreqTableFromFile(input);
            List<NumberNode> nodes = charArrToNumberNodes(freqTable);
            int huffmanCodeLength = FileHandler.readHuffmanCodeLengthFromFile(input);

            BitString bitStringAsBytes = new BitString(FileHandler.readBytesFromFile(input));
            String bitString = bitStringAsBytes.getBitString();

            HuffmanDecode huffmanDecode = new HuffmanDecode(nodes, bitString.substring(0, huffmanCodeLength));
            String decoded = huffmanDecode.getNumResult();

            // Prepare for LZ decompression
            String[] decodedArr = decoded.split(",");
            List<Integer> integerList = new ArrayList<>();
            for (String s : decodedArr) {
                if(s.equals(",")) continue;
                integerList.add(Integer.parseInt(s));
            }
            // parses the string back to Lempel-Ziv decompression method
            String decompressed = LempelZiv.decompressLZ77(integerList);

            // Writes to file
            BufferedWriter writer = new BufferedWriter(new FileWriter(decompressIntoFile));
            writer.write(decompressed);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static String getCompressedFileName(String originalFileName) {
        return originalFileName + "_compressed.bin";
    }

    private static List<NumberNode> charArrToNumberNodes(char[] input) {
        List<NumberNode> nodes = new ArrayList<>();

        for (int i = 0; i < input.length; i+=2) {
            int n = input[i];
            int frequency = input[i+1];

            nodes.add(new NumberNode(n, frequency));
        }

        return nodes;
    }
}
