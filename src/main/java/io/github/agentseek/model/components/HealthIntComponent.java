package ryleh.model.components;

import ryleh.common.Timer;
import ryleh.common.TimerImpl;
import ryleh.controller.events.GameOverEvent;
import ryleh.controller.events.RemoveEntityEvent;
import ryleh.model.Type;
import ryleh.model.World;
/**
 * This component is used to track an object's health points. Here, hps are implemented as an integer. 
 */
public class HealthIntComponent extends AbstractComponent {

    private int currentHp;
    private int maxHp;
    private boolean isImmortal;
    private final Timer timer;
    private static final double WAIT_TIME = 2000;
    /**
     * Instantiate this component in World, given max amount of Health points. 
     * @param world World where this component is added.
     * @param maxHp Max amount of health points for this component.
     */
    public HealthIntComponent(final World world, final int maxHp) {
        super(world);
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        timer = new TimerImpl(WAIT_TIME);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double deltaTime) {
        if (timer.isElapsed()) {
            this.isImmortal = false;
        }
        if (this.currentHp <= 0) {
            if (super.getObject().getType().equals(Type.PLAYER)) {
                super.getWorld().notifyWorldEvent(new GameOverEvent());
            } else {
                super.getWorld().notifyWorldEvent(new RemoveEntityEvent(super.getObject()));
            }
        }
    }

    /**
     * Decreases currentHp. If it's below zero, it will call a death event.
     * @param dmg Amount of damage.
     */
    public void damage(final int dmg) {
        if (super.getObject().getType().equals(Type.PLAYER)) {
            if (!this.isImmortal) {
                this.currentHp -= dmg;
                this.setImmortality();
            }
        } else {
            this.currentHp -= dmg;
        }
    }

    /**
     * Increases currentHp only if not exceeds maxHp.
     * 
     * @param heal Amount of HP healed
     */
    public void heal(final int heal) {
        this.currentHp = this.currentHp >= this.maxHp ? this.maxHp : this.currentHp + heal;
    }
    /**
     * Increases max amount of health points.
     * @param inc Amount of health points increased.
     */
    public void increaseMaxHp(final int inc) {
        this.maxHp += inc;
    }

    /**
     * Decreases max health. Can't have maximum health below 1. If current HP is
     * bigger than new maxHp, currentHp is automatically set to maximum.
     * 
     * @param dec Amount of HP decreased
     */
    public void decreaseMaxHp(final int dec) {
        this.maxHp = this.maxHp == 1 ? 1 : this.maxHp - dec;
        this.currentHp = this.currentHp > this.maxHp ? this.maxHp : this.currentHp;
    }
    /**
     * Gets max amount of health points.
     * @return Max amount of health points.
     */
    public int getMaxHp() {
        return maxHp;
    }
    /**
     * Sets max amount of health points.
     * @param maxHp Max amount of health points to be set.
     */
    public void setMaxHp(final int maxHp) {
        this.maxHp = maxHp;
    }
    /**
     * Gets current amount of health points left.
     * @return Amount of health points left.
     */
    public int getCurrentHp() {
        return currentHp;
    }
    /**
     * Sets amount of health points left.
     * @param currentHp Health points remaining to be set.
     */
    public void setCurrentHp(final int currentHp) {
        this.currentHp = currentHp;
    }
    /**
     * Checks if this component is currently immortal.
     * @return True if this component is currently immortal.
     */
    public boolean isImmortal() {
        return isImmortal;
    }
    /**
     * Sets Immortality on and starts timer: immortality will be set to false after timer is elapsed.
     */
    public void setImmortality() {
        timer.startTimer();
        this.isImmortal = true;
    }

}
