package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.StudentOperation;

import java.util.Map;

public class OperationParserServiceImpl implements PortalResponseDataParserService<StudentOperation> {
  public static final String DOC_REQUEST_NAME = "docRequest";
  public static final String CONSENT_REQUEST_NAME = "consentRequest";
  public static final String COUNT_OF_COURSE_NAME = "countOfCourse";
  public static final String COUNT_OF_WITHDRAW_NAME = "countOfWithdraw";

  @Override
  public StudentOperation parseResponse(String studentId, Map<String, Object> response) {
    StudentOperation studentOperation = new StudentOperation();

    studentOperation.setStudentId(studentId);
    studentOperation.setDocRequest((Integer) response.get(DOC_REQUEST_NAME));
    studentOperation.setConsentRequest((Integer) response.get(CONSENT_REQUEST_NAME));
    studentOperation.setCountOfCourse((Integer) response.get(COUNT_OF_COURSE_NAME));
    studentOperation.setCountOfWithdraw((Integer) response.get(COUNT_OF_WITHDRAW_NAME));

    return studentOperation;
  }
}
