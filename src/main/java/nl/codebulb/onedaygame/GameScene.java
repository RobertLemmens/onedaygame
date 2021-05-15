package nl.codebulb.onedaygame;

import nl.daedalus.engine.events.Event;
import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.math.Vec3f;
import nl.daedalus.engine.renderer.texture.SubTexture;
import nl.daedalus.engine.renderer.texture.TextureAtlas;
import nl.daedalus.engine.scene.Entity;
import nl.daedalus.engine.scene.EntityRegistry;
import nl.daedalus.engine.scene.Scene;
import nl.daedalus.engine.scene.components.CameraComponent;
import nl.daedalus.engine.scene.components.CollisionComponent;
import nl.daedalus.engine.scene.components.TransformComponent;

import java.util.List;
import java.util.Random;

public class GameScene {

    private Scene bgScene;
    private HudScene hudScene;
    private Player player;

    private float gameTime = 0;
    private float spawnAccum = 0;
    private float spawnLimit = 4.0f;
    private Score score; //TODO we really want text input... (drawText())
    private GameLoop gameLoop;

    public void init(TextureAtlas spriteAtlas, TextureAtlas tileSheetAtlas, GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        hudScene = new HudScene();

        Map map = new Map(tileSheetAtlas);

        bgScene = new Scene();
        bgScene.setTileMap(map.getTileMap());

        player = new Player(bgScene, spriteAtlas);

        Entity sceneCamera = bgScene.createEntity("Camera");
        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.getCamera().setOrthographicSize(15);
        sceneCamera.add(cameraComponent);
        sceneCamera.add(new TransformComponent(new Mat4f()));

        Enemy.init(new SubTexture[]{spriteAtlas.subTextures[1][0], spriteAtlas.subTextures[2][0]});

        score = new Score(bgScene, tileSheetAtlas);

        Projectile.init(spriteAtlas.subTextures[5][49]);

        bgScene.onViewportResize(Application.WINDOW_WIDTH, Application.WINDOW_HEIGHT);
        GameLoop.worldBounds = cameraComponent.getCamera().getBounds(); // TODO moet makkelijker
        hudScene.init();
        hudScene.setHealthBarSize(player.getHealth());
        hudScene.setBoostBarSize(player.getBoost());
    }

    public void onUpdate(float dt) {
        // some random spawning mechanics made on the fly
        gameTime += dt;
        spawnAccum += dt;
        if (spawnAccum >= spawnLimit) {
            Random rand = new Random();
            int bound = spawnLimit >= 2 ? 4 : 10;
            int amount = rand.nextInt(bound);
            for (int i = 0; i <= amount; i++) {
                Enemy.spawnEnemy(bgScene, player.getTransformComponent());
            }
            spawnAccum -= spawnLimit;
        }

        if (spawnLimit > 0.5f) {
            if (gameTime >= 10 && spawnLimit > 2) {
                spawnLimit--;
                gameTime = 0;
            }
            if (gameTime >= 30 && spawnLimit <= 2) {
                spawnLimit-=0.5f;
                gameTime = 0;
            }
        }

        if (player.getSpeed() == 0.2f) {
            player.setBoost(player.getBoost() - 0.2f);
            hudScene.setBoostBarSize(player.getBoost());
        }
        if (player.getBoost() <= 0) {
            player.setSpeed(0.1f);
        }


        checkForCollisions();

        bgScene.onUpdate(dt); //TODO entities nog niet bound by scene...
        hudScene.onUpdate(dt);
    }
    public void checkForCollisions() {
        List<Entity> projectiles = EntityRegistry.getGroup(ProjectileComponent.class);
        List<Entity> ents = EntityRegistry.getGroup(CollisionComponent.class); //TODO create exclude filter
        for (Entity ent : ents) {
            Vec3f enemyLocation = ent.getComponent(TransformComponent.class).getPosition();
            if (checkCollision(player.getTransformComponent().getPosition(), enemyLocation)){
                player.setHealth(player.getHealth()-10);
                if (player.getHealth() == 0) {
                    // game over!
                    gameLoop.backToMenu();
                }
                EntityRegistry.removeEntity(ent.getName());
                hudScene.setHealthBarSize(player.getHealth());
            }
        }

        if (!projectiles.isEmpty()) {
            for (Entity projectile : projectiles) {
                Vec3f projectileLocation = projectile.getComponent(TransformComponent.class).getPosition();
                for (Entity ent : ents) {
                    if (ent.getName().contains("Projectile"))
                        continue;
                    Vec3f enemyLocation = ent.getComponent(TransformComponent.class).getPosition();

                    if (checkCollision(projectileLocation, enemyLocation)) {
                        EntityRegistry.removeEntity(projectile.getName());
                        EntityRegistry.removeEntity(ent.getName());
                        score.addPoint();
                        if (player.getBoost() < 100) {
                            player.setBoost(player.getBoost() + 0.5f);
                            hudScene.setBoostBarSize(player.getBoost());
                        }
                    }
                }
            }

        }

    }

    public boolean checkCollision(Vec3f one, Vec3f two) {
        // collision x-axis?
        boolean collisionX = one.x() + 1 >= two.x() &&
                two.x() + 1 >= one.x();
        // collision y-axis?
        boolean collisionY = one.y() + 1 >= two.y() &&
                two.y() + 1 >= one.y();
        // collision only if on both axes
        return collisionX && collisionY;
    }

    public void onEvent(Event e) {
        player.onEvent(e);
    }

}
