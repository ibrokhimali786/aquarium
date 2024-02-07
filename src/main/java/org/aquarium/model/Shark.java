package org.aquarium.model;

import lombok.Data;

@Data
public class Shark {
    private Integer age;
    private final Integer killerAge = 4;
    private final Integer oneYearFishFoodCount = 10;

    public Shark() {
        this.age = 0;
    }
}
