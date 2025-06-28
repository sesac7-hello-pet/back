package com.sesac7.hellopet.common.utils;

import com.sesac7.hellopet.common.store.DataStore;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@Transactional
@RequiredArgsConstructor
public class DataSetterImpl implements DataSetter {

    DataStore data = new DataStore();

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void saveUser(int number) {
        for (int i = 0; i < number; i++) {
            UserRole role;
            if(i % 10 == 0) {
                role = UserRole.ADMIN;
            } else if (i % 2 != 0) {
                role = UserRole.USER;
            } else {
                role = UserRole.SHELTER;
            }
            Integer phone = Integer.valueOf(new StringBuilder().append("010").append(getRandomIntInRange(1000, 9999)).append(getRandomIntInRange(1000, 9999)).toString());

            User user = new User(null, "test" + i +"@test.com", passwordEncoder.encode("!test1234"), role, null);

            UserDetail userDetail = new UserDetail(null,
                    data.getNicknames().get(getRandomIndex(data.getNicknames())) + i,
                    data.getNames().get(getRandomIndex(data.getNames())),
                    data.getUserProfiles().get(getRandomIndex(data.getUserProfiles())),
                    data.getAddresses().get(getRandomIndex(data.getAddresses())), phone, user);

            user.setUserDetail(userDetail);
            userRepository.save(user);
        }
    }

    private int getRandomIndex(List<?> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        return ThreadLocalRandom.current().nextInt(list.size());
    }

    private int getRandomIntInRange(int start, int end) {
        if (end < start) {
            throw new IllegalArgumentException("end must be >= start");
        }
        // ThreadLocalRandom.current().nextInt(origin, bound) 은 bound 미포함이므로 end+1 사용
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }
}
