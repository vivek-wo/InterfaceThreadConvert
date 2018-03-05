package vivek.wo.interfacethreadconvert;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertProxyQueue {
    private ConvertProxyObject head;
    private ConvertProxyObject tail;

    public synchronized void enqueue(ConvertProxyObject convertProxyObject) {
        if (convertProxyObject == null) {
            throw new NullPointerException("ConvertProxyObject cannot be null");
        }
        if (tail != null) {
            tail.next = convertProxyObject;
            tail = convertProxyObject;
        } else if (head == null) {
            head = tail = convertProxyObject;
        } else {
            throw new IllegalStateException("Head present , no tail");
        }
        notifyAll();
    }

    public synchronized ConvertProxyObject poll() {
        ConvertProxyObject convertProxyObject = head;
        if (head != null) {
            head = head.next;
            if (head == null) {
                tail = null;
            }
        }
        return convertProxyObject;
    }

    public synchronized ConvertProxyObject poll(int maxMillisToWait) throws InterruptedException {
        if (head == null) {
            wait(maxMillisToWait);
        }
        return poll();
    }
}
