package hotelprice;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Common {
    public static String convertDate(Date startDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(startDate);
        return date;
    }
}

