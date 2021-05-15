package nl.codebulb.onedaygame;

import nl.daedalus.engine.events.Event;
import nl.daedalus.engine.events.KeyPressedEvent;
import nl.daedalus.engine.events.KeyReleasedEvent;
import nl.daedalus.engine.events.MousePressedEvent;
import nl.daedalus.engine.input.DaedalusInput;
import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.math.Vec2f;
import nl.daedalus.engine.math.Vec3f;
import nl.daedalus.engine.renderer.texture.TextureAtlas;
import nl.daedalus.engine.scene.Entity;
import nl.daedalus.engine.scene.Scene;
import nl.daedalus.engine.scene.components.InputComponent;
import nl.daedalus.engine.scene.components.SpriteComponent;
import nl.daedalus.engine.scene.components.TransformComponent;
import nl.daedalus.engine.util.EventProcessor;
import nl.daedalus.engine.util.InputProcessor;

import static nl.daedalus.engine.input.KeyCodes.*;

public class Player implements InputProcessor, EventProcessor {

    private Scene scene;
    private TextureAtlas spriteAtlas;

    private TransformComponent transformComponent;

    private int health = 100;
    private float boost = 100;
    private float speed = 0.1f;

    public Player(Scene scene, TextureAtlas spriteAtlas) {
        this.scene = scene;
        this.spriteAtlas = spriteAtlas;
        Entity entity = scene.createEntity("Player");
        transformComponent = new TransformComponent(new Mat4f());
        entity.add(transformComponent);
        entity.add(new SpriteComponent(spriteAtlas.subTextures[0][0]));
        InputComponent inputComponent = new InputComponent(this);
        entity.add(inputComponent);
    }

    public void attack() {
        Vec2f mousePosition = DaedalusInput.getMousePosition(); //TODO transform naar world bounds
        float width = GameLoop.worldBounds.g() - GameLoop.worldBounds.r();
        float height = GameLoop.worldBounds.a() - GameLoop.worldBounds.b();
        float xClip = (mousePosition.x() / Application.WINDOW_WIDTH) * width - width * 0.5f; // TODO moet makkelijker (screenToWorld()) functie oid introduceren
        float yClip = height * 0.5f - (mousePosition.y() / Application.WINDOW_HEIGHT) * height;

        Projectile.spawnProjectile(scene, transformComponent.getPosition(), new Vec3f(xClip, yClip, 1.0f));
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setBoost(float boost) {
        this.boost = boost;
    }

    public float getBoost() {
        return boost;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public TransformComponent getTransformComponent() {
        return this.transformComponent;
    }

    @Override
    public void onInput() {
        if (DaedalusInput.isKeyPressed(DAE_KEY_UP) || DaedalusInput.isKeyPressed(DAE_KEY_W)) {
            if(transformComponent.getPosition().y() < GameLoop.worldBounds.a() - 0.5f) {
                transformComponent.setPosition(transformComponent.getPosition().addY(speed));
            }
        } else if (DaedalusInput.isKeyPressed(DAE_KEY_DOWN) || DaedalusInput.isKeyPressed(DAE_KEY_S)) {
            if(transformComponent.getPosition().y() > GameLoop.worldBounds.b() + 0.5f) {
                transformComponent.setPosition(transformComponent.getPosition().subtractY(speed));
            }
        }
        if (DaedalusInput.isKeyPressed(DAE_KEY_LEFT) || DaedalusInput.isKeyPressed(DAE_KEY_A)) {
            if(transformComponent.getPosition().x() > GameLoop.worldBounds.r() + 0.5f) {
                transformComponent.setPosition(transformComponent.getPosition().subtractX(speed));
            }

        } else if (DaedalusInput.isKeyPressed(DAE_KEY_RIGHT) || DaedalusInput.isKeyPressed(DAE_KEY_D)) {
            if(transformComponent.getPosition().x() < GameLoop.worldBounds.g() - 0.5f) {
                transformComponent.setPosition(transformComponent.getPosition().addX(speed));
            }
        }
    }

    @Override
    public void onEvent(Event event) {
        if (Event.EventType.KeyPressed == event.getType()) {
            KeyPressedEvent e = (KeyPressedEvent) event;
            if (e.getKeyCode() == DAE_KEY_SPACE) {
                attack();
            }
            if (e.getKeyCode() == DAE_KEY_LEFT_SHIFT && boost >= 0) {
                speed = 0.2f;
            }
        }
        if (Event.EventType.KeyReleased == event.getType()) {
            KeyReleasedEvent e = (KeyReleasedEvent) event;
            if (e.getKeyCode() == DAE_KEY_LEFT_SHIFT) {
                speed = 0.1f;
            }
        }
        if(Event.EventType.MouseButtonPressed == event.getType()) {
            MousePressedEvent e = (MousePressedEvent)  event;
            if (e.getButton() == DAE_MOUSE_BUTTON_1) {
                attack();
            }
        }
    }
}
