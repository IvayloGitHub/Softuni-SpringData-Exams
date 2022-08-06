package softuni.exam.instagraphlite.service;

import softuni.exam.instagraphlite.models.entity.UserEntity;

import java.io.IOException;

public interface UserService {
    boolean areImported();
    String readFromFileContent() throws IOException;
    String importUsers() throws IOException;

    boolean isEntityExists(String username);

    String exportUsersWithTheirPosts();

    UserEntity findUserByUsername(String username);
}
