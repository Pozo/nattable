package com.github.pozo;

import java.io.InputStream;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.nebula.widgets.nattable.NatTable;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommand;
import org.eclipse.nebula.widgets.nattable.command.ILayerCommandHandler;
import org.eclipse.nebula.widgets.nattable.config.AbstractRegistryConfiguration;
import org.eclipse.nebula.widgets.nattable.config.CellConfigAttributes;
import org.eclipse.nebula.widgets.nattable.config.IConfigRegistry;
import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.data.person.Person;
import org.eclipse.nebula.widgets.nattable.examples.data.person.PersonService;
import org.eclipse.nebula.widgets.nattable.freeze.CompositeFreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.FreezeLayer;
import org.eclipse.nebula.widgets.nattable.freeze.command.FreezeColumnCommand;
import org.eclipse.nebula.widgets.nattable.grid.GridRegion;
import org.eclipse.nebula.widgets.nattable.grid.layer.ColumnHeaderLayer;
import org.eclipse.nebula.widgets.nattable.grid.layer.DefaultColumnHeaderDataLayer;
import org.eclipse.nebula.widgets.nattable.layer.AbstractLayer;
import org.eclipse.nebula.widgets.nattable.layer.CompositeLayer;
import org.eclipse.nebula.widgets.nattable.layer.DataLayer;
import org.eclipse.nebula.widgets.nattable.layer.ILayer;
import org.eclipse.nebula.widgets.nattable.layer.cell.ColumnOverrideLabelAccumulator;
import org.eclipse.nebula.widgets.nattable.layer.cell.ILayerCell;
import org.eclipse.nebula.widgets.nattable.painter.cell.BackgroundImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.BackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.CellPainterWrapper;
import org.eclipse.nebula.widgets.nattable.painter.cell.GradientBackgroundPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ICellPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.ImagePainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.TextPainter;
import org.eclipse.nebula.widgets.nattable.painter.cell.decorator.CellPainterDecorator;
import org.eclipse.nebula.widgets.nattable.selection.SelectionLayer;
import org.eclipse.nebula.widgets.nattable.style.CellStyleAttributes;
import org.eclipse.nebula.widgets.nattable.style.DisplayMode;
import org.eclipse.nebula.widgets.nattable.style.HorizontalAlignmentEnum;
import org.eclipse.nebula.widgets.nattable.style.Style;
import org.eclipse.nebula.widgets.nattable.ui.util.CellEdgeEnum;
import org.eclipse.nebula.widgets.nattable.util.GUIHelper;
import org.eclipse.nebula.widgets.nattable.viewport.ViewportLayer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

public class Main {
	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 
	*/
	public static void main(String[] args) {
		/*StandaloneNatExampleRunner.run(600, 400, new _4221_NatGridLayerPainterExample());*/
		
		try {
			Main window = new Main();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		
		final NatTable natTable = createExampleControl(shell);
		
		final Button button = new Button(shell, SWT.CHECK);
		button.setText("Miafasz");
		button.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				natTable.doCommand(new ToggleFreezeCommand());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {}
		});
		button.setSelection(true);
		button.notifyListeners(SWT.Selection, new Event());

