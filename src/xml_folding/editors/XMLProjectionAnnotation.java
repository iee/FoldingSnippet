package xml_folding.editors;

import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;

public class XMLProjectionAnnotation extends ProjectionAnnotation {
	public void paint(GC gc, Canvas canvas, Rectangle rectangle) {
		/*Image image= super.getImage(canvas.getDisplay());
		if (image != null) {
			ImageUtilities.drawImage(image, gc, canvas, rectangle, SWT.CENTER, SWT.TOP);
			if (fIsRangeIndication) {
				FontMetrics fontMetrics= gc.getFontMetrics();
				int delta= (fontMetrics.getHeight() - image.getBounds().height)/2;
				rectangle.y += delta;
				rectangle.height -= delta;
				drawRangeIndication(gc, canvas, rectangle);
			}
		}*/
	}
	// сделаем заглушки, таким образом сворачивание разворачивание будет вызываться только нашими методами
	public void markCollapsed() {
		//fIsCollapsed= true;
	}

	/**
	 * Marks this annotation as being unfolded.
	 */
	public void markExpanded() {
		//fIsCollapsed= false;
	}
	
	public void markCollapsedReal() {
		super.markCollapsed();
	}

	/**
	 * Marks this annotation as being unfolded.
	 */
	public void markExpandedReal() {
		super.markExpanded();
	}
}
