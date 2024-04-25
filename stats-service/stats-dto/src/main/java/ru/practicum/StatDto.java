package ru.practicum;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatDto {
    @NotBlank
    private String app;
    @NotBlank
    private String uri;
    private String ip;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatDto statDto = (StatDto) o;
        return Objects.equals(app, statDto.app) && Objects.equals(uri, statDto.uri) && Objects.equals(ip, statDto.ip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, ip);
    }
}
