package com.example.persistence.repository;

import com.example.persistence.entity.DataEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataEventRepository extends JpaRepository<DataEventEntity, String> {
}
