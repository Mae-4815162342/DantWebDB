public static class Main {
  public static void main(String[] args) {
    SkipListJava<Integer, String> skip = new SkipListJava<>();
    for (int i = 20; i < 35; i++) {
      skip.add(i, String.valueOf(i));
    }
    System.out.println(skip);
    assert skip.listsize() == 10;
    int count = 0;
    for (Integer i : skip){
      assert i.equals(count++);
      skip.remove(23);
      System.out.println(skip);
      skip.remove(25);
      skip.remove(33);
      skip.remove(30);
      System.out.println(skip);
      skip.remove(28);
      skip.add(25, "25");
      System.out.println(skip);
      assert skip.listsize() == 0;
      assert skip.empty();
    }
  }
} 
