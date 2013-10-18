package lt.agmis.feedbackcollection.integration.moduletests;


import com.jayway.jsonpath.JsonPath;
import lt.agmis.feedbackcollection.integration.testutils.MockRequest;
import org.dbunit.IDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.File;

import static com.jayway.jsonassert.JsonAssert.emptyCollection;
import static com.jayway.jsonassert.JsonAssert.with;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@WebAppConfiguration
@ContextConfiguration({"classpath:beans/context/root-context.xml", "classpath:beans/context/servlet-context.xml"})
@Test
@ActiveProfiles(profiles={"db-integration-test","system-integration-test"})
public class SubjectIT extends AbstractTestNGSpringContextTests
{

	@Resource(name="databaseTester")
	private IDatabaseTester databaseTester;

	@Autowired
    private WebApplicationContext wac;

    @Value("${dbDataForTestsLocationPreffix}")
    private String TEST_DATA_LOCATION;

	private MockMvc mockMvc;
    private MockRequest mockRequest;

	@BeforeMethod
	public void setUp() throws Exception {
		mockMvc = webAppContextSetup(wac)
				.defaultRequest(get("/").contextPath("/feedbackcollection").servletPath("/v1"))
				.build();
		mockRequest = new MockRequest(mockMvc);

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

    @DataProvider
    public static final Object[][] getUnsuportedHttpMethodsForSubjectWs() {
        final String SUBJECTS = "/feedbackcollection/v1/subjects";
        final String ONE_SUBJECT = "/feedbackcollection/v1/subjects/{id}";
        final int ANY_ID = 1;
        return new Object[][] {
                {"subjects PUT method",
                        put(SUBJECTS)},
                {"subjects DELETE method",
                        delete(SUBJECTS)},
                {"one subject POST method",
                        post(ONE_SUBJECT, ANY_ID)}};
    }

    @Test(dataProvider = "getUnsuportedHttpMethodsForSubjectWs")
    public void unsupportedHttpMethodsForSubjectsShouldBeNotAllowed(String description, MockHttpServletRequestBuilder link) throws Exception {
        mockMvc.perform(link).andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void shouldCreateSubject() throws Exception {
        final String ANY_SUBJECT_AS_JSON = "{\"name\":\"Maths\"}";
        String subjectIdAsJson = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/v1/subjects"), ANY_SUBJECT_AS_JSON);
        Integer subjectId = JsonPath.read(subjectIdAsJson, "$.id");
        String subjectAsJson = mockRequest.retrieveJson(get("/feedbackcollection/v1/subjects/{id}", subjectId));
        with(subjectAsJson)
                .assertThat("$.id", equalTo(subjectId));
    }

    public void shouldRetrieveAllSubjects() throws Exception {
        final String SUBJECT_AS_JSON_1 = "{\"name\":\"Maths\"}";
        final String SUBJECT_AS_JSON_2 = "{\"name\":\"Maths\"}";
        String subject1IdAsJson = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/v1/subjects"), SUBJECT_AS_JSON_1);
        String subject2IdAsJson = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/v1/subjects"), SUBJECT_AS_JSON_2);
        Integer subjectId1 = JsonPath.read(subject1IdAsJson, "$.id");
        Integer subjectId2 = JsonPath.read(subject2IdAsJson, "$.id");
        String subjectListAsJson = mockRequest.retrieveJson(get("/feedbackcollection/v1/subjects"));
        with(subjectListAsJson)
                .assertThat("$.subjects[*].id", contains(subjectId1, subjectId2))
                ;
    }

    @Test(dependsOnMethods = {"shouldCreateSubject"})
    public void shouldUpdateSubject() throws Exception {
        final String MATH_SUBJECT_AS_JSON = "{\"name\":\"Maths\"}";
        final String PHYSICS_SUBJECT_AS_JSON = "{\"name\":\"Physics\"}";
        String subjectIdAsJson = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/v1/subjects"), MATH_SUBJECT_AS_JSON);
        Integer subjectId = JsonPath.read(subjectIdAsJson, "$.id");
        mockRequest.submitJson(put("/feedbackcollection/v1/subjects/{id}", subjectId), PHYSICS_SUBJECT_AS_JSON);
        String subjectAsJson = mockRequest.retrieveJson(get("/feedbackcollection/v1/subjects/{id}", subjectId));
        with(subjectAsJson)
                .assertThat("$.name", equalTo("Physics"));
    }

    @Test(dependsOnMethods = {"shouldCreateSubject"})
    public void shouldDeleteSubject() throws Exception {
        final String ANY_SUBJECT_AS_JSON = "{\"name\":\"Maths\"}";
        String subjectIdAsJson = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/v1/subjects"), ANY_SUBJECT_AS_JSON);
        Integer subjectId = JsonPath.read(subjectIdAsJson, "$.id");
        mockRequest.submitRequest(delete("/feedbackcollection/v1/subjects/{id}", subjectId));
        mockRequest.return404(get("/feedbackcollection/v1/subjects/{id}", subjectId));
    }


}