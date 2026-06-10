package com.conciergerie.service;

import com.conciergerie.dto.CategorieDTO;
import com.conciergerie.entity.Categorie;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.CategorieMapper;
import com.conciergerie.repository.CategorieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategorieService {
    private final CategorieRepository repository;
    private final CategorieMapper mapper;

    public List<CategorieDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public CategorieDTO findById(Long id){
        Categorie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", id));
        return mapper.toDto(entity);
    }

    public CategorieDTO create(CategorieDTO dto){
        Categorie entity = mapper.toEntity(dto);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public CategorieDTO update(Long id, CategorieDTO dto){
        Categorie existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", id));
        Categorie entity = mapper.toEntity(dto);
        entity.setId(id);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Categorie existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", id));
        repository.deleteById(id);
    }
}
