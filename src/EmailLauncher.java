
import static javax.swing.JOptionPane.ERROR_MESSAGE;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.versacomllc.emailer.ConfigurationData;
import com.versacomllc.emailer.ConfigurationDialog;
import com.versacomllc.emailer.EmailNotificationHanlder;
import com.versacomllc.emailer.EmployeeInfo;
import com.versacomllc.emailer.PersistenceManager;
import com.versacomllc.emailer.Result;
import com.versacomllc.emailer.WorkSheetProcessor;

@SuppressWarnings("serial")
public class EmailLauncher extends JPanel implements Observer {
	protected JButton b1, btnLoad, btnRun;
	private JTextArea textArea;
	private String filePath;
	private int total =0;
	private int counter =0;
	public EmailLauncher() {
		ImageIcon leftButtonIcon = createImageIcon("images/ic_action_gear.png");

		ImageIcon rightButtonIcon = createImageIcon("images/ic_action_upload.png");

		ImageIcon runIcon = createImageIcon("images/ic_action_flash.png");

		b1 = new JButton("Email Sender Configuration", leftButtonIcon);

		b1.setMnemonic(KeyEvent.VK_E);
		b1.setActionCommand("disable");

		btnLoad = new JButton("Load Excel File", rightButtonIcon);
		// Use the default text position of CENTER, TRAILING (RIGHT).
		btnLoad.setMnemonic(KeyEvent.VK_L);
		btnLoad.setActionCommand("enable");

		btnRun = new JButton("Send Email Notification", runIcon);
		// Use the default text position of CENTER, TRAILING (RIGHT).
		btnRun.setMnemonic(KeyEvent.VK_R);
		btnRun.setActionCommand("enable");
		final EmailLauncher dlg = this;
		btnRun.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (filePath == null || filePath.isEmpty()) {
					JOptionPane
							.showMessageDialog(
									dlg,
									"You should load the Excel file before start processing",
									"Warning", JOptionPane.WARNING_MESSAGE);
					return;
				}

				WorkSheetProcessor processor = new WorkSheetProcessor(filePath);
				ConfigurationData conf;
				try {
					conf = PersistenceManager.getConfiguration();

					/** Verify configuration */
					if (conf == null) {
						JOptionPane
								.showMessageDialog(
										dlg,
										"Unable to load email configuration file. Please setup email configuration",
										"Warning", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if(isNull(conf.getMailServer()) ){
						JOptionPane
						.showMessageDialog(
								dlg,
								"Mail server configuration is missing! Please check email configuration",
								"Warning", JOptionPane.WARNING_MESSAGE);
							return;
					}
					if(isNull(conf.getSenderUser())){
						JOptionPane
						.showMessageDialog(
								dlg,
								"Sender email address is missing! Please check email configuration",
								"Warning", JOptionPane.WARNING_MESSAGE);
							return;
					}		
					if( isNull(conf.getSubject())){
						JOptionPane
						.showMessageDialog(
								dlg,
								"Email subject is missing! Please check email configuration",
								"Warning", JOptionPane.WARNING_MESSAGE);
							return;
					}
					
					EmailNotificationHanlder hanlder2 = new EmailNotificationHanlder(
							conf);

					processor.addObserver(dlg);
					processor.addObserver(hanlder2);

					Thread t = new Thread(processor);
					t.start();

				} catch (IOException e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(dlg, "" + e1.getMessage(),
							"Error", ERROR_MESSAGE);
				}

			}
		});

		// Listen for actions on buttons 1 and 3.
		b1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ConfigurationDialog dialog = new ConfigurationDialog(null);
				dialog.setVisible(true);

			}
		});

		btnLoad.addActionListener(new ActionListener() {
			final JFileChooser fileChooser = new JFileChooser();

			@Override
			public void actionPerformed(ActionEvent e) {
				int returnVal = fileChooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					filePath = file.getAbsolutePath();
					// tfStatus.setText("Loaded");
					if (filePath != null && !filePath.isEmpty()) {
						textArea.append("File loaded from: " + filePath);
					}

				} else {
					System.out.println("File access cancelled by user.");
				}

			}
		});

		b1.setToolTipText("Click this button to configure email settings.");

		btnLoad.setToolTipText("Click this button to load excel file.");
		btnRun.setToolTipText("Click this button to send email notification.");

		// Add Components to this container, using the default FlowLayout.
		// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new BorderLayout());
		JPanel headerPanel = new JPanel(new BorderLayout());
		JLabel header = new JLabel("Auto email notification");
		header.setAlignmentX(Component.CENTER_ALIGNMENT);
		headerPanel.add(header);

		add(headerPanel, BorderLayout.PAGE_START);
		textArea = new JTextArea();

		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(10);
		textArea.setWrapStyleWord(true);

		JScrollPane jScrollPane1 = new JScrollPane(textArea);
		add(jScrollPane1, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();

		buttonPanel.add(b1);
		buttonPanel.add(btnLoad);
		buttonPanel.add(btnRun);

		add(buttonPanel, BorderLayout.PAGE_END);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = EmailLauncher.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
	private static void createAndShowGUI() {

		// Create and set up the window.
		JFrame frame = new JFrame("Versacom Emailer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create and set up the content pane.
		EmailLauncher newContentPane = new EmailLauncher();
		newContentPane.setOpaque(true); // content panes must be opaque
		frame.setContentPane(newContentPane);

		// Display the window.
		frame.pack();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		/* Use an appropriate Look and Feel */
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			// UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		/* Turn off metal's use of bold fonts */
		UIManager.put("swing.boldMetal", Boolean.FALSE);
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	@Override
	public void update(Observable arg0, Object obj) {
		if (obj instanceof EmployeeInfo) {
			EmployeeInfo info = (EmployeeInfo) obj;
			counter ++;
			textArea.append("\n");
			if(info.isSent()){
				textArea.append("Sending email to employee " + info.getFirstName() + " "
						+ info.getLastName());
			}
			else{
				textArea.append("Skipped: " + info.getFirstName() + " "
						+ info.getLastName());
			}

		}
		if (obj instanceof Result) {
			textArea.append("\n");
			Result res = (Result) obj;
		
			if (res.getCode() == 2) {
				total = Integer.parseInt(res.getMessage());
			}
			if(counter >= total){
				textArea.append("\n");
				textArea.append("Processing finished.");
			}
			else{
				textArea.append(res.getMessage());
			}
		}

	}
	private static boolean isNull(String text){
		return (text == null || text.isEmpty());
	}
}