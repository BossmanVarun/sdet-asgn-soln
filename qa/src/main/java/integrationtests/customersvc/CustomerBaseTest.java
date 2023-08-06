package integrationtests.customersvc;

import constants.GenericConstants;
import java.util.Objects;
import listeners.TestNgListener;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import pojos.Customer;
import pojos.Customer.CustomerBuilder;
import utils.CustomerHelper;

@Listeners(TestNgListener.class)
public class CustomerBaseTest {

  protected final CustomerHelper helper = new CustomerHelper();
  protected CustomerBuilder cBuilder;

  @BeforeClass(alwaysRun = true)
  @Parameters(GenericConstants.BASE_URL)
  public void initTest(@Optional String url) {
    helper.setBaseUrl(Objects.nonNull(url) ? url : "http://localhost:8080");
  }

  @BeforeMethod(alwaysRun = true)
  public void setUp() {
    cBuilder =
        Customer.builder()
            .id(RandomStringUtils.randomAlphanumeric(10))
            .name("Test" + RandomStringUtils.randomAlphabetic(10))
            .phoneNumber("9999999999");
  }
}
