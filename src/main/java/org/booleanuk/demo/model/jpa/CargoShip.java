package org.booleanuk.demo.model.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name ="cargos")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CargoShip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private String nation;

    public CargoShip(String name, String location, String nation) {
        this.name = name;
        this.location = location;
        this.nation = nation;
    }

}
