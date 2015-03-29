
import java.awt.Button;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * "about_dlg" explains the purpose of the application to the user.
 *
 * @version 1.0
 * @since 01/12/99
 */
class AboutDialog extends Dialog {

	private static final long serialVersionUID = 8327424462983350563L;

	boolean notified = false; // reset addNotify flag

	Label label1 = new Label();
	Label label2 = new Label();
	Label label3 = new Label();

	Button okButton = new Button();

	/**
	 * "about_dlg( Frame, boolean )"
	 *
	 * @return nothing
	 */
	public AboutDialog(Frame parent, boolean modal) {
		super(parent, modal);

		setLayout(null);
		setSize(250, 150);
		setVisible(false);

		label1.setText("A Java Application for visualizing");
		add(label1);
		label1.setBounds(20, 20, 210, 22);

		label2.setText("a Genetic Algorithm.");
		add(label2);
		label2.setBounds(20, 40, 210, 22);

		label3.setText("December, 2004");
		add(label3);
		label3.setBounds(20, 80, 210, 22);

		okButton.setLabel("OK");
		add(okButton);
		okButton.setBounds(85, 120, 80, 24);

		setTitle("Traveling Salesman - About");

		window_interface wi = new window_interface();
		this.addWindowListener(wi);
		action_interface action = new action_interface();
		okButton.addActionListener(action);
	}

	/**
	 * "addNotify()" notifies a Component that it has been added to a container.
	 *
	 * @return nothing
	 */
	public void addNotify() {
		Dimension d = getSize(); // get size window size prior to
		super.addNotify(); // calling parents addNotify.

		if (notified) // only do this once
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
			// get size and location of parent and dialog
			Rectangle parent = getParent().getBounds();
			Rectangle child = getBounds();

			// center this dialog
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

		public void actionPerformed(ActionEvent event) {
			Object object = event.getSource();

			if (object == okButton)
				dispose();
		}
	}

	/**
	 * "window_interface" class for handling system commands.
	 *
	 * @since 01/12/00
	 */
	class window_interface extends WindowAdapter {

		public void windowClosing(WindowEvent event) {
			Object object = event.getSource();

			if (object == AboutDialog.this)
				dispose();
		}
	}
}
