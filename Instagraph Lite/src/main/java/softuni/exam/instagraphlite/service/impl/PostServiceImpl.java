package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PostSeedRootDto;
import softuni.exam.instagraphlite.models.entity.PostEntity;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.service.PostService;
import softuni.exam.instagraphlite.service.UserService;
import softuni.exam.instagraphlite.util.ValidationUtil;
import softuni.exam.instagraphlite.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PostServiceImpl implements PostService {
    private static final String POSTS_FILE_PATH = "src/main/resources/files/posts.xml";
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final PictureService pictureService;
    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, ModelMapper modelMapper, XmlParser xmlParser, ValidationUtil validationUtil, PictureService pictureService, UserService userService) {
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.pictureService = pictureService;
        this.userService = userService;
    }


    @Override
    public boolean areImported() {
        return postRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files
                .readString(Path.of(POSTS_FILE_PATH));
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        xmlParser
                .fromFile(POSTS_FILE_PATH, PostSeedRootDto.class)
                .getPosts()
                .stream()
                .filter(postSeedDto -> {
                    boolean isValid = validationUtil.isValid(postSeedDto)
                            && pictureService.isEntityExists(postSeedDto.getPicture().getPath())
                            && userService.isEntityExists(postSeedDto.getUser().getUsername());

                    sb
                            .append(isValid ? String.format("Successfully imported Post, made by %s", postSeedDto.getUser().getUsername())
                                    : "Invalid Post")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(postSeedDto -> {
                    PostEntity post = modelMapper.map(postSeedDto, PostEntity.class);
                    post.setUser(userService.findUserByUsername(postSeedDto.getUser().getUsername()));
                    post.setPicture(pictureService.findByPath(postSeedDto.getPicture().getPath()));
                    return post;
                })
                .forEach(postRepository::save);

        return sb.toString();
    }
}
