package nl.codebulb.onedaygame;

import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.math.Vec3f;
import nl.daedalus.engine.renderer.texture.SubTexture;
import nl.daedalus.engine.scene.Entity;
import nl.daedalus.engine.scene.EntityRegistry;
import nl.daedalus.engine.scene.Scene;
import nl.daedalus.engine.scene.components.MovementComponent;
import nl.daedalus.engine.scene.components.SpriteComponent;
import nl.daedalus.engine.scene.components.TransformComponent;

public class Projectile {

    private static SubTexture texture;
    private static int projectileCount = 0;
    private Vec3f start;
    private Vec3f end;
    private float speed;
    private TransformComponent transformComponent;

    public static void init(SubTexture tex) {
       texture = tex;
    }

    public Projectile(Scene scene, Vec3f start, Vec3f end) {
        this.start = start;
        this.end = end;
        speed = 1;

        String name = "Projectile_" + projectileCount;
        Entity entity = scene.createEntity(name);
        transformComponent = new TransformComponent(new Mat4f());
        transformComponent.setPosition(start.x(), start.y(), 1.0f);

        Vec3f diff = new Vec3f(
                end.x() - start.x(),
                end.y() - start.y(),
                0
        ).normalize().scale(0.1f);


        float dx = end.x() - start.x();
        float dy = end.y() - start.y();

        float angle = (float) Math.toDegrees(Math.atan2(dy, dx));

        if (angle < 0) {
            angle += 360;
        }

        transformComponent.setRotation(angle - 90);

        MovementComponent movementComponent = new MovementComponent((dt) -> {
            transformComponent.setPosition(transformComponent.getPosition().add(diff));
            if (transformComponent.getPosition().x() < GameLoop.worldBounds.r() ||
                    transformComponent.getPosition().x() > GameLoop.worldBounds.g() ||
                    transformComponent.getPosition().y() < GameLoop.worldBounds.b() ||
                    transformComponent.getPosition().y() > GameLoop.worldBounds.a()) {
                EntityRegistry.removeEntity(name);
            }
        });

        entity.add(transformComponent);
        entity.add(new SpriteComponent(texture));
        entity.add(movementComponent);
        entity.add(new ProjectileComponent());
    }

    public static void spawnProjectile(Scene scene, Vec3f start, Vec3f end) {
        new Projectile(scene, start, end);
        projectileCount++;
    }

}
