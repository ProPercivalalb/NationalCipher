package nationalciphernew;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
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
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import javalibrary.ForceDecryptManager;
import javalibrary.IForceDecrypt;
import javalibrary.Output;
import javalibrary.cipher.stats.StatCalculator;
import javalibrary.cipher.stats.TraverseTree;
import javalibrary.cipher.stats.WordSplit;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.Timer;
import javalibrary.listener.CustomMouseListener;
import javalibrary.math.MathHelper;
import javalibrary.math.Rounder;
import javalibrary.math.Statistics;
import javalibrary.math.Units.Time;
import javalibrary.string.LetterCount;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.string.ValueFormat;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ImageUtil;
import javalibrary.swing.LayoutUtil;
import javalibrary.swing.ProgressValue;
import javalibrary.swing.SwingHelper;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import javalibrary.thread.Threads;
import nationalciphernew.cipher.DecryptionManager;
import nationalciphernew.cipher.DecryptionMethod;
import nationalciphernew.cipher.IDecrypt;

/**
 *
 * @author Alex
 */
public class UINew extends JFrame {

	public static String BEST_SOULTION = "";
	public KeyPanel keyPanel;
	
	public Settings settings;
	public Output output;

	private Thread thread;
	private Timer threadTimer;
	private List<JDialog> dialogs;
	private List<JDialog> lastStates;
	
    public UINew() {
    	super("Cryptography Solver");
    	this.settings = new Settings();
    	this.threadTimer = new Timer();
    	this.dialogs = new ArrayList<JDialog>();
    	this.lastStates = new ArrayList<JDialog>();
    	
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
		final JDialog dialog = optionPane.createDialog(this, "Loading...");
		dialog.setModal(false);
		dialog.setVisible(true);
		dialog.setLocationRelativeTo(this);
		
		//Loading
		Threads.runTask(new Runnable() {
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

	public void finishComponents() {
		this.addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent event) {
				int newState = event.getNewState();
				if((newState & Frame.ICONIFIED) == Frame.ICONIFIED) {
					for(JDialog dialog : dialogs) {
						dialog.setVisible(false);
					}
				}
			}
		});
		this.addWindowListener(new WindowAdapter() {
			
            @Override
            public void windowDeactivated(WindowEvent e) {
            	for(JDialog dialog : dialogs) {
            		dialog.setVisible(false);
            	}
            }

            @Override
            public void windowActivated(WindowEvent e) {
            	for(JDialog dialog : dialogs)
            		dialog.setVisible(lastStates.contains(dialog));
            }
        });
		
		this.output = new Output.TextComponent(this.outputTextArea);
        this.pack();
		this.setSize(900, 800);
        this.setVisible(true);
    }
                     
