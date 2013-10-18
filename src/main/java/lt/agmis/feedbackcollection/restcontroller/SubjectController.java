package lt.agmis.feedbackcollection.restcontroller;

import lt.agmis.feedbackcollection.domain.Subject;
import lt.agmis.feedbackcollection.dto.CategoryListWrapper;
import lt.agmis.feedbackcollection.dto.IdWrapper;
import lt.agmis.feedbackcollection.dto.SubjectListWrapper;
import lt.agmis.feedbackcollection.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;


@RequestMapping(value = {"/subjects"})
@Controller 
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

	@RequestMapping(produces = {"application/json"}, method = RequestMethod.OPTIONS)
	@ResponseBody
	public CategoryListWrapper getCategories() {
        return null;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Subject getSubject(@PathVariable("id") int id) {
        return subjectService.findById(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public IdWrapper createSubject(@RequestBody Subject subject) {
        IdWrapper wrapper = new IdWrapper();
        wrapper.setId(subjectService.create(subject));
        return wrapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public SubjectListWrapper getSubjectList() {
        SubjectListWrapper wrapper = new SubjectListWrapper();
        wrapper.setSubjects(subjectService.getAll());
        return wrapper;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void updateSubject(@PathVariable("id") int id, @RequestBody Subject subject) {
        subjectService.update(id, subject);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteSubject(@PathVariable("id") int id) {
        subjectService.delete(id);
    }
}
