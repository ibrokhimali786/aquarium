package org.aquarium.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.aquarium.enums.FishState;
import org.aquarium.enums.GenderEnum;
import org.aquarium.util.PrintUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.aquarium.util.FishUtils.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Fish extends Thread {

    public static final List<Fish> FISH_LIST = Collections.synchronizedList(new ArrayList<>());
    private static final Lock lock = new ReentrantLock();
    private static final int THREAD_SLEEP_TIME = 1000;


    private GenderEnum gender;
    private Integer maxAge;
    private Integer birthAge;
    private FishState fishState;
    private Integer weddingAge;

    @SneakyThrows
    @Override
    public void run() {

        for (int i = 1; i <= maxAge; i++) {

            this.birthAge = i;

            Thread.sleep(THREAD_SLEEP_TIME);

            if (isChild(this)) {
                this.fishState = FishState.CHILD;
            }

            lock.lock();
            try {
                if (isMature(this) && !isMarried(this)) {

                    this.fishState = FishState.MATURE;
                    this.weddingAge = getWeddingAge();

                    Optional<Fish> optionalFish = getHusbandFish();
                    if (optionalFish.isPresent()) {
                        Fish fish = optionalFish.get();

                        this.fishState = FishState.MARRIED;
                        fish.fishState = FishState.MARRIED;

                        addBabyFish(this, fish);
                    }
                }
            } catch (Exception ignored) {
            } finally {
                lock.unlock();
            }

            if (isAdult(this)) {
                this.fishState = FishState.ADULT;
            }

            if (isTimeToDie(this)) {
                this.fishState = FishState.DEAD;
                PrintUtils.printDead(this);
                this.interrupt();
                FISH_LIST.remove(this);
            }
        }
    }

    public Optional<Fish> getHusbandFish() {
        for (Fish fish : FISH_LIST) {
            if (Objects.equals(fish.getWeddingAge(), weddingAge) && this.gender != fish.getGender() && isMature(fish) && fish.getFishState() != FishState.MARRIED) {
                return Optional.of(fish);
            }
        }
        return Optional.empty();
    }

    public static void startTheAquarium() {
        Result result = getResult();
        System.out.printf("Tom akvariumga %d ta erkak va %d ta urg'ochi baliq tashladi \n", result.maleCount(), result.femaleCount());
    }

    public void addBabyFish(Fish father, Fish mother) {
        Result result = getResult();
        PrintUtils.printEvent(father, mother, result.maleCount(), result.femaleCount());
    }

    public Fish(GenderEnum gender, int maxAge) {
        this.fishState = FishState.BORN;
        this.birthAge = 0;
        this.gender = gender;
        this.maxAge = maxAge;
    }
}
