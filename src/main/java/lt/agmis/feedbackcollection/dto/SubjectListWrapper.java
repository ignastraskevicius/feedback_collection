package lt.agmis.feedbackcollection.dto;

import lt.agmis.feedbackcollection.domain.Category;
import lt.agmis.feedbackcollection.domain.Subject;

import javax.validation.constraints.NotNull;
import java.util.List;

public class SubjectListWrapper {
	
	@NotNull
	private List<Subject> subjects;

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }
}
