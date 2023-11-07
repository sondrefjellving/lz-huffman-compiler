public class Heap {

  private int length;
  private HuffmanNode[] nodes;

  public int over(int i) {
    return (i-1) >>1;
  }

  private int left(int i) {
    return (i << 1) + 1;
  }

  public int right(int i) {
    return (i+1) << 1;
  }

  public int getLength() {
    return length;
  }

  public Heap(int len) {
    this.length = 0;
    this.nodes = new HuffmanNode[len];
  }

  public void addNode(HuffmanNode node){
    this.nodes[length++] = node;
    repairHeap(length -1);
  }

  /**
   * Swap method for swapping two integers in an array.
   *
   * @param array given array
   * @param i index of first integer
   * @param j index of second integer
   */
  private static void swap(HuffmanNode[] array, int i, int j) {
    HuffmanNode temp = array[i];
    array[i] = array[j];
    array[j] = temp;
  }

  /**
   * Repairs the heap
   * @param i the index to repair from
   */
  public void repairHeap(int i) {
    int m = left(i);
    if (m < length) {
      int h = m + 1;
      if (h < length && nodes[h].getFrequency() > nodes[m].getFrequency()) {
        m = h;
      }
      if (nodes[m].getFrequency() > nodes[i].getFrequency()) {
        swap(nodes, i, m);
        repairHeap(m);
      }
    }
  }

  /**
   * Makes the heap
   */
  public void makeHeap(){
    int i = length >> 1;
    while (i --> 0) {
      repairHeap(i);
    }
  }


  public void upgrade(int i, int p) {
    int f;
    int x = nodes[i].getFrequency();
    nodes[i].setFrequency(p+x);
    while (i > 0 && nodes[i].getFrequency() > nodes[f = over(i)].getFrequency()) {
      swap(nodes, i, f);
      i = f;
    }
  }


  public HuffmanNode getMin() {
    int min = nodes[0].getFrequency();
    if(length == 0){
      return nodes[0];
    }
    nodes[0] = nodes[--length];
    repairHeap(0);
    heapsort();
    return nodes[0];
  }

  public HuffmanNode getMax() {
    HuffmanNode max = nodes[0];
    nodes[0] = nodes[--length];
    repairHeap(0);
    return max;
  }

  public void heapsort() {
    makeHeap();
    int l = length;
    while (length > 1) {
      int x = getMax().getFrequency();
      nodes[length].setFrequency(x);
    }
    length = l;
  }

  public void printHeap() {
    for (int i = 0; i < length; i++) {
      System.out.println(nodes[i].getFrequency());
    }
  }


}