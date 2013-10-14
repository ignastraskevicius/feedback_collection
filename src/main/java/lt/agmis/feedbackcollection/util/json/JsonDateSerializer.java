package lt.agmis.feedbackcollection.util.json;

import java.io.IOException;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonDateSerializer extends JsonSerializer<LocalDateTime> {

	DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd_HH:mm:ss");
	
	private static Logger logger = LoggerFactory.getLogger(FailSafeJsonDateDeserializer.class.getName());
	
	@Override
	public void serialize(LocalDateTime value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		logger.debug("json serializer invoked");
		String formattedDate = fmt.print(value);
        jgen.writeString(formattedDate);
	}
}
