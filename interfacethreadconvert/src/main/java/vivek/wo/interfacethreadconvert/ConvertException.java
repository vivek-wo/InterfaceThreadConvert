package vivek.wo.interfacethreadconvert;

/**
 * Created by VIVEK-WO on 2018/3/5.
 */

public class ConvertException extends RuntimeException {

    public ConvertException() {
        super();
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertException(Throwable cause) {
        super(cause);
    }

}
