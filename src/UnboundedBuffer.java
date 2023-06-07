import java.util.LinkedList;
import java.util.Queue;

public class UnboundedBuffer<T> {
    private Queue<T> queue;
    private boolean terminated;
    private boolean driverUpdate;


    public UnboundedBuffer() {
        queue = new LinkedList<>();
    }

    public synchronized void enqueue(T item) {
        queue.offer(item);
        notifyAll(); // Notify waiting threads that an item is added
    }

    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty() && !terminated && !driverUpdate) {
            wait(); // Wait until there is an item in the buffer
        }
        if (terminated||driverUpdate){
            return null;
        }


        T item = queue.poll();
        notifyAll();
        return item;
    }

    public synchronized T peek() {
        return queue.peek();
    }

    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }

    public synchronized int size() {
        return queue.size();
    }

    public synchronized void clear() {
        queue.clear();
    }

    public synchronized void setTerminated() {
        this.terminated = true;
        if (terminated) {
            notifyAll(); // Notify waiting threads that termination is requested
        }
    }

    public synchronized void driverUpdate(boolean update) {
        driverUpdate=update;
        if (driverUpdate) {
            notifyAll(); // Notify waiting threads that termination is requested
        }
    }
}
