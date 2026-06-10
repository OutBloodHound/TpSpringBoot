package com.conciergerie.service;

import com.conciergerie.dto.PieceJointeDTO;
import com.conciergerie.entity.Demande;
import com.conciergerie.entity.PieceJointe;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.PieceJointeMapper;
import com.conciergerie.repository.DemandeRepository;
import com.conciergerie.repository.PieceJointeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PieceJointeService {
    private final PieceJointeRepository repository;
    private final PieceJointeMapper mapper;
    private final DemandeRepository demandeRepository;

    public List<PieceJointeDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PieceJointeDTO findById(Long id){
        PieceJointe entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PieceJointe", id));
        return mapper.toDto(entity);
    }

    public PieceJointeDTO create(PieceJointeDTO dto){
        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande", dto.getDemandeId()));

        PieceJointe entity = mapper.toEntity(dto);
        entity.setDemande(demande);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public PieceJointeDTO update(Long id, PieceJointeDTO dto){
        PieceJointe existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PieceJointe", id));
        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande", dto.getDemandeId()));

        PieceJointe entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setDemande(demande);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        PieceJointe existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PieceJointe", id));
        repository.deleteById(id);
    }
}
