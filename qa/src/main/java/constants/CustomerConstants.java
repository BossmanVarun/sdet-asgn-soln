package constants;

import java.util.Map;

public class CustomerConstants {

  public static final String BASE_PATH = "/api";
  public static final Map<String, ?> HEADERS =
      Map.of("x-session-token", "authorized-user", "user-agent", "any");
}
