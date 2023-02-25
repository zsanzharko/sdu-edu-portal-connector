package kz.sdu.portal.connector.service.responsehandler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.sdu.portal.connector.dto.portal.PortalResponse;
import kz.sdu.portal.connector.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
public class MapResponseHandler implements HttpClientResponseHandler<PortalResponse> {
  private final ObjectMapper mapper;
  private final TypeReference<Map<String, Object>> typeRef =
          new TypeReference<Map<String, Object>>() {};

  public MapResponseHandler(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  /**
   * Processes an {@link ClassicHttpResponse} and returns some value
   * corresponding to that response.
   *
   * @param response The response to process
   * @return A value determined by the response
   * @throws IOException in case of a problem or the connection was aborted
   */
  @Override
  public PortalResponse handleResponse(ClassicHttpResponse response) throws IOException {
    PortalResponse portalResponse = new PortalResponse();
    int status = response.getCode();
    if (status == 200) {
      InputStream dataStream = response.getEntity().getContent();
      String data = StringUtil.getStringFromInputStream(dataStream);
      portalResponse.setHttpStatus(status);
      portalResponse.setData(mapper.readValue(data, typeRef));
      return portalResponse;
    } if (status >= 400 && status <= 499) {
      portalResponse.setHttpStatus(status);
      portalResponse.setData(null);
      return portalResponse;
    } else {
      String content = StringUtil.getStringFromInputStream(response.getEntity().getContent());
      log.info(""+status);
      log.info(content);
    }
    return null;
  }
}
