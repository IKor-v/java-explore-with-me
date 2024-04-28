package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.StatDtoOut;
import ru.practicum.entity.Stat;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Stat, Long> {

    List<Stat> findByTimestampAfter(LocalDateTime start);

    List<Stat> findByTimestampBetween(LocalDateTime start, LocalDateTime end);


    @Query("select new ru.practicum.StatDtoOut(s.app, s.uri, count(distinct s.ip) as hits) " +
            "from Stat as s " +
            "where s.timestamp between ?1 and ?2 " +
            "group by s.app, s.uri " +
            "order by count(distinct s.ip) desc")
    List<StatDtoOut> findByTimestampBetweenAndUniqueIp(LocalDateTime start, LocalDateTime end);

    List<Stat> findByTimestampBetweenAndUriIn(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("select new ru.practicum.StatDtoOut(s.app, s.uri, count(distinct s.ip) as hits) " +
            "from Stat as s " +
            "where s.timestamp between ?1 and ?2 " +
            "and s.uri in (?3)" +
            "group by s.app, s.uri " +
            "order by count(distinct s.ip) desc")
    List<StatDtoOut> findByTimestampBetweenAndUrisAndUniqueIp(LocalDateTime start, LocalDateTime end, List<String> uris);
}
