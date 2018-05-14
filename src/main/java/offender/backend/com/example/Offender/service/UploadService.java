package offender.backend.com.example.Offender.service;

import offender.backend.com.example.Offender.entities.UploadDataRecord;
import offender.backend.com.example.Offender.entities.User;
import offender.backend.com.example.Offender.repository.UploadDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UploadService {
    @Autowired
    private UploadDataRepository uploadDataRepository;

    public List<UploadDataRecord> findByUser(User user){
        return uploadDataRepository.findByCreatorId(user.getId());
    }

    public void insert(UploadDataRecord uploadDataRecord){
        uploadDataRepository.save(uploadDataRecord);
    }
}
