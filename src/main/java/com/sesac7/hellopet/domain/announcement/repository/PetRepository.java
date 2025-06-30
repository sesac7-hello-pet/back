package com.sesac7.hellopet.domain.announcement.repository;

import com.sesac7.hellopet.domain.announcement.entity.Pet;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    // 공고번호로 찾기 (PK가 공고번호일 경우)


}
