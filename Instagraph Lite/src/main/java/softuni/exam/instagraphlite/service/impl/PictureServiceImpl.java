package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.dto.PictureSeedDto;
import softuni.exam.instagraphlite.models.entity.PictureEntity;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;
import softuni.exam.instagraphlite.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURE_FILE_PATH = "src/main/resources/files/pictures.json";
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public PictureServiceImpl(PictureRepository pictureRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }


    @Override
    public boolean areImported() {
        return pictureRepository.count() > 0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        return Files
                .readString(Path.of(PICTURE_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readFromFileContent(), PictureSeedDto[].class))
                .filter(pictureSeedDto -> {
                    boolean isValid = validationUtil.isValid(pictureSeedDto)
                            && !isEntityExists(pictureSeedDto.getPath());

                    sb
                            .append(isValid ? String.format("Successfully imported Picture, with size %.2f", pictureSeedDto.getSize())
                                    : "Invalid Picture")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(pictureSeedDto -> modelMapper.map(pictureSeedDto, PictureEntity.class))
                .forEach(pictureRepository::save);
        return sb.toString();
    }
    @Override
    public boolean isEntityExists(String path) {
        return pictureRepository.existsByPath(path);
    }

    @Override
    public String exportPictures() {
        StringBuilder sb = new StringBuilder();

        pictureRepository.findAllBySizeGreaterThanOrderBySize(30000.00)
                .forEach(pictureEntity -> {
                    sb
                            .append(String.format("%.2f – %s", pictureEntity.getSize(), pictureEntity.getPath()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }

    @Override
    public PictureEntity findByPath(String path) {
        return pictureRepository.findByPath(path)
                .orElse(null);
    }
}
