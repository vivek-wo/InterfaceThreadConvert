package vivek.wo.interfacethreadconvert;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertInvocationHandler implements InvocationHandler {
    private Object proxy;
    private ConvertThread convertThread;

    public ConvertInvocationHandler(Object proxy, ConvertThread convertThread) {
        this.proxy = proxy;
        this.convertThread = convertThread;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.convertThread.enqueue(this.proxy, method, args);
    }
}
