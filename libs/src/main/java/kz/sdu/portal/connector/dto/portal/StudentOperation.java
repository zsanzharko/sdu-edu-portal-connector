package kz.sdu.portal.connector.dto.portal;

import lombok.Data;

@Data
public class StudentOperation implements ParseablePortalInfoResponse {
  private String studentId;
  private Integer docRequest;
  private Integer consentRequest;
  private Integer countOfCourse;
  private Integer countOfWithdraw;
}
