package org.booleanuk.demo.model.repository;

import org.booleanuk.demo.model.jpa.CargoShip;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoShipRepository extends JpaRepository<CargoShip, Integer> {
}
