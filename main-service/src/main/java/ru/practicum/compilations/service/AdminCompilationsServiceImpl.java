package ru.practicum.compilations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDtoIn;
import ru.practicum.compilations.dto.CompilationDtoOut;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.entity.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.events.entity.Event;
import ru.practicum.events.repository.EventsRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class AdminCompilationsServiceImpl implements AdminCompilationsService {
    private final CompilationsRepository compilationsRepository;
    private final EventsRepository eventsRepository;

    @Autowired
    public AdminCompilationsServiceImpl(CompilationsRepository compilationsRepository, EventsRepository eventsRepository) {
        this.compilationsRepository = compilationsRepository;
        this.eventsRepository = eventsRepository;
    }

    @Override
    @Transactional
    public CompilationDtoOut postNewCompilation(CompilationDtoIn compilationDtoIn) {
        validation(compilationDtoIn);
        if (compilationDtoIn.getPinned() == null) {
            compilationDtoIn.setPinned(false);
        }
        if (compilationDtoIn.getEvents() == null) {
            compilationDtoIn.setEvents(new HashSet<>());
        }

        List<Event> eventsInCompilation = eventsRepository.findByIdIn(compilationDtoIn.getEvents());

        if (((eventsInCompilation == null) || eventsInCompilation.isEmpty()) && compilationDtoIn.getEvents().size() != eventsInCompilation.size()) {
            throw new NotFoundException("События, указанные в подборке, не найденны.");
        }
        try {
            return CompilationMapper.toCompilationDtoOut(compilationsRepository.save(CompilationMapper.toCompilation(compilationDtoIn, eventsInCompilation)));
        } catch (RuntimeException e) {
            throw new ConflictException("Ошибка при сохранении подборки: попробуйте другое название.");
        }

    }


    @Override
    @Transactional
    public CompilationDtoOut patchCompilation(CompilationDtoIn compilation, Long comId) {
        validation(compilation);
        Set<Event> eventsInCompilation;
        Compilation oldCompilation = compilationsRepository.findById(comId).orElseThrow(() -> new ValidationException("Не найденно такой подборки с id = " + comId));
        if (compilation.getEvents() != null) {
            eventsInCompilation = new HashSet<>(eventsRepository.findByIdIn(compilation.getEvents()));
        } else {
            eventsInCompilation = oldCompilation.getEvents();
        }
        return CompilationMapper.toCompilationDtoOut(compilationsRepository.save(CompilationMapper.updateCompilation(oldCompilation, compilation, eventsInCompilation)));
    }

    @Override
    @Transactional
    public void delCompilation(Long comId) {
        compilationsRepository.deleteById(comId);
    }

    private void validation(CompilationDtoIn compilationDtoIn) {
        if (compilationDtoIn == null) {
            throw new ValidationException("Передано пустое тело подборки.");
        }
    }
}


