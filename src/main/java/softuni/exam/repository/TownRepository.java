package softuni.exam.repository;


import org.aspectj.lang.JoinPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Town;

@Repository
public interface TownRepository  extends JpaRepository<Town, Long> {

    Town findTownByName(String name);

}
