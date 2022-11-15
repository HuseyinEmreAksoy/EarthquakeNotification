package DataStructures;
public class SinglyLinkedList<E>  {
 //---------------- nested Node class ----------------

 private static class Node<E> {

   private E element;            // reference to the element stored at this node

   private Node<E> next;         // reference to the subsequent node in the list


   public Node(E e, Node<E> n) {
     element = e;
     next = n;
   }

   // Accessor methods

   public E getElement() { return element; }


   public Node<E> getNext() { return next; }

   // Modifier methods

   public void setNext(Node<E> n) { next = n; }
 } //----------- end of nested Node class -----------

 // instance variables of the SinglyLinkedList
 /** The head node of the list */
 private Node<E> head = null;               // head node of the list (or null if empty)

 /** The last node of the list */
 private Node<E> tail = null;               // last node of the list (or null if empty)

 /** Number of nodes in the list */
 private int size = 0;                      // number of nodes in the list

 /** Constructs an initially empty list. */
 public SinglyLinkedList() { }              // constructs an initially empty list


 public int size() { return size; }


 public boolean isEmpty() { return size == 0; }


 public E first() {             // returns (but does not remove) the first element
   if (isEmpty()) return null;
   return head.getElement();
 }

 public E last() {              // returns (but does not remove) the last element
   if (isEmpty()) return null;
   return tail.getElement();
 }

 public void addFirst(E e) {                // adds element e to the front of the list
   head = new Node<>(e, head);              // create and link a new node
   if (size == 0)
     tail = head;                           // special case: new node becomes tail also
   size++;
 }


 public void addLast(E e) {                 // adds element e to the end of the list
   Node<E> newest = new Node<>(e, null);    // node will eventually be the tail
   if (isEmpty())
     head = newest;                         // special case: previously empty list
   else
     tail.setNext(newest);                  // new node after existing tail
   tail = newest;                           // new node becomes the tail
   size++;
 }


 public E removeFirst() {                   // removes and returns the first element
   if (isEmpty()) return null;              // nothing to remove
   E answer = head.getElement();
   head = head.getNext();                   // will become null if list had only one node
   size--;
   if (size == 0)
     tail = null;                           // special case as list is now empty
   return answer;
 }

 @SuppressWarnings({"unchecked"})
 public boolean equals(Object o) {
   if (o == null) return false;
   if (getClass() != o.getClass()) return false;
   SinglyLinkedList other = (SinglyLinkedList) o;   // use nonparameterized type
   if (size != other.size) return false;
   Node walkA = head;                               // traverse the primary list
   Node walkB = other.head;                         // traverse the secondary list
   while (walkA != null) {
     if (!walkA.getElement().equals(walkB.getElement())) return false; //mismatch
     walkA = walkA.getNext();
     walkB = walkB.getNext();
   }
   return true;   // if we reach this, everything matched successfully
 }


 public String toString() {
   StringBuilder sb = new StringBuilder("(");
   Node<E> walk = head;
   while (walk != null) {
     sb.append(walk.getElement());
     if (walk != tail)
       sb.append(", ");
     walk = walk.getNext();
   }
   sb.append(")");
   return sb.toString();
 }
}