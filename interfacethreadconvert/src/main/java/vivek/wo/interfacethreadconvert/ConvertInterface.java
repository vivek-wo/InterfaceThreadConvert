package vivek.wo.interfacethreadconvert;

import android.os.Looper;

import java.lang.reflect.Proxy;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertInterface {

    public static <T> T canvertInterfaceToMainHandlerThread(Class<T> cls, T t) {
        return canvertInterfaceToHandlerThread(cls, t, new ConvertHandlerThread(Looper.getMainLooper()));
    }

    public static <T> T canvertInterfaceToHandlerThread(Class<T> cls, T t, ConvertThread convertThread) {
        ConvertInvocationHandler convertInvocationHandler = new ConvertInvocationHandler(t, convertThread);
        return (T) Proxy.newProxyInstance(cls.getClassLoader(),
                new Class[]{cls},
                convertInvocationHandler);
    }
}
