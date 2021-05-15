package nl.codebulb.onedaygame;

import nl.daedalus.engine.events.Event;
import nl.daedalus.engine.events.KeyPressedEvent;
import nl.daedalus.engine.input.KeyCodes;
import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.math.Vec3f;
import nl.daedalus.engine.math.Vec4f;
import nl.daedalus.engine.renderer.Renderer;
import nl.daedalus.engine.renderer.camera.OrthographicCamera;

public class StartScreen {
    private OrthographicCamera camera;
    private Vec4f healthBarColor = new Vec4f(0.9f, 0.1f, 0.3f, 1.0f);
    private GameLoop gameLoop;

    public void init(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
        this.camera = new OrthographicCamera(GameLoop.worldBounds.r(), GameLoop.worldBounds.g(),
                GameLoop.worldBounds.b(), GameLoop.worldBounds.a());
    }

    public void onUpdate(float dt) {

        Renderer.begin(camera);
        Renderer.drawQuad(new Vec3f(1, 1, 1), Mat4f.scale(new Vec3f(0.45f, 0.45f, 1f)), healthBarColor);
        Renderer.end();

    }

    public void onEvent(Event event) {
        if (event.getType() == Event.EventType.KeyPressed) {
            KeyPressedEvent e = (KeyPressedEvent) event;
            if (e.getKeyCode() == KeyCodes.DAE_KEY_SPACE) {
                // start game
                gameLoop.startPlaying();
            }
        }
    }
}
