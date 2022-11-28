package hotelprice;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

public class HotelList implements Serializable {
	private static Map<String, Hotel> hotelList = new HashMap<>();
	private static Map<String, HashSet<String>> locationMap = new HashMap<>();
	private static Map<String, HashSet<String>> startDateMap = new HashMap<>();
	private static Trie locationTrie = new Trie();
	private static Trie allWordsTrie = new Trie();
	private static String fileName = "./serializedValues";
	private static SplayTree splayTree = new SplayTree();

	public HotelList() {
		readValues();
	}

	public static void addDocumentToList(Document doc, WebDriver driver, Date startDate, Date endDate) {
		Elements elements = doc.getElementsByClass("kzGk");
		System.out.println("here. elements: " + elements.size());
		// System.out.println(doc.html());
		int count = 0;
		for (Element element : elements) {
			// if (count == 1)
			// break;
			// count++;
			int index = hotelList.size();
			String price = element.getElementsByClass("zV27-price").first().text();
			String location = element.getElementsByClass("FLpo-location-name").first().text().toLowerCase();
			String score = element.getElementsByClass("FLpo-score").first().text();
			String name = element.getElementsByClass("FLpo-big-name").first().text().toLowerCase();
			String url = Config.DOMAIN_NAME + element.getElementsByClass("FLpo-big-name").first().attr("href");
			String text = fetchTextFromUrl(driver, url, name).toLowerCase();

			String[] words = text.split(" ");
			words = Arrays.asList(words).stream().map(String::toLowerCase).toArray(String[]::new);
			allWordsTrie.insertWords(Arrays.asList(words));
			List<String> locationList = new ArrayList<>();
			locationList.add(location.toLowerCase());
			locationTrie.insertWords(locationList);

			System.out.println("Crawled: " + name);

			// create maps based on location, startDate.
			// Map has a key as location/startDate and value is a index of hotels.
			addToMap(locationMap, location, name);

			addToMap(startDateMap, Common.convertDate(startDate), name);

			hotelList.put(name, new Hotel(price, location, score, name, url, words));
		}
		saveValues();
	}

	public static void addToMap(Map<String, HashSet<String>> map, String key, String hotelName) {
		if (map.containsKey(key)) {
			map.get(key).add(hotelName);
		} else {
			HashSet<String> set = new HashSet<>();
			set.add(hotelName);
			map.put(key, set);
		}
	}

	public static String fetchTextFromUrl(WebDriver driver, String url, String name) {
		String html = HTMLUtils.fetchHtml(driver, url, name);
		Document doc = HTMLUtils.parse(html);
		return doc.body().text();
	}

	public static Map<String, HashSet<String>> getLocationMap() {
		return locationMap;
	}

	public static Map<String, Hotel> getHotelList() {
		return hotelList;
	}

	public static Map<String, HashSet<String>> getStartDateMap() {
		return startDateMap;
	}

	public static SplayTree getSplayTree() {
		return splayTree;
	}

	public static Trie getAllWordsTrie() {
		return allWordsTrie;
	}

	public static void saveValues() {
		try {
			FileOutputStream fileOut = new FileOutputStream(fileName);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(hotelList);
			out.writeObject(locationMap);
			out.writeObject(startDateMap);
			out.writeObject(locationTrie);
			out.writeObject(allWordsTrie);
			out.close();
			fileOut.close();
			System.out.println("Serialized data is saved in " + fileName);
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	public static void readValues() {
		try {
			FileInputStream fileIn = new FileInputStream(fileName);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			hotelList = (Map<String, Hotel>) in.readObject();
			locationMap = (Map<String, HashSet<String>>) in.readObject();
			startDateMap = (Map<String, HashSet<String>>) in.readObject();
			locationTrie = (Trie) in.readObject();
			allWordsTrie = (Trie) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
			return;
		} catch (ClassNotFoundException c) {
			System.out.println("Employee class not found");
			c.printStackTrace();
			return;
		}
	}

	public static Trie getLocationTrie() {
		return locationTrie;
	}
}
