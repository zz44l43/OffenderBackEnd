package offender.backend.com.example.Offender.repository;

import offender.backend.com.example.Offender.entities.Attributes;
import offender.backend.com.example.Offender.entities.AttributesVM;
import offender.backend.com.example.Offender.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AttributesRepository extends JpaRepository<AttributesVM,Long> {
    List<AttributesVM> findByCreatorId(Long id);
    AttributesVM findByAttributeKeyAndCreator(String attributeKey, User creator);

    @Modifying
    @Transactional
    @Query("update AttributesVM a set a.selected = ?1 where a.attributeKey = ?2 and a.creator.id=?3")
    void setSelectedByAttributeKeyAndCreator(boolean selected, String attributeKey,Long id);

}