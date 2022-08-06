package hiberspring.repository;

import hiberspring.domain.entities.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    @Query("SELECT e FROM EmployeeEntity AS e JOIN BranchEntity AS b ON e.branch.id = b.id WHERE SIZE(b.products) > 0 ORDER BY CONCAT(e.firstName, ' ', e.lastName), LENGTH(e.position) DESC")
    List<EmployeeEntity> findAllEmployeesWhereBranchHasMoreThanOneProduct();


    boolean existsByCard_Number(String card_number);
}
