package ru.practicum.compilations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilations.dto.CompilationDtoOut;
import ru.practicum.compilations.dto.CompilationMapper;
import ru.practicum.compilations.entity.Compilation;
import ru.practicum.compilations.repository.CompilationsRepository;
import ru.practicum.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class CompilationsServiceImpl implements CompilationsService {
    private final CompilationsRepository compilationsRepository;

    @Autowired
    public CompilationsServiceImpl(CompilationsRepository compilationsRepository) {
        this.compilationsRepository = compilationsRepository;
    }

    @Override
    public List<CompilationDtoOut> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> listCompilation;
        Pageable pageable = PageRequest.of(from / size, size);
        if (pinned == null) {
            listCompilation = compilationsRepository.findAll(pageable).toList();
        } else {
            listCompilation = compilationsRepository.findByPinned(pinned, pageable);
        }

        return listCompilation.stream().map(CompilationMapper::toCompilationDtoOut).collect(Collectors.toList());
    }

    @Override
    public CompilationDtoOut getCompilationById(Long comId) {
        Compilation compilation = compilationsRepository.findById(comId).orElseThrow(() -> new ValidationException("Подборка не найдена или недоступна"));
        return CompilationMapper.toCompilationDtoOut(compilation);
    }
}
