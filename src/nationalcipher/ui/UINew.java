package nationalcipher.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.Image;
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
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
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
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import javalibrary.Output;
import javalibrary.cipher.permentate.PermentateArray;
import javalibrary.cipher.permentate.Permentations;
import javalibrary.cipher.stats.TraverseTree;
import javalibrary.cipher.stats.WordSplit;
import javalibrary.dict.Dictionary;
import javalibrary.fitness.TextFitness;
import javalibrary.language.ILanguage;
import javalibrary.language.Languages;
import javalibrary.lib.OSIdentifier;
import javalibrary.lib.Timer;
import javalibrary.listener.CustomMouseListener;
import javalibrary.math.MathUtil;
import javalibrary.math.Rounder;
import javalibrary.math.Statistics;
import javalibrary.math.Units.Time;
import javalibrary.string.StringAnalyzer;
import javalibrary.string.StringTransformer;
import javalibrary.string.ValueFormat;
import javalibrary.swing.ButtonUtil;
import javalibrary.swing.DocumentUtil;
import javalibrary.swing.ImageUtil;
import javalibrary.swing.LayoutUtil;
import javalibrary.swing.MenuScroller;
import javalibrary.swing.ProgressValue;
import javalibrary.swing.SwingHelper;
import javalibrary.swing.chart.ChartData;
import javalibrary.swing.chart.ChartList;
import javalibrary.swing.chart.JBarChart;
import javalibrary.thread.ThreadCancelable;
import javalibrary.thread.Threads;
import javalibrary.util.ArrayUtil;
import javalibrary.util.ListUtil;
import javalibrary.util.MapHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.LoadElement;
import nationalcipher.Settings;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.ProgressiveKey;
import nationalcipher.cipher.base.RandomEncrypter;
import nationalcipher.cipher.base.other.Solitaire;
import nationalcipher.cipher.base.other.Solitaire.SolitaireAttack;
import nationalcipher.cipher.base.transposition.ColumnarRow;
import nationalcipher.cipher.decrypt.complete.AttackRegistry;
import nationalcipher.cipher.decrypt.complete.CipherAttack;
import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.manage.DecryptionManager;
import nationalcipher.cipher.manage.DecryptionMethod;
import nationalcipher.cipher.manage.Solution;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.StatisticHandler;

/**
 *
 * @author Alex
 */
public class UINew extends JFrame implements IApplication {

	public static char[] BEST_SOULTION;
	public static ShowTopSolutionsAction topSolutions;
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
    	
    	AttackRegistry.loadCiphers(this.settings);
    	DecryptionManager.loadCiphers(this.settings);
    	this.settings.readFromFile();
    	
