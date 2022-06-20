package nl.codebulb.onedaygame;

import nl.daedalus.engine.core.DaedalusApplication;
import nl.daedalus.engine.core.DaedalusOptions;

/**
 * A game made in a day in my custom 2d engine.
 *
 * We are just figuring out the engine API and looking for the spots that need work
 * and features we might want to work on next. Actually building something in the engine is a good way of doing that.
 *
 */
public class Application extends DaedalusApplication {

    public static int WINDOW_WIDTH = 1920;
    public static int WINDOW_HEIGHT = 1080;

    public static void main(String[] args) {
        DaedalusOptions options = new DaedalusOptions(WINDOW_WIDTH, WINDOW_HEIGHT, "One day game");

        Application application = new Application();
        application.run(new GameLoop(), options);
    }

}
