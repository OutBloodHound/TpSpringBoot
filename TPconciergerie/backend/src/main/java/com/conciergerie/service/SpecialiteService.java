package com.conciergerie.service;

import com.conciergerie.dto.SpecialiteDTO;
import com.conciergerie.entity.Specialite;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.SpecialiteMapper;
import com.conciergerie.repository.SpecialiteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SpecialiteService {
    private final SpecialiteRepository repository;
    private final SpecialiteMapper mapper;

    public List<SpecialiteDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public SpecialiteDTO findById(Long id){
        Specialite entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", id));
        return mapper.toDto(entity);
    }

    public SpecialiteDTO create(SpecialiteDTO dto){
        Specialite entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public SpecialiteDTO update(Long id, SpecialiteDTO dto){
        Specialite existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", id));
        Specialite entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Specialite existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Specialite", id));
        repository.deleteById(id);
    }
}
