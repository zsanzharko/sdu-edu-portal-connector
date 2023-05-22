package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.AuthorizeStudentCredential;
import kz.sdu.portal.connector.dto.portal.PortalResponse;
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

    PortalParserService.parseResponse(studentId,
            response.getData(), new DashboardParserServiceImpl()
    );
  }

  @Test
  void getDtoScheduler() throws IOException, PortalException {
    PortalResponse response = service.getScheduleResponse();
    Assertions.assertNotNull(response);

    PortalParserService.parseResponse(
            studentId, response.getData(), new ScheduleParserServiceImpl()
    );
  }

  @Test
  void getDtoAttendance() throws IOException, PortalException {
    PortalResponse response = service.getAttendanceResponse();
    Assertions.assertNotNull(response);

    PortalParserService.parseResponse(
            studentId, response.getData(), new AttendanceParserServiceImpl()
    );
  }

  @Test
  void getDtoMyAcademic() throws IOException, PortalException {
    PortalResponse response = service.getMyAcademicResponse();
    Assertions.assertNotNull(response);

    PortalParserService.parseResponse(
            studentId, response.getData(), new AcademicParserServiceImpl()
    );
  }

  @Test
  void getDtoOperationResponse() throws IOException, PortalException {
    PortalResponse response = service.getOperationResponse();
    Assertions.assertNotNull(response);

    PortalParserService.parseResponse(
            studentId, response.getData(), new OperationParserServiceImpl()
    );
  }

  @AfterAll
  public static void tearDown() {
    service.close();
  }
}