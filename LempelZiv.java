import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * First argument: "filename" (diverse.lyx)
 * Second argument: "compress" or "decompress"
 */
public class LempelZiv {

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("No filename");
            return;
        }

        String fileName = args[0];
        String operation = args.length > 1 ? args[1] : "compress";
        String algorithm = args.length > 2 ? args[2] : "LZW";
        String compressedFileName = getCompressedFileName(fileName);

        if ("compress".equalsIgnoreCase(operation)) {
            String fileContent = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);
            List<Integer> compressedData = null;
            if ("LZW".equalsIgnoreCase(algorithm)) {
                compressedData = compressLZW(fileContent);
            } else if ("LZ77".equalsIgnoreCase(algorithm)) {
                compressedData = compressLZ77(fileContent);
                System.out.println(Arrays.asList(compressedData));
            } else if ("LZSS".equalsIgnoreCase(algorithm)) {
                compressedData = compressLZSS(fileContent);
            } else {
                System.out.println("Invalid algorithm. Please use 'LZW' or 'LZ77'.");
                return;
            }
            saveCompressed(compressedData, compressedFileName);
        } else if ("decompress".equalsIgnoreCase(operation)) {
            List<Integer> compressedData = loadCompressed(compressedFileName);
            String decompressedData = null;
            if ("LZW".equalsIgnoreCase(algorithm)) {
                decompressedData = decompressLZW(compressedData);
            } else if ("LZ77".equalsIgnoreCase(algorithm)) {
                decompressedData = decompressLZ77(compressedData);
            } else if ("LZSS".equalsIgnoreCase(algorithm)){
                decompressedData = decompressLZSS(compressedData);
            }
            else {
                System.out.println("Invalid algorithm. Please use 'LZW' or 'LZ77'.");
                return;
            }
            if (decompressedData != null && decompressedData.equals(new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8))) {
                System.out.println("Successfully decompressed");
            } else {
                System.out.println("Error while decompressing");
            }
        } else {
            System.out.println("Invalid operation. Please use 'compress' or 'decompress'.");
        }
    }

    public static List<Integer> compressLZ77(String str) {
        List<Integer> compressed = new ArrayList<>();
        int windowSize = 32000;
        int bufferMaxLen = 2048;

        int i = 0;
        while (i < str.length()) {
            int bestLength = 0;
            int bestOffset = 0;

            int maxOffset = Math.max(0, i - windowSize);
            for (int j = i - 1; j >= maxOffset; j--) {
                int length = 0;
                while (length < bufferMaxLen && i + length < str.length() && str.charAt(j + length) == str.charAt(i + length)) {
                    length++;
                }
                if (length > bestLength) {
                    bestLength = length;
                    bestOffset = i - j;
                }
            }

            compressed.add(bestOffset);
            compressed.add(bestLength);
            if (bestLength > 0) {
                i += bestLength;
            } else {
                compressed.add((int) str.charAt(i));
                i++;
            }
        }
        return compressed;
    }

    public static String decompressLZ77(List<Integer> compressed) {
        StringBuilder decompressed = new StringBuilder();
        for (int i = 0; i < compressed.size(); ) {
            int offset = compressed.get(i++);
            int length = compressed.get(i++);
            if (length > 0) {
                int start = decompressed.length() - offset;
                for (int j = 0; j < length; j++) {
                    decompressed.append(decompressed.charAt(start + j));
                }
            } else {
                decompressed.append((char) (int) compressed.get(i++));
            }
        }
        return decompressed.toString();
    }

    private static String getCompressedFileName(String originalFileName) {
        return originalFileName + "_compressed.bin";
    }

    private static void saveCompressed(List<Integer> compressed, String fileName) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4 + compressed.size() * 2);
        buffer.putInt(compressed.size());
        for (int value : compressed) {
            buffer.putShort((short) value);
        }
        Files.write(Paths.get(fileName), buffer.array());
        System.out.println("Compressed file saved as: " + fileName);
    }

    private static List<Integer> loadCompressed(String fileName) throws IOException {
        byte[] fileContent = Files.readAllBytes(Paths.get(fileName));
        ByteBuffer readbuffer = ByteBuffer.wrap(fileContent);
        int length = readbuffer.getInt();

        List<Integer> compressedData = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            compressedData.add((int) readbuffer.getShort());
        }
        return compressedData;
    }

    public static String compressToString(String algorithm, String input){
        List list;
        switch (algorithm) {
            case "LZW": list = compressLZW(input); break;
            case "LZ77": list = compressLZ77(input); break;
            case "LZSS": list = compressLZSS(input); break;
            default:
                list = null;
        }

        if (list == null) {
            System.out.println("No such algorithm..");
            System.exit(0);
        }

        StringBuilder sb = new StringBuilder();
        for (Object o : list) {
            String[] stringList = o.toString().split(",");
            sb.append(stringList[0]).append(", ");
        }
        return sb.toString();
    }

    public static String compressToString(String input){
        StringBuilder sb = new StringBuilder();
        List list = compressLZ77(input);
        for (Object o : list) {
             String[] stringList = o.toString().split(",");
             sb.append(stringList[0]).append(", ");
        }
        return sb.toString();
    }

    public static List<Integer> compressLZW(String str) {
        List<Integer> compressed = new ArrayList<>();
        List<String> dictionary = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            dictionary.add(String.valueOf((char) i));
        }

        addNonAsciiCodes(dictionary);

        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()) {
            sb.append(c);
            if (!dictionary.contains(sb.toString())) {
                String subStr = sb.substring(0, sb.length() - 1);
                int i = dictionary.indexOf(subStr);
                if (i == -1) {
                    System.out.println("Error: " + subStr);
                }
                compressed.add(i);
                dictionary.add(sb.toString());
                sb = new StringBuilder(String.valueOf(c));
            }
        }
        if (sb.length() > 0) {
            compressed.add(dictionary.indexOf(sb.toString()));
        }

        return compressed;
    }

    public static String decompressLZW(List<Integer> compressedCharSequences) {
        StringBuilder decompressed = new StringBuilder();
        List<String> dictionary = new ArrayList<>();
        for (int i = 0; i < 256; i++) {
            dictionary.add(String.valueOf((char) i));
        }

        addNonAsciiCodes(dictionary);

        String prevEntry = String.valueOf((char) (int) compressedCharSequences.get(0));
        decompressed.append(prevEntry);

        for (int i = 1; i < compressedCharSequences.size(); i++) {
            String entry;
            if (compressedCharSequences.get(i) < dictionary.size()) {
                entry = dictionary.get(compressedCharSequences.get(i));
            } else {
                entry = prevEntry + prevEntry.charAt(0);
            }
            decompressed.append(entry);
            dictionary.add(prevEntry + entry.charAt(0));
            prevEntry = entry;
        }

        return decompressed.toString();
    }


    public static List<Integer> compressLZSS(String str) {
        List<Integer> compressedData = new ArrayList<>();
        int windowSize = 4095;  // 12-bit
        int bufferSize = 15;  // 4-bit
        int i = 0;

        while (i < str.length()) {
            int maxLen = 0;
            int maxOffset = 0;

            // Define the search window and look-ahead buffer
            int start = Math.max(i - windowSize, 0);
            String searchWindow = str.substring(start, i);
            int end = Math.min(i + bufferSize, str.length());
            String buffer = str.substring(i, end);

            // Try to find the longest match
            for (int j = 0; j < searchWindow.length(); j++) {
                int k = 0;
                while (k < buffer.length() && j + k < searchWindow.length() && searchWindow.charAt(j + k) == buffer.charAt(k)) {
                    k++;
                }

                if (k > maxLen) {
                    maxLen = k;
                    maxOffset = i - start - j;
                }
            }

            // Generate compressed data
            if (maxLen > 0) {
                int encoded = (maxOffset << 4) | maxLen;
                compressedData.add(encoded);
                i += maxLen;
            } else {
                compressedData.add((int) str.charAt(i));
                i++;
            }
        }

        return compressedData;
    }

    public static String decompressLZSS(List<Integer> compressed) {
        StringBuilder sb = new StringBuilder();
        Queue<Integer> queue = new LinkedList<>(compressed);
        while (!queue.isEmpty()) {
            int flag = queue.poll();
            if (flag == 1) {
                // Literal
                sb.append((char) (int) queue.poll());
            } else {
                // Pointer
                int distance = queue.poll();
                int length = queue.poll();
                int start = sb.length() - distance;
                for (int i = 0; i < length; i++) {
                    sb.append(sb.charAt(start + i));
                }
            }
        }
        return sb.toString();
    }



    private static void addNonAsciiCodes(List<String> dictionary) {
        //TODO: Generalize this?
        char[] specialChars = {'•', '–', '…', '∗', ' ', 'Ω'};
        for (char c : specialChars) {
            dictionary.add(String.valueOf(c));
        }
    }
}

