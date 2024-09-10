package com.newrun5.create_course.dao;

import com.newrun5.create_course.dto.CurriculumDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CurriculumDao {

    @Select("SELECT certification_name FROM certifications WHERE topic = #{topic}")
    List<String> findCertificationsByTopic(@Param("topic") String topic);

    @Insert("INSERT INTO curriculums (topic, reason, difficulty) VALUES (#{topic}, #{reason}, #{difficulty})")
    void insertCurriculum(CurriculumDto curriculumDto);

    // Define other methods as needed
}
