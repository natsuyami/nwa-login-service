package com.natsuyami.project.nwa.repository;

import com.natsuyami.project.nwa.model.NwaUserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface NwaUserRepository extends JpaRepository<NwaUserModel, Long> {
  List<NwaUserModel> findByCreated(Date created);
  NwaUserModel findByUsername(String username);
}
