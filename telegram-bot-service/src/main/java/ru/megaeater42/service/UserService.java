package ru.megaeater42.service;

import ru.megaeater42.entity.User;
import ru.megaeater42.telegram.abstraction.ClassifiedUpdate;

public interface UserService {
    User findOrCreateUserByClassifiedUpdate(ClassifiedUpdate classifiedUpdate);
}
