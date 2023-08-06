package integrationtests.customersvc;

import static org.awaitility.Awaitility.await;

import dataproviders.CustomerDataProvider;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.time.Duration;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pojos.Customer;
import pojos.CustomerResponse;
import pojos.ErrorResponse;
import pojos.SuccessResponse;

@Slf4j
public class PostCustomerTests extends CustomerBaseTest {

  @Test(description = "[TC00] Test for customer creation")
  public void sanityPositiveTest() {
    SoftAssert sa = new SoftAssert();
    Response r = helper.postCustomer(cBuilder.build());
    sa.assertEquals(r.statusCode(), 200);
    sa.assertTrue(r.as(SuccessResponse.class).getMessage().contains("customer created"));
    sa.assertAll();
  }

  @Test(
      description = "[TC01] Data Type validation for Id Field",
      dataProvider = "negativeIdDataTypeProvider",
      dataProviderClass = CustomerDataProvider.class)
  public void idFieldTypeTests(Object negId) {
    SoftAssert sa = new SoftAssert();
    Customer c = cBuilder.id(negId).build();
    Response r = helper.postCustomer(c);
    sa.assertEquals(r.statusCode(), 400);
    sa.assertTrue(r.as(ErrorResponse.class).getError().contains("json: cannot unmarshal"));
    sa.assertAll();
  }

  @Test(description = "[TC02] Length Check for the Name field")
  public void idNameLengthTest() {
    SoftAssert sa = new SoftAssert();
    Customer c = cBuilder.name(RandomStringUtils.randomAlphabetic(51)).build();
    Response r = helper.postCustomer(c);
    sa.assertEquals(r.statusCode(), 400); // 5xx - BUG here
    sa.assertEquals(
        r.as(ErrorResponse.class).getError(), "CHECK constraint failed: length(name) <= 50");
    sa.assertAll();
  }

  @Test(description = "[TC03] Unique Constraint Check")
  public void uniqueConstraintCheck() {
    SoftAssert sa = new SoftAssert();
    Customer c = cBuilder.name(RandomStringUtils.randomAlphabetic(49)).build();
    Assert.assertEquals(helper.postCustomer(c).statusCode(), 200);
    Response r = helper.postCustomer(c);
    sa.assertEquals(r.statusCode(), 400); // 5xx - BUG here
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "UNIQUE constraint failed: customers.id");
    sa.assertAll();
  }

  @Test(
      description = "[TC04] Check Phone Number Length",
      dataProvider = "phoneLengthNegativeDataProvider",
      dataProviderClass = CustomerDataProvider.class)
  public void phoneLengthCheck(Object len) {
    SoftAssert sa = new SoftAssert();
    Customer c = cBuilder.phoneNumber(len).build();
    helper.postCustomer(c);
    Response r = helper.postCustomer(c);
    sa.assertEquals(r.statusCode(), 400); // 5xx - BUG here
    sa.assertEquals(
        r.as(ErrorResponse.class).getError(), "CHECK constraint failed: length(phone_number) = 10");
    sa.assertAll();
  }

  @Test(description = "[TC05] Check Name Regex")
  public void regexNameCheckTest() {
    SoftAssert sa = new SoftAssert();
    Customer c = cBuilder.name(RandomStringUtils.randomAlphanumeric(10)).build();
    Response r = helper.postCustomer(c);
    sa.assertEquals(r.statusCode(), 400); // BUG here
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "name has special characters");
    sa.assertAll();
  }

  @Test(description = "[TC06] Check that sms updation occurs within time frame")
  public void smsUpdationTest() {
    SoftAssert sa = new SoftAssert();
    Customer c = cBuilder.id(RandomStringUtils.randomNumeric(10))
        .name(RandomStringUtils.randomAlphabetic(20)).build();
    Response r = helper.postCustomer(c);
    Assert.assertEquals(r.statusCode(), 200);
    await()
        .atLeast(Duration.ofSeconds(10))
        .atMost(Duration.ofSeconds(20))
        .pollInterval(Duration.ofSeconds(5))
        .until(
            () -> {
              boolean res = false;
              try {
                CustomerResponse tst = helper.getCustomer((String) c.getId()).as(CustomerResponse.class);
                res = tst.isSmsSent();
                log.info(tst.toString());
                log.info("================================================================================");
              } catch (Exception e) {
                log.warn("exception during getCustomer call");
              }
              return res;
            });
    sa.assertEquals(r.statusCode(), 400); // BUG here
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "name has special characters");
    sa.assertAll();
  }

  @Test(description = "[TC07] Check for bad headers")
  public void badHeaderTest() {
    SoftAssert sa = new SoftAssert();
    RequestSpecification rSpec =
        RestAssured.given()
            .baseUri(helper.getBaseUrl())
            .contentType(ContentType.JSON)
            .header(new Header("x-session-token", "blah"));
    Response r = helper.postCustomer(cBuilder.build(), rSpec);
    sa.assertEquals(r.statusCode(), 403);
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "request cannot be authenticated!");
    sa.assertAll();
  }

  @Test(
      description = "[TC08] Check for bot headers",
      dataProvider = "badBotDataProvider",
      dataProviderClass = CustomerDataProvider.class)
  public void botHeaderTest(String badBotStr) {
    SoftAssert sa = new SoftAssert();
    RequestSpecification rSpec =
        RestAssured.given()
            .baseUri(helper.getBaseUrl())
            .contentType(ContentType.JSON)
            .headers(Map.of("user-agent", badBotStr, "x-session-token", "authorized-user"));
    Response r = helper.postCustomer(cBuilder.build(), rSpec);
    sa.assertEquals(r.statusCode(), 400);
    sa.assertEquals(r.as(ErrorResponse.class).getError(), "bad bot, go away!");
    sa.assertAll();
  }
}
