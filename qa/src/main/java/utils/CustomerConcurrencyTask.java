package utils;

import java.util.concurrent.Callable;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import pojos.Customer;

@Getter
@Setter
@Slf4j
public class CustomerConcurrencyTask implements Callable {

  private CustomerHelper helper;
  private Customer c;

  public CustomerConcurrencyTask(Customer c) {
    this.c = c;
  }

  @Override
  public Object call() throws Exception {
    return helper.postCustomer(c).statusCode() == 200;
  }
}
