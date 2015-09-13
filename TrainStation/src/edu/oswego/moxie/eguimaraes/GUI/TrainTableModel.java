package edu.oswego.moxie.eguimaraes.GUI;
import javax.swing.table.DefaultTableModel;

public class TrainTableModel extends DefaultTableModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -472768868723156229L;

	@Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    
    
	
}
