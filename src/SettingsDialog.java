import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * "settings_dlg" implements a dialog which asks the user to confirm change
 * simulation parameters.
 *
 * @author Stephen Schmitt
 * @version 1.0
 * @since 01/12/00
 */
class SettingsDialog extends Dialog {
	private static final long serialVersionUID = -7568945264260924047L;
	Button ok = new Button();
	Button defaults = new Button();
	Button cancel = new Button();

	Label labelP = new Label("POPULATION PARAMETERS", Label.LEFT);
	Label numChromLabel = new Label("Number of Chromosomes.", Label.LEFT);
	TextField numChromField = new TextField(60);

	Checkbox eliteCB = new Checkbox(" Elitism On", true);
	Checkbox mutateCB = new Checkbox(" Mutation On", true);

	Label labelD = new Label("DISPLAY PARAMETERS", Label.LEFT);

	Label numInRowLabel = new Label("Number of chromosomes in each row.",
			Label.LEFT);
	TextField numInRowField = new TextField(60);

	Label numInColLabel = new Label("Number of chromosomes in each column.",
			Label.LEFT);
	TextField numInColField = new TextField(60);

	Label numInRunLabel = new Label("Number Run iterations (Ctrl-V).",
			Label.LEFT);
	TextField numInRunField = new TextField(60);

	int numberOfChromosomes; // settings for application
	int numInRow, numInCol, numInRun;
	boolean elitism, mutation;

	boolean notified = false; // reset addNotify flag
	Frame frame = null; // Invoking frame

	/**
	 * "settings_dlg( Frame, boolean )"
	 *
	 * @return nothing
	 */
	public SettingsDialog(Frame parent, boolean modal) {
		super(parent, modal);

		// Keep a local reference to the invoking frame
		frame = parent;

		setTitle("Genetic Algorithm - parameters");
		setLayout(null);
		setSize(360, 300);
		setVisible(false);

		ok.setLabel(" OK ");
		add(ok);
		ok.setFont(new Font("Dialog", Font.BOLD, 12));
		ok.setBounds(20, 250, 80, 22);

		defaults.setLabel(" Defaults ");
		add(defaults);
		defaults.setFont(new Font("Dialog", Font.BOLD, 12));
		defaults.setBounds(140, 250, 80, 22);

		cancel.setLabel(" Cancel ");
		add(cancel);
		cancel.setFont(new Font("Dialog", Font.BOLD, 12));
		cancel.setBounds(260, 250, 80, 22);

		add(labelP); // POPULATION PARAMETERS
		labelP.setBounds(90, 5, 260, 22);

		// Number of Chromosomes
		numChromField.setBounds(20, 35, 60, 22);
		add(numChromField);
		numChromLabel.setBounds(90, 35, 260, 22);
		add(numChromLabel);

		add(eliteCB); // Elitism
		eliteCB.setBounds(60, 65, 100, 22);

		add(mutateCB); // Mutation
		mutateCB.setBounds(210, 65, 100, 22);

		add(labelD); // DISPLAY PARAMETERS
		labelD.setBounds(100, 100, 260, 22);

		// number of Chromosomes in each row
		add(numInRowField);
		numInRowField.setBounds(20, 130, 60, 22);
		add(numInRowLabel);
		numInRowLabel.setBounds(90, 130, 260, 22);

		// number of Chromosomes in each column
		add(numInColField);
		numInColField.setBounds(20, 160, 60, 22);
		add(numInColLabel);
		numInColLabel.setBounds(90, 160, 260, 22);

		// number of iterations RUN
		add(numInRunField);
		numInRunField.setBounds(20, 190, 60, 22);
		add(numInRunLabel);
		numInRunLabel.setBounds(90, 190, 260, 22);

		window_interface wi = new window_interface();
		this.addWindowListener(wi);

		action_interface action = new action_interface();
		cancel.addActionListener(action);
		defaults.addActionListener(action);
		ok.addActionListener(action);
	}

	void set_num_chrom(int n) {
		numberOfChromosomes = n;
		numChromField.setText(Integer.toString(n));
	}

	void set_num_in_row(int n) {
		numInRow = n;
		numInRowField.setText(Integer.toString(n));
	}

	void set_num_in_col(int n) {
		numInCol = n;
		numInColField.setText(Integer.toString(n));
	}

	/**
	 * "set_num_in_Run( int )" sets the number of steps in Cntrl-V in the dialog
	 * window.
	 *
	 * @return nothing
	 */
	void set_num_in_Run(int n) {
		numInRun = n;
		numInRunField.setText(Integer.toString(n));
	}

