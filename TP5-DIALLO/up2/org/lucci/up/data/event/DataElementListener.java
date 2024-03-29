package org.lucci.up.data.event;

import java.util.EventListener;

import org.lucci.up.data.DataElement;
import org.lucci.up.data.rendering.DataElementRenderer;

/**
 * @author luc
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public interface DataElementListener extends EventListener
{
	void renderedAdded( DataElement dataElement, DataElementRenderer renderer );
	void renderedRemoved( DataElement dataElement, DataElementRenderer renderer );
}
