import java.util.Iterator;
import java.util.Random;
import java.util.NoSuchElementException;
public class SkipListJava<K extends Comparable<K>, V> implements Iterable<K> {
  private int listsize;
  private double pb;
  protected static final Random randomGen = new Random();
  protected static final double DEFAULT_PB = 0.5;
  private NodeKeyValue<K, V> head;
  
  public SkipListJava() {
    this(DEFAULT_PB);
  }
  public SkipListJava(double pb) {
    this.head = new NodeKeyValue<K, V>(null, null, 0);
    this.pb = pb;
    this.listsize = 0;
  }
  public V get(K key) {
    checkKeyValid(key);
    NodeKeyValue<K, V> listnode = findNode(key);
    if (listnode.getKey().compareTo(key) == 0)
      return listnode.getValue();
    else
      return null;
  }
  public void add(K key, V value) {
    checkKeyValid(key);
    NodeKeyValue<K, V> listnode = findNode(key);
    if (listnode.getKey() != null && listnode.getKey().compareTo(key) == 0) {
      listnode.setValue(value);
      return;
    }
    NodeKeyValue<K, V> newlistNode = new NodeKeyValue<K, V>(key, value, listnode.getLevel());
    horizontalInsertList(listnode, newlistNode);
    int curLevel = listnode.getLevel();
    int headlistLevel = head.getLevel();
    while (isBuildLevel()) {
      if (curLevel >= headlistLevel) {
        NodeKeyValue<K, V> newHeadEle = new NodeKeyValue<K, V>(null, null, headlistLevel + 1);
        verticalLink(newHeadEle, head);
        head = newHeadEle;
        headlistLevel = head.getLevel();
      }
      while (listnode.getUp() == null) {
        listnode = listnode.getPrevious();
      }
      listnode = listnode.getUp();
      NodeKeyValue<K, V> tmp = new NodeKeyValue<K, V>(key, value, listnode.getLevel());
      horizontalInsertList(listnode, tmp);
      verticalLink(tmp, newlistNode);
      newlistNode = tmp;
      curLevel++;
    }
    listsize++;
  }
  public void remove(K key) {
    checkKeyValid(key);
    NodeKeyValue<K, V> listnode = findNode(key);
    if (listnode == null || listnode.getKey().compareTo(key) != 0)
      throw new NoSuchElementException("Key does not exist!");
    while (listnode.getDownList() != null)
      listnode = listnode.getDownList();
      NodeKeyValue<K, V> previous = null;
      NodeKeyValue<K, V> next = null;
      for (; listnode != null; listnode = listnode.getUp()) {
      previous = listnode.getPrevious();
      next = listnode.getNext();
      if (previous != null)
        previous.setNext(next);
      if (next != null)
        next.setPreviousVal(previous);
      }
      while (head.getNext() == null && head.getDownList() != null) {
      head = head.getDownList();
      head.setUp(null);
    }
    listsize--;
  }
  public boolean contains(K key) {
    return get(key) != null;
  }
  public int listsize() {
    return listsize;
  }
  public boolean empty() {
    return listsize == 0;
  }
  protected NodeKeyValue<K, V> findNode(K key) {
    NodeKeyValue<K, V> listnode = head;
    NodeKeyValue<K, V> next = null;
    NodeKeyValue<K, V> down = null;
    K nodeKey = null;
    while (true) {
      next = listnode.getNext();
      while (next != null && lessThanEqual(next.getKey(), key)) {
        listnode = next;
        next = listnode.getNext();
      }
      nodeKey = listnode.getKey();
      if (nodeKey != null && nodeKey.compareTo(key) == 0){
        break;
      }
      down = listnode.getDownList();
      if (down != null) {
        listnode = down;
      } else {
        break;
      }
    }
    return listnode;
  }
  protected void checkKeyValid(K key) {
    if (key == null) throw new IllegalArgumentException("Key must be not null!");
  }
  protected boolean lessThanEqual(K a, K b) {
    return a.compareTo(b) <= 0;
  }
  protected boolean isBuildLevel() {
    return randomGen.nextDouble() < pb;
  }
  protected void horizontalInsertList(NodeKeyValue<K, V> a, NodeKeyValue<K, V> b) {
    b.setPreviousVal(a);
    b.setNext(a.getNext());
    if (a.getNext() != null){
      a.getNext().setPreviousVal(b);
      a.setNext(b);
    }
  }
  protected void verticalLink(NodeKeyValue<K, V> a, NodeKeyValue<K, V> b) {
    a.setDown(b);
    b.setUp(a);
  }
  @Override
  public String toString() {
    StringBuilder stringbuild = new StringBuilder();
    NodeKeyValue<K, V> listnode = head;
    while (listnode.getDownList() != null) { 
      listnode = listnode.getDownList();
      while (listnode.getPrevious() != null){
        listnode = listnode.getPrevious();
        if (listnode.getNext() != null){
          listnode = listnode.getNext();
          while (listnode != null) {
            stringbuild.append(listnode.toString()).append("\n");
            listnode = listnode.getNext();
          }
        }
      }
    }
    return stringbuild.toString();
  }
  @Override
  public Iterator<K> iterator() {
    return new SkipListIterator<K, V>(head);
  }
}