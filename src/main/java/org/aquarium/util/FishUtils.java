package org.aquarium.util;

import lombok.experimental.UtilityClass;
import org.aquarium.enums.GenderEnum;
import org.aquarium.model.Fish;
import org.aquarium.enums.FishState;

import java.util.Objects;
import java.util.Random;

import static org.aquarium.enums.GenderEnum.MALE;
import static org.aquarium.model.Fish.FISH_LIST;

@UtilityClass
public class FishUtils {
    private final Random ran = new Random();
    private final int MALE_FISH_MAX_AGE = 10;
    private final int FEMALE_FISH_MAX_AGE = 10;
    private final int FISH_MAX_AGE = 20;

    public int maxAge() {
        return ran.nextInt(FISH_MAX_AGE - 10, FISH_MAX_AGE);
    }

    public boolean isMature(Fish fish) {
        if (isMale(fish)) {
            return fish.getBirthAge() >= MALE_FISH_MAX_AGE && fish.getBirthAge() <= MALE_FISH_MAX_AGE + 5;
        } else {
            return fish.getBirthAge() >= FEMALE_FISH_MAX_AGE && fish.getBirthAge() <= FEMALE_FISH_MAX_AGE + 5;
        }
    }

    public boolean isMarried(Fish fish) {
        return fish.getFishState() == FishState.MARRIED;
    }

    public boolean isAdult(Fish fish) {
        if (isMale(fish)) {
            return fish.getBirthAge() > MALE_FISH_MAX_AGE + 5 && !fish.getBirthAge().equals(fish.getMaxAge());
        } else {
            return fish.getBirthAge() > FEMALE_FISH_MAX_AGE + 5 && !fish.getBirthAge().equals(fish.getMaxAge());
        }
    }

    public void changeState(Fish fish, FishState fishState) {
        fish.setFishState(fishState);
    }

    public boolean isTimeToDie(Fish fish) {
        return Objects.equals(fish.getBirthAge(), fish.getMaxAge());
    }

    public void toKill(Fish fish) {
        fish.setFishState(FishState.DEAD);
        PrintUtils.printDead(fish);
        fish.interrupt();
        FISH_LIST.remove(fish);
    }

    public int fishesCount() {
        return ran.nextInt(5, 10);
    }

    public int getWeddingAge() {
        return ran.nextInt(2, 4);
    }

    public boolean isChild(Fish fish) {
        if (!isMale(fish)) return fish.getBirthAge() < FEMALE_FISH_MAX_AGE;
        return fish.getBirthAge() < MALE_FISH_MAX_AGE;
    }

    private boolean isMale(Fish fish) {
        return fish.getGender() == MALE;
    }

    public static GenderEnum getGenderRandom() {
        return ran.nextBoolean() ? GenderEnum.MALE : GenderEnum.FEMALE;
    }
}
