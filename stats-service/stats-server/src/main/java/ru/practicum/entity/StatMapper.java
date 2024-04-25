package ru.practicum.entity;

import lombok.experimental.UtilityClass;
import ru.practicum.StatDto;
import ru.practicum.StatDtoOut;

@UtilityClass
public class StatMapper {
    public Stat toStat(StatDto statDto){
        return Stat.builder()
                .app(statDto.getApp())
                .uri(statDto.getUri())
                .ip(statDto.getIp())
                .timestamp(statDto.getTimestamp())
                .build();
    }

    public StatDtoOut toStatDtoOut(Stat stat, int hits){
        return StatDtoOut.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(hits)
                .build();
    }

    public StatDtoOut toStatDtoOut(StatDto statDto, int hits){
        return StatDtoOut.builder()
                .app(statDto.getApp())
                .uri(statDto.getUri())
                .hits(hits)
                .build();
    }

    public StatDto toStatDto(Stat stat){
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .ip(stat.getIp())
                .timestamp(stat.getTimestamp())
                .build();
    }
}
