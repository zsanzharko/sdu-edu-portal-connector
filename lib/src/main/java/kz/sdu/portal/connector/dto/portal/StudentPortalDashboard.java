package kz.sdu.portal.connector.dto.portal;

import lombok.Data;

@Data
public class StudentPortalDashboard implements ParseablePortalInfoResponse {
  private String id;
  private String status;
  private String program;
  private Integer programCode;
  private Integer programYear;
  private String departamentCode;
  private String facultyCode;
  private String advisor;
  private Double gpa;
  private Integer docRequestCount;
  private Integer consentRequestCount;
}
