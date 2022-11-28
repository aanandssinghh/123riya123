package hotelprice;

import java.io.FileWriter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

public class HTMLUtils {
	// function to use drive object and get the content for the hotel.
	// use jsoup
	public static Document parse(String html) {
		return Jsoup.parse(html);
	}

	public static String fetchHtml(WebDriver driver, String url, String name) {
		driver.get(url);

		try {
			Thread.sleep(15000);
		} catch (Exception e) {
		}
		String s = driver.getPageSource();

		try {
			FileWriter myWriter = new FileWriter("webpages\\" + name + ".html");
			myWriter.write(s);
			myWriter.close();
		} catch (Exception e) {
			System.out.println(e);
		}
		return s;
	}
}
