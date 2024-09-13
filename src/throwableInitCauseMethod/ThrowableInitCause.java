package throwableInitCauseMethod;

public class ThrowableInitCause {
    public static void main(String[] args) {
        try {
            String text = null;
            text.length();
        } catch (NullPointerException exception){
            Throwable t = exception.getCause();
            System.out.println("Cause is: " + exception.getCause());

            exception.initCause(null);

            exception.initCause(new ArithmeticException("This is the cause"));
        }
    }
}
