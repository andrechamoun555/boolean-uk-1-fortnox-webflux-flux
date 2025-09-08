package org.booleanuk.demo.controller;

import org.booleanuk.demo.model.dto.CargoShipDto;
import org.booleanuk.demo.service.CargoShipService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("api/ships")
public class CargoShipController {

    private final CargoShipService service;

    public CargoShipController(CargoShipService service) {
        this.service = service;
    }

    @GetMapping
    public Mono<List<CargoShipDto>> getAll() {
        return service.getAll();
    }

    @GetMapping("{id}")
    public Mono<CargoShipDto> findById(@PathVariable int id) {
        return service.findById(id);
    }

    @PostMapping
    public Mono<CargoShipDto> create(@RequestBody CargoShipDto dto) {
        return service.create(dto);
    }

    @PutMapping("{id}")
    public Mono<CargoShipDto> update(@PathVariable int id, @RequestBody CargoShipDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("{id}")
    public Mono<CargoShipDto> delete(@PathVariable int id) {
        return service.delete(id);
    }

    @GetMapping(value = "{id}/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CargoShipDto> subscribe(@PathVariable int id) {
        return service.subscribe(id);
    }
}
