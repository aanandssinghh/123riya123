package hotelprice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class UserInput {
    private static String startDate;
    private static String endDate;
    private static String location;

    public UserInput(Scanner sc) throws ParseException {
        Date startDate1 = new Date();
        Date endDate1 = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("Enter the start date in dd/MM/yyyy format: ");
        startDate = sc.nextLine();
        while (!validateDate(startDate)) {
            startDate1 = sdf.parse(startDate);
            System.out.println("\nInvalid date format. Please enter the start date in the format dd/MM/yyyy: ");
            startDate = sc.nextLine();
        }
        System.out.println("\nEnter the end date in dd/MM/yyyy format: ");
        endDate = sc.nextLine();
        while (!validateDate(endDate)) {
            endDate1 = sdf.parse(endDate);
            System.out.println("\nInvalid date format. Please enter the end date in the format dd/MM/yyyy: ");
            endDate = sc.nextLine();
        }

        do {
            System.out.println("\nEnter the location: ");
            location = sc.nextLine();
            location = suggestNearestMatchingWord(sc, location, HotelList.getLocationMap().keySet());
            sc.nextLine();
        } while(!validateLocation(location));


        if (validateDate(startDate) && validateDate(endDate) && validateLocation(location)) {
            System.out.println("\nStart date: " + startDate + "\nEnd date: " + endDate + "\nLocation: " + location+"\n");
            //find accurate results
            Set<String> locationHotels = HotelList.getLocationMap().get(location);

            startDate1 = sdf.parse(startDate);
            endDate1 = sdf.parse(endDate);
            Calendar start = Calendar.getInstance();
            start.setTime(startDate1);
            Calendar end = Calendar.getInstance();
            end.setTime(endDate1);

            Set<String> startDateHotels = HotelList.getStartDateMap().get(startDate);
            Set<String> commonStartDateHotels = startDateHotels;
            Map<String, Hotel> hotelList = HotelList.getHotelList();


            for (Date date = start.getTime(); start.before(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
                startDateHotels = HotelList.getStartDateMap().get(Common.convertDate(date));

                if (startDateHotels!=null && commonStartDateHotels!=null && !startDateHotels.isEmpty() && !commonStartDateHotels.isEmpty()) {
                    commonStartDateHotels = getCommonHotels(startDateHotels, commonStartDateHotels);
                }
            }

            Set<String> commonHotels = new HashSet<>();
            if (locationHotels!=null && commonStartDateHotels!=null && !locationHotels.isEmpty() && !commonStartDateHotels.isEmpty()) {
                commonHotels = getCommonHotels(locationHotels, commonStartDateHotels);
            }

            if(!commonHotels.isEmpty()) {
                System.out.println("\nFor the entered input, please check the following hotels:");

                for (String hotel : commonHotels) {
                    System.out.println("    -- Name: "+hotelList.get(hotel).getName());
                    System.out.println("       Price: "+hotelList.get(hotel).getPrice());
                    System.out.println("       Location: "+hotelList.get(hotel).getLocation());
                    System.out.println("       Ratings: "+hotelList.get(hotel).getScore());
                    System.out.print("\n");
                }

                System.out.println("\nDo you want to search for other input? y or n");
                String searchAgain = sc.nextLine();
                if(searchAgain.equals("y")) {
                    new UserInput(sc);
                }
                
            } else {
                System.out.println("No hotel found between the dates you entered.");
                System.out.println("\nSearch for other input:");
                new UserInput(sc);
            }
        }
    }

    private String suggestWordCompletion(Scanner sc, String location, Set<String> locationSet) {
        System.out.println("Word completion:");
        Trie locationTrie = HotelList.getLocationTrie();
        List<String> completedWords = locationTrie.completeWord(location);
        int count = 1;
        
        for (int i=0; i <((completedWords.size()<5)?completedWords.size():5);i++) {
            System.out.println("Did you mean:--------------" + completedWords.get(i)+"? "+"Type "+count);
            count++;
        }
        int userInput = sc.nextInt();
        switch(userInput) {
            case 1:
                return completedWords.get(0);
            case 2:
                return completedWords.get(1);
            case 3:
                return completedWords.get(2);
            case 4:
                return completedWords.get(3);
            case 5:
                return completedWords.get(4);
            case 9:
                return "";
        }

        return location;
    }

    private String suggestNearestMatchingWord(Scanner sc, String location, Set<String> locationSet) {
        //edit distance
        int min = Integer.MAX_VALUE;
        String minWord = "";
        
        int secondMin = Integer.MAX_VALUE;
        String secondMinWord = "";

        for (String loc : locationSet) {
            int distance = EditDistance.editDistance(loc, location);   
            if(distance<min) {
                secondMin = min;
                secondMinWord = minWord;

                min = distance;
                minWord = loc;
            } else if(distance<secondMin) {
                secondMin = distance;
                secondMinWord = loc;
            }
        }
        int c=1;
        if(!minWord.isEmpty()) {
            System.out.println("\nDo you mean:--------------" + minWord+"? "+"Type "+c);
            c++;
        } 
        if(!secondMinWord.isEmpty()) {
            System.out.println("Do you mean:--------------" + secondMinWord+"? "+"Type "+c);
        } 
            System.out.println("Complete Words:------------"+"Type 0");
            System.out.println("Enter again:---------------"+"Type 9");


        int userInput = sc.nextInt();
        
        switch(userInput) {
            case 0:
                return suggestWordCompletion(sc, location, HotelList.getLocationMap().keySet());
            case 1:
                return minWord;
            case 2:
                return secondMinWord;
            case 9:
                return "";
        }
        return "";

    }

    public static boolean validateDate(String date) throws ParseException {
        Date todayDate = new Date();
        Calendar todayCalendar = setDateToCalendar(todayDate);
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(date);
            if (date1.compareTo(todayCalendar.getTime()) < 0) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false; //if date is not in the format dd/MM/yyyy
        }
    }

    public static Calendar setDateToCalendar(Date date) throws ParseException {
       
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public static Set<String> getCommonHotels(Set<String> locationHotels, Set<String> startDateHotels) {
        Set<String> commonHotels = new HashSet<>();
        for (String hotel : locationHotels) {
            if (startDateHotels.contains(hotel)) {
                commonHotels.add(hotel);
            }
        }
        return commonHotels;
    }

    public static boolean validateLocation(String location) {
        Set<String> locations = HotelList.getLocationMap().keySet();
        if(locations.contains(location)) {
            return true;
        } else {
            return false;
        }
        
    }

}
