package vtiger;

import java.io.File;
import java.io.IOException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import com.google.common.io.Files;

public class TakeScreenshot {

	public static void main(String[] args) throws IOException {
		
		WebDriver driver=new ChromeDriver();
		driver.get("http://localhost:8888/");
		
		TakesScreenshot tss=(TakesScreenshot)driver;
		
		File fileFrom= tss.getScreenshotAs(OutputType.FILE);
	    File fileTo	=new File("C:\\Users\\Admin\\Desktop\\Screenshot\\vtiger\\loginPage1.png");
		
	    Files.copy(fileFrom, fileTo);
		
	}

}
