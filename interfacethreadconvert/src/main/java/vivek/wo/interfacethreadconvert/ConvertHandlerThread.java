package vivek.wo.interfacethreadconvert;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

import java.lang.reflect.Method;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertHandlerThread extends Handler implements ConvertThread {
    private int maxMillisInsideHandleMessage;
    private ConvertProxyQueue convertProxyQueue;
    private boolean handlerActive;

    public ConvertHandlerThread(Looper looper) {
        this(looper, 10);
    }

    public ConvertHandlerThread(Looper looper, int maxMillisInsideHandleMessage) {
        super(looper);
        this.maxMillisInsideHandleMessage = maxMillisInsideHandleMessage;
        convertProxyQueue = new ConvertProxyQueue();
    }

    @Override
    public Object enqueue(Object proxy, Method method, Object[] args) {
        ConvertProxyObject convertProxyObject = ConvertProxyObject.obtainCanvertProxyObjact(proxy, method, args);
        synchronized (this) {
            convertProxyQueue.enqueue(convertProxyObject);
            if (!handlerActive) {
                handlerActive = true;
                if (!sendMessage(obtainMessage())) {
                    throw new ConvertException("can not send handler message");
                }
            }
        }
        return null;
    }

    @Override
    public void handleMessage(Message msg) {
        boolean rescheduled = false;
        try {
            long started = SystemClock.uptimeMillis();
            while (true) {
                ConvertProxyObject convertProxyObject = convertProxyQueue.poll();
                if (convertProxyObject == null) {
                    synchronized (this) {
                        convertProxyObject = convertProxyQueue.poll();
                        if (convertProxyObject == null) {
                            handlerActive = false;
                            return;
                        }
                    }
                }
                convertProxyObject.invoke();
                long timeInMethod = SystemClock.uptimeMillis() - started;
                if (timeInMethod >= maxMillisInsideHandleMessage) {
                    if (!sendMessage(obtainMessage())) {
                        throw new ConvertException("can not send handler message");
                    }
                    rescheduled = true;
                    return;
                }
            }
        } finally {
            handlerActive = rescheduled;
        }
    }
}
