import java.awt.Canvas;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.MenuShortcut;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class TSPDisplay extends JFrame {
	
	private static final long serialVersionUID = -1342019186113559923L;
	private static String[] OnOff = { "ON", "OFF" };
	private String app_name = new String("Traveling Salesman Problem");


	// declarations for the command menu
	MenuBar main_menu = new MenuBar();
	Menu file_menu = new Menu();
	MenuItem file_exit = new MenuItem();
	Menu comp_menu = new Menu();
	MenuItem comp_run = new MenuItem();
	MenuItem comp_step = new MenuItem();
	MenuItem comp_settings = new MenuItem();
	Menu help_menu = new Menu();
	MenuItem help_about = new MenuItem();

	// Global Settings -- defaults
	private int pop_size = 16;
	private int plotsHorizontal = 4;
	private int plotsVertical = 4;
	private int NUM_IN_RUN = 500;
	private int eliteIndex, mutateIndex=0;
	private boolean mutateStatus = true;
	private boolean eliteStatus = true;
	
	private boolean DEBUG = false;
	private Plot_Area[] plot; 				// areas for plotting
	private static int mouse_x = 0, mouse_y = 0; 	// current positn of mouse
	private Label[] label; 							// the status bars
	private Chromosome route = null; 				// current tour

	private double route_length = 0; 				// length of route

	private Generation myGen;

	public TSPDisplay(int p, int pVert, int pHor, boolean e, boolean m) {
		pop_size = p;
		plotsHorizontal = pHor;
		plotsVertical = pVert;
		mutateStatus = m;
		eliteStatus = e;

		myGen = new Generation();
		
		setLocation(100, 100);
		set_menus(); // setup the menu bar

		setNewPlotAreas();

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				 file_exit_action();
			}
		});

		pack();

	}

	private void setNewPlotAreas() {

		plot = new Plot_Area[pop_size];
		label = new Label[pop_size];
		System.out.println(pop_size + " " + plotsHorizontal + " " + plotsVertical + " " + eliteStatus + " "
				+ mutateStatus);
		eliteIndex = (eliteStatus ? 0 : 1);
		mutateIndex = (mutateStatus ? 0 : 1 );

		if (myGen != null)
			setTitle("Traveling Salesman Problem  " + "                        Generation: " + myGen.number
					+ "                        Elitism: " + OnOff[eliteIndex] + "                        Mutation: "
					+ OnOff[mutateIndex]);
		else
			setTitle("Traveling Salesman Problem " + "                        Generation: " + 0
					+ "                        Elitism: " + OnOff[eliteIndex] + "                        Mutation: "
					+ OnOff[mutateIndex]);

		Container container = getContentPane();
		container.removeAll(); // remove any previous displays
		GridLayout grid = new GridLayout(plotsVertical, plotsHorizontal);
		container.setLayout(grid);

		JFrame frame = new JFrame();
		Container frameContainer = new Container();

		for (int i = 0; i < pop_size; i++) {
			frame = new JFrame();
			frameContainer = frame.getContentPane();
			GridBagLayout gb = new GridBagLayout(); // layout for window
			frameContainer.setLayout(gb);

			plot[i] = new Plot_Area(this);
			GridBagConstraints c = new GridBagConstraints();

			c.fill = GridBagConstraints.BOTH;
			c.weighty = 1.0;
			c.gridwidth = GridBagConstraints.REMAINDER;
			gb.setConstraints(plot[i], c);
			frameContainer.add(plot[i]);

			label[i] = new Label(" ");
			label[i].setBackground(Color.lightGray);
			label[0].setText("Click in the above map area.");

			c.fill = GridBagConstraints.HORIZONTAL;
			c.weightx = 1.0;
			c.weighty = 0.0;
			gb.setConstraints(label[i], c);
			frameContainer.add(label[i]);

			container.add(frameContainer);
		}

		setVisible(true);
	}

	//inner class for plotting
	class Plot_Area extends Canvas {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -5763298262677922857L;
		Point mouse = null;
		TSPDisplay parent; // of plot area

		/**
		 * "plot_area( tsp2 )"
		 *
		 * @return nothing
		 */
		public Plot_Area(TSPDisplay ctrl) {
			parent = ctrl;

			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));

			addMouseListener(new MouseAdapter() {
				/**
				 * "mousePressed( MouseEvent )" adds new point to route when
				 * mouse button is pressed
				 *
				 * @return nothing
				 */
				public void mousePressed(MouseEvent e) {
					
					Graphics g = parent.plot[0].getGraphics();
					mouse = e.getPoint();
					if( route == null )
						route = new Chromosome();
					route.addCity(mouse.x, mouse.y);
					System.out.println( "mousepressed");
					
					myGen = new Generation( route );					
					route_length = myGen.getFitness();
					parent.plot[0].update(g);
					display_map_status(0);
					show_mouse_location();
					
					for (int i = 1; i < pop_size; i++) {
						if(myGen.chromosomes[i]!= null) {
							g = parent.plot[i].getGraphics();
							route = myGen.chromosomes[i];
							route_length = myGen.chromosomes[i].fitness();
							parent.plot[i].update(g);
							display_map_status(i);
						}
						
					}
				}
			});

			addMouseMotionListener(new MouseMotionAdapter() {
				/**
				 * "mouseMoved( MouseEvent )" updates current location of mouse
				 * in the plot area.
				 *
				 * @return nothing
				 */
				public void mouseMoved(MouseEvent e) {
					mouse = e.getPoint();
					show_mouse_location();
				}
			});
		}

		/**
		 * "show_mouse_location()" updates display of mouse location on
		 *
		 * status bar.
		 * 
		 * @return nothing
		 */
		public void show_mouse_location() {
			if (mouse != null)
				mouse_location(mouse);
		}

		public void mouse_location(Point pt) {
			mouse_x = pt.x; // save current mouse location
			mouse_y = pt.y;

			// label[0].setText( "[ " + mouse_x + " : " + mouse_y + " ] ");
			display_map_status(0); // show its location
			// display_status(0);
		}

		/**
		 * "paint( Graphics )" updates plot area display.
		 *
		 * @return pointer to list item
		 */
		public void paint(Graphics g) {
			
			City pt;
			int n, i = 0;
			int firstX, firstY, x1, x2 = 0;
			int y1, y2 = 0;
			Color fg = getForeground();

			g.setColor(fg); // set default foreground color
			g.setPaintMode();
			g.drawLine(0, 0, 0, getHeight() - 1);
			g.drawLine(0, 0, getWidth() - 2, 0);
			g.drawLine(getWidth() - 2, 0, getWidth() - 2, getHeight() - 1);
			g.drawLine(0, getHeight() - 1, getWidth() - 2, getHeight() - 1);

			n = myGen.chromosomes.length; // number of city's in list

			if (n == 0 || route==null) // no points to plot
				return;

			if (DEBUG)
				System.out.println("paint " + route);
			
			pt = route.getCity(0); 		// first item
			x1 = pt.getxLoc();
			y1 = pt.getyLoc();
			firstX = pt.getxLoc();
			firstY = pt.getyLoc();

			g.setColor(Color.blue); 	// paint the first point
			g.setPaintMode(); 			// as blue square
			g.fillRect(x1 - 3, y1 - 3, 7, 7);

			g.setColor(fg); // paint intermediate points
			g.setPaintMode(); // the default foreground color
			
			if (route.size() > 1) {
				for (i = 0; i < route.size()-1; i++) {   // do intermediate points
				
					//System.out.println("painting" + i);
					x2 = x1; // save last point
					y2 = y1;

					pt = parent.route.getCity(i + 1); // get current pt
					x1 = pt.getxLoc();
					y1 = pt.getyLoc();

					g.drawLine(x1, y1, x2, y2); // line from last to current
					g.fillOval(x1 - 2, y1 - 2, 5, 5); // draw current pt as
														// circle
				}
				g.drawLine(x1, y1, firstX, firstY); // connect to beginning

				g.setColor(Color.red); // paint the last point
				g.setPaintMode(); // as red square
				g.fillRect(x1 - 3, y1 - 3, 7, 7);
			}
		}
	}
	//terminates inner class Plot_Area

	/**
	 * "comp_run_action()" run the algorithm to completion. Initial route is
	 * saved so that it can be restored using the reset command.
	 *
	 * @return nothing
	 */
	void comp_run_action() {
		Cursor c = plot[0].getCursor(); // save cursor setting

		Graphics g = plot[0].getGraphics();
		plot[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// do genetic algorithm
		// create a new generation and store best tour in "route"
		do {
			myGen.nextGen();

			for (int i = 0; i < pop_size; i++) {
				g = plot[i].getGraphics();
				route = myGen.chromosomes[i];
				route_length = myGen.chromosomes[i].fitness();
				if (DEBUG)
					System.out.println("step " + route);
				plot[i].update(g);
				display_status(i);
			}
			route_length = myGen.chromosomes[0].fitness();

		} while (myGen.number % NUM_IN_RUN > 0);

		plot[0].setCursor(c); // restore cursor setting
	}

	/**
	 * "comp_step_action()" runs the algorithm for a single generation. The
	 * previous route is saved at each step so that it can be restored using the
	 * reset command.
	 *
	 * @return nothing
	 */
	void comp_step_action() {

		Cursor c = plot[0].getCursor(); // save cursor setting

		// set wait cursor
		plot[0].setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

		// plot new generation
		myGen.nextGen();

		for (int i = 0; i < pop_size; i++) {
			Graphics g = plot[i].getGraphics();
			route = myGen.chromosomes[i];
			route_length = myGen.chromosomes[i].fitness();
			if (DEBUG)
				System.out.println("step " + route);
			plot[i].update(g);
			display_status(i);
		}

		plot[0].setCursor(c); // restore cursor setting
	}

	public void display_status(int i) {
		if (myGen != null) {
			label[i].setText("" + myGen.chromosomes[i].fitness());
			this.setTitle("Traveling Salesman Problem  " + "                        Generation: " + myGen.number
					+ "                        Elitism: " + OnOff[eliteIndex] + "                        Mutation: "
					+ OnOff[mutateIndex]);
		} else {
			label[i].setText("" + route_length);
			this.setTitle("Traveling Salesman Problem  " + "                        Generation:  0");
		}
	}

	public void display_map_status(int i) {
		if (myGen != null && myGen.chromosomes[i] != null) {
			route_length = myGen.chromosomes[i].fitness();
			this.setTitle("Traveling Salesman Problem  " + "                        Generation: " + myGen.number
					+ "                        Elitism: " + OnOff[eliteIndex] + "                        Mutation: "
					+ OnOff[mutateIndex]);
		}
		if (label[i] != null && i == 0)
			label[i].setText("[" + mouse_x + " : " + mouse_y + "]   " + route_length);
		else if (label[i] != null)
			label[i].setText("" + route_length);

	}

	/**
	 * "comp_settings_action()" calls a dialog box that allows user to adjust
	 * parameters of the genetic algorithm.
	 *
	 * @return nothing
	 */
	private void comp_settings_action() {
		
		try {
			SettingsDialog dialog = new SettingsDialog(this, true);
			dialog.set_mutation(mutateStatus);
			dialog.set_elitism(eliteStatus);
			dialog.set_num_chrom(pop_size);
			dialog.set_num_in_row(plotsHorizontal);
			dialog.set_num_in_col(plotsVertical);
			dialog.set_num_in_Run(NUM_IN_RUN);
			dialog.setVisible(true); // display dialog

			pop_size = dialog.get_numberOfChromosomes();
			plotsVertical = dialog.get_numInCol();
			plotsHorizontal = dialog.get_numInRow();
			mutateStatus = dialog.get_mutation();
			eliteStatus = dialog.get_elitism();
			NUM_IN_RUN = dialog.get_numInRun();

			Generation.POP_SIZE=pop_size;
			Generation.ELITISM = eliteStatus;
			Generation.MUTATION = mutateStatus;
			myGen = new Generation(myGen.firstRoute);
			dispose();
			setNewPlotAreas();
			
		} catch (Exception e) 
		{}
	}


	/**
	 * "set_menus()" creates the main menu bar and sub-menus.
	 *
	 * @return pointer to list item
	 */
	void set_menus() {
		// File menu
		file_menu.setLabel("File");

		file_menu.add(file_exit);
		file_exit.setLabel("Exit");

		main_menu.add(file_menu);

		// Compute menu
		comp_menu.setLabel("Compute");

		comp_menu.add(comp_run);
		comp_run.setLabel("Run");
		comp_run.setShortcut(new MenuShortcut(KeyEvent.VK_V, false));

		comp_menu.add(comp_step);
		comp_step.setLabel("Step");
		comp_step.setShortcut(new MenuShortcut(KeyEvent.VK_X, false));

		comp_menu.add(comp_settings);
		comp_settings.setLabel("Settings");

		main_menu.add(comp_menu);

		// Help menu
		help_menu.setLabel("Help");

		help_menu.add(help_about);
		help_about.setLabel("About...");

		main_menu.add(help_menu);
		setMenuBar(main_menu);
		action_interface action = new action_interface();
		file_exit.addActionListener(action);
		comp_run.addActionListener(action);
		comp_step.addActionListener(action);
		comp_settings.addActionListener(action);

		help_about.addActionListener(action);
	}

	/**
	 * "action_interface" links menu command actions to application methods.
	 *
	 * @return nothing
	 */
	class action_interface implements ActionListener {
		/**
		 * "actionPerformed( ActionEvent )" selects a method corresponding to a
		 * user selected menu command.
		 *
		 * @return nothing
		 */
		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();

			// select menu command response
			if (object == file_exit)
				file_exit_action();
			else 
			if (object == comp_run)
				comp_run_action();
			else if (object == comp_step)
				comp_step_action();
			else if (object == comp_settings)
				comp_settings_action();
		}
	}

	/**
	 * "file_exit_action()" displays a dialog to confirm the exit command.
	 */
	void file_exit_action() {
		try {
			int response = JOptionPane.showConfirmDialog(null, "Do you want to quit?");
			if( response == 0 )
				System.exit(0);
			else return;
		} catch (Exception e) 
		{}
	}

}
