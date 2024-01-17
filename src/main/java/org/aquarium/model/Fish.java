package org.aquarium.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.util.FishUtils.*;
import static org.example.util.PrintUtils.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class Fish extends Thread {

    public static final List<Fish> fishList = Collections.synchronizedList(new ArrayList<>());
    private static final Lock lock = new ReentrantLock();
    private static final int TreedSleepTime = 1000;
    private Gender gender;
    private Integer maxAge;
    private Integer birthAge;
    private FishState fishState;
    private Integer happyDay;

    @SneakyThrows
    @Override
    public void run() {
        for (int i = 1; i <= maxAge; i++) {
            this.birthAge = i;
            Thread.sleep(TreedSleepTime);

            if (isChild(this)) {
                this.fishState = FishState.CHILD;
            }

            lock.lock();
            if (isMature(this) && !isMarried(this)) {
                this.fishState = FishState.MATURE;
                this.happyDay = happyDay();

                Optional<Fish> optionalFish = getHappyFish();
                if (optionalFish.isPresent()) {
                    Fish fish = optionalFish.get();

                    this.fishState = FishState.MARRIED;
                    fish.fishState = FishState.MARRIED;

                    addFish(this, fish);
                }
            }
            lock.unlock();

            if (isAdult(this)){
                this.fishState = FishState.ADULT;
            }

            if (isTimeToDie(this)) {
                this.fishState = FishState.DEAD;
                printDead(this);
                this.interrupt();
                fishList.remove(this);
            }
        }
    }

    public Fish(Gender gender, int maxAge) {
        this.fishState = FishState.BORN;
        this.birthAge = 0;
        this.gender = gender;
        this.maxAge = maxAge;
    }



    public  Optional<Fish> getHappyFish() {

//        return fishList.stream()
//                .filter(fish -> fish.getHappyDay() == happyDay && this.gender != fish.gender && isMature(fish) && fish.fishState != FishState.MARRIED)
//                .findFirst();

        for (int i = 0; i < fishList.size(); i++) {
            if (Objects.equals(fishList.get(i).getHappyDay(), happyDay) && this.gender!=fishList.get(i).getGender()
               && isMature(fishList.get(i)) && fishList.get(i).getFishState()!=FishState.MARRIED){
               return Optional.of(fishList.get(i));
            }
        }
         return Optional.empty();
    }



    public static void addFish() {
        int maleCount = fishesCount();
        int femaleCount = fishesCount();

        for (int i = 0; i < maleCount; i++) {
            Fish fish = new Fish(Gender.MALE, maxAge());
            fishList.add(fish);
            fish.start();
        }

        for (int i = 0; i < femaleCount; i++) {
            Fish fish = new Fish(Gender.FEMALE, maxAge());
            fishList.add(fish);
            fish.start();
        }
        System.out.printf("boshida akvariumda %d ta ogil va %d ta qiz bor \n",maleCount,femaleCount);
    }


    public void addFish(Fish father, Fish mother) {
        int maleCount = fishesCount();
        int femaleCount = fishesCount();

        for (int i = 0; i < maleCount; i++) {
            Fish fish = new Fish(Gender.MALE, maxAge());
            fishList.add(fish);
            fish.start();
        }

        for (int i = 0; i < femaleCount; i++) {
            Fish fish = new Fish(Gender.FEMALE, maxAge());
            fishList.add(fish);
            fish.start();
        }
        printMarriage(father, mother, maleCount,femaleCount);
    }
}
