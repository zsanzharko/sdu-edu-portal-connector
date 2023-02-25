package kz.sdu.portal.connector.dto.portal.attendance;

import kz.sdu.portal.connector.dto.portal.ParseablePortalInfoResponse;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Data
public class StudentPortalAttendance implements ParseablePortalInfoResponse {
  private String studentId;
  @Getter(AccessLevel.NONE)
  private List<StudentAttendanceCourse> attendanceCourseList = new ArrayList<>(10);

  public void addStudentAttendanceCourse(StudentAttendanceCourse studentAttendanceCourse) {
    attendanceCourseList.add(studentAttendanceCourse);
  }

  public List<StudentAttendanceCourse> getAttendanceCourseList() {
    return new ArrayList<>(attendanceCourseList);
  }
}