	/**
	 * "set_elitism( boolean )" sets the elitism toggle in the dialog window.
	 *
	 * @return nothing
	 */
	void set_elitism(boolean b) {
		elitism = b;
		eliteCB.setState(b);
	}

	/**
	 * "set_mutation( boolean )" sets the mutation toggle in the dialog window.
	 *
	 * @return nothing
	 */
	void set_mutation(boolean b) {
		mutation = b;
		mutateCB.setState(b);
	}

	/**
	 * "get_numberOfChromosomes()" returns updated setting to parent.
	 *
	 * @return nothing
	 */
	int get_numberOfChromosomes() {
		return numberOfChromosomes;
	}

	/**
	 * "get_numInRow()" returns updated setting to parent.
	 *
	 * @return nothing
	 */
	int get_numInRow() {
		return numInRow;
	}

	/**
	 * "get_numInCol()" returns updated setting to parent.
	 *
	 * @return nothing
	 */
	int get_numInCol() {
		return numInCol;
	}

	/**
	 * "get_numInRun()" returns updated setting to parent.
	 *
	 * @return nothing
	 */
	int get_numInRun() {
		return numInRun;
	}

	/**
	 * "get_mutation()" returns updated setting to parent.
	 *
	 * @return nothing
	 */
	boolean get_mutation() {
		return mutation;
	}

	/**
	 * "get_elitism()" returns updated setting to parent.
	 *
	 * @return nothing
	 */
	boolean get_elitism() {
		return elitism;
	}

	/**
	 * "addNotify()" notifies a Component that it has been added to a container.
	 *
	 * @return nothing
	 */
	public void addNotify() {
		Dimension d = getSize(); // get window size prior to
		super.addNotify(); // calling parents addNotify.

		if (notified) // only do this once.
			return;

		Insets in = getInsets(); // adjust according to insets
		setSize(in.left + in.right + d.width, in.top + in.bottom + d.height);

		Component components[] = getComponents(); // list of components

		for (int i = 0; i < components.length; i++) {
			Point p = components[i].getLocation();
			p.translate(in.left, in.top);
			components[i].setLocation(p);
		}

		notified = true; // set flag
	}

	/**
	 * "setVisible( boolean )" shows or hides the component depending on the
	 * boolean flag b.
	 *
	 * @return nothing
	 */
	public void setVisible(boolean b) {
		if (b) {
			Rectangle parent = getParent().getBounds();
			Rectangle child = getBounds();

			setLocation(parent.x + (parent.width - child.width) / 2, parent.y
					+ (parent.height - child.height) / 2);

			Toolkit.getDefaultToolkit().beep();
		}
		super.setVisible(b);
	}

	/**
	 * "action_interface" class for handling user input actions.
	 *
	 * @return nothing
	 */
	class action_interface implements ActionListener {
		/**
		 * actionPerformed(ActionEvent) selects response to user input actions
		 *
		 * @return nothing
		 */
		public void actionPerformed(ActionEvent event) {
			Integer n;
			Integer nr;
			Integer nc;
			Integer nrun;
			Boolean e;
			Boolean m;
			Object object = event.getSource();

			if (object == ok) {
				try {
					n = new Integer(numChromField.getText());
					numberOfChromosomes = n.intValue();

					nr = new Integer(numInRowField.getText());
					numInRow = nr.intValue();

					nc = new Integer(numInColField.getText());
					numInCol = nc.intValue();

					nrun = new Integer(numInRunField.getText());
					numInRun = nrun.intValue();

					e = new Boolean(eliteCB.getState());
					elitism = e.booleanValue();

					m = new Boolean(mutateCB.getState());
					mutation = m.booleanValue();
				} catch (Exception ex) {
				}
				dispose();
			} else if (object == defaults) {
				// strings should agree with initial values in tsp
				numChromField.setText("16");
				numInRowField.setText("4");
				numInColField.setText("4");
				numInRunField.setText("1000");
				eliteCB.setState(true);
				mutateCB.setState(true);
			} else if (object == cancel)
				dispose();
		}
	}

	/**
	 * "window_interface" class for handling system commands.
	 *
	 * @since 01/12/00
	 */
	class window_interface extends WindowAdapter {
		/**
		 * "windowClosing( WindowEvent )" processes a window closing system
		 * command.
		 *
		 * @return nothing
		 */
		public void windowClosing(WindowEvent event) {
			Object object = event.getSource();

			if (object == SettingsDialog.this)
				dispose();
		}
	}
}