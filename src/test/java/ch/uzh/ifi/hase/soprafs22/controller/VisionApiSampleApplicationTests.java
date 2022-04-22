/**package ch.uzh.ifi.hase.soprafs22.controller;

import java.util.List;
import java.util.stream.Collectors;

import ch.uzh.ifi.hase.soprafs22.service.UserService;
import com.google.cloud.spring.vision.CloudVisionTemplate;
import com.google.cloud.vision.v1.EntityAnnotation;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServlet;

import static org.assertj.core.api.Assertions.assertThat;

@WebMvcTest(VisionController.class)
public class VisionApiSampleApplicationTests {

    private static final String LABEL_IMAGE_URL = "/extractLabels?imageUrl=https://upload.wikimedia.org/wikipedia/commons/7/7d/4_Kittens.jpg";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CloudVisionTemplate cloudVisionTemplate;


    @Test
    public void testClassifyImageLabels() throws Exception {
        this.mockMvc.perform(get(LABEL_IMAGE_URL))
                .andDo((response) -> {
                    MockHttpServletResponse result = response.getResponse();
                    //List<EntityAnnotation> annotations = (List<EntityAnnotation>) result.get("annotations");

                    //List<String> annotationNames = annotations.stream()
                            //.map(annotation -> annotation.getDescription().toLowerCase().trim())
                            //.collect(Collectors.toList());

                    //assertThat(annotationNames).contains("glasses");
                });
    }
}**/