package offender.backend.com.example.Offender.repository;

import offender.backend.com.example.Offender.entities.UploadDataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UploadDataRepository extends JpaRepository<UploadDataRecord,Long> {
    List<UploadDataRecord> findByCreatorId(Long id);
}
