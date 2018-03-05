package vivek.wo.interfacethreadconvert;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertAsycThread implements Runnable, ConvertThread {
    private ConvertProxyQueue convertProxyQueue;
    private Executor executor;

    public ConvertAsycThread() {
        this(Executors.newCachedThreadPool());
    }

    public ConvertAsycThread(Executor executor) {
        this.executor = executor;
        convertProxyQueue = new ConvertProxyQueue();
    }

    @Override
    public Object enqueue(Object proxy, Method method, Object[] args) {
        ConvertProxyObject convertProxyObject = ConvertProxyObject.obtainCanvertProxyObjact(proxy, method, args);
        convertProxyQueue.enqueue(convertProxyObject);
        executor.execute(this);
        return null;
    }

    @Override
    public void run() {
        ConvertProxyObject convertProxyObject = convertProxyQueue.poll();
        if (convertProxyObject == null) {
            throw new IllegalStateException("No ProxyObject available");
        }
        convertProxyObject.invoke();
    }
}
