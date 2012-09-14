package net.hulte.sgfx.graphics

import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Point}
import java.io.{InputStream, IOException, File, FileInputStream}
import javax.imageio.ImageIO

object ImageIo {

  def load(in: InputStream): BufferedImage = {
    val img: BufferedImage = ImageIO.read(in)
    if (img == null) throw new IOException("Unable to read image, read() returned null.") else img
  }

  def load(imageFile: File): BufferedImage = {
    val in = new FileInputStream(imageFile)
    try {
      load(in)
    } finally {
      in.close()
    }
  }
}
