package xml_to_csv;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;


/*
 * GU 8/11/22
 * Class utilizzata da P2mDecoder per aprire uno chooser con l'utente e selezionare
 * la cartella/directory dove verranno analizzati i file XML
 */

public class JavaFileChooser extends JPanel
	implements ActionListener {
	
	public static final String title = "Software";
	private static final long serialVersionUID = 1L;
	private static Color  lightblue  = new Color(173, 216, 230);
	//static ImageIcon img = new ImageIcon("src/resources/favicon.png");
	
	JButton go;
	JTextArea description;
	JTextArea description2;
	
	JFileChooser chooser;
	String choosertitle;
	
	private static Object lock = new Object();
	private static JFrame frame = new JFrame();
	/* -------------------------------------------------- */
	
	public JavaFileChooser() {
		description = new JTextArea("Seleziona cartella contenente file da convertire:");
		description.setBackground(lightblue);
		description.setEditable(false);
		add(description);
		
		go = new JButton("Seleziona percorso");
	    go.addActionListener(this);
	    go.setBackground(Color.white);
	    add(go);
	    
	    description2 = new JTextArea("Il file Riepilogo Fatture verr� creato nella cartella selezionata");
		description2.setEditable(false);
		description2.setBackground(lightblue);
		add(description2);
	}
	
	
	public void actionPerformed(@SuppressWarnings("exports") ActionEvent e) {            
		 chooser = new JFileChooser(); 
		 chooser.setCurrentDirectory(new java.io.File("."));
		 chooser.setDialogTitle(title); //finestra di navigazione cartelle
		 chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);		 
		 chooser.setAcceptAllFileFilterUsed(false); // disable the "All files" option.
		 
		 if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			   System.out.println("getCurrentDirectory(): "+ chooser.getCurrentDirectory());
			   System.out.println("getSelectedFile() : "+ chooser.getSelectedFile());
			   P7mDecoder.folder = chooser.getSelectedFile();
			   
			   frame.setVisible(false);
			   synchronized (lock) {
	                frame.setVisible(false);
	                lock.notify();
	           }
		 } else {
			   System.out.println("No Selection");
			   P7mDecoder.folder = null;
			   
			   frame.setVisible(false);
			   synchronized (lock) {
	                frame.setVisible(false);
	                lock.notify();
	           }
		 }
	}
	
	
	@SuppressWarnings("exports")
	public Dimension getPreferredSize(){
		return new Dimension(350, 150);
	}
	
	
	public static void createAndShowGUI() throws InterruptedException {
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    JavaFileChooser panel = new JavaFileChooser();
		frame.getContentPane().add(panel,"Center");
		frame.getContentPane().setForeground(lightblue);
		frame.setSize(panel.getPreferredSize());
		frame.setResizable(false);
		frame.setTitle(title);
		//frame.setIconImage(img.getImage());

	    frame.setVisible(true);

	    Thread t = new Thread() {
	        public void run() {
	            synchronized(lock) {
	                while (frame.isVisible())
	                    try {
	                        lock.wait();
	                    } catch (InterruptedException e) {
	                        e.printStackTrace();
	                    }
	                System.out.println("User interface Working now");
	            }
	        }
	    };
	    t.start();
	    t.join();
		
		System.out.println("end - User interface");		 
	}
	
}
