/**
 * Class represents a node in a Huffman encoding tree.
 * @author johanneslorentzen
 */
public class HuffmanNode implements Comparable<HuffmanNode> {

  private HuffmanNode leftNode;
  private HuffmanNode rightNode;
  private int frequency;

  public HuffmanNode(HuffmanNode leftNode, HuffmanNode rightNode) {
    this.leftNode = leftNode;
    this.rightNode = rightNode;
    this.frequency = leftNode.getFrequency() + rightNode.getFrequency();
  }

  public HuffmanNode(int frequency) {
    this.leftNode = null;
    this.rightNode = null;
    this.frequency = frequency;
  }



  public HuffmanNode getLeftNode() {
    return leftNode;
  }

  public HuffmanNode getRightNode() {
    return rightNode;
  }


  public int getFrequency(){
    return frequency;
  }

  public void increaseFrequency() {
    this.frequency++;
  }

  public void setFrequency(int frequency) {
    this.frequency = frequency;
  }

  @Override
  public int compareTo(HuffmanNode node) {
    return Integer.compare(frequency, node.getFrequency());
  }
}
