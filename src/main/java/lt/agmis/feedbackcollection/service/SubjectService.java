package lt.agmis.feedbackcollection.service;

import lt.agmis.feedbackcollection.domain.Subject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/14/13
 * Time: 12:32 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubjectService {
    Subject findById(int id);
    int create(Subject subject);
    void update(int id, Subject subject);

    List<Subject> getAll();

    void delete(int id);
}
