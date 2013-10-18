package lt.agmis.feedbackcollection.serviceimpl;

import lt.agmis.feedbackcollection.dao.SubjectDao;
import lt.agmis.feedbackcollection.domain.Subject;
import lt.agmis.feedbackcollection.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/3/13
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
@Transactional
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectDao subjectDao;

    @Override
    public Subject findById(int id) {
        return subjectDao.findById(id);
    }

    @Override
    public int create(Subject subject) {
        return subjectDao.create(subject).getId();
    }

    @Override
    public void update(int id, Subject subjectToGrabDataFrom) {
        Subject subjectToUpdate = subjectDao.findById(id);
        subjectToUpdate.setName(subjectToGrabDataFrom.getName());
    }

    @Override
    public List<Subject> getAll() {
        return subjectDao.findAll();
    }

    @Override
    public void delete(int id) {
        subjectDao.delete(id);
    }
}
