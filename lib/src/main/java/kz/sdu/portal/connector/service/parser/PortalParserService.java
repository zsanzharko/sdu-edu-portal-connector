package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.ParseablePortalInfoResponse;
import kz.sdu.portal.connector.exception.PortalParseResponseException;

import java.util.Map;

public class PortalParserService {
  private static final String RESPONSE_KEY_RESOLVER_NAME = "data";

  public <T extends ParseablePortalInfoResponse> T parseResponse(
          String studentId, Map<String, Object> response,
          PortalResponseDataParserService<T> parserService) throws PortalParseResponseException {
    if (response.containsKey(RESPONSE_KEY_RESOLVER_NAME)) {
      @SuppressWarnings("unchecked")
      Map<String, Object> data = (Map<String, Object>) response.get(RESPONSE_KEY_RESOLVER_NAME);
      return parserService.parseResponse(studentId, data);
    } else {
      throw new PortalParseResponseException(String.format("Impossible parse response. Can't find key: '%s'",
              RESPONSE_KEY_RESOLVER_NAME));
    }
  }
}
