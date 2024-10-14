package ryleh.view;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

/**
 * An enumeration class that contains all the textures needed, with their path,
 * width and height.
 */
public enum Textures {
    /**
     * Player down Texture-frame 1.
     */
    PLAYER_DOWN("assets/texture/player/PlayerDown1.png", 120, 120),
    /**
     * Player down Texture-frame 2.
     */
    PLAYER_DOWN2("assets/texture/player/PlayerDown2.png", 120, 120),
    /**
     * Player Front Texture-frame 4.
     */
    PLAYER_DOWN4("assets/texture/player/PlayerDown3.png", 120, 120),

    /**
     * Player up Texture-frame 1.
     */
    PLAYER_UP("assets/texture/player/PlayerUp1.png", 120, 120),
    /**
     * Player up Texture-frame 2.
     */
    PLAYER_UP2("assets/texture/player/PlayerUp2.png", 120, 120),
    /**
     * Player up Texture-frame 4.
     */
    PLAYER_UP4("assets/texture/player/PlayerUp3.png", 120, 120),

    /**
     * Player right texture-frame 1.
     */
    PLAYER_RIGHT("assets/texture/player/PlayerRight1.png", 120, 120),
    /**
     * Player right texture-frame 2.
     */
    PLAYER_RIGHT2("assets/texture/player/PlayerRight2.png", 120, 120),
    /**
     * Player right texture-frame 4.
     */
    PLAYER_RIGHT4("assets/texture/player/PlayerRight3.png", 120, 120),

    /**
     * Player left texture-frame 1.
     */
    PLAYER_LEFT("assets/texture/player/PlayerLeft1.png", 120, 120),
    /**
     * Player left texture-frame 2.
     */
    PLAYER_LEFT2("assets/texture/player/PlayerLeft2.png", 120, 120),
    /**
     * Player left texture-frame 4.
     */
    PLAYER_LEFT4("assets/texture/player/PlayerLeft3.png", 120, 120),

    /**
     * Fire texture-frame 1.
     */
    FIRE1("assets/texture/obstacles/Fire1.png", 140, 140),
    /**
     * Fire texture-frame 2.
     */
    FIRE2("assets/texture/obstacles/Fire2.png", 140, 140),
    /**
     * Fire texture-frame 3.
     */
    FIRE3("assets/texture/obstacles/Fire3.png", 140, 140),
    /**
     * Fire texture-frame 4.
     */
    FIRE4("assets/texture/obstacles/Fire4.png", 140, 140),
    /**
     * Fire texture-frame 5.
     */
    FIRE5("assets/texture/obstacles/Fire5.png", 140, 140),


    /**
     * Rock texture 1.
     */
    ROCK("assets/texture/obstacles/rock.png", 145, 145),
    /**
     * Rock texture 2.
     */
    ROCK2("assets/texture/obstacles/rock2.png", 145, 145),

    /**
     * Door texture-frame 1.
     */
    DOOR1("assets/texture/levels/ManHole1.png", 200, 200),
    /**
     * Door texture-frame 2.
     */
    DOOR2("assets/texture/levels/ManHole2.png", 200, 200),
    /**
     * Door texture-frame 3.
     */
    DOOR3("assets/texture/levels/ManHole3.png", 200, 200),
    /**
     * Door texture-frame 4.
     */
    DOOR4("assets/texture/levels/ManHole4.png", 200, 200),
    /**
     * Door texture-frame 5.
     */
    DOOR5("assets/texture/levels/ManHole5.png", 200, 200),

    /**
     * Background1 texture.
     */
    BACKGROUND("assets/texture/levels/Background1.png", 1920, 1080),
    /**
     * Background2 texture.
     */
    BACKGROUND2("assets/texture/levels/Background2.png", 1920, 1080),
    /**
     * Background3 texture.
     */
    BACKGROUND3("assets/texture/levels/Background3.png", 1920, 1080),

    /**
     * Item texture-frame 1.
     */
    ITEM1("assets/texture/items/Chest1.png", 90, 90),
    /**
     * Item texture-frame 2.
     */
    ITEM2("assets/texture/items/Chest2.png", 90, 90),

    /**
     * Enemy Drunk texture.
     */
    ENEMY_DRUNK("assets/texture/enemies/EnemyDrunk.png", 100, 100),
    /**
     * Enemy Shooter texture.
     */
    ENEMY_SHOOTER("assets/texture/enemies/EnemyShooter.png", 100, 100),
    /**
     * Enemy Lurker texture.
     */
    ENEMY_LURKER("assets/texture/enemies/EnemyLurker.png", 100, 100),
    /**
     * Enemy Spinner texture.
     */
    ENEMY_SPINNER("assets/texture/enemies/EnemySpinner.png", 100, 100),
    /**
     * Enemy DrunkSpinner texture.
     */
    ENEMY_DRUNKSPINNER("assets/texture/enemies/EnemyDrunkSpinner.png", 100, 100),

    /**
     * Player Bullet texture.
     */
    PLAYER_BULLET("assets/texture/player/PlayerBullet.png", 40, 40),
    /**
     * Enemy Bullet texture.
     */
    ENEMY_BULLET("assets/texture/enemies/EnemyBullet.png", 40, 40);

    private String texture;
    private int width;
    private int height;

    /**
     * textures.
     * 
     * @param string The path of the asset.
     * @param width  The width of the asset.
     * @param height The height of the asset.
     */
    Textures(final String string, final int width, final int height) {
        this.texture = string;
        this.width = width;
        this.height = height;
    }

    /**
     * A method that returns the ImagePattern.
     * 
     * @return the ImagePattern of the associated Texture.
     */
    public ImagePattern getImagePattern() {
        return new ImagePattern(new Image(this.texture));
    }

    /**
     * A method to get the scaled width of a texture.
     * 
     * @return The scaled width of a Texture.
     */
    public int getWidth() {
        return (int) (this.width * ViewHandlerImpl.getScaleModifier());
    }

    /**
     * A method to get the scaled height of a texture.
     * 
     * @return The scaled height of a Texture.
     */
    public int getHeight() {
        return (int) (this.height * ViewHandlerImpl.getScaleModifier());
    }
}
