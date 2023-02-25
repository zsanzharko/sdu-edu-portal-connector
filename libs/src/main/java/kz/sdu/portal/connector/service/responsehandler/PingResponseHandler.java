package kz.sdu.portal.connector.service.responsehandler;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

public class PingResponseHandler implements HttpClientResponseHandler<Boolean> {
  /**
   * Processes an {@link ClassicHttpResponse} and returns some value
   * corresponding to that response.
   *
   * @param response The response to process
   * @return A value determined by the response
   */
  @Override
  public Boolean handleResponse(ClassicHttpResponse response) {
    int status = response.getCode();
    if (status >= 200 && status < 300) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }
}
