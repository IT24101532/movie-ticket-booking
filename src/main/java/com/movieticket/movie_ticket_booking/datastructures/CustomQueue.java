package com.movieticket.movie_ticket_booking.datastructures;

public class CustomQueue<T> {    //T is a type placeholder so we can use any primitive data type
    private static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) {
            this.data = data; }
    }

    private Node<T> front;
    private Node<T> rear;
    private int size;

    public CustomQueue() {
        front  = null;
        rear = null;
        size = 0;
    }

    // Add item to the end of the queue
    public synchronized void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    // Remove and return item from the front of the queue
    public synchronized T dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue is empty");
        T item = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return item;
    }

    // we didnnt add any isFullmethod because as this dynamically growing , there is no need of it

    public synchronized boolean isEmpty() {
        return size == 0;
    }
    public synchronized int size() {
        return size;
    }
}
