package lt.agmis.feedbackcollection.integration.matchers;

import org.apache.commons.lang.ObjectUtils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 9/13/13
 * Time: 9:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class IsPositiveInteger extends TypeSafeMatcher<Integer> {

    private Integer item;

    @Override
    protected boolean matchesSafely(Integer item) {
        this.item = item;
        return ObjectUtils.compare(item, 0) > 0;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(item + "is not positive");
    }

    public static <T> Matcher<Integer> positiveInteger() {
        return new IsPositiveInteger();
    }
}
