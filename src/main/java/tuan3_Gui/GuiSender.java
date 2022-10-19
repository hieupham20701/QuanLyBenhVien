package tuan3_Gui;

import java.awt.EventQueue;

import javax.swing.JFrame;

import org.apache.log4j.BasicConfigurator;

import tuan3_Data.Student;
import tuan3_Helper.XMLConvert;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class GuiSender extends JFrame implements ActionListener {
	private JTextField textField;
	private JTextArea textArea;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiSender frame = new GuiSender();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	
	public GuiSender() {
		setResizable(false);
		setAlwaysOnTop(true);
		getContentPane().setLayout(null);
		setSize(600,330);
		setTitle("Home Sender");
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(20, 20, 545, 220);
		getContentPane().add(textArea);
		
		textField = new JTextField();
		textField.setBounds(20, 250, 450, 25);
		getContentPane().add(textField);
		textField.setColumns(7);
		
		JButton btnNewButton = new JButton("Gửi");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(480, 250, 85, 25);
		getContentPane().add(btnNewButton);
		
		btnNewButton.addActionListener(this);
		textField.addActionListener(this);
	}

	public void seed() throws Exception {
		BasicConfigurator.configure();
		//config environment for JNDI
		Properties settings = new Properties();
		settings.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
		settings.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
		//create context
		Context ctx = new InitialContext(settings);
		//lookup JMS connection factory
		ConnectionFactory factory = (ConnectionFactory) ctx.lookup("ConnectionFactory");
		//lookup destination. (If not exist-->ActiveMQ create once)
		Destination destination = (Destination) ctx.lookup("dynamicQueues/thanthidet");
		//get connection using credential
		Connection con = factory.createConnection("admin", "admin");
		//connect to MOM
		con.start();
		//create session
		Session session = con.createSession(/* transaction */false, /* ACK */Session.AUTO_ACKNOWLEDGE);
		//create producer
		MessageProducer producer = session.createProducer(destination);
		//create text message
		Message msg = session.createTextMessage("hello mesage from ActiveMQ");
		producer.send(msg);
		try {
				String name = textField.getText();
				Student p = new Student(9999,name, new Date());
				String xml = new XMLConvert<Student>(p).object2XML(p);
				msg = session.createTextMessage(xml);
				producer.send(msg);
				textField.setText("");
				textArea.setText(textArea.getText() + "\n" + name);
				System.out.println(name);

		} finally {
			session.close();
			con.close();
			System.out.println("Finished...");
		}
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		try {
			seed();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
