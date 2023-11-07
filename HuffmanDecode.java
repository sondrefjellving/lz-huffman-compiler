import java.util.Arrays;
import java.util.List;

/**
 * HuffmanDecode class
 * @author johanneslorentzen
 */
public class HuffmanDecode {

  private Huffman huffman;
  private String encodedMsg;

  /**
   * Constructor for HuffmanDecode
   *
   * @param nodes frequency nodes
   * @param encodedMsg the encoded message
   */
  HuffmanDecode(List nodes, String encodedMsg) {
    huffman = new Huffman(nodes);
    huffman.huffmanTree();
    this.encodedMsg = encodedMsg;
  }

  /**
   * Prints the result of the decoding.
   */
  public void printResult() {
    System.out.println(decodeHuffman(huffman.getRoot(), encodedMsg));
  }

  public String getNumResult() {
    return decodeNumber(huffman.getRoot(), encodedMsg);
  }


  /**
   * Decodes a Huffman encoded message containing numbers.
   *
   * @param root the root of the Huffman tree
   * @param encodedMessage the encoded message
   * @return the decoded message
   */
  public String decodeHuffman(HuffmanNode root, String encodedMessage) {
    StringBuilder decodedMessage = new StringBuilder();
    HuffmanNode currentNode = root;

    for (char bit : encodedMessage.toCharArray()) {
      if (bit == '0') {
        currentNode = currentNode.getLeftNode();
      } else {
        currentNode = currentNode.getRightNode();
      }

      if (currentNode instanceof CharNode) {
        decodedMessage.append(((CharNode) currentNode).getCharacter());
        currentNode = root;  // Reset to root for next character
      }
    }

    return decodedMessage.toString();
  }

  /**
   * Decodes a Huffman encoded message.
   *
   * @param root the root of the Huffman tree
   * @param encodedMessage the encoded message
   * @return the decoded message
   */
  public String decodeNumber(HuffmanNode root, String encodedMessage) {
    StringBuilder decodedMessage = new StringBuilder();
    HuffmanNode currentNode = root;

    for (char bit : encodedMessage.toCharArray()) {
      if (bit == '0') {
        currentNode = currentNode.getLeftNode();
      } else {
        currentNode = currentNode.getRightNode();
      }

      if (currentNode instanceof NumberNode) {
        decodedMessage.append(((NumberNode) currentNode).getNumber()).append(",");
        currentNode = root;  // Reset to root for next character
      }
    }

    return decodedMessage.toString();
  }


}
