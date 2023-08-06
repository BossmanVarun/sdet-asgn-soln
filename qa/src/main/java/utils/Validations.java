package utils;

import org.testng.asserts.SoftAssert;
import pojos.Customer;
import pojos.CustomerResponse;

public class Validations {

  public static void validateCustomerDetails(CustomerResponse res, Customer c) {
    SoftAssert sa = new SoftAssert();
    sa.assertEquals((String) c.getId(), res.getId(), "Id mismatch");
    sa.assertTrue(res.isSmsSent(), "Sms Sent mismatch");
    sa.assertEquals((String) c.getPhoneNumber(), res.getPhoneNumber(), "PhoneNumber mismatch");
    sa.assertEquals((String) c.getName(), res.getName(), "Name mismatch");
    sa.assertAll();
  }
}
