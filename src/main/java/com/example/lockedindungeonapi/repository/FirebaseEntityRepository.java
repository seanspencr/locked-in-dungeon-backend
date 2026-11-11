package com.example.lockedindungeonapi.repository;

import com.example.lockedindungeonapi.model.FirebaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FirebaseEntityRepository extends JpaRepository<FirebaseEntity, Long> {

}
