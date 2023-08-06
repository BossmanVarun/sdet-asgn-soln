import java.util.ArrayList;
import java.util.List;
import listeners.TestNgListener;
import org.testng.TestNG;
import org.testng.xml.XmlPackage;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

public class Executor {

  public static void main(String[] args) {
    TestNgListener testNgListener = new TestNgListener();
    XmlSuite suite = new XmlSuite();
    suite.setName("CustomerSvc Test Suite");
    XmlTest test = new XmlTest(suite);
    test.setName("TmpTest");
    List<XmlPackage> xmlPackages = new ArrayList<XmlPackage>();
    xmlPackages.add(new XmlPackage("integrationtests.customersvc"));
    test.setXmlPackages(xmlPackages);
    List<XmlSuite> suites = new ArrayList<XmlSuite>();
    suites.add(suite);
    TestNG tng = new TestNG();
    tng.addListener(testNgListener);
    tng.setXmlSuites(suites);
    tng.run();
  }

}
