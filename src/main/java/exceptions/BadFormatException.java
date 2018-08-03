package exceptions;

/**
 * The exception that is thrown when the format of input file is invalid.
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
public class BadFormatException extends RuntimeException {

    /**
     * Constructs an instance of <code>BadFormatException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public BadFormatException(String msg) {
        super(msg);
    }
    /**
     * Constructs an instance of <code>BadFormatException</code> with the specified detail message and cause.
     *
     * @param msg the detail message.
     * @param cause the cause.
     */
    public BadFormatException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
