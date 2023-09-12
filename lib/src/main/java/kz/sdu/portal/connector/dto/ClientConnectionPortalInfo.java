package kz.sdu.portal.connector.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * This class represents the information about a client connection portal.
 */
@Data
public class ClientConnectionPortalInfo {
  private static final int REQUIRED_LENGTH = 10;
  private static final String PADDING = "000";
  private static final String ZONE_ID = "Asia/Almaty";
  private String token = null;
  private LocalDateTime tokenExpireIn = null;
  private String tokenType = null;
  private AuthorizeStudentCredential studentCredential = null;

  /**
   * Sets the bearer token expiration time using the provided token string.
   * If the length of the bearer token string is 10, it appends three zeros to its end.
   * It transforms the bearer token string to milliseconds and sets the bearer token expiration time using a LocalDateTime
   * instance with the timezone configured to "Asia/Almaty".
   *
   * @param tokenString the string representation of the bearer token expiration time
   */
  public void setTokenExpireIn(String tokenString) {
    if (tokenString.length() == REQUIRED_LENGTH) {
      tokenString += PADDING;
    }
    this.tokenExpireIn = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(Long.parseLong(tokenString)), ZoneId.of(ZONE_ID));
  }

  /**
   * Checks if the authentication is valid.
   * It checks if the token expiration time and token are not null.
   * If any of them is null, it returns false.
   * Otherwise, it checks if the token expiration time is later than the current time.
   *
   * @return true if the authentication is valid, false otherwise
   */
  public boolean validAuth() {
    boolean haveATokenExpireIn = tokenExpireIn == null;
    boolean haveAToken = token == null;
    if (haveAToken || haveATokenExpireIn) {
      return false;
    }
    return tokenExpireIn.isAfter(LocalDateTime.now());
  }
}
