package edu.oswego.moxie.eguimaraes.GUI;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import edu.oswego.moxie.eguimaraes.animation.Board;
import edu.oswego.moxie.eguimaraes.control.Control;
import edu.oswego.moxie.eguimaraes.domain.Train;
import edu.oswego.moxie.eguimaraes.domain.TrainStation;

public class GUI {

	private JFrame frame;
	private static JTable tableStations;
	private static JTable tableTrain;
	private static JTextField textFieldAVG;
	private static JPanel panel_1;
	private static JPanel panel_2;
	private static JScrollPane scrollPane;
	private static JScrollPane scrollPane_1;
	private static Control control = new Control();
	private static Thread controlThread = new Thread(control);
	private static DefaultTableModel dtmStations;
	private static DefaultTableModel dtmTrains;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		createRows();

		while (true) {
			updateInformation();
		}
	}

	private static void createRows() {
		for (TrainStation station : Control.stations) {
			dtmStations.addRow(new Object[] { station.getName(), station.getPassengers().size() });
		}
		for (Train train : control.trains) {
			dtmTrains.addRow(new Object[] { train.getName(), train.getState(), train.getCurrentStation().getName(), train.occupedSeats() });
		}
	}

	private static void updateInformation() {
		updateTrainTable();
		updateStationsTable();
		textFieldAVG.setText(Integer.toString(Control.counterPassengers.get()));
	}

	private static void updateStationsTable() {
		int i = 0;
		for (TrainStation station : Control.stations) {
			dtmStations.setValueAt(station.getNumberOfPassengers(), i, 1);
			i++;
		}
	}

	private static void updateTrainTable() {
		int i = 0;
		for (Train train : control.trains) {
			dtmTrains.setValueAt(train.getState(), i, 1);
			dtmTrains.setValueAt(train.getCurrentStation().getName(), i, 2);
			dtmTrains.setValueAt(train.occupedSeats(), i, 3);
			dtmTrains.fireTableCellUpdated(i, 1);
			i++;
		}
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(20, 100, 1264, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Train Stations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(4, 9, 205, 451);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 16, 193, 428);
		panel_1.add(scrollPane);

		dtmStations = new DefaultTableModel(0, 0);
		String header[] = new String[] { "Station", "Passengers" };
		dtmStations.setColumnIdentifiers(header);
		tableStations = new JTable();
		scrollPane.setViewportView(tableStations);
		tableStations.setModel(dtmStations);

		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Trains", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(207, 9, 345, 548);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 334, 524);
		panel_2.add(scrollPane_1);

		dtmTrains = new TrainTableModel();
		String headerTrains[] = new String[] { "Train", "State", "Last Location", "Passengers" };
		dtmTrains.setColumnIdentifiers(headerTrains);
		tableTrain = new JTable();
		tableTrain.setModel(dtmTrains);
		tableTrain.setCellSelectionEnabled(false);
		tableTrain.setDefaultRenderer(String.class, new ColorRenderer(tableTrain));
		tableTrain.setRowSelectionAllowed(true);
		scrollPane_1.setViewportView(tableTrain);

		JButton btnStart = new JButton("Start");
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int rows[] = tableTrain.getSelectedRows();
				if (rows.length == 0)
					JOptionPane.showMessageDialog(frame, "Please, select one or more trains to start.");
				for (int i = 0; i < rows.length; i++) {
					control.trains.get(rows[i]).setStart(true);
				}
			}
		});
		btnStart.setBounds(26, 464, 165, 35);
		frame.getContentPane().add(btnStart);

		JPanel panel = new JPanel();
		panel.setBackground(Color.LIGHT_GRAY);
		panel.setBounds(10, 510, 193, 40);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JLabel lblAVG = new JLabel("Num of Passengers");
		lblAVG.setBounds(10, 15, 85, 14);
		panel.add(lblAVG);

		textFieldAVG = new JTextField();
		textFieldAVG.setEditable(false);
		textFieldAVG.setBounds(97, 12, 86, 20);
		panel.add(textFieldAVG);
		textFieldAVG.setColumns(10);
		
		JPanel animation_panel = new Board(control);
		animation_panel.setBounds(562, 9, 686, 548);
		frame.getContentPane().add(animation_panel);

		controlThread.start();

	}
}
