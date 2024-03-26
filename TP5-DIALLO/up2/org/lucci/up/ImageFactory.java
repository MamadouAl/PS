package org.lucci.up;


import java.awt.*;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.*;

import org.lucci.up.system.*;
import java.io.StringWriter;
/*
import org.apache.batik.dom.svg.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.apache.batik.util.SVGConstants; */
import org.lucci.up.system.*;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * The user may want to get an image object or the data of an
 * image file (PNG, JPEG, SVG...) for, for instance, return it to a 
 * HTTP-client that will have to draw the image on the web page
 * it will show.
 * 
 * @author Luc Hogie
 */
public class ImageFactory
{
	private Graphics2DPlotter graphics2DPlotter = new Graphics2DPlotter();

    public Graphics2DPlotter getGraphics2DPlotter()
    {
        return graphics2DPlotter;
    }

    public void setGraphics2DPlotter(Graphics2DPlotter graphics2DPlotter)
    {
        this.graphics2DPlotter = graphics2DPlotter;
    }
	

	/**
	 * Creates and image with the given dimension.
	 * @param width
	 * @param height
	 * @return Image
	 */
	public Image getAWTImage( int width, int height )
	{
		BufferedImage image = new BufferedImage( height, width, BufferedImage.TYPE_INT_RGB );
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setClip(0, 0, width, height);
		getGraphics2DPlotter().paint(graphics);
		graphics.dispose();
		return image;
	}


	/**
	 * Creates a PNG-encoded byte array of an image with the given dimension.
	 * @param width
	 * @param height
	 * @return byte[]
	 */
	public byte[] getPNGData( int width, int height )
	{
		return createImageData( (RenderedImage) getAWTImage(width, height), "PNG" );
	}
	
	/**
	 * Creates a JPEG-encoded byte array of an image with the given dimension.
	 * Warning! The JPEG is format is definitively adapted 
	 * to photographies, and only photographies. The JPEG image
	 * returned by the bitmap image factory will be ugly.
	 * 
	 * @param width
	 * @param height
	 * @return byte[]
	 */
	public byte[] getJPEGData( int width, int height )
	{
		return createImageData( (RenderedImage) getAWTImage(width, height), "JPEG" );
	}

	/**
	 * Creates the SVG code of an image with the given dimension.
	 * 
	 * @param // width
	 * @param //height
	 * @return String
	 */
	/*
	public String getSVGCode( int width, int height )
	{
		DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
		String namespaceURI = SVGDOMImplementation.SVG_NAMESPACE_URI;
		Document document = impl.createDocument(namespaceURI, SVGConstants.SVG_SVG_TAG, null);
		SVGGraphics2D graphics = new SVGGraphics2D(document);
		graphics.setClip(0, 0, width, height );
		graphics2DPlotter.paint(graphics);
		StringWriter writer = new StringWriter();
		
		try
		{
			graphics.stream(writer);
		}
		catch (SVGGraphics2DIOException e)
		{
			throw new IllegalStateException( "Batik couldn't write SVG code" );
		}

		return writer.getBuffer().toString();
	} */

	private byte[] createImageData( RenderedImage image, String type )
	{
		try
		{
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write( image, type, os );
			os.close();
			image = null;
			return os.toByteArray();
		}
		catch ( IOException ex )
		{
			throw new IllegalStateException( "I/O error shouldn't have occured" );
		}
	}
}
