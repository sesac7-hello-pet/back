package com.sesac7.hellopet.domain.announcement.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface ShelterRepository extends JpaRepository<Shelter, Long> {
    // 필요 시 이름으로 찾기 같은 커스텀 추가 가능
}

