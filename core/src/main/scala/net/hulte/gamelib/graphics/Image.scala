package net.hulte.gamelib.graphics

import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Point}
import java.io.{InputStream, IOException, File, FileInputStream}
import javax.imageio.ImageIO


object Image {

  /**
   * Loads an image from a stream. Returns the loaded image as an <code>Image</code>.
   * @throws IOException
   */
  def load(in: InputStream): Image = {
    val img:BufferedImage = ImageIO.read(in)
    if (img == null) {
      throw new IOException("unable to read image, read() returned null")
    } else {
      new Image(img)
    }
  }


  /**
   * Loads an image from a file. Returns the laoded image as an <code>Image</code>.
   * @throws IOException
   */
  def load(imageFile: File): Image = {
    val in = new FileInputStream(imageFile)
    try {
      return load(in)
    } finally {
      in.close()
    }
  }
}


/**
 * Represents a raster image which can be drawn with a renderer.
 */
class Image(private val img: BufferedImage) {

  def size: Point = new Point(img.getWidth(), img.getHeight())

  def draw(renderer: Graphics2D, logic: Function2[Graphics2D, BufferedImage, Unit]): Unit = { // TODO whats the right syntax here? Not Function2 =)
    // TODO convert to compatible image if not already done
    logic(renderer, img)
  }
}

