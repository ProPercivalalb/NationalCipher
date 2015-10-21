package nationalcipher;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javalibrary.ForceDecryptManager;
import javalibrary.IForceDecrypt;
import javalibrary.Output;
import javalibrary.cipher.ColumnarRow;
import javalibrary.cipher.auto.ColumnarAuto;
import javalibrary.cipher.stats.StatCalculator;
import javalibrary.cipher.stats.StatisticType;
import javalibrary.cipher.stats.TraverseTree;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.ChiSquared;
import javalibrary.fitness.QuadgramStats;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.listener.CustomMouseListener;
import javalibrary.math.ArrayHelper;
import javalibrary.math.MathHelper;
import javalibrary.math.Rounder;
import javalibrary.math.Statistics;
import javalibrary.math.Units.Time;
import javalibrary.string.LetterCount;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.swing.FrameUtil;
import javalibrary.swing.ImageUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.swing.SwingHelper;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import javalibrary.thread.Threads;
import javalibrary.util.MapHelper;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * @author Alex Barter (10AS)
 */
@SuppressWarnings("serial")
public class UIDefinition extends JFrame {

	public Timer threadTimer;
	public Thread thread;
	
	public Dimension lastSize;
	public Point lastLocation;
	
	public JTextArea output;
	public JTextArea input;
	
	public JPanel contentPane;
	public JMenuBar menuBar;
	public JMenu file;
	public JMenu language;
	public JMenuItem fullScreen;
	public JMenuItem exit;
	public JMenu settings;
	public JMenu simulatedAnnealing;
	public JCheckBoxMenuItem full;
	public JCheckBoxMenuItem half;
	public JMenu analyzer;
	public JMenuItem letterCount;
	public JMenuItem unknowncipher;
	public JMenuItem wordCount;
	public JMenuItem letterCombinationCount;
	public JMenu helper;
	public JMenuItem modular;
	public JMenuItem multiplicativeFactors;
	public JMenuItem binaryToString;
	public JMenuItem bifidPeriod;
	public JMenuItem adfgvxKey;
	
	public JProgressBar progressBar;
	public JTextField mostLikely;
	
	public UIDefinition() {
		super("Cryptography Solver");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.threadTimer = new Timer();
		this.thread = null;
	}
	
	public void initializeObjects() {
		contentPane = new JPanel();
		
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setOpaque(true); 
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setContentPane(contentPane);
		
		JPanel topPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(0, 0, 5, 0));
		topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
		
