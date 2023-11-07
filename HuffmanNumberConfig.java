import java.util.ArrayList;
import java.util.List;

public class HuffmanNumberConfig {

  private HuffmanNode root;
  private List nodes;
  private String text;
  private HeapArrayList priorityQueue;

  /**
   * Constructor for Huffman class.
   *
   * @param text given a text to be encoded.
   */
  public HuffmanNumberConfig(String text) {
    this.text = text;
    this.nodes = new ArrayList<NumberNode>();
    generateNumberNodes();
  }

  /**
   * Constructor for Huffman class.
   * @param nodes given a list of nodes.
   */
  public HuffmanNumberConfig(List nodes) {
    this.nodes = nodes;
  }

  /**
   * Generates a list of NumberNodes from the given text.
   */
  private void generateNumberNodes() {
    String[] numberArr = text.replace(",", " ").trim().split("  ");
    for (String num : numberArr) {
      int n = Integer.parseInt(num);
      if(!checkIfCharExists(n)) {
        nodes.add(new NumberNode(n, 1));
        continue;
      }
      nodes.stream()
              .filter((node) -> (((NumberNode) node).getNumber() == n))
              .forEach((node) -> ((NumberNode) node).increaseFrequency());
    }
  }

  /**
   * Check if the number exists in the dictionary.
   *
   * @param num given number
   * @return true if number exists, false if not.
   */
  private boolean checkIfCharExists(int num) {
    return nodes.stream()
            .anyMatch((node) -> ((NumberNode) node).getNumber() == num);
  }

  /**
   * Uses a heap to create a priority queue of numbers and their frequencies.
   */
  public void priorityQueue() {
    priorityQueue = new HeapArrayList(nodes.size());
    nodes.stream().forEach((node) -> priorityQueue.addNode((NumberNode) node));
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
   * Generates the Huffman code for each number.
   * @return the Huffman code.
   */
  public String huffmanCode() {

    huffmanTree();
    generateCodes(root, "");
    return getCodes();
  }

  /**
   * Generates the Huffman code for each number.
   *
   * @param node the node to be traversed.
   * @param code the code to be generated.
   */
  private void generateCodes(HuffmanNode node, String code) {
    if (node instanceof NumberNode) {
      ((NumberNode)node).setCode(code);
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
    for (String num: text.replace(",", " ").trim().split("  ")) {
      int n = Integer.parseInt(num.replace(",", " ").trim());
      nodes.stream().filter((node) -> (((NumberNode) node).getNumber() == n))
              .forEach((node) -> sb.append(((NumberNode) node).getCode()));
    }
    return sb.toString();
  }


  public HuffmanNode getRoot() {
    return root;
  }

  /**
   * Prints the frequency for each number.
   */
  public String getFrequencyTable() {
    StringBuilder sb = new StringBuilder();
    for (Object numNode : nodes) {
      sb.append(((NumberNode) numNode).getNumber()).append(":").append(((NumberNode) numNode).getFrequency()).append(",");
    }
    return sb.toString();
  }


}
