package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.AuthorizeStudentCredential;
import kz.sdu.portal.connector.dto.portal.PortalResponse;
import kz.sdu.portal.connector.dto.portal.StudentAcademic;
import kz.sdu.portal.connector.dto.portal.StudentOperation;
import kz.sdu.portal.connector.dto.portal.StudentPortalDashboard;
import kz.sdu.portal.connector.dto.portal.attendance.StudentPortalAttendance;
import kz.sdu.portal.connector.dto.portal.schedule.StudentSchedule;
import kz.sdu.portal.connector.exception.PortalException;
import kz.sdu.portal.connector.service.PortalRequestAPIConnector;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

class PortalResponseDataParserServiceTest {
  private static final Properties studentCredentials = new Properties();
  private static PortalRequestAPIConnector service;
  private static final PortalParserService portalParserService = new PortalParserService();
  private static String studentId;

  @BeforeAll
  public static void setUp() throws IOException {
    String filePath = "src/test/resources/cred/credentials.properties";
    File file = new File(filePath);
    if (file.exists()) {
      studentCredentials.load(Files.newInputStream(file.toPath()));
    } else {
      studentCredentials.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath));
    }
    studentId = studentCredentials.getProperty("student.id");
    AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
            studentId,
            studentCredentials.getProperty("student.password").toCharArray());

    service = new PortalRequestAPIConnector();
    service.authorize(authorizeStudentCredential);
  }

  @BeforeEach
  public void waitRequest() throws InterruptedException {
    long waitSec = (2 + ((int) (Math.random() * 5))) * 1000;
    Thread.sleep(waitSec);
  }

  @Test
  void getDtoDashboard() throws IOException, PortalException {
    PortalResponse response = service.getDashboardResponse();
    Assertions.assertNotNull(response);

    StudentPortalDashboard dashboard = portalParserService.parseResponse(studentId,
            response.getData(), new DashboardParserServiceImpl()
    );

    System.out.println("getDtoDashboard");
    System.out.println(dashboard);
  }

  @Test
  void getDtoScheduler() throws IOException, PortalException {
    PortalResponse response = service.getScheduleResponse();
    Assertions.assertNotNull(response);

    StudentSchedule studentSchedule = portalParserService.parseResponse(
            studentId, response.getData(), new ScheduleParserServiceImpl()
    );

    System.out.println("getDtoScheduler");
    System.out.println(studentSchedule);
  }

  @Test
  void getDtoAttendance() throws IOException, PortalException {
    PortalResponse response = service.getAttendanceResponse();
    Assertions.assertNotNull(response);

    StudentPortalAttendance studentPortalAttendance = portalParserService.parseResponse(
            studentId, response.getData(), new AttendanceParserServiceImpl()
    );

    System.out.println("getDtoAttendance");
    System.out.println(studentPortalAttendance);
  }

  @Test
  void getDtoMyAcademic() throws IOException, PortalException {
    PortalResponse response = service.getMyAcademicResponse();
    Assertions.assertNotNull(response);

    StudentAcademic studentAcademic = portalParserService.parseResponse(
            studentId, response.getData(), new AcademicParserServiceImpl()
    );
    System.out.println("getDtoMyAcademic");
    System.out.println(studentAcademic);
  }

  @Test
  void getDtoOperationResponse() throws IOException, PortalException {
    PortalResponse response = service.getOperationResponse();
    Assertions.assertNotNull(response);

    StudentOperation studentOperation = portalParserService.parseResponse(
            studentId, response.getData(), new OperationParserServiceImpl()
    );

    System.out.println("getDtoOperationResponse");
    System.out.println(studentOperation);
  }

  @AfterAll
  public static void tearDown() {
    service.close();
  }
}