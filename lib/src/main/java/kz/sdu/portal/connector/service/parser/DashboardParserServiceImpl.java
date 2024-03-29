package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.StudentPortalDashboard;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class DashboardParserServiceImpl implements PortalResponseDataParserService<StudentPortalDashboard> {
  private static final String STUDENT_INFO_STATUS_NAME = "status";
  private static final String STUDENT_INFO_PROGRAM_NAME = "program";
  private static final String STUDENT_INFO_PROGRAM_CODE_NAME = "program_code";
  private static final String STUDENT_INFO_DEPARTMENT_CODE_NAME = "department_code";
  private static final String STUDENT_INFO_FACULTY_CODE_NAME = "faculty_code";
  private static final String STUDENT_INFO_PROGRAM_YEAR_NAME = "program_year";
  private static final String STUDENT_INFO_ADVISER_NAME = "advisor";
  private static final String STUDENT_GPA_NAME = "gpa";
  private static final String DOC_REQUESTS_NAME = "docRequests";
  private static final String CONSENT_REQUESTS_NAME = "consentRequest";

  @Override
  public StudentPortalDashboard parseResponse(String studentId, Map<String, Object> response) {
    final StudentPortalDashboard portalDashboard = new StudentPortalDashboard();

    @SuppressWarnings("unchecked")
    Map<String, Object> studentInfo = (Map<String, Object>) response.get("studentInfo");
    @SuppressWarnings("unchecked")
    Map<String, Object> requestCount = (Map<String, Object>) response.get("requestCount");

    portalDashboard.setId(studentId);
    portalDashboard.setStatus((String) studentInfo.get(STUDENT_INFO_STATUS_NAME));
    portalDashboard.setProgram((String) studentInfo.get(STUDENT_INFO_PROGRAM_NAME));
    portalDashboard.setProgramCode(Integer.valueOf((String) studentInfo.get(STUDENT_INFO_PROGRAM_CODE_NAME)));
    portalDashboard.setDepartamentCode((String) studentInfo.get(STUDENT_INFO_DEPARTMENT_CODE_NAME));
    portalDashboard.setFacultyCode((String) studentInfo.get(STUDENT_INFO_FACULTY_CODE_NAME));
    portalDashboard.setProgramYear(Integer.valueOf((String) studentInfo.get(STUDENT_INFO_PROGRAM_YEAR_NAME)));
    portalDashboard.setGpa(Double.parseDouble((String) response.get(STUDENT_GPA_NAME)));
    portalDashboard.setAdvisor((String) studentInfo.get(STUDENT_INFO_ADVISER_NAME));
    portalDashboard.setDocRequestCount((Integer) requestCount.get(DOC_REQUESTS_NAME));
    portalDashboard.setConsentRequestCount((Integer) requestCount.get(CONSENT_REQUESTS_NAME));
    return portalDashboard;
  }
}
