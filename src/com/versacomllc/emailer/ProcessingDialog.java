package com.versacomllc.emailer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ProcessingDialog extends JDialog implements Observer {

	private JTextField tfStatus;

	private JScrollPane jScrollPane1;
	private JTextArea textArea;

	private JLabel lbUsername;

	private JLabel lbBody;

	private JButton btnProcessing;
	private JButton btnSave;
	private JButton btnCancel;

	private String filePath;

	public ProcessingDialog(Frame parent) {
		super(parent, "Processing Dialog", true);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		lbUsername = new JLabel("Status: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbUsername, cs);

		tfStatus = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfStatus, cs);

		lbBody = new JLabel("Log: ");
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 1;
		panel.add(lbBody, cs);

		textArea = new JTextArea();

		textArea.setColumns(40);
		textArea.setLineWrap(true);
		textArea.setRows(10);
		textArea.setWrapStyleWord(true);

		jScrollPane1 = new JScrollPane(textArea);

		cs.gridx = 1;
		cs.gridy = 4;
		cs.gridwidth = 4;
		panel.add(jScrollPane1, cs);

		panel.setBorder(new LineBorder(Color.GRAY));

		btnProcessing = new JButton("Load");
		btnProcessing.addActionListener(new ActionListener() {
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

		btnSave = new JButton("Run");
		final ProcessingDialog dlg = this;
		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				WorkSheetProcessor processor = new WorkSheetProcessor(filePath);
				ConfigurationData conf;
				try {
					conf = PersistenceManager.getConfiguration();
					EmailNotificationHanlder hanlder2 = new EmailNotificationHanlder(
							conf);

					processor.addObserver(dlg);
					processor.addObserver(hanlder2);

					Thread t = new Thread(processor);
					t.start();

				} catch (IOException e1) {
					e1.printStackTrace();
				}

			}
		});

		btnCancel = new JButton("Close");
		btnCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		JPanel bp = new JPanel();
		bp.add(btnProcessing);
		bp.add(btnSave);
		bp.add(btnCancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);

	}

	@Override
	public void update(Observable arg0, Object obj) {

		if (obj instanceof EmployeeInfo) {
			EmployeeInfo info = (EmployeeInfo) obj;
			textArea.append("\n");
			textArea.append("Processing employee " + info.getFirstName() + " "
					+ info.getLastName());
		}
		if (obj instanceof Result) {
			textArea.append("\n");
			Result res = (Result) obj;
			textArea.append(res.getMessage());
		}
	}

}
