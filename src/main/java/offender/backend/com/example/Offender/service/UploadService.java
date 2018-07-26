package offender.backend.com.example.Offender.service;

import offender.backend.com.example.Offender.entities.AttributesVM;
import offender.backend.com.example.Offender.entities.UploadDataRecord;
import offender.backend.com.example.Offender.entities.User;
import offender.backend.com.example.Offender.repository.AttributesRepository;
import offender.backend.com.example.Offender.repository.UploadDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadService {
    @Autowired
    private UploadDataRepository uploadDataRepository;

    @Autowired
    private AttributesRepository attributeDataRepository;

    public List<UploadDataRecord> findByUser(User user){
        return uploadDataRepository.findByCreatorId(user.getId());
    }

    public List<AttributesVM> findAttributesByUser(User user){
        return attributeDataRepository.findByCreatorId(user.getId());
    }

    public AttributesVM findAttributesWithUser(String key, User user){
        return attributeDataRepository.findByAttributeKeyAndCreator(key,user);
    }

    public void insert(UploadDataRecord uploadDataRecord){
        uploadDataRepository.save(uploadDataRecord);
    }

    public void insertAttribute(AttributesVM attributesVM){attributeDataRepository.save(attributesVM);}
    public void saveOrUpdateAttributes(AttributesVM attributesVM, User user){
        AttributesVM attributesVMExisting = findAttributesWithUser(attributesVM.getAttributeKey(),user);
        if(attributesVMExisting == null) {
            attributeDataRepository.save(attributesVM);
        }
        else{
            attributeDataRepository.setSelectedByAttributeKeyAndCreator(false, attributesVM.getAttributeKey(),user.getId());
        }
    }

    public void update(AttributesVM attributesVM, User user){

    }
}