    private void initComponents() {
    	this.cipherSelect = new JComboBox<String>(DecryptionManager.getNames());
    	this.decryptionType = new JComboBox<DecryptionMethod>();
    	this.inputPanel = new JPanel();
        this.inputTextScroll = new JScrollPane();
        this.inputTextArea = new JTextArea();
        this.statTextArea = new JTextArea();
    	this.outputTextScroll = new JScrollPane();
        this.outputTextArea = new JTextArea();
        this.progressBar = new JProgressBar();
        this.toolBar = new JToolBar();
        this.toolBarStart = new JButton();
        this.toolBarStop = new JButton();
        this.menuBar = new JMenuBar();
        this.menuItemFile = new JMenu();
        this.menuItemFullScreen = new JMenuItem();
        this.menuItemExit = new JMenuItem();
        this.menuItemEdit = new JMenu();
        this.menuItemPaste = new JMenuItem();
        this.menuItemCopySolution = new JMenuItem();
        this.menuItemBinary = new JMenuItem();
        this.menuItemTools = new JMenu();
        this.menuItemNGram = new JMenuItem();
        this.menuItemLetterFrequency = new JMenuItem();
        this.menuItemIoC = new JMenu();
        this.menuItemIoCNormal = new JMenuItem();
        this.menuItemIoCBifid = new JMenuItem();
        this.menuItemIoCNicodemus = new JMenuItem();
        this.menuItemIoCVigenere = new JMenuItem();
        this.menuItemIdentify = new JMenuItem();
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
        

      	JButton nospaces = new JButton(" Remove _ ");
		nospaces.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = inputTextArea.getText();
				inputTextArea.setText(s.replaceAll("\\s+", ""));
			}
		});
		this.toolBar.add(nospaces);
		
		
		JButton nonletters = new JButton(" Keep A-Z ");
		nonletters.addMouseListener(new CustomMouseListener() {
			@Override
			public void mouseClicked(MouseEvent event) {
				String s = inputTextArea.getText();
				inputTextArea.setText(s.replaceAll("[^a-zA-Z]+", ""));
			}
		});
		this.toolBar.add(nonletters);
      	
		
        
        this.cipherSelect.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
        this.cipherSelect.addActionListener(new CipherSelectAction());
        this.toolBar.add(this.cipherSelect);
     		 
     	this.decryptionType.setMaximumSize(new Dimension(130, Integer.MAX_VALUE));
		List<DecryptionMethod> methods = getDecryptManager().getDecryptionMethods();
		
		for(DecryptionMethod method : methods)
			decryptionType.addItem(method);
      	this.toolBar.add(this.decryptionType);

        this.toolBarStart.setText("Execute");
        this.toolBarStart.setIcon(ImageUtil.createImageIcon("/image/accept.png", "Start"));
        this.toolBarStart.setFocusPainted(false);
        this.toolBarStart.setToolTipText("Tries to decrypt the given text.");
        this.toolBarStart.addActionListener(new ExecuteAction());
        this.toolBar.add(this.toolBarStart);
        
        this.toolBarStop.setText("Terminate");
        this.toolBarStop.setIcon(ImageUtil.createImageIcon("/image/stop.png", "Terminate"));
        this.toolBarStop.setFocusPainted(false);
        this.toolBarStop.setEnabled(false);
        this.toolBarStop.setToolTipText("Terminates the current process.");
        this.toolBarStop.addActionListener(new TerminateAction());
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
        //scrollPane.setPreferredSize(new Dimension(300, 0));
        scrollPane.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
    	scrollPane.setViewportView(this.statTextArea);
    	
	    this.inputPanel.add(scrollPane);
	    this.add(this.inputPanel, LayoutUtil.createConstraints(0, 1, 1, 0.2));
	    
	    
	    this.keyPanel = new KeyPanel();

	    this.add(this.keyPanel, LayoutUtil.createConstraints(0, 2, 0, 0));
	    
	    
	    
	    
	    
	    
	    //Output panel
        this.outputTextArea.setEditable(false);
        this.outputTextScroll.setViewportView(this.outputTextArea);
        this.outputTextScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.add(this.outputTextScroll, LayoutUtil.createConstraints(0, 3, 1, 0.5));
        
        
        this.progressBar = new JProgressBar(0, 10);
		this.progressBar.setValue(0);
		this.progressBar.setStringPainted(true);
		this.progressBar.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent event) {
				progressBar.setString(Rounder.round(progressBar.getPercentComplete() * 100, 1) + "%");
			}
	    });
		this.add(this.progressBar, LayoutUtil.createConstraints(0, 4, 1, 0.01));
        
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
        
        this.menuItemCopySolution.setText("Copy Solution");
        this.menuItemCopySolution.setIcon(ImageUtil.createImageIcon("/image/page_copy.png", "Copy Solution"));
        this.menuItemCopySolution.addActionListener(new CopySolutionAction());
        this.menuItemCopySolution.setToolTipText("Copies the best lastest solution to the system clipboard.");
        this.menuItemEdit.add(this.menuItemCopySolution);
        
        this.menuItemEdit.addSeparator();
        
        this.menuItemBinary.setText("Binary to Text");
        this.menuItemBinary.setIcon(ImageUtil.createImageIcon("/image/page_white_text.png", "Binary Convert"));
        this.menuItemBinary.addActionListener(new BinaryConvertAction());
        this.menuItemEdit.add(this.menuItemBinary);
        
        this.menuBar.add(this.menuItemEdit);

        this.menuItemTools.setText("Tools");
        this.menuItemLetterFrequency.setText("Letter Frequency");
        this.menuItemLetterFrequency.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Letter Frequency"));
        this.menuItemLetterFrequency.addActionListener(new LetterFrequencyAction());
        this.menuItemTools.add(this.menuItemLetterFrequency);
        
        
        this.menuItemNGram.setText("N-Gram Frequency");
        this.menuItemNGram.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "N-Gram Frequency"));
        this.menuItemNGram.addActionListener(new NGramFrequencyAction());
        this.menuItemTools.add(this.menuItemNGram);
        
        
        this.menuItemIoC.setText("Index of Coincidence");
        //this.menuItemIoC.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Letter Frequency"));
        this.menuItemTools.add(this.menuItemIoC);
        
        this.menuItemIoCNormal.setText("Normal");
        this.menuItemIoCNormal.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Normal IoC"));
        this.menuItemIoCNormal.addActionListener(new NormalIoCAction());
        this.menuItemIoC.add(this.menuItemIoCNormal);
        
        this.menuItemIoCBifid.setText("Bifid");
        this.menuItemIoCBifid.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Bifid IoC"));
        this.menuItemIoCBifid.addActionListener(new BifidIoCAction());
        this.menuItemIoC.add(this.menuItemIoCBifid);
        
        this.menuItemIoCNicodemus.setText("Nicodemus");
        this.menuItemIoCNicodemus.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Nicodemus"));
        this.menuItemIoCNicodemus.addActionListener(new NicodemusIoCAction());
        this.menuItemIoC.add(this.menuItemIoCNicodemus);
        
        this.menuItemIoCVigenere.setText("Vigenere");
        this.menuItemIoCVigenere.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Vigenere"));
        this.menuItemIoCVigenere.addActionListener(new VigenereIoCAction());
        this.menuItemIoC.add(this.menuItemIoCVigenere);
        
        this.menuItemTools.addSeparator();
        
        this.menuItemWordSplit.setText("Word Split");
        this.menuItemWordSplit.setIcon(ImageUtil.createImageIcon("/image/spellcheck.png", "Paste"));
        this.menuItemWordSplit.addActionListener(new WordSplitAction());
        this.menuItemTools.add(this.menuItemWordSplit);
        
        this.menuItemIdentify.setText("Identify Cipher");
        this.menuItemIdentify.setIcon(ImageUtil.createImageIcon("/image/page_white_find.png", "Idenifty Cipher"));
        this.menuItemIdentify.addActionListener(new IdentifyAction());
        this.menuItemTools.add(this.menuItemIdentify);
        
        this.menuItemInfo.setText("Text Information");
        this.menuItemInfo.setIcon(ImageUtil.createImageIcon("/image/information.png", "Information"));
        this.menuItemInfo.addActionListener(new TextInformationAction());
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
				
				menuItemBinary.setEnabled(inputText.length() != 0 && inputText.replaceAll("[^0-1]", "").length() == inputText.length());
			} 
			catch(BadLocationException e) {
				e.printStackTrace();
			}
		}
    }
    
    private class CipherSelectAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		DecryptionMethod lastMethod = (DecryptionMethod)decryptionType.getSelectedItem();
    		decryptionType.removeAllItems();
    		IDecrypt decrypt = getDecryptManager();
    		List<DecryptionMethod> methods = decrypt.getDecryptionMethods();
    		
    		for(DecryptionMethod method : methods)
    			decryptionType.addItem(method);
    		
    		if(methods.contains(lastMethod))
    			decryptionType.setSelectedItem(lastMethod);
    	}
    }
    
    private class ExecuteAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			final String text = inputTextArea.getText();
			
			if(text == null || text.isEmpty())
				return;
			
			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					threadTimer.restart();
					UINew.BEST_SOULTION = "";
					IDecrypt force = getDecryptManager();
					output.println("Cipher: " + force.getName());
					
					DecryptionMethod method = (DecryptionMethod)decryptionType.getSelectedItem();
					
					try {
						force.attemptDecrypt(text, settings, method, settings.language, output, keyPanel, new ProgressValue(1000, progressBar));
					}
					catch(Exception e) {
						output.println(e.toString());
						e.printStackTrace();
					}
					
					DecimalFormat df = new DecimalFormat("#.#");
					output.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
					output.println("");
					toolBarStart.setEnabled(true);
					toolBarStop.setEnabled(false);
					menuItemSettings.setEnabled(true);
					try {
						Thread.sleep(1000L);
					} 
					catch(InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			});
			thread.start();
			toolBarStart.setEnabled(false);
			toolBarStop.setEnabled(true);
			menuItemSettings.setEnabled(false);
		}
    }
    
    public class TerminateAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if(thread != null)
				thread.stop();
			
			DecimalFormat df = new DecimalFormat("#.#");
			output.println("Time Running: %sms - %ss", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
			output.println("");
			toolBarStart.setEnabled(true);
			toolBarStop.setEnabled(false);
			menuItemSettings.setEnabled(true);
			
			try {
				Thread.sleep(500L);
			} 
			catch(InterruptedException e) {
				e.printStackTrace();
			}
			progressBar.setMaximum(10);
			progressBar.setValue(0);
		}
    }
    
    private class FullScreenAction implements ActionListener {

    	public Dimension lastSize;
    	public Point lastLocation;
    	
		@Override
		public void actionPerformed(ActionEvent event) {
			dispose();
			if(!isUndecorated()) {
				setExtendedState(Frame.MAXIMIZED_BOTH);
		    	setUndecorated(true);
			}
			else {
				setSize(this.lastSize);
				setLocation(this.lastLocation);
				setExtendedState(Frame.NORMAL);
				setUndecorated(false);
			}
			
			this.lastSize = getSize();
			this.lastLocation = getLocation();
			setVisible(true);
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

    public class CopySolutionAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		if(!BEST_SOULTION.isEmpty()) {
	    		StringSelection selection = new StringSelection(BEST_SOULTION);
	    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    		}
		}
    }
    
    public class BinaryConvertAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = inputTextArea.getText();
			List<String> split = StringTransformer.splitInto(binaryText, (int)5);
			
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
			inputTextArea.setText(cipherText);
		}
    }
    
    public class WordSplitAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JTextArea textOutput;
    	
    	public WordSplitAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Word Split");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 200));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.textOutput = new JTextArea();
	        this.textOutput.setLineWrap(true);
	        panel.add(this.textOutput);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		String split = WordSplit.splitText(inputTextArea.getText().replaceAll(" ", ""));
    		textOutput.setText(split);
    	}
    }
    
    private class JDialogCloseEvent extends WindowAdapter {
    	
    	private JDialog dialog;
    	
    	public JDialogCloseEvent(JDialog dialog) {
    		this.dialog = dialog;
    		dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    	}
    	
		@Override
		public void windowClosed(WindowEvent event) {
			
			lastStates.remove(dialog);
			
		}
    }
    
    public class LetterFrequencyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chartAlphabeticly;
    	
    	public LetterFrequencyAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}
				}
    			
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Letter Frequency");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
	        panel.add(this.chart);
	        
	        this.chartAlphabeticly = new JBarChart(new ChartList());
	        this.chartAlphabeticly.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered Alphabeticly"));
	        panel.add(this.chartAlphabeticly);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
	        this.chartAlphabeticly.resetAll();
    		
    		String text = getInputTextOnlyAlpha();
			
    		if(!text.isEmpty()) {
	    		
	    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(text, 1, 1, false);
				
				List<String> asendingOrder = new ArrayList<String>(counts.keySet());
				Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
				Collections.reverse(asendingOrder);
				
		        for(String letterCount : asendingOrder)
		        	this.chart.values.add(new ChartData(letterCount, (double)counts.get(letterCount)));
				
				
		        if(!counts.isEmpty())
		        	for(char ch = 'A'; ch <= 'Z'; ch++)
		        		this.chartAlphabeticly.values.add(new ChartData("" + ch, (double)(counts.containsKey("" + ch) ? counts.get("" + ch) : 0)));
    		}
    		
			this.chart.repaint();
			this.chartAlphabeticly.repaint();
    	}
    }
    
    private class NGramFrequencyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JComboBox<String> comboBox;
    	
    	public NGramFrequencyAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}
				}
    			
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("N-Gram Frequency");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(900, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart();
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
	        panel.add(this.chart);

	        this.comboBox = new JComboBox<String>(new String[] {"ALL", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
	        this.comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	        this.comboBox.addItemListener(new ItemListener() {
				@Override
			    public void itemStateChanged(ItemEvent event) {
					if(event.getStateChange() == ItemEvent.SELECTED) {
						updateDialog();
			       }
			    }       
			});
	        panel.add(this.comboBox);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
    		
    		String text = getInputTextOnlyAlpha();
			
    		if(!text.isEmpty()) {
	    		
	    		
	    		String label = (String)this.comboBox.getSelectedItem();
				int minlength = 2;
	      		int maxlength = 15;
	      		if(!label.contains("ALL")) {
	      			minlength = Integer.valueOf(label);
	      			maxlength = minlength;
	      		}
    			
	    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(text, minlength, maxlength, true);
				
	    		List<String> asendingOrder = new ArrayList<String>(counts.keySet());
				Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
				Collections.reverse(asendingOrder);
	    		
		        for(String ngram : asendingOrder)
		        	if(this.chart.values.size() < 40)
		        		this.chart.values.add(new ChartData(ngram, (double)counts.get(ngram)));
    		}
    		
			this.chart.repaint();
    	}
    }
    
    private class NormalIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public NormalIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Normal IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Normal IoC Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 0; period <= Math.min(40, text.length()); ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.language.getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    			
    		    this.chart.setSelected(bestPeriod);
    		}
    		
    		this.chart.repaint();
    	}
    }
    
    public class BifidIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public BifidIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Bifid IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Step Calculation"));
	        panel.add(this.chart);
	        
	        this.chart2 = new JBarChart(new ChartList());
	        this.chart2.setHasBarText(false);
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart2);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		lastStates.add(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		this.chart.resetAll();
    		this.chart2.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
				
				Map<Integer, Double> values = new HashMap<Integer, Double>();
				double maxValue = Double.MIN_VALUE;
				int maxStep = -1;
				
				double secondValue = Double.MIN_VALUE;
				
				for(int step = 1; step <= Math.min(40, text.length()); step++) {
					HashMap<String, Integer> counts = new HashMap<String, Integer>();
					for(int i = 0; i < text.length() - step; i++) {
						String s = text.charAt(i) + "" + text.charAt(i + step);
						counts.put(s, counts.containsKey(s) ? counts.get(s) + 1 : 1);
					}
					
					Statistics stats = new Statistics(counts.values());
				    double variance = stats.getVariance();
				 
				    this.chart.values.add(new ChartData("Step: " + step, variance));
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
	
				
				int periodGuess = -1;
	
				if(maxStep != -1) {
					if(maxValue - maxValue / 4 > secondValue)
						periodGuess = maxStep * 2;
					else {
						double max = Double.MAX_VALUE;
						int bestStep = 0;
						
						for(int step = maxStep - 1; step <= maxStep + 1; step++) {
							if(!values.containsKey(step) || step == maxStep)
								continue;
							
							double diff = Math.abs(values.get(maxStep) - values.get(step));
							if(diff < max) {
								max = diff;
								bestStep = step;
							}
						}
						this.chart.setSelected(bestStep - 1);
						
						periodGuess = Math.min(bestStep, maxStep) * 2 + Math.abs(bestStep - maxStep);
					}
				}
				
				int bestPeriod = -1;
				double bestIC = Double.MIN_VALUE;
			    for(int period = 0; period <= 40; period++) {
			    	if(period == 1) continue;
			    	
			        double score = StatCalculator.calculateBifidDiagraphicIC(text, period);
			        this.chart2.values.add(new ChartData("Period: " + period, score));
			        if(bestIC < score)
			        	bestPeriod = period;
			        
			        bestIC = Math.max(bestIC, score);
			    }
			    
			    this.chart.setSelected(maxStep - 1);
				this.chart2.setSelected(bestPeriod > 0 ? bestPeriod - 1 : 0);
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    private class NicodemusIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public NicodemusIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Nicodemus IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestIC = Double.POSITIVE_INFINITY;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateNicodemusIC(text, 5, period) - settings.language.getNormalCoincidence(), 2) * 10000;
    		    	
    		    	if(sqDiff < bestIC)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIC = Math.min(bestIC, sqDiff);
    		    }
    			
    		    this.chart.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    	}
    }
    
    private class VigenereIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public VigenereIoCAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Vigenere IoC");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.language.getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    		    
    		    this.chart.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    	}
    }
    
    private class IdentifyAction implements ActionListener {
    	
    	public IdentifyAction() {
    		
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		
		}
    }
     
    private class TextInformationAction implements ActionListener {
    	
    	private JDialog dialog;
    	public JTextArea output;
    	
    	public TextInformationAction() {
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Text Statistics");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(375, 600));
	       
	        this.output = new JTextArea();
			JScrollPane outputScrollPanel = new JScrollPane(this.output);
			outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			outputScrollPanel.setPreferredSize(new Dimension(1000, 200));
			this.output.setEditable(false);
			this.output.setLineWrap(true);
		
	
    		this.dialog.add(outputScrollPanel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		lastStates.add(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			
			
    		String text = getInputTextOnlyAlpha();
    		int length = text.length();
    		
    		String outputText = "Length: " + length;
			outputText += "\nEstimated Fitness for length: " + Rounder.round(TextFitness.getEstimatedFitness(text, settings.language), 4);
    		
    		if(!text.isEmpty()) {

    			outputText += "\n IC: " + StatCalculator.calculateIC(text);
    			outputText += "\n MIC: " + StatCalculator.calculateMaxIC(text, 1, 15);
    		    outputText += "\n MKA: " + StatCalculator.calculateMaxKappaIC(text, 1, 15);
    		    outputText += "\n DIC: " + StatCalculator.calculateDiagrahpicIC(text);
    		    outputText += "\n EDI: " + StatCalculator.calculateEvenDiagrahpicIC(text) * 10000;
    		    outputText += "\n LR: " + StatCalculator.calculateLR(text);
    		    outputText += "\n ROD: " + StatCalculator.calculateROD(text);
    		    outputText += "\n LDI: " + StatCalculator.calculateLDI(text);
    		    outputText += "\n SDD: " + StatCalculator.calculateSDD(text);

    		    outputText += "\n A_LDI: " + StatCalculator.calculateALDI(text);
    		    outputText += "\n B_LDI: " + StatCalculator.calculateBLDI(text);
    		    outputText += "\n P_LDI: " + StatCalculator.calculatePLDI(text);
    		    outputText += "\n S_LDI: " + StatCalculator.calculateSLDI(text);
    		    outputText += "\n V_LDI: " + StatCalculator.calculateVLDI(text);
    		    
    		    outputText += "\n NOMOR: " + StatCalculator.calculateNormalOrder(text, settings.language);
    		    outputText += "\n RDI: " + StatCalculator.calculateRDI(text);
    		    outputText += "\n PTX: " + StatCalculator.calculatePTX(text);
    		    outputText += "\n NIC: " +  StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
    		    outputText += "\n PHIC: " + StatCalculator.calculatePHIC(text);
    		    outputText += "\n BDI: " +  StatCalculator.calculateBestBifidDiagraphicIC(text, 3, 15);
    		    outputText += "\n CDD: " +  StatCalculator.calculateCDD(text);
    		    outputText += "\n SSTD: " +  StatCalculator.calculateSSTD(text);
    		    outputText += "\n MPIC: " + StatCalculator.calculateMPIC(text);
    		    outputText += "\n SERP: " + StatCalculator.calculateSeriatedPlayfair(text);
    		    
    		    
    		    outputText += "\n DIV_2: " + StatCalculator.isLengthDivisible2(text);
    		    outputText += "\n DIV_3: " + StatCalculator.isLengthDivisible3(text);
    		    outputText += "\n DIV_5: " + StatCalculator.isLengthDivisible5(text);
    		    outputText += "\n DIV_25: " + StatCalculator.isLengthDivisible25(text);
    		    outputText += "\n DIV_4_15: " + StatCalculator.isLengthDivisible4_15(text);
    		    outputText += "\n DIV_4_30: " + StatCalculator.isLengthDivisible4_30(text);
    		    List<Integer> factors =  MathHelper.getFactors(length);
    		    Collections.sort(factors);
    		    outputText += "\n DIV_N: " + factors;
    		    outputText += "\n PSQ: " + StatCalculator.isLengthPerfectSquare(text);
    		    outputText += "\n HAS_LETTERS: " + StatCalculator.containsLetter(text);
    		    outputText += "\n HAS_DIGITS: " + StatCalculator.containsDigit(text);
    		    outputText += "\n HAS_J: " + StatCalculator.containsJ(text);
    		    outputText += "\n HAS_#: " + StatCalculator.containsHash(text);
    		    outputText += "\n HAS_0: " + StatCalculator.calculateHAS0(text);
    		    outputText += "\n DBL: " + StatCalculator.calculateDBL(text);
    		    int lastPos = this.output.getCaretPosition();
    		    this.output.setText(outputText);
    		    this.output.setCaretPosition(lastPos);
    		}
    		else {
    			outputText += "\n IC: 0.0";
    			outputText += "\n MIC: 0.0";
    			outputText += "\n MKA: 0.0";
    			outputText += "\n DIC: 0.0";
    			outputText += "\n EDI: 0.0";
    			outputText += "\n LR: 0.0";
    			outputText += "\n ROD: 0.0";
    			outputText += "\n LDI: 0.0";
    		    
    			outputText += "\n SDD: 0.0";
    		    
    			outputText += "\n A_LDI: 0.0";
    			outputText += "\n B_LDI: 0.0";
    			outputText += "\n P_LDI: 0.0";
    			outputText += "\n S_LDI: 0.0";
    			outputText += "\n V_LDI: 0.0";
    		    
    			outputText += "\n NOMOR: 0.0";
    			outputText += "\n RDI: 0.0";
    			outputText += "\n PTX: 0.0";
    			outputText += "\n NIC: 0.0";
    			outputText += "\n PHIC: 0.0";
    			outputText += "\n BDI: 0.0";
    			outputText += "\n CDD: 0.0";
    		    outputText += "\n SSTD: 0.0";
    		    outputText += "\n MPIC: 0.0";
    		    outputText += "\n SERP: 0.0";
    		    
    		    
    		    outputText += "\n DIV_2: true";
    		    outputText += "\n DIV_3: true";
    		    outputText += "\n DIV_5: true";
    		    outputText += "\n DIV_25: true";
    		    outputText += "\n DIV_4_15: true";
    		    outputText += "\n DIV_4_30: true";
    		    outputText += "\n DIV_N: []";
    		    outputText += "\n PSQ: true";
    		    outputText += "\n HAS_LETTERS: false";
    		    outputText += "\n HAS_DIGITS: false";
    		    outputText += "\n HAS_J: false";
    		    outputText += "\n HAS_#: false";
    		    outputText += "\n HAS_0: false";
    		    outputText += "\n DBL: false";
    		}
    		int lastPos = this.output.getCaretPosition();
 		    this.output.setText(outputText);
 		    this.output.setCaretPosition(lastPos);
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
    
    public String getInputTextOnlyAlpha() {
    	return this.inputTextArea.getText().replaceAll("[^a-zA-Z]+", "");
    }
    
    public IDecrypt getDecryptManager() {
    	return DecryptionManager.ciphers.get(cipherSelect.getSelectedIndex());
    }
     
    private JComboBox<String> cipherSelect;
    private JComboBox<DecryptionMethod> decryptionType;
    private JPanel inputPanel;
    private JScrollPane inputTextScroll;
    private JTextArea inputTextArea;
    private JTextArea statTextArea;
    private JScrollPane outputTextScroll;
    private JTextArea outputTextArea;
    private JProgressBar progressBar;
    private JToolBar toolBar;
    private JButton toolBarStart;
    private JButton toolBarStop;
    private JMenuBar menuBar;
    private JMenu menuItemFile;
	private JMenuItem menuItemFullScreen;
	private JMenuItem menuItemExit;
    private JMenu menuItemEdit;
    private JMenuItem menuItemPaste;
    private JMenuItem menuItemCopySolution;
    private JMenuItem menuItemBinary;
    private JMenu menuItemTools; 
    private JMenuItem menuItemLetterFrequency;
    private JMenuItem menuItemNGram;
    private JMenu menuItemIoC;
    private JMenuItem menuItemIoCNormal;
    private JMenuItem menuItemIoCBifid;
    private JMenuItem menuItemIoCNicodemus;
    private JMenuItem menuItemIoCVigenere;
    private JMenuItem menuItemIdentify;
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
