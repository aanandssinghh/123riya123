package hotelprice;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.text.SimpleDateFormat;
import org.jsoup.nodes.Document;

public class WebCrawler {
	// List<Hotel> hotelList = new ArrayList<Hotel>();
	Map<Integer, Map<String, Integer>> wordFrequency = new HashMap<>();
	// Map<String, Map<String, Integer>> wordToDocMap = new HashMap<>();
	InvertedIndex invertedIndex = new InvertedIndex(HotelList.getHotelList());

	private Date startDate;
	private Date endDate;
	private int numberOfAdults;
	private WebDriver driver;

	public WebCrawler(Date startDate, Date endDate, int numberOfAdults, WebDriver driver) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.numberOfAdults = numberOfAdults;
		this.driver = driver;
	}

	private String buildURL(Date startDate, Date endDate, int numberOfAdults) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
		String url = Config.BASE_URL.replace("<SD>", dateFormat.format(startDate));
		url = url.replace("<ED>", dateFormat.format(endDate));
		url = url.replace("<NOA>", Integer.toString(numberOfAdults));
		return url;
	}

	public void runCrawler() {
		// WebCrawler webCrawler = new WebCrawler(new Date(), new Date(), 2);

		String url = this.buildURL(startDate, endDate, 2);
		String name = "start";

		String html = HTMLUtils.fetchHtml(this.driver, url, name);
		// class="kzGk"

		// webCrawler.parseHtml(html);
		Document domForHotelList = HTMLUtils.parse(html);
		HotelList.addDocumentToList(domForHotelList, this.driver, startDate, endDate);
	}

}
