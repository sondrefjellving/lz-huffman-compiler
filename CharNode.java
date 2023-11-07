/**
 * Represents a node in the Huffman tree that contains a character.
 * The character is the leaf of the tree.
 * @author johanneslorentzen
 */
public class CharNode extends HuffmanNode {

  private final char character;
  private String code;

  /**
   * Constructor for CharNode.
   *
   * @param character character
   * @param frequency frequency of the character
   */
  public CharNode(char character, int frequency) {
    super(frequency);
    this.character = character;
  }

  public char getCharacter() {
    return character;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String  getCode() {
    return code;
  }

  @Override
  public String toString() {
    return character + " " + getFrequency();
  }
}
