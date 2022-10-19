package tuan3_Gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.BasicConfigurator;

public class GuiReceiver extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiReceiver frame = new GuiReceiver();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GuiReceiver() {
		setResizable(false);
		setAlwaysOnTop(true);
		getContentPane().setLayout(null);
		setSize(600,330);
		setTitle("Home Receiver");
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(20, 20, 545, 220);
		getContentPane().add(textArea);

		textField = new JTextField();
		textField.setBounds(20, 250, 450, 25);
		getContentPane().add(textField);
		textField.setColumns(10);

		JButton btnNewButton = new JButton("Gửi");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(480, 250, 85, 25);
		getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(this);
		try {
			receiver();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public void receiver() throws Exception {
		BasicConfigurator.configure();
		
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		
		Context ctx = new InitialContext(settings);
		
		Object obj = ctx.lookup("ConnectionFactory");
		ConnectionFactory factory = (ConnectionFactory) obj;
		
		
		Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
		
		Connection con = factory.createConnection("admin", "admin");
		
		con.start();
		
		Session session = con.createSession(/* transaction */false, /* ACK */Session.CLIENT_ACKNOWLEDGE);
		
		MessageConsumer receiver = session.createConsumer(destination);
	
		receiver.setMessageListener(new MessageListener() {

			
			public void onMessage(Message msg) {
				try {
					if (msg instanceof TextMessage) {
						TextMessage tm = (TextMessage) msg;
						String txt = tm.getText();
						System.out.println("Đã nhận: " + txt);

						int indexStart = txt.indexOf("<hoten>");
						int indexEnd = txt.indexOf("</hoten>");
						int indexMSStart = txt.indexOf("<mssv>");
						int indexMSEnd = txt.indexOf("</mssv>");
						System.out.println("index " + indexStart);
							System.out.println(txt);
							String textMS = txt.substring(indexMSStart, indexMSEnd);
							String text = txt.substring(indexStart + 7, indexEnd);
							text.replaceAll("<hoten>", "");
							textMS.replaceAll("<mssv>", "");
							textArea.append("\nLoc: " + text);
						
						msg.acknowledge();
					} else if (msg instanceof ObjectMessage) {
						ObjectMessage om = (ObjectMessage) msg;
						System.out.println(om);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}