		final JComboBox cipher = new JComboBox(ForceDecryptManager.getNames());
		cipher.setMaximumSize(new Dimension(180, 20));
		topPanel.add(cipher);
		final JButton decode = new JButton("Force Decrypt");
		final JButton dictionary = new JButton("Dictionary Attack");
		dictionary.setEnabled(ForceDecryptManager.ciphers.get(cipher.getSelectedIndex()).canDictionaryAttack());
		final JButton cancel = new JButton("Cancel");
		cancel.setEnabled(false);
		final Output outputObj = new Output() {

			@Override
			public void print(String text, Object... format) {
				addText(text, format);
			}

			@Override
			public void println(String text, Object... format) {
				addText(text + "\n", format);
			}
			
		};
		cipher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				IForceDecrypt force = ForceDecryptManager.ciphers.get(cipher.getSelectedIndex());
				dictionary.setEnabled(force.canDictionaryAttack());
			}
		});
		decode.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(input.getText() == null || input.getText().isEmpty())
					return;
				
				thread = new Thread(new Runnable() {

					@Override
					public void run() {
						threadTimer.restart();
						IForceDecrypt force = ForceDecryptManager.ciphers.get(cipher.getSelectedIndex());
						outputObj.println("Cipher: " + force.getName());
						try {
							force.tryDecode(input.getText(), force.getEncryptionData(), Main.instance.language, outputObj, new ProgressValue(1000, progressBar), mostLikely);
						}
						catch(Exception e) {
							outputObj.println(e.toString());
							e.printStackTrace();
						}
						
						DecimalFormat df = new DecimalFormat("#.#");
						outputObj.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
						outputObj.println("");
						decode.setEnabled(true);
						dictionary.setEnabled(ForceDecryptManager.ciphers.get(cipher.getSelectedIndex()).canDictionaryAttack());
						cancel.setEnabled(false);
						language.setEnabled(true);
						try {
							Thread.sleep(1000L);
						} 
						catch(InterruptedException e) {
							e.printStackTrace();
						}
						
						progressBar.setValue(0);
					}
					
				});
				thread.start();
				decode.setEnabled(false);
				dictionary.setEnabled(false);
				cancel.setEnabled(true);
				language.setEnabled(false);
			}
		});
		cancel.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(thread != null)
					thread.stop();
				
				DecimalFormat df = new DecimalFormat("#.#");
				outputObj.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
				outputObj.println("");
				decode.setEnabled(true);
				dictionary.setEnabled(ForceDecryptManager.ciphers.get(cipher.getSelectedIndex()).canDictionaryAttack());
				cancel.setEnabled(false);
				language.setEnabled(true);
				
				try {
					Thread.sleep(500L);
				} 
				catch(InterruptedException e) {
					e.printStackTrace();
				}
				progressBar.setMaximum(10);
				progressBar.setValue(0);
			}
		});
		dictionary.addActionListener(new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(input.getText() == null || input.getText().isEmpty())
					return;
				thread = new Thread(new Runnable() {

					@Override
					public void run() {
						threadTimer.restart();
						IForceDecrypt force = ForceDecryptManager.ciphers.get(cipher.getSelectedIndex());
						outputObj.println("Cipher: " + force.getName());
						
						if(!force.canDictionaryAttack()) 
							return;

						try {
							force.tryDictionaryAttack(input.getText(), Dictionary.words, Main.instance.language, outputObj, new ProgressValue(1000, progressBar));
						}
						catch(Exception e) {
							outputObj.println(e.toString());
							e.printStackTrace();
						}
						
						DecimalFormat df = new DecimalFormat("#.#");
						outputObj.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
						outputObj.println("");
						decode.setEnabled(true);
						dictionary.setEnabled(true);
						cancel.setEnabled(false);
						language.setEnabled(true);
						try {
							Thread.sleep(1000L);
						} 
						catch(InterruptedException e) {
							e.printStackTrace();
						}
						
						progressBar.setValue(0);
					}
					
				});
				thread.start();
				decode.setEnabled(false);
				dictionary.setEnabled(false);
				cancel.setEnabled(true);
				language.setEnabled(false);
			}
		});
		topPanel.add(decode);
		topPanel.add(dictionary);
		topPanel.add(cancel);
		
		contentPane.add(topPanel);
		
		final JPanel varsPanel = new JPanel();
		topPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
		varsPanel.setMaximumSize(new Dimension(5000, 200));
		final CardLayout cardLayout = new CardLayout();
		varsPanel.setLayout(cardLayout);
		for(IForceDecrypt ifd : ForceDecryptManager.getObjects())
			varsPanel.add(ifd.getVarsPanel(), ifd.getName());
		
		cipher.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				cardLayout.show(varsPanel, ForceDecryptManager.getNames()[cipher.getSelectedIndex()]);
			}
		});
		
		
		final JButton test = new JButton("Test");
		varsPanel.add(test);
		contentPane.add(varsPanel);
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
		inputPanel.setBorder(new EmptyBorder(5, 0, 0, 0));
		input = new JTextArea();
		JScrollPane inputScrollPanel = new JScrollPane(input);
		inputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputScrollPanel.setPreferredSize(new Dimension(500, 200));
		input.setLineWrap(true);
		input.setBackground(new Color(255, 255, 220));
		inputPanel.add(inputScrollPanel);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 0));
		buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonPanel.setLayout(new GridLayout(4, 1));
		JButton uppercase = new JButton("Uppercase");
		JButton nospaces = new JButton("Remove _");
		JButton nonletters = new JButton("Keep A-Z");
		JButton textStats = new JButton("Text Stats");
		uppercase.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = input.getText();
				input.setText(s.toUpperCase());
			}
		});
		
		nospaces.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = input.getText();
				input.setText(s.replaceAll("\\s+", ""));
			}
		});
		
		nonletters.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = input.getText();
				input.setText(s.replaceAll("[^a-zA-Z]+", ""));
			}
		});
		
		textStats.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				JFrame panel = new JFrame();
				
				final JTextArea output = new JTextArea();
				JScrollPane outputScrollPanel = new JScrollPane(output);
				outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				outputScrollPanel.setPreferredSize(new Dimension(1000, 200));
				output.setEditable(false);
				
				String cipherText = input.getText();
				int length = cipherText.length();
				
				String text = "Length: " + length;


				text += "\n IC: " + StatCalculator.calculateIC(cipherText);
				text += "\n MIC: " + StatCalculator.calculateMIC(cipherText);
			    text += "\n MKA: " + StatCalculator.calculateMKA(cipherText);
			    text += "\n DIC: " + StatCalculator.calculateDIC(cipherText);
			    text += "\n EDI: " + StatCalculator.calculateEDI(cipherText);
			    text += "\n LR: " + StatCalculator.calculateLR(cipherText);
			    text += "\n ROD: " + StatCalculator.calculateROD(cipherText);
			    text += "\n LDI: " + StatCalculator.calculateLDI(cipherText);
			    text += "\n SDD: " + StatCalculator.calculateSDD(cipherText);
			    
			    text += "\n DIV_2: " + StatCalculator.calculateDIV2(cipherText);
			    text += "\n DIV_3: " + StatCalculator.calculateDIV3(cipherText);
			    text += "\n DIV_5: " + StatCalculator.calculateDIV5(cipherText);
			    text += "\n DIV_25: " + StatCalculator.calculateDIV25(cipherText);
			    text += "\n DIV_4_15: " + StatCalculator.calculateDIV4_15(cipherText);
			    text += "\n DIV_4_30: " + StatCalculator.calculateDIV4_30(cipherText);
			    text += "\n PSQ: " + StatCalculator.calculatePSQ(cipherText);
			    text += "\n HAS_LETTERS: " + StatCalculator.calculateHASL(cipherText);
			    text += "\n HAS_DIGITS: " + StatCalculator.calculateHASD(cipherText);
			    text += "\n HAS_J: " + StatCalculator.calculateHASJ(cipherText);
			    text += "\n HAS_#: " + StatCalculator.calculateHASH(cipherText);
			    text += "\n DBL: " + StatCalculator.calculateDBL(cipherText);
			    
			    text += "\n A_LDI: " + StatCalculator.calculateALDI(cipherText);
			    text += "\n B_LDI: " + StatCalculator.calculateBLDI(cipherText);
			    text += "\n P_LDI: " + StatCalculator.calculatePLDI(cipherText);
			    text += "\n S_LDI: " + StatCalculator.calculateSLDI(cipherText);
			    text += "\n V_LDI: " + StatCalculator.calculateVLDI(cipherText);
			    
			    text += "\n NOMOR: " + StatCalculator.calculateNOMOR(cipherText);
			    text += "\n RDI: " + StatCalculator.calculateRDI(cipherText);
			    text += "\n PTX: " + StatCalculator.calculatePTX(cipherText);
			    text += "\n NIC: " +  StatCalculator.calculateNIC(cipherText);
			    text += "\n PHIC: " + StatCalculator.calculatePHIC(cipherText);
			    text += "\n HAS_0: " + StatCalculator.calculateHAS0(cipherText);
			    text += "\n BDI: " +  StatCalculator.calculateBDI(cipherText);
			    text += "\n CDD: " +  StatCalculator.calculateCDD(cipherText);
			    text += "\n SSTD: " +  StatCalculator.calculateSSTD(cipherText);
			    text += "\n MPIC: " + StatCalculator.calculateMPIC(cipherText);
			    text += "\n SERP: " + StatCalculator.calculateSERP(cipherText);
			    
				text += "\n Suggested Fitness: " + QuadgramStats.getEstimatedFitness(cipherText, Main.instance.language);
				
				output.setText(text);
				
				
				panel.add(output);
				panel.pack();
				panel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				panel.setVisible(true);
			}
		});
		buttonPanel.setPreferredSize(new Dimension(200, 200));
		buttonPanel.setMaximumSize(new Dimension(300, 200));
		
		buttonPanel.add(uppercase);
		buttonPanel.add(nospaces);
		buttonPanel.add(nonletters);
		buttonPanel.add(textStats);
		inputPanel.add(buttonPanel);
		contentPane.add(inputPanel);
		
		this.output = new JTextArea();
		JScrollPane outputScrollPanel = new JScrollPane(this.output);
		outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		outputScrollPanel.setPreferredSize(new Dimension(500, 200));
		this.output.setEditable(false);
		contentPane.add(outputScrollPanel);
		
		this.progressBar = new JProgressBar(0, 10);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		
		this.progressBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				progressBar.setString(Rounder.round(progressBar.getPercentComplete() * 100, 1) + "%");
			}
	    });
		this.contentPane.add(this.progressBar);
		
		this.mostLikely = new JTextField();
		this.mostLikely.setMaximumSize(new Dimension(5000, 100));
		this.contentPane.add(this.mostLikely);
		
		this.menuBar = new JMenuBar();
		this.file = new JMenu("File");
		this.menuBar.add(this.file);
		this.language = new JMenu("Language");
		this.language.setIcon(ImageUtil.createImageIcon("/image/globe.png", "Language"));
		final JMenuItem currentLanguage = new JMenuItem("Current: English");
		
		this.language.add(currentLanguage);
		this.language.addSeparator();
		
		for(final ILanguage language : Languages.languages) {
			JMenuItem jmi = new JMenuItem(language.getName(), ImageUtil.createImageIcon(language.getImagePath(), language.getName()));
			
			jmi.addActionListener(new ActionListener () {
				@Override
				public void actionPerformed(ActionEvent event) {
					Main.instance.language = language;
					currentLanguage.setText("Current: " + language.getName());
					outputObj.println("Set language to " + language.getName());
				}
			});
			this.language.add(jmi);
		}
		this.file.add(this.language);
		this.fullScreen = new JMenuItem("Full Screen");
		this.fullScreen.addActionListener(new ActionListener() {
			@Override
		 	public void actionPerformed(ActionEvent e) {
				removeNotify();
				if(!isUndecorated()) {
					setExtendedState(Frame.MAXIMIZED_BOTH);
			    	setUndecorated(true);
				}
				else {
					setSize(lastSize);
					setLocation(lastLocation);
					setExtendedState(Frame.NORMAL);
					setUndecorated(false);
				}
				
				lastSize = getSize();
				lastLocation = getLocation();
			    addNotify();
		    }
		});
		this.file.add(this.fullScreen);

		this.exit = new JMenuItem("Exit");
	    this.exit.addActionListener(new ActionListener() {
	    	@Override
	    	public void actionPerformed(ActionEvent e) {
	    		System.exit(128);
	    	}
	    });
	    this.file.add(this.exit);
	    
	    this.settings = new JMenu("Settings");
		this.menuBar.add(this.settings);

		/**
		final JTextField textfield = new JTextField();
		this.settings.add(new JMenuItem("A popup menu item"));
		this.settings.add(textfield);
		this.settings.add(new JMenuItem("Another popup menu item"));
		this.settings.addMenuListener(new MenuListener(){
	            public void menuCanceled(MenuEvent e) {}
	            public void menuDeselected(MenuEvent e) {}
	            public void menuSelected(MenuEvent e) {
	                EventQueue.invokeLater(new Runnable(){
	                    public void run() {
	                        textfield.grabFocus();
	                    }
	                });
	            }
	        });**/
		
		this.simulatedAnnealing = new JMenu("Simulated Annealing");
		
		
		JTextField tempSetting = new JTextField("20.0");
		this.simulatedAnnealing.add(new JLabel("Temperature Value"));
		this.simulatedAnnealing.add(tempSetting);
		this.simulatedAnnealing.addSeparator();
		JTextField tempStepSetting = new JTextField("0.1");
		this.simulatedAnnealing.add(new JLabel("Temperature Step"));
		this.simulatedAnnealing.add(tempStepSetting);
		this.simulatedAnnealing.addSeparator();
		JTextField countSetting = new JTextField("500");
		this.simulatedAnnealing.add(new JLabel("Count"));
		this.simulatedAnnealing.add(countSetting);
		
		this.settings.add(this.simulatedAnnealing);
		
		this.settings.addSeparator();
		
		this.full = new JCheckBoxMenuItem("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		this.half = new JCheckBoxMenuItem("NOPQRSTUVWXYZABCDEFGHIJKLM");
		this.half.setSelected(true);
		this.half.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				full.setSelected(!half.isSelected());
			}
		});
		this.full.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				half.setSelected(!full.isSelected());
			}
		});
		this.settings.add(this.full);
		this.settings.add(this.half);

	    this.analyzer = new JMenu("Analyzer");
	    this.menuBar.add(this.analyzer);
		
		this.unknowncipher = new JMenuItem("Identify Unknown Cipher");
		this.unknowncipher.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

			    String cipherText = input.getText();
		
				HashMap<StatisticType, Double> currentData = new HashMap<StatisticType, Double>();
			    
			    currentData.put(StatisticType.INDEX_OF_COINCIDENCE, StatCalculator.calculateIC(cipherText));
			    currentData.put(StatisticType.MAX_IOC, StatCalculator.calculateMIC(cipherText));
			    currentData.put(StatisticType.MAX_KAPPA, StatCalculator.calculateMKA(cipherText));
			    currentData.put(StatisticType.DIGRAPHIC_IOC, StatCalculator.calculateDIC(cipherText));
			    currentData.put(StatisticType.EVEN_DIGRAPHIC_IOC, StatCalculator.calculateEDI(cipherText));
			    currentData.put(StatisticType.LONG_REPEAT_3, StatCalculator.calculateLR(cipherText));
			    currentData.put(StatisticType.LONG_REPEAT_ODD, StatCalculator.calculateROD(cipherText));
			    currentData.put(StatisticType.LOG_DIGRAPH, StatCalculator.calculateLDI(cipherText));
			    currentData.put(StatisticType.SINGLE_LETTER_DIGRAPH, StatCalculator.calculateSDD(cipherText));
			   
			    List<List<Object>> num_dev = StatCalculator.getResultsFromStats(currentData);
			    
			    Comparator<List<Object>> comparator = new Comparator<List<Object>>() {
			    	@Override
			        public int compare(List<Object> c1, List<Object> c2) {
			        	double diff = (double)c1.get(1) - (double)c2.get(1);
			        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
			        }
			    };

			    Collections.sort(num_dev, comparator);
			    JPanel cipherInfoPanel = new JPanel();
			    JScrollPane scrollPane = new JScrollPane(cipherInfoPanel);
			    scrollPane.getVerticalScrollBar().setUnitIncrement(12);
			    cipherInfoPanel.setLayout(new BoxLayout(cipherInfoPanel, BoxLayout.Y_AXIS));
			    scrollPane.setPreferredSize(new Dimension(480, 450));
			    scrollPane.setMaximumSize(new Dimension(480, 450));
			    scrollPane.setMinimumSize(new Dimension(480, 450));
			    JLabel titleLabel = new JLabel("Cipher" + StringTransformer.repeat(" ", 20 - "Cipher".length()) + " Likelyhood");
			    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
		    	cipherInfoPanel.add(titleLabel);
			    
			    for(int i = 0; i < num_dev.size(); i++) {
			    	JLabel cipherInfoLabel = new JLabel(num_dev.get(i).get(0) + ":" + StringTransformer.repeat(" ", 20 - ((String)num_dev.get(i).get(0)).length()) + Rounder.round((double)num_dev.get(i).get(1), 2));
			    	cipherInfoLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	cipherInfoPanel.add(cipherInfoLabel);
			    }
			    panel.add(scrollPane);
			    
			    Object[] statValues = new Object[38];
			    
			    statValues[0] = StatCalculator.calculateIC(cipherText);
				statValues[1] = StatCalculator.calculateMIC(cipherText);
				statValues[2] = StatCalculator.calculateMKA(cipherText);
				statValues[3] = StatCalculator.calculateDIC(cipherText);
				statValues[4] = StatCalculator.calculateEDI(cipherText);
				statValues[5] = StatCalculator.calculateLR(cipherText);
				statValues[6] = StatCalculator.calculateROD(cipherText);
				statValues[7] = StatCalculator.calculateLDI(cipherText);
				statValues[8] = StatCalculator.calculateSDD(cipherText);
			    
				statValues[9] = "CIPHER";
			    
				statValues[10] = StatCalculator.calculateDIV2(cipherText);
				statValues[11] = StatCalculator.calculateDIV3(cipherText);
				statValues[12] = StatCalculator.calculateDIV5(cipherText);
				statValues[13] = StatCalculator.calculateDIV25(cipherText);
				statValues[14] = StatCalculator.calculateDIV4_15(cipherText);
				statValues[15] = StatCalculator.calculateDIV4_30(cipherText);
				statValues[16] = StatCalculator.calculatePSQ(cipherText);
				statValues[17] = StatCalculator.calculateHASL(cipherText);
				statValues[18] = StatCalculator.calculateHASD(cipherText);
				statValues[19] = StatCalculator.calculateHASJ(cipherText);
				statValues[20] = StatCalculator.calculateHASH(cipherText);
				statValues[21] = StatCalculator.calculateDBL(cipherText);
				
				
				statValues[22] = StatCalculator.calculateALDI(cipherText);
				statValues[23] = StatCalculator.calculateBLDI(cipherText);
				statValues[24] = StatCalculator.calculatePLDI(cipherText);
				statValues[25] = StatCalculator.calculateSLDI(cipherText);
				statValues[26] = StatCalculator.calculateVLDI(cipherText);
				statValues[27] = StatCalculator.calculateNOMOR(cipherText);
				statValues[28] = StatCalculator.calculateRDI(cipherText);
				statValues[29] = StatCalculator.calculatePTX(cipherText);
				statValues[30] = StatCalculator.calculateNIC(cipherText);
				statValues[31] = StatCalculator.calculatePHIC(cipherText);
				statValues[32] = StatCalculator.calculateHAS0(cipherText);
				statValues[33] = StatCalculator.calculateBDI(cipherText);
				statValues[34] = StatCalculator.calculateCDD(cipherText);
				statValues[35] = StatCalculator.calculateSSTD(cipherText);
				statValues[36] = StatCalculator.calculateMPIC(cipherText);
				statValues[37] = StatCalculator.calculateSERP(cipherText);
			    
				Map<String, Integer> answers = new HashMap<String, Integer>();
				
				for(Map map : TraverseTree.trees) {
					String answer = TraverseTree.traverse_tree(map, statValues);
					answers.put(answer, 1 + (answers.containsKey(answer) ? answers.get(answer) : 0));
				}
				answers = MapHelper.sortMapByValue(answers, false);
				
			
				System.out.println(answers);
				
				
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "Identify Unknown Cipher");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			    dialog.dispose();
			}
		});
		this.analyzer.add(this.unknowncipher);
	    
	    
	    this.letterCount = new JMenuItem("Letter Count");
	    this.letterCount.addActionListener(new ActionListener() {
		     public void actionPerformed(ActionEvent e) {
		         JPanel panel = new JPanel();
		         panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		          
		         final JBarChart chart = new JBarChart(new ChartList());
		         chart.setMinimumSize(new Dimension(400, 200));
		         chart.setMaximumSize(new Dimension(400, 200));
		         chart.setPreferredSize(new Dimension(400, 200));
		         chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
		         panel.add(chart);
		         
		          
		         final JBarChart chartAlphabeticly = new JBarChart(new ChartList());
		         chartAlphabeticly.setMinimumSize(new Dimension(400, 200));
		         chartAlphabeticly.setMaximumSize(new Dimension(400, 200));
		         chartAlphabeticly.setPreferredSize(new Dimension(400, 200));
		         chartAlphabeticly.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered Alphabeticly"));
		         panel.add(chartAlphabeticly);
		         
		         Hashtable<Character, LetterCount> letters = StringAnalyzer.countLetters(input.getText());
		         ArrayList<LetterCount> alphabeticlySorted = StringAnalyzer.alphabeticOrder((Hashtable<Character, LetterCount>)letters.clone());
		         ArrayList<LetterCount> sortedItems = StringAnalyzer.sizeOrder(letters);
		         
		         ChartList values1 = new ChartList();
				 for(LetterCount letterCount : alphabeticlySorted)
					 values1.add(new ChartData("" + letterCount.ch, (double)letterCount.count));
			  		
			  		
				 ChartList values = new ChartList();
		         for(LetterCount letterCount : sortedItems)
		        	 values.add(new ChartData("" + letterCount.ch, (double)letterCount.count));

		          
		         chart.values = values;
		         chart.updateUI();
		          
		         chartAlphabeticly.values = values1;
		         chartAlphabeticly.updateUI();
		          
		         JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		         JDialog dialog = optionPane.createDialog(Main.instance.definition, "Letter Analyzer");
		         dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		         dialog.setModal(true);
		         dialog.setVisible(true);
		         dialog.dispose();
		     }
		});
		this.analyzer.add(this.letterCount);
		
		this.letterCombinationCount = new JMenuItem("Letter Combination Analyzer");
		this.letterCombinationCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			          
			    final JBarChart chart = new JBarChart(new ChartList()).setHasBarText(false);
			    chart.setMinimumSize(new Dimension(1000, 200));
			    chart.setMaximumSize(new Dimension(1000, 200));
			    chart.setPreferredSize(new Dimension(1000, 200));
			    chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
			    panel.add(chart);
			    
			    TreeMap<String, Integer> wordCount = StringAnalyzer.getEmbeddedStrings(input.getText(), 2, 12);
						
				ChartList sizeSorted = new ChartList();
				  		
				while(wordCount.size() > 0 && sizeSorted.size() < 89) {
				  	String largest = null;
				  	int lastLargest = 0;
				  			
				  	Iterator<String> ite = wordCount.keySet().iterator();
				  			
				  	while(ite.hasNext()) {
				  		String str = ite.next();
				  		if(largest == null) {
				  			largest = str;
				  			lastLargest = wordCount.get(str);
				  		}
				  		else if(wordCount.get(str) > lastLargest) {
				  			largest = str;
				  			lastLargest = wordCount.get(str);
				  		}
				  	}
				  			
				  	sizeSorted.add(new ChartData(largest, lastLargest));
				  	wordCount.remove(largest);	
				}

			    chart.values = sizeSorted;
			    chart.updateUI();
				JComboBox comboBox = new JComboBox(new String[] {"ALL", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
				panel.add(comboBox);
			    
				class ItemChangeListener implements ItemListener{
					@Override
				    public void itemStateChanged(ItemEvent event) {
						if(event.getStateChange() == ItemEvent.SELECTED) {
							String label = (String)event.getItem();
							int minlength = 2;
				      		int maxlength = 12;
				      		if(!label.contains("ALL")) {
				      			minlength = Integer.valueOf(label);
				      			maxlength = minlength;
				      		}
			
				      		TreeMap<String, Integer> wordCount = StringAnalyzer.getEmbeddedStrings(input.getText(), minlength, maxlength);
						
				      		ChartList sizeSorted = new ChartList();
						  		
				      		while(wordCount.size() > 0 && sizeSorted.size() < 89) {
				      			String largest = null;
							  	int lastLargest = 0;
							  			
							  	Iterator<String> ite = wordCount.keySet().iterator();
							  			
							  	while(ite.hasNext()) {
							  		String str = ite.next();
							  		if(largest == null) {
							  			largest = str;
							  			lastLargest = wordCount.get(str);
							  		}
							  		else if(wordCount.get(str) > lastLargest) {
							  			largest = str;
							  			lastLargest = wordCount.get(str);
							  		}
							  	}
							  			
							  	sizeSorted.add(new ChartData(largest, lastLargest));
							  	wordCount.remove(largest);	
							}
	
						    chart.values = sizeSorted;
						    chart.updateUI();
				       }
				    }       
				}
				comboBox.addItemListener(new ItemChangeListener());
				
				
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "Letter Combination Analyzer");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			    dialog.dispose();
			}
		});
		this.analyzer.add(this.letterCombinationCount);
		
		this.helper = new JMenu("Helpers");
	    this.menuBar.add(this.helper);
		
	    this.modular = new JMenuItem("Modular");
	    this.modular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    JLabel encodedButton = new JLabel("Type text into the encoded textbox"); 
			    encodedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			    panel.add(encodedButton);

				JPanel textBoxPanel = new JPanel();
				textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.X_AXIS));
			    final JTextField inputTextBox = new JTextField();
			    final JTextField modularTextBox = new JTextField("26");
			    //TODO modularTextBox.setDocument(new CustomDocument());
			    textBoxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Input Text Area"));
			    final JLabel label = new JLabel("In modular 26: 0");      
			    label.setAlignmentX(Component.CENTER_ALIGNMENT);
			    
			 	inputTextBox.addKeyListener(new KeyListener() {
			 		@Override
					public void keyPressed(KeyEvent event) {}

					@Override
					public void keyReleased(KeyEvent event) {
						String newText = inputTextBox.getText();
						int modular = new Integer(modularTextBox.getText());
						int target = new Integer(newText);
						
						label.setText(String.format("In modular %d: %d", modular, MathHelper.wrap(target, 0, modular)));
					}

					@Override
					public void keyTyped(KeyEvent event) {}
				});
			 		  
			 	textBoxPanel.add(inputTextBox);
			 	textBoxPanel.add(modularTextBox);
			 	panel.add(textBoxPanel);
			 	panel.add(label);
			          
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "Modular");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			    //dialog.dispose();
			}
	    	
	    });
	    
		this.helper.add(this.modular);
		
	    this.multiplicativeFactors = new JMenuItem("Multiplicative Factors");
	    this.multiplicativeFactors.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    JLabel encodedButton = new JLabel("Type text into the encoded textbox"); 
			    encodedButton.setAlignmentX(Component.CENTER_ALIGNMENT);
			    panel.add(encodedButton);

				JPanel textBoxPanel = new JPanel();
				textBoxPanel.setLayout(new BoxLayout(textBoxPanel, BoxLayout.X_AXIS));
			    final JTextField modularTextBox = new JTextField("26");
			    
			    
			    final DefaultListModel<String> listModel = new DefaultListModel<String>();
			    final JList<String> list = new JList<String>(listModel);
			    list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
			    list.setLayoutOrientation(JList.VERTICAL);
			    final JScrollPane scrollPane = new JScrollPane(list);
				scrollPane.getVerticalScrollBar().setUnitIncrement(16);
				scrollPane.setPreferredSize(new Dimension(80, 250));
			    textBoxPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Input Text Area"));   
			    
			    modularTextBox.addKeyListener(new KeyListener() {
			 		@Override
					public void keyPressed(KeyEvent event) {}

					@Override
					public void keyReleased(KeyEvent event) {
						int modular = new Integer(modularTextBox.getText());
						
						ArrayList<Integer[]> list = MathHelper.getMultiplicativeFactors(modular);
						listModel.removeAllElements();
						
						for(Integer[] array : list) {
							final JLabel label = new JLabel(String.format("%d %d", array[0], array[1]));      
							label.setAlignmentX(Component.CENTER_ALIGNMENT);
							listModel.addElement(String.format("%d     %d", array[0], array[1]));
						}
					}

					@Override
					public void keyTyped(KeyEvent event) {}
				});
			 		  
			 	panel.add(scrollPane);
			 	textBoxPanel.add(modularTextBox);
			 	panel.add(textBoxPanel);

			          
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "Multiplicative Factors");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			}
	    	
	    });
	    
		this.helper.add(this.multiplicativeFactors);
		
		this.binaryToString = new JMenuItem("Binary Converter");
	    this.binaryToString.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    
			    final JTextArea input = new JTextArea();
				JScrollPane inputScrollPanel = new JScrollPane(input);
				inputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				inputScrollPanel.setPreferredSize(new Dimension(500, 200));
				input.setLineWrap(true);
				
				final JTextArea output = new JTextArea();
				JScrollPane outputScrollPanel = new JScrollPane(output);
				outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				outputScrollPanel.setPreferredSize(new Dimension(500, 200));
				output.setLineWrap(true);
				output.setEditable(false);
				
				final JSpinner spinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
				
				JButton button = new JButton("Convert");
				button.addMouseListener(new CustomMouseListener() {
					@Override
					public void mouseClicked(MouseEvent event) {
						String binaryText = input.getText();
						List<String> split = StringTransformer.splitInto(input.getText(), (int)spinner.getValue());
						
						String cipherText = "";
						for(String binary : split) {
							try {
								int decimal = Integer.parseInt(binary, 2);
								if(decimal < 0 || decimal > 25) {
									cipherText = "ERROR: Binary number \'" + binary + "' dec(" + decimal + ") is valid letter"; 
									break;
								}
								char letter = (char)(decimal + 'A');
								cipherText += letter;
							}
							catch(NumberFormatException e) {
								cipherText = "ERROR: Cannot parse \'" + binary + "'"; 
								break;
							}
						}
						output.setText(cipherText);
					}
				});
				
				panel.add(inputScrollPanel);
				panel.add(outputScrollPanel);
				panel.add(spinner);
				panel.add(button);
				
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "Binary Converter");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			}
	    });
	    
		this.helper.add(this.binaryToString);
		
		this.bifidPeriod = new JMenuItem("Bifid Period");
	    this.bifidPeriod.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    
			    final JTextArea input = new JTextArea();
				JScrollPane inputScrollPanel = new JScrollPane(input);
				inputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				inputScrollPanel.setPreferredSize(new Dimension(500, 200));
				input.setLineWrap(true);

				final JBarChart chart = new JBarChart(new ChartList()).setHasBarText(false);
			    chart.setMinimumSize(new Dimension(1000, 200));
			    chart.setMaximumSize(new Dimension(1000, 200));
			    chart.setPreferredSize(new Dimension(1000, 200));
			    chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Steps 1-20"));
			    final JLabel label = new JLabel("Guess Period: ");
				JButton button = new JButton("Convert");
				button.addMouseListener(new CustomMouseListener() {
					@Override
					public void mouseClicked(MouseEvent event) {
						String text = input.getText();
						ChartList chartList = new ChartList();
						
						Map<Integer, Double> values = new HashMap<Integer, Double>();
						double maxValue = Double.MIN_VALUE;
						int maxStep = 0;
						
						double secondValue = Double.MIN_VALUE;
						
						for(int step = 1; step < 20; step++) {
							HashMap<String, Integer> counts = new HashMap<String, Integer>();
							for(int i = 0; i < text.length() - step; i++) {
								String s = text.charAt(i) + "" + text.charAt(i + step);
								counts.put(s, counts.containsKey(s) ? counts.get(s) + 1 : 1);
							}
							
							Statistics stats = new Statistics(counts.values());

						    double variance = stats.getVariance();
						 
							chartList.add(new ChartData("Step: " + step, variance));
							values.put(step, variance);
							
							if(variance > maxValue) {
								secondValue = maxValue;
								maxValue = variance;
								maxStep = step;
							}
							else if(variance > secondValue) {
								secondValue = variance;
							}
						}
		
						
						int periodGuess = 0;
		
						if(maxValue - maxValue / 4 > secondValue)
							periodGuess = maxStep * 2;
						else {
							double max = Double.MAX_VALUE;
							int bestStep = 0;
							
							for(int step = maxStep - 1; step <= maxStep + 1; step++) {
								if(step < 1 || step == maxStep || maxStep > 20)
									continue;
								
								double diff = Math.abs(values.get(maxStep) - values.get(step));
								if(diff < max) {
									max = diff;
									bestStep = step;
								}
							}
							
							
							periodGuess = Math.min(bestStep, maxStep) * 2 + Math.abs(bestStep - maxStep);
						}
						
						label.setText("Guess Period: " + periodGuess);
						
						chart.values = chartList;
						chart.updateUI();
					}
				});
				
				panel.add(inputScrollPanel);
			    panel.add(chart);
				panel.add(button);
				panel.add(label);
				
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "Bifid Period");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			}
	    });
	    
		this.helper.add(this.bifidPeriod);
		
		this.adfgvxKey = new JMenuItem("ADFGVX Key");
	    this.adfgvxKey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JPanel panel = new JPanel();
			    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
			    
			    final JTextArea input = new JTextArea();
				JScrollPane inputScrollPanel = new JScrollPane(input);
				inputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				inputScrollPanel.setPreferredSize(new Dimension(500, 200));
				input.setLineWrap(true);

				final JTextArea output = new JTextArea();
				JScrollPane outputScrollPanel = new JScrollPane(output);
				outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
				outputScrollPanel.setPreferredSize(new Dimension(500, 200));
				output.setLineWrap(true);
				output.setEditable(false);
				
				JButton button = new JButton("Convert");
				button.addMouseListener(new CustomMouseListener() {
					@Override
					public void mouseClicked(MouseEvent event) {
						final String text = input.getText();
						
						smallest = Double.MAX_VALUE;
						orders1 = new ArrayList<Integer[]>();
						closestIC = Double.MAX_VALUE;
						orders2 = new ArrayList<Integer[]>();
						
						for(int length = 2; length <= 6; length++)
							permutate(text, ArrayHelper.range(0, length), 0, true, Main.instance.language);
						for(int length = 2; length <= 6; length++)
							permutate(text, ArrayHelper.range(0, length), 0, false, Main.instance.language);
						
						String s = "";
						
						s += "Like english: " + smallest + "\n";
						for(Integer[] i : orders1) {
							s += Arrays.toString(i) + "\n";
						}
						
						s += "\nIOC: " + closestIC + " off normal\n";
						
						for(Integer[] i : orders2) {
							s += Arrays.toString(i) + "\n";
						}
						
						output.setText(s);
					}
				});
				
				panel.add(inputScrollPanel);
				panel.add(outputScrollPanel);
				panel.add(button);
				
			    JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
			    JDialog dialog = optionPane.createDialog(Main.instance.definition, "ADFGVX Key");
			    dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			    dialog.setModal(true);
			    dialog.setVisible(true);
			}
	    });
	    
		this.helper.add(this.adfgvxKey);
		
		//Add the components pane to this panel.
		this.setJMenuBar(this.menuBar);
	}
	
	private double smallest = Double.MAX_VALUE;
	private List<Integer[]> orders1 = new ArrayList<Integer[]>();
	private double closestIC = Double.MAX_VALUE;
	private List<Integer[]> orders2 = new ArrayList<Integer[]>();
	
	public void permutate(String text, int[] arr, int pos, boolean flag, ILanguage language) {
	    if(arr.length - pos == 1) {
	    	String s = ColumnarRow.decode(text, arr);
	    	double n = calculate(s, language);
	    	double ic = indexOfc(s);

	    	Integer[] newArray = new Integer[arr.length];
	    	int i = 0;
	    	for (int value : arr)
	    	    newArray[i++] = Integer.valueOf(value);
	    	
	    	
	    	if(flag && n <= this.smallest) {
	    		if(n != this.smallest)
	    			orders1.clear();
	    		orders1.add(newArray);
	    		this.smallest = n;
	    	}
	    	double diff = Math.abs(Languages.english.getNormalCoincidence() - ic);
	    	if(!flag && diff <= closestIC) {
	    		if(n != this.smallest)
	    			orders2.clear();
	    		orders2.add(newArray);
	    		
	    		this.closestIC = diff;
	    	}
	    }
	    else
	        for(int i = pos; i < arr.length; i++) {
	            int h = arr[pos];
	            int j = arr[i];
	            arr[pos] = j;
	            arr[i] = h;
	            
	            permutate(text, arr, pos + 1, flag, language);
	            arr[pos] = h;
	    	    arr[i] = j;
	        }
	}
	
	public int findKeywordLength(String text, ILanguage language) {
		int bestLength = 2;
	
	    double smallestDifference = Double.MAX_VALUE;
	    for(int i = 2; i <= 15; ++i) {
	    	double total = 0;
	    	String temp = StringTransformer.rotateRight(text, i);
		    for(int j = 0; j < text.length(); j += 2)
		       if(temp.substring(j, j + 2).equals(text.substring(j, j + 2)))
		    	   total += 1;
	    	
	    	double average = total / (text.length() / 2);
	    	double currentSquaredDiff = Math.abs((language.getNormalCoincidence() - average));
	    	
	        if(currentSquaredDiff < smallestDifference) {
	        	bestLength = i;
	        	smallestDifference = currentSquaredDiff;
	        }
	    }
	    
	    return bestLength;
	}	
	
	public static double indexOfc(String text) {
		Map<String, Integer> letters = StringAnalyzer.getEmbeddedStrings(text, 2, 2, false);

		double total = 0.0D;
		
		for(String letter : letters.keySet())
			total += letters.get(letter) * (letters.get(letter) - 1);

		int length = text.length() / 2;
		
		return total / (length * (length - 1));
	}
	
	public static double calculate(String text, ILanguage language) {
		Map<String, Integer> letters = MapHelper.sortMapByValue(StringAnalyzer.getEmbeddedStrings(text, 2, 2, false), false);
		double total = 0.0D;
		
		List<Double> normalOrder = language.getFrequencyLargestFirst();
		System.out.println(normalOrder);
		int index = 0;
		for(String letter : letters.keySet()) {
			
			double count = letters.get(letter);
			double expectedCount = normalOrder.get(index) * (text.length() / 2) / 100;
			
			double sum = Math.abs(count - expectedCount);
			index += 1;
			total += sum;
			if(index >= normalOrder.size())
				break;
		}
		//while(index < 26) {
		//	total += normalOrder[index] * (text.length() / 2) / 100;
		//	index += 1;
		//}
		
		return total;
	}

	public void placeObjects() {
		
	}

	public void finalizeObjects() {
		//Loading
		Threads.runTask(new Runnable() {
			@Override
			public void run() {
				Map<Component, Boolean> stateMap = SwingHelper.disableAllChildComponents(contentPane, menuBar);

                JProgressBar loadBar = new JProgressBar(0, Languages.languages.size() + 3);
                loadBar.setStringPainted(true);
                loadBar.setPreferredSize(new Dimension(500, 60));
				
                Object[] options = {"Cancel"};

				JOptionPane optionPane = new JOptionPane(loadBar, JOptionPane.PLAIN_MESSAGE, JOptionPane.CANCEL_OPTION, null, options, options[0]);
				JDialog dialog = optionPane.createDialog(contentPane, "Loading...");
				dialog.setModal(false);
				dialog.setVisible(true);
				dialog.setLocationRelativeTo(UIDefinition.this);
				dialog.setTitle("Loading... TranverseTree");
				TraverseTree.onLoad();
				loadBar.setValue(loadBar.getValue() + 1);
				dialog.setTitle("Loading... Dictinary");
				Dictionary.onLoad();
				loadBar.setValue(loadBar.getValue() + 1);
				dialog.setTitle("Loading... SaveData");
				SaveHandler.load();
				loadBar.setValue(loadBar.getValue() + 1);
				
				for(ILanguage language : Languages.languages) {
					dialog.setTitle("Loading... Lang(" + language.getName() + ")");
					language.loadNGramData();
					loadBar.setValue(loadBar.getValue() + 1);
				}
				
				SwingHelper.rewindAllChildComponents(stateMap);
				dialog.dispose();
			}
		});
	}
	
	public void end() {
		this.pack();
		FrameUtil.repostionToCentre(this);
        this.setVisible(true);
	}

	public void addText(String text, Object... format) {
		this.output.append(String.format(text, format));
        this.output.setCaretPosition(this.output.getText().length());
	}

	public void readFromMap(Map<String, Object> input) {
		
	}
	
	public void writeToMap(Map<String, Object> output) {
		
	}
}