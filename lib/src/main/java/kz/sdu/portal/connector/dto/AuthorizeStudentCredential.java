package kz.sdu.portal.connector.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * The {@code AuthorizeStudentCredential} class represents the credentials of a student for authentication purposes.
 * It stores the student's ID and password.
 */
@RequiredArgsConstructor
@Getter
public class AuthorizeStudentCredential {
  public static final String USERNAME = "student";
  public static final String PASSWORD = "password";
  private final String studentId;
  private final char[] password;
}
