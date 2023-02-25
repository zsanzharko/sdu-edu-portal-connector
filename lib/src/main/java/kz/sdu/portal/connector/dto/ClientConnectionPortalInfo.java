package kz.sdu.portal.connector.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class ClientConnectionPortalInfo {
  private String token = null;
  private LocalDateTime tokenExpireIn = null;
  private String tokenType = null;
  private AuthorizeStudentCredential studentCredential = null;

  public void setTokenExpireIn(String tokenString) {
    //TODO: maybe hava better solution
    if (tokenString.length() == 10) {
      tokenString = String.format("%s000", tokenString);
    }
    this.tokenExpireIn = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(Long.parseLong(tokenString)), ZoneId.of("Asia/Almaty"));
  }

  public boolean validAuth() {
    boolean haveATokenExpireIn = tokenExpireIn == null;
    boolean haveAToken = token == null;
    if (haveAToken || haveATokenExpireIn) {
      return false;
    }
    return tokenExpireIn.isAfter(LocalDateTime.now());
  }
}
