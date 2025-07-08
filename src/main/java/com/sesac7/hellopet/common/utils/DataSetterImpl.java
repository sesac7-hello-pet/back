package com.sesac7.hellopet.common.utils;

import com.sesac7.hellopet.common.store.DataStore;
import com.sesac7.hellopet.domain.announcement.entity.Announcement;
import com.sesac7.hellopet.domain.announcement.entity.AnnouncementStatus;
import com.sesac7.hellopet.domain.announcement.entity.Pet;
import com.sesac7.hellopet.domain.announcement.repository.AnnouncementRepository;
import com.sesac7.hellopet.domain.announcement.repository.PetRepository;
import com.sesac7.hellopet.domain.application.dto.request.*;
import com.sesac7.hellopet.domain.application.entity.Application;
import com.sesac7.hellopet.domain.application.entity.ApplicationStatus;
import com.sesac7.hellopet.domain.application.entity.info.agreement.AgreementInfo;
import com.sesac7.hellopet.domain.application.entity.info.care.CareInfo;
import com.sesac7.hellopet.domain.application.entity.info.experience.PetExperienceInfo;
import com.sesac7.hellopet.domain.application.entity.info.family.FamilyInfo;
import com.sesac7.hellopet.domain.application.entity.info.financial.FinancialInfo;
import com.sesac7.hellopet.domain.application.entity.info.housing.HousingInfo;
import com.sesac7.hellopet.domain.application.entity.info.plan.FuturePlanInfo;
import com.sesac7.hellopet.domain.application.repository.ApplicationRepository;
import com.sesac7.hellopet.domain.board.entity.Board;
import com.sesac7.hellopet.domain.board.entity.BoardCategory;
import com.sesac7.hellopet.domain.board.entity.BoardComment;
import com.sesac7.hellopet.domain.board.entity.PetType;
import com.sesac7.hellopet.domain.board.repository.BoardCommentRepository;
import com.sesac7.hellopet.domain.board.repository.BoardRepository;
import com.sesac7.hellopet.domain.comment.entity.Comment;
import com.sesac7.hellopet.domain.comment.repository.CommentRepository;
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
    private final CommentRepository commentRepository;
    private final ApplicationRepository applicationRepository;

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

            Pet pet = new Pet(null, data.getPetTypes().get(getRandomIndex(data.getPetTypes())), gender,
                    data.getHealthStatuses().get(getRandomIndex(data.getHealthStatuses())),
                    data.getPersonalities().get(getRandomIndex(data.getPersonalities())),
                    i % 10,
                    data.getAddresses().get(getRandomIndex(data.getAddresses())),
                    data.getBreeds().get(getRandomIndex(data.getBreeds())),
                    data.getDogPhotos().get(getRandomIndex(data.getDogPhotos()))

            );

            petRepository.save(pet);

            Announcement announcement = new Announcement(null,
                    shelters.get(getRandomIndex(shelters)),
                    AnnouncementStatus.IN_PROGRESS,
                    pet,
                    LocalDateTime.now(),
                    null,
                    LocalDateTime.now());

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
                    boardCategory[i % 2],
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
            Comment parentComment = new Comment(null,
                    data.getComments().get(getRandomIndex(data.getComments())),
                    LocalDateTime.now(), null,
                    boards.get(getRandomIndex(boards)),
                    users.get(getRandomIndex(users))
            );
            commentRepository.save(parentComment);
        }
    }

    public void saveApplications(int number) {
        List<User> applicants = userRepository.findByRole(UserRole.USER);
        List<Announcement> announcements = announcementRepository.findAll();

        for (int i = 0; i < number; i++) {
            // 1) 랜덤 신청자 & 공고
            User applicant = applicants.get(getRandomIndex(applicants));
            Announcement announcement = announcements.get(getRandomIndex(announcements));

            // 2) 랜덤 사유
            String reason = data.getReasons().get(getRandomIndex(data.getReasons()));

            HousingInfo housingInfo = new HousingInfo(
                    data.getHousingTypes().get(getRandomIndex(data.getHousingTypes())),
                    data.getResidenceTypes().get(getRandomIndex(data.getResidenceTypes())),
                    data.getPetAllowedOptions().get(getRandomIndex(data.getPetAllowedOptions())),
                    data.getPetLivingPlaces().get(getRandomIndex(data.getPetLivingPlaces())),
                    data.getHouseSizeRanges().get(getRandomIndex(data.getHouseSizeRanges()))
            );

            // 4) FamilyInfo
            FamilyInfo familyInfo = new FamilyInfo(
                    data.getNumberOfHouseholds().get(getRandomIndex(data.getNumberOfHouseholds())),
                    data.getHasChildUnder13Options().get(getRandomIndex(data.getHasChildUnder13Options())),
                    data.getFamilyAgreements().get(getRandomIndex(data.getFamilyAgreements())),
                    data.getHasPetAllergyOptions().get(getRandomIndex(data.getHasPetAllergyOptions()))
            );

            // 5) CareInfo
            CareInfo careInfo = new CareInfo(
                    data.getAbsenceTimes().get(getRandomIndex(data.getAbsenceTimes())),
                    data.getCareTimes().get(getRandomIndex(data.getCareTimes()))
            );

            // 6) FinancialInfo
            FinancialInfo financialInfo = new FinancialInfo(
                    data.getMonthlyBudgets().get(getRandomIndex(data.getMonthlyBudgets())),
                    data.getHasEmergencyFundOptions().get(getRandomIndex(data.getHasEmergencyFundOptions()))
            );

            // 7) PetExperienceInfo
            PetExperienceInfo petExperienceInfo = new PetExperienceInfo(
                    data.getHasPetExperienceOptions().get(getRandomIndex(data.getHasPetExperienceOptions())),
                    data.getExperienceDetails().get(getRandomIndex(data.getExperienceDetails()))
            );

            // 8) FuturePlanInfo
            FuturePlanInfo futurePlanInfo = new FuturePlanInfo(
                    data.getHasFuturePlanOptions().get(getRandomIndex(data.getHasFuturePlanOptions())),
                    data.getPlanDetails().get(getRandomIndex(data.getPlanDetails()))
            );

            // 9) AgreementInfo
            AgreementInfo agreementInfo = new AgreementInfo(
                    data.getAgreedToAccuracyOptions().get(0),
                    data.getAgreedToCareOptions().get(0),
                    data.getAgreedToPrivacyOptions().get(0)
            );

            // 10) 빌더로 Application 생성 & 저장
            Application application = Application.builder()
                    .user(applicant)
                    .announcement(announcement)
                    .reason(reason)
                    .housingInfo(housingInfo)
                    .familyInfo(familyInfo)
                    .careInfo(careInfo)
                    .financialInfo(financialInfo)
                    .petExperienceInfo(petExperienceInfo)
                    .futurePlanInfo(futurePlanInfo)
                    .agreementInfo(agreementInfo)
                    .status(ApplicationStatus.PENDING)
                    .build();

            applicationRepository.save(application);
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
        int howMany = commentRepository.findAll().size();
        saveComment(num - howMany);
    }

    @Override
    public void applicationGenerator(int num) {
        int howMany = applicationRepository.findAll().size();
        saveApplications(num - howMany);
    }
}
