import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class ColorRenderer extends DefaultTableCellRenderer {
		
		JTable tableTrain;
		/**
		 * 
		 */
		private static final long serialVersionUID = -5945641647773551129L;
		Color backgroundColor = getBackground();

		public ColorRenderer(JTable tableTrain) {
			this.tableTrain = tableTrain;
		}
		
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (isSelected) {
				setBackground(table.getSelectionBackground());
				setForeground(table.getSelectionForeground());
			} else {
				if (tableTrain.getModel().getValueAt(row, column) instanceof Train.TrainState
						&& (tableTrain.getModel().getValueAt(row, column).equals(Train.TrainState.departing)
								|| tableTrain.getModel().getValueAt(row, column).equals(Train.TrainState.motion))
						|| tableTrain.getModel().getValueAt(row, column).equals(Train.TrainState.arriving)) {
					c.setBackground(Color.green.brighter());
				} else if (tableTrain.getModel().getValueAt(row, column) instanceof Train.TrainState
						&& (tableTrain.getModel().getValueAt(row, column).equals(Train.TrainState.boarding)
								|| tableTrain.getModel().getValueAt(row, column).equals(Train.TrainState.disembark))) {
					c.setBackground(Color.YELLOW);
				} else if (tableTrain.getModel().getValueAt(row, column) instanceof Train.TrainState
						&& tableTrain.getModel().getValueAt(row, column).equals(Train.TrainState.stopped)) {
					c.setBackground(Color.RED);
				} else {
					c.setBackground(backgroundColor);
				}
				c.setFont(c.getFont().deriveFont(Font.BOLD));
			}
			return c;
		}

	}