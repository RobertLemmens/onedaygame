package nl.codebulb.onedaygame;

import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.math.Vec3f;
import nl.daedalus.engine.math.Vec4f;
import nl.daedalus.engine.renderer.Renderer;
import nl.daedalus.engine.renderer.camera.OrthographicCamera;

// Custom scene with immediate renderer access
// TODO we might want a native Hud / UI library in the engine
public class HudScene {

    private OrthographicCamera camera;
    private Vec4f healthBarColor = new Vec4f(0.9f, 0.1f, 0.3f, 1.0f);
    private Vec4f boostBarColor = new Vec4f(0.9f, 0.9f, 0.3f, 1.0f);
    private float xScale = 10;

    private float boostXscale = 10;


    public void init() {
        this.camera = new OrthographicCamera(GameLoop.worldBounds.r(), GameLoop.worldBounds.g(),
                GameLoop.worldBounds.b(), GameLoop.worldBounds.a());
    }

    public void setHealthBarSize(float playerHealth) {
        this.xScale = playerHealth / 10.0f;
    }

    public void setBoostBarSize(float playerBoost) {
        this.boostXscale = playerBoost / 10.0f;
    }

    public void onUpdate(float dt) {

        Renderer.begin(camera);

        Renderer.drawQuad(new Vec3f(2, 8, 1), Mat4f.scale(new Vec3f(xScale, 0.45f, 1.0f)), healthBarColor);
        Renderer.drawQuad(new Vec3f(2, 7.5f, 1), Mat4f.scale(new Vec3f(boostXscale, 0.45f, 1.0f)), boostBarColor);

        Renderer.end();

    }

}
