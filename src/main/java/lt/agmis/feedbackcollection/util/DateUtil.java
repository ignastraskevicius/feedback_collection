package lt.agmis.feedbackcollection.util;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil {

	private static final DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm:ss");
	private static Logger logger = LoggerFactory.getLogger(DateUtil.class.getName());
	
	public static LocalDateTime deserializeDate(String dateString, LocalDateTime defaultDateTime) {
		
	    LocalDateTime date = null;
        try {
            date = fmt.parseLocalDateTime(dateString);
        } catch (Exception e) {
        	logger.info(String.format("String '%s' cannot be parsed with date format '%s'. Use current date instead", dateString, fmt.toString()));
        	date = defaultDateTime;
        }
		return date;
	}

}
