package exceptions;

public class ConcurrencyException extends Exception {
  public ConcurrencyException(String message) {
    super(message);
  }
}
