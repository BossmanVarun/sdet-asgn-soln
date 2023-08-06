package integrationtests.customersvc;

import exceptions.ConcurrencyException;
import org.testng.Assert;
import org.testng.annotations.Test;
import pojos.Customer;
import utils.ConcurrencyUtil;
import utils.CustomerConcurrencyTask;

public class ConcurrencyTest extends CustomerBaseTest {

  @Test
  public void concurrentPostCustomer() {
    Customer c = cBuilder.build();
    CustomerConcurrencyTask task = new CustomerConcurrencyTask(c);
    task.setHelper(helper);

    try {
      ConcurrencyUtil.executeTasksConcurrently(task, 20, 1);
    } catch (ConcurrencyException e) {
      e.printStackTrace();
      Assert.fail();
    } catch (InterruptedException e) {
      Assert.fail();
    }
  }
}
