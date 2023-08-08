package com.example.hackaton;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class OpenAiController {
    @Autowired
    private OpenAiService openAiService;

    @GetMapping("/chatCompletions")
    public String chatCompletition(@RequestParam String message)  {
        return openAiService.chatCompletions(message);
    }

    @GetMapping("/generateImages")
    public void generateImages(@RequestParam String message, HttpServletResponse response) throws IOException {
        response.sendRedirect(openAiService.generateImages(message));
    }
}
