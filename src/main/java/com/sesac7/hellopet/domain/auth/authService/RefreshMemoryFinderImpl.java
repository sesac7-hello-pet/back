package com.sesac7.hellopet.domain.auth.authService;

import com.sesac7.hellopet.domain.auth.entity.RefreshToken;
import com.sesac7.hellopet.domain.user.entity.User;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RefreshMemoryFinderImpl {

    private static final PriorityQueue<RefreshToken> userTTL = new PriorityQueue<>(
            Comparator.comparing(RefreshToken::getCreatedAt));
    private static final Map<String, RefreshToken> tokenStore = new ConcurrentHashMap<>();
    private static final Map<Long, String> userIndex = new ConcurrentHashMap<>();
    private static final Map<String, Long> emailIndex = new ConcurrentHashMap<>();

    public void saveRefresh(RefreshToken refreshToken) {
        evictExpired();

        tokenStore.put(refreshToken.getToken(), refreshToken);
        userIndex.put(refreshToken.getUser().getId(), refreshToken.getToken());
        emailIndex.put(refreshToken.getUser().getEmail(), refreshToken.getUser().getId());

        userTTL.offer(refreshToken);
    }

    public void deleteRefreshByUser(User foundUser) {
        evictExpired();
        String token = userIndex.remove(foundUser.getId());
        if (token != null) {
            tokenStore.remove(token);
            emailIndex.remove(foundUser.getEmail());
        }
    }

    public User getUserByToken(String token) {
        evictExpired();
        RefreshToken rt = tokenStore.get(token);
        return rt != null ? rt.getUser() : null;
    }

    public boolean existRefreshByUser(User foundUser) {
        evictExpired();
        return userIndex.get(foundUser.getId()) != null;
    }

    public void deleteRefreshByEmail(String email) {
        evictExpired();
        Long l = emailIndex.remove(email);
        String s = userIndex.remove(l);
        tokenStore.remove(s);
    }

    private void evictExpired() {
        LocalDateTime now = LocalDateTime.now();

        while (!userTTL.isEmpty() &&
                userTTL.peek().getCreatedAt().isBefore(now)) {

            RefreshToken expired = userTTL.poll();      // O(log n)

            tokenStore.remove(expired.getToken());
            userIndex.remove(expired.getUser().getId());
            emailIndex.remove(expired.getUser().getEmail());
        }
    }
}
