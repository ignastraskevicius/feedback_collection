package lt.agmis.feedbackcollection.util.json;

import java.io.IOException;

import lt.agmis.feedbackcollection.util.DateUtil;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class FailSafeJsonDateDeserializer extends JsonDeserializer<LocalDateTime> {

	private static Logger logger = LoggerFactory.getLogger(FailSafeJsonDateDeserializer.class.getName());

	@Override
	public LocalDateTime deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String dateStr = jp.getText();
		return DateUtil.deserializeDate(dateStr, null);
	}
}
