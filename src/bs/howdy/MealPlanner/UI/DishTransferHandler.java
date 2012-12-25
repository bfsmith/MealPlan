package bs.howdy.MealPlanner.UI;

import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.io.IOException;

import javax.swing.*;

import bs.howdy.MealPlanner.Entities.Dish;
import bs.howdy.MealPlanner.Entities.MainDish;
import bs.howdy.MealPlanner.Entities.SideDish;

public class DishTransferHandler extends TransferHandler {
	@Override
    public boolean canImport(TransferSupport support) {
		return true;
        //return (support.getComponent() instanceof Droppable<?>) && support.isDataFlavorSupported(TransferableDish.DATA_FLAVOR);
    }

    @Override
    public boolean importData(TransferSupport support) {
        boolean accept = false;
        if (canImport(support)) {
            try {
                Transferable t = support.getTransferable();
                Dish value = (Dish)t.getTransferData(TransferableDish.DATA_FLAVOR);
                if (value instanceof Dish) {
                    Component component = support.getComponent();
                    if (component instanceof Droppable<?>) {
                        ((Droppable<Dish>)component).drop(value);
                    }
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        }
        return accept;
    }

    @Override
    public int getSourceActions(JComponent c) {
        return DnDConstants.ACTION_COPY;
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        Transferable t = null;
        if (c instanceof JList) {
        	JList list = (JList) c;
            Object value = list.getSelectedValue();
            if (value instanceof Dish) {
                Dish dish = (Dish) value;
                t = new TransferableDish(dish);
            }
        }
        return t;
    }

    @Override
    protected void exportDone(JComponent source, Transferable data, int action) {
        System.out.println("ExportDone");
        if(data instanceof TransferableDish) {
        	try {
				Dish dish = (Dish)data.getTransferData(TransferableDish.DATA_FLAVOR);
				if(dish != null) {
					System.out.println("Tranfered " + dish.getName());					
				}
			} catch (Exception e) {
			
			}
        }
        // Here you need to decide how to handle the completion of the transfer,
        // should you remove the item from the list or not...
    }
}