        initComponents();
        finishComponents();
        loadDataFiles();
    }
    
    public void addDialog(JDialog dialog) {
    	lastStates.add(dialog);
    	changeDialog(dialog);
    }
    
    public void removeDialog(JDialog dialog) {
    	lastStates.remove(dialog);
    	changeDialog(dialog);
    }
    
    public void changeDialog(JDialog dialog) {
    	this.menuScreenShot.removeAll();
    	for(final JDialog listDialog : lastStates) {
    		JMenuItem jmi = new JMenuItem(listDialog.getTitle());
    		this.menuScreenShot.add(jmi);
    		jmi.addActionListener(new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent event) {
    	        	listDialog.setBackground(Color.red);
    	        	BufferedImage image = new BufferedImage(listDialog.getContentPane().getWidth(), listDialog.getContentPane().getHeight(), BufferedImage.TYPE_INT_RGB);
    	        	listDialog.getContentPane().paint(image.getGraphics());
    	          //  JOptionPane.showMessageDialog(null, new JLabel(new ImageIcon(image)));
    	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd_hh.mm.ss");
    	            String dateTime = sdf.format(Calendar.getInstance().getTime());
    	            try {
    	            	ImageIO.write(image, "png", new File(OSIdentifier.getMyDataFolder("nationalcipher/screenshots"), "screenshot" + dateTime + ".png"));
    	            }
    	          	catch(Exception e) {
    	          		e.printStackTrace();
    	          	}
    	        }
    		});
    	}
    	this.menuScreenShot.setEnabled(!lastStates.isEmpty());
    }

    public void loadDataFiles() {
    	final Map<Component, Boolean> stateMap = SwingHelper.disableAllChildComponents((JComponent)getContentPane(), menuBar);
    	
    	
    	this.progressBar.setMaximum(Languages.languages.size() + 4);
		//Loading
		Threads.runTask(new Runnable() {
			@Override
			public void run() {
				output.println("Loading data files\n	TranverseTree");
				TraverseTree.onLoad();
				progressBar.setValue(progressBar.getValue() + 1);
				output.println("	Dictinary");
				Dictionary.onLoad();
				progressBar.setValue(progressBar.getValue() + 1);
				output.println("	Word statitics");
				WordSplit.loadFile();
				progressBar.setValue(progressBar.getValue() + 1);
				
				for(ILanguage language : Languages.languages) {
					output.println("	Lang(" + language.getName() + ")");
					language.loadNGramData();
					progressBar.setValue(progressBar.getValue() + 1);
				}
				
				StatisticHandler.registerStatistics();
				progressBar.setValue(progressBar.getValue() + 1);
				  
				BufferedReader updateReader3 = new BufferedReader(new InputStreamReader(TraverseTree.class.getResourceAsStream("/javalibrary/cipher/stats/trigraph.txt")));

				String[] split = null;
				try {
					split = updateReader3.readLine().split(",");
				} catch (IOException e) {
					e.printStackTrace();
				}
				int i = 0;
				for(String s : split) {
					StatCalculator.bstd[i] = Integer.valueOf(s);
					i += 1;
				}
				
				SwingHelper.rewindAllChildComponents(stateMap);
		
				progressBar.setValue(0);
				output.clear();
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
		
		this.output = new Output.TextComponent(this.outputTextArea, this.outputTextScroll);
        this.pack();
		this.setSize(900, 800);
        this.setVisible(true);
    }
                     
    private void initComponents() {
    	this.cipherSelect = new JComboBox<String>(AttackRegistry.getNames());
    	this.decryptionType = new JComboBox<DecryptionMethod>();
    	this.inputPanel = new JPanel();
        this.inputTextScroll = new JScrollPane();
        this.inputTextArea = new JTextArea(1, 1);
        this.statTextArea = new JTextArea(1, 1);
    	this.outputTextScroll = new JScrollPane();
        this.outputTextArea = new JTextArea(1, 1);
        this.progressBar = new JProgressBar();
        this.toolBar = new JToolBar();
        this.toolBarSettings = new JButton();
        this.toolBarStart = new JButton();
        this.toolBarStop = new JButton();
        this.menuBar = new JMenuBar();
        this.menuItemFile = new JMenu();
        this.menuItemFullScreen = new JMenuItem();
        this.menuScreenShot = new JMenu();
        this.menuItemExit = new JMenuItem();
        this.menuItemEdit = new JMenu();
        this.menuItemPaste = new JMenuItem();
        this.menuItemCopySolution = new JMenuItem();
        this.menuItemShowTopSolutions = new JMenuItem();
        this.menuItemBinary = new JMenuItem();
        this.menuItemASCII = new JMenuItem();
        this.menuItemShuffle = new JMenuItem();
        this.menuItemReverseText = new JMenuItem();
        this.menuItemTools = new JMenu();
        this.menuItemNGram = new JMenuItem();
        this.menuItemLetterFrequency = new JMenuItem();
        this.menuItemIoC = new JMenu();
        this.menuItemIoCADFGX = new JMenuItem();
        this.menuItemIoCNormal = new JMenuItem();
        this.menuItemIoCBifid = new JMenuItem();
        this.menuItemIoCNicodemus = new JMenuItem();
        this.menuItemIoCNihilist = new JMenuItem();
        this.menuItemIoCPortax = new JMenuItem();
        this.menuItemIoCProgKey = new JMenuItem();
        this.menuItemIoCSeriatedPlayfair = new JMenuItem();
        this.menuItemIoCSlidefair = new JMenuItem();
        this.menuItemIoCSwagman = new JMenuItem();
        this.menuItemIoCTrifid = new JMenuItem();
        this.menuItemIoCVigenere = new JMenuItem();
        this.menuItemSolitaire = new JMenuItem();
        this.menuItemIdentify = new JMenuItem();
        this.menuItemWordSplit = new JMenuItem();
        this.menuItemInfo = new JMenuItem();
        this.menuItemEncrypter = new JMenu();
        this.menuItemEncode = new JMenuItem();
        this.menuItemEncodeChose = new JMenu();
        this.menuItemSettings = new JMenu();
        this.menuItemLanguage = new JMenu();
        this.menuItemCurrentLanguage = new JMenuItem();
        this.menuItemKeyword = new JMenu();
		this.menuItemKeywordNormal = new JCheckBoxMenuItem();
		this.menuItemKeywordHalf = new JCheckBoxMenuItem();
		this.menuItemKeywordReverse = new JCheckBoxMenuItem();
		this.menuItemSimulatedAnnealing = new JMenu();
        this.menuItemSAPreset = new JMenu();
        this.menuItemUpdateProgress = new JCheckBoxMenuItem();
        this.menuCipherAttack = new JMenu();
        this.menuCribInput = new JMenuItem();
        this.menuItemCurrentAttack = new JMenuItem();
        
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
        
		
		
		//this.cipherSelect.setText("DECRYPTION");
        this.cipherSelect.setMaximumSize(new Dimension(180, Integer.MAX_VALUE));
        this.cipherSelect.addActionListener(new CipherSelectAction());
        this.toolBar.add(this.cipherSelect);
     		 
     	this.decryptionType.setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
		List<DecryptionMethod> methods = getCipherAttack().getAttackMethods();
		
		for(DecryptionMethod method : methods)
			decryptionType.addItem(method);
      	this.toolBar.add(this.decryptionType);

      	this.toolBarSettings.setText("Settings");
        this.toolBarSettings.setIcon(ImageUtil.createImageIcon("/image/cog_edit.png", "Settings"));
        this.toolBarSettings.setFocusPainted(false);
        this.toolBarSettings.setToolTipText("Edits the settings for the current selected cipher.");
        this.toolBarSettings.addActionListener(new CipherSettingsAction());
        this.toolBar.add(this.toolBarSettings);
      	
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
        //scrollPane.setMinimumSize(new Dimension(300, 300));
        //scrollPane.setPreferredSize(new Dimension(300, 0));
        //scrollPane.setMaximumSize(new Dimension(300, Integer.MAX_VALUE));
    	scrollPane.setViewportView(this.statTextArea);
    	
	    this.inputPanel.add(scrollPane);
	    this.add(this.inputPanel, LayoutUtil.createConstraints(0, 1, 1, 0.2));
	    
	    
	    this.keyPanel = new KeyPanel(this.settings);

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
				if(settings.updateProgress())
					progressBar.setString(Rounder.round(progressBar.getPercentComplete() * 100, 1) + "%");
			}
	    });
		this.add(this.progressBar, LayoutUtil.createConstraints(0, 4, 1, 0.01));
        
        this.menuItemFile.setText("File");
        
        this.menuItemFullScreen.setText("Full Screen");
        this.menuItemFullScreen.setIcon(ImageUtil.createImageIcon("/image/page_white_magnify.png", "Full Screen"));
        this.menuItemFullScreen.addActionListener(new FullScreenAction());
        this.menuItemFile.add(this.menuItemFullScreen);
        
        this.menuScreenShot.setText("Take Picture");
        this.menuScreenShot.setIcon(ImageUtil.createImageIcon("/image/picture_save.png", "Take Picture"));
        this.menuScreenShot.setEnabled(false);
        this.menuItemFile.add(this.menuScreenShot);
        
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
        
        this.menuItemShowTopSolutions.setText("Top Solutions");
       // this.menuItemShowTopSolutions.setIcon(ImageUtil.createImageIcon("/image/page_copy.png", "Top Solutions"));
        this.menuItemShowTopSolutions.addActionListener(topSolutions = new ShowTopSolutionsAction());
        this.menuItemShowTopSolutions.setToolTipText("Shows the top solutions.");
        this.menuItemEdit.add(this.menuItemShowTopSolutions);
        
        this.menuItemEdit.addSeparator();
        
        this.menuItemBinary.setText("Binary to Text");
        this.menuItemBinary.setIcon(ImageUtil.createImageIcon("/image/page_white_text.png", "Binary Convert"));
        this.menuItemBinary.addActionListener(new BinaryConvertAction());
        this.menuItemBinary.setEnabled(false);
        this.menuItemEdit.add(this.menuItemBinary);
        
        this.menuItemASCII.setText("ASCII to Text");
        this.menuItemASCII.setIcon(ImageUtil.createImageIcon("/image/page_white_text.png", "ASCII Convert"));
        this.menuItemASCII.addActionListener(new ASCIIConvertAction());
        this.menuItemEdit.add(this.menuItemASCII);
        
        this.menuItemShuffle.setText("Shuffle Text");
        this.menuItemShuffle.setIcon(ImageUtil.createImageIcon("/image/page_white_text.png", "ASCII Convert"));
        this.menuItemShuffle.addActionListener(new ShuffleTextAction());
        this.menuItemEdit.add(this.menuItemShuffle);
        
        this.menuItemReverseText.setText("Reverse Text");
        this.menuItemReverseText.setIcon(ImageUtil.createImageIcon("/image/page_white_text.png", "Reverse Text"));
        this.menuItemReverseText.addActionListener(new ReverseTextAction());
        this.menuItemEdit.add(this.menuItemReverseText);
        
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
        
        this.menuItemIoCADFGX.setText("ADFGX");
        this.menuItemIoCADFGX.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "ADFGX IoC"));
        this.menuItemIoCADFGX.addActionListener(new ADFGXIoCAction());
        this.menuItemIoC.add(this.menuItemIoCADFGX);
        
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
        
        this.menuItemIoCNihilist.setText("Nihilist Substitution");
        this.menuItemIoCNihilist.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Nihilist"));
        this.menuItemIoCNihilist.addActionListener(new NihilistIoCAction());
        this.menuItemIoC.add(this.menuItemIoCNihilist);

        this.menuItemIoCPortax.setText("Portax");
        this.menuItemIoCPortax.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Portax"));
        this.menuItemIoCPortax.addActionListener(new PortaxIoCAction());
        this.menuItemIoC.add(this.menuItemIoCPortax);
        
        
        this.menuItemIoCProgKey.setText("Progressive Key");
        this.menuItemIoCProgKey.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Progressive Key"));
        this.menuItemIoCProgKey.addActionListener(new ProgressiveKeyIoCAction());
        this.menuItemIoC.add(this.menuItemIoCProgKey);
        
        this.menuItemIoCSeriatedPlayfair.setText("Seriated Playfair");
        this.menuItemIoCSeriatedPlayfair.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Seriated Playfair"));
        this.menuItemIoCSeriatedPlayfair.addActionListener(new SeriatedPlayfairIoCAction());
        this.menuItemIoC.add(this.menuItemIoCSeriatedPlayfair);
        
        this.menuItemIoCSlidefair.setText("Slidefair");
        this.menuItemIoCSlidefair.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Slidefair"));
        this.menuItemIoCSlidefair.addActionListener(new SlidefairIoCAction());
        this.menuItemIoC.add(this.menuItemIoCSlidefair);
        
        this.menuItemIoCSwagman.setText("Swagman");
        this.menuItemIoCSwagman.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Swagman"));
        this.menuItemIoCSwagman.addActionListener(new SwagmanTestAction());
        this.menuItemIoC.add(this.menuItemIoCSwagman);
        
        this.menuItemIoCTrifid.setText("Trifid");
        this.menuItemIoCTrifid.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Trifid"));
        this.menuItemIoCTrifid.addActionListener(new TrifidIoCAction());
        this.menuItemIoC.add(this.menuItemIoCTrifid);
        
        this.menuItemIoCVigenere.setText("Vigenere");
        this.menuItemIoCVigenere.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Vigenere"));
        this.menuItemIoCVigenere.addActionListener(new VigenereIoCAction());
        this.menuItemIoC.add(this.menuItemIoCVigenere);
  
        this.menuItemSolitaire.setText("Solitaire");
        this.menuItemSolitaire.setIcon(ImageUtil.createImageIcon("/image/playing_card.png", "Card"));
        this.menuItemSolitaire.addActionListener(new SolitaireAction());
        this.menuItemTools.add(this.menuItemSolitaire);
        
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

        this.menuItemEncrypter.setText("Encrypter");
        
        this.menuItemEncode.setText("Randomly Encode");
        this.menuItemEncode.setIcon(ImageUtil.createImageIcon("/image/key_go.png", "Encode"));
        this.menuItemEncode.addActionListener(new EncodeAction());
        this.menuItemEncode.setEnabled(false);
        this.menuItemEncrypter.add(this.menuItemEncode);
  
        
        final JSlider slider = new JSlider(JSlider.HORIZONTAL);
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(0, new JLabel("Easy"));
        labelTable.put(5, new JLabel("Hard"));
        slider.setLabelTable( labelTable );
        slider.setMinimum(0);
        slider.setMaximum(5);
        slider.setMinorTickSpacing(1);
        slider.setMajorTickSpacing(5);
        slider.setSnapToTicks(false);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setFocusable(false);
        this.menuItemEncrypter.add(slider);
        
        this.menuItemEncrypter.addSeparator();
        
        this.menuItemEncodeChose.setText("Specific");
        MenuScroller.setScrollerFor(this.menuItemEncodeChose, 15, 125, 0, 0);
        for(final IRandEncrypter encrypt : RandomEncrypter.ciphers) {
        	JMenuItem jmi = new JMenuItem(encrypt.getClass().getSimpleName());
        	jmi.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					String text = getInputTextOnlyAlpha();
	    			String cipherText = encrypt.randomlyEncrypt(text);
	 
	    			StringSelection selection = new StringSelection(cipherText);
		    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	    			
	    			output.println(cipherText);
				}
        	});
        	this.menuItemEncodeChose.add(jmi);
        }
        
        this.menuItemEncrypter.add(this.menuItemEncodeChose);
        
        this.menuBar.add(this.menuItemEncrypter);
        
        
        this.menuItemSettings.setText("Settings");
        
        this.menuItemLanguage.setText("Language");

		this.menuItemLanguage.setIcon(ImageUtil.createImageIcon("/image/globe.png", "Language"));
		this.menuItemCurrentLanguage.setText("Current: " + settings.getLanguage().getName());
		
		
		this.menuItemLanguage.add(this.menuItemCurrentLanguage);
		this.menuItemLanguage.addSeparator();
		ButtonGroup group = new ButtonGroup();
		for(ILanguage language : Languages.languages) {
			JMenuItem jmi = new JCheckBoxMenuItem(language.getName(), ImageUtil.createImageIcon(language.getImagePath(), language.getName()));
			jmi.addActionListener(new LanguageChangeAction(language));
			this.menuItemLanguage.add(jmi);
			group.add(jmi);
			if(language == this.settings.getLanguage()) jmi.setSelected(true);
		}
		this.menuItemSettings.add(this.menuItemLanguage);

		this.menuItemKeyword.setText("Keyword");
		
		this.menuItemKeywordNormal.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		this.menuItemKeywordHalf.setText("NOPQRSTUVWXYZABCDEFGHIJKLM");
		this.menuItemKeywordReverse.setText("ZYXWVUTSRQPONMLKJIHGFEDCBA");
		
		new JMenuItem[]{this.menuItemKeywordNormal, this.menuItemKeywordHalf, this.menuItemKeywordReverse}[this.settings.getKeywordCreationId()].setSelected(true);
		
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
		
		JTextField tempSetting = new JTextField(ValueFormat.getNumber(this.settings.getSATempStart()));
		((AbstractDocument)tempSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentDoubleInput(tempSetting));
		tempSetting.addKeyListener(new SimulatedAnnealingAction(tempSetting, 0));
		this.menuItemSimulatedAnnealing.add(new JLabel("Temperature Value"));
		this.menuItemSimulatedAnnealing.add(tempSetting);
		this.menuItemSimulatedAnnealing.addSeparator();
		JTextField tempStepSetting = new JTextField(ValueFormat.getNumber(this.settings.getSATempStep()));
		((AbstractDocument)tempStepSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentDoubleInput(tempStepSetting));
		tempStepSetting.addKeyListener(new SimulatedAnnealingAction(tempStepSetting, 1));
		this.menuItemSimulatedAnnealing.add(new JLabel("Temperature Step"));
		this.menuItemSimulatedAnnealing.add(tempStepSetting);
		this.menuItemSimulatedAnnealing.addSeparator();
		JTextField countSetting = new JTextField(ValueFormat.getNumber(this.settings.getSACount()));
		((AbstractDocument)countSetting.getDocument()).setDocumentFilter(new DocumentUtil.DocumentIntegerInput());
		countSetting.addKeyListener(new SimulatedAnnealingAction(countSetting, 2));
		this.menuItemSimulatedAnnealing.add(new JLabel("Count"));
		this.menuItemSimulatedAnnealing.add(countSetting);
		
		this.menuItemSAPreset.setText("Presets");
		JMenuItem saPreset1 = new JMenuItem("Substitution");
		saPreset1.addActionListener(new PresetAction(tempSetting, tempStepSetting, countSetting, 20D, 0.1D, 100));
		this.menuItemSAPreset.add(saPreset1);
		JMenuItem saPreset2 = new JMenuItem("Playfair");
		saPreset2.addActionListener(new PresetAction(tempSetting, tempStepSetting, countSetting, 20D, 0.1D, 10000));
		this.menuItemSAPreset.add(saPreset2);
		//JMenuItem saPreset3 = new JMenuItem("Substitution");
		//saPreset3.addActionListener(new PresetAction(tempSetting, tempStepSetting, countSetting, 20D, 0.1D, 100));
		//this.menuItemSAPreset.add(saPreset3);
		
		
		
		this.menuItemSimulatedAnnealing.add(this.menuItemSAPreset);
		
		this.menuItemSettings.add(this.menuItemSimulatedAnnealing);
		this.menuItemSettings.addSeparator();
		
		this.menuItemUpdateProgress.setText("Update Progress");
		this.menuItemUpdateProgress.setSelected(this.settings.updateProgress());
		this.keyPanel.setIterationUnsed();
		this.menuItemUpdateProgress.addActionListener(new UpdateProgressAction(this.menuItemUpdateProgress));
		this.menuItemSettings.add(this.menuItemUpdateProgress);		
        this.menuBar.add(this.menuItemSettings);

        this.menuCipherAttack.setText("Cipher Attack");
        
        this.menuCribInput.setText("Crib Input");
        this.menuCribInput.addActionListener(new CribInputAction());
        this.menuCipherAttack.add(this.menuCribInput);
        
        this.menuItemCurrentAttack.setText("Target: Caesar Shift");
		
		
		this.menuCipherAttack.add(this.menuItemCurrentAttack);
		this.menuCipherAttack.addSeparator();
        
        ButtonGroup cipherAttackGroup = new ButtonGroup();
        JMenu substitution = new JMenu("Substitution");
        JMenu transpostion = new JMenu("Transpostion");
        
        for(String name : new String[] {"Caesar Shift", "Affine", "Simple Subsitution", "Bazeries"}) {
	        JMenuItem attackButton = new JCheckBoxMenuItem(name);
	        if(name.equals("Caesar Shift")) attackButton.setSelected(true);
	        attackButton.addActionListener(new CipherAttackChangeAction(attackButton));
	        substitution.add(attackButton);
	        cipherAttackGroup.add(attackButton);
        }
        for(String name : new String[] {"Rail Fence", "Redefence", "Bazeries"}) {
	        JMenuItem attackButton = new JCheckBoxMenuItem(name);
	        attackButton.addActionListener(new CipherAttackChangeAction(attackButton));
	        transpostion.add(attackButton);
	        cipherAttackGroup.add(attackButton);
        }
        
        
        this.menuCipherAttack.add(substitution);
        this.menuCipherAttack.add(transpostion);
        
        this.menuBar.add(this.menuCipherAttack);
        
        
        this.setJMenuBar(this.menuBar);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
        
        //this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        ((JPanel)this.getContentPane()).setBorder(new EmptyBorder(0, 5, 5, 5));
    }
    
    private class NCCDialog {
    	
    	public JDialog dialog;
    	
    	public NCCDialog(String title, String icon) {
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle(title);
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);

    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource(icon)));
    		this.dialog.setFocusableWindowState(false);
    		dialogs.add(this.dialog);
    	}
    }
    
    private class InputTextChange extends DocumentUtil.DocumentChangeAdapter {

		@Override
		public void onUpdate(DocumentEvent event) {
			try {
				String inputText = event.getDocument().getText(0, event.getDocument().getLength());
				String statText = "";
				
				List<Integer> factors = MathUtil.getFactors(inputText.length());
				Collections.sort(factors);
				statText +=  "Length: " + inputText.length() + " " + factors;
				statText += "\n A-Z: " + StringTransformer.countLetterChars(inputText);
				statText += "\n 0-9: " + StringTransformer.countDigitChars(inputText);
				statText += "\n ___: " + StringTransformer.countSpacesChars(inputText);
				statText += "\n *?!: " + StringTransformer.countOtherChars(inputText);
				statText += "\n Unique Characters: " + StringTransformer.countUniqueChars(inputText);
				statText += "\nSuggested Fitness: " + TextFitness.getEstimatedFitness(inputText, settings.getLanguage());
				statText += "\nActual Fitness: " + TextFitness.scoreFitnessQuadgrams(inputText, settings.getLanguage());
				statTextArea.setText(statText);
				
				menuItemBinary.setEnabled(inputText.length() != 0 && inputText.replaceAll("[^0-1]", "").length() == inputText.length());
				menuItemEncode.setEnabled(inputText.length() != 0);
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
    		CipherAttack decrypt = getCipherAttack();
    		List<DecryptionMethod> methods = decrypt.getAttackMethods();
    		
    		for(DecryptionMethod method : methods)
    			decryptionType.addItem(method);
    		
    		if(methods.contains(lastMethod))
    			decryptionType.setSelectedItem(lastMethod);
    	}
    }
    
    private class CipherSettingsAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		JDialog dialog = new JDialog(UINew.this);
    		
    		dialog.setTitle("Cipher Settings");
    		
    		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/cog.png")));
    		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		dialog.setResizable(false);
    		dialog.setMinimumSize(new Dimension(400, 200));
    		dialog.setModal(true);
   
    		ActionListener escListener = new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	        	dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
    	        }
    	    };

    	    dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    		
    		JPanel panel = new JPanel();
    		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 2, 2, 2), BorderFactory.createEtchedBorder()));
    	    dialog.add(panel);
    		
	        CipherAttack force = getCipherAttack();
	        force.createSettingsUI(dialog, panel);
    		dialog.setLocationRelativeTo(UINew.this);
    		
	        
    		dialog.pack();
			dialog.setVisible(true);
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
					UINew.BEST_SOULTION = null;
					UINew.this.topSolutions.solutions.clear();
					
					CipherAttack force = getCipherAttack();
					output.println("Cipher: " + force.getDisplayName());
					
					DecryptionMethod method = (DecryptionMethod)decryptionType.getSelectedItem();
					progressValue = new ProgressValueNC(1000, progressBar, settings);
					if(!settings.updateProgress())
						UINew.this.progressValue.setIndeterminate(true);
					try {
						force.attemptAttack(text, method, UINew.this);
					}
					catch(Exception e) {
						output.println(e.toString());
						e.printStackTrace();
					}
					force.onTermination(false);
					DecimalFormat df = new DecimalFormat("#.#");
					output.println("Time Running: %sms - %ss - %sm\n", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)), df.format(threadTimer.getTimeRunning(Time.MINUTE)));
		
					toolBarStart.setEnabled(true);
					toolBarStop.setEnabled(false);
					menuItemSettings.setEnabled(true);
					
					UINew.this.progressValue.setIndeterminate(false);
					progressBar.setValue(0);
				}
				
			});
			thread.setDaemon(true);
			toolBarStart.setEnabled(false);
			toolBarStop.setEnabled(true);
			menuItemSettings.setEnabled(false);
			thread.start();
		}
    }
    
    public class TerminateAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if(thread != null) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						thread.stop();
						CipherAttack force = getCipherAttack();
						force.onTermination(true);
						DecimalFormat df = new DecimalFormat("#.#");
						output.println("Time Running: %sms - %ss\n", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)));
						toolBarStart.setEnabled(true);
						toolBarStop.setEnabled(false);
						menuItemSettings.setEnabled(true);

						UINew.this.progressValue.setIndeterminate(false);
						progressBar.setValue(0);
						System.gc();
					}
					
				});
			
			}
		}
    }
    
    private class FullScreenAction implements ActionListener {

    	public Dimension lastSize;
    	public Point lastLocation;
    	public int lastState;
    	
		@Override
		public void actionPerformed(ActionEvent event) {
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					dispose();
					if(!isUndecorated()) {
						lastSize = getSize();
						lastLocation = getLocation();
						lastState = getExtendedState();
						setExtendedState(Frame.MAXIMIZED_BOTH);
				    	setUndecorated(true);
					}
					else {
						setSize(lastSize);
						setLocation(lastLocation);
						setExtendedState(lastState);
						setUndecorated(false);
					}
					
					setVisible(true);
				}
			});
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
    		if(BEST_SOULTION != null) {
	    		StringSelection selection = new StringSelection(new String(BEST_SOULTION));
	    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    		}
		}
    }
    
    public class ShowTopSolutionsAction extends NCCDialog implements ActionListener {
    	
    	private JTextArea textOutput;
    	public List<Solution> solutions;
    	private boolean updateNeed;
    	
    	public ShowTopSolutionsAction() {
    		super("Top Solutions", "image/lock_break.png");
    		this.dialog.setMinimumSize(new Dimension(900, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        this.textOutput = new JTextArea();

	        JScrollPane scrollPane = new JScrollPane(this.textOutput);
	
	        panel.add(scrollPane);
	        
	        JButton button = new JButton("Sort lastest solutions");
	        //button.setMinimumSize(new Dimension(20, 0));
	        
	        
	        button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					sortSolutions();
					updateNeed = false;
				}
	        	
	        });
	        
	        panel.add(button);
	        
    		this.dialog.add(panel);
    		
    		this.solutions = new ArrayList<Solution>();
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
		}
    	
    	public void sortSolutions() {
    		Set<Solution> hs = new HashSet<Solution>();
    		hs.addAll(this.solutions);
    		
    		List<Solution> sorted = new ArrayList<Solution>();
    		sorted.addAll(hs);
    		Collections.sort(sorted);
    		
    		String text = "";
    		int min = Math.min(250, sorted.size());
    		for(int i = 0; i < min; i++)
    			text += String.format(i + " %s\n", sorted.get(i));
    		this.solutions = sorted.subList(0, min);

    		textOutput.setText(text);
    		textOutput.revalidate();
    		textOutput.setCaretPosition(0);
    	}

    	public void addSolution(Solution solution) {
    		this.solutions.add(solution);
    		updateNeed = true;
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
    
    public class ASCIIConvertAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = inputTextArea.getText();
			
			//inputTextArea.setText(cipherText);
		}
    }

    public class ShuffleTextAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = inputTextArea.getText();
    		char[] chars = binaryText.toCharArray();
    		ArrayUtil.shuffle(chars);
    		inputTextArea.setText(new String(chars));	
		}
    }
    
    public class ReverseTextAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = StringTransformer.reverseString(inputTextArea.getText());
    		inputTextArea.setText(binaryText);	
		}
    }
    
    public class WordSplitAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JTextArea textOutput;
    	private JButton copyText;
    	
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

    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(800, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
	        
	        this.textOutput = new JTextArea();
	        this.textOutput.setLineWrap(true);

	        JScrollPane scrollPane = new JScrollPane(this.textOutput);
	        
	        panel.add(scrollPane);
	        
	        
	        this.copyText = new JButton("Copy");
	    	this.copyText.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					StringSelection selection = new StringSelection(textOutput.getText());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
				}
	    		
	    	});
	    	panel.add(this.copyText, BorderLayout.CENTER);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		String split = WordSplit.splitText(inputTextArea.getText().replaceAll(" ", ""));
    		textOutput.setText(split);
    		textOutput.revalidate();
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
			removeDialog(dialog);
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
    		addDialog(this.dialog);
    		
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
    	private JComboBox<String> comboBoxOverlap;
    	
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
	        this.comboBoxOverlap = new JComboBox<String>(new String[] {"Overlap", "n-Apart"});
	        this.comboBoxOverlap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	        this.comboBoxOverlap.addItemListener(new ItemListener() {
				@Override
			    public void itemStateChanged(ItemEvent event) {
					if(event.getStateChange() == ItemEvent.SELECTED) {
						updateDialog();
			       }
			    }       
			});
	        panel.add(this.comboBoxOverlap);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
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
	      		
	      		boolean overlap = true;
	      		String labelOverlap = (String)this.comboBoxOverlap.getSelectedItem();
    			if(!labelOverlap.equals("Overlap"))
    				overlap = false;
	      		
	    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(text, minlength, maxlength, overlap);
				
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
    
    public class ADFGXIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public ADFGXIoCAction() {
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
    		this.dialog.setTitle("ADFGX IoC");
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
    		addDialog(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		this.chart.resetAll();
    		this.chart2.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			
    			Figure figure = new Figure(text.toCharArray(), settings.getLanguage());
    			
    			for(int length = 2; length <= 6; length++)
    				Permentations.permutate(figure, ArrayUtil.range(0, length));
    			
    			String s = "";
    			
    			s += "Like english: " + figure.smallest + "\n";
    			for(Integer[] i : figure.orders1) {
    				s += Arrays.toString(i) + "\n";
    			}
    			
    			s += "\nIOC: " + figure.closestIC + " off normal\n";
    			
    			for(Integer[] i : figure.orders2) {
    				s += Arrays.toString(i) + "\n";
    			}
    			
    			  output.println("----------- ADFGVX ----------- ");
    				output.print(s);
    			  //  output.println(" IoC Calculation: " + bestPeriod);
    			    output.println("");
    			
    			/**
    			int bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 20; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateEvenDiagrahpicIC(StringTransformer.getEveryNthBlock(text, 2, i, period));
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.chart2.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.chart2.setSelected(bestPeriod - 2);**/
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    public static class Figure implements PermentateArray {

		public char[] text;
		public ILanguage language;
		public double smallest = Double.MAX_VALUE;
		public List<Integer[]> orders1 = new ArrayList<Integer[]>();
		public double closestIC = Double.MAX_VALUE;
		public List<Integer[]> orders2 = new ArrayList<Integer[]>();
		
		
		public Figure(char[] text, ILanguage language) {
			this.text = text;
			this.language = language;
		}
		
		@Override
		public void onPermentate(int[] array) {
			System.out.println(""+ Arrays.toString(array));
			char[] s = ColumnarRow.decode(this.text, array);
			String str = new String(s);
			double n = calculate(str, this.language);
	    	double evenDiagraphicIC = StatCalculator.calculateEvenDiagrahpicIC(str);
	    	
	    	Integer[] arr = new Integer[array.length];
	    	for(int i = 0; i <array.length; i++)
	    		arr[i] = Integer.valueOf(array[i]);
	    	
	    	
	    	if(n <= smallest) {
	    		if(n != smallest)
	    			orders1.clear();
	    		orders1.add(arr);
	    		smallest = n;
	    	}
	    	double sqDiff = Math.pow(this.language.getNormalCoincidence() - evenDiagraphicIC, 2);
	    	if(sqDiff <= closestIC) {
	    		if(n != smallest)
	    			orders2.clear();
	    		orders2.add(arr);
	    		
	    		closestIC = sqDiff;
	    	}
		}
		
	}
	
	public static double calculate(String text, ILanguage language) {
		Map<String, Integer> letters = MapHelper.sortMapByValue(StringAnalyzer.getEmbeddedStrings(text, 2, 2, false), false);
		double total = 0.0D;
		
		List<Double> normalOrder = language.getFrequencyLargestFirst();
		
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
		
		return total;
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
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 0; period <= Math.min(40, text.length()); ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Step Calculation x2"));
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
    		addDialog(this.dialog);
    		
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
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestIC = Double.POSITIVE_INFINITY;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateNicodemusIC(text, 5, period) - settings.getLanguage().getNormalCoincidence(), 2) * 10000;
    		    	
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
    
    private class NihilistIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public NihilistIoCAction() {
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
    		this.dialog.setTitle("Nihilist Substitution IoC");
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kappa IoC Calculation"));
	        panel.add(this.chart);
	        
	        this.chart2 = new JBarChart(new ChartList());
	        this.chart2.setHasBarText(false);
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Average IoC Calculation"));
	        panel.add(this.chart2);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			this.chart2.resetAll();
			
    		String text = getInputTextOnlyDigits();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestIC = Double.POSITIVE_INFINITY;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateDiagraphicKappaIC(text, period * 2) - settings.getLanguage().getNormalCoincidence(), 2) * 10000;
    		    	
    		    	if(sqDiff < bestIC)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIC = Math.min(bestIC, sqDiff);
    		    }
    			
    		    this.chart.setSelected(bestPeriod - 2);
    		    
    		    bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateDiagrahpicIC(StringTransformer.getEveryNthBlock(text, 2, i, period));
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.chart2.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.chart2.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    public class PortaxIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public PortaxIoCAction() {
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
    		this.dialog.setTitle("Portax IoC");
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Portax IoC Calculation"));
	        panel.add(this.chart);

	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty() && text.length() % 2 == 0) {
				double bestIoC = 0.0D;
				int bestPeriod = -1;
				
				for(int period = 2; period <= 40; period++) {
			        int step = 2 * period;
			        
			 
			        String string = "";
			        for(int j = 0; j < text.length(); j += step) {
			        	for(int k = 0; k < period; k++) {
			                if(j + k + period >= text.length()) 
			                	break;
			        		int c1 = text.charAt(j + k) - 'A';
			                int c2 = text.charAt(j + k + period) - 'A';
			                int[] result = decodePair(k, c1, c2);
			                if (result[0] == 1) {
			                	char c3 = (char)(result[1] + 'A');
			                	char c4 = (char)(result[2] + 'A');
			                	string += c3 + "" + c4;
			                }
			        	}
			        }
			        double sqDiff = StatCalculator.calculateIC(string);
			        
			        if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
			
			    }
				
				this.chart.setSelected(bestPeriod - 2);
			}
    		
    		this.chart.repaint();
    	}
    	
    	private int[] decodePair(int k, int c1, int c2) {
    		int t_flag = 0;
    		int b_flag = 0;
            if (c1 < 13) 
            	t_flag = 0;
            else 
            	t_flag = 2;
            if (c2 % 2 == 1) 
            	b_flag = 1;
            else 
            	b_flag = 0;
            int[] rvalue = new int[] {0,0,0};
            int sum = t_flag + b_flag;
            if(sum == 2)
    			if(c1 - 13 != (c2 >> 1))
    				rvalue = new int[] {1, (c2 >> 1) + 13, (c1 - 13) << 1};
            if(sum == 3)
    			if(c1 - 13 != (c2 >> 1))
    				rvalue = new int[] {1, (c2 >> 1) + 13, ((c1 - 13) << 1 ) + 1};
            return rvalue;
    	} 
    }
    
    public class ProgressiveKeyIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private ThreadCancelable threadCancel;
    	
    	public ProgressiveKeyIoCAction() {
    		threadCancel = new ThreadCancelable(new Runnable() {

							@Override
							public void run() {
								updateDialog();
							}
						});
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						threadCancel.restart();
						
					}	
				}
    		});
    		
    		this.dialog = new JDialog();
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setTitle("Progressive Key IoC");
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Progressive Key IoC Calculation"));
	        panel.add(this.chart);

	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		threadCancel.restart();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty()) {
				char[] arrayText = text.toCharArray();
				int bestPeriod = -1;
				int bestEverProgressivePeriod = -1;
				int bestEverProgressiveIndex = -1;
				double bestEverIoC = Double.MAX_VALUE;
				
				for(int period = 2; period <= 40; period++) {
					int bestProgressivePeriod = -1;
					int bestProgressiveIndex = -1;
					double bestIoC = Double.MAX_VALUE;
					List<ProgressiveKeySearch> list = new ArrayList<ProgressiveKeySearch>();
					
					for(int progressivePeriod = 1; progressivePeriod <= 25; progressivePeriod++) {
						for(int progressiveIndex = 1; progressiveIndex <= 25; progressiveIndex++) {
							char[] decoded = ProgressiveKey.decodeBase(arrayText, progressivePeriod, progressiveIndex);
							
							
							double total = 0.0D;
		    		    	for(int i = 0; i < period; i++)
		    		    		total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(decoded, i, period));
		    		    	total /= period;
		    		    	
		    		    	double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
		    		    	
		    		    	if(sqDiff < bestIoC) {
		    		    		bestProgressivePeriod = progressivePeriod;
		    		    		bestProgressiveIndex = progressiveIndex;
		    		    	}
		  
		    		    	bestIoC = Math.min(bestIoC, sqDiff);
		    		    	list.add(new ProgressiveKeySearch(progressivePeriod, progressiveIndex, sqDiff));
						}
					}
					
					Collections.sort(list);
		
					if(bestIoC < bestEverIoC) {
						bestEverIoC = bestIoC;
						bestPeriod = period;
						bestEverProgressivePeriod = bestProgressivePeriod; //WILL GIVE A FACTOR OF THE ACCUTAL ANSWER
    		    		bestEverProgressiveIndex = bestProgressivePeriod; //WILL GIVE A FACTOR OF THE ACCUTAL ANSWER
					}
				
					this.chart.values.add(new ChartData(String.format("Period: %d, PP %d, PI %d", period, bestProgressivePeriod, bestProgressiveIndex), bestIoC));
					this.chart.repaint();
				}
				
				//output.println("Period: " + bestPeriod);
				///output.println("  Prog Period: " + bestProgressivePeriod);
				//output.println("  Prog Index" + bestprogressiveIndex);
			}
    		
    		this.chart.repaint();
    	}
    }
    
    private class ProgressiveKeySearch implements Comparable<ProgressiveKeySearch> {
    	
    	public final int progPeriod, progIndex;
    	public final double score;
    	
    	public ProgressiveKeySearch(int progPeriod, int progIndex, double score) {
    		this.progPeriod = progPeriod;
    		this.progIndex = progIndex;
    		this.score = score;
    	}
    	
    	@Override
    	public int compareTo(ProgressiveKeySearch o) {
    		double dF = o.score - this.score;
    		return dF == 0 ? 0 : dF > 0 ? 1 : -1;
    	}
    	
    	@Override
    	public String toString() {
    		return String.format("Score: %f, ProgPeriod: %d, ProgIndex: %d", this.score, this.progPeriod, this.progIndex);
    	}
    }
    
    private class SeriatedPlayfairIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	
    	public SeriatedPlayfairIoCAction() {
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
    		this.dialog.setTitle("Seriated Playfair IoC");
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Double Letter Calculation"));
	        panel.add(this.chart);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
	
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty() && text.length() % 2 == 0)
				for(int period = 2; period <= 40; period++)
					this.chart.values.add(new ChartData("Period: " + period, StatCalculator.calculateSeriatedPlayfair(text, period) ? 1 : 0));
    		
    		this.chart.repaint();
    	}
    }
    
    public class SlidefairIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public SlidefairIoCAction() {
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
    		this.dialog.setTitle("Slidefair IoC");
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Step Calculation x3"));
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
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			this.chart2.resetAll();
	
			String text = getInputText();
			if(!text.isEmpty()) {
				int bestPeriod = -1;
	    		double bestIoC = Double.MAX_VALUE;
	    		    
	    		for(int period = 2; period <= 40; ++period) {
	    		    double total = 0.0D;
	    		    for(int i = 0; i < period * 2; i++)
	    		    	total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period * 2));
	    		    total /= period * 2;
	    		    	
	    		    double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
	    		    	
	    		    if(sqDiff < bestIoC)
	    		    	bestPeriod = period;
	    		    this.chart2.values.add(new ChartData("Period: " + period, sqDiff));
	    		    	
	    		    bestIoC = Math.min(bestIoC, sqDiff);
	    		}
	    		    
	    		this.chart2.setSelected(bestPeriod - 2);
			}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    public class SwagmanTestAction extends NCCDialog implements ActionListener {
    	
    	private JBarChart chart;
    	
    	public SwagmanTestAction() {
    		super("Swagman Test", "image/lock_break.png");
    		inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {

				@Override
				public void onUpdate(DocumentEvent event) {
					if(dialog.isVisible()) {
						updateDialog();
					}	
				}
    		});
    		

    		this.dialog.setMinimumSize(new Dimension(800, 400));

    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	          
	        this.chart = new JBarChart(new ChartList());
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Swagman Test"));
	        panel.add(this.chart);

	         
    		this.dialog.add(panel);
    
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty()) {
				int bestScore = Integer.MIN_VALUE;
				int bestPeriod = -1;
				for(int period = 2; period <= 9; period++) {
		            if(text.length() % period != 0) {
		            	this.chart.values.add(new ChartData("" + period, 0));
		            	continue;
		            }
		            
		            if(3 * period * period > text.length()) {
		                this.chart.values.add(new ChartData("" + period, 0));
		            	continue;
		            }
		            
		            int result = StatCalculator.swagTest(text, period);
		            if(result > bestScore) {
		            	bestScore = result;
		            	bestPeriod = period;
		            }
		            this.chart.values.add(new ChartData("" + period, result));
		        }
				if(bestPeriod != -1)
					this.chart.setSelected(bestPeriod - 2);
			}
    		
    		this.chart.repaint();
    	}
    }
    
    public class TrifidIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
    	public TrifidIoCAction() {
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
    		this.dialog.setTitle("Trifid IoC");
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
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Step Calculation x3"));
	        panel.add(this.chart);
	         
	        this.chart2 = new JBarChart(new ChartList());
	
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Trigraphic Phi Test"));
	        panel.add(this.chart2);
	        
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			this.chart2.resetAll();
	
			String text = getInputText();
			if(!text.isEmpty()) {

				
				Map<Integer, Double> values = new HashMap<Integer, Double>();
				double maxValue = Double.MIN_VALUE;
				int maxStep = -1;
				
				double secondValue = Double.MIN_VALUE;
				
				for(int step = 1; step <= Math.min(40, text.length()); step++) {
					HashMap<String, Integer> counts = new HashMap<String, Integer>();
					for(int i = 0; i < text.length() - step * 2; i++) {
						String s = text.charAt(i) + "" + text.charAt(i + step) + "" + text.charAt(i + step * 2);
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
						periodGuess = maxStep * 3;
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
						
						periodGuess = Math.min(bestStep, maxStep) * 3 + Math.abs(bestStep - maxStep);
					}
				}
				
				int bestPeriod = -1;
				double bestIC = Double.MIN_VALUE;
			    for(int period = 0; period <= 40; period++) {
			    	if(period == 1) continue;
			    	if(period == 2) continue;
			    	
			        double score = StatCalculator.calculateTrifidDiagraphicIC(text, period);
			        this.chart2.values.add(new ChartData("" + period, score));
			        if(bestIC < score)
			        	bestPeriod = period;
			        
			        bestIC = Math.max(bestIC, score);
			    }
			    
			    this.chart.setSelected(maxStep - 1);
				this.chart2.setSelected(bestPeriod > 0 ? bestPeriod - 2 : 0);
			}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    private class VigenereIoCAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JBarChart chart;
    	private JBarChart chart2;
    	
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
	          
	        this.chart = new JBarChart();
	        this.chart.setHasBarText(false);
	        this.chart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Kappa IoC Calculation"));
	        panel.add(this.chart);
	        
	        this.chart2 = new JBarChart();
	        this.chart2.setHasBarText(false);
	        this.chart2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Average IoC Calculation"));
	        panel.add(this.chart2);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
			this.chart.resetAll();
			this.chart2.resetAll();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.chart.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    		    
    		    this.chart.setSelected(bestPeriod - 2);
    		    
    		    bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period));
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.chart2.values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.chart2.setSelected(bestPeriod - 2);
    		}
    		
    		this.chart.repaint();
    		this.chart2.repaint();
    	}
    }
    
    private class SolitaireAction implements ActionListener, LoadElement {
    	
    	private JDialog dialog;
    	private JTextField passKeyStartingOrder;
    	private JButton copyOrder;
    	private int[] cardOrder;
    	private List<String> order;
    	
    	public SolitaireAction() {
    		this.dialog = new JDialog();
    		this.dialog.setTitle("Solitaire Cipher");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.addWindowListener(new JDialogCloseEvent(this.dialog));
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/playing_card.png")));
    		this.dialog.setMinimumSize(new Dimension(900, 600));
    		this.dialog.setFocusableWindowState(false);
    		
    		this.order = new ArrayList<String>();
    		
    		JPanel panel = new JPanel();
    		panel.setLayout(new FlowLayout(FlowLayout.LEFT));
	
			
			
			String[] suits = new String[] {"clubs", "diamonds", "hearts", "spades"};
			String[] cards = new String[] {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};
			final JPanel[] suitPanels = new JPanel[suits.length];
			for(int s = 0; s < suits.length; s++) {
				suitPanels[s] = new JPanel();
				suitPanels[s].setLayout(new BoxLayout(suitPanels[s], BoxLayout.X_AXIS));
				panel.add(suitPanels[s]);
			}
			
			final List<JButton> buttons = new ArrayList<JButton>();
			
			for(int s = 0; s < suits.length; s++) {
				for(int c = 0; c < cards.length; c++) {
					final int s2 = s;
					final int id = s * cards.length + c;
					JButton button = new JButton();
					button.setFocusPainted(false);
					final ImageIcon imageIcon = ImageUtil.createScaledImageIcon("/image/cards/" + cards[c] + "_of_" + suits[s] + ".png", 1D / 8D);
					ButtonUtil.setButtonSizeToIconSize(button, imageIcon);
					button.setIcon(imageIcon);
					button.setDisabledIcon(imageIcon);
					button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JButton button = (JButton)e.getSource();
							if(button.getIcon() == null) {
								button.setIcon(imageIcon);
								order.remove("" + id);
							}
							else {
								button.setIcon(null);
								order.add("" + id);
							}
							passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
							suitPanels[s2].repaint();
						}
					});
					
					buttons.add(button);
					suitPanels[s].add(button);
				}
			}
			
			JButton button = new JButton();
			button.setFocusPainted(false);
			final ImageIcon imageIcon = ImageUtil.createScaledImageIcon("/image/cards/black_joker.png", 1D / 8D);
			ButtonUtil.setButtonSizeToIconSize(button, imageIcon);
			button.setIcon(imageIcon);
			button.setDisabledIcon(imageIcon);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton)e.getSource();
					if(button.getIcon() == null) {
						button.setIcon(imageIcon);
						order.remove("52");
					}
					else {
						button.setIcon(null);
						order.add("52");
					}
					passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
					suitPanels[0].repaint();
				}
			});
			buttons.add(button);
			suitPanels[0].add(button);
			
			button = new JButton();
			button.setFocusPainted(false);
			final ImageIcon imageIcon2 = ImageUtil.createScaledImageIcon("/image/cards/red_joker.png", 1D / 8D);
			ButtonUtil.setButtonSizeToIconSize(button, imageIcon2);
			button.setIcon(imageIcon2);
			button.setDisabledIcon(imageIcon2);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					JButton button = (JButton)e.getSource();
					if(button.getIcon() == null) {
						button.setIcon(imageIcon2);
						order.remove("53");
					}
					else {
						button.setIcon(null);
						order.add("53");
					}
					passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
					suitPanels[0].repaint();
				}
			});
			buttons.add(button);
			suitPanels[1].add(button);
			
			button = new JButton("Blank");
			button.setFocusPainted(false);
			button.setBackground(Color.black);
			ButtonUtil.setButtonSizeToIconSize(button, imageIcon);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					order.add("*");
	
					passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
					suitPanels[0].repaint();
				}
			});
			suitPanels[2].add(button);
			
			button = new JButton("Start");
			button.setFocusPainted(false);
			button.setBackground(Color.black);
			ButtonUtil.setButtonSizeToIconSize(button, imageIcon);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					order.clear();
					for(JButton but : buttons) {
						but.setIcon(but.getDisabledIcon());
					}
					
					passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
					suitPanels[0].repaint();
				}
			});
			suitPanels[3].add(button);
		
	        this.cardOrder = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};
	    	
	    	passKeyStartingOrder = new JTextField("");
	    	passKeyStartingOrder.setMinimumSize(new Dimension(882, 0));
	    	passKeyStartingOrder.setPreferredSize(new Dimension(882, 20));
	    	panel.add(this.passKeyStartingOrder);
			
			
	    	this.copyOrder = new JButton("Copy");
	    	this.copyOrder.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					StringSelection selection = new StringSelection(passKeyStartingOrder.getText());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
				}
	    		
	    	});
	    	panel.add(this.copyOrder);
	    	
    		this.dialog.add(panel);
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
    		//this.cardOrder = Solitaire.nextCardOrder(this.cardOrder);
		}

		@Override
		public void write(HashMap<String, Object> map) {
			
		}

		@Override
		public void read(HashMap<String, Object> map) {
			
		}
    }
    
    private class SoiltaireStartAttack implements SolitaireAttack {

    	public Solution bestSolution;
    	public int[] intText;
    	
    	private SoiltaireStartAttack(String cipherText) {
    		this.bestSolution = new Solution();
    		this.intText = new int[cipherText.length()];
    		for(int i = 0; i < cipherText.length(); i++)
    			this.intText[i] = cipherText.charAt(i) - 'A';
    	}
    	
		@Override
		public void tryKeyStream(int[] keyStream, int[] lastOrder) {
			char[] chars = Solitaire.decodeWithKeyStream(this.intText, keyStream);
			Solution last = new Solution(chars, settings.getLanguage());
			
			if(this.bestSolution.score < last.score) {
				this.bestSolution = last;
			
				int[] order = lastOrder;
				//for(int i = 0; i < times; i++) {
				//	order = Solitaire.previousCardOrder(order);
				//}
				
				this.bestSolution.setKeyString(ListUtil.toCardString2(order, 0));
				output.println("%s", this.bestSolution);
			}
			
		}

		@Override
		public int getSubBranches() {
			return this.intText.length;
		}
    }
    
    private class IdentifyAction implements ActionListener {
    	
    	private JDialog dialog;
    	private JPanel cipherInfoPanel;
    	private JPanel cipherScorePanel;
    	private JScrollPane scrollPane;
    	private JTree tree;
    	private DefaultMutableTreeNode top;
    	private JScrollPane treeView;
    	
    	public IdentifyAction() {
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
    		this.dialog.setTitle("Identify Cipher");
    		this.dialog.setAlwaysOnTop(true);
    		this.dialog.setModal(false);
    		this.dialog.setResizable(false);
    		this.dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/lock_break.png")));
    		this.dialog.setFocusableWindowState(false);
    		this.dialog.setMinimumSize(new Dimension(500, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
	          
	        JPanel basePanel = new JPanel();
	        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
	        scrollPane = new JScrollPane(basePanel);
		    scrollPane.getVerticalScrollBar().setUnitIncrement(12);
	        
	        
	        this.cipherInfoPanel = new JPanel();
		    this.cipherInfoPanel.setLayout(new BoxLayout(this.cipherInfoPanel, BoxLayout.Y_AXIS));
		    basePanel.add(this.cipherInfoPanel);
		    
		    this.cipherScorePanel = new JPanel();
		    this.cipherScorePanel.setLayout(new BoxLayout(this.cipherScorePanel, BoxLayout.Y_AXIS));
		    basePanel.add(this.cipherScorePanel);
		    
			this.top = new DefaultMutableTreeNode("The Java Series");
			//createNodes(top);
			this.tree = new JTree(this.top);
			this.tree.putClientProperty("JTree.lineStyle", "None");
			this.tree.setFont(this.tree.getFont().deriveFont(20F));
			this.tree.setRowHeight((int) (this.tree.getFont().getSize() * 1.2F));
			
		 //treeView = new JScrollPane(this.tree);
			//panel.add(treeView);
		    panel.add(scrollPane);
	         
    		this.dialog.add(panel);
    		
    		dialogs.add(this.dialog);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		this.cipherInfoPanel.removeAll();
    		this.cipherScorePanel.removeAll();
    		this.top.removeAllChildren();
    		
    		String text = getInputText();
    		
    		if(!text.isEmpty()) {
    			
    			
    			Object[] statValues = new Object[38];
			    
			    //Numerical values
			    statValues[0] = StatCalculator.calculateIC(text) * 1000.0D;
				statValues[1] = StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D;
				statValues[2] = StatCalculator.calculateMaxKappaIC(text, 1, 15);
				statValues[3] = StatCalculator.calculateDiagrahpicIC(text) * 10000.0D;
				statValues[4] = StatCalculator.calculateEvenDiagrahpicIC(text) * 10000;
				statValues[5] = StatCalculator.calculateLR(text);
				statValues[6] = StatCalculator.calculateROD(text);
				statValues[7] = PolyalphabeticIdentifier.calculateLDI(text);
				statValues[8] = StatCalculator.calculateSDD(text);
			    statValues[22] = PolyalphabeticIdentifier.calculateAutokeyPortaLDI(text);
				statValues[23] = PolyalphabeticIdentifier.calculateBeaufortLDI(text);
				statValues[24] = PolyalphabeticIdentifier.calculatePortaLDI(text);
				statValues[25] = PolyalphabeticIdentifier.calculateSLDI(text);
				statValues[26] = PolyalphabeticIdentifier.calculateVigenereLDI(text);
				statValues[27] = StatCalculator.calculateNormalOrder(text, settings.getLanguage());
				statValues[28] = PolyalphabeticIdentifier.calculateRDI(text);
				statValues[29] = PolyalphabeticIdentifier.calculatePTX(text);
				statValues[30] = StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
				statValues[31] = StatCalculator.calculatePHIC(text);
			    statValues[33] = StatCalculator.calculateMaxBifidDiagraphicIC(text, 3, 15);
				statValues[34] = StatCalculator.calculateCDD(text);
				statValues[35] = StatCalculator.calculateSSTD(text);
				statValues[36] = StatCalculator.calculateMPIC(text);
			    
			    
				statValues[9] = "CIPHER";
			    
				statValues[10] = StatCalculator.isLengthDivisible2(text);
				statValues[11] = StatCalculator.isLengthDivisible3(text);
				statValues[12] = StatCalculator.isLengthDivisible5(text);
				statValues[13] = StatCalculator.isLengthDivisible25(text);
				statValues[14] = StatCalculator.isLengthDivisible4_15(text);
				statValues[15] = StatCalculator.isLengthDivisible4_30(text);
				statValues[16] = StatCalculator.isLengthPerfectSquare(text);
				statValues[17] = StatCalculator.containsLetter(text);
				statValues[18] = StatCalculator.containsDigit(text);
				statValues[19] = StatCalculator.containsJ(text);
				statValues[20] = StatCalculator.containsHash(text);
				statValues[21] = StatCalculator.calculateDBL(text);
				
				
	
				statValues[32] = StatCalculator.calculateHAS0(text);
		
				statValues[37] = StatCalculator.calculateSeriatedPlayfair(text, 3, 10);
			    
				Map<String, Integer> answers = new HashMap<String, Integer>();
				
				for(Map map : TraverseTree.trees) {
					String answer = TraverseTree.traverse_tree(map, statValues);
					answers.put(answer, 1 + (answers.containsKey(answer) ? answers.get(answer) : 0));
				}
				answers = MapHelper.sortMapByValue(answers, false);
				
				System.out.println(answers);
				
    			
    		
				List<List<Object>> num_dev = StatisticHandler.orderCipherProbibitly(text);
				 
			    
			    Comparator<List<Object>> comparator = new Comparator<List<Object>>() {
			    	@Override
			        public int compare(List<Object> c1, List<Object> c2) {
			        	double diff = (double)c1.get(1) - (double)c2.get(1);
			        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
			        }
			    };

			    Collections.sort(num_dev, comparator);
		
			    
			    
			    JLabel titleLabel = new JLabel("Cipher");
			    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
			    this.cipherInfoPanel.add(titleLabel);
			    
			    JLabel titleScoreLabel = new JLabel("Likelyhood");
			    titleScoreLabel.setFont(titleScoreLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
			    this.cipherScorePanel.add(titleScoreLabel);
			    
			    for(int i = 0; i < num_dev.size(); i++) {
			    	JLabel cipherInfoLabel = new JLabel(num_dev.get(i).get(0) + ":            ");
			    	cipherInfoLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	this.cipherInfoPanel.add(cipherInfoLabel);
			    	
			    	
			    	JLabel cipherScoreLabel = new JLabel(String.format("%.2f", (double)num_dev.get(i).get(1), 2));
			    	cipherScoreLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	this.cipherScorePanel.add(cipherScoreLabel);
			    }
    			
    		}
    		this.cipherInfoPanel.revalidate();
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
     		addDialog(this.dialog);
     		
    		this.updateDialog();
		}
    	
    	public void updateDialog() {
    		String text = getInputTextOnlyAlpha();
    		int length = text.length();
    		
    		String outputText = "Length: " + length;
			outputText += "\nEstimated Fitness for length: " + Rounder.round(TextFitness.getEstimatedFitness(text, settings.getLanguage()), 4);
    		
    		if(!text.isEmpty()) {

    			outputText += "\n IC: " + StatCalculator.calculateIC(text) * 1000.0D;
    			outputText += "\n MIC: " + StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D;
    		    outputText += "\n MKA: " + StatCalculator.calculateMaxKappaIC(text, 1, 15);
    		    outputText += "\n DIC: " + StatCalculator.calculateDiagrahpicIC(text) * 10000.0D;
    		    outputText += "\n EDI: " + StatCalculator.calculateEvenDiagrahpicIC(text) * 10000;
    		    outputText += "\n LR: " + StatCalculator.calculateLR(text);
    		    outputText += "\n ROD: " + StatCalculator.calculateROD(text);
    		    outputText += "\n LDI: " + PolyalphabeticIdentifier.calculateLDI(text);
    		    outputText += "\n SDD: " + StatCalculator.calculateSDD(text);

    		    outputText += "\n A_LDI: " + PolyalphabeticIdentifier.calculateAutokeyPortaLDI(text);
    		    outputText += "\n B_LDI: " + PolyalphabeticIdentifier.calculateBeaufortLDI(text);
    		    outputText += "\n P_LDI: " + PolyalphabeticIdentifier.calculatePortaLDI(text);
    		    outputText += "\n S_LDI: " + PolyalphabeticIdentifier.calculateSLDI(text);
    		    outputText += "\n V_LDI: " + PolyalphabeticIdentifier.calculateVigenereLDI(text);
    		    
    		    outputText += "\n NOMOR: " + StatCalculator.calculateNormalOrder(text, settings.getLanguage());
    		    outputText += "\n RDI: " + PolyalphabeticIdentifier.calculateRDI(text);
    		    outputText += "\n PTX: " + PolyalphabeticIdentifier.calculatePTX(text);
    		    outputText += "\n NIC: " +  StatCalculator.calculateMaxNicodemusIC(text, 3, 15);
    		    outputText += "\n PHIC: " + StatCalculator.calculatePHIC(text);
    		    outputText += "\n BDI: " +  StatCalculator.calculateMaxBifidDiagraphicIC(text, 3, 15);
    		    outputText += "\n CDD: " +  StatCalculator.calculateCDD(text);
    		    outputText += "\n SSTD: " +  StatCalculator.calculateSSTD(text);
    		    outputText += "\n MPIC: " + StatCalculator.calculateMPIC(text);
    		    outputText += "\n SERP: " + StatCalculator.calculateSeriatedPlayfair(text, 3, 10);
    		    
    		    
    		    outputText += "\n DIV_2: " + StatCalculator.isLengthDivisible2(text);
    		    outputText += "\n DIV_3: " + StatCalculator.isLengthDivisible3(text);
    		    outputText += "\n DIV_5: " + StatCalculator.isLengthDivisible5(text);
    		    outputText += "\n DIV_25: " + StatCalculator.isLengthDivisible25(text);
    		    outputText += "\n DIV_4_15: " + StatCalculator.isLengthDivisible4_15(text);
    		    outputText += "\n DIV_4_30: " + StatCalculator.isLengthDivisible4_30(text);
    		    List<Integer> factors =  MathUtil.getFactors(length);
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
    
    private class EncodeAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String text = getInputTextOnlyAlpha();
    		double esitmated = TextFitness.getEstimatedFitness(text, settings.getLanguage());
    		double acutal = TextFitness.scoreFitnessQuadgrams(text, settings.getLanguage());
    		if(Math.abs(esitmated - acutal) > esitmated * -0.30) {
    			Object[] options = {"Yes", "Cancel"};
    			
    			Image img = ImageUtil.createImageIcon("/image/error.png", "Error").getImage() ;  
    			Image newimg = img.getScaledInstance( 40, 40,  java.awt.Image.SCALE_SMOOTH ) ;  
    			ImageIcon icon = new ImageIcon(newimg);
    			
    			int n = JOptionPane.showOptionDialog(UINew.this,
    					"This text doesn't seem to be English\nDo you wish to continue?",
    					"Random Encryption",
    				    JOptionPane.YES_NO_CANCEL_OPTION,
    				    JOptionPane.QUESTION_MESSAGE,
    				    icon,
    				    options,
    				    options[1]);
    			
    			if(n != JOptionPane.YES_OPTION)
    				return;
    		}
    		
    		if(!text.isEmpty()) {
    			IRandEncrypter randomEncrypt = RandomUtil.pickRandomElement(RandomEncrypter.ciphers);
    			String cipherText = randomEncrypt.randomlyEncrypt(text);
    			output.println(randomEncrypt.getClass().getSimpleName());
    			output.println(StringTransformer.repeat("\n", 25));
    			StringSelection selection = new StringSelection(cipherText);
	    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    			
    			output.println(cipherText);
    		}
		}
    }
    
    
    private class LanguageChangeAction implements ActionListener {
    	
    	public ILanguage language;
    	
    	public LanguageChangeAction(ILanguage language) {
    		this.language = language;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			settings.setLanguage(this.language);
			menuItemCurrentLanguage.setText("Current: " + this.language.getName());
		}
    }
    
    private class KeywordCreationAction implements ActionListener {
    	
    	public int id;
    	
    	public KeywordCreationAction(int id) {
    		this.id = id;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
			settings.setKeywordCreationId(this.id);
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
			settings.getSA().set(this.id, Double.valueOf(this.textComponent.getText()));
			
		}

		@Override
		public void keyTyped(KeyEvent e) {
			
		}
    }
    
    public class PresetAction implements ActionListener {
    	
    	public JTextField tempSetting;
    	public JTextField tempStepSetting;
    	public JTextField countSetting;
    	public double tempStart;
    	public double tempStep;
    	public int count;
    	
    	public PresetAction(JTextField tempSetting, JTextField tempStepSetting, JTextField countSetting, double tempStart, double tempStep, int count) {
    		this.tempSetting = tempSetting;
    		this.tempStepSetting = tempStepSetting;
    		this.countSetting = countSetting;
    		this.tempStart = tempStart;
    		this.tempStep = tempStep;
    		this.count = count;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent e) {
			settings.getSA().set(0, this.tempStart);
			settings.getSA().set(1, this.tempStep);
			settings.getSA().set(2, (double)this.count);
			tempSetting.setText(ValueFormat.getNumber(this.tempStart));

			tempStepSetting.setText(ValueFormat.getNumber(this.tempStep));

			countSetting.setText(ValueFormat.getNumber(this.count));	
		}
    }
    
    public class UpdateProgressAction implements ActionListener {

    	public JCheckBoxMenuItem checkBox;
    	
    	public UpdateProgressAction(JCheckBoxMenuItem checkBox) {
    		this.checkBox = checkBox;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent event) {
			if(checkBox.isSelected()) {
				settings.updateProgressBars = true;
				progressBar.setString("0.0%");
			}
			else {
				progressBar.setValue(0);
				progressBar.setString("Inactive");
				settings.updateProgressBars = false;
			}

			keyPanel.setIterationUnsed();
		}
    }
    
    public class CribInputAction implements ActionListener {

    	public void actionPerformed(ActionEvent event) {
    		JDialog dialog = new JDialog(UINew.this);
    		
    		dialog.setTitle("Crib Input");
    		
    		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/cog.png")));
    		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		dialog.setResizable(false);
    		dialog.setMinimumSize(new Dimension(400, 200));
    		dialog.setModal(true);
   
    		ActionListener escListener = new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	        	dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
    	        }
    	    };

    	    dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    		
    		JPanel panel = new JPanel();
    		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 2, 2, 2), BorderFactory.createEtchedBorder()));
    	    dialog.add(panel);
    		
    	    panel.add(new JTextArea());
    		dialog.setLocationRelativeTo(UINew.this);
    		
	        
    		dialog.pack();
			dialog.setVisible(true);
    	}
    	
    }
    
    public class CipherAttackChangeAction implements ActionListener {
    	
    	public JMenuItem menuItem;
    	
    	public CipherAttackChangeAction(JMenuItem menuItem) {
    		this.menuItem = menuItem;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		menuItemCurrentAttack.setText("Target: " + this.menuItem.getText());
    		
    		int index = 0;
    		for(String name : AttackRegistry.getNames()) {
    			if(this.menuItem.getText().equals(name)) {
    				cipherSelect.setSelectedIndex(index);
    				break;
    			}
    			index++;
    		}
		}
    }
    
    public String getInputText() {
    	return this.inputTextArea.getText();
    }
    
    public String getInputTextOnlyAlpha() {
    	return this.inputTextArea.getText().replaceAll("[^a-zA-Z]+", "");
    }
    
    public String getInputTextOnlyDigits() {
    	return this.inputTextArea.getText().replaceAll("[^0-9]+", "");
    }
    
    //public IDecrypt getDecryptManager() {
    //	return DecryptionManager.ciphers.get(cipherSelect.getSelectedIndex());
    //}
    
    public CipherAttack getCipherAttack() {
    	return AttackRegistry.ciphers.get(cipherSelect.getSelectedIndex());
    }
     
    //IApplication methods
	@Override
	public Settings getSettings() {
		return this.settings;
	}
	
	@Override
	public ILanguage getLanguage() {
		return this.settings.getLanguage();
	}

	@Override
	public Output out() {
		return this.output;
	}

	@Override
	public KeyPanel getKeyPanel() {
		return this.keyPanel;
	}

	@Override
	public ProgressValue getProgress() {
		return this.progressValue;
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
    private ProgressValue progressValue;
    private JToolBar toolBar;
    private JButton toolBarSettings;
    private JButton toolBarStart;
    private JButton toolBarStop;
    private JMenuBar menuBar;
    private JMenu menuItemFile;
	private JMenuItem menuItemFullScreen;
	private JMenu menuScreenShot;
	private JMenuItem menuItemExit;
    private JMenu menuItemEdit;
    private JMenuItem menuItemPaste;
    private JMenuItem menuItemCopySolution;
    private JMenuItem menuItemShowTopSolutions;
    private JMenuItem menuItemBinary;
    private JMenuItem menuItemASCII;
    private JMenuItem menuItemShuffle;
    private JMenuItem menuItemReverseText;
    private JMenu menuItemTools; 
    private JMenuItem menuItemLetterFrequency;
    private JMenuItem menuItemNGram;
    private JMenu menuItemIoC;
    private JMenuItem menuItemIoCADFGX;
    private JMenuItem menuItemIoCNormal;
    private JMenuItem menuItemIoCBifid;
    private JMenuItem menuItemIoCNicodemus;
    private JMenuItem menuItemIoCNihilist;
    private JMenuItem menuItemIoCPortax;
    private JMenuItem menuItemIoCProgKey;
    private JMenuItem menuItemIoCSeriatedPlayfair;
    private JMenuItem menuItemIoCSlidefair;
    private JMenuItem menuItemIoCSwagman;
    private JMenuItem menuItemIoCTrifid;
    private JMenuItem menuItemIoCVigenere;
    private JMenuItem menuItemIdentify;
    private JMenuItem menuItemWordSplit;
    private JMenuItem menuItemInfo;
    private JMenuItem menuItemSolitaire;
    private JMenu menuItemEncrypter;
    private JMenuItem menuItemEncode;
    private JMenu menuItemEncodeChose;
    private JMenu menuItemSettings;
    private JMenu menuItemLanguage;
    private JMenuItem menuItemCurrentLanguage;
    private JMenu menuItemKeyword;
    private JMenuItem menuItemKeywordNormal;
    private JMenuItem menuItemKeywordHalf;
    private JMenuItem menuItemKeywordReverse;
    private JMenu menuItemSimulatedAnnealing;
    private JMenu menuItemSAPreset;
    private JCheckBoxMenuItem menuItemUpdateProgress;
    private JMenu menuCipherAttack;
    private JMenuItem menuCribInput;
    private JMenuItem menuItemCurrentAttack;
    
}
