protected static class SkipListIterator<K extends Comparable<K>, V> implements Iterator<K> {
  private NodeKeyValue<K, V> listnode;
  public SkipListIterator(NodeKeyValue<K, V> listnode) {
    while (listnode.getDownList() != null){
      listnode = listnode.getDownList();
      while (listnode.getPrevious() != null){
        listnode = listnode.getPrevious();
        if (listnode.getNext() != null) {
          listnode = listnode.getNext();
        }
      }
      this.listnode = listnode;
    }
  }
  @Override
  public boolean hasNext() {
    return this.listnode != null;
  }
  @Override
  public K next() {
    K result = listnode.getKey();
    listnode = listnode.getNext();
    return result;
  }
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
  