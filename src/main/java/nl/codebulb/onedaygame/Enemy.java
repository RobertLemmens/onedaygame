package nl.codebulb.onedaygame;

import nl.daedalus.engine.core.DaedalusLogger;
import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.math.Vec3f;
import nl.daedalus.engine.renderer.texture.SubTexture;
import nl.daedalus.engine.scene.Entity;
import nl.daedalus.engine.scene.Scene;
import nl.daedalus.engine.scene.components.CollisionComponent;
import nl.daedalus.engine.scene.components.MovementComponent;
import nl.daedalus.engine.scene.components.SpriteComponent;
import nl.daedalus.engine.scene.components.TransformComponent;

import java.util.Random;

public class Enemy {
    private static int enemyCount = 0;
    private static SubTexture[] availableSprites;
    private Scene scene;
    private TransformComponent playerTransformComponent;

    public Enemy(Scene scene, int id, TransformComponent playerTransformComponent) {
        this.playerTransformComponent = playerTransformComponent;
        this.scene = scene;
        Entity entity = scene.createEntity("Enemy_" + id);
        TransformComponent transformComponent = new TransformComponent(new Mat4f());

        Random spawnRand = new Random();
        int spawnNumX = spawnRand.nextInt(14);
        int spawnNumY = spawnRand.nextInt(10);

        transformComponent.setPosition(spawnNumX-7,spawnNumY-5, 1f);
        entity.add(transformComponent);
        entity.add(new CollisionComponent());
        entity.add(new MovementComponent((dt)-> {
            Vec3f diff = new Vec3f(
                    playerTransformComponent.getPosition().x() - transformComponent.getPosition().x(),
                    playerTransformComponent.getPosition().y() - transformComponent.getPosition().y(),
                    0
            ).normalize().scale(0.1f);

            transformComponent.setPosition(transformComponent.getPosition().add(diff));

        }));
        Random random = new Random();
        int spriteNum = random.nextInt(availableSprites.length);
        entity.add(new SpriteComponent(availableSprites[spriteNum]));
    }

    public static void init(SubTexture[] sprites) {
        availableSprites = sprites;
    }

    public static void spawnEnemy(Scene scene, TransformComponent playerTransformComponent) {
        if (availableSprites.length == 0) {
            DaedalusLogger.error("No sprites available!");
            return;
        }
        new Enemy(scene, enemyCount, playerTransformComponent);
        enemyCount++;
    }

}
