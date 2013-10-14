package lt.agmis.feedbackcollection.integration.testutils;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created with IntelliJ IDEA.
 * User: ignas
 * Date: 9/13/13
 * Time: 8:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class MockRequest {


    private MockMvc mockMvc;

    public MockRequest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    public String retrieveJson(MockHttpServletRequestBuilder link) throws Exception {
        MvcResult result = mockMvc.perform(link.accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        return json;
    }

    public String submitJsonRetrieveJson(MockHttpServletRequestBuilder link, String jsonString) throws Exception {
        MvcResult result = mockMvc.perform(
                link
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();
        String json = result.getResponse().getContentAsString();
        return json;
    }

    public void submitJson(MockHttpServletRequestBuilder link, String jsonString) throws Exception {
        mockMvc.perform(
                link
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
            .andExpect(status().isOk());
    }

    public void submitRequest(MockHttpServletRequestBuilder link) throws Exception {
        mockMvc.perform(link)
                .andExpect(status().isOk());
    }
}
