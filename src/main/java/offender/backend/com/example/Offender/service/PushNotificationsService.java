package offender.backend.com.example.Offender.service;

import offender.backend.com.example.Offender.interceptor.HeaderRequestInterceptor;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

@Service
public class PushNotificationsService {

    private static final String FIREBASE_SERVER_KEY = "AAAAhbhH6pY:APA91bGDQJPO8o7Kcnu49coTKEe3KzLo95yeD0LyWtH0CmlV-5QJYxK_sHB4FCmMEPmOX_24QMB69roahvUf5ttQuR1eDZAAE-bLjqsAUDZZnY1PVGKX1wGpbep8cFJB10ym6aqZKZ3mX7Tmj9PmP9ovBDeKug0mpA";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        /**
         https://fcm.googleapis.com/fcm/send
         Content-Type:application/json
         Authorization:key=FIREBASE_SERVER_KEY*/

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
