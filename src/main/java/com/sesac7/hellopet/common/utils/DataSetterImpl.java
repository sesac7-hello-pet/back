package com.sesac7.hellopet.common.utils;

import com.sesac7.hellopet.common.store.DataStore;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import com.sesac7.hellopet.domain.board.entity.BoardComment;
import com.sesac7.hellopet.domain.board.entity.PetType;
import com.sesac7.hellopet.domain.board.repository.BoardCommentRepository;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import com.sesac7.hellopet.domain.user.entity.User;
import com.sesac7.hellopet.domain.user.entity.UserDetail;
import com.sesac7.hellopet.domain.user.entity.UserRole;
import com.sesac7.hellopet.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class DataSetterImpl implements DataSetter {

    DataStore data = new DataStore();

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final AnnouncementRepository announcementRepository;
    private final BoardRepository boardRepository;
    private final BoardCommentRepository boardCommentRepository;

    private final PasswordEncoder passwordEncoder;

    private void saveUser(int number) {
        for (int i = 0; i < number; i++) {
            UserRole role;
            if (i % 10 == 0) {
                role = UserRole.ADMIN;
            } else if (i % 2 != 0) {
                role = UserRole.USER;
            } else {
                role = UserRole.SHELTER;
            }
            String phone = "010" + getRandomIntInRange(1000, 9999)
                    + getRandomIntInRange(1000, 9999);

            User user = new User(null, "test" + i + "@test.com", passwordEncoder.encode("!test1234"), role, true, null);

            UserDetail userDetail = new UserDetail(null,
                    data.getNicknames().get(getRandomIndex(data.getNicknames())) + i,
                    data.getNames().get(getRandomIndex(data.getNames())),
                    data.getUserProfiles().get(getRandomIndex(data.getUserProfiles())),
                    data.getAddresses().get(getRandomIndex(data.getAddresses())), phone, user);

            user.setUserDetail(userDetail);
            userRepository.save(user);
        }
    }

    private void saveAnnouncement(int number) {

        List<User> shelters = userRepository.findByRole(UserRole.SHELTER);
        String gender;
        for (int i = 0; i < number; i++) {

            if (i % 2 == 0) {
                gender = "수컷";
            } else {
                gender = "암컷";
            }

            Pet pet = new Pet(null, gender,
                    data.getHealthStatuses().get(getRandomIndex(data.getHealthStatuses())),
                    data.getPersonalities().get(getRandomIndex(data.getPersonalities())),
                    i % 10,

                    data.getAddresses().get(getRandomIndex(data.getAddresses())),
                    "견종",
                    data.getDogPhotos().get(getRandomIndex(data.getDogPhotos()))

                    );


            petRepository.save(pet);

            Announcement announcement = new Announcement(null,
                    shelters.get(getRandomIndex(shelters)),
                    AnnouncementStatus.IN_PROGRESS,
                    pet,
                    LocalDateTime.now(), null);

            announcementRepository.save(announcement);
        }
    }

    private void saveBoard(int num) {

        List<User> users = userRepository.findAll();

        BoardCategory[] boardCategory = new BoardCategory[]{BoardCategory.FREE, BoardCategory.QNA, BoardCategory.TOTAL};
        PetType[] petType = new PetType[]{PetType.DOG, PetType.CAT, PetType.ETC};

        for (int i = 0; i < num; i++) {
            Board board = new Board(null,
                    data.getBoardTitles().get(getRandomIndex(data.getBoardTitles())),
                    data.getBoardContents().get(getRandomIndex(data.getBoardContents())),
                    data.getDogPhotos().get(getRandomIndex(data.getDogPhotos())),
                    0, 0, 0, LocalDateTime.now(), null,
                    boardCategory[ThreadLocalRandom.current().nextInt(boardCategory.length)],
                    petType[ThreadLocalRandom.current().nextInt(petType.length)],
                    users.get(getRandomIndex(users)), null
            );

            boardRepository.save(board);
        }

    }

    private void saveComment(int num) {
        List<User> users = userRepository.findAll();
        List<Board> boards = boardRepository.findAll();

        for (int i = 0; i < num; i++) {
            BoardComment parentComment = new BoardComment(null,
                    data.getComments().get(getRandomIndex(data.getComments())),
                    LocalDateTime.now(), null,
                    users.get(getRandomIndex(users)),
                    boards.get(getRandomIndex(boards)),
                    null
            );
            boardCommentRepository.save(parentComment);
        }

        List<BoardComment> parents = boardCommentRepository.findAll();

        for (int i = 0; i < num; i++) {
            BoardComment children = new BoardComment(null,
                    data.getComments().get(getRandomIndex(data.getComments())),
                    LocalDateTime.now(), null,
                    users.get(getRandomIndex(users)),
                    boards.get(getRandomIndex(boards)),
                    parents.get(getRandomIndex(parents))
            );
            boardCommentRepository.save(children);
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
        return ThreadLocalRandom.current().nextInt(start, end + 1);
    }

    @Override
    public void userGenerator(int num) {
        int howMany = userRepository.findAll().size();
        saveUser(num - howMany);
    }

    @Override
    public void announcementGenerator(int num) {
        int howMany = announcementRepository.findAll().size();
        saveAnnouncement(num - howMany);
    }

    @Override
    public void boardGenerator(int num) {
        int howMany = boardRepository.findAll().size();
        saveBoard(num - howMany);
    }

    @Override
    public void commentGenerator(int num) {
        int howMany = boardCommentRepository.findAll().size();
        saveComment(num - howMany);
    }
}
