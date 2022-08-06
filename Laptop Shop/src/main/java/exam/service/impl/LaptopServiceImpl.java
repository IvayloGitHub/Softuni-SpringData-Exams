package exam.service.impl;

import com.google.gson.Gson;
import exam.model.entity.LaptopEntity;
import exam.model.entity.dto.LaptopSeedDto;
import exam.repository.LaptopRepository;
import exam.service.LaptopService;
import exam.service.ShopService;
import exam.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class LaptopServiceImpl implements LaptopService {

    private static final String LAPTOPS_FILE_PATH = "src/main/resources/files/json/laptops.json";
    private final LaptopRepository laptopRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ShopService shopService;

    public LaptopServiceImpl(LaptopRepository laptopRepository, ModelMapper modelMapper, Gson gson, ValidationUtil validationUtil, ShopService shopService) {
        this.laptopRepository = laptopRepository;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.shopService = shopService;
    }

    @Override
    public boolean areImported() {
        return laptopRepository.count() > 0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        return Files
                .readString(Path.of(LAPTOPS_FILE_PATH));
    }

    @Override
    public String importLaptops() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readLaptopsFileContent(), LaptopSeedDto[].class))
                .filter(laptopSeedDto -> {
                    boolean isValid = validationUtil.isValid(laptopSeedDto)
                            && !isEntityExistsByMacAddress(laptopSeedDto.getMacAddress());

                    sb
                            .append(isValid ? String.format("Successfully imported Laptop %s - %.2f - %d - %d",
                                    laptopSeedDto.getMacAddress(), laptopSeedDto.getCpuSpeed(), laptopSeedDto.getRam(),
                                    laptopSeedDto.getStorage())
                                    : "Invalid Laptop")
                            .append(System.lineSeparator());
                    return isValid;
                })
                .map(laptopSeedDto -> {
                    LaptopEntity laptop = modelMapper.map(laptopSeedDto, LaptopEntity.class);
                    laptop.setShop(shopService.findShopByName(laptopSeedDto.getShop().getName()));
                    return laptop;
                })
                .forEach(laptopRepository::save);
        return sb.toString();
    }

    private boolean isEntityExistsByMacAddress(String macAddress) {
        return laptopRepository.existsByMacAddress(macAddress);
    }

    @Override
    public String exportBestLaptops() {
        StringBuilder sb = new StringBuilder();

        laptopRepository.findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc()
                .forEach(laptopEntity -> {
                    sb
                            .append(String.format("""
                                    Laptop - %s
                                    *Cpu speed - %.2f
                                    **Ram - %d
                                    ***Storage - %d
                                    ****Price - %.2f
                                    #Shop name - %s
                                    ##Town - %s
                                    """,
                                    laptopEntity.getMacAddress(), laptopEntity.getCpuSpeed(),
                                    laptopEntity.getRam(), laptopEntity.getStorage(),
                                    laptopEntity.getPrice(), laptopEntity.getShop().getName(),
                                    laptopEntity.getShop().getTown().getName()))
                            .append(System.lineSeparator());
                });
        return sb.toString();
    }
}
