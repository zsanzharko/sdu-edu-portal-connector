package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.attendance.StudentAttendanceCourse;
import kz.sdu.portal.connector.dto.portal.attendance.StudentPortalAttendance;

import java.util.List;
import java.util.Map;

public class AttendanceParserServiceImpl implements PortalResponseDataParserService<StudentPortalAttendance> {
  private static final String BASE_PATH_NAME = "attendance";
  private static final String COURSE_CODE_NAME = "ders_kod";
  private static final String COURSE_TITLE_NAME = "title";
  private static final String COURSE_ABSENCE_COUNT_NAME = "absence_count";
  private static final String COURSE_ATTEND_COUNT_NAME = "attend_count";
  private static final String COURSE_PERMIT_COUNT_NAME = "permit_count";
  private static final String COURSE_HOURS_NAME = "hour_num";
  private static final String COURSE_YEAR_NAME = "year";
  private static final String COURSE_TERM_NAME = "term";
  private static final String COURSE_ABSENCE_NAME = "absence";

  @Override
  public StudentPortalAttendance parseResponse(String studentId, Map<String, Object> response) {
    final StudentPortalAttendance portalAttendance = new StudentPortalAttendance();
    @SuppressWarnings("unchecked")
    final Map<String, List<Object>> attendance = (Map<String, List<Object>>) response.get(BASE_PATH_NAME);

    portalAttendance.setStudentId(studentId);

    for (List<Object> o : attendance.values()) {
      for (Object attendanceCourseObject : o) {
        @SuppressWarnings("unchecked")
        Map<String, Object> attendanceCourse = (Map<String, Object>) attendanceCourseObject;
        addAttendanceCourseFromResponse(portalAttendance, attendanceCourse);
      }
    }

    return portalAttendance;
  }

  private void addAttendanceCourseFromResponse(StudentPortalAttendance studentPortalAttendance,
                                               Map<String, Object> attendanceCourse) {
    StudentAttendanceCourse studentAttendanceCourse = new StudentAttendanceCourse();

    studentAttendanceCourse.setCourseCode((String) attendanceCourse.get(COURSE_CODE_NAME));
    studentAttendanceCourse.setTitle((String) attendanceCourse.get(COURSE_TITLE_NAME));
    studentAttendanceCourse.setAbsenceCount(Integer.parseInt((String) attendanceCourse.get(COURSE_ABSENCE_COUNT_NAME)));
    studentAttendanceCourse.setAttendCount(Integer.parseInt((String) attendanceCourse.get(COURSE_ATTEND_COUNT_NAME)));
    studentAttendanceCourse.setPermitCount(Integer.parseInt((String) attendanceCourse.get(COURSE_PERMIT_COUNT_NAME)));
    studentAttendanceCourse.setHours(Integer.parseInt((String) attendanceCourse.get(COURSE_HOURS_NAME)));
    studentAttendanceCourse.setYear(Integer.parseInt((String) attendanceCourse.get(COURSE_YEAR_NAME)));
    studentAttendanceCourse.setTerm(Integer.parseInt((String) attendanceCourse.get(COURSE_TERM_NAME)));
    studentAttendanceCourse.setAbsence(Integer.parseInt((String) attendanceCourse.get(COURSE_ABSENCE_NAME)));

    studentPortalAttendance.addStudentAttendanceCourse(studentAttendanceCourse);
  }
}
