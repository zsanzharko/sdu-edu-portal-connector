package kz.sdu.portal.connector.exception;

public class PortalException extends Exception {

  /**
   * Constructs a new exception with the specified detail message.  The
   * cause is not initialized, and may subsequently be initialized by
   * a call to {@link #initCause}.
   *
   * @param message the detail message. The detail message is saved for
   *                later retrieval by the {@link #getMessage()} method.
   */
  public PortalException(String message) {
    super(message);
  }
}
