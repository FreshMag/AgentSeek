package ryleh.model.physics;

import ryleh.common.Circle2d;
import ryleh.common.Shape2d;
import ryleh.common.Vector2d;

/**
 * This type of hit box uses a Circle to handle geometric intersection.
 */
public class CircleHitBox implements HitBox {

    private final Circle2d form;

    /**
     * Instantiate a CircleHitBox given the form of this hit box.
     * 
     * @param form Form of the hit box.
     */
    public CircleHitBox(final Circle2d form) {
        this.form = form;
    }

    /**
     * Instantiate a CircleHitBox given the radius of his circle.
     * 
     * @param radius Radius of the form of this HitBox.
     */
    public CircleHitBox(final int radius) {
        this.form = new Circle2d(radius);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape2d getForm() {
        return this.form;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCollidingWith(final HitBox box) {
        return this.form.intersect(box.getForm());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOutOfBounds(final Shape2d bounds) {
        return !(bounds.contains(this.form.getPosition())
                && bounds.contains(this.form.getPosition().sum(new Vector2d(0, form.getRadius())))
                && bounds.contains(this.form.getPosition().sum(new Vector2d(0, -form.getRadius())))
                && bounds.contains(this.form.getPosition().sum(new Vector2d(form.getRadius(), 0)))
                && bounds.contains(this.form.getPosition().sum(new Vector2d(-form.getRadius(), 0))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "CircleHitBox [form=" + form + "]";
    }

}
