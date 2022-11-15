package DataStructures;


import java.util.Iterator;


public class DoublyLinkedList<E extends Comparable> implements Iterable {

    public Iterator<E> iterator() {
        return new Iterator<>() {

            Node<E> cursor = header.getNext();

            public boolean hasNext() {
                return (cursor.getNext() != null);
            }

            public E next() {
                cursor = cursor.getNext();
                return cursor.getPrev().getElement();
            }


        };
    }

    private static class Node<E> {


        private E element;               // reference to the element stored at this node


        private Node<E> prev;            // reference to the previous node in the list


        private Node<E> next;            // reference to the subsequent node in the list


        public Node(E e, Node<E> p, Node<E> n) {
            element = e;
            prev = p;
            next = n;
        }

        // public accessor methods

        public E getElement() {
            return element;
        }

        public Node<E> getPrev() {
            return prev;
        }

        public Node<E> getNext() {
            return next;
        }

        // Update methods

        public void setPrev(Node<E> p) {
            prev = p;
        }

        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    // instance variables of the DoublyLinkedList
    private Node<E> header;                    // header sentinel

    private Node<E> trailer;                   // trailer sentinel

    private int size = 0;                      // number of elements in the list


    public DoublyLinkedList() {
        header = new Node<>(null, null, null);      // create header
        trailer = new Node<>(null, header, null);   // trailer is preceded by header
        header.setNext(trailer);                    // header is followed by trailer
    }

    // public accessor methods

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public E first() {
        if (isEmpty()) return null;
        return header.getNext().getElement();   // first element is beyond header
    }


    public E last() {
        if (isEmpty()) return null;
        return trailer.getPrev().getElement();    // last element is before trailer
    }


    public void addFirst(E e) {
        addBetween(e, header, header.getNext());    // place just after the header
    }


    public void addLast(E e) {
        addBetween(e, trailer.getPrev(), trailer);  // place just before the trailer
    }


    public E removeFirst() {
        if (isEmpty()) return null;                  // nothing to remove
        return remove(header.getNext());             // first element is beyond header
    }

    public E removeLast() {
        if (isEmpty()) return null;                  // nothing to remove
        return remove(trailer.getPrev());            // last element is before trailer
    }

    // private update methods

    private void addBetween(E e, Node<E> predecessor, Node<E> successor) {
        // create and link a new node
        Node<E> newest = new Node<>(e, predecessor, successor);
        predecessor.setNext(newest);
        successor.setPrev(newest);
        size++;
    }

    private E remove(Node<E> node) {
        Node<E> predecessor = node.getPrev();
        Node<E> successor = node.getNext();
        predecessor.setNext(successor);
        successor.setPrev(predecessor);
        size--;
        return node.getElement();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        Node<E> walk = header.getNext();
        while (walk != trailer) {
            sb.append(walk.getElement());
            walk = walk.getNext();
            if (walk != trailer)
                sb.append(", ");
        }
        sb.append(")");
        return sb.toString();
    }

    private void addBefore(Node<E> node, E newData) {
        Node<E> newest = new Node<>(newData, node.getPrev(), node);
        node.getPrev().setNext(newest);
        node.setPrev(newest);
        size++;
    }

    private void removeBefore(Node<E> node) {
        if (node == header)
            System.out.println("Sentimentals can not delete");
        node.setPrev(node.prev.prev);
        node.prev.setNext(node);

        size--;
    }

    private Node<E> getNode(int i) {

        if (size / 2 > i) {
            Node<E> temp = header;
            while (i >= 0) {
                temp = temp.getNext();
                i--;
            }
            return temp;
        } else {
            Node<E> temp = trailer;
            while (i <= size - 1) {
                temp = temp.getPrev();
                i++;
            }
            return temp;
        }
    }

    public E get(int i) {
        return getNode(i).getElement();
    }

    public void add(int i, E newData) {
        addBefore(getNode(i), newData);
    }

    public E remove(int i) {
        E remove = get(i);
        removeBefore(getNode(i).getNext());
        return remove;
    }

    public int find(E data) {
        Node<E> temp = header.getNext();
        int count = 1;
        while (true) {
            if (temp == trailer)
                break;
            if (temp.element.compareTo(data) == 0)
                return count;
            temp = temp.getNext();
            count++;

        }
        return -1;
    }

    private void swap(Node<E> node1, Node<E> node2) {
        if (node1.getNext() == node2 || node1.getPrev() == node2) {
            node1.setNext(node2.getNext());
            node2.setPrev(node1.getPrev());
            node1.getPrev().setNext(node2);
            node2.getNext().setPrev(node1);
            node1.setPrev(node2);
            node2.setNext(node1);
        } else {
            node2.getNext().setPrev(node1);
            node2.prev.setNext(node2.getNext());
            node2.setNext(node1.getNext());
            node1.setNext(node2.prev.getNext());
            node2.prev.setNext(node1);

            node1.getPrev().setNext(node2);
            node1.next.setPrev(node1.getPrev());
            node1.setPrev(node2.getPrev());
            node2.setPrev(node1.next.getPrev());
            node1.next.setPrev(node2);
        }

    }

    public void sort() {
        int count = size;
        int count1 = size;
        Node<E> temp = header;
        while (count1 > 1) {
            while (count > 1) {
                temp = temp.getNext();
                if (temp.getElement().compareTo(temp.getNext().getElement()) > 1) {
                    swap(temp, temp.getNext());
                    temp = temp.getPrev();

                }
                count--;
            }
            count = size;
            temp = header;
            count1--;
        }
    }


} //----------- end of DoublyLinkedList class -----------
