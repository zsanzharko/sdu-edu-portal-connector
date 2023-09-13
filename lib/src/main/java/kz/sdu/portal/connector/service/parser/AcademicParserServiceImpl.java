package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.StudentAcademic;
import kz.sdu.portal.connector.util.StringUtil;

import java.util.Map;

public class AcademicParserServiceImpl implements PortalResponseDataParserService<StudentAcademic> {
  private static final String TOTAL_ECTS_NAME = "totalEcts";
  private static final String TOTAL_ECTS_FOR_SEMESTER_NAME = "totalEctsForSemester";
  private static final String TOTAL_ECTS_RETAKE_NAME = "totalEctsRetake";
  private static final String TOTAL_ECTS_COMPLETED_NAME = "totalEctsCompleted";

  @Override
  public StudentAcademic parseResponse(String studentId, Map<String, Object> response) {
    StudentAcademic studentAcademic = new StudentAcademic();
    studentAcademic.setStudentId(studentId);
    studentAcademic.setTotalEcts(getEctsFromData(response, TOTAL_ECTS_NAME));
    studentAcademic.setTotalEctsForSemester(getEctsFromData(response, TOTAL_ECTS_FOR_SEMESTER_NAME));
    studentAcademic.setTotalEctsRetake(getEctsFromData(response, TOTAL_ECTS_RETAKE_NAME));
    studentAcademic.setTotalEctsCompleted(getEctsFromData(response, TOTAL_ECTS_COMPLETED_NAME));
    return studentAcademic;
  }

  private static Integer getEctsFromData(Map<String, Object> response, final String key) {
    if (response.get(key) instanceof String) {
      String v = (String) response.get(key);
      if (StringUtil.isNumber(v)) {
        return Integer.parseInt(v);
      } else {
        String e = String.format("Can't cast String '%S' to Integer, cause values is %s",
            key, v);
        throw new ClassCastException(e);
      }
    } else if (response.get(key) instanceof Integer) {
      return (Integer) response.get(key);
    } else {
      String e = String.format("Can't cast value from '%S'. Value is: '%s'",
          key, response.get(key));
      throw new ClassCastException(e);
    }
  }
}
