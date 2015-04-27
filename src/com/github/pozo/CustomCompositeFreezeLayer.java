package com.github.pozo;

import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.freeze.CompositeFreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.FreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.IFreezeConfigAttributes;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.painter.layer.ILayerPainter;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;

public class CustomCompositeFreezeLayer extends CompositeFreezeLayer {

	private FreezeLayer freezeLayer;
	private ILayerPainter iLayerPainter = new FreezableLayerPainter();
	
	public CustomCompositeFreezeLayer(FreezeLayer freezeLayer,
			ViewportLayer viewportLayer, SelectionLayer selectionLayer) {
		super(freezeLayer, viewportLayer, selectionLayer);
		this.freezeLayer = freezeLayer;
	}
	public CustomCompositeFreezeLayer(FreezeLayer freezeLayer,
			ViewportLayer viewportLayer, SelectionLayer selectionLayer, boolean defaultConfig) {
		super(freezeLayer, viewportLayer, selectionLayer,defaultConfig);
		this.freezeLayer = freezeLayer;
	}	
	@Override
	public ILayerPainter getLayerPainter() {
		return iLayerPainter;
	}

	class FreezableLayerPainter extends CompositeLayerPainter {
		
		public FreezableLayerPainter() {
		}
		
		@Override
		public void paintLayer(ILayer natLayer, GC gc, int xOffset, int yOffset, Rectangle rectangle, IConfigRegistry configRegistry) {
			super.paintLayer(natLayer, gc, xOffset, yOffset, rectangle, configRegistry);
			
			Color separatorColor = configRegistry.getConfigAttribute(IFreezeConfigAttributes.SEPARATOR_COLOR, DisplayMode.NORMAL);
			if (separatorColor == null) {
				separatorColor = GUIHelper.COLOR_GRAY;
			}
			
			gc.setClipping(rectangle);
			Color oldFg = gc.getForeground();
			gc.setForeground(separatorColor);
			final int freezeWidth = freezeLayer.getWidth() - 1;
			if (freezeWidth > 0) {
				gc.drawLine(xOffset + freezeWidth, yOffset, xOffset + freezeWidth, yOffset + getHeight() - 1);
			}
			final int freezeHeight = freezeLayer.getHeight() - 1;
			if (freezeHeight > 0) {
				gc.drawLine(xOffset, yOffset + freezeHeight, xOffset + getWidth() - 1, yOffset + freezeHeight);
			}
			gc.setForeground(oldFg);
		}
		
	}
}
