package lt.agmis.feedbackcollection.util.timing;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class TimingUtils {
	public void sleep(int millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			
		}
	}
	
	public long getCurrentTimeMillis() {
		return DateTimeUtils.currentTimeMillis();
	}

	public LocalDateTime getCurrentTime() {
		return new LocalDateTime();
	}
}
