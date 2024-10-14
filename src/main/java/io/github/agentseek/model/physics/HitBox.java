package ryleh.model.physics;

import ryleh.common.Shape2d;
/**
 * Hit boxes are zones attached to a game object that represent where that object is collidable.
 */
public interface HitBox {
    /** Gets the form of this hit box. Hit boxes should have two-dimensional geometric forms, like rectangle or circle. 
     * @return A two dimensional shape.*/
    Shape2d getForm();
    /**
     * Checks if this hit box is colliding with an another hit box, intersecting their two-dimensional shapes. 
     * @param box Box that is colliding with this hit box.
     * @return True if that hit box is colliding with this hit box.
     */
    boolean isCollidingWith(HitBox box);
    /**
     * Checks if this hit box is out of bounds.
     * @param bounds Bounds of the game world.
     * @return True if this hit box is out of world bounds.
     */
    boolean isOutOfBounds(Shape2d bounds);

}
