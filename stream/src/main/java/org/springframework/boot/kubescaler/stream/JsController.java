package org.springframework.boot.kubescaler.stream;

import java.net.URI;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.util.UriComponentsBuilder;

// Helper controller that shows JS test page
@Controller
public class JsController {
  private final String USER_FIELD = "userId";
  private final String PROFILE_FIELD = "profileId";
  private final String URL_FIELD = "url";

  @GetMapping("/index/{userId}/{profileId}")
  public String index(Model model, UriComponentsBuilder builder, @PathVariable UUID userId, @PathVariable UUID profileId) {
    URI uri = builder.path("/stream/{userId}/{profileId}").build(Map.of(USER_FIELD, userId, PROFILE_FIELD, profileId));
    model.addAttribute(URL_FIELD, uri);
    return "index";
  }

  @GetMapping("/")
  public ResponseEntity<String> indexBlank() {
    return ResponseEntity.ok("Use /index/userId/profileId/");
  }
}
