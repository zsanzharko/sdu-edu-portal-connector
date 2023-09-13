package kz.sdu.portal.connector.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringUtil {

  public static String getStringFromInputStream(InputStream stream) throws IOException {
    InputStreamReader isReader = new InputStreamReader(stream);
    BufferedReader reader = new BufferedReader(isReader);
    StringBuilder sb = new StringBuilder();
    String str;
    while ((str = reader.readLine()) != null) {
      sb.append(str);
    }
    return sb.toString();
  }

  public static boolean isNumber(String s) {
    for (char c : s.toCharArray()) {
      if (!Character.isDigit(c)) return false;
    }
    return true;
  }
}
