/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package arraylinkedlist;
public class ArrayLinkedListSimulation {
    private static final int INITIAL_CAPACITY = 10;
    private static final int NULL = -1;
    private static final int EMPTY_VALUE = -1;
    
    private Node[] nodes;
    private int size;
    private int head;
    private int tail;
    private int freeList;
    
    // Node class to represent each element in the linked list
    private static class Node {
        int data;
        int next;
        boolean used;
        
        Node() {
            data = EMPTY_VALUE;
            next = NULL;
            used = false;
        }
    }
    
    // Constructor
    public ArrayLinkedList() {
        nodes = new Node[INITIAL_CAPACITY];
        initializeFreeList();
        size = 0;
        head = NULL;
        tail = NULL;
    }
    
    // Initialize the free list with all available nodes
    private void initializeFreeList() {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = new Node();
            if (i < nodes.length - 1) {
                nodes[i].next = i + 1;
            } else {
                nodes[i].next = NULL;
            }
        }
        freeList = 0;
    }
    
    // Allocate a node from the free list
    private int allocateNode() {
        if (freeList == NULL) {
            expandArray();
        }
        
        int index = freeList;
        freeList = nodes[freeList].next;
        nodes[index].used = true;
        nodes[index].next = NULL;
        return index;
    }
    
    // Free a node and return it to the free list
    private void freeNode(int index) {
        nodes[index].used = false;
        nodes[index].data = EMPTY_VALUE;
        nodes[index].next = freeList;
        freeList = index;
    }
    
    // Expand the array when more space is needed
    private void expandArray() {
        int oldCapacity = nodes.length;
        int newCapacity = oldCapacity * 2;
        
        Node[] newNodes = new Node[newCapacity];
        System.arraycopy(nodes, 0, newNodes, 0, oldCapacity);
        
        // Initialize new nodes
        for (int i = oldCapacity; i < newCapacity; i++) {
            newNodes[i] = new Node();
        }
        
        // Link new nodes to free list
        for (int i = oldCapacity; i < newCapacity - 1; i++) {
            newNodes[i].next = i + 1;
        }
        newNodes[newCapacity - 1].next = NULL;
        
        // Connect old free list to new free list
        if (freeList == NULL) {
            freeList = oldCapacity;
        } else {
            // Find the end of current free list
            int current = freeList;
            while (nodes[current].next != NULL) {
                current = nodes[current].next;
            }
            nodes[current].next = oldCapacity;
        }
        
        nodes = newNodes;
        System.out.println("Array expanded to capacity: " + newCapacity);
    }
    
    // Queue operation: Add to the end (FIFO)
    public void add(int value) {
        int newIndex = allocateNode();
        nodes[newIndex].data = value;
        
        if (head == NULL) {
            // First element
            head = newIndex;
            tail = newIndex;
        } else {
            // Add to the end
            nodes[tail].next = newIndex;
            tail = newIndex;
        }
        size++;
        System.out.println("Added: " + value);
    }
    
    // Queue operation: Remove from the front (FIFO)
    public int poll() {
        if (head == NULL) {
            System.out.println("Cannot poll - list is empty");
            return EMPTY_VALUE;
        }
        
        int result = nodes[head].data;
        int oldHead = head;
        head = nodes[head].next;
        
        freeNode(oldHead);
        size--;
        
        if (head == NULL) {
            tail = NULL;
        }
        
        System.out.println("Polled: " + result);
        return result;
    }
    
    // Stack operation: Remove from the end (LIFO)
    public int pop() {
        if (tail == NULL) {
            System.out.println("Cannot pop - list is empty");
            return EMPTY_VALUE;
        }
        
        if (head == tail) {
            // Only one element
            int result = nodes[head].data;
            freeNode(head);
            head = NULL;
            tail = NULL;
            size--;
            System.out.println("Popped: " + result);
            return result;
        }
        
        // Find the node before tail
        int current = head;
        while (nodes[current].next != tail) {
            current = nodes[current].next;
        }
        
        int result = nodes[tail].data;
        freeNode(tail);
        nodes[current].next = NULL;
        tail = current;
        size--;
        
        System.out.println("Popped: " + result);
        return result;
    }
    
    // Peek at the front element
    public int peek() {
        if (head == NULL) {
            System.out.println("Cannot peek - list is empty");
            return EMPTY_VALUE;
        }
        
        int result = nodes[head].data;
        System.out.println("Peeked: " + result);
        return result;
    }
    
    // Check if the list is empty
    public boolean isEmpty() {
        return size == 0;
    }
    
    // Get the size of the list
    public int size() {
        return size;
    }
    
    // Display the current state of the list
    public void display() {
        if (head == NULL) {
            System.out.println("List is empty");
            return;
        }
        
        System.out.print("List: ");
        int current = head;
        while (current != NULL) {
            System.out.print(nodes[current].data + " ");
            current = nodes[current].next;
        }
        System.out.println("(Size: " + size + ")");
    }
    
    // Display the internal array structure (for debugging)
    public void displayInternal() {
        System.out.println("Internal array structure:");
        System.out.println("Index\tData\tNext\tUsed");
        for (int i = 0; i < nodes.length; i++) {
            System.out.println(i + "\t" + nodes[i].data + "\t" + 
                             nodes[i].next + "\t" + nodes[i].used);
        }
        System.out.println("Head: " + head + ", Tail: " + tail + ", FreeList: " + freeList);
    }
    
    // Main method to test the implementation
    public static void main(String[] args) {
        ArrayLinkedList list = new ArrayLinkedList();
        
        System.out.println("=== Testing Array-Based Linked List ===\n");
        
        // Test 1: Queue operations (FIFO)
        System.out.println("1. Testing Queue operations (FIFO):");
        list.add(10);
        list.add(20);
        list.add(30);
        list.display();
        
        list.peek();
        list.poll();
        list.display();
        
        list.poll();
        list.display();
        
        // Test 2: Stack operations (LIFO)
        System.out.println("\n2. Testing Stack operations (LIFO):");
        list.add(40);
        list.add(50);
        list.display();
        
        list.pop();
        list.display();
        
        list.pop();
        list.display();
        
        // Test 3: Mixed operations
        System.out.println("\n3. Testing Mixed operations:");
        list.add(100);
        list.add(200);
        list.add(300);
        list.add(400); // This might trigger array expansion
        list.display();
        
        list.poll(); // Remove from front (100)
        list.display();
        
        list.pop();  // Remove from end (400)
        list.display();
        
        list.peek();
        
        // Test 4: Edge cases
        System.out.println("\n4. Testing Edge cases:");
        list.poll(); // Remove 200
        list.poll(); // Remove 300 - list should be empty now
        list.display();
        
        // Try operations on empty list
        list.poll();
        list.pop();
        list.peek();
        
        // Test 5: Refill and test again
        System.out.println("\n5. Refilling and testing:");
        for (int i = 1; i <= 8; i++) {
            list.add(i * 10);
        }
        list.display();
        
        while (!list.isEmpty()) {
            list.poll();
            list.display();
        }
        
        // Display internal structure for educational purposes
        System.out.println("\n6. Internal structure overview:");
        list.displayInternal();
    }
}