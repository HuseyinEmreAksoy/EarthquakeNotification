package DataStructures;


public class LinkedQueue<E> implements Queue<E> {

  /** The primary storage for elements of the queue */
  private SinglyLinkedList<E> list = new SinglyLinkedList<>();   // an empty  list

  /** Constructs an initially empty queue. */
  public LinkedQueue() { }                  // new queue relies on the initially empty list

  @Override
  public int size() { return list.size(); }


  @Override
  public boolean isEmpty() { return list.isEmpty(); }


  @Override
  public void enqueue(E element) { list.addLast(element); }


  @Override
  public E first() { return list.first(); }


  @Override
  public E dequeue() { return list.removeFirst(); }


  public String toString() {
    return list.toString();
  }
}
