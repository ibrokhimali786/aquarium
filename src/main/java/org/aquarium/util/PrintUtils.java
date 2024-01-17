package org.aquarium.util;

import lombok.experimental.UtilityClass;
import org.aquarium.enums.GenderEnum;
import org.aquarium.model.Fish;

@UtilityClass
public class PrintUtils {
    public void printEvent(Fish male, Fish female, int maleChild, int femaleChild) {
        System.out.printf(
                "Ismi \"%s\" bo'lgan, %d yoshli %s baliq bilan ismi \"%s\" bo'lgan, %d yoshli %s baliq turmush qurushdi " +
                        "      va ular %d ta og'il va %d ta qiz farzand ko'rishdi %n", male.getName(), male.getBirthAge(), "erkak", female.getName(), female.getBirthAge(), "urg'ochi", maleChild, femaleChild);

    }

    public void printDead(Fish fish) {
        System.out.printf("\"%s\" ismili baliq %d yoshida vafot etdi \n", fish.getName(), fish.getMaxAge());
    }

}
