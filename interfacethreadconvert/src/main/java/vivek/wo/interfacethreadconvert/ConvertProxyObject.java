package vivek.wo.interfacethreadconvert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertProxyObject {
    private final static List<ConvertProxyObject> CANVERT_PROXY_OBJECT_POOL = new ArrayList<ConvertProxyObject>();
    Object proxy;
    Method method;
    Object[] args;
    ConvertProxyObject next;

    private ConvertProxyObject(Object proxy, Method method, Object[] args) {
        this.proxy = proxy;
        this.method = method;
        this.args = args;
    }

    static ConvertProxyObject obtainCanvertProxyObjact(Object proxy, Method method, Object[] args) {
        synchronized (CANVERT_PROXY_OBJECT_POOL) {
            int size = CANVERT_PROXY_OBJECT_POOL.size();
            if (size > 0) {
                ConvertProxyObject convertProxyObject = CANVERT_PROXY_OBJECT_POOL.remove(size - 1);
                convertProxyObject.proxy = proxy;
                convertProxyObject.method = method;
                convertProxyObject.args = args;
                convertProxyObject.next = null;
                return convertProxyObject;
            }
        }
        return new ConvertProxyObject(proxy, method, args);
    }

    static void releaseCanvertProxyObjact(ConvertProxyObject convertProxyObject) {
        convertProxyObject.proxy = null;
        convertProxyObject.method = null;
        convertProxyObject.args = null;
        synchronized (CANVERT_PROXY_OBJECT_POOL) {
            if (CANVERT_PROXY_OBJECT_POOL.size() < 100) {
                CANVERT_PROXY_OBJECT_POOL.add(convertProxyObject);
            }
        }
    }

    Object invoke() {
        try {
            return method.invoke(proxy, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
