
package gui;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import simulator.Simulator;

/**
 * Assignment 2 Reference from Professor Shahriar (Shawn) Emami
 * 
 * @author Yi Jiang Class ElevatorApplication
 * @version March.11, 2018
 */

public class ElevatorApplication extends Application implements Observer {
	private ElevatorAnimotor ea;
	private Simulator simulator;
	static final int FLOOR_COUNT = 21;
	
	// declare array of all lable and lableTile here
	String[] stringLableTitle=new String[] {" Elevator ID "," Target Floor "," Current Floor "," Power Consumed "};
	Label[] lblelevatorID = new Label[4];
	Label[] lblTargetFloor = new Label[4];
	Label[] lblCurrentFloor = new Label[4];
	Label[] lblPowerConsumed = new Label[4];

	// declare array of all textfields here
	TextField[] tfElevatorID=new TextField[4];
	TextField[] tfTargetFloor=new TextField[4];
	TextField[] tfCurrentFloor=new TextField[4];
	TextField[] tfPowerConsumed=new TextField[4];

	private Label[][] floors;

	/**
	 * inicial the elevator floor attributes
	 */
	@Override
	public void init() throws Exception {
		super.init();
		simulator = new Simulator(this);
		ea = new ElevatorAnimotor();

		floors = new Label[4][FLOOR_COUNT];
		
		for (int id = 0; id < 4; id++) {
			for (int i = 0; i < FLOOR_COUNT; i++) {

				floors[id][i] = new Label("    " + i + "    ");
				floors[id][i].setId("empty");
				floors[id][i].setMaxSize(150, 28);
				floors[id][i].setMinSize(150, 28);
			}
			floors[id][0].setId("elevator");
		}
	};

	/**
	 * start the elevator
	 * 
	 * @param primaryStage-the object of stage
	 *            
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		if (primaryStage == null) {
			throw new NullPointerException("the primaryStage should not be null");
		}
		
		GridPane rootPane = new GridPane();
		rootPane.setHgap(5);
		rootPane.setVgap(2);

		// create floorPane
		GridPane[] floorPane = new GridPane[4];
		for (int id = 0; id < 4; id++) {
			floorPane[id] = new GridPane();
			for (int i = FLOOR_COUNT - 1; i >= 0; i--) {

				floorPane[id].add(floors[id][i], 1, (FLOOR_COUNT - 1) - i);
			}
		}

		// add floorPane to rootPane
		rootPane.add(floorPane[0], 0, 1);
		rootPane.add(floorPane[1], 3, 1);
		rootPane.add(floorPane[2], 6, 1);
		rootPane.add(floorPane[3], 9, 1);

		// create info pane
		GridPane infoPane[] = new GridPane[4];
		for (int id = 0; id < 4; id++) {
			infoPane[id] = new GridPane();
			//initial lable
			lblelevatorID[id]=new Label(stringLableTitle[0]);
			lblTargetFloor[id]=new Label(stringLableTitle[1]);
			lblCurrentFloor[id]=new Label(stringLableTitle[2]);
			lblPowerConsumed[id]=new Label(stringLableTitle[3]);
			//initial textfield
			tfElevatorID[id] = new TextField("  " + (id + 1));
			tfElevatorID[id].setMaxWidth(120);
			tfTargetFloor[id] = new TextField();
			tfTargetFloor[id].setMaxWidth(120);
			tfCurrentFloor[id] = new TextField();
			tfCurrentFloor[id].setMaxWidth(120);
			tfPowerConsumed[id] = new TextField();
			tfPowerConsumed[id].setMaxWidth(120);

			// add labels to infoPane
			infoPane[id].add(lblelevatorID[id], 1, 0);
			infoPane[id].add(lblTargetFloor[id], 1, 1);
			infoPane[id].add(lblCurrentFloor[id], 1, 2);
			infoPane[id].add(lblPowerConsumed[id], 1, 3);

			// add textfields to infoPane
			infoPane[id].add(tfElevatorID[id], 2, 0);
			infoPane[id].add(tfTargetFloor[id], 2, 1);
			infoPane[id].add(tfCurrentFloor[id], 2, 2);
			infoPane[id].add(tfPowerConsumed[id], 2, 3);
		}

		// add infoPane to rootPane
		rootPane.add(infoPane[0], 1, 1);
		rootPane.add(infoPane[1], 4, 1);
		rootPane.add(infoPane[2], 7, 1);
		rootPane.add(infoPane[3], 10, 1);

		// add rootPane to scene
		Scene scene = new Scene(rootPane);
		scene.getStylesheets().add(ElevatorApplication.class.getResource("elevator.css").toExternalForm());

		scene.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (event.getCode() == KeyCode.ESCAPE) {
				ea.stop();
				primaryStage.hide();
			}
		});

		primaryStage.setScene(scene);
		primaryStage.setTitle("Elevator Simulator");
		primaryStage.show();
		ea.start();
		simulator.start();
	}

	/**
	 * Assignment 2 Reference from Professor Shahriar (Shawn) Emami
	 * 
	 * @author Yi Jiang Class ElevatorAnimotor
	 * @version March.11, 2018
	 */
	public class ElevatorAnimotor extends AnimationTimer {

		private int targetFloor;
		private int id;
		private int[] currentFloor = new int[simulator.getElevatorCount()];
		private double powerUsed;

		int step;
		private Queue<List<Number>> queue = new LinkedList<>();

		/**
		 * method will add list of pair when queque is empty
		 * 
		 * @param pair -list of pair
		 *           
		 */
		public void addPair(List<Number> pair) {
			if (pair == null) {
				throw new NullPointerException("the pair should not be null");
			}

			queue.add(pair);

		}

		/**
		 * @param now-current time
		 *            
		 * 
		 */
		@Override
		public void handle(long now) {

			if (!queue.isEmpty()) {
				List<Number> list = queue.poll();

				id = list.get(0).intValue();
				floors[id][currentFloor[id]].setId("empty");
				
				targetFloor = list.get(2).intValue();
				currentFloor[id] = list.get(1).intValue();
				powerUsed = list.get(3).doubleValue();
				floors[id][targetFloor].setId("target");
				floors[id][currentFloor[id]].setId("elevator");
				tfCurrentFloor[id].setText(String.valueOf(currentFloor[id]));
				tfTargetFloor[id].setText(String.valueOf(targetFloor));
				tfPowerConsumed[id].setText(String.valueOf(powerUsed));
			}
		}
	};

	/**
	 * main method to launch the elevator thread
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);

	}

	/**
	 * method to contral the thread and let it to be stop correct way
	 */
	@Override
	public void stop() throws Exception {

		super.stop();
		simulator.shutdown();

	}

	/**
	 * the method will update the each elevator observable after reach the target
	 * floor
	 */
	@Override
	public void update(Observable observable, Object obj) {
		if (obj == null) {
			throw new NullPointerException("the obj should not be null");
		}
		@SuppressWarnings("unchecked")
		List<Number> pair = (List<Number>) obj;
		ea.addPair(pair);

	}

}
