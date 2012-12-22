package bs.howdy.MealPlanner.UI;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

import bs.howdy.MealPlanner.Entities.Dish;

public class TransferableDish implements Transferable {
	public final static DataFlavor DATA_FLAVOR = new DataFlavor(Dish.class, "mp/dish");
	private Dish _dish;
	public TransferableDish(Dish dish) {
		_dish = dish;
	}
	
	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DATA_FLAVOR };
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return DATA_FLAVOR.equals(flavor);
	}

	@Override
	public Object getTransferData(DataFlavor flavor) {
		return _dish;
	}
	
}
