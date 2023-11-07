import java.util.ArrayList;
import java.util.List;

public class Huffman {

  private HuffmanNode root;
  private List nodes;
  private String text;
  private HeapArrayList priorityQueue;

  /**
   * Constructor for Huffman class.
   *
   * @param text given a text to be encoded.
   */
  public Huffman(String text) {
    this.text = text;
    this.nodes = new ArrayList<CharNode>();
    generateCharNodes();
  }

  /**
   * Constructor for Huffman class.
   * @param nodes given a list of nodes.
   */
  public Huffman(List nodes) {
    this.nodes = nodes;
  }

  /**
   * Generates a list of CharNodes from the given text.
   */
  private void generateCharNodes() {
    char[] charArr = text.toCharArray();
    for (char c : charArr) {
        if(!checkIfCharExists(c)) {
          nodes.add(new CharNode(c, 1));
          continue;
        }
      nodes.stream()
              .filter((node) -> (((CharNode) node).getCharacter() == c))
              .forEach((node) -> ((CharNode) node).increaseFrequency());
    }
  }

  /**
   * Check if character exists in the dictionary.
   *
   * @param c given character
   * @return true if character exists, false if not.
   */
  private boolean checkIfCharExists(char c) {
    return nodes.stream()
            .anyMatch((node) -> ((CharNode) node).getCharacter() == c);
  }

  /**
   * Uses a heap to create a priority queue of characters and their frequencies.
   */
  public void priorityQueue() {
    priorityQueue = new HeapArrayList(nodes.size());
    for (Object node: nodes) {
      priorityQueue.addNode((HuffmanNode) node);
    }
    priorityQueue.heapSort();
  }

  /**
   * Creates a Huffman tree.
   */
  public void huffmanTree() {
    priorityQueue();
    while (priorityQueue.getLength() > 1) {
      priorityQueue.addNode(new HuffmanNode(priorityQueue.getAndRemoveFirst(), priorityQueue.getAndRemoveFirst()));
    }
    root = priorityQueue.getMin();
  }

  /**
   * Generates the Huffman code for each character.
   * @return the Huffman code.
   */
  public String huffmanCode() {

    huffmanTree();
    generateCodes(root, "");
    return getCodes();
  }

  /**
   * Generates the Huffman code for each character.
   *
   * @param node the node to be traversed.
   * @param code the code to be generated.
   */
  private void generateCodes(HuffmanNode node, String code) {
    if (node instanceof CharNode) {
      ((CharNode)node).setCode(code);
      return;
    }
    generateCodes(node.getLeftNode(), code + "0");
    generateCodes(node.getRightNode(), code + "1");
  }

  /*private void extendCode(long additionalCode) {
    if (encodedText == null) {
      encodedText = additionalCode; // First code
    } else {
      int length = Long.toBinaryString(additionalCode).length();
      encodedText = (encodedText << length) | additionalCode;
    }
  }*/
  

  /**
   * Gets the Huffman code for the given text.
   *
   * @return the Huffman code.
   */
  private String getCodes() {
    StringBuilder sb = new StringBuilder();
    for (char c: text.toCharArray()) {
      nodes.stream().filter((node) -> (((CharNode) node).getCharacter() == c))
              .forEach((node) -> sb.append(((CharNode) node).getCode()));
    }
    return sb.toString();
  }


  public HuffmanNode getRoot() {
    return root;
  }

  /**
   * Prints the frequency for each character.
   */
  public void printHuffmanInfo() {
    StringBuilder sb = new StringBuilder();
    for (Object charNode : nodes) {

      sb.append(((CharNode) charNode).getCharacter()).append(((CharNode) charNode).getFrequency());
    }
    System.out.println(sb.toString());
  }
}
