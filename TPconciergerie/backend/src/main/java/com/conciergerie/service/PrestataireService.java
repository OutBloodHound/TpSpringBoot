package com.conciergerie.service;

import com.conciergerie.dto.PrestataireDTO;
import com.conciergerie.entity.Prestataire;
import com.conciergerie.entity.Utilisateur;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.PrestataireMapper;
import com.conciergerie.repository.PrestataireRepository;
import com.conciergerie.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrestataireService {
    private final PrestataireRepository repository;
    private final PrestataireMapper mapper;
    private final UtilisateurRepository utilisateurRepository;

    public List<PrestataireDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public PrestataireDTO findById (Long id){
        Prestataire entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", id));
        return mapper.toDto(entity);
    }

    public PrestataireDTO create(PrestataireDTO dto){
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));

        Prestataire entity = mapper.toEntity(dto);

        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public PrestataireDTO update(Long id, PrestataireDTO dto){
        Prestataire existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", id));
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));

        Prestataire entity = mapper.toEntity(dto);

        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Prestataire existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prestataire", id));
        repository.deleteById(id);
    }
}
