package com.conciergerie.service;


import com.conciergerie.dto.DemandeDTO;
import com.conciergerie.entity.*;
import com.conciergerie.repository.CategorieRepository;
import com.conciergerie.repository.ClientRepository;
import com.conciergerie.mapper.DemandeMapper;
import com.conciergerie.repository.DemandeRepository;
import com.conciergerie.repository.PrestataireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.conciergerie.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DemandeService {
    private final DemandeRepository repository;
    private final DemandeMapper mapper;
    private final ClientRepository clientRepository;
    private final CategorieRepository categorieRepository;
    private final PrestataireRepository prestataireRepository;

    public List<DemandeDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public DemandeDTO findById(Long id) {
        Demande entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande", id));
        return mapper.toDto(entity);
    }

    public DemandeDTO create(DemandeDTO dto){
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", dto.getCategorieId()));

        Demande entity = mapper.toEntity(dto);

        entity.setClient(client);
        entity.setCategorie(categorie);
        entity.setStatut(Statut.EN_ATTENTE);
        entity.setCreeLe(LocalDateTime.now());

        if (dto.getPrestataireId() != null){
            Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestataire", dto.getPrestataireId()));
            entity.setPrestataire(prestataire);
        }

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public DemandeDTO update (Long id, DemandeDTO dto){
        Demande existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande", id));
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Categorie categorie = categorieRepository.findById(dto.getCategorieId())
                .orElseThrow(() -> new ResourceNotFoundException("Categorie", dto.getCategorieId()));

        Demande entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setClient(client);
        entity.setCategorie(categorie);
        entity.setCreeLe(existing.getCreeLe());

        if (dto.getPrestataireId() != null) {
            Prestataire prestataire = prestataireRepository.findById(dto.getPrestataireId())
                    .orElseThrow(() -> new ResourceNotFoundException("Prestataire", dto.getPrestataireId()));
            entity.setPrestataire(prestataire);
        }
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Demande existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Demande", id));

        repository.deleteById(id);
    }
}
