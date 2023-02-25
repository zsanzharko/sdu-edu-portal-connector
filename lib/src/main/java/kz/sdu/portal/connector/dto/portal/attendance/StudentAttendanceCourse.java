package kz.sdu.portal.connector.dto.portal.attendance;

import lombok.Data;

@Data
public class StudentAttendanceCourse {
  private String courseCode;
  private String title;
  private Integer absenceCount;
  private Integer attendCount;
  private Integer permitCount;
  private Integer hours;
  private Integer year;
  private Integer term;
  private Integer absence;
}
