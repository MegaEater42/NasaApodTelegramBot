package ru.megaeater42.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.megaeater42.entity.User;
import ru.megaeater42.entity.UserState;
import ru.megaeater42.entity.enums.State;
import ru.megaeater42.repository.UserRepository;
import ru.megaeater42.repository.UserStateRepository;
import ru.megaeater42.service.UserService;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserStateRepository userStateRepository;

    public User findOrCreateUserByClassifiedUpdate(ClassifiedUpdate classifiedUpdate) {
        Optional<User> optionalUser = userRepository.findByStateChatId(classifiedUpdate.getChatId());
        return optionalUser.map(user -> updateExistUser(classifiedUpdate, user)).orElseGet(() -> createUserByClassifiedUpdate(classifiedUpdate));
    }

    public User createUserByClassifiedUpdate(ClassifiedUpdate classifiedUpdate) {
        try {
            User user = new User();
            user.setUserName(classifiedUpdate.getUserName());
            user.setFirstName(classifiedUpdate.getFirstName());
            user.setLastName(classifiedUpdate.getLastName());

            UserState userState = new UserState();
            userState.setChatId(classifiedUpdate.getChatId());
            userState.setValue(State.BASIC_ACCESS);
            userState.setUser(user);

            user.setState(userState);
            userStateRepository.save(userState);

            return user;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private User updateExistUser(ClassifiedUpdate classifiedUpdate, User user) {
        if ((user.getUserName() != null) && (!user.getUserName().equals(classifiedUpdate.getUserName()))) {
            user.setUserName(classifiedUpdate.getUserName());
        }
        if((user.getFirstName() == null) && (classifiedUpdate.getFirstName() != null)) {
            user.setFirstName(classifiedUpdate.getFirstName());
        }
        if((user.getLastName() == null) && (classifiedUpdate.getLastName() != null)) {
            user.setLastName(classifiedUpdate.getLastName());
        }
        return user;
    }
}
