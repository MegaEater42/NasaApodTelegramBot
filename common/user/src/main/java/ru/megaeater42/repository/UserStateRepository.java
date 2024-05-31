package ru.megaeater42.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.UserState;

import java.util.Optional;

@Repository
public interface UserStateRepository extends JpaRepository<UserState, Long> {
    Optional<UserState> findByChatId(Long chatId);
    Optional<UserState> findByUser(User user);
}