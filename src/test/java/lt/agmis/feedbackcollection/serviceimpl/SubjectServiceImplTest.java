package lt.agmis.feedbackcollection.serviceimpl;

import lt.agmis.feedbackcollection.dao.SubjectDao;
import lt.agmis.feedbackcollection.domain.Subject;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.classic.Session;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/15/13
 * Time: 7:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Test
public class SubjectServiceImplTest {

    @Mock SubjectDao subjectDao;

    @InjectMocks SubjectServiceImpl sut;

    @BeforeMethod
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    public void shouldUpdateSubject() {
        final int VALID_ID = 5;

        Subject subjectToGrabDataFrom = new Subject();
        subjectToGrabDataFrom.setName("New Name");
        Subject subjectToUpdate = mock(Subject.class);
        when(subjectDao.findById(anyInt())).thenReturn(subjectToUpdate);

        sut.update(VALID_ID, subjectToGrabDataFrom);

        verify(subjectToUpdate).setName("New Name");
    }


}
