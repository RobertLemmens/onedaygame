package nl.codebulb.onedaygame;

import nl.daedalus.engine.math.Mat4f;
import nl.daedalus.engine.renderer.texture.TextureAtlas;
import nl.daedalus.engine.scene.Entity;
import nl.daedalus.engine.scene.Scene;
import nl.daedalus.engine.scene.components.SpriteComponent;
import nl.daedalus.engine.scene.components.TransformComponent;

public class Score {

    private int score = 0;
    private int scoreTens = 0;
    private Entity scoreEntity;
    private Entity scoreEntityTens;
    private TextureAtlas tileSheetAtlas;

    public Score(Scene scene, TextureAtlas tileSheetAtlas) {
        this.tileSheetAtlas = tileSheetAtlas;
        scoreEntity = scene.createEntity("score");
        scoreEntityTens = scene.createEntity("scoretens");
        TransformComponent scoreTransform = new TransformComponent(new Mat4f());
        scoreTransform.setPosition(12, 7, 1);
        scoreEntity.add(scoreTransform);
        scoreEntity.add(new SpriteComponent(tileSheetAtlas.subTextures[0][score]));


        TransformComponent scoreTensTransform = new TransformComponent(new Mat4f());
        scoreTensTransform.setPosition(11.3f, 7, 1);
        scoreEntityTens.add(scoreTensTransform);
        scoreEntityTens.add(new SpriteComponent(tileSheetAtlas.subTextures[0][score]));
    }

    public int getScore() {
        return score;
    }

    public void addPoint() {
        if (scoreTens <= 9) {
            score++;
            if (score == 10) {
                score = 0;
                scoreTens++;
                var spriteTens = scoreEntityTens.getComponent(SpriteComponent.class);
                spriteTens.setTexture(tileSheetAtlas.subTextures[0][scoreTens]);
            }
            var sprite = scoreEntity.getComponent(SpriteComponent.class);
            sprite.setTexture(tileSheetAtlas.subTextures[0][score]);
        }
    }

}
