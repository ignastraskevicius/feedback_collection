package lt.agmis.feedbackcollection.util.httputils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/14/13
 * Time: 2:46 PM
 * To change this template use File | Settings | File Templates.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{}
