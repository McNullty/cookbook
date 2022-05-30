package hr.vgsoft.cookbook.repository;

import hr.vgsoft.cookbook.domain.Unit;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Unit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    Unit findByName(String name);

    Set<Unit> findAllByNameIn(List<String> unitNames);
}
