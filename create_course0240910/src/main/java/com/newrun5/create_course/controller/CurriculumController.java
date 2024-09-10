package com.newrun5.create_course.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.newrun5.create_course.service.CurriculumService;
import com.newrun5.create_course.dto.CurriculumDto;

import java.util.List;

@RestController
@RequestMapping("/api/curriculum")
public class CurriculumController {

    @Autowired
    private CurriculumService curriculumService;

    @PostMapping("/create")
    public CurriculumDto createCurriculum(@RequestBody CurriculumRequest request) {
        return curriculumService.createCurriculum(request.getTopic(), request.getReason(), request.getDifficulty());
    }

    @GetMapping("/certifications")
    public List<String> getCertifications(@RequestParam String topic) {
        return curriculumService.getCertificationList(topic);
    }
}

