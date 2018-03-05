package vivek.wo.interfacethreadconvert;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertBackgroundThread implements Runnable, ConvertThread {
    private ConvertProxyQueue convertProxyQueue;
    private Executor executor;
    private boolean excutorActive;

    public ConvertBackgroundThread() {
        this(Executors.newCachedThreadPool());
    }

    public ConvertBackgroundThread(Executor executor) {
        this.executor = executor;
        convertProxyQueue = new ConvertProxyQueue();
    }

    @Override
    public Object enqueue(Object proxy, Method method, Object[] args) {
        ConvertProxyObject convertProxyObject = ConvertProxyObject.obtainCanvertProxyObjact(proxy, method, args);
        synchronized (this) {
            convertProxyQueue.enqueue(convertProxyObject);
            if (!excutorActive) {
                excutorActive = true;
                executor.execute(this);
            }
        }
        return null;
    }

    @Override
    public void run() {
        try {
            try {
                while (true) {
                    ConvertProxyObject convertProxyObject = convertProxyQueue.poll(1000);
                    if (convertProxyObject == null) {
                        synchronized (this) {
                            convertProxyObject = convertProxyQueue.poll();
                            if (convertProxyObject == null) {
                                excutorActive = false;
                                return;
                            }
                        }
                    }
                    convertProxyObject.invoke();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            excutorActive = false;
        }
    }
}
