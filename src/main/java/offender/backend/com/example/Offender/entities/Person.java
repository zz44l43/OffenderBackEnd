package offender.backend.com.example.Offender.entities;

import lombok.Data;

@Data
public class Person {
    Entities entity;
    Double facialSimilarity;
    Double voiceSimilarity;
    Double overallSimilarity;


}
