import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Main {

	private Resource[] totalResources, availableResources;
	private Resource[][] allocatedProcesses, maxProcesses;
	private int numProcesses;
	private int numResources;
	private JFrame frame;

	public Main() {
		numProcesses=0;
		numResources=0;
		makeFrame();
	}

	private void makeFrame() {

		frame = new JFrame();
		frame.setSize(800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(firstPanel());
		frame.setVisible(true);

	}

	public JPanel firstPanel(){

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.weightx = 0.5;
		c.weighty = 1;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("How many processes are there?"),c);

		c.gridx = 1;
		c.gridy = 0;
		JTextField numProcessesField = new JTextField();
		panel.add(numProcessesField, c);

		c.gridx = 0;
		c.gridy = 1;
		panel.add(new JLabel("How many types of resources are there?"),c);

		c.gridx = 1;
		c.gridy = 1;
		JTextField numResourcesField = new JTextField();
		panel.add(numResourcesField, c);

		JButton accept = new JButton("Accept");
		c.weightx = 1;
		c.gridwidth=2;
		c.gridx = 0;
		c.gridy = 3;
		panel.add(accept,c);

		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				numResourcesField.setText(numResourcesField.getText().replaceAll("[^0-9]",""));
				numProcessesField.setText(numProcessesField.getText().replaceAll("[^0-9]",""));
				if(numResourcesField.getText().isEmpty()||Integer.parseInt(numResourcesField.getText())<1)
					JOptionPane.showMessageDialog(frame, "The number of resources must be a number greater than 0.");
				else if(numProcessesField.getText().isEmpty()||Integer.parseInt(numProcessesField.getText())<1)
					JOptionPane.showMessageDialog(frame, "The number of processes must be a number greater than 0.");
				else{
					numProcesses = Integer.parseInt(numProcessesField.getText());
					numResources = Integer.parseInt(numResourcesField.getText());
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(secondPanel());
				}
			}});

		return panel;

	}

	private JPanel secondPanel() {

		totalResources = new Resource[numResources];
		allocatedProcesses = new Resource[numProcesses][numResources];
		maxProcesses = new Resource[numProcesses][numResources];
		availableResources = new Resource[numResources];

		for(int i = 0;i<numProcesses;i++){
			for(int j = 0;j<numResources;j++){
				allocatedProcesses[i][j] = new Resource(Character.toString((char) (65+i))+"",0);
				maxProcesses[i][j] = new Resource(Character.toString((char) (65+i))+"",0);
			}
		}

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth=numResources;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx=0;
		c.gridy=0;
		panel.add(new JLabel("How many instances of each resource are there?"),c);

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		c.gridwidth=1;

		for(int i = 0;i<numResources;i++){
			c.gridy = 1;
			c.gridx = i;
			panel.add(new JLabel(Character.toString((char) (65+i))),c);
			c.gridy = 2;
			JTextField dummy = new JTextField();
			panel.add(dummy,c);
			fields.add(dummy);
		}

		JButton accept = new JButton("Accept");
		c.weightx = 1;
		c.gridwidth=numResources;
		c.gridx = 0;
		c.gridy = 3;
		panel.add(accept,c);

		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				for(int i = 0; i<numResources;i++){
					fields.get(i).setText(fields.get(i).getText().replaceAll("[^0-9]",""));
					if(fields.get(i).getText().isEmpty()||Integer.parseInt(fields.get(i).getText())<1){
						JOptionPane.showMessageDialog(frame, "The number of instances must be a number greater than 0.");
						i=numResources;
						error = true;
					}
				}
				if(!error){
					for(int i = 0; i<numResources;i++){
						totalResources[i] = new Resource(Character.toString((char) (65+i)), Integer.parseInt(fields.get(i).getText()));
						fields.get(i).setText(fields.get(i).getText().replaceAll("[^0-9]",""));
					}
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(mainPanel());
				}	
			}
		});

		return panel;
	}

	private JPanel mainPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth=1;
		c.fill = GridBagConstraints.HORIZONTAL;
		if(numProcesses==0){
			c.gridx=0;
			c.gridy=0;
			panel.add(new JLabel("All Processes Terminated"),c);
		}
		else{

			JButton declareMaxResourceUsageButton = new JButton("Declare max resource usage");
			c.gridx=0;
			c.gridy=0;
			panel.add(declareMaxResourceUsageButton, c);

			declareMaxResourceUsageButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(declareMaxResourceUsagePanel());
				}

			});

			JButton requestResourcesButton = new JButton("Request Resources");
			c.gridx=0;
			c.gridy=1;
			panel.add(requestResourcesButton, c);

			requestResourcesButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(requestResourcesPanel());
				}

			});

			JButton releaseResourcesButton = new JButton("Release Recources");
			c.gridx=0;
			c.gridy=2;
			panel.add(releaseResourcesButton, c);

			releaseResourcesButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(releaseResourcesPanel());
				}

			});

			JButton terminateButton = new JButton("Terminate a process");
			c.gridx=0;
			c.gridy=3;
			panel.add(terminateButton, c);

			terminateButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(terminatePanel());
				}});

			c.gridwidth=numResources;
			c.gridx=1;
			c.gridy=0;
			panel.add(new JLabel("Total Resources in System"), c);

			c.gridwidth=1;

			for(int i=1;i<3;i++){
				c.gridy=i;
				for(int j=1;j<1+numResources;j++){
					c.gridx=j;
					if(i==1)
						panel.add(new JLabel(Character.toString((char)(64+j))),c);
					else
						panel.add(new JLabel(""+totalResources[j-1].getNumber()),c);
				}
			}

			c.gridwidth=numResources;
			c.gridx=1;
			c.gridy=3;
			panel.add(new JLabel("Available System Resources"), c);

			c.gridwidth=1;
			for(int i=4;i<6;i++){
				c.gridy=i;
				for(int j=1;j<numResources+1;j++){
					c.gridx=j;
					if(i==4)
						panel.add(new JLabel(Character.toString((char)(64+j))),c);
					else{
						int dummySum = 0;
						for(int k =0; k<numProcesses;k++){
							dummySum+=allocatedProcesses[k][j-1].getNumber();
						}
						dummySum = totalResources[j-1].getNumber()-dummySum;
						availableResources[j-1] = new Resource((char)(64+j)+"",dummySum);
						panel.add(new JLabel(""+dummySum),c);
					}
				}
			}

			c.gridwidth=numResources;
			c.gridx=1;
			c.gridy=6;
			panel.add(new JLabel("Processes (currently allocated resources):"), c);

			c.gridwidth=1;
			for(int i=7;i<8+numProcesses;i++){
				c.gridy=i;
				for(int j=1;j<numResources+2;j++){
					c.gridx=j;
					if(i==7&&j!=1)
						panel.add(new JLabel(Character.toString((char)(63+j))),c);
					else if(i!=7&&j==1){
						int dum = i-8;
						panel.add(new JLabel("P"+dum),c);
					}
					else if(i!=7&&j!=1){
						panel.add(new JLabel(allocatedProcesses[i-8][j-2].getNumber()+""),c);
					}
				}
			}

			c.gridwidth=numResources;
			c.gridx=1;
			c.gridy=8+numProcesses;
			panel.add(new JLabel("Processes (maximum resources):"), c);

			c.gridwidth=1;
			for(int i=9+numProcesses;i<10+2*numProcesses;i++){
				c.gridy=i;
				for(int j=1;j<numResources+2;j++){
					c.gridx=j;
					if(i==9+numProcesses&&j!=1)
						panel.add(new JLabel(Character.toString((char)(63+j))),c);
					else if(i!=9+numProcesses&&j==1){
						int dum = i-(10+numProcesses);
						panel.add(new JLabel("P"+dum),c);
					}
					else if(i!=9+numProcesses&&j!=1){
						panel.add(new JLabel(maxProcesses[i-(10+numProcesses)][j-2].getNumber()+""),c);
					}
				}
			}

			c.gridwidth=numResources;
			c.gridx=1;
			c.gridy=10+2*numProcesses;
			panel.add(new JLabel("Processes (need resources):"), c);

			c.gridwidth=1;
			for(int i=11+2*numProcesses;i<12+3*numProcesses;i++){
				c.gridy=i;
				for(int j=1;j<numResources+2;j++){
					c.gridx=j;
					if(i==11+2*numProcesses&&j!=1)
						panel.add(new JLabel(Character.toString((char)(63+j))),c);
					else if(i!=11+2*numProcesses&&j==1){
						int dum = i-(12+2*numProcesses);
						panel.add(new JLabel("P"+dum),c);
					}
					else if(i!=9+numProcesses&&j!=1){
						panel.add(new JLabel((maxProcesses[i-(12+2*numProcesses)][j-2].getNumber()-(allocatedProcesses[i-(12+2*numProcesses)][j-2].getNumber()))+""),c);
					}
				}
			}
		}

		return panel;
	}

	private JPanel terminatePanel() {

		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth=1;
		c.fill = GridBagConstraints.HORIZONTAL;


		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Which process would you like to terminate?"),c);
		
		c.gridx = 1;

		JComboBox<String> processes = new JComboBox<String>();
		for(int i = 0;i<numProcesses;i++){
			processes.addItem("P"+i);
		}
		panel.add(processes,c);
		
		JButton accept = new JButton("Accept");
		c.gridx = 0;
		c.gridy = 1;
		panel.add(accept,c);
		
		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int selectedIndex = processes.getSelectedIndex();
				numProcesses--;
				for(int i = selectedIndex;i<numProcesses;i++){
					for(int j = 0; j<numResources;j++){
						maxProcesses[i][j].setResourceType(maxProcesses[i+1][j].getResourceType());
						maxProcesses[i][j].setNumber(maxProcesses[i+1][j].getNumber());
						allocatedProcesses[i][j].setResourceType(allocatedProcesses[i+1][j].getResourceType());
						allocatedProcesses[i][j].setNumber(allocatedProcesses[i+1][j].getNumber());
					}
				}
				panel.setVisible(false);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel());
			}

		});

		JButton cancel = new JButton("Cancel");
		c.gridx = 0;
		c.gridy = 2;
		panel.add(cancel,c);

		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(false);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel());
			}

		});
		
		return panel;
	}

	private JPanel requestResourcesPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth=1;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridwidth=numResources-2;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Which process would you like to request resources for?"),c);

		c.gridwidth=1;
		c.gridx = numProcesses-1;

		JComboBox<String> processes = new JComboBox<String>();
		for(int i = 0;i<numProcesses;i++){
			processes.addItem("P"+i);
		}
		panel.add(processes,c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth=numResources;
		panel.add(new JLabel("How many instances of each resources would you like to request for this process?"),c);

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		c.gridwidth=1;

		for(int i = 0;i<numResources;i++){
			c.gridy = 2;
			c.gridx = i;
			panel.add(new JLabel(Character.toString((char) (65+i))),c);
			c.gridy = 3;
			JTextField dummy = new JTextField();
			panel.add(dummy,c);
			fields.add(dummy);
		}

		JButton accept = new JButton("Accept");
		c.weightx = 1;
		c.gridwidth = numProcesses;
		c.gridx = 0;
		c.gridy = 4;
		panel.add(accept,c);

		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				int selectedIndex = processes.getSelectedIndex();
				for(int i = 0; i<numResources;i++){
					fields.get(i).setText(fields.get(i).getText().replaceAll("[^0-9]",""));
					if(fields.get(i).getText().isEmpty())
						fields.get(i).setText(0+"");
					int pInt = Integer.parseInt(fields.get(i).getText()); 
					int dummySum = 0;
					for(int k =0; k<numProcesses;k++){
						dummySum+=allocatedProcesses[k][i].getNumber();
					}
					dummySum = totalResources[i].getNumber()-dummySum;
					if(pInt+allocatedProcesses[selectedIndex][i].getNumber()>maxProcesses[selectedIndex][i].getNumber()){
						i=numResources;
						error=true;
						JOptionPane.showMessageDialog(frame, "The request for resources cannot exceed the processes max storage of that resource.");
					} 
					else if (pInt>dummySum){
						i=numResources;
						error=true;
						JOptionPane.showMessageDialog(frame, "The request for resources cannot exceed the amount of available resource.");
					}
				}
				//BANKER'S ALGROTHIM
				if(!error){
					Resource dummyAvailableResources[] = new Resource[numResources];
					Resource dummyAllocatedProcesses[][] = new Resource[numProcesses][numResources];

					for(int i =0;i<numResources;i++){
						for(int j = 0;j<numProcesses;j++){
							Resource ap = allocatedProcesses[j][i]; 
							dummyAllocatedProcesses[j][i]= new Resource(ap.getResourceType(),ap.getNumber());
						}
						int rInt = Integer.parseInt(fields.get(i).getText());
						dummyAvailableResources[i] = new Resource(((char)(65+1))+"", availableResources[i].getNumber()-rInt);
						dummyAllocatedProcesses[selectedIndex][i].setNumber(allocatedProcesses[selectedIndex][i].getNumber()+rInt);;
					}
					boolean loop=true;
					int p = 0, safeTotal = 0, newSafeTotal = 0;
					ArrayList<Integer> terminatedProcesses = new ArrayList<Integer>();
					while(loop){
						boolean terminate = true;
						if(!terminatedProcesses.contains(p)){
							//System.out.println("Testing p"+p);
							for(int r = 0;r<numResources;r++){
								if(dummyAllocatedProcesses[p][r].getNumber()+dummyAvailableResources[r].getNumber()<maxProcesses[p][r].getNumber()){
									//System.out.println("not enough "+r);
									terminate=false;
									r=numResources;
								}
							}
							if(terminate){
								safeTotal++;
								//System.out.println(safeTotal);
								for(int r = 0;r<numResources;r++){
									dummyAvailableResources[r].setNumber(dummyAvailableResources[r].getNumber()+maxProcesses[p][r].getNumber());
								}
								terminatedProcesses.add(p);
								//System.out.println("P"+p+" Terminated");

							}
						}
						p++;
						if(p==numProcesses){
							if((safeTotal-newSafeTotal)==0){
								error=true;
								loop=false;
								//System.out.println("unsafe");
								JOptionPane.showMessageDialog(frame, "Unsafe Request.");
							}
							else{
								newSafeTotal = safeTotal;
								p=0;
							}
						}
						if(terminatedProcesses.size()==numProcesses)
							loop=false;
					}
				}
				if(!error)
					for(int j = 0; j<numResources;j++){
						int pInt = Integer.parseInt(fields.get(j).getText());
						allocatedProcesses[selectedIndex][j].setNumber(allocatedProcesses[selectedIndex][j].getNumber()+pInt);
						panel.setVisible(false);
						frame.getContentPane().removeAll();
						frame.getContentPane().add(mainPanel());
					}
			}

		});

		JButton cancel = new JButton("Cancel");
		c.gridx = 0;
		c.gridy = 5;
		panel.add(cancel,c);

		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(false);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel());
			}

		});

		return panel;
	}

	private JPanel releaseResourcesPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth=1;
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridwidth=numResources-2;
		c.gridx = 0;
		c.gridy = 0;
		panel.add(new JLabel("Which process would you like to release resources from?"),c);

		c.gridwidth=1;
		c.gridx = numResources-1;

		JComboBox<String> processes = new JComboBox<String>();
		for(int i = 0;i<numProcesses;i++){
			processes.addItem("P"+i);
		}
		panel.add(processes,c);

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth=numResources;
		panel.add(new JLabel("How many instances of each resources would you like to request for that process?"),c);

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		c.gridwidth=1;

		for(int i = 0;i<numResources;i++){
			c.gridy = 2;
			c.gridx = i;
			panel.add(new JLabel(Character.toString((char) (65+i))),c);
			c.gridy = 3;
			JTextField dummy = new JTextField();
			panel.add(dummy,c);
			fields.add(dummy);
		}

		JButton accept = new JButton("Accept");
		c.weightx = 1;
		c.gridwidth = numProcesses;
		c.gridx = 0;
		c.gridy = 4;
		panel.add(accept,c);

		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				int selectedIndex = processes.getSelectedIndex();
				for(int i = 0; i<numResources;i++){
					fields.get(i).setText(fields.get(i).getText().replaceAll("[^0-9]",""));
					if(fields.get(i).getText().isEmpty())
						fields.get(i).setText(0+"");
					int pInt = Integer.parseInt(fields.get(i).getText()); 
					if(pInt>allocatedProcesses[selectedIndex][i].getNumber()){
						i=numResources;
						error=true;
						JOptionPane.showMessageDialog(frame, "The request for resources cannot exceed the processes max storage of that resource.");
					} 
				}
				if(!error)
					for(int j = 0; j<numResources;j++){
						int pInt = Integer.parseInt(fields.get(j).getText());
						allocatedProcesses[selectedIndex][j].setNumber(allocatedProcesses[selectedIndex][j].getNumber()-pInt);
						panel.setVisible(false);
						frame.getContentPane().removeAll();
						frame.getContentPane().add(mainPanel());
					}
			}

		});

		JButton cancel = new JButton("Cancel");
		c.gridx = 0;
		c.gridy = 5;
		panel.add(cancel,c);

		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(false);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel());
			}

		});

		return panel;
	}

	private JPanel declareMaxResourceUsagePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.weighty = 1;
		c.weightx = 1;
		c.gridwidth=1;
		c.fill = GridBagConstraints.HORIZONTAL;

		ArrayList<JTextField> fields = new ArrayList<JTextField>();
		for(int i=0;i<1+numProcesses;i++){
			c.gridy=i;
			for(int j=0;j<numResources+1;j++){
				c.gridx=j;
				if(i==0&&j!=0)
					panel.add(new JLabel(Character.toString((char)(65+j))),c);
				else if(i!=0&&j==0){
					int dum = i;
					panel.add(new JLabel("P"+dum),c);
				}
				else if(i!=0&&j!=0){
					JTextField dummy = new JTextField();
					dummy.setText(maxProcesses[i-1][j-1].getNumber()+"");
					fields.add(dummy);
					panel.add(dummy,c);
				}
			}
		}

		JButton accept = new JButton("Accept");
		c.weightx = 1;
		c.gridwidth = numResources+1;
		c.gridx = 0;
		c.gridy = 2 + numProcesses;
		panel.add(accept,c);

		accept.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				boolean error = false;
				for(int i = 0; i<fields.size();i++){
					fields.get(i).setText(fields.get(i).getText().replaceAll("[^0-9]",""));
					if(fields.get(i).getText().isEmpty())
						fields.get(i).setText(0+"");
					else if(Integer.parseInt(fields.get(i).getText())>totalResources[i%numResources].getNumber()){
						JOptionPane.showMessageDialog(frame, "A process' max resource allocation cannot be greater than the total amount of that resource.");
						i=fields.size();
						error = true;
					}
				}
				if(!error){
					for(int i = 0; i<fields.size();i++){

						int cool = Math.floorDiv(i, numResources);
						int alright = i%numResources;
						String jeffery = fields.get(i).getText(); 
						//System.out.println("\ni:"+i);
						//System.out.println("proc:"+cool);
						//System.out.println("reso:"+alright);
						//System.out.println("jeffery:"+jeffery);
						
						maxProcesses[cool][alright].setNumber(Integer.parseInt(fields.get(i).getText()));
					}
					panel.setVisible(false);
					frame.getContentPane().removeAll();
					frame.getContentPane().add(mainPanel());
				}
			}
		});

		JButton cancel = new JButton("Cancel");
		c.gridy = 3 + numProcesses;
		panel.add(cancel,c);

		cancel.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				panel.setVisible(false);
				frame.getContentPane().removeAll();
				frame.getContentPane().add(mainPanel());
			}

		});

		return panel;
	}

	public static void main(String[] args) {
		new Main();
	}

}
