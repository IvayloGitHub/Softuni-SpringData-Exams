package exam.repository;

import exam.model.entity.LaptopEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LaptopRepository extends JpaRepository<LaptopEntity, Long> {
    boolean existsByMacAddress(String macAddress);
// second option @Query("SELECT l FROM LaptopEntity AS l ORDER BY l.cpuSpeed DESC, l.ram DESC, l.storage DESC, l.macAddress")
    List<LaptopEntity> findAllByOrderByCpuSpeedDescRamDescStorageDescMacAddressAsc();
}
