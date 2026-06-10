package com.conciergerie.service;

import com.conciergerie.dto.UtilisateurDTO;
import com.conciergerie.entity.*;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.UtilisateurMapper;
import com.conciergerie.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UtilisateurService {
    private final UtilisateurRepository repository;
    private final UtilisateurMapper mapper;

    public List<UtilisateurDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public UtilisateurDTO findById(Long id){
        Utilisateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        return mapper.toDto(entity);
    }

    public UtilisateurDTO create(UtilisateurDTO dto, String motDePasse){
        Utilisateur entity = mapper.toEntity(dto);

        entity.setMotDePasse(motDePasse);
        entity.setActif(true);
        entity.setCreeLe(LocalDateTime.now());

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public UtilisateurDTO update (Long id, UtilisateurDTO dto){
        Utilisateur existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));

        Utilisateur entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setMotDePasse(existing.getMotDePasse());
        entity.setActif(dto.isActif());
        entity.setCreeLe(existing.getCreeLe());

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Utilisateur existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", id));
        repository.deleteById(id);
    }
}
