package utils;

import constants.CustomerConstants;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;

@Data
public class CustomerHelper {

  private String baseUrl;

  private RequestSpecification initReqSpec() {
    return RestAssured.given()
        .baseUri(baseUrl)
        .contentType(ContentType.JSON)
        .headers(CustomerConstants.HEADERS);
  }

  public Response postCustomer(Object c) {
    return initReqSpec()
        .log()
        .all()
        .body(c)
        .post(CustomerConstants.BASE_PATH)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public Response postCustomer(Object c, RequestSpecification requestSpecification) {
    return requestSpecification
        .log()
        .all()
        .body(c)
        .post("/api")
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public Response getCustomer(String id) {
    return initReqSpec()
        .body(new byte[] {})
        .log()
        .all()
        .queryParam("id", id)
        .get(CustomerConstants.BASE_PATH)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }

  public Response getCustomer(String id, RequestSpecification requestSpecification) {
    return requestSpecification
        .log()
        .all()
        .queryParam("id", id)
        .get(CustomerConstants.BASE_PATH)
        .then()
        .log()
        .all()
        .extract()
        .response();
  }
}
