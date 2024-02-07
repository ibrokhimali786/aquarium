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

import static org.aquarium.enums.FishState.*;
import static org.aquarium.enums.GenderEnum.MALE;
import static org.aquarium.util.FishUtils.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Fish extends Thread {

    public static final List<Fish> FISH_LIST = Collections.synchronizedList(new ArrayList<>());
    private static final Lock lock = new ReentrantLock();
    private static final int THREAD_SLEEP_TIME = 1000;

    private static final Shark shark = new Shark();
    private static final Aquarium aquarium = new Aquarium();

    private GenderEnum gender;
    private Integer maxAge;

    private Integer birthAge;
    private FishState fishState;
    private Integer weddingAge;

    public boolean canKill(Shark shark) {
        return shark.getAge() <= shark.getKillerAge();
    }
    @SneakyThrows
    @Override
    public void run() {

        // Thread run bo'lganda fordagi har bir sikl baliqni
        // 1 yoshidan max age igacha yashashini taminlaydi
        for (int i = 1; i <= maxAge; i++) {

            this.birthAge = i;

            shark.setAge(shark.getAge() + 1);
            if (canKill(shark)) eatShark();

            Thread.sleep(THREAD_SLEEP_TIME);

            if (isChild(this)) changeState(this, CHILD);

            // Lock dan maqsad bir baliqni juftini topayotgan payt u qidirilayotgan juft
            // bir vaqtning o'zida boshqa juft tomonidan qidirilmasligini taminlaydi
            lock.lock();
            try {
                if (isMature(this) && !isMarried(this)) {

                    changeState(this, MATURE);

                    this.weddingAge = getWeddingAge();

                    Optional<Fish> optionalFish = getHusbandFish();

                    if (optionalFish.isPresent()) {

                        Fish fish = optionalFish.get();

                        changeState(this, MARRIED);
                        changeState(fish, MARRIED);

                        addBabyFish(this, fish);
                    }
                }
            } finally {
                lock.unlock();
            }

            if (isAdult(this)) changeState(this, ADULT);

            if (isTimeToDie(this)) toKill(this);
        }

    }

    private static void eatShark() {

        lock.lock();
        if (FISH_LIST.size() <= shark.getOneYearFishFoodCount())
            PrintUtils.noFish();
        else {
            Collections.shuffle(FISH_LIST);
            for (int j = 0; j < shark.getOneYearFishFoodCount(); j++)
                FISH_LIST.remove(j);
        }
        lock.unlock();
    }

    public static Wedding doWedding() {

        int maleCount = 0;
        int femaleCount = 0;

        GenderEnum gender;

        for (int i = 0; i < fishesCount(); i++) {

            gender = getGenderRandom();

            if (gender.equals(MALE)) maleCount++;
            else femaleCount++;

            if (FISH_LIST.size() == aquarium.getMaxFishesCount()) break;

            Fish fish = new Fish(gender, maxAge());
            FISH_LIST.add(fish);
            fish.start();
        }
        return new Wedding(maleCount, femaleCount);
    }

    public Optional<Fish> getHusbandFish() {

        for (Fish fish : FISH_LIST)
            if (Objects.equals(fish.getWeddingAge(), weddingAge) && this.gender != fish.getGender() && isMature(fish) && fish.getFishState() != FishState.MARRIED)
                return Optional.of(fish);

        return Optional.empty();
    }

    public static void startTheAquarium() {
        Wedding result = doWedding();
        PrintUtils.startPrint(result);
    }

    public void addBabyFish(Fish father, Fish mother) {
        Wedding result = doWedding();
        PrintUtils.printEvent(father, mother, result.maleCount(), result.femaleCount());
    }

    public Fish(GenderEnum gender, int maxAge) {
        this.fishState = FishState.BORN;
        this.birthAge = 0;
        this.gender = gender;
        this.maxAge = maxAge;
    }
}
