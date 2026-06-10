package com.conciergerie.service;

import com.conciergerie.dto.ClientDTO;
import com.conciergerie.entity.Client;
import com.conciergerie.entity.Utilisateur;
import com.conciergerie.mapper.ClientMapper;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.repository.UtilisateurRepository;
import com.conciergerie.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class ClientService {
    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final UtilisateurRepository utilisateurRepository;

    public List<ClientDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ClientDTO findById (Long id){
        Client entity = repository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Client", id));
        return mapper.toDto(entity);
    }

    public ClientDTO create(ClientDTO dto){
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));
        Client entity = mapper.toEntity(dto);

        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public ClientDTO update (Long id, ClientDTO dto){
        Client existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        Utilisateur utilisateur = utilisateurRepository.findById(dto.getUtilisateurId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", dto.getUtilisateurId()));
        Client entity = mapper.toEntity(dto);

        entity.setId(id);
        entity.setUtilisateur(utilisateur);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Client existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client", id));
        repository.deleteById(id);
    }
}
