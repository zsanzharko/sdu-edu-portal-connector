package kz.sdu.portal.connector.service;

import kz.sdu.portal.connector.dto.AuthorizeStudentCredential;
import kz.sdu.portal.connector.dto.portal.PortalResponse;
import kz.sdu.portal.connector.exception.PortalBadAuthorizationException;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;


public class PortalRequestAPIConnectorTest {
  private static final Properties studentCredentials = new Properties();
  private static PortalRequestAPIConnector service;

  @BeforeAll
  public static void setCredentials() throws IOException {
    String filePath = "src/test/resources/cred/credentials.properties";
    File file = new File(filePath);
    if (file.exists()) {
      studentCredentials.load(Files.newInputStream(file.toPath()));
    } else {
      studentCredentials.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath));
    }
    AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
            studentCredentials.getProperty("student.id"),
            studentCredentials.getProperty("student.password").toCharArray());

    service = new PortalRequestAPIConnector();
    service.authorize(authorizeStudentCredential);
  }

  @BeforeEach
  public void waitRequest() throws InterruptedException {
    long waitSec = (2 + ((int)(Math.random() * 5))) * 1000;
    Thread.sleep(waitSec);
  }

  @Test
  void pingWebsite() throws IOException {
    Assertions.assertTrue(service.ping());
  }

  @Test
  void authorizeCorrect() throws IOException {
    AuthorizeStudentCredential authorizeStudentCredential = new AuthorizeStudentCredential(
            studentCredentials.getProperty("student.id"),
            studentCredentials.getProperty("student.password").toCharArray());

    boolean clientAuthorized = service.authorize(authorizeStudentCredential);
    Assertions.assertTrue(clientAuthorized);
  }

  @Test
  void authorizeIncorrect() throws IOException {
    PortalRequestAPIConnector service = new PortalRequestAPIConnector();
    AuthorizeStudentCredential authorizeStudentCredential =
            new AuthorizeStudentCredential("student", new char[] {'a', 'b', 'c'});
    boolean clientAuthorized = service.authorize(authorizeStudentCredential);
    service.close();
    Assertions.assertFalse(clientAuthorized);
  }

  @Test
  void pingDashboard() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getDashboardResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingScheduler() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getScheduleResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingAttendance() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getAttendanceResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingGrades() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getGradesResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingSystemCalendar() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getSystemCalendarResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingMyAcademic() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getMyAcademicResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  @Disabled("Cause server side not handle transcript endpoint")
  void pingTranscript() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getTranscriptResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingOperationResponse() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getOperationResponse();
    Assertions.assertNotNull(response);
  }

  @Test
  void pingRegistration() throws IOException, PortalBadAuthorizationException {
    PortalResponse response = service.getRegistrationResponse();
    Assertions.assertNotNull(response);
  }

  @AfterAll
  public static void closePortalService() {
    service.close();
  }
}