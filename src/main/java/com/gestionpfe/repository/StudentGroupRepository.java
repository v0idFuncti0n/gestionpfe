package com.gestionpfe.repository;

import com.gestionpfe.model.StudentGroup;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface StudentGroupRepository extends CrudRepository<StudentGroup, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM StudentGroup sg WHERE sg.id = ?1")
    void deleteById(Long studentGroup);
}
