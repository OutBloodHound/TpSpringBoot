package com.conciergerie.service;

import com.conciergerie.dto.RessourceDTO;
import com.conciergerie.entity.Ressource;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.RessourceMapper;
import com.conciergerie.repository.RessourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RessourceService {
    private final RessourceRepository repository;
    private final RessourceMapper mapper;

    public List<RessourceDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public RessourceDTO findById(Long id){
        Ressource entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource", id));
        return mapper.toDto(entity);
    }

    public RessourceDTO create(RessourceDTO dto){
        Ressource entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public RessourceDTO update(Long id, RessourceDTO dto){
        Ressource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource", id));
        Ressource entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Ressource existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ressource", id));
        repository.deleteById(id);
    }
}
