package ru.practicum.comments.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.comments.dto.CommentDtoCount;
import ru.practicum.comments.entity.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEventId(Long eventId);

    List<Comment> findByEventId(Long eventId, Pageable pageable);

    List<Comment> findByCreatorId(Long userId, Pageable pageable);

    List<Comment> findByCreatorIdAndCreatedOnBefore(Long userId, LocalDateTime end, Pageable pageable);

    List<Comment> findByCreatorIdAndCreatedOnBetween(Long userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select c " +
            "from Comment as c " +
            "where ((?1 IS NULL) or (c.event.id in ?1)) " +
            "and ((?2 IS NULL) or (c.creator.id in ?2)) " +
            "and ((?3 IS NULL) or (c.createdOn >= ?3)) " +
            "and ((?4 IS NULL) or (c.createdOn <= ?4))")
    List<Comment> getCommentsWithParam(List<Long> eventId, List<Long> userId, LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("Select new ru.practicum.comments.dto.CommentDtoCount(c.event.id, count(c)) " +
            "from Comment as c " +
            "group by c.event.id")
    List<CommentDtoCount> countGroupByEventId();

}
