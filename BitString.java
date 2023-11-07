public class BitString {
    private String bitString;
    private byte[] bytes;

    public BitString(byte[] bytes) {
        this.bytes = bytes;
    }

    public BitString(String bitString) {
        this.bitString = bitString;
    }

    public String getBitString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            byte currentByte = bytes[i];
            //int bitOverflow = (i == bytes.length - 1) ? 8 - (bytes.length % 8) : 0; !!DOESNT WORK // checks if last element has "missing bits"
            for (int j = 0; j < 8; j++) {
                sb.append((currentByte >> 7) & 1); // gets the leftmost bit
                currentByte <<= 1;                 // leftshifts 1. Before: 01000000. After: 1000000
            }
        }

        return sb.toString();
    }

    public byte[] getBytes() {
        byte[] bytes = bitString.getBytes();

        byte bitOverflow = (byte)(bytes.length % 8); // if the bytes length isn't devisable by 8, it means that the last element of the compressed byte array won't be filled up completely
        int numOfBytes = bytes.length / 8;           // calculates the byte size of the compressed byte array
        byte[] compressedBytes;
        compressedBytes = (bitOverflow != 0) ? new byte[numOfBytes + 1] : new byte[numOfBytes]; // if bitstring has overflowing bits, one more byte will get added because of whole number division

        int compressedIndex = 0; // Index for the compressedBytes array
        byte compressedByte = 0; // Temporary value for the compressed byte
        for (int i = 0; i < bytes.length; i++) {
            if (i != 0 && i % 8 == 0) {                             // runs when compressedByte has been "filled" with 8 bits
                compressedBytes[compressedIndex] = compressedByte;  // adds compressedByte to the compressedByte array
                compressedByte = 0;
                compressedIndex++;
            }

            compressedByte <<= 1;                                   // left-shifts compressed byte 1 bit. Before shift: 00001000. After shift: 00010000

            if (bytes[i] == '1') {                                  // if the current bit is 1...
                compressedByte |= 1;                                // Set the rightmost bit if the character is '1'. Before: 00010000. After: 00010001
            }

            if (i == bytes.length-1) {  // checks if loop is in last round, because if the current compressedByte hasn't been added to the last element of the array
                compressedBytes[compressedIndex] = (bitOverflow != 0) ? (byte)(compressedByte << 8-bitOverflow) : compressedByte;
            }
        }

        return compressedBytes;
    }
}
