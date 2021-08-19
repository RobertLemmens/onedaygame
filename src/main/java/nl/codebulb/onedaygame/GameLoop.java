package nl.codebulb.onedaygame;

import nl.daedalus.engine.core.DaedalusLoop;
import nl.daedalus.engine.events.Event;
import nl.daedalus.engine.math.Vec2f;
import nl.daedalus.engine.math.Vec4f;
import nl.daedalus.engine.renderer.texture.Texture;
import nl.daedalus.engine.renderer.texture.TextureAtlas;
import nl.daedalus.engine.scene.EntityRegistry;
import nl.daedalus.engine.scene.components.CameraComponent;


public class GameLoop implements DaedalusLoop {

    private GameScene gameScene;
    private StartScreen startScreen;

    private Texture tileSheet;
    private TextureAtlas tileSheetAtlas;
    private Texture spriteSheet;
    private TextureAtlas spriteAtlas;

    private boolean isPlaying = false;
    public static Vec4f worldBounds;

    @Override
    public void onInit() {

        CameraComponent cameraComponent = new CameraComponent();
        cameraComponent.getCamera().setOrthographicSize(15);

        spriteSheet = Texture.create("textures/spritesheet/roguelikeChar_transparent.png");
        spriteAtlas = new TextureAtlas(spriteSheet, new Vec2f(16f,16f), 1);

        tileSheet = Texture.create("textures/tilemap/towerDefense_tilesheet.png");
        tileSheetAtlas = new TextureAtlas(tileSheet, new Vec2f(64,64), 0);

        cameraComponent.getCamera().setViewportSize(Application.WINDOW_WIDTH, Application.WINDOW_HEIGHT);
        GameLoop.worldBounds = cameraComponent.getCamera().getBounds(); // TODO moet makkelijker

        initStartScene();
    }

    public void initStartScene() {
        startScreen = new StartScreen();
        startScreen.init(this);
    }

    public void initGameScene() {
        gameScene = new GameScene();
        gameScene.init(spriteAtlas, tileSheetAtlas, this);
    }


    public void startPlaying() {
        initGameScene();
        isPlaying = true;
        startScreen = null;
    }

    public void backToMenu() {
        isPlaying = false;
        initStartScene();
        gameScene = null;
        EntityRegistry.removeAll();
    }

    @Override
    public void onUpdate(float v) {
        if (isPlaying) {
            gameScene.onUpdate(v);
        } else {
            startScreen.onUpdate(v);
        }
    }

    @Override
    public void onEvent(Event event) {
        if (isPlaying) {
            gameScene.onEvent(event);
        } else {
            startScreen.onEvent(event);
        }
    }

    @Override
    public void onExit() {

    }
}
