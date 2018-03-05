package vivek.wo.interfacethreadconvert;

import java.lang.reflect.Method;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public interface ConvertThread {

    Object enqueue(Object proxy, Method method, Object[] args);
}
