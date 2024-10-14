package ryleh.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

import ryleh.controller.core.GameEngine;
/**
 * A class that determines which entities are going to be spawned.
 */
public final class LevelDesigner {
    /** Above this integer, difficulty range indicates "hard" as difficulty for this level.*/
    private static final int HARD_MODE = 50;
    /** Above this integer, difficulty range indicates "medium" as difficulty for this level.*/
    private static final int MEDIUM_MODE = 50;
    /** Above this integer, difficulty range indicates "easy" as difficulty for this level.*/
    private static final int EASY_MODE = 20;
    /**
     * Value used to calculate type of enemies to spawn.
     */
    private static final int TROUBLE_INCREASER = 2;
    /**
     * Value used to calculate type of enemies to spawn.
     */
    private static final int TROUBLE_DECREASER = 15;
    private int levelNum;
    private final List<Type> entities;
    private final Random random;
    private final Function<Integer, Integer> difficultyFunction;
    public LevelDesigner() {
        levelNum = 0;
        entities = new ArrayList<>();
        random = new Random();
        /** This function represents how difficulty raises along with the amount of levels explored.*/
        difficultyFunction = (x) -> (int) (200 * Math.pow(Math.E, -(double) (x) / 7) - 80);
    }

    public List<Type> generateLevelEntities() {
        levelNum++;
        if (levelNum == 1) {
            this.entities.clear();
        } else {
            this.generateObstacles();
            this.generateItems();
            this.generateEnemies();
        }
        return entities;
    }

    /**
     * This method is used to determine a range that represents the difficulty of
     * the current level.
     * 
     * @param enemyNumber
     * @return the value of a function with enemy number as input.
     */
    private int getDifficultyRange(final int enemyNumber) {
        return difficultyFunction.apply(enemyNumber);
    }

    /**
     * This method generates a list of Entity type, that represents the entities
     * inside current level.
     */
    private void generateEnemies() {
        final int enemyNumber = (int) (random.nextGaussian() + 3);
        int difficultyRange = this.getDifficultyRange(enemyNumber);
        difficultyRange = difficultyRange / TROUBLE_DECREASER + levelNum * TROUBLE_INCREASER;
        final int difficulty = (int) (random.nextGaussian() + difficultyRange);

        for (int i = 0; i < enemyNumber; i++) {
            if (difficulty >= 0 && difficulty <= EASY_MODE) {
                if (random.nextInt(2) == 0) {
                    entities.add(Type.ENEMY_DRUNK);
                } else {
                    entities.add(Type.ENEMY_LURKER);
                }
            } else if (difficulty > EASY_MODE && difficulty <= MEDIUM_MODE) {
                if (random.nextInt(2) == 0) {
                    entities.add(Type.ENEMY_SHOOTER);
                } else {
                    entities.add(Type.ENEMY_SPINNER);
                }
            } else if (difficulty > HARD_MODE) {
                if (random.nextInt(2) == 0) {
                    entities.add(Type.ENEMY_DRUNKSPINNER);
                } else {
                    entities.add(Type.ENEMY_DRUNKSPINNER);
                }
            }
        }
        GameEngine.runDebugger(() -> System.out.println(entities));
    }

    /**
     * This method add "ITEM" to the entity list of the current level. It does so
     * only every three levels.
     */
    private void generateItems() {
        if (levelNum % 3 == 0) {
            entities.add(Type.ITEM);
        }
    }

    /**
     * Generates random obstacles choosing between "ROCK" type and "FIRE" type.
     */
    private void generateObstacles() {
        final int obstacleNum = (int) random.nextGaussian() + 2;
        for (int i = 0; i < obstacleNum; i++) {
            if (random.nextInt(2) == 0) {
                entities.add(Type.FIRE);
            } else {
                entities.add(Type.ROCK);
            }
        }
    }

    /**
     * Clears the entity list of the current level.
     */
    public void clearLevel() {
        this.entities.clear();
    }

    /**
     * Sets current level to level. Note: this method should be used ONLY in
     * developer mode.
     * 
     * @param level Level to be set.
     */
    public void setLevel(final int level) {
        this.levelNum = level;
    }
}
