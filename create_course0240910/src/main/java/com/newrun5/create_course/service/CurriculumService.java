package com.newrun5.create_course.service;

import com.newrun5.create_course.dao.CurriculumDao;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.newrun5.create_course.dto.CurriculumDto;

import java.util.List;

@Service
public class CurriculumService {

    @Autowired
    private CurriculumDao curriculumDao;

    public CurriculumDto createCurriculum(String topic, String reason, String difficulty) {
        // Implement logic based on user's input for reason and difficulty
        // Call DAO layer to save or retrieve curriculum data
        CurriculumDto curriculum = new CurriculumDto();
        // Set properties and save to database

        curriculum.setTopic(topic);
        curriculum.setReason(reason);
        curriculum.setDifficulty(difficulty);

        // Save curriculum to database
        curriculumDao.insertCurriculum(curriculum);

        return curriculum;
    }

    public List<String> getCertificationList(String topic) {
        // Return a list of certifications based on the topic
        return curriculumDao.findCertificationsByTopic(topic);
    }
}

