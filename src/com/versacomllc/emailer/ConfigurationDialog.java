package com.versacomllc.emailer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class ConfigurationDialog extends JDialog {

	private JTextField tfUsername;
	private JTextField tfUser;
	private JPasswordField pfPassword;
	private JTextField tfSubject;
	private JTextField tfServer;
	private JTextField tfPort;
	private JTextField tfGreeting;
	//private JScrollPane jScrollPane1;
	//private JTextArea textArea;

	private JLabel lbUsername;
	private JLabel lbUser;
	private JLabel lbPassword;
	private JLabel lbServer;
	private JLabel lbPort;
	private JLabel lbSubject;
	private JLabel lbBody;

	private JButton btnSave;
	private JButton btnCancel;

	public ConfigurationDialog(Frame parent) {
		super(parent, "Configuration Dialog", true);
		ConfigurationData conf = null;
		try {
			conf = PersistenceManager.getConfiguration();
		} catch (FileNotFoundException e2) {
			e2.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints cs = new GridBagConstraints();

		cs.fill = GridBagConstraints.HORIZONTAL;

		lbUsername = new JLabel("Sender email: ");
		cs.gridx = 0;
		cs.gridy = 0;
		cs.gridwidth = 1;
		panel.add(lbUsername, cs);

		tfUsername = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 0;
		cs.gridwidth = 2;
		panel.add(tfUsername, cs);

		
		lbUser = new JLabel("User: ");
		cs.gridx = 0;
		cs.gridy = 1;
		cs.gridwidth = 1;
		panel.add(lbUser, cs);

		tfUser = new JTextField(20);
		cs.gridx = 1;
		cs.gridy = 1;
		cs.gridwidth = 2;
		panel.add(tfUser, cs);
		
		
		
		///----
		lbPassword = new JLabel("Email password: ");
		cs.gridx = 0;
		cs.gridy = 2;
		cs.gridwidth = 1;
		panel.add(lbPassword, cs);

		pfPassword = new JPasswordField(20);
		cs.gridx = 1;
		cs.gridy = 2;
		cs.gridwidth = 1;
		panel.add(pfPassword, cs);

		lbServer = new JLabel("Mail server: ");
		cs.gridx = 0;
		cs.gridy = 3;
		cs.gridwidth = 1;
		panel.add(lbServer, cs);

		tfServer = new JTextField(40);
		cs.gridx = 1;
		cs.gridy = 3;
		cs.gridwidth = 1;
		panel.add(tfServer, cs);
		
		lbPort = new JLabel("Port: ");
		cs.gridx = 0;
		cs.gridy = 4;
		cs.gridwidth = 1;
		panel.add(lbPort, cs);

		tfPort = new JTextField(40);
		cs.gridx = 1;
		cs.gridy = 4;
		cs.gridwidth = 1;
		panel.add(tfPort, cs);
		
		lbSubject = new JLabel("Email subject: ");
		cs.gridx = 0;
		cs.gridy = 5;
		cs.gridwidth = 1;
		panel.add(lbSubject, cs);

		tfSubject = new JTextField(50);
		cs.gridx = 1;
		cs.gridy = 5;
		cs.gridwidth = 4;
		panel.add(tfSubject, cs);

		lbBody = new JLabel("Message Greetings prefix: ");
		cs.gridx = 0;
		cs.gridy = 6;
		cs.gridwidth = 1;
		panel.add(lbBody, cs);

		tfGreeting = new JTextField(50);

	

		cs.gridx = 1;
		cs.gridy = 6;
		cs.gridwidth = 5;
		panel.add(tfGreeting, cs);

		if(conf != null){
			tfUsername.setText(conf.getSenderEmail());
			tfUser.setText(conf.getSenderUser());
			tfPort.setText(String.valueOf(conf.getPort()));
			pfPassword.setText(conf.getSenderPass());
			tfSubject.setText(conf.getSubject());
			tfGreeting.setText(conf.getGreeting());
			if(conf.getMailServer() == null || conf.getMailServer().isEmpty()){
				tfServer.setText("dalexch3.versacomllc.com");
			}
			else{
				tfServer.setText(conf.getMailServer());
			}
			
		}
		else{
			tfServer.setText("dalexch3.versacomllc.com");
		}
		panel.setBorder(new LineBorder(Color.GRAY));

		btnSave = new JButton("Save");

		btnSave.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ConfigurationData conf = new ConfigurationData();
				conf.setGreeting(tfGreeting.getText());
				conf.setSubject(tfSubject.getText());
				conf.setSenderPass(String.valueOf(pfPassword.getPassword()));
				conf.setSenderEmail(tfUsername.getText());
				conf.setSenderUser(tfUser.getText());
				conf.setMailServer(tfServer.getText());
				
				conf.setPort(Integer.parseInt(tfPort.getText()));

				try {
					PersistenceManager.saveConfiguration(conf);
					JOptionPane.showMessageDialog(null,
							"Email configuration saved successfully");

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
		bp.add(btnSave);
		bp.add(btnCancel);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(bp, BorderLayout.PAGE_END);

		pack();
		setResizable(false);
		setLocationRelativeTo(parent);

	}

}
