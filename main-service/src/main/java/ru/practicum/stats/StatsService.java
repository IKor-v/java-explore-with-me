package ru.practicum.stats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;
import ru.practicum.StatServiceClient;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.entity.Event;
import ru.practicum.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
//@Slf4j
public class StatsService {
    private final String appName = "main-service";
    private final String address = "http://localhost:9090";
    private final StatServiceClient statServiceClient;


    @Autowired
    public StatsService(StatServiceClient statServiceClient) throws MalformedURLException {
        this.statServiceClient = statServiceClient;
    }

    public Map<Long, Long> getView(List<Event> events) {
        if ((events == null) || (events.isEmpty())) {
            return null;
        }


        ObjectMapper mapper = new ObjectMapper();
        Map<Long, Long> views = new HashMap<>();
        LocalDateTime start = getDateToStart(events);

        if (start == null) {
            return Map.of();
        }

        List<String> uris = events.stream()
                .map(id -> {
                    return "/events/" + id.getId();
                })
                .collect(Collectors.toList());

        ResponseEntity<Object> response = statServiceClient.getStats(start.format(EventMapper.formatter),
                LocalDateTime.now().plusMinutes(1).format(EventMapper.formatter),
                uris,
                true);
        try {
            StatDtoOut[] stats = mapper.readValue(mapper.writeValueAsString(response.getBody()), StatDtoOut[].class);
            for (StatDtoOut stat : stats) {
                views.put(Long.parseLong(stat.getUri().replaceAll("\\D+", "")), stat.getHits());
            }
        } catch (JsonProcessingException e) {
            throw new ValidationException("Ошибка при получении статистики");
        }
        return views;
    }


    public void addHit(HttpServletRequest request) {
        statServiceClient.postHit(StatDto.builder()
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI())
                .app(appName)
                .build());
    }

    public void addHit(HttpServletRequest request, Long id) {
        statServiceClient.postHit(StatDto.builder()
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .uri(request.getRequestURI() + "/" + id)
                .app(appName)
                .build());
    }

    private LocalDateTime getDateToStart(List<Event> events) {
        if ((events == null) || (events.isEmpty())) {
            return null;
        }
        LocalDateTime result = null;

        for (Event event : events) {
            if (result == null) {
                result = event.getCreatedOn();
            }
            if ((result.isAfter(event.getEventDate())) && (event.getCreatedOn() != null)) {
                result = event.getCreatedOn();
            }

        }

        return result;
    }
}
