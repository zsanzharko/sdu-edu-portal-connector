package kz.sdu.portal.connector.dto.portal.schedule;

import kz.sdu.portal.connector.dto.portal.ParseablePortalInfoResponse;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StudentSchedule implements ParseablePortalInfoResponse {
  private String studentId;
  private List<ScheduleCourse> scheduleCourseList = new ArrayList<>(10);
  private Map<Integer, Integer> yearTermScheduleRate = new HashMap<>(12);

  public boolean addScheduleCourse(ScheduleCourse scheduleCourse) {
    return scheduleCourseList.add(scheduleCourse);
  }

  public void addYearTermScheduleRate(Integer year, Integer term) {
    yearTermScheduleRate.put(year, term);
  }

  public List<ScheduleCourse> getScheduleCourseList() {
    return new ArrayList<>(scheduleCourseList);
  }

  public Map<Integer, Integer> getYearTermScheduleRate() {
    return new HashMap<>(yearTermScheduleRate);
  }
}
