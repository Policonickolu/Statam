package systeme;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateActuelle {

	/**
	 * Recupère la date actuelle dans système
	 * 
	 * @return la date
	 */
	public static Date getDateActuelle() {

		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		return Date.valueOf(dateFormat.format(cal.getTime()));

	}

}
