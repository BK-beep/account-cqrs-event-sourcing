package ma.enset.comptecqrses.query.repositories;

import ma.enset.comptecqrses.common_api.enums.OperationType;
import ma.enset.comptecqrses.query.entities.Operation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation,Long> {
}
