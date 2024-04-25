package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;
import ru.practicum.entity.Stat;
import ru.practicum.entity.StatMapper;
import ru.practicum.repository.StatsRepository;


import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StatServerServiceImpl implements StatServerService{
    private final StatsRepository repository;

    @Autowired
    public StatServerServiceImpl(StatsRepository repository) {
        this.repository = repository;
    }

    @Override
    public ResponseEntity<List<StatDtoOut>> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if ((start == null) || (end == null)) {
            throw new ValidationException("Промежуток времени не может быть пустым.");
        }
        if(start.isAfter(end)){
            throw new ValidationException("Начало промежутка не может быть позже его окончания.");
        }
        List<Stat> response;
        List<StatDtoOut> result;
        if ((uris == null) || (uris.isEmpty())){
            if (unique) {
                result = repository.findByTimestampBetweenAndUniqueIp(start, end);
            } else {
                response = repository.findByTimestampBetween(start, end);
                result = getStatDtoOut(response);
            }
        } else {
            if (unique) {
                result = repository.findByTimestampBetweenAndUrisAndUniqueIp(start, end, uris);
            } else {
                response = repository.findByTimestampBetweenAndUriIn(start, end, uris);
                result = getStatDtoOut(response);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }


    @Override
    @Transactional
    public ResponseEntity<StatDto> postStats(StatDto statDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(StatMapper.toStatDto(repository.save(StatMapper.toStat(statDto))));
    }

    private List<StatDtoOut> getStatDtoOut(List<Stat> response) {
        Map<StatDto, Integer> statsAndCount = new HashMap<>();
        for (Stat stat : response) {
            StatDto value = StatMapper.toStatDto(stat);
            if (statsAndCount.containsKey(value)) {
                statsAndCount.put(value, statsAndCount.get(value) + 1);
            } else {
                statsAndCount.put(value, 1);
            }
        }
        List<StatDtoOut> result = new ArrayList<>();
        for (Map.Entry<StatDto,Integer> statDto: statsAndCount.entrySet()){
            result.add(StatMapper.toStatDtoOut(statDto.getKey(), statDto.getValue()));
        }
        return result;
    }

}
