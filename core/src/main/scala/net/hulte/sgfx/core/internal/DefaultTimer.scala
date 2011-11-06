package net.hulte.sgfx.core.internal

import net.hulte.sgfx.core.Timer


/**
 * Default Timer-implementation, based on System.nanoTime().
 *
 * @author lars
 */
private[core] final class DefaultTimer extends Timer {

  private var previousTime: Long = time
  private var currentTime: Long = time

  private var fpsSamplingTime: Long = 0; // how many ms we've sampled for the fps-count
  private var fpsCounter: Int = 0; // number of updates during that time
  private var fpsVal: Double = 0;

  private def time = System.nanoTime()

  override def elapsedMs = nanoToMs(currentTime - previousTime)
  private def nanoToMs(nano: Long) = (.000001 * nano).toLong

  override def fps = fpsVal


  /**
   * Returns the number of ms elapsed since the last call to update(), i.e. returns a
   * 'live' version of elapsedMs
   */
  def elapsedMsSinceUpdate() = nanoToMs(time - currentTime) // TODO dirty johan, make this public on Timer?


  /**
   * Updates the internal state of the timer, should be called once for each drawing-loop.
   */
  def update() {
    updateTime();
    updateFps();
    // TODO calculate the anim-delta & shit here?
  }


  private def updateTime() {
    previousTime = currentTime
    currentTime = time;
  }


  private def updateFps() {
    fpsCounter += 1
    fpsSamplingTime += elapsedMs
    if (fpsSamplingTime >= DefaultTimer.FpsSampleSize) {
      fpsVal = (fpsSamplingTime / 1000.) * fpsCounter
      fpsCounter = 0
      fpsSamplingTime = 0
    }
  }
}


private object DefaultTimer {

  /** Defines the sampling-size in ms used to calculate the FPS */
  val FpsSampleSize = 1000;
}