		shell.setText("SWT Application");

	}
    public NatTable createExampleControl(Composite panel) {
        List<Person> persons = PersonService.getPersons(10);
        
		IDataProvider bodyDataProvider = new RotatedDataBodyProvider(persons);
        DataLayer bodyDataLayer = new DataLayer(bodyDataProvider);

		final ColumnOverrideLabelAccumulator columnLabelAccumulator = new ColumnOverrideLabelAccumulator(bodyDataLayer);
		
		for (int columnIndex = 0; columnIndex < bodyDataLayer.getColumnCount(); columnIndex++) {
			columnLabelAccumulator.registerColumnOverrides(columnIndex, PainterConfiguration.COMPARABLE);
		}
		
		bodyDataLayer.setConfigLabelAccumulator(columnLabelAccumulator);
		/**/
		
		
        
        final SelectionLayer selectionLayer = new SelectionLayer(bodyDataLayer);
        final ViewportLayer viewportLayer = new ViewportLayer(selectionLayer);
        final FreezeLayer freezeLayer = new FreezeLayer(selectionLayer);        
        final CompositeFreezeLayer compositeFreezeLayer = new CustomCompositeFreezeLayer(freezeLayer, viewportLayer, selectionLayer,false);

        // build the column header layer
        IDataProvider columnHeaderDataProvider = new RotatedDataColumnProvider(persons);
        DataLayer columnHeaderDataLayer = new DefaultColumnHeaderDataLayer(columnHeaderDataProvider);
        ColumnHeaderLayer columnHeaderLayer = new ColumnHeaderLayer(columnHeaderDataLayer, compositeFreezeLayer, selectionLayer);
        
        CompositeLayer compositeLayer = new CompositeLayer(1, 2);
		compositeLayer.setChildLayer(GridRegion.BODY, compositeFreezeLayer, 0, 1);
		compositeLayer.setChildLayer(GridRegion.COLUMN_HEADER,columnHeaderLayer, 0, 0);
        
        final NatTable natTable = new NatTable(panel, compositeLayer, false);

        natTable.configure();

        panel.setLayout(new GridLayout());
        GridDataFactory.fillDefaults().grab(true, true).applyTo(panel);
        GridDataFactory.fillDefaults().grab(true, true).applyTo(natTable);

        natTable.addConfiguration(new PainterConfiguration());
        natTable.configure();
        natTable.registerCommandHandler(new ToggerFreezeCommandHandler(compositeFreezeLayer));
        return natTable;
    }
	class PainterConfiguration extends AbstractRegistryConfiguration  {
		public static final String COMPARABLE = "COMPARABLE";
		@Override
		public void configureRegistry(IConfigRegistry configRegistry) {
			InputStream resourceAsStream = getClass().getResourceAsStream("/org/eclipse/nebula/widgets/nattable/examples/resources/column_header_bg.png");
			Image bgImage = new Image(
					Display.getDefault(), 
					resourceAsStream);
			
			TextPainter txtPainter2 = new TextPainter() {

				@Override
				protected String convertDataType(ILayerCell cell, IConfigRegistry configRegistry) {
					Object model = cell.getDataValue();
					return super.convertDataType(cell, configRegistry);
				}
				
			};
			TextPainter txtPainter = new TextPainter(false,false);
			ImagePainter bgImagePainter = new ImagePainter(bgImage);
			
			CellPainterDecorator cellPainterDecorator = new CellPainterDecorator(
					txtPainter, 
					CellEdgeEnum.LEFT, 
					bgImagePainter);
			 
			configRegistry.registerConfigAttribute(
					CellConfigAttributes.CELL_PAINTER, 
					cellPainterDecorator, 
					DisplayMode.NORMAL, 
					GridRegion.BODY);
			
			Style cellStyle = new Style();
			
			cellStyle.setAttributeValue(
				CellStyleAttributes.BACKGROUND_COLOR, Display.getDefault().getSystemColor(SWT.COLOR_GRAY));
			configRegistry.registerConfigAttribute(CellConfigAttributes.CELL_STYLE, cellStyle);
		}
		
	}
    private class ToggerFreezeCommandHandler implements ILayerCommandHandler<ToggleFreezeCommand> {
    	private static final boolean IS_TOGGLE = true;
    	private AbstractLayer abstractayer;

		public ToggerFreezeCommandHandler(AbstractLayer abstractayer) {
    		this.abstractayer = abstractayer;
    	}
		@Override
		public Class<ToggleFreezeCommand> getCommandClass() {
			return ToggleFreezeCommand.class;
		}

		@Override
		public boolean doCommand(ILayer targetLayer, ToggleFreezeCommand command) {
			
			return abstractayer.doCommand(new FreezeColumnCommand(abstractayer,1,IS_TOGGLE));
		}
    	
    }
    private class ToggleFreezeCommand implements ILayerCommand {
		@Override
		public boolean convertToTargetLayer(ILayer targetLayer) {
			return true;
		}

		@Override
		public ILayerCommand cloneCommand() {
			return new ToggleFreezeCommand();
		}
    	
    }
}
