package lt.agmis.feedbackcollection.dao;

import lt.agmis.feedbackcollection.domain.Subject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/14/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubjectDao {
    Subject findById(int id);

    Subject create(Subject subject);

    List<Subject> findAll();

    void delete(int id);
}
