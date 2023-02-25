package kz.sdu.portal.connector.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class AuthorizeStudentCredential {
  public static final String USERNAME = "student";
  public static final String PASSWORD = "password";
  private final String studentId;
  private final char[] password;
}
