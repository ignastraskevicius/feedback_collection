package lt.agmis.feedbackcollection.restcontroller;

import lt.agmis.feedbackcollection.domain.Subject;
import lt.agmis.feedbackcollection.integration.testutils.MockRequest;
import lt.agmis.feedbackcollection.service.SubjectService;
import lt.agmis.feedbackcollection.util.httputils.ResourceNotFoundException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;

import static com.jayway.jsonassert.JsonAssert.collectionWithSize;
import static com.jayway.jsonassert.JsonAssert.with;
import static java.util.Arrays.asList;
import static java.util.Arrays.equals;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 10/14/13
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */

@Test
public class SubjectControllerTest {

    @Mock SubjectService subjectService;

    @InjectMocks SubjectController sut;

    private MockMvc mockMvc;
    private MockRequest mockRequest;
    private Subject subjectA;
    private Subject subjectB;

    @BeforeMethod
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(sut)
                .defaultRequest(get("/").contextPath("/feedbackcollection").servletPath("/v1"))
                .build();
        mockRequest = new MockRequest(mockMvc);


        subjectA = new Subject();
        subjectB = new Subject();
    }




    public void searchingForUnexistingSubjectShouldReturn404() throws Exception {
        when(subjectService.findById(anyInt())).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(get("/feedbackcollection/v1/subjects/{id}", 1)).andExpect(status().isNotFound());
    }

    public void searchingForSubjectShouldReturnCertainFields() throws Exception {
        final int SUBJECT_ID = 2;
        subjectA.setName("Maths");
        subjectA.setId(SUBJECT_ID);
        when(subjectService.findById(anyInt())).thenReturn(subjectA);
        String subjectAsJson = mockRequest.retrieveJson(get("/feedbackcollection/v1/subjects/{id}", SUBJECT_ID));
        with(subjectAsJson)
                .assertThat("$.id", equalTo(SUBJECT_ID))
                .assertThat("$.name", equalTo("Maths"));
    }

    @Test
    public void shouldRetrieveEmptyCollectionIfNoDataFound() throws Exception {
        String subjectListAsJson = mockRequest.retrieveJson(get("/feedbackcollection/v1/subjects"));

        with(subjectListAsJson)
                .assertNotNull("$.subjects")
                .assertThat("$.subjects", is(collectionWithSize(equalTo(0))));
    }

    @Test(dependsOnMethods = {"shouldRetrieveEmptyCollectionIfNoDataFound"})
    public void retrievingAllSubjectsShouldReturnCertainFields() throws Exception {
        final int ID_SUBJECT_A = 2;
        final int ID_SUBJECT_B = 15;
        subjectA.setName("Maths");
        subjectA.setId(ID_SUBJECT_A);

        subjectB.setId(ID_SUBJECT_B);
        subjectB.setName("Physics");

        final int COLLECTION_SIZE = 2;

        when(subjectService.getAll()).thenReturn(asList(subjectA, subjectB));

        String subjectListAsJson = mockRequest.retrieveJson(get("/feedbackcollection/v1/subjects"));

        with(subjectListAsJson)
                .assertNotNull("$.subjects")
                .assertThat("$.subjects", is(collectionWithSize(equalTo(COLLECTION_SIZE))))
                .assertEquals("$.subjects[0].name", "Maths")
                .assertEquals("$.subjects[0].id", ID_SUBJECT_A)
                ;
    }

    public void subjectCreationShouldReturnId() throws Exception {
        final int ID = 1;
        final String ANY_SUBJECT_AS_JSON = "{\"name\":\"Maths\"}";
        when(subjectService.create(any(Subject.class))).thenReturn(ID);
        String idAsJson = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/v1/subjects"), ANY_SUBJECT_AS_JSON);
        with(idAsJson)
                .assertThat("$.id", equalTo(ID));
    }

    @Test(enabled = false)
    public void deletingUnexistingSubjectShoulrReturn404() throws Exception {
        final int VALID_ID = 5;
        doThrow(ResourceNotFoundException.class).when(subjectService).delete(anyInt());
        mockRequest.return404(delete("/feedbackcollection/v1/subjects/{id}", VALID_ID));
    }



}
