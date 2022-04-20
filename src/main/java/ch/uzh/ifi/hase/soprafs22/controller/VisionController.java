package ch.uzh.ifi.hase.soprafs22.controller;

import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.spring.vision.CloudVisionTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class VisionController {

    @Autowired private ResourceLoader resourceLoader;

    @Autowired private CloudVisionTemplate cloudVisionTemplate;

    /**
     * This method downloads an image from a URL and sends its contents to the Vision API for label
     * detection.
     *
     * @param imageUrl the URL of the image
     * @param map the model map to use
     * @return a string with the list of labels and percentage of certainty
     */
    @GetMapping("/vision")
    public ResponseEntity<?> extractLabels(String imageUrl, ModelMap map){
        AnnotateImageResponse response =
                this.cloudVisionTemplate.analyzeImage(
                        this.resourceLoader.getResource(imageUrl) , Type.LABEL_DETECTION);

        Map<String, Float> imageLabels =
                response.getLabelAnnotationsList().stream()
                        .collect(
                                Collectors.toMap(
                                        EntityAnnotation::getDescription,
                                        EntityAnnotation::getScore,
                                        (u, v) -> {
                                            throw new IllegalStateException(String.format("Duplicate key %s", u));
                                        },
                                        LinkedHashMap::new));
        // [END spring_vision_image_labelling]

        map.addAttribute("annotations", imageLabels);
        map.addAttribute("imageUrl", imageUrl);

        return ResponseEntity.ok(map);
    }
}

