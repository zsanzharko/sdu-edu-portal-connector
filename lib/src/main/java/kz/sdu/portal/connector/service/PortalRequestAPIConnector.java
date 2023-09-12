package kz.sdu.portal.connector.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.sdu.portal.connector.dto.AuthorizeStudentCredential;
import kz.sdu.portal.connector.dto.ClientConnectionPortalInfo;
import kz.sdu.portal.connector.dto.portal.PortalResponse;
import kz.sdu.portal.connector.enums.PortalLanguageConnector;
import kz.sdu.portal.connector.exception.PortalBadAuthorizationException;
import kz.sdu.portal.connector.service.responsehandler.MapResponseHandler;
import kz.sdu.portal.connector.service.responsehandler.PingResponseHandler;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a connector for making API requests to the portal server. It provides methods for
 * authorizing a client, retrieving various data from the portal, and logging out.
 * The class implements the AutoCloseable interface to ensure proper cleanup of resources.
 */
@Slf4j
public class PortalRequestAPIConnector implements AutoCloseable {
  private final static String BASE_URL = "https://my.sdu.edu.kz";
  private final static String LOGIN_API_NAME = "/api/login";
  private final static String LOGOUT_API_NAME = "/auth/logout";
  private final static String DASHBOARD_API_NAME = "/api/v1/my-dashboard";
  private final static String ACADEMIC_SCHEDULER_API_NAME = "/api/v1/my-academic/my-schedule";
  private final static String ACADEMIC_ATTENDANCE_API_NAME = "/api/v1/my-academic/my-attendance";
  private final static String ACADEMIC_GRADES_API_NAME = "/api/v1/my-academic/my-grades";
  private final static String ACADEMIC_SYSTEM_CALENDAR_API_NAME = "/api/v1/my-system-calendar";
  private final static String ACADEMIC_MY_ACADEMIC_API_NAME = "/api/v1/my-academic";
  private final static String ACADEMIC_TRANSCRIPT_API_NAME = "/api/v1/my-academic/my-transcript";
  private final static String OPERATION_API_NAME = "/api/v1/my-operations";
  private final static String ACADEMIC_REGISTRATION_API_NAME = "/api/v1/my-registration";

  @Getter
  private final ObjectMapper mapper = new ObjectMapper();
  private final String language;
  private final CloseableHttpClient httpClient;

  private final ClientConnectionPortalInfo client = new ClientConnectionPortalInfo();

  /**
   * Constructs a new instance of PortalRequestAPIConnector.
   * This constructor initializes the HTTP client using the default configuration
   * provided by HttpClients.createDefault(). The initial language is set to English (EN)
   * by default.
   */
  public PortalRequestAPIConnector() {
    this.httpClient = HttpClients.createDefault();
    this.language = PortalLanguageConnector.EN;
  }

  /**
   * Sends a ping request to the server.
   * @return true if the ping request is successful, otherwise false.
   * @throws IOException if there was an error executing the ping request.
   */
  public boolean ping() throws IOException {
    return httpClient.execute(new HttpGet(BASE_URL), new PingResponseHandler());
  }

  /**
   * Authorizes the student credential for accessing the portal.
   *
   * @param authorizeStudentCredential the student credential to be authorized
   * @return true if the authorization is successful, false otherwise
   * @throws IOException if there is an IO error during the authorization process
   */
  public boolean authorize(AuthorizeStudentCredential authorizeStudentCredential) throws IOException {
    if (client.validAuth()) {
      return true;
    }
    HttpPost authClient = new HttpPost(BASE_URL + LOGIN_API_NAME);
    authClient.setEntity(new StringEntity(getClientCredential(authorizeStudentCredential)));
    setDefaultPortalHeaders(authClient);

    PortalResponse clientAuthResponse = httpClient.execute(authClient, new MapResponseHandler(mapper));
    if (clientAuthResponse.getData() != null) {
      Map<String, Object> clientData = clientAuthResponse.getData();
      if (clientData.get("data") instanceof Map) {
        @SuppressWarnings("unchecked")
        Map<String, Object> data = (Map<String, Object>) clientData.get("data");
        client.setStudentCredential(authorizeStudentCredential);
        client.setTokenType((String) data.get("token_type"));
        client.setToken((String) data.get("access_token"));
        client.setTokenExpireIn(String.valueOf(data.get("expires_in")));
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves the dashboard response from the portal.
   *
   * @return the portal response representing the dashboard
   * @throws IOException if an I/O error occurs while executing the HTTP request
   * @throws PortalBadAuthorizationException if the portal authorization token is invalid or expired
   */
  public PortalResponse getDashboardResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(DASHBOARD_API_NAME);
  }

  /**
   * Retrieves the schedule response from the portal.
   *
   * @return the portal response representing the schedule
   * @throws IOException if an I/O error occurs while executing the HTTP request
   * @throws PortalBadAuthorizationException if the portal authorization token is invalid or expired
   */
  public PortalResponse getScheduleResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_SCHEDULER_API_NAME);
  }

