package ru.megaeater42.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.megaeater42.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByStateChatId(Long chatId);
    Optional<User> findByUserName(String userName);
}
