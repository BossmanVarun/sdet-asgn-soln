package integrationtests.customersvc;

import constants.CustomerConstants;
import dataproviders.CustomerDataProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pojos.Customer;
import pojos.CustomerResponse;
import pojos.ErrorResponse;
import utils.Validations;

@Slf4j
public class GetCustomerTests extends CustomerBaseTest {

  private Customer c;

  @BeforeMethod
  public void createTestCustomer() {
    c = cBuilder.build();
    Response r = helper.postCustomer(c);
    Assert.assertEquals(r.statusCode(), 200);
  }

  @Test(description = "[TC09] Check for bad headers")
  public void badHeaderTest() {
    SoftAssert sa = new SoftAssert();
    RequestSpecification rSpec =
        RestAssured.given()
            .baseUri(helper.getBaseUrl())
            .contentType(ContentType.JSON)
            .header(new Header("x-session-token", "blah"));
    Response r = helper.getCustomer((String) c.getId(), rSpec);
    sa.assertEquals(r.statusCode(), 403);
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "request cannot be authenticated!");
    sa.assertAll();
  }

  @Test(
      description = "[TC10] Check for bot headers",
      dataProvider = "badBotDataProvider",
      dataProviderClass = CustomerDataProvider.class)
  public void botHeaderTest(String badBotStr) {
    SoftAssert sa = new SoftAssert();
    RequestSpecification rSpec =
        RestAssured.given()
            .baseUri(helper.getBaseUrl())
            .contentType(ContentType.JSON)
            .headers(Map.of("user-agent", badBotStr, "x-session-token", "authorized-user"));
    Response r = helper.getCustomer((String) c.getId(), rSpec);
    sa.assertEquals(r.statusCode(), 400);
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "bad bot, go away!");
    sa.assertAll();
  }

  @Test(description = "[TC11] Check for missing id")
  public void getCustomerIdNotPresentTest() {
    SoftAssert sa = new SoftAssert();
    Response r = helper.getCustomer("999999");
    sa.assertEquals(r.statusCode(), 400); // Bug Here
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "error while fetching customer");
    sa.assertAll();
  }

  @Test(description = "[TC12] Check for empty Id")
  public void getCustomerEmptyIdTest() {
    SoftAssert sa = new SoftAssert();
    Response r = helper.getCustomer("");
    sa.assertEquals(r.statusCode(), 400); // Bug Here
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "error while fetching customer");
    sa.assertAll();
  }

  @Test(description = "[TC13] Check for Id not present in params")
  public void getCustomerIdParamMissingTest() {
    SoftAssert sa = new SoftAssert();
    Response r =
        RestAssured.given()
            .headers(CustomerConstants.HEADERS)
            .log()
            .all()
            .get(CustomerConstants.BASE_PATH)
            .then()
            .log()
            .all()
            .extract()
            .response();
    sa.assertEquals(r.statusCode(), 400); // Bug Here
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "error while fetching customer");
    sa.assertAll();
  }

  @Test(description = "[TC14] Get customer details post success on creation")
  public void getCustomerPositiveTest() {
    Response r = helper.getCustomer((String) c.getId());
    Assert.assertEquals(r.statusCode(), 200); // Bug Here
    Validations.validateCustomerDetails(r.as(CustomerResponse.class), c);
  }
}
