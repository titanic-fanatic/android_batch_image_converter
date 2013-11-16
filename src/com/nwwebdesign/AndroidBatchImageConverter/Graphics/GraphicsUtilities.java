package com.nwwebdesign.AndroidBatchImageConverter.Graphics;

import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

public class GraphicsUtilities
{
  public static BufferedImage loadCompatibleImage(URL paramURL)
    throws IOException
  {
    BufferedImage localBufferedImage = ImageIO.read(paramURL);
    return toCompatibleImage(localBufferedImage);
  }

  public static BufferedImage createCompatibleImage(int paramInt1, int paramInt2) {
    return getGraphicsConfiguration().createCompatibleImage(paramInt1, paramInt2);
  }

  public static BufferedImage toCompatibleImage(BufferedImage paramBufferedImage) {
    if (isHeadless()) {
      return paramBufferedImage;
    }

    if (paramBufferedImage.getColorModel().equals(getGraphicsConfiguration().getColorModel())) {
      return paramBufferedImage;
    }

    BufferedImage localBufferedImage = getGraphicsConfiguration().createCompatibleImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), paramBufferedImage.getTransparency());

    Graphics localGraphics = localBufferedImage.getGraphics();
    localGraphics.drawImage(paramBufferedImage, 0, 0, null);
    localGraphics.dispose();

    return localBufferedImage;
  }

  public static BufferedImage createCompatibleImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2) {
    return getGraphicsConfiguration().createCompatibleImage(paramInt1, paramInt2, paramBufferedImage.getTransparency());
  }

  private static GraphicsConfiguration getGraphicsConfiguration()
  {
    GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
    return localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration();
  }

  private static boolean isHeadless() {
    return GraphicsEnvironment.isHeadless();
  }

  public static BufferedImage createTranslucentCompatibleImage(int paramInt1, int paramInt2) {
    return getGraphicsConfiguration().createCompatibleImage(paramInt1, paramInt2, 3);
  }

  public static int[] getPixels(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int[] paramArrayOfInt)
  {
    if ((paramInt3 == 0) || (paramInt4 == 0)) {
      return new int[0];
    }

    if (paramArrayOfInt == null)
      paramArrayOfInt = new int[paramInt3 * paramInt4];
    else if (paramArrayOfInt.length < paramInt3 * paramInt4) {
      throw new IllegalArgumentException("Pixels array must have a length >= w * h");
    }

    int i = paramBufferedImage.getType();
    if ((i == 2) || (i == 1)) {
      WritableRaster localWritableRaster = paramBufferedImage.getRaster();
      return (int[])(int[])localWritableRaster.getDataElements(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt);
    }

    return paramBufferedImage.getRGB(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfInt, 0, paramInt3);
  }
}