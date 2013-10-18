package lt.agmis.feedbackcollection.daohibernateimpl;

import lt.agmis.feedbackcollection.dao.SubjectDao;
import lt.agmis.feedbackcollection.domain.Subject;
import lt.agmis.feedbackcollection.util.httputils.ResourceNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.classic.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubjectDaoImpl implements SubjectDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Subject findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        Subject subject = (Subject) session.get(Subject.class, id);
        if(subject == null) {
            throw new ResourceNotFoundException();
        }
        return subject;
    }

    @Override
    public Subject create(Subject subject) {
        sessionFactory.getCurrentSession().save(subject);
        return subject;
    }

    @Override
    public List<Subject> findAll() {
        List<Subject> subjects = (List<Subject>) sessionFactory.getCurrentSession().createCriteria(Subject.class).list();
        return subjects;
    }

    @Override
    public void delete(int id) {
        Session session = sessionFactory.getCurrentSession();
        Subject subjectToDelete = (Subject) session.get(Subject.class, id);
        session.delete(subjectToDelete);
    }
}
