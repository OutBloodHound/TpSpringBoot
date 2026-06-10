package com.conciergerie.service;

import com.conciergerie.dto.NotationDTO;
import com.conciergerie.entity.Demande;
import com.conciergerie.entity.Notation;
import com.conciergerie.entity.Prestataire;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.NotationMapper;
import com.conciergerie.repository.DemandeRepository;
import com.conciergerie.repository.NotationRepository;
import com.conciergerie.repository.PrestataireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotationService {
    private final NotationRepository repository;
    private final NotationMapper mapper;
    private final DemandeRepository demandeRepository;
    private final PrestataireRepository prestataireRepository;

    public List<NotationDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public NotationDTO findById(Long id){
        Notation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notation", id));
        return mapper.toDto(entity);
    }

    public NotationDTO create(NotationDTO dto){
        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande", dto.getDemandeId()));
        Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", dto.getPrestataireId()));

        Notation entity = mapper.toEntity(dto);
        entity.setDemande(demande);
        entity.setPrestataire(prestataire);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public NotationDTO update(Long id, NotationDTO dto){
        Notation existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notation", id));
        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande", dto.getDemandeId()));
        Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", dto.getPrestataireId()));

        Notation entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setDemande(demande);
        entity.setPrestataire(prestataire);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Notation existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notation", id));
        repository.deleteById(id);
    }
}
