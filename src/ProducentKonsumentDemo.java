/* 
 *  Autor: Artur Kardas - 226148
 *  Klasy:
 *  class ProducentKonsumentDemo-klasa odpowiedzialna za interface graficzny oraz za s³uchaczy zdarzeñ
 *  class Producent-klasa reprezentuj¹ca producentów, dziedziczy klase Thread
 *  class Konsument-klassa reprezentuj¹ca konsumentów, dzidziczy klasê Thread
 *  class Bufor-klasa odpowiedzialna za bufor dla produktów produkowanych
 *   Data: 12.12.2017
 */

import javax.swing.*;
import java.awt.event.*;


public class ProducentKonsumentDemo extends JFrame implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String AUTOR = "Autor: Artur Kardas\n "
								+ "Nr. Albumu: 226148";
	private static String INFO =  "Program demontruj¹cy problem\n"
								+ "KONSUMENT-PRODUCENT\n"
								+ "Posiada trzy pola tekstowe\n"
								+ "w których mo¿emy okreœliæ\n"
								+ "parametry dla liczby konsumentów\n"
								+ "liczby producentów oraz wilkoœæ\n"
								+ "bufora.";
	
	JLabel przebiegSymulacjiLabel = new JLabel ("Przebieg symulacji:");
    JTextArea symulacjaNotScroll = new JTextArea();
	JScrollPane symulacjaScroll;
    JButton startButton = new JButton("Start");
    JButton stopButton = new JButton("Pause");
    JButton wznowButton = new JButton("Resume");
    JLabel producentLabel = new JLabel ("Liczba Produc.:");
    JTextField producentText = new JTextField(10);
    JLabel konsumentLabel = new JLabel ("Liczba Konsum.:");
    JTextField konsumentText = new JTextField(10);
    JLabel buforLabel = new JLabel ("Poj. Bufora:");
    JTextField buforText = new JTextField(10);
    JMenuBar menuBar=new JMenuBar();
    JMenu menu =new JMenu("Informacje");
    JMenuItem help= new JMenuItem("Pomoc");
    JMenuItem autor= new JMenuItem("Autor");
    Bufor c = new Bufor();
    
	Producent producenci[];
	Konsument konsumenci[];

    ProducentKonsumentDemo()
    {	

    	super("ProducentKonsumentDemo");
     	setSize(435,450);
     	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
     	setResizable(false);
     	menu.add(help);
     	menu.add(autor);
     	menuBar.add(menu);
     	
     	setJMenuBar(menuBar);
     	 
     	JPanel panel = new JPanel();
     	panel.setLayout(null);
		symulacjaNotScroll.setEditable(false);

     	symulacjaNotScroll.setToolTipText("Tok:");
    	symulacjaNotScroll.setLineWrap(true);
     	symulacjaScroll = new JScrollPane(symulacjaNotScroll);
 		symulacjaScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
     	
     	przebiegSymulacjiLabel.setBounds(10,120, 130, 20);
     	symulacjaScroll.setBounds(10,150,400,250);
        startButton.setBounds(10,20,130,30);
        stopButton.setBounds(145,20,130,30);
     	wznowButton.setBounds(280,20,130,30);
     	producentLabel.setBounds(30, 62, 130, 20);
     	producentText.setBounds(10,82,130,20);	
     	konsumentLabel.setBounds(165, 62, 130, 20);
     	konsumentText.setBounds(145,82,130,20);	
     	buforLabel.setBounds(295, 62, 130, 20);
		buforText.setBounds(280,82,130,20);	
     	
     	
     	startButton.addActionListener(this); 
     	stopButton.addActionListener(this); 
     	wznowButton.addActionListener(this);  
     	help.addActionListener(this);
     	autor.addActionListener(this);
     	
     	panel.add(przebiegSymulacjiLabel); 
     	panel.add(symulacjaScroll);
     	panel.add(startButton);
     	panel.add(stopButton);
     	panel.add(wznowButton);
     	panel.add(producentLabel); 
     	panel.add(producentText); 
     	panel.add(konsumentLabel); 
     	panel.add(konsumentText);
     	panel.add(buforLabel);
     	panel.add(buforText);
     	
     	setContentPane(panel);  
     	setVisible(true);
    }
 	

 	class Producent extends Thread
	{
		char item = 'A';
		boolean pleaseWait = false;
		Bufor buf;
		int number;
		
 
		public Producent(Bufor c, int number)
		{ 
			buf = c;
			this.number = number;
		}
		
 
		public void run()
		{ 
			char c;
			while(true)
			{
				c = item++;
				buf.put(number, c);
			
				synchronized (this) 
				{
                	while (pleaseWait) 
					try 
					{
						wait();
					} 
					catch (InterruptedException e) { }
				}
				try 
				{
					sleep((int)(Math.random() * 500));
				} 
				catch (InterruptedException e) { }
			}
		}
	}
	
 
	class Konsument extends Thread
	{
 
		Bufor buf;
	    int number;
		boolean pleaseWait = false;
		
 
		public Konsument(Bufor c, int number)
		{ 
			buf = c;
			this.number = number;
		}
		
 
		public void run()
		{ 
			while(true)
			{ 
				buf.get(number);
				synchronized (this) 
				{
                	while (pleaseWait) 
					try 
					{
						wait();
					}
				 	catch (InterruptedException e) { }
				}
				try 
				{
					sleep((int)(Math.random() * 500));
				} 
				catch (InterruptedException e) { }
			}
		}
	}
 
	class Bufor
	{
 
		private int pojemnosc = 1;
 
		private int available = 0;
		private char contents;
	

		public synchronized int get(int kons)
		{
 
			symulacjaNotScroll.append("Konsument #" + kons + " chce zabrac\n");
			System.out.println("Konsument #" + kons + " chce zabrac");
			
 
			while (available == 0)
			{
				try 
				{ 
 
					System.out.println("Konsument #" + kons + "   bufor pusty - czekam");
					symulacjaNotScroll.append("Konsument #" + kons + "   bufor pusty - czekam\n");
					wait();
				} 
				catch (InterruptedException e) { }
			}
 			available--;
 			System.out.println("Konsument #" + kons + "      zabral: " + contents);
			symulacjaNotScroll.append("Konsument #" + kons + "      zabral: " + contents +"\n");
			 
			symulacjaNotScroll.setCaretPosition(symulacjaNotScroll.getDocument().getLength());
			notifyAll();
			return contents;
		}
		
 
		public synchronized void put(int prod, char value)
		{
			System.out.println("Producent #" + prod + "  chce oddac: " + value);
			symulacjaNotScroll.append("Producent #" + prod + "  chce oddac: " + value + "\n");
			
			while (available == pojemnosc)
			{
				try 
				{ 
					 
					System.out.println("Producent #" + prod + "   bufor zajety - czekam");
					symulacjaNotScroll.append("Producent #" + prod + "   bufor zajety - czekam\n");
					wait();
				} 
				catch (InterruptedException e) { }
			}
			
			contents = value;
			 
			available++;
			 
			System.out.println("Producent #" + prod + "       oddal: " + value);
			symulacjaNotScroll.append("Producent #" + prod + "       oddal: " + value + "\n");
			symulacjaNotScroll.setCaretPosition(symulacjaNotScroll.getDocument().getLength());
			notifyAll();
		}
		public void ustawPojemnosc(int poj) 
    	{ 
    		pojemnosc = poj; 
    	}
	}
	
 
	public boolean validateNumber(String num) 
	{
        try 
        {
           	Integer.parseInt(num);
            return true;
        } 
        catch (Exception e) 
        {
            return false;
        }
    }
    
    
 
 
 
 public void actionPerformed (ActionEvent evt)
 { 
    	
  
    	Object zrodlo = evt.getSource();
    	 
    	String ko, pr, pb;
 
     	if (zrodlo==startButton)
        { 
 
			pr=producentText.getText();
			ko=konsumentText.getText();
			pb=buforText.getText();
 
			if(ko.equals("") && pr.equals("") && pb.equals(""))
			{	
				JOptionPane.showMessageDialog(null, "Nie wprowadzono ilosci producentow i konsumentow, oraz pojemnosci bufora");
			}
			else if(ko.equals(""))
			{	
				JOptionPane.showMessageDialog(null, "Nie wprowadzono ilosci konsumentow");
			}
			else if(pr.equals(""))
			{	
				JOptionPane.showMessageDialog(null, "Nie wprowadzono ilosci producentow");
			}
			else if(pb.equals(""))
			{	
				JOptionPane.showMessageDialog(null, "Nie wprowadzono pojemnosci bufora");
			}
 
			else if(!(validateNumber(pr)) || !(validateNumber(ko)))
			{
            	JOptionPane.showMessageDialog(null, "Liczba producentow i konsumentow musi byc calkowita");
        	}
        	else if(!(validateNumber(pb)))
			{
            	JOptionPane.showMessageDialog(null, "Rozmiar bufora musi byc liczba calkowita");
        	}			
			else 
			{
 
				int ileProducentow = Integer.parseInt(pr);
				int ileKonsumentow = Integer.parseInt(ko);
				int pojemnoscBufora = Integer.parseInt(pb);
 
				producenci = new Producent[ileProducentow];
    			konsumenci = new Konsument[ileKonsumentow];
				
				c.ustawPojemnosc(pojemnoscBufora);
 
				for(int i = 0; i<ileProducentow; i++)
				{	
					producenci[i] = new Producent(c, i+1);
				}
				for(int i = 0; i<ileKonsumentow; i++)
				{	
					konsumenci[i] = new Konsument(c, i+1);
				}
				
 
				for(Konsument k : konsumenci)
				{	
					k.start();
				}
				for(Producent p : producenci)
				{	
					p.start();
				}
			}
        } 
        
 
        if (zrodlo==stopButton)
        { 
 
           	for(Producent p : producenci)
			{
           		synchronized (p) 
           		{
        			p.pleaseWait = true;
    			}
			}
    		
    		for(Konsument k : konsumenci)
			{
           		synchronized (k) 
           		{
        			k.pleaseWait = true;
    			}
			}
        } 
        	
 
        if (zrodlo==wznowButton)
        {   
 
			for(Producent p : producenci)
			{
           		synchronized (p) 
           		{
       				p.pleaseWait = false;
        			p.notify();
    			}
			}
			
			for(Konsument k : konsumenci)
			{
           		synchronized (k) 
           		{
       				k.pleaseWait = false;
        			k.notify();
    			}
			}		
        } 
        if(zrodlo==autor)
        {
        	JOptionPane.showMessageDialog(null, AUTOR);
        	
        }
        if(zrodlo==help)
        {
        	JOptionPane.showMessageDialog(null, INFO);
        }
    }
   
 
    static  public void main(String arg[])
    { 
    	new  ProducentKonsumentDemo();      
    } 
 }