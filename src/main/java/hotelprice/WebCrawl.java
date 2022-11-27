package hotelprice;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
// import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class WebCrawl {
    private Date startDate;
    private Date endDate;
    private int numberOfAdults;
    public static final String BASE_URL = "https://www.ca.kayak.com/hotels/Canada-u43/<SD>/<ED>/<NOA>adults?sort=rank_a";

    public WebCrawl(Date startDate, Date endDate, int numberOfAdults) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.numberOfAdults = numberOfAdults;
    }

    public String buildURL(Date startDate, Date endDate, int numberOfAdults) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY-MM-dd");
        String url = BASE_URL.replace("<SD>", dateFormat.format(startDate));
        url = url.replace("<ED>", dateFormat.format(endDate));
        url = url.replace("<NOA>", Integer.toString(numberOfAdults));
        return url;
    }

    
    public String fetchData(String url) throws IOException {
        StringBuilder result = new StringBuilder();
        URL urlObject = new URL(url);
        Scanner scanner = new Scanner(urlObject.openStream());
        while (scanner.hasNext()) {
            result.append(scanner.nextLine());
        }
        scanner.close();
        return result.toString();
    }

    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", ".\\drivers\\chromedriver.exe");

        //Initiating your chromedriver
        WebDriver driver=new ChromeDriver();

        //Applied wait time
        // driver.manage().timeouts().implicitlyWait(10, );
        //maximize window
        driver.manage().window().maximize();

        //open browser with desried URL
        driver.get("https://www.google.com");

        //closing the browser
        driver.close();
    }
}