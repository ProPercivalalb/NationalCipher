package nationalciphernew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowStateListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import javalibrary.ForceDecryptManager;
import javalibrary.Output;
import javalibrary.cipher.stats.TraverseTree;
import javalibrary.cipher.stats.WordSplit;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.math.MathHelper;
import javalibrary.math.Rounder;
import javalibrary.string.LetterCount;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.string.ValueFormat;
import javalibrary.swing.ButtonUtil;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ImageUtil;
import javalibrary.swing.LayoutUtil;
import javalibrary.swing.SwingHelper;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import javalibrary.thread.Threads;
import nationalcipher.Main;
import nationalcipher.SaveHandler;
import nationalcipher.UIDefinition;

/**
 *
 * @author Alex
 */
public class UINew extends JFrame {

	public Settings settings;
	public Output output;

    public UINew() {
    	super("Cryptography Solver");
    	this.settings = new Settings();
    	
        initComponents();
        finishComponents();
        loadDataFiles();
    }

    public void loadDataFiles() {
    	final Map<Component, Boolean> stateMap = SwingHelper.disableAllChildComponents((JComponent)getContentPane(), menuBar);
        final JProgressBar loadBar = new JProgressBar(0, Languages.languages.size() + 3);
        loadBar.setStringPainted(true);
        loadBar.setPreferredSize(new Dimension(500, 60));
		
        Object[] options = {"Cancel"};

		JOptionPane optionPane = new JOptionPane(loadBar, JOptionPane.PLAIN_MESSAGE, JOptionPane.CANCEL_OPTION, null, options, options[0]);
		final JDialog dialog = optionPane.createDialog(UINew.this, "Loading...");
		dialog.setModal(false);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(UINew.this);
		
		//Loading
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				
				
				dialog.setTitle("Loading... TranverseTree");
				TraverseTree.onLoad();
				loadBar.setValue(loadBar.getValue() + 1);
				dialog.setTitle("Loading... Dictinary");
				Dictionary.onLoad();
				loadBar.setValue(loadBar.getValue() + 1);
				dialog.setTitle("Loading... Word statitics");
				WordSplit.loadFile();
				loadBar.setValue(loadBar.getValue() + 1);
				
				/**
				for(ILanguage language : Languages.languages) {
					dialog.setTitle("Loading... Lang(" + language.getName() + ")");
					language.loadNGramData();
					loadBar.setValue(loadBar.getValue() + 1);
				}**/
				Languages.english.loadNGramData();
				
				SwingHelper.rewindAllChildComponents(stateMap);
				dialog.dispose();
			}
		});
		
	}

	public void finishComponents() {
		this.output = new Output.TextComponent(this.outputTextArea);
        this.pack();
		this.setSize(900, 800);
        this.setVisible(true);
    }
                     
    private void initComponents() {
    	this.inputPanel = new JPanel();
        this.inputTextScroll = new JScrollPane();
        this.inputTextArea = new JTextArea();
        this.statTextArea = new JTextArea();
    	this.outputTextScroll = new JScrollPane();
        this.outputTextArea = new JTextArea();
        this.toolBar = new JToolBar();
        this.toolBarStart = new JButton();
        this.toolBarStop = new JButton();
        this.menuBar = new JMenuBar();
        this.menuItemFile = new JMenu();
        this.menuItemFullScreen = new JMenuItem();
        this.menuItemExit = new JMenuItem();
        this.menuItemEdit = new JMenu();
        this.menuItemPaste = new JMenuItem();
        this.menuItemTools = new JMenu();
        this.menuItemLetterFrequency = new JMenuItem();
        this.menuItemWordSplit = new JMenuItem();
        this.menuItemInfo = new JMenuItem();
        this.menuItemSettings = new JMenu();
        this.menuItemLanguage = new JMenu();
        this.menuItemCurrentLanguage = new JMenuItem();
        this.menuItemKeyword = new JMenu();
		this.menuItemKeywordNormal = new JCheckBoxMenuItem();
		this.menuItemKeywordHalf = new JCheckBoxMenuItem();
		this.menuItemKeywordReverse = new JCheckBoxMenuItem();
		this.menuItemSimulatedAnnealing = new JMenu();
        
		this.setLayout(new GridBagLayout());

		this.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_open.png")));
		//this.setIconImage(image);
	

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setMinimumSize(new Dimension(0, 25));
        panel.setPreferredSize(new Dimension(0, 25));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
 
     
		
        this.toolBar.setFloatable(false);
        //this.toolBar.setfr
        JComboBox cipher = new JComboBox(ForceDecryptManager.getNames());
     	cipher.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
     	this.toolBar.add(cipher);
     		 
     	final JComboBox type = new JComboBox(new String[] {"Brute Force", "Calculated", "Dictionary"});
     	type.setMaximumSize(new Dimension(120, Integer.MAX_VALUE));
      	this.toolBar.add(type);
        
        this.toolBarStart.setText("Execute");
        this.toolBarStart.setIcon(ImageUtil.createImageIcon("/image/accept.png", "Start"));
        this.toolBarStart.setFocusPainted(false);
        this.toolBarStart.setToolTipText("Tries to decrypt the given text.");
        this.toolBar.add(this.toolBarStart);
        
        this.toolBarStop.setText("Terminate");
        this.toolBarStop.setIcon(ImageUtil.createImageIcon("/image/stop.png", "Terminate"));
        this.toolBarStop.setFocusPainted(false);
        this.toolBarStop.setEnabled(false);
        this.toolBarStop.setToolTipText("Terminates the current process.");
        this.toolBar.add(this.toolBarStop);
   
        //this.toolBar.add(ButtonUtil.createIconButton(ImageUtil.createImageIcon("/image/text_letterspacing.png", "Remove Letters")));
		
		panel.add(this.toolBar);
        this.add(panel, LayoutUtil.createConstraints(0, 0, 1, 0));
        
		this.inputPanel.setLayout(new BoxLayout(this.inputPanel, BoxLayout.X_AXIS));
		this.inputTextArea.setBackground(new Color(255, 255, 220));
  
        this.inputTextArea.setLineWrap(true);
        this.inputTextArea.getDocument().addDocumentListener(new InputTextChange());
        DocumentUtil.addUndoManager(this.inputTextArea);
        this.inputTextScroll.setViewportView(this.inputTextArea);
        this.inputTextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        ((AbstractDocument)this.inputTextArea.getDocument()).setDocumentFilter(new DocumentUtil.DocumentUpperCaseInput());

        this.inputPanel.add(this.inputTextScroll);
        
        this.statTextArea.setLineWrap(true);
        this.statTextArea.setEditable(false);
        this.statTextArea.setFont(this.statTextArea.getFont().deriveFont(12F));
        this.statTextArea.setText("Statistics");
	    JScrollPane scrollPane = new JScrollPane();
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMinimumSize(new Dimension(300, 0));
        scrollPane.setPreferredSize(new Dimension(300, 0));
        scrollPane.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
    	scrollPane.setViewportView(this.statTextArea);
    	
	   // this.inputPanel.add(scrollPane, LayoutUtil.createConstraintsIPad(1, 0, 0.25, 1, 200, 0));
	    this.inputPanel.add(scrollPane);
	    this.add(this.inputPanel, LayoutUtil.createConstraints(0, 1, 1, 0.2));
	    
	    //Output panel
	    
 
        this.outputTextArea.setEditable(false);
        this.outputTextScroll.setViewportView(this.outputTextArea);
        this.outputTextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.add(this.outputTextScroll, LayoutUtil.createConstraints(0, 2, 1, 0.5));
        
        this.menuItemFile.setText("File");
        
        this.menuItemFullScreen.setText("Full Screen");
        this.menuItemFullScreen.addActionListener(new FullScreenAction());
        this.menuItemFullScreen.setIcon(ImageUtil.createImageIcon("/image/page_white_magnify.png", "Full Screen"));
        this.menuItemFile.add(this.menuItemFullScreen);
        
        this.menuItemExit.setText("Exit");
        this.menuItemExit.addActionListener(new ExitAction());
        this.menuItemFile.add(this.menuItemExit);
        
        this.menuBar.add(this.menuItemFile);

        this.menuItemEdit.setText("Edit");
        
        this.menuItemPaste.setText("Paste");
        this.menuItemPaste.setIcon(ImageUtil.createImageIcon("/image/paste_plain.png", "Paste"));
        this.menuItemPaste.addActionListener(new PasteAction());
        this.menuItemPaste.setToolTipText("Pastes the text from the system clipboard.");
        this.menuItemEdit.add(this.menuItemPaste);
        
        this.menuBar.add(this.menuItemEdit);

        this.menuItemTools.setText("Tools");
        
        this.menuItemLetterFrequency.setText("Letter Frequency");
        this.menuItemLetterFrequency.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Letter Frequency"));
        this.menuItemLetterFrequency.addActionListener(new LetterFrequencyAction());
        this.menuItemTools.add(this.menuItemLetterFrequency);
        
        this.menuItemTools.addSeparator();
        
        this.menuItemWordSplit.setText("Word Split");
        this.menuItemWordSplit.setIcon(ImageUtil.createImageIcon("/image/spellcheck.png", "Paste"));
        this.menuItemWordSplit.addActionListener(new WordSplitAction());
        this.menuItemTools.add(this.menuItemWordSplit);
        
        this.menuItemInfo.setText("Text Information");
        this.menuItemInfo.setIcon(ImageUtil.createImageIcon("/image/information.png", "Information"));
        //this.menuItemInfo.addActionListener(new Action());
        this.menuItemTools.add(this.menuItemInfo);
        
        this.menuBar.add(this.menuItemTools);

        this.menuItemSettings.setText("Settings");
        
        this.menuItemLanguage.setText("Language");

		this.menuItemLanguage.setIcon(ImageUtil.createImageIcon("/image/globe.png", "Language"));
		this.menuItemCurrentLanguage.setText("Current: " + settings.language.getName());
		
		
		this.menuItemLanguage.add(this.menuItemCurrentLanguage);
		this.menuItemLanguage.addSeparator();
		ButtonGroup group = new ButtonGroup();
		for(ILanguage language : Languages.languages) {
			JMenuItem jmi = new JCheckBoxMenuItem(language.getName(), ImageUtil.createImageIcon(language.getImagePath(), language.getName()));
			jmi.addActionListener(new LanguageChangeAction(language));
			this.menuItemLanguage.add(jmi);
			group.add(jmi);
			if(language == this.settings.language) jmi.setSelected(true);
		}
		this.menuItemSettings.add(this.menuItemLanguage);

		this.menuItemKeyword.setText("Keyword");
		
		this.menuItemKeywordNormal.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		this.menuItemKeywordHalf.setText("NOPQRSTUVWXYZABCDEFGHIJKLM");
		this.menuItemKeywordReverse.setText("ZYXWVUTSRQPONMLKJIHGFEDCBA");
		
		new JMenuItem[]{this.menuItemKeywordNormal, this.menuItemKeywordHalf, this.menuItemKeywordReverse}[this.settings.keywordCreation].setSelected(true);
		
		this.settings.keywordCreationGroup = new ButtonGroup();
		this.settings.keywordCreationGroup.add(this.menuItemKeywordNormal);
		this.settings.keywordCreationGroup.add(this.menuItemKeywordHalf);
		this.settings.keywordCreationGroup.add(this.menuItemKeywordReverse);
		
		this.menuItemKeywordNormal.addActionListener(new KeywordCreationAction(0));
		this.menuItemKeywordHalf.addActionListener(new KeywordCreationAction(1));
		this.menuItemKeywordReverse.addActionListener(new KeywordCreationAction(2));
		
		this.menuItemKeyword.add(this.menuItemKeywordNormal);
		this.menuItemKeyword.add(this.menuItemKeywordHalf);
		this.menuItemKeyword.add(this.menuItemKeywordReverse);
		
		this.menuItemSettings.add(this.menuItemKeyword);
		
		this.menuItemSimulatedAnnealing.setText("Simulated Annealing");
		
		JTextField tempSetting = new JTextField(ValueFormat.getNumber(this.settings.simulatedAnnealing.get(0)));
		((AbstractDocument)tempSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentDoubleInput(tempSetting));
		tempSetting.addKeyListener(new SimulatedAnnealingAction(tempSetting, 0));
		this.menuItemSimulatedAnnealing.add(new JLabel("Temperature Value"));
		this.menuItemSimulatedAnnealing.add(tempSetting);
		this.menuItemSimulatedAnnealing.addSeparator();
		JTextField tempStepSetting = new JTextField(ValueFormat.getNumber(this.settings.simulatedAnnealing.get(1)));
		((AbstractDocument)tempStepSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentDoubleInput(tempStepSetting));
		tempStepSetting.addKeyListener(new SimulatedAnnealingAction(tempStepSetting, 1));
		this.menuItemSimulatedAnnealing.add(new JLabel("Temperature Step"));
		this.menuItemSimulatedAnnealing.add(tempStepSetting);
		this.menuItemSimulatedAnnealing.addSeparator();
		JTextField countSetting = new JTextField(ValueFormat.getNumber(this.settings.simulatedAnnealing.get(2)));
		((AbstractDocument)countSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		countSetting.addKeyListener(new SimulatedAnnealingAction(countSetting, 2));
		this.menuItemSimulatedAnnealing.add(new JLabel("Count"));
		this.menuItemSimulatedAnnealing.add(countSetting);
		
		this.menuItemSettings.add(this.menuItemSimulatedAnnealing);
		
		
        this.menuBar.add(this.menuItemSettings);

        
        
        
        this.setJMenuBar(this.menuBar);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        //this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        ((JPanel)this.getContentPane()).setBorder(new EmptyBorder(0, 5, 5, 5));
    }
    
    private class InputTextChange extends DocumentUtil.DocumentChangeAdapter {

		@Override
		public void onUpdate(DocumentEvent event) {
			try {
				String inputText = event.getDocument().getText(0, event.getDocument().getLength());
				String statText = "";
				
				List<Integer> factors = MathHelper.getFactors(inputText.length());
				Collections.sort(factors);
				statText +=  "Length: " + inputText.length() + " " + factors;
				statText += "\n A-Z: " + StringTransformer.countLetterChars(inputText);
				statText += "\n 0-9: " + StringTransformer.countDigitChars(inputText);
				statText += "\n ___: " + StringTransformer.countSpacesChars(inputText);
				statText += "\n *?!: " + StringTransformer.countOtherChars(inputText);
				statText += "\nSuggested Fitness: " + TextFitness.getEstimatedFitness(inputText, settings.language);
				statTextArea.setText(statText);
			} 
			catch(BadLocationException e) {
				e.printStackTrace();
			}
		}
    	
    }
    
    public boolean outFullscreen = false;
    
    private class FullScreenAction implements ActionListener {

    	public Dimension lastSize;
    	public Point lastLocation;
    	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			removeNotify();
			if(!isUndecorated()) {
				setExtendedState(Frame.MAXIMIZED_BOTH);
		    	setUndecorated(true);
		    	outFullscreen = true;
			}
			else {
				setSize(this.lastSize);
				setLocation(this.lastLocation);
				setExtendedState(Frame.NORMAL);
				setUndecorated(false);
				outFullscreen = true;
			}
			
			this.lastSize = getSize();
			this.lastLocation = getLocation();
		    addNotify();
		}
    }
    
    private class ExitAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			System.exit(128);
		}
    }
    
    public class PasteAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		try {
				String data = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
				inputTextArea.setText(data);
			} 
    		catch(Exception e) {
				e.printStackTrace();
			}
		}
    }
    
    public class WordSplitAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String split = WordSplit.splitText(inputTextArea.getText());
    		output.println(split);
		}
    }
    
    public class LetterFrequencyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chartAlphabeticly;
    	
    	public LetterFrequencyAction() {
    		addWindowStateListener(new WindowStateListener() {
    			
    			@Override
    			public void windowStateChanged(WindowEvent event) {
    				int newState = event.getNewState();
    				if((newState | Frame.ICONIFIED) == Frame.ICONIFIED) {
    					dialog.toBack();
    				}
    			}
    		});
    		addWindowListener(new WindowAdapter() {
    			
                @Override
                public void windowDeactivated(WindowEvent e) {
                	if(dialog != null && !outFullscreen) {
                		dialog.toBack();
                		toBack();
                	}
                	outFullscreen = false;
                }

                @Override
                public void windowActivated(WindowEvent e) {
                	if(dialog != null)
                		dialog.setAlwaysOnTop(true);
                }
            });
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(inputTextArea.getText().replaceAll("[^a-zA-Z]+", ""), 1, 1, false);
						
						List<String> asendingOrder = new ArrayList<String>(counts.keySet());
						Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
						Collections.reverse(asendingOrder);
						
						chart.values.clear();
				        for(String letterCount : asendingOrder)
				        	chart.values.add(new ChartData(letterCount, (double)counts.get(letterCount)));
						
						
				        chartAlphabeticly.values.clear();
						if(!counts.isEmpty())
							for(char ch = 'A'; ch <= 'Z'; ch++)
								chartAlphabeticly.values.add(new ChartData("" + ch, (double)(counts.containsKey("" + ch) ? counts.get("" + ch) : 0)));
	
						
				        chart.repaint();
				        chartAlphabeticly.repaint();
					}
				}
    			
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.setTitle("Letter Frequency");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        chart = new JBarChart(new ChartList());
	        chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
	        panel.add(chart);
	        
	        chartAlphabeticly = new JBarChart(new ChartList());
	        chartAlphabeticly.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered Alphabeticly"));
	        panel.add(chartAlphabeticly);
	         
    		this.dialog.add(panel);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		
    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(inputTextArea.getText().replaceAll("[^a-zA-Z]+", ""), 1, 1, false);
			
			List<String> asendingOrder = new ArrayList<String>(counts.keySet());
			Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
			Collections.reverse(asendingOrder);
			
			this.chart.values.clear();
	        for(String letterCount : asendingOrder)
	        	this.chart.values.add(new ChartData(letterCount, (double)counts.get(letterCount)));
			
			
	        this.chartAlphabeticly.values.clear();
	        if(!counts.isEmpty())
	        	for(char ch = 'A'; ch <= 'Z'; ch++)
	        		this.chartAlphabeticly.values.add(new ChartData("" + ch, (double)(counts.containsKey("" + ch) ? counts.get("" + ch) : 0)));
		  		
			this.chart.repaint();
			this.chartAlphabeticly.repaint();
		}
    }
    
    private class LanguageChangeAction implements ActionListener {
    	
    	public ILanguage language;
    	
    	public LanguageChangeAction(ILanguage language) {
    		this.language = language;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			settings.language = language;
			menuItemCurrentLanguage.setText("Current: " + language.getName());
		}
    }
    
    private class KeywordCreationAction implements ActionListener {
    	
    	public int id;
    	
    	public KeywordCreationAction(int id) {
    		this.id = id;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			settings.keywordCreation = id;
		}
    }
    
    private class SimulatedAnnealingAction implements KeyListener {
    	
    	public JTextComponent textComponent;
    	public int id;
    	
    	public SimulatedAnnealingAction(JTextComponent textComponent, int id) {
    		this.textComponent = textComponent;
    		this.id = id;
    	}
    	
		@Override
		public void keyPressed(KeyEvent arg0) {
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			settings.simulatedAnnealing.set(this.id, Double.valueOf(this.textComponent.getText()));
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
    }
     
    private JPanel inputPanel;
    private JScrollPane inputTextScroll;
    private JTextArea inputTextArea;
    private JTextArea statTextArea;
    private JScrollPane outputTextScroll;
    private JTextArea outputTextArea;
    private JToolBar toolBar;
    private JButton toolBarStart;
    private JButton toolBarStop;
    private JMenuBar menuBar;
    private JMenu menuItemFile;
	private JMenuItem menuItemFullScreen;
	private JMenuItem menuItemExit;
    private JMenu menuItemEdit;
    private JMenuItem menuItemPaste;
    private JMenu menuItemTools; 
    private JMenuItem menuItemLetterFrequency;
    private JMenuItem menuItemWordSplit;
    private JMenuItem menuItemInfo;
    private JMenu menuItemSettings;
    private JMenu menuItemLanguage;
    private JMenuItem menuItemCurrentLanguage;
    private JMenu menuItemKeyword;
    private JMenuItem menuItemKeywordNormal;
    private JMenuItem menuItemKeywordHalf;
    private JMenuItem menuItemKeywordReverse;
    public JMenu menuItemSimulatedAnnealing;
}
