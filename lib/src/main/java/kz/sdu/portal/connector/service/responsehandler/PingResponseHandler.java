package kz.sdu.portal.connector.service.responsehandler;

import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

public class PingResponseHandler implements HttpClientResponseHandler<Boolean> {
  /**
   * Processes an {@link ClassicHttpResponse} and determines whether the response is successful or not.
   *
   * @param response The response to process
   * @return {@code true} if the response is successful, otherwise {@code false}
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
