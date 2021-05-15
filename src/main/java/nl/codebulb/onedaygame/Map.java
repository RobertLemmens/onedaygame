package nl.codebulb.onedaygame;

import nl.daedalus.engine.math.Vec2f;
import nl.daedalus.engine.renderer.texture.SubTexture;
import nl.daedalus.engine.renderer.texture.TextureAtlas;
import nl.daedalus.engine.renderer.texture.TileMap;

public class Map {

    private TextureAtlas textureAtlas;
    private TileMap tileMap;

    public Map(TextureAtlas textureAtlas) {
        this.textureAtlas = textureAtlas;

        textureAtlas.addCombined("grass", new Vec2f(1,1), new Vec2f(1,2));

        SubTexture[][] backgroundTileMapData = new SubTexture[Application.WINDOW_HEIGHT/64][Application.WINDOW_WIDTH/64];
        for (int y = 0; y < backgroundTileMapData.length; y++) {
            for (int x = 0; x < backgroundTileMapData[0].length; x++) {
                backgroundTileMapData[y][x] = textureAtlas.getCombined("grass");
            }
        }

        tileMap = new TileMap(backgroundTileMapData);
    }


    public TileMap getTileMap() {
        return tileMap;
    }
}
