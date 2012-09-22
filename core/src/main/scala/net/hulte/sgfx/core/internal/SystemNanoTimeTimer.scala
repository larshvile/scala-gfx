package net.hulte.sgfx.core
package internal

import java.util.concurrent.TimeUnit._

private object SystemNanoTimeTimer {
  val FpsSampleLimitNanos = SECONDS.toNanos(1);
}

private[core] final class SystemNanoTimeTimer extends Timer {
  import SystemNanoTimeTimer._

  private var previousTime = time
  private var currentTime = time

  private var fpsSampleSizeNanos = 0L;
  private var frameCount = 0;
  private var avgFps = .0
  
  private def time = System.nanoTime()
  private def elapsedNanos = currentTime - previousTime
  
  override def fps = avgFps
  override def frameSecond = elapsedNanos / SECONDS.toNanos(1).toDouble


  def tick() {
    updateTime();
    updateFps();
  }
  
  def elapsedMsSinceTick = NANOSECONDS.toMillis(time - currentTime)


  private def updateTime() {
    previousTime = currentTime
    currentTime = time;
  }

  private def updateFps() {
    frameCount += 1
    fpsSampleSizeNanos += elapsedNanos
    if (fpsSampleSizeNanos >= FpsSampleLimitNanos) {
      avgFps = (fpsSampleSizeNanos / FpsSampleLimitNanos.toDouble) * frameCount
      frameCount = 0
      fpsSampleSizeNanos = 0
    }
  }
}
