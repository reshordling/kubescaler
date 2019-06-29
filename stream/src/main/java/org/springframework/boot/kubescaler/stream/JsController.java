package org.springframework.boot.kubescaler.stream;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Helper controller that shows JS test page
@Controller
public class JsController {
  @GetMapping("/")
  public String index() {
    return "index";
  }
}
