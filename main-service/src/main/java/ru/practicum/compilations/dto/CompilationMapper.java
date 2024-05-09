package ru.practicum.compilations.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.compilations.entity.Compilation;
import ru.practicum.events.dto.EventMapper;
import ru.practicum.events.entity.Event;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public CompilationDtoOut toCompilationDtoOut(Compilation compilation) {
        return CompilationDtoOut.builder()
                .id(compilation.getId())
                .title(compilation.getTitle())
                .pinned(compilation.getPinned())
                .events(new HashSet<>(compilation.getEvents().stream().map(EventMapper::toEventDto).collect(Collectors.toList())))
                .build();
    }


    public Compilation toCompilation(CompilationDtoIn compilationDtoIn, List<Event> events) {
        return Compilation.builder()
                .title(compilationDtoIn.getTitle())
                .pinned(compilationDtoIn.getPinned())
                .events(new HashSet<>(events))
                .build();
    }

    public Compilation toCompilation(Long id, CompilationDtoIn compilationDtoIn, List<Event> events) {
        return Compilation.builder()
                .id(id)
                .title(compilationDtoIn.getTitle())
                .pinned(compilationDtoIn.getPinned())
                .events(new HashSet<>(events))
                .build();
    }

    public Compilation updateCompilation(Compilation compilation, CompilationDtoIn compilationDtoIn) {
        if (compilationDtoIn.getPinned() != null) {
            compilation.setPinned(compilationDtoIn.getPinned());
        }
        if (compilationDtoIn.getTitle() != null) {
            compilation.setTitle(compilationDtoIn.getTitle());
        }
        if (compilationDtoIn.getEvents() != null) {  //Это законно?

/*            Set<Event> events = new HashSet<>();
            for (Long idEvent :compilationDtoIn.getEvents()){
                events.add(Event.builder().id(idEvent).build());
            }
            compilation.setEvents(events);*/
        }
        return compilation;
    }

    public Compilation updateCompilation(Compilation compilation, CompilationDtoIn compilationDtoIn, Set<Event> events) {
        if (compilationDtoIn.getPinned() != null) {
            compilation.setPinned(compilationDtoIn.getPinned());
        }
        if (compilationDtoIn.getTitle() != null) {
            compilation.setTitle(compilationDtoIn.getTitle());
        }
        if (compilationDtoIn.getEvents() != null) {
            compilation.setEvents(events);
        }
        return compilation;
    }
}
