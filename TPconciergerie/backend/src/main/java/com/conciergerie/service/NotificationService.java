package com.conciergerie.service;

import com.conciergerie.dto.NotificationDTO;
import com.conciergerie.entity.Notification;
import com.conciergerie.entity.Utilisateur;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.NotificationMapper;
import com.conciergerie.repository.NotificationRepository;
import com.conciergerie.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {
    private final NotificationRepository repository;
    private final NotificationMapper mapper;
    private final UtilisateurRepository utilisateurRepository;

    public List<NotificationDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public NotificationDTO findById(Long id){
        Notification entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        return mapper.toDto(entity);
    }

    public NotificationDTO create(NotificationDTO dto){
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));

        Notification entity = mapper.toEntity(dto);
        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public NotificationDTO update(Long id, NotificationDTO dto){
        Notification existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));

        Notification entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setUtilisateur(utilisateur);
        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Notification existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", id));
        repository.deleteById(id);
    }
}
