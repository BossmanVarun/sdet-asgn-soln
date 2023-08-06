package dataproviders;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public class CustomerDataProvider {

  @DataProvider
  public static Iterator<Object[]> negativeIdDataTypeProvider() {
    return List.of(new Object[] {1}, new Object[] {true}).iterator();
  }

  @DataProvider
  public static Iterator<Object[]> phoneLengthNegativeDataProvider() {
    return List.of(
            new Object[] {RandomStringUtils.randomNumeric(9)},
            new Object[] {RandomStringUtils.randomNumeric(11)})
        .iterator();
  }

  @DataProvider
  public static Object[][] badBotDataProvider() {
    return new Object[][] {
      {"BOT"}, {"  BOT  "}, {"bot"}, {"   bot   "}, {"aergaerbot"}, {"botawerfawerg"}
    };
  }
}
