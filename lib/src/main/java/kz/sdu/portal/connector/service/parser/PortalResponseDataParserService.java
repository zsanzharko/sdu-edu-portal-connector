package kz.sdu.portal.connector.service.parser;

import kz.sdu.portal.connector.dto.portal.ParseablePortalInfoResponse;

import java.util.Map;

public interface PortalResponseDataParserService<T extends ParseablePortalInfoResponse> {

  T parseResponse(String studentId, Map<String, Object> response);
}
