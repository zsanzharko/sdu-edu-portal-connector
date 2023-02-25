package kz.sdu.portal.connector.dto.portal;

import lombok.Data;
import java.util.Map;

@Data
public class PortalResponse {
  private int httpStatus;
  private Map<String, Object> data;
}
