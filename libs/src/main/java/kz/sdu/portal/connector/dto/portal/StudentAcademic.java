package kz.sdu.portal.connector.dto.portal;

import lombok.Data;

@Data
public class StudentAcademic implements ParseablePortalInfoResponse {
  private String studentId;
  private Integer totalEcts;
  private Integer totalEctsForSemester;
  private Integer TotalEctsRetake;
  private Integer totalEctsCompleted;
}
