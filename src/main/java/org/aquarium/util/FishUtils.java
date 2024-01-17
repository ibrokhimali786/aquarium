package org.aquarium.util;

import lombok.experimental.UtilityClass;
import org.example.model.Fish;
import org.example.model.FishState;
import org.example.model.Gender;

import java.util.Random;

@UtilityClass
public class FishUtils {
    private final Random ran = new Random();
    private static final int OGILBOLA_BALIQ_KOTTA_YOSHI = 10;
    private static final int QIZBOLA_BALIQ_KOTTA_YOSHI = 10;
    private static final int FISH_MAX_AGE = 20;

    public static int maxAge() {
        return ran.nextInt(FISH_MAX_AGE - 10, FISH_MAX_AGE);
    }

    public static boolean isMature(Fish fish) {
        if (isMale(fish)) {
            return fish.getBirthAge() >= OGILBOLA_BALIQ_KOTTA_YOSHI && fish.getBirthAge() <= OGILBOLA_BALIQ_KOTTA_YOSHI + 5;
        }else {
            return fish.getBirthAge() >= QIZBOLA_BALIQ_KOTTA_YOSHI && fish.getBirthAge() <= QIZBOLA_BALIQ_KOTTA_YOSHI + 5;
        }
    }

    public static boolean isMarried(Fish fish) {
        return fish.getFishState() == FishState.MARRIED;
    }
    public static boolean isAdult(Fish fish) {
        if (isMale(fish)) {
            return fish.getBirthAge() > OGILBOLA_BALIQ_KOTTA_YOSHI + 5 && fish.getBirthAge() != fish.getMaxAge();
        }else {
            return fish.getBirthAge() > QIZBOLA_BALIQ_KOTTA_YOSHI + 5 && fish.getBirthAge() != fish.getMaxAge();
        }
    }

    public static boolean isTimeToDie(Fish fish){
        return fish.getBirthAge() == fish.getMaxAge();
    }


    public static int fishesCount() {
        return ran.nextInt(1, 6);
    }

    public static int happyDay() {
        return ran.nextInt(2, 4);
    }

    public static boolean isChild(Fish fish){
        if (isMale(fish)){
            return fish.getBirthAge() < OGILBOLA_BALIQ_KOTTA_YOSHI;
        }else {
            return fish.getBirthAge() < QIZBOLA_BALIQ_KOTTA_YOSHI;
        }
    }

    private static boolean isMale(Fish fish){
        return fish.getGender() == Gender.MALE;
    }
}
