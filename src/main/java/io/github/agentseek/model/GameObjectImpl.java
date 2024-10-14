package ryleh.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import ryleh.common.Point2d;
import ryleh.model.components.AbstractComponent;
import ryleh.model.physics.CircleHitBox;
import ryleh.model.physics.HitBox;

/**
 * A class that provides the implementation of the interface GameObject,
 * handling the operations related to a GameObject.
 */
public class GameObjectImpl implements GameObject {
    private Type type;
    private String id;
    private Point2d position;
    private HitBox box;
    private final List<AbstractComponent> components;
    /**
     * The default HitBox radius of a GameObject.
     */
    private static final int DEFAULT_HITBOX_RADIUS = 100;

    /**
     * Default constructor. Instantiate this object with a default position and a
     * default hit box.
     */
    public GameObjectImpl() {
        position = new Point2d(0, 0);
        components = new ArrayList<>();
        this.box = new CircleHitBox(DEFAULT_HITBOX_RADIUS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAdded(final World world) {
        this.id = world.generateId("gameObject");
        box.getForm().setPosition(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onUpdate(final double deltaTime) {
        components.forEach(i -> i.onUpdate(deltaTime));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point2d getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPosition(final Point2d position) {
        this.position = position;
        box.getForm().setPosition(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<AbstractComponent> getComponents() {
        return components;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<? extends AbstractComponent> getComponent(final Class<? extends AbstractComponent> type) {
        return components.stream().filter(type::isInstance).findAny();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addComponent(final AbstractComponent component) {
        if (this.components.stream().anyMatch(i -> component.getClass().isInstance(i))) {
            throw new IllegalStateException();
        } else {
            this.components.add(component);
            component.onAdded(this);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "GameObjectImpl [id=" + id + ", type=" + type + "]";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHitBox(final HitBox box) {
        this.box = box;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HitBox getHitBox() {
        return this.box;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GameObjectImpl other = (GameObjectImpl) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

}
