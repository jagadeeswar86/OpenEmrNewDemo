package org.slk.base;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slk.utilities.PropUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

public class WebDriverWrapper {

	protected WebDriver driver;
	public static Logger logger; // Added logger
	
	@Parameters({ "browser", "node"})  //, "node" //@Optional("ch") ,String node
	@BeforeMethod
	public void setUp(String browserName,String node) throws IOException {
		
		logger = Logger.getLogger("OpenEmrDemo"); // Added logger name of the project  testcasename
		PropertyConfigurator.configure("Log4j.properties");// Added logger
		
		//if (node != null && !(node.equalsIgnoreCase("no")))  @Optional("ie") @Optional("ch")
			if (node != null) 
		{ 
			DesiredCapabilities dc = new DesiredCapabilities();
		
			if (browserName.equals("ch")) {
				//dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.CHROME);
				dc.setCapability("browserName", "chrome");
			} 
			else if (browserName.equals("ff")) {
				
				//dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.FIREFOX);
				dc.setCapability("browserName", "firefox");
				
				

			} else {
				dc.setCapability(CapabilityType.BROWSER_NAME, BrowserType.IE);
			
				//dc.setCapability("browserName", "ie");

			}
			
			driver=new RemoteWebDriver(new URL(node),dc);
			
		} else {

	
			if (browserName.equals("ch")) {
				System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
				driver = new ChromeDriver();
			} else if (browserName.equals("ff")) {
				System.setProperty("webdriver.gecko.driver", "driver/geckodriver.exe");
				driver = new FirefoxDriver();

			} else {
				System.setProperty("webdriver.ie.driver", "driver/IEDriverServer.exe");
				driver = new InternetExplorerDriver();

			}

		}
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		String baseurl = PropUtils.getValueFromProperties("config.Properties", "url");

		if (baseurl != null) {
			driver.get(baseurl);
		}else {

			driver.get("https://demo.openemr.io/b/openemr/interface/login/login.php?site=default");
		}        
		       

		

		
//     driver.manage().window().maximize();
//		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//		driver.get("https://demo.openemr.io/a/openemr/interface/login/login.php?site=default");
//

	}

	@AfterMethod
	public void tearDown() {

		
		String date = new Date().toString().replace(":", "-");

		TakesScreenshot ts = (TakesScreenshot) driver;

		File file = ts.getScreenshotAs(OutputType.FILE);

			

		file.renameTo(new File("ScreenShots/scroonshot-" + date + " .jpg"));
		
		driver.quit();
		


		}

}
