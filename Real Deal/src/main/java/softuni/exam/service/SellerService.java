package softuni.exam.service;

import softuni.exam.models.entity.SellerEntity;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface SellerService {
    
    boolean areImported();

    String readSellersFromFile() throws IOException;

    String importSellers() throws IOException, JAXBException;

    SellerEntity findById(Long id);
}
