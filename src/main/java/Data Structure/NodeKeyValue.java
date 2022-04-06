protected static class NodeKeyValue<K extends Comparable<K>, V> {
  private K key;
  private V value;
  private int skiplevel;
  private NodeKeyValue<K, V> up, down, next, previous;
  public NodeKeyValue(K key, V value, int skiplevel) {
    this.key = key;
    this.value = value;
    this.skiplevel = skiplevel;
  }
  @Override
  public String toString() {
    StringBuilder stringbuild = new StringBuilder();
    stringbuild.append("Node[")
    .append("key:");
    if (this.key == null){
      stringbuild.append("None");
    }
    else{
      stringbuild.append(this.key.toString());
      stringbuild.append(", value:");
      if (this.value == null){
        stringbuild.append("None");
      }
      else{
        stringbuild.append(this.value.toString());
        stringbuild.append("]");
      }
    }  
    return stringbuild.toString();
  }
  public K getKey() {
    return key;
  }
  public void setKey(K key) {
    this.key = key;
  }
  public V getValue() {
    return value;
  }
  public void setValue(V value) {
    this.value = value;
  }
  public int getLevel() {
    return skiplevel;
  }
  public void setLevel(int skiplevel) {
    this.skiplevel = skiplevel;
  }
  public NodeKeyValue<K, V> getUp() {
    return up;
  }
  public void setUp(NodeKeyValue<K, V> up) {
    this.up = up;
  }
  public NodeKeyValue<K, V> getDownList() {
    return down;
  }
  public void setDown(NodeKeyValue<K, V> down) {
    this.down = down;
  }
  public NodeKeyValue<K, V> getNext() {
    return next;
  }
  public void setNext(NodeKeyValue<K, V> next) {
    this.next = next;
  }
  public NodeKeyValue<K, V> getPrevious() {
    return previous;
  }
  public void setPreviousVal(NodeKeyValue<K, V> previous) {
    this.previous = previous;
  }
}