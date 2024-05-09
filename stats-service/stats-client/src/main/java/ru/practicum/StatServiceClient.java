package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatServiceClient extends BaseClient {
    @Autowired
    public StatServiceClient(RestTemplateBuilder templateBuilder) {
        super(
                templateBuilder
                        .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postHit(StatDto statDto) {
        return post("/hit", statDto, null);
    }

    public ResponseEntity<Object> getStats(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("start", start);
        parameters.put("end", end);
        if ((uris != null) && (!uris.isEmpty())) {
            parameters.put("uris", String.join(",", uris));
        }
        if (unique != null) {
            parameters.put("unique", unique);
        }
        String s = String.format("/stats?start=%s&end=%s&uris=%s&unique=%b", start, end, String.join(",", uris), unique);
        return get(s, parameters);
    }


}
