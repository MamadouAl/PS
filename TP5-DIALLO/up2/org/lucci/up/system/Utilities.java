package org.lucci.up.system;

/**
 * @author luc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class Utilities
{
	/**
	 * this is a ugly hack used to correct a bug of the JVM
	 * that does 6 + 1 = 7.000003
	 * a better solution should be used!
	 */
	public static float blabla( float i )
	{
    	i *= 100000;
    	i = Math.round(i);
    	i /= 100000;
    	return i;
	}
}
