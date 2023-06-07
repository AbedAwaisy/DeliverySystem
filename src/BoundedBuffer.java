import java.util.LinkedList;
import java.util.Queue;

class BoundedBuffer<T> {
    private Queue<T> buffer;
    private final int capacity=14;
    private boolean terminated;

    public BoundedBuffer() {
        this.buffer = new LinkedList<>();
        this.terminated = false;
    }

    public synchronized void enqueue(T item) throws InterruptedException {
        while (buffer.size() >= capacity) {
            wait(); // Wait until there is space in the buffer
        }

        buffer.add(item);
        notifyAll(); // Notify waiting threads that an item is added
    }

    public synchronized T dequeue() throws InterruptedException {
        while (buffer.isEmpty() && !terminated) {
            wait(); // Wait until there is an item in the buffer
        }
        if (terminated){
            return null;
        }
        T item = buffer.poll();
        notifyAll(); // Notify waiting threads that an item is removed
        return item;
    }

    public synchronized int size() {
        return buffer.size();
    }

    public synchronized void setTerminated() {
        this.terminated = true;
        if (terminated) {
            notifyAll(); // Notify waiting threads that termination is requested
        }
    }
}
