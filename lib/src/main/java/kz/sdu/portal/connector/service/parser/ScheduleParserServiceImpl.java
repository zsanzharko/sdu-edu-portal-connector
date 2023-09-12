package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.schedule.ScheduleCourse;
import kz.sdu.portal.connector.dto.portal.schedule.StudentSchedule;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class ScheduleParserServiceImpl implements PortalResponseDataParserService<StudentSchedule> {
  private static final String BASE_SCHEDULE_PATH = "schedule";
  private static final String BASE_TERM_PATH = "terms";
  private static final String COURSE_CODE_NAME = "ders_kod";
  private static final String COURSE_TITLE_NAME = "course_title";
  private static final String SECTION_TITLE_NAME = "section_title";
  private static final String COURSE_INSTRUCTOR_NAME = "instructor";
  private static final String COURSE_WEEK_DAY_ID_NAME = "week_d_id";
  private static final String COURSE_START_TIME_NAME = "start_time";
  private static final String COURSE_END_TIME_NAME = "end_time";
  private static final String COURSE_ROOM_CODE_NAME = "room";

  @Override
  public StudentSchedule parseResponse(String studentId, Map<String, Object> response) {
    StudentSchedule studentSchedule = new StudentSchedule();

    studentSchedule.setStudentId(studentId);

    @SuppressWarnings("unchecked")
    List<Map<String, Object>> scheduleResponseList = (List<Map<String, Object>>) response.get(BASE_SCHEDULE_PATH);
    @SuppressWarnings("unchecked")
    List<String> termResponseList = (List<String>) response.get(BASE_TERM_PATH);

    scheduleResponseList.forEach(scheduleResponse -> addScheduleCourseFromResponse(studentSchedule, scheduleResponse));
    termResponseList.forEach(term -> addYearTermFromResponse(studentSchedule, term));

    return studentSchedule;
  }

  private void addScheduleCourseFromResponse(StudentSchedule studentSchedule, Map<String, Object> response) {
    ScheduleCourse scheduleCourse = new ScheduleCourse();

    scheduleCourse.setCourseCode((String) response.get(COURSE_CODE_NAME));
    scheduleCourse.setCourseTitle((String) response.get(COURSE_TITLE_NAME));
    scheduleCourse.setSectionTitle((String) response.get(SECTION_TITLE_NAME));
    scheduleCourse.setInstructor((String) response.get(COURSE_INSTRUCTOR_NAME));
    scheduleCourse.setWeekDayId(Integer.valueOf((String) response.get(COURSE_WEEK_DAY_ID_NAME)));
    scheduleCourse.setStartTime(LocalTime.parse((String) response.get(COURSE_START_TIME_NAME)));
    scheduleCourse.setEndTime(LocalTime.parse((String) response.get(COURSE_END_TIME_NAME)));
    scheduleCourse.setRoomCode((String) response.get(COURSE_ROOM_CODE_NAME));

    studentSchedule.addScheduleCourse(scheduleCourse);
  }

  private void addYearTermFromResponse(StudentSchedule studentSchedule, String termString) {
    //TODO next version, need to refactor using regex
    Integer term = Integer.parseInt(termString.substring(0, 1));
    Integer year = Integer.parseInt(termString.substring(10));

    studentSchedule.addYearTermScheduleRate(year, term);
  }
}
