package lt.agmis.feedbackcollection.dto;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 9/13/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class CreateResult {
    private boolean success;
    private String description;
    private Object id;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }
}
