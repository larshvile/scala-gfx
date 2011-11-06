package net.hulte.sgfx.core


/**
 * Represents a simple, game-related timing-device. 
 * 
 * @author lars
 */
trait Timer {

    /**
     * Returns the number of elapsed milliseconds since the last frame.
     */
    def elapsedMs : Long;

    // TODO animationDelta shit...

    /**
     * Returns the current FPS-count.
     */
    def fps : Double;

}

