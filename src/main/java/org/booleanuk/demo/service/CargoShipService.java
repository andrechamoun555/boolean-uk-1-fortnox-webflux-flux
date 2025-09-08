package org.booleanuk.demo.service;

import org.booleanuk.demo.model.dto.CargoShipDto;
import org.booleanuk.demo.model.jpa.CargoShip;
import org.booleanuk.demo.model.repository.CargoShipRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class CargoShipService {
    private static final Logger log = LoggerFactory.getLogger(CargoShipService.class);

    private final CargoShipRepository cargoShipRepository;

    public CargoShipService(CargoShipRepository cargoShipRepository) {
        this.cargoShipRepository = cargoShipRepository;
    }

    public Mono<List<CargoShipDto>> getAll(){
        return Mono.fromCallable(() -> {
            List<CargoShip>  cargoShips = cargoShipRepository.findAll();
            List<CargoShipDto> dtos = new ArrayList<>();
            for (CargoShip cargoShip : cargoShips) {
                dtos.add(toDto(cargoShip));
            }

            return dtos;
        }).subscribeOn(Schedulers.boundedElastic());
    }


    public Mono<CargoShipDto> findById(int id) {
        return Mono.fromCallable(() -> {
            CargoShip cargoShip = cargoShipRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Cargo ship " + id + " not found"));
            return toDto(cargoShip);
        }).subscribeOn(Schedulers.boundedElastic());
    }


    public Mono<CargoShipDto> create(CargoShipDto request) {
        return Mono.fromCallable(() -> {
            CargoShip toSave = new CargoShip(request.name(),
                    request.location(), request.nation());
            CargoShip save = cargoShipRepository.save(toSave);
            return toDto(save);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<CargoShipDto> update(int id, CargoShipDto request) {
        return Mono.fromCallable(() -> {
            CargoShip toUpdate = cargoShipRepository.findById(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                                    "Cargo ship " + id + " not found"));
            toUpdate.setName(request.name());
            toUpdate.setLocation(request.location());
            toUpdate.setNation(request.nation());

            CargoShip saved = cargoShipRepository.save(toUpdate);
            return toDto(saved);
        }).subscribeOn(Schedulers.boundedElastic());

    }

    public Mono<CargoShipDto> delete(int id) {
        return Mono.fromCallable(() -> {
            CargoShip toDelete = cargoShipRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Cargo ship " + id + " not found"));
            cargoShipRepository.delete(toDelete);
            return toDto(toDelete);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public CargoShipDto toDto(CargoShip cargoShip) {
        return new CargoShipDto(
                cargoShip.getName(),
                cargoShip.getLocation(),
                cargoShip.getNation()
        );
    }

    public Flux<CargoShipDto> subscribe(int id) {
        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> findById(id))
                .flatMap(dto -> {
                    if (dto.location() == null || dto.location().isBlank()) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Cargo ship " + id + " has empty location"
                        ));
                    }
                    return Mono.just(dto);
                })
                .filter(dto -> !"uknown".equalsIgnoreCase(dto.nation()))
                .distinctUntilChanged(dto -> dto.location() + "|" + dto.nation())
                .subscribeOn(Schedulers.boundedElastic());
    }
}
