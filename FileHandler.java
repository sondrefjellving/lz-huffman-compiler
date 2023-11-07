import java.io.*;

public class FileHandler {

    public static void writeFreqTableToFile(String freqTable, DataOutputStream out) throws IOException {
        String[] nodes = freqTable.split(",");
        char numOfNodes = (char)nodes.length;
        out.writeChar(numOfNodes);

        for (String node : nodes) {
            String[] charAndFreq = node.split(":");
            out.writeChar((char)Integer.parseInt(charAndFreq[0]));
            out.writeChar((char)Integer.parseInt(charAndFreq[1]));
        }
    }

    public static void writeHuffmanCodeLengthToFile(int length, DataOutputStream out) throws IOException {
        out.writeInt(length);
    }

    public static char[] readFreqTableFromFile(DataInputStream in) throws IOException {
        char numberOfNodes = in.readChar();
        char[] charsAndFrequency = new char[numberOfNodes*2];

        for (int i = 0; i < charsAndFrequency.length; i++) {
            charsAndFrequency[i] = in.readChar();
        }

        return charsAndFrequency;
    }

    public static int readHuffmanCodeLengthFromFile(DataInputStream in) throws IOException {
        return in.readInt();
    }


    public static void writeStringToFile(String filePath, String content) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(content);
        }
    }
    public static void writeBytesToFile (DataOutputStream out, byte[] bytes) throws IOException {
        for (byte currentByte : bytes) {
            out.writeByte(currentByte);
        }
    }

    public static byte[] readBytesFromFile (DataInputStream in) throws IOException {
        byte[] bytes = new byte[in.available()];
        in.readFully(bytes);
        return bytes;
    }

    public static Pair readFileWithFrequencyTable(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String frequencyTable = reader.readLine(); // Read the frequency table line
            try (DataInputStream in = new DataInputStream(new FileInputStream(filePath))) {
                // Skip the frequency table part plus the newline
                in.skipBytes(frequencyTable.length() + 1);

                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(byteBuffer)) != -1) {
                    buffer.write(byteBuffer, 0, bytesRead);
                }
                return new Pair(frequencyTable, buffer.toByteArray());
            }
        }
    }
    static class Pair{
        String frequencyTable;
        byte[] bytes;
        public Pair(String frequencyTable, byte[] bytes) {
            this.bytes = bytes;
            this.frequencyTable = frequencyTable;
        }

        public byte[] getBytes() {
            return bytes;
        }

        public String getFrequencyTable() {
            return frequencyTable;
        }
    }
}

