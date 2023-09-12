package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.StudentAcademic;

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
    studentAcademic.setTotalEcts(Integer.parseInt((String) response.get(TOTAL_ECTS_NAME)));
    studentAcademic.setTotalEctsForSemester(Integer.parseInt((String) response.get(TOTAL_ECTS_FOR_SEMESTER_NAME)));
    studentAcademic.setTotalEctsRetake((Integer)response.get(TOTAL_ECTS_RETAKE_NAME));
    studentAcademic.setTotalEctsCompleted(Integer.parseInt((String) response.get(TOTAL_ECTS_COMPLETED_NAME)));
    return studentAcademic;
  }
}