  /**
   * Retrieves the attendance response from the portal.
   *
   * @return the portal response representing the attendance
   * @throws IOException if an I/O error occurs while executing the HTTP request
   * @throws PortalBadAuthorizationException if the portal authorization token is invalid or expired
   */
  public PortalResponse getAttendanceResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_ATTENDANCE_API_NAME);
  }

  /**
   * Retrieves the grades response from the portal.
   *
   * @return the portal response representing the grades
   * @throws IOException if an I/O error occurs while executing the HTTP request
   * @throws PortalBadAuthorizationException if the portal authorization token is invalid or expired
   */
  public PortalResponse getGradesResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_GRADES_API_NAME);
  }

  /**
   * Retrieves the system calendar response from the portal.
   *
   * @return the portal response representing the system calendar
   * @throws IOException if an I/O error occurs while executing the HTTP request
   * @throws PortalBadAuthorizationException if the portal authorization token is invalid or expired
   */
  public PortalResponse getSystemCalendarResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_SYSTEM_CALENDAR_API_NAME);
  }

  /**
   * Retrieves the academic response for the current user.
   * This method makes an HTTP GET request to the My Academic API endpoint and returns the response.
   *
   * @return The academic response as a PortalResponse object.
   * @throws IOException if there is an error during the HTTP request.
   * @throws PortalBadAuthorizationException if the authorization for accessing the API is invalid.
   */
  public PortalResponse getMyAcademicResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_MY_ACADEMIC_API_NAME);
  }

  /**
   * Retrieves the response from the academic transcript API.
   *
   * @throws IOException                       If an I/O error occurs while making the HTTP request.
   * @throws PortalBadAuthorizationException   If the authorization credentials are invalid.
   *
   * @return The portal response from the academic transcript API.
   */
  public PortalResponse getTranscriptResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_TRANSCRIPT_API_NAME);
  }

  /**
   * Retrieves the portal response for the operation.
   *
   * @return the portal response object
   * @throws IOException if there is an I/O error while retrieving the response
   * @throws PortalBadAuthorizationException if the authorization is invalid
   */
  public PortalResponse getOperationResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(OPERATION_API_NAME);
  }

  /**
   * Retrieves the registration response from the portal.
   *
   * @throws IOException                  if an I/O error occurs when making the HTTP request
   * @throws PortalBadAuthorizationException if the authorization is invalid or expired
   *
   * @return the response from the portal for the registration
   */
  public PortalResponse getRegistrationResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_REGISTRATION_API_NAME);
  }

  /**
   * Executes logout client request to the portal.
   *
   * @return true if the logout request was successful, false otherwise.
   * @throws PortalBadAuthorizationException if the authorization is invalid.
   * @throws IOException if an I/O error occurs during the execution of the request.
   */
  public boolean getLogoutClient() throws PortalBadAuthorizationException, IOException {
    HttpGet request = new HttpGet(BASE_URL + LOGOUT_API_NAME);
    setDefaultPortalHeaders(request);
    return httpClient.execute(request, new PingResponseHandler());
  }

  private String getClientCredential(AuthorizeStudentCredential authorizeStudentCredential) throws JsonProcessingException {
    Map<String, String> clientCredential = new HashMap<>(2);
    clientCredential.put(AuthorizeStudentCredential.USERNAME, authorizeStudentCredential.getStudentId());
    clientCredential.put(AuthorizeStudentCredential.PASSWORD, new String(authorizeStudentCredential.getPassword()));
    return mapper.writeValueAsString(clientCredential);
  }

  private PortalResponse getHttpGetPortalResponse(String apiName)
          throws IOException, PortalBadAuthorizationException {
    if (!client.validAuth()) {
      log.debug(String.format("%s is not authorized. Retrying authorization with client credentials...",
              client.getStudentCredential().getStudentId()));
      if (!authorize(client.getStudentCredential())) {
        throw new PortalBadAuthorizationException("Client token is null or token is expired");
      } else {
        log.debug(String.format("%s successful authorized",
                client.getStudentCredential().getStudentId()));
      }
    }
    HttpGet request = new HttpGet(BASE_URL + apiName);
    setDefaultPortalHeaders(request);
    return httpClient.execute(request, new MapResponseHandler(mapper));
  }

  private void setDefaultPortalHeaders(HttpUriRequestBase requestBase) {
    if (client.validAuth()) {
      requestBase.setHeader("Authorization", String.format("%s %s",
              client.getTokenType(),
              client.getToken()));
    }
    requestBase.setHeader("Accept", "application/json");
    requestBase.setHeader("Accept-Encoding", "gzip, deflate, br");
    requestBase.setHeader("Content-type", "application/json");
    requestBase.setHeader("Content-Language", language);
    requestBase.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, PATCH, DELETE");
    requestBase.setHeader("Cache-Control", "no-cache, private");
  }

  @Override
  public void close() {
    try {
      if (getLogoutClient()) {
        log.debug("Client is logout");
      }
      httpClient.close();
    } catch (IOException | PortalBadAuthorizationException e) {
      throw new RuntimeException(e);
    }
  }
}
