package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Game;
import ch.uzh.ifi.hase.soprafs22.service.GameService;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.spring.vision.CloudVisionTemplate;

import java.text.DecimalFormat;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.protocol.ResponseContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;


@RestController
public class VisionController {

    @Autowired private CloudVisionTemplate cloudVisionTemplate;

    @Autowired private GameService gameService;

    /**
     * This method checks the current image and sends to the Vision API for label
     * detection.
     *
     * @param gameToken game identification
     * @param map the model map to use
     * @return a string with the list of labels and percentage of certainty
     */
    @GetMapping("/vision/{gameToken}")
    public ResponseEntity<?> extractLabels(@PathVariable String gameToken, ModelMap map){
        Game game = gameService.getGameByToken(gameToken);
        String image = game.getGameRoundList().get(game.getCurrentGameRound()).getImg();
        if (image==null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The image is null");
        }
        image = image.replace("data:image/png;base64,", "");
        byte[] decodeImg = Base64.getDecoder().decode(image);
        Resource resource = new ByteArrayResource(decodeImg);

        AnnotateImageResponse response =
                this.cloudVisionTemplate.analyzeImage(
                        resource , Type.LABEL_DETECTION);

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

        if(imageLabels.isEmpty()){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No classification was fetched vom Google Vision API");
        }
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        Map<String, String> formatedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Float> entry : imageLabels.entrySet() ) {
            double newFloat = Math.round(entry.getValue() * 100.0);
            formatedMap.put(entry.getKey(), (int)newFloat + "%");
        }

        map.addAttribute("annotations", formatedMap);
        //map.addAttribute("gameID", gameToken);
        gameService.setMap(map);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/vision/{gameToken}/drawerPoints")
    public ResponseEntity<String> givePointsToDrawer(@PathVariable String gameToken){
        return gameService.givePointsToDrawer(gameToken);
    }
}

