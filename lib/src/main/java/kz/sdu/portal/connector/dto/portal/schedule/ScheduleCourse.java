package kz.sdu.portal.connector.dto.portal.schedule;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ScheduleCourse {
  private String courseCode;
  private String courseTitle;
  private String sectionTitle;
  private String instructor;
  private Integer weekDayId;
  private LocalTime startTime;
  private LocalTime endTime;
  private String roomCode;
}
