package net.hulte.sgfx.core

/**
 * A simple, game-related timing device. 
 */
trait Timer {
    
    /**
     * Ah yes, the well known frame-second =). Returns the time elapsed between the previous & current frame
     * relative to a second. I.e. Running at 100 fps this would be {@code 10ms/1000ms = .01}. The typical
     * use-case for this is moving an object on-screen with a speed relative to the time passed instead of
     * the frame-rate. 
     * 
     * <pre>
     *   // moving an object at 40px/s
     *   pxPos += 40 * timer.frameSecond
     * </pre>
     */
    def frameSecond: Double

    /**
     * Returns an estimate of the number of frames/s.
     */
    def fps: Double

}
