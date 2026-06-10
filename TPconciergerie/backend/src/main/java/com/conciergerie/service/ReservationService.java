package com.conciergerie.service;

import com.conciergerie.dto.ReservationDTO;
import com.conciergerie.entity.Client;
import com.conciergerie.entity.Reservation;
import com.conciergerie.entity.Ressource;
import com.conciergerie.exception.ResourceNotFoundException;
import com.conciergerie.mapper.ReservationMapper;
import com.conciergerie.repository.ClientRepository;
import com.conciergerie.repository.ReservationRepository;
import com.conciergerie.repository.RessourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {
    private final ReservationRepository repository;
    private final ReservationMapper mapper;
    private final ClientRepository clientRepository;
    private final RessourceRepository ressourceRepository;

    public List<ReservationDTO> findAll(){
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public ReservationDTO findById(Long id){
        Reservation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        return mapper.toDto(entity);
    }

    public ReservationDTO create(ReservationDTO dto){
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Ressource ressource = ressourceRepository.findById(dto.getRessourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Ressource", dto.getRessourceId()));

        Reservation entity = mapper.toEntity(dto);
        entity.setClient(client);
        entity.setRessource(ressource);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public ReservationDTO update(Long id, ReservationDTO dto){
        Reservation existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        Client client = clientRepository.findById(dto.getClientId())
                .orElseThrow(() -> new ResourceNotFoundException("Client", dto.getClientId()));
        Ressource ressource = ressourceRepository.findById(dto.getRessourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Ressource", dto.getRessourceId()));

        Reservation entity = mapper.toEntity(dto);
        entity.setId(id);
        entity.setClient(client);
        entity.setRessource(ressource);

        entity = repository.save(entity);
        return mapper.toDto(entity);
    }

    public void delete(Long id){
        Reservation existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation", id));
        repository.deleteById(id);
    }
}
