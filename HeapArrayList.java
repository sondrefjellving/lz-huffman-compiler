import java.util.ArrayList;

public class HeapArrayList {

  private int length;
  private ArrayList<HuffmanNode> nodes;

  public int over(int i) {
    return (i - 1) >> 1;
  }

  private int left(int i) {
    return (i << 1) + 1;
  }

  public int right(int i) {
    return (i + 1) << 1;
  }

  public int getLength() {
    return length;
  }

  public HeapArrayList(int len) {
    this.length = 0;
    this.nodes = new ArrayList<>(len);
  }

  public void addNode(HuffmanNode node) {
    this.nodes.add(node);
    this.length++;
    repairHeap(length - 1);
  }

  private static void swap(ArrayList<HuffmanNode> array, int i, int j) {
    HuffmanNode temp = array.get(i);
    array.set(i, array.get(j));
    array.set(j, temp);
  }

  public void repairHeap(int i) {
    int m = left(i);
    if (m < length) {
      int h = m + 1;
      if (h < length && nodes.get(h).getFrequency() > nodes.get(m).getFrequency()) {
        m = h;
      }
      if (nodes.get(m).getFrequency() > nodes.get(i).getFrequency()) {
        swap(nodes, i, m);
        repairHeap(m);
      }
    }
  }

  public void makeHeap() {
    int i = length >> 1;
    while (i-- > 0) {
      repairHeap(i);
    }
  }

  public void upgrade(int i, int p) {
    int f;
    int x = nodes.get(i).getFrequency();
    nodes.get(i).setFrequency(p + x);
    while (i > 0 && nodes.get(i).getFrequency() > nodes.get(f = over(i)).getFrequency()) {
      swap(nodes, i, f);
      i = f;
    }
  }

  public HuffmanNode getMax() {
    if (length == 0) {
      return null;
    }
    HuffmanNode maxNode = nodes.get(0); // Max element is at root in max-heap
    // Swap root with last element
    nodes.set(0, nodes.get(length - 1));
    nodes.set(length - 1, maxNode);

    // Decrease heap size and repair the heap
    length--;
    repairHeap(0);

    return maxNode;
  }

  public void heapSort() {
    // Build the initial max heap
    makeHeap();

    // Save original length
    int originalLength = length;

    // Loop to extract maximum elements and put them at the end
    for (int i = originalLength - 1; i >= 0; i--) {
      HuffmanNode maxNode = getMax(); // Extract maximum node

      // Place it at the end of the current heap
      nodes.set(i, maxNode);
    }

    // Reset length
    length = originalLength;
  }




  public HuffmanNode getMin() {
    if (length == 0) {
      return null; // Heap is empty
    }

    HuffmanNode minNode = nodes.get(0); // Initialize to root
    for (int i = 1; i < length; i++) {
      if (nodes.get(i).getFrequency() < minNode.getFrequency()) {
        minNode = nodes.get(i);
      }
    }

    return minNode;
  }

  public HuffmanNode getAndRemoveFirst() {
    if (length == 0) {
      return null;
    }

    // The first element is the root, which is the maximum in a max-heap
    HuffmanNode firstNode = nodes.get(0);

    // Swap the first node with the last node
    nodes.set(0, nodes.get(length - 1));

    // Remove the last node (which is now the firstNode)
    nodes.remove(length - 1);

    // Decrease heap size
    length--;

    // Repair the heap to maintain the max-heap property
    repairHeap(0);

    heapSort();

    return firstNode;
  }




  public void printHeap() {
    for (int i = 0; i < length; i++) {
      System.out.println(nodes.get(i).getFrequency());
    }
  }
}
