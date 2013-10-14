package lt.agmis.feedbackcollection.integration.moduletests;


import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;

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

import static lt.agmis.feedbackcollection.integration.matchers.IsPositiveInteger.positiveInteger;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

import static com.jayway.jsonassert.JsonAssert.emptyCollection;
import static com.jayway.jsonassert.JsonAssert.with;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;



@WebAppConfiguration
@ContextConfiguration({"classpath:beans/context/root-context.xml", "classpath:beans/context/servlet-context.xml"})
@Test
@ActiveProfiles(profiles={"db-integration-test","system-integration-test"})
public class CategoryIT extends AbstractTestNGSpringContextTests
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
				.defaultRequest(get("/").contextPath("/feedbackcollection").servletPath("/api"))
				.build();
		mockRequest = new lt.agmis.feedbackcollection.integration.testutils.MockRequest(mockMvc);

		databaseTester.setDataSet(getFlatXmlDataSet());
		databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
		databaseTester.setTearDownOperation(DatabaseOperation.NONE);
		databaseTester.onSetup();
	}
	
	private IDataSet getFlatXmlDataSet() throws Exception 
	 {        
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
	    builder.setColumnSensing(true);
	    IDataSet dataSet = builder.build(new File(TEST_DATA_LOCATION + "category-it.xml"));
	    return dataSet;
	 }

    @DataProvider
    public static final Object[][] getUnsuportedHttpMethodsForCategoryWs() {
        return new Object[][] {
                {"categories PUT method",
                        put("/feedbackcollection/api/categories")},
                {"categories DELETE method",
                        delete("/feedbackcollection/api/categories")},
                {"categories/id GET method",
                        get("/feedbackcollection/api/categories/{id}", 1)},
                {"categories/id POST method",
                        post("/feedbackcollection/api/categories/{id}", 1)}};
    }

    @Test(dataProvider = "getUnsuportedHttpMethodsForCategoryWs")
    public void categoryWsShouldRejectUnsuportedHttpMethodsWith404(String description, MockHttpServletRequestBuilder link) throws Exception {
        mockMvc.perform(link).andExpect(status().isMethodNotAllowed());
    }

    @Test(dependsOnMethods = {"categoryWsShouldRejectUnsuportedHttpMethodsWith404"})
     public void shouldReturnZeroCategoriesWhenNoCategoriesWereCreated() throws Exception {
        String json =  mockRequest.retrieveJson(get("/feedbackcollection/api/categories"));

        with(json).assertThat("$.categories", is(emptyCollection()));
    }

    @Test(dependsOnMethods = {"categoryWsShouldRejectUnsuportedHttpMethodsWith404"})
    public void creationOfCategoryShouldReturnId() throws Exception {
        String json = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/api/categories"), "{\"name\":\"Programming Languages Category\"}");

        with(json)
                .assertThat("$.success", is(equalTo(true)))
                .assertThat("$.id", is(positiveInteger()));
    }

    @Test(dependsOnMethods = {"creationOfCategoryShouldReturnId", "shouldReturnZeroCategoriesWhenNoCategoriesWereCreated"})
    public void shouldCreateAndRetrieveCategory() throws Exception {
        String jsonWithCreatedCategoryId = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/api/categories"), "{\"name\":\"Programming Languages Category\"}");
        Integer categoryId = JsonPath.read(jsonWithCreatedCategoryId, "$.id");
        String jsonCategoryList = mockRequest.retrieveJson(get("/feedbackcollection/api/categories"));
        String jsonCategory = extractCategoryJsonFromListJsonWithId(jsonCategoryList, categoryId);
        with(jsonCategory)
                .assertThat("$.id", equalTo(categoryId))
                .assertThat("$.name", equalTo("Programming Languages Category"));
    }

    @Test(dependsOnMethods = {"shouldCreateAndRetrieveCategory"})
    public void shouldUpdateCategory() throws Exception {
        String jsonWithCreatedCategoryId = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/api/categories"), "{\"name\":\"Programming Languages Category\"}");
        Integer categoryId = JsonPath.read(jsonWithCreatedCategoryId, "$.id");
        mockRequest.submitJson(put("/feedbackcollection/api/categories/{id}", categoryId), "{\"name\":\"Updated Category\"}");
        String jsonCategoryList = mockRequest.retrieveJson(get("/feedbackcollection/api/categories"));
        String jsonCategory = extractCategoryJsonFromListJsonWithId(jsonCategoryList, categoryId);
        with(jsonCategory)
                .assertThat("$.id", equalTo(categoryId))
                .assertThat("$.name", equalTo("Updated Category"));
    }

    @Test(dependsOnMethods = {"shouldCreateAndRetrieveCategory"})
    public void shouldDeleteCategory() throws Exception {
        String jsonWithCreatedCategoryId = mockRequest.submitJsonRetrieveJson(post("/feedbackcollection/api/categories"), "{\"name\":\"Programming Languages Category\"}");
        Integer categoryId = JsonPath.read(jsonWithCreatedCategoryId, "$.id");
        mockRequest.submitRequest(delete("/feedbackcollection/api/categories/{id}", categoryId));
        String jsonCategoryList = mockRequest.retrieveJson(get("/feedbackcollection/api/categories"));
        with(jsonCategoryList)
                .assertThat("$.categories[*].id", not(hasItem(categoryId)));
    }

    private String extractCategoryJsonFromListJsonWithId(String jsonCategoryList, int categoryId) {
        with(jsonCategoryList)
                .assertThat("$.categories[*].id", hasItem(categoryId));
        List<Object> categoriesJsons = JsonPath.read(jsonCategoryList, "$.categories[?]", filter(where("id").is(categoryId)));

        Object jsonCategory = categoriesJsons.get(0);
        return jsonCategory.toString();
    }
}