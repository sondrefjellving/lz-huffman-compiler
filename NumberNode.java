/**
 * Represents a node in the Huffman tree that contains a character.
 * The character is the leaf of the tree.
 * @author johanneslorentzen
 */
public class NumberNode extends HuffmanNode {

  private final int number;
  private String code;

  /**
   * Constructor for CharNode.
   *
   * @param number character
   * @param frequency frequency of the character
   */
  public NumberNode(int number, int frequency) {
    super(frequency);
    this.number = number;
  }

  public int getNumber() {
    return number;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }

  @Override
  public String toString() {
    return number + " " + getFrequency();
  }
}
