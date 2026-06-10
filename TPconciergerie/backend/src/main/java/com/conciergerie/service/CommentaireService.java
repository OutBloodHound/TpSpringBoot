package com.conciergerie.service;

import com.conciergerie.dto.CommentaireDTO;
import com.conciergerie.entity.Commentaire;
import com.conciergerie.entity.Demande;
import com.conciergerie.entity.Utilisateur;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.CommentaireMapper;
import com.conciergerie.repository.CommentaireRepository;
import com.conciergerie.repository.DemandeRepository;
import com.conciergerie.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentaireService {
    private final CommentaireRepository repository;
    private final CommentaireMapper mapper;
    private final DemandeRepository demandeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public List<CommentaireDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public CommentaireDTO findById(Long id){
        Commentaire entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire", id));
        return mapper.toDto(entity);
    }

    public CommentaireDTO create(CommentaireDTO dto){
        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande", dto.getDemandeId()));
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));

        Commentaire entity = mapper.toEntity(dto);
        entity.setDemande(demande);
        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public CommentaireDTO update(Long id, CommentaireDTO dto){
        Commentaire existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire", id));
        Demande demande = demandeRepository.findById(dto.getDemandeId())
                .orElseThrow(() -> new ResourceNotFoundException("Demande", dto.getDemandeId()));
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));

        Commentaire entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setDemande(demande);
        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Commentaire existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commentaire", id));
        repository.deleteById(id);
    }
}
