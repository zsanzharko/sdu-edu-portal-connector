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

@Slf4j
public class PortalRequestAPIService implements AutoCloseable {
  private final static String BASE_URL = "https://my.sdu.edu.kz/";
  private final static String LOGIN_API_NAME = "api/login";
  private final static String LOGOUT_API_NAME = "auth/logout";
  private final static String DASHBOARD_API_NAME = "api/v1/my-dashboard";
  private final static String ACADEMIC_SCHEDULER_API_NAME = "api/v1/my-academic/my-schedule";
  private final static String ACADEMIC_ATTENDANCE_API_NAME = "api/v1/my-academic/my-attendance";
  private final static String ACADEMIC_GRADES_API_NAME = "api/v1/my-academic/my-grades";
  private final static String ACADEMIC_SYSTEM_CALENDAR_API_NAME = "api/v1/my-system-calendar";
  private final static String ACADEMIC_MY_ACADEMIC_API_NAME = "api/v1/my-academic";
  private final static String ACADEMIC_TRANSCRIPT_API_NAME = "api/v1/my-academic/my-transcript";
  private final static String OPERATION_API_NAME = "api/v1/my-operations";
  private final static String ACADEMIC_REGISTRATION_API_NAME = "api/v1/my-registration";

  @Getter
  private final ObjectMapper mapper = new ObjectMapper();
  private final String language;
  private final CloseableHttpClient httpClient;

  private final ClientConnectionPortalInfo client = new ClientConnectionPortalInfo();

  public PortalRequestAPIService() {
    this.httpClient = HttpClients.createDefault();
    this.language = PortalLanguageConnector.EN;
  }

  public boolean ping() throws IOException {
    return httpClient.execute(new HttpGet(BASE_URL), new PingResponseHandler());
  }

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

  public PortalResponse getDashboardResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(DASHBOARD_API_NAME);
  }

  public PortalResponse getScheduleResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_SCHEDULER_API_NAME);
  }

  public PortalResponse getAttendanceResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_ATTENDANCE_API_NAME);
  }

  public PortalResponse getGradesResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_GRADES_API_NAME);
  }

  public PortalResponse getSystemCalendarResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_SYSTEM_CALENDAR_API_NAME);
  }

  public PortalResponse getMyAcademicResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_MY_ACADEMIC_API_NAME);
  }

  public PortalResponse getTranscriptResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_TRANSCRIPT_API_NAME);
  }

  public PortalResponse getOperationResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(OPERATION_API_NAME);
  }

  public PortalResponse getRegistrationResponse() throws IOException, PortalBadAuthorizationException {
    return getHttpGetPortalResponse(ACADEMIC_REGISTRATION_API_NAME);
  }

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
