package com.sesac7.hellopet.domain.user.service;

import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserFinderMemoryImpl implements UserFinder {
    private final UserRepository userRepository;

    private static final Deque<Map<LocalDateTime, User>> userTTL = new ArrayDeque<>();
    private static final Map<String, User> userMap = new HashMap<>();

    public User findLoggedInUserByUsername(String email) {
        evictExpired();
        User user;

        if (userMap.get(email) != null) {
            user = userMap.get(email);
        } else {
            user = userRepository.findByEmail(email)
                                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "등록된 유저가 없습니다."));
            userMap.put(user.getEmail(), user);
            Map<LocalDateTime, User> expired = new HashMap<>();
            expired.put(LocalDateTime.now().plusMinutes(30), user);
            userTTL.addLast(expired);
        }
        return user;
    }

    public void deleteUsername(String email) {
        evictExpired();
        if (userMap.get(email) != null) {
            userMap.remove(email);
        }
    }

    private void evictExpired() {

        while (!userTTL.isEmpty()) {
            Map<LocalDateTime, User> head = userTTL.getFirst();
            Map.Entry<LocalDateTime, User> entry = head.entrySet().iterator().next();

            LocalDateTime expired = entry.getKey();
            User user = entry.getValue();

            if (expired.isAfter(LocalDateTime.now())) {
                break;
            }
            userTTL.pollFirst();
            userMap.remove(user.getEmail());
        }
    }
}
