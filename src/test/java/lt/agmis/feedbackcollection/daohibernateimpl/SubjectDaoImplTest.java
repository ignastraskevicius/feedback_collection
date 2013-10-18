package lt.agmis.feedbackcollection.daohibernateimpl;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import lt.agmis.feedbackcollection.domain.Subject;
import lt.agmis.feedbackcollection.util.httputils.ResourceNotFoundException;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.annotation.Resource;

import java.io.File;
import java.util.Collections;
import java.util.List;


import static com.google.common.collect.Lists.newArrayList;
import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static java.util.Arrays.asList;
import static org.testng.Assert.*;


@ContextConfiguration({"classpath:beans/context/root-context.xml", "classpath:beans/context/servlet-context.xml"})
@ActiveProfiles("db-integration-test")
@TransactionConfiguration
@Test
public class SubjectDaoImplTest extends AbstractTransactionalTestNGSpringContextTests {

    @Resource(name="databaseTester")
    private IDatabaseTester databaseTester;

    @Value("${dbDataForTestsLocationPreffix}")
    private String TEST_DATA_LOCATION;

    @Autowired
    private SubjectDaoImpl sut;

    private Subject subjectA;
    private Subject subjectB;

    @BeforeMethod
    public void setUp() throws Exception {
        subjectA = new Subject();
        subjectB = new Subject();

        databaseTester.setDataSet(getFlatXmlDataSet());
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        databaseTester.setTearDownOperation(DatabaseOperation.NONE);
        databaseTester.onSetup();
    }

    private IDataSet getFlatXmlDataSet() throws Exception
    {
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        builder.setColumnSensing(true);
        IDataSet dataSet = builder.build(new File(TEST_DATA_LOCATION + "subject-it.xml"));
        return dataSet;
    }

    public void insertionShouldRetrieveSubjectWithId() {
        subjectA.setName("Maths");

        Subject createdSubject = sut.create(subjectA);
        assertNotNull(createdSubject);
        assertNotNull(createdSubject.getId());
    }

    @Test(expectedExceptions = ResourceNotFoundException.class)
    public void retrievingSubjectShouldThrowRNFEIfSubjectNotFound() {
        final int NON_EXISTSTENT_SUBJECT_ID = 1;
        sut.findById(NON_EXISTSTENT_SUBJECT_ID);
    }

    @Test
    public void shouldCreateSubject() {
        subjectA.setName("Maths");

        int subjectId = sut.create(subjectA).getId();
        Subject subject = sut.findById(subjectId);
        assertEquals(subject.getName(), "Maths");
    }

    public void retrievingAllSubjectsShouldReturnEmptyCollectionWhenNoRecordsFound() {
        assertEquals(sut.findAll(), Collections.<Subject>emptyList());
    }

    public void shouldRetrieveAllSubjects() {
        subjectA.setName("Maths");
        subjectB.setName("Physics");

        int idSubjectA = sut.create(subjectA).getId();
        int idSubjectB = sut.create(subjectB).getId();

        assertEquals(extractIds(sut.findAll()), asList(idSubjectA, idSubjectB));
    }

    private List<Integer> extractIds(List<Subject> subjects) {
        return newArrayList(Iterables.transform(subjects, new Function<Subject, Integer>() {
            @Override
            public Integer apply(lt.agmis.feedbackcollection.domain.Subject subject) {
                return subject.getId();
            }
        }));
    }

    @Test
    public void deletingShouldNotThrowRNFE() {
        final int NOT_EXISTENT_SUBJECT_ID = 18;
        catchException(sut).delete(NOT_EXISTENT_SUBJECT_ID);
        assertFalse(caughtException() instanceof ResourceNotFoundException);
    }

    @Test(dependsOnMethods = {"shouldCreateSubject", "deletingShouldNotThrowRNFE"}, expectedExceptions = ResourceNotFoundException.class)
    public void shouldDeleteSubject() {
        subjectA.setName("Maths");

        int subjectId = sut.create(subjectA).getId();
        sut.delete(subjectId);
        sut.findById(subjectId);
    }

    @Test(dependsOnMethods = {"shouldDeleteSubject", "retrievingSubjectShouldThrowRNFEIfSubjectNotFound"})
    public void shouldNotDeleteAllSubjects() {
        final String ANY_NAME_1 = "Any Name 1";
        final String ANY_NAME_2 = "Any Name 2";

        subjectA.setName(ANY_NAME_1);
        subjectB.setName(ANY_NAME_2);

        int subject1Id = sut.create(subjectA).getId();
        int subject2Id = sut.create(subjectB).getId();
        sut.delete(subject1Id);
        catchException(sut).findById(subject2Id);
        assertFalse(caughtException() instanceof ResourceNotFoundException);
    }
}
