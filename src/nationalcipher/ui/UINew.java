package nationalcipher.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import java.awt.event.MouseAdapter;
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
import javax.swing.JCheckBox;
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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

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
import javalibrary.list.DynamicResultList;
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
import javalibrary.swing.chart.JBarChart;
import javalibrary.thread.ThreadCancelable;
import javalibrary.thread.Threads;
import javalibrary.util.ArrayUtil;
import javalibrary.util.MapHelper;
import javalibrary.util.RandomUtil;
import nationalcipher.LoadElement;
import nationalcipher.Settings;
import nationalcipher.cipher.base.IRandEncrypter;
import nationalcipher.cipher.base.RandomEncrypter;
import nationalcipher.cipher.base.transposition.ColumnarTransposition;
import nationalcipher.cipher.decrypt.AttackRegistry;
import nationalcipher.cipher.decrypt.CipherAttack;
import nationalcipher.cipher.decrypt.methods.DecryptionMethod;
import nationalcipher.cipher.decrypt.methods.Solution;
import nationalcipher.cipher.identify.PolyalphabeticIdentifier;
import nationalcipher.cipher.stats.IdentifyOutput;
import nationalcipher.cipher.stats.StatCalculator;
import nationalcipher.cipher.stats.StatisticHandler;
import nationalcipher.cipher.stats.TextStatistic;

/**
 *
 * @author Alex
 */
public class UINew extends JFrame implements IApplication {

	public static byte[] BEST_SOULTION;
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
		StatisticHandler.registerStatistics();
    	this.settings.readFromFile();
    	
        this.initComponents();
        this.finishComponents();
        this.loadDataFiles();
    }
    
    public void addDialog(JDialog dialog) {
    	addDialog(dialog, true);
    }
    
    public void addDialog(JDialog dialog, boolean picture) {
    	if(!this.lastStates.contains(dialog))
    		this.lastStates.add(dialog);
    	if(picture)
    		this.changeDialog(dialog);
    }
    
    public void removeDialog(JDialog dialog) {
    	this.lastStates.remove(dialog);
    	this.changeDialog(dialog);
    }
    
    public void changeDialog(JDialog dialog) {
    	this.menuScreenShot.removeAll();
    	for(JDialog listDialog : this.lastStates) {
    		JMenuItem jmi = new JMenuItem(listDialog.getTitle());
    		if(listDialog.getIconImages() != null && !listDialog.getIconImages().isEmpty())
    			jmi.setIcon(new ImageIcon(listDialog.getIconImages().get(0)));
    		this.menuScreenShot.add(jmi);
    		jmi.addActionListener(new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent event) {
    	        	listDialog.setBackground(Color.red);
    	        	BufferedImage image = new BufferedImage(listDialog.getContentPane().getWidth(), listDialog.getContentPane().getHeight(), BufferedImage.TYPE_INT_RGB);
    	        	listDialog.getContentPane().paint(image.getGraphics());
    	        	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd_hh.mm.ss");
    	            String dateTime = sdf.format(Calendar.getInstance().getTime());
    	          	new PictureDialog(dateTime, new ImageIcon(image));
    	            try {
    	            	ImageIO.write(image, "png", new File(OSIdentifier.getMyDataFolder("nationalcipher/screenshots"), "screenshot" + dateTime + ".png"));
    	            }
    	          	catch(Exception e) {
    	          		e.printStackTrace();
    	          	}
    	        }
    		});
    	}
    	this.menuScreenShot.setEnabled(!this.lastStates.isEmpty());
    }
    
    public class PictureDialog extends NCCDialog {

		public PictureDialog(String title, ImageIcon imageIcon) {
			super(title, "image/picture_save.png");
			
			this.dialog.add(new JLabel(imageIcon));
			this.dialog.setMinimumSize(new Dimension(imageIcon.getIconWidth() + 20, imageIcon.getIconHeight() + this.dialog.getHeight() + 30));
			this.dialog.setVisible(true);
			UINew.this.addDialog(this.dialog, false);
		}
    }

    public void loadDataFiles() {
    	final Map<Component, Boolean> stateMap = SwingHelper.disableAllChildComponents((JComponent)getContentPane(), this.menuBar);
    	
    	this.progressBar.setMaximum(Languages.languages.size() + 3);
		//Loading
		Threads.runTask(new Runnable() {
			@Override
			public void run() {
				UINew.this.output.println("Loading data files\n	TranverseTree");
				//TraverseTree.onLoad();
				UINew.this.progressBar.setValue(progressBar.getValue() + 1);
				UINew.this.output.println("	Dictinary");
				Dictionary.onLoad();
				UINew.this.progressBar.setValue(progressBar.getValue() + 1);
				UINew.this.output.println("	Word statitics");
				WordSplit.loadFile();
				UINew.this.progressBar.setValue(progressBar.getValue() + 1);
				
				for(ILanguage language : Languages.languages) {
					UINew.this.output.println("	Lang(" + language.getName() + ")");
					language.loadNGramData();
					UINew.this.progressBar.setValue(progressBar.getValue() + 1);
				}
			
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
		
				UINew.this.progressBar.setValue(0);
				UINew.this.output.clear();
			}
		});
		
	}

	public void finishComponents() {
		this.addWindowStateListener(new WindowStateListener() {
			
			@Override
			public void windowStateChanged(WindowEvent event) {
				int newState = event.getNewState();
				if((newState & Frame.ICONIFIED) == Frame.ICONIFIED)
					for(JDialog dialog : UINew.this.dialogs) 
						dialog.setVisible(false);
			}
		});
		this.addWindowListener(new WindowAdapter() {
			
            @Override
            public void windowDeactivated(WindowEvent e) {
            	for(JDialog dialog : UINew.this.dialogs)
            		dialog.setVisible(false);
            }

            @Override
            public void windowActivated(WindowEvent e) {
            	for(JDialog dialog : UINew.this.dialogs)
            		dialog.setVisible(UINew.this.lastStates.contains(dialog));
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
        this.menuItemClearOutput = new JMenuItem();
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
        this.menuItemIoCHill = new JMenuItem();
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
        this.encodingDiffSlider = new JSlider(JSlider.HORIZONTAL);
        this.menuItemEncode = new JMenuItem();
        this.menuItemEncodeChose = new JMenu();
        this.menuItemSettings = new JMenu();
        this.menuItemLanguage = new JMenu();
        this.menuItemCurrentLanguage = new JMenuItem();
        this.menuItemKeyword = new JMenu();
		this.menuItemKeywordNormal = new JCheckBoxMenuItem();
		this.menuItemKeywordHalf = new JCheckBoxMenuItem();
		this.menuItemKeywordReverse = new JCheckBoxMenuItem();
		this.menuItemCheckShift = new JCheckBoxMenuItem();
		this.menuItemCheckReverse = new JCheckBoxMenuItem();
		this.menuItemCheckRoutes = new JCheckBoxMenuItem();
		this.menuItemSimulatedAnnealing = new JMenu();
        this.menuItemSAPreset = new JMenu();
        this.menuItemUpdateProgress = new JCheckBoxMenuItem();
        this.menuItemCollectSolutions = new JCheckBoxMenuItem();
        this.menuCipherAttack = new JMenu();
        this.menuCribInput = new JMenuItem();
        this.menuItemCurrentAttack = new JMenuItem();
        this.menuDataFiles = new JMenu();
        this.addWordAddDictonary = new JMenuItem();
        
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
        
        this.menuItemClearOutput.setText("Clear Output");
        this.menuItemClearOutput.setIcon(ImageUtil.createImageIcon("/image/page_white.png", "Clear Ouput"));
        this.menuItemClearOutput.addActionListener(new ClearOutputAction());
        this.menuItemFile.add(this.menuItemClearOutput);
        
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
        this.menuItemShowTopSolutions.setIcon(ImageUtil.createImageIcon("/image/table_sort.png", "Top Solutions"));
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
        
        this.menuItemIoCHill.setText("Hill");
        this.menuItemIoCHill.setIcon(ImageUtil.createImageIcon("/image/chart_bar.png", "Hill IoC"));
        this.menuItemIoCHill.addActionListener(new HillIoCAction());
        this.menuItemIoC.add(this.menuItemIoCHill);
        
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
        this.menuItemWordSplit.setIcon(ImageUtil.createImageIcon("/image/book_open.png", "Word Split"));
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
  
        
        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(1, new JLabel("Easy"));
        labelTable.put(10, new JLabel("Hard"));
        this.encodingDiffSlider.setLabelTable(labelTable);
        this.encodingDiffSlider.setMinimum(1);
        this.encodingDiffSlider.setMaximum(10);
        this.encodingDiffSlider.setMinorTickSpacing(1);
        this.encodingDiffSlider.setMajorTickSpacing(9);
        this.encodingDiffSlider.setSnapToTicks(true);
        this.encodingDiffSlider.setPaintTicks(true);
        this.encodingDiffSlider.setPaintLabels(true);
        this.encodingDiffSlider.setFocusable(false);
        this.menuItemEncrypter.add(this.encodingDiffSlider);
        
        this.menuItemEncrypter.addSeparator();
        
        this.menuItemEncodeChose.setText("Specific");
        MenuScroller.setScrollerFor(this.menuItemEncodeChose, 15, 125, 0, 0);
        for(final String key : RandomEncrypter.ciphers.keySet()) {
        	JMenuItem jmi = new JMenuItem(key);
        	jmi.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					String text = getInputTextOnlyAlpha();
	    			String cipherText = RandomEncrypter.getFromName(key).randomlyEncrypt(text);
	 
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
		this.menuItemKeyword.addSeparator();
		
		this.menuItemCheckShift.setText("Check Shift?");
		this.menuItemCheckReverse.setText("Check Reverse?");
		this.menuItemCheckRoutes.setText("Check Routes?");
		
		this.menuItemCheckShift.setSelected(this.settings.checkShift());
		this.menuItemCheckReverse.setSelected(this.settings.checkReverse());
		this.menuItemCheckRoutes.setSelected(this.settings.checkRoutes());
		
		this.menuItemCheckShift.addActionListener(new CheckShiftAction(this.menuItemCheckShift));
		this.menuItemCheckReverse.addActionListener(new CheckReverseAction(this.menuItemCheckReverse));
		this.menuItemCheckRoutes.addActionListener(new CheckRoutesAction(this.menuItemCheckRoutes));

		this.menuItemKeyword.add(this.menuItemCheckShift);
		this.menuItemKeyword.add(this.menuItemCheckReverse);
		this.menuItemKeyword.add(this.menuItemCheckRoutes);
		
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
		
		this.menuItemCollectSolutions.setText("Collect Solutions");
		this.menuItemCollectSolutions.setSelected(this.settings.collectSolutions());
		this.menuItemCollectSolutions.addActionListener(new CollectSolutionsAction(this.menuItemCollectSolutions));
		this.menuItemSettings.add(this.menuItemCollectSolutions);	
		
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
        
        
        this.menuDataFiles.setText("Data Files");
        
        this.addWordAddDictonary.setText("Add words to Dictonary");
        this.addWordAddDictonary.setIcon(ImageUtil.createImageIcon("/image/book_add.png", "Hill IoC"));
     	this.addWordAddDictonary.addActionListener(new WordAddAction());
        this.menuDataFiles.add(this.addWordAddDictonary);
        this.menuBar.add(this.menuDataFiles);
        
        
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
    		UINew.this.dialogs.add(this.dialog);
    		
    		UINew.this.inputTextArea.getDocument().addDocumentListener(new DocumentUtil.DocumentChangeAdapter() {
				@Override
				public void onUpdate(DocumentEvent event) {
					if(NCCDialog.this.dialog.isVisible())
						NCCDialog.this.updateOnWithTextArea();
				}
    		});
    	}
    	
    	public void updateOnWithTextArea() {}
    }
    
    private class NCCDialogBarChart extends NCCDialog {

    	public JPanel mainPanel;
    	public JBarChart[] barCharts;
    	
    	public NCCDialogBarChart(String title, int graphs) {
    		this(title, "image/chart_bar.png", graphs);
    	}
    	
    	public NCCDialogBarChart(String title, String icon, int graphs) {
    		super(title, icon);
    		this.barCharts = new JBarChart[graphs];
    		
    		this.mainPanel = new JPanel();
    		JScrollPane scrollPane = new JScrollPane(this.mainPanel);
    		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    		this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));
	         
    		
    		for(int i = 0; i < this.barCharts.length; i++) {
    			this.barCharts[i] = new JBarChart();
    			this.initialiseChart(this.barCharts[i], i);
    			this.mainPanel.add(this.barCharts[i]);
    		}
	        
    		this.mainPanel.revalidate();
    		this.dialog.add(scrollPane);
    	}
    	
    	public void initialiseChart(JBarChart barChart, int index) {
    		
    	}
    	
    	public void repaintCharts() {
    		for(int i = 0; i < this.barCharts.length; i++) {
    			this.barCharts[i].revalidate();
    			this.barCharts[i].repaint();
    		}
    	}
    	
    	public void resetCharts() {
    		for(int i = 0; i < this.barCharts.length; i++) 
    			this.barCharts[i].resetAll();
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
				Set<Character> uniqueChars = StringTransformer.getUniqueCharSet(inputText);
				statText += "\n Unique Characters: " + uniqueChars.size() + " \n" + uniqueChars;
				statText += "\nSuggested Fitness: " + TextFitness.getEstimatedFitness(inputText, UINew.this.getLanguage());
				statText += "\nActual Fitness: " + TextFitness.scoreFitnessQuadgrams(inputText, UINew.this.getLanguage());
				UINew.this.statTextArea.setText(statText);
				
				UINew.this.menuItemBinary.setEnabled(inputText.length() != 0 && inputText.replaceAll("[^0-1]", "").length() == inputText.length());
				UINew.this.menuItemEncode.setEnabled(inputText.length() != 0);
			} 
			catch(BadLocationException e) {
				e.printStackTrace();
			}
		}
    }
    
    private class CipherSelectAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		DecryptionMethod lastMethod = (DecryptionMethod)UINew.this.decryptionType.getSelectedItem();
    		UINew.this.decryptionType.removeAllItems();
    		CipherAttack decrypt = getCipherAttack();
    		List<DecryptionMethod> methods = decrypt.getAttackMethods();
    		
    		for(DecryptionMethod method : methods)
    			UINew.this.decryptionType.addItem(method);
    		
    		if(methods.contains(lastMethod))
    			UINew.this.decryptionType.setSelectedItem(lastMethod);
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
			final String text = UINew.this.inputTextArea.getText();
			
			if(text == null || text.isEmpty())
				return;
			
			UINew.this.thread = new Thread(new Runnable() {

				@Override
				public void run() {
					UINew.this.threadTimer.restart();
					UINew.BEST_SOULTION = null;
					UINew.topSolutions.reset();
					
					CipherAttack force = getCipherAttack();
					UINew.this.output.println("Cipher: " + force.getDisplayName());
					UINew.this.output.println("Optimizations . Progress Update: %b (" + (char)916 + "s = x3) | Collect Solutions: %b (" + (char)916 + "s = x1.5)", settings.updateProgress(), settings.collectSolutions());
					DecryptionMethod method = (DecryptionMethod)decryptionType.getSelectedItem();
					UINew.this.progressValue = new ProgressValueNC(1000, UINew.this.progressBar, UINew.this.getSettings());
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
					UINew.this.output.println("Time Running: %sms - %ss - %sm\n", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)), df.format(threadTimer.getTimeRunning(Time.MINUTE)));
		
					UINew.this.toolBarStart.setEnabled(true);
					UINew.this.toolBarStop.setEnabled(false);
					UINew.this.menuItemSettings.setEnabled(true);
					
					UINew.this.progressValue.setIndeterminate(false);
					UINew.this.progressBar.setValue(0);
				}
				
			});
			UINew.this.thread.setDaemon(true);
			UINew.this.toolBarStart.setEnabled(false);
			UINew.this.toolBarStop.setEnabled(true);
			UINew.this.menuItemSettings.setEnabled(false);
			UINew.this.thread.start();
		}
    }
    
    public class TerminateAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			if(UINew.this.thread != null) {
				UINew.this.thread.stop();
				CipherAttack force = UINew.this.getCipherAttack();
				force.onTermination(true);
				DecimalFormat df = new DecimalFormat("#.#");
				UINew.this.output.println("Time Running: %sms - %ss - %sm\n", df.format(threadTimer.getTimeRunning(Time.MILLISECOND)), df.format(threadTimer.getTimeRunning(Time.SECOND)), df.format(threadTimer.getTimeRunning(Time.MINUTE)));
				UINew.this.toolBarStart.setEnabled(true);
				UINew.this.toolBarStop.setEnabled(false);
				UINew.this.menuItemSettings.setEnabled(true);
				
				UINew.this.progressValue.setIndeterminate(false);
				UINew.this.progressBar.setValue(0);
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
					UINew.this.dispose();
					if(!isUndecorated()) {
						FullScreenAction.this.lastSize = UINew.this.getSize();
						FullScreenAction.this.lastLocation = UINew.this.getLocation();
						FullScreenAction.this.lastState = UINew.this.getExtendedState();
						UINew.this.setExtendedState(Frame.MAXIMIZED_BOTH);
						UINew.this.setUndecorated(true);
					}
					else {
						UINew.this.setSize(lastSize);
						UINew.this.setLocation(lastLocation);
						UINew.this.setExtendedState(lastState);
						UINew.this.setUndecorated(false);
					}
					
					UINew.this.setVisible(true);
				}
			});
		}
    }
    
    private class ClearOutputAction implements ActionListener {

  		@Override
  		public void actionPerformed(ActionEvent event) {
  			UINew.this.output.clear();
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
				UINew.this.inputTextArea.setText(data);
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
    	public DynamicResultList<Solution> solutions; //Can sort up to 10 Million
    	private boolean updateNeed;
    	
    	public ShowTopSolutionsAction() {
    		super("Top Solutions", "image/table_sort.png");
    		this.dialog.setMinimumSize(new Dimension(900, 400));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        this.textOutput = new JTextArea();

	        JScrollPane scrollPane = new JScrollPane(this.textOutput);
	
	        panel.add(scrollPane);
	        
	        JButton button = new JButton("Sort lastest solutions");
	        
	        button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					sortSolutions();
					updateNeed = false;
				}
	        });
	        
	        panel.add(button);
	        
    		this.dialog.add(panel);
    		
    		this.solutions = new DynamicResultList<Solution>(256);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
		}
    	
    	public void sortSolutions() {
    		this.solutions.sort();
    		
    		String text = "";
    		for(int i = 0; i < this.solutions.size(); i++)
    			text += String.format(i + " %s\n", this.solutions.get(i));

    		this.textOutput.setText(text);
    		this.textOutput.revalidate();
    		this.textOutput.setCaretPosition(0);
    	}

    	public void addSolution(Solution solution) {
    		this.solutions.addResult(solution);
    		this.updateNeed = true;
    	}
    	
    	public void reset() {
    		this.solutions.clear();
    	}
    }
    
    public class BinaryConvertAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = UINew.this.inputTextArea.getText();
			String[] split = StringTransformer.splitInto(binaryText, 5);
			
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
			UINew.this.inputTextArea.setText(cipherText);
		}
    }
    
    public class ASCIIConvertAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = UINew.this.inputTextArea.getText();
			//TODO
			//inputTextArea.setText(cipherText);
		}
    }

    public class ShuffleTextAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = UINew.this.inputTextArea.getText();
    		char[] chars = binaryText.toCharArray();
    		ArrayUtil.shuffle(chars);
    		UINew.this.inputTextArea.setText(new String(chars));	
		}
    }
    
    public class ReverseTextAction implements ActionListener {
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		String binaryText = StringTransformer.reverseString(UINew.this.inputTextArea.getText());
    		UINew.this.inputTextArea.setText(binaryText);	
		}
    }
    
    public class WordSplitAction extends NCCDialog implements ActionListener {
    	
    	private JTextArea textOutput;
    	private JButton copyText;
    	
    	public WordSplitAction() {
    		super("Word Split", "image/book_open.png");
    		this.dialog.setMinimumSize(new Dimension(800, 300));
    		
    		JPanel panel = new JPanel();
	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        
	        this.textOutput = new JTextArea();
	        this.textOutput.setLineWrap(true);
	        this.textOutput.setWrapStyleWord(true);
	        JScrollPane scrollPane = new JScrollPane(this.textOutput);
	        
	        panel.add(scrollPane);
	        
	        
	        this.copyText = new JButton("Copy");
	    	this.copyText.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					StringSelection selection = new StringSelection(WordSplitAction.this.textOutput.getText());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
				}
	    		
	    	});
	    	this.copyText.setHorizontalAlignment(JButton.LEFT);
	    	panel.add(this.copyText);
	    	this.dialog.add(panel);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void updateOnWithTextArea() {
    		String split = WordSplit.splitText(UINew.this.inputTextArea.getText().replaceAll(" ", ""));
    		WordSplitAction.this.textOutput.setText(split.toLowerCase());
    		WordSplitAction.this.textOutput.revalidate();
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
			UINew.this.removeDialog(this.dialog);
		}
    }
    
    public class LetterFrequencyAction extends NCCDialogBarChart implements ActionListener {
    	
    	public LetterFrequencyAction() {
    		super("Letter Frequency", 2);
    		this.dialog.setMinimumSize(new Dimension(500, 300));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		String[] graphTitle = new String[] {"Ordered by Size", "Ordered Alphabeticly"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
    		
    		String text = getInputTextOnlyAlpha();
			
    		if(!text.isEmpty()) {
	    		Map<String, Integer> counts = StringAnalyzer.getEmbeddedStrings(text, 1, 1, false);
				
				List<String> asendingOrder = new ArrayList<String>(counts.keySet());
				Collections.sort(asendingOrder, new StringAnalyzer.SortStringInteger(counts));
				Collections.reverse(asendingOrder);
				
		        for(String letterCount : asendingOrder)
		        	this.barCharts[0].values.add(new ChartData(letterCount, (double)counts.get(letterCount)));
				
				
		        if(!counts.isEmpty())
		        	for(char ch = 'A'; ch <= 'Z'; ch++)
		        		this.barCharts[1].values.add(new ChartData("" + ch, (double)(counts.containsKey("" + ch) ? counts.get("" + ch) : 0)));
    		}
    		
			this.repaintCharts();
    	}
    }
    
    private class NGramFrequencyAction extends NCCDialogBarChart implements ActionListener {
    	
    	private JComboBox<String> comboBox;
    	private JComboBox<String> comboBoxOverlap;
    	
    	public NGramFrequencyAction() {
    		super("N-Gram Frequency", 1);
    		this.dialog.setMinimumSize(new Dimension(900, 300));
    		
	        this.comboBox = new JComboBox<String>(new String[] {"ALL", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15"});
	        this.comboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	        this.comboBox.addItemListener(new ItemListener() {
	        	@Override
			    public void itemStateChanged(ItemEvent event) {
	        		if(event.getStateChange() == ItemEvent.SELECTED)
	        			NGramFrequencyAction.this.updateOnWithTextArea();
			    }       
			});
	        this.mainPanel.add(this.comboBox);
	        this.comboBoxOverlap = new JComboBox<String>(new String[] {"Overlap", "n-Apart"});
	        this.comboBoxOverlap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
	        this.comboBoxOverlap.addItemListener(new ItemListener() {
				@Override
			    public void itemStateChanged(ItemEvent event) {
					if(event.getStateChange() == ItemEvent.SELECTED)
						NGramFrequencyAction.this.updateOnWithTextArea();
			    }       
			});
	        this.mainPanel.add(this.comboBoxOverlap);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
     	@Override
     	public void initialiseChart(JBarChart barChart, int index) {
     		barChart.setHasBarText(false);
     		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Ordered by Size"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
    		
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
		        	if(this.barCharts[0].values.size() < 40)
		        		this.barCharts[0].values.add(new ChartData(ngram, (double)counts.get(ngram)));
    		}
    		
			this.repaintCharts();
    	}
    }
    
    public class ADFGXIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public ADFGXIoCAction() {
    		super("ADFGX IoC", 2);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		String[] graphTitle = new String[] {"Step Calculation", "Periodic IoC Calculation"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
    		this.resetCharts();
			
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
    		}
    		
    		this.repaintCharts();
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
			byte[] s = ColumnarTransposition.decode(this.text, new byte[this.text.length], array, false);
			String str = new String(s);
			double n = calculate(str, this.language);
	    	double evenDiagraphicIC = StatCalculator.calculateIC(str, 2, false);
	    	
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
    
    private class NormalIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public NormalIoCAction() {
    		super("Normal IoC", 1);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Normal IoC Calculation"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 0; period <= Math.min(40, text.length()); ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.barCharts[0].values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    			
    		    this.barCharts[0].setSelected(bestPeriod);
    		}
    		
    		this.repaintCharts();
    	}
    }
    
    public class BifidIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public BifidIoCAction() {
    		super("Bifid IoC", 2);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		String[] graphTitle = new String[] {"Step Calculation x2", "Periodic IoC Calculation"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
    		this.resetCharts();
			
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
				 
				    this.barCharts[0].values.add(new ChartData("Step: " + step, variance));
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
						this.barCharts[0].setSelected(bestStep - 1);
						
						periodGuess = Math.min(bestStep, maxStep) * 2 + Math.abs(bestStep - maxStep);
					}
				}
				
				int bestPeriod = -1;
				double bestIC = Double.MIN_VALUE;
			    for(int period = 0; period <= 40; period++) {
			    	if(period == 1) continue;
			    	
			        double score = StatCalculator.calculateBifidDiagraphicIC(text, period);
			        this.barCharts[1].values.add(new ChartData("Period: " + period, score));
			        if(bestIC < score)
			        	bestPeriod = period;
			        
			        bestIC = Math.max(bestIC, score);
			    }
			    
			    this.barCharts[0].setSelected(maxStep - 1);
				this.barCharts[1].setSelected(bestPeriod > 0 ? bestPeriod - 1 : 0);
    		}
    		
    		this.repaintCharts();
    	}
    }
    
    private class HillIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public HillIoCAction() {
    		super("Hill IoC", 1);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Periodic IoC Calculation"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
    		String text = getInputTextOnlyAlpha();
			if(!text.isEmpty()) {
				
				int bestPeriod = -1;
				double bestIC = Double.MIN_VALUE;
				
			    for(int period = 2; period <= 10; period++) {
			        double score = StatCalculator.calculateLongIC(text, period, 26);
			        this.barCharts[0].values.add(new ChartData("" + period, score));
			        if(bestIC < score)
			        	bestPeriod = period;
			        
			        bestIC = Math.max(bestIC, score);
			    }
			    
			    this.barCharts[0].setSelected(bestPeriod - 2);
			}
    		
    		this.repaintCharts();
    	}
    }
    
    private class NicodemusIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public NicodemusIoCAction() {
    		super("Nicodemus IoC", 10);
    		this.dialog.setMinimumSize(new Dimension(800, 900));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		UINew.this.addDialog(this.dialog);
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Block Height: " + (index + 1)));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
    		
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    		    
    		    for(int i = 0; i < this.barCharts.length; i++) {
        			int bestPeriod = -1;
        		    double bestIC = Double.POSITIVE_INFINITY;
    		    
	    		    for(int period = 2; period <= 40; ++period) {
	    		    	double sqDiff = Math.abs(StatCalculator.calculateNicodemusIC(text, i + 1, period) - UINew.this.settings.getLanguage().getNormalCoincidence()) * 1000;
	    		    	
	    		    	if(sqDiff < bestIC)
	    		    		bestPeriod = period;
	    		    	
	    		    	this.barCharts[i].values.add(new ChartData("Period: " + period, sqDiff));
	    		    	
	    		    	bestIC = Math.min(bestIC, sqDiff);
	    		    }
	    			this.barCharts[i].setSelected(bestPeriod - 2);
    		    }
    		}
    		
    		this.repaintCharts();
    	}
    }
    
    private class NihilistIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public NihilistIoCAction() {
    		super("Nihilist Substitution IoC", 2);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		String[] graphTitle = new String[] {"Kappa IoC Calculation", "Average IoC Calculation"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
    		String text = getInputTextOnlyDigits();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestIC = Double.POSITIVE_INFINITY;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateDiagraphicKappaIC(text, period * 2) - settings.getLanguage().getNormalCoincidence(), 2) * 10000;
    		    	
    		    	if(sqDiff < bestIC)
    		    		bestPeriod = period;
    		    	this.barCharts[0].values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIC = Math.min(bestIC, sqDiff);
    		    }
    			
    		    this.barCharts[0].setSelected(bestPeriod - 2);
    		    
    		    bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateIC(StringTransformer.getEveryNthBlock(text, 2, i, period), 2, false);
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.abs(total - settings.getLanguage().getNormalCoincidence()) * 1000;
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.barCharts[1].values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.barCharts[1].setSelected(bestPeriod - 2);
    		}
    		
    		this.repaintCharts();
    	}
    }
    
    public class PortaxIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public PortaxIoCAction() {
    		super("Portax IoC", 1);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Portax IoC Calculation"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty() && text.length() % 2 == 0) {
				double bestIoC = 0.0D;
				int bestPeriod = -1;
				
				for(int period = 2; period <= 40; period++) {
			        int step = 2 * period;
			        int[][] counts = new int[period][26 * 26];
			        for(int j = 0; j < text.length(); j += step) {
			        	for(int k = 0; k < period; k++) {
			                if(j + k + period >= text.length()) 
			                	break;
			        		int c1 = text.charAt(j + k) - 'A';
			        		int c2 = text.charAt(j + k + period) - 'A';
			                counts[k][c1 * 26 + c2] += 1;
			                //string += c1 + "" + c2;
			        	}
			        }
			       // double sqDiff = StatCalculator.calculateIC(string.toCharArray(), 2, false) * 1000;
			        double sumIC = 0.0D;
					for(int i = 0; i < period; i++) {
						double total = 0D;
						int n = 0;
						for(int j = 0; j < 26 * 26; j++) {
							double count = counts[i][j];
							total += count * (count - 1);
							n += count;
						}
						
						if(n > 1)
							sumIC += total / (n * (n - 1));
					}
					sumIC *= 10000;
					sumIC /= period;
			        
			        if(sumIC < bestIoC)
    		    		bestPeriod = period;
    		    	this.barCharts[0].values.add(new ChartData("Period: " + period, sumIC));
    		    	
    		    	bestIoC = Math.min(bestIoC, sumIC);
			    }
				
				this.barCharts[0].setSelected(bestPeriod - 2);
			}
    		
    		this.repaintCharts();
    	}
    	
    	private int[] decodePair(int c1, int c2) {
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
    
    public class ProgressiveKeyIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	private ThreadCancelable threadCancel;
    	
    	public ProgressiveKeyIoCAction() {
    		super("Progressive Key IoC", 1);
    		this.threadCancel = new ThreadCancelable(new Runnable() {
    			@Override
    			public void run() {
    				ProgressiveKeyIoCAction.this.updateOnWithTextArea();
    			}
    		});
    		
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.threadCancel.restart();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Progressive Key IoC Calculation"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
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
							//TODO
							/**
							char[] decoded = ProgressiveKey.decodeBase(arrayText, progressivePeriod, progressiveIndex);
							
							
							double total = 0.0D;
		    		    	for(int i = 0; i < period; i++)
		    		    		total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(decoded, i, period), 1, true);
		    		    	total /= period;
		    		    	
		    		    	double sqDiff = Math.abs(total - settings.getLanguage().getNormalCoincidence()) * 1000;
		    		    	
		    		    	if(sqDiff < bestIoC) {
		    		    		bestProgressivePeriod = progressivePeriod;
		    		    		bestProgressiveIndex = progressiveIndex;
		    		    	}
		  
		    		    	bestIoC = Math.min(bestIoC, sqDiff);
		    		    	list.add(new ProgressiveKeySearch(progressivePeriod, progressiveIndex, sqDiff));**/
						}
					}
					
					Collections.sort(list);
		
					if(bestIoC < bestEverIoC) {
						bestEverIoC = bestIoC;
						bestPeriod = period;
						bestEverProgressivePeriod = bestProgressivePeriod; //WILL GIVE A FACTOR OF THE ACCUTAL ANSWER
    		    		bestEverProgressiveIndex = bestProgressivePeriod; //WILL GIVE A FACTOR OF THE ACCUTAL ANSWER
					}
				
					this.barCharts[0].values.add(new ChartData(String.format("Period: %d, PP %d, PI %d", period, bestProgressivePeriod, bestProgressiveIndex), bestIoC));
		    		this.repaintCharts();
				}
				
				//output.println("Period: " + bestPeriod);
				///output.println("  Prog Period: " + bestProgressivePeriod);
				//output.println("  Prog Index" + bestprogressiveIndex);
			}
    		
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
    
    private class SeriatedPlayfairIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public SeriatedPlayfairIoCAction() {
    		super("Seriated Playfair IoC", 1);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
     	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
     		barChart.setHasBarText(false);
     		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Double Letter Calculation"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
	
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty() && text.length() % 2 == 0)
				for(int period = 2; period <= 40; period++)
					this.barCharts[0].values.add(new ChartData("Period: " + period, StatCalculator.calculateSeriatedPlayfair(text, period) ? 1 : 0));
    		
    		this.repaintCharts();
    	}
    }
    
    public class SlidefairIoCAction extends NCCDialogBarChart implements ActionListener {

    	public SlidefairIoCAction() {
    		super("Slidefair IoC", 2);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		String[] graphTitle = new String[] {"Step Calculation x3", "Periodic IoC Calculation"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
	
			String text = getInputText();
			if(!text.isEmpty()) {
				int bestPeriod = -1;
	    		double bestIoC = Double.MAX_VALUE;
	    		    
	    		for(int period = 2; period <= 40; ++period) {
	    		    double total = 0.0D;
	    		    for(int i = 0; i < period * 2; i++)
	    		    	total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period * 2), 1, true);
	    		    total /= period * 2;
	    		    	
	    		    double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
	    		    	
	    		    if(sqDiff < bestIoC)
	    		    	bestPeriod = period;
	    		    this.barCharts[1].values.add(new ChartData("Period: " + period, sqDiff));
	    		    	
	    		    bestIoC = Math.min(bestIoC, sqDiff);
	    		}
	    		    
	    		this.barCharts[1].setSelected(bestPeriod - 2);
			}
    		
    		this.repaintCharts();
    	}
    }
    
    public class SwagmanTestAction extends NCCDialogBarChart implements ActionListener {

    	public SwagmanTestAction() {
    		super("Swagman Test", 1);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Swagman Test"));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
			String text = getInputTextOnlyAlpha();
			if(!text.isEmpty()) {
				int bestScore = Integer.MIN_VALUE;
				int bestPeriod = -1;
				for(int period = 2; period <= 9; period++) {
		            if(text.length() % period != 0) {
		            	this.barCharts[0].values.add(new ChartData("" + period, 0));
		            	continue;
		            }
		            
		            if(3 * period * period > text.length()) {
		                this.barCharts[0].values.add(new ChartData("" + period, 0));
		            	continue;
		            }
		            
		            int result = StatCalculator.swagTest(text, period);
		            if(result > bestScore) {
		            	bestScore = result;
		            	bestPeriod = period;
		            }
		            this.barCharts[0].values.add(new ChartData("" + period, result));
		        }
				if(bestPeriod != -1)
					this.barCharts[0].setSelected(bestPeriod - 2);
			}
    		
    		this.repaintCharts();
    	}
    }
    
    public class TrifidIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public TrifidIoCAction() {
    		super("Trifid IoC", 2);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		String[] graphTitle = new String[] {"Step Calculation x3", "Trigraphic Phi Test"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
    	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
	
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
				 
				    this.barCharts[0].values.add(new ChartData("Step: " + step, variance));
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
						this.barCharts[0].setSelected(bestStep - 1);
						
						periodGuess = Math.min(bestStep, maxStep) * 3 + Math.abs(bestStep - maxStep);
					}
				}
				
				int bestPeriod = -1;
				double bestIC = Double.MIN_VALUE;
				int size = 3;
				
			    for(int period = 0; period <= 40; period++) {
			    	if(period > 0 && period < size) continue;
			    	
			        double score = StatCalculator.calculateStrangeIC(text, period, size, 27);
			        this.barCharts[1].values.add(new ChartData("" + period, score));
			        if(bestIC < score)
			        	bestPeriod = period;
			        
			        bestIC = Math.max(bestIC, score);
			    }
			    
			    this.barCharts[0].setSelected(maxStep - 1);
				this.barCharts[1].setSelected(bestPeriod > 0 ? bestPeriod - 2 : 0);
			}
    		
    		this.repaintCharts();
    	}
    }
    
    private class VigenereIoCAction extends NCCDialogBarChart implements ActionListener {
    	
    	public VigenereIoCAction() {
    		super("Vigenere IoC", 2);
    		this.dialog.setMinimumSize(new Dimension(800, 400));
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		addDialog(this.dialog);
    		
    		this.updateOnWithTextArea();
		}
    	
     	@Override
    	public void initialiseChart(JBarChart barChart, int index) {
    		barChart.setHasBarText(false);
    		String[] graphTitle = new String[] {"Kappa IoC Calculation", "Average IoC Calculation"};
    		barChart.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), graphTitle[index]));
    	}
    	
     	@Override
    	public void updateOnWithTextArea() {
			this.resetCharts();
			
    		String text = getInputTextOnlyAlpha();
    		if(!text.isEmpty()) {
    			int bestPeriod = -1;
    		    double bestKappa = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double sqDiff = Math.pow(StatCalculator.calculateKappaIC(text, period) - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestKappa)
    		    		bestPeriod = period;
    		    	this.barCharts[0].values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestKappa = Math.min(bestKappa, sqDiff);
    		    }
    		    
    		    this.barCharts[0].setSelected(bestPeriod - 2);
    		    
    		    bestPeriod = -1;
    		    double bestIoC = Double.MAX_VALUE;
    		    
    		    for(int period = 2; period <= 40; ++period) {
    		    	double total = 0.0D;
    		    	for(int i = 0; i < period; i++)
    		    		total += StatCalculator.calculateIC(StringTransformer.getEveryNthChar(text, i, period), 1, true);
    		    	total /= period;
    		    	
    		    	double sqDiff = Math.pow(total - settings.getLanguage().getNormalCoincidence(), 2);
    		    	
    		    	if(sqDiff < bestIoC)
    		    		bestPeriod = period;
    		    	this.barCharts[1].values.add(new ChartData("Period: " + period, sqDiff));
    		    	
    		    	bestIoC = Math.min(bestIoC, sqDiff);
    		    }
    		    
    		    this.barCharts[1].setSelected(bestPeriod - 2);
    		}
    		
    		this.repaintCharts();
    	}
    }
    
    private class SolitaireAction extends NCCDialog implements ActionListener, LoadElement {
    	
    	private JTextField passKeyStartingOrder;
    	private JButton copyOrder;
    	private int[] cardOrder;
    	private List<String> order;
    	
    	public SolitaireAction() {
    		super("Solitaire Cipher", "image/playing_card.png");
    		this.dialog.setMinimumSize(new Dimension(900, 600));
    		
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
					final ImageIcon imageIcon = ImageUtil.createScaledImageIcon(String.format("/image/cards/%s_of_%s.png", cards[c], suits[s]), 1D / 8D);
					ButtonUtil.setButtonSizeToIconSize(button, imageIcon);
					button.setIcon(imageIcon);
					button.setDisabledIcon(imageIcon);
					button.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							JButton button = (JButton)e.getSource();
							if(button.getIcon() == null) {
								button.setIcon(imageIcon);
								SolitaireAction.this.order.remove("" + id);
							}
							else {
								button.setIcon(null);
								SolitaireAction.this.order.add("" + id);
							}
							SolitaireAction.this.passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
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
						SolitaireAction.this.order.remove("52");
					}
					else {
						button.setIcon(null);
						SolitaireAction.this.order.add("52");
					}
					SolitaireAction.this.passKeyStartingOrder.setText(StringTransformer.joinWith(SolitaireAction.this.order, ","));
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
						SolitaireAction.this.order.remove("53");
					}
					else {
						button.setIcon(null);
						SolitaireAction.this.order.add("53");
					}
					SolitaireAction.this.passKeyStartingOrder.setText(StringTransformer.joinWith(order, ","));
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
					SolitaireAction.this.order.add("*");
	
					SolitaireAction.this.passKeyStartingOrder.setText(StringTransformer.joinWith(SolitaireAction.this.order, ","));
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
					SolitaireAction.this.order.clear();
					for(JButton but : buttons)
						but.setIcon(but.getDisabledIcon());
					
					SolitaireAction.this.passKeyStartingOrder.setText(StringTransformer.joinWith(SolitaireAction.this.order, ","));
					suitPanels[0].repaint();
				}
			});
			suitPanels[3].add(button);
		
	        this.cardOrder = new int[] {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};
	    	
	        this.passKeyStartingOrder = new JTextField("");
	        this.passKeyStartingOrder.setMinimumSize(new Dimension(882, 0));
	        this.passKeyStartingOrder.setPreferredSize(new Dimension(882, 20));
	    	panel.add(this.passKeyStartingOrder);
			
			
	    	this.copyOrder = new JButton("Copy");
	    	this.copyOrder.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					StringSelection selection = new StringSelection(SolitaireAction.this.passKeyStartingOrder.getText());
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
				}
	    		
	    	});
	    	panel.add(this.copyOrder);
	    	
    		this.dialog.add(panel);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		UINew.this.addDialog(this.dialog);
    		//this.cardOrder = Solitaire.nextCardOrder(this.cardOrder);
		}

		@Override
		public void write(HashMap<String, Object> map) {
			
		}

		@Override
		public void read(HashMap<String, Object> map) {
			
		}
    }
    
    private class IdentifyAction extends NCCDialog implements ActionListener {
    	
    	private JPanel cipherInfoPanel;
    	private JPanel cipherScorePanel;
    	private JScrollPane scrollPane;
    	private HashMap<String, JCheckBox> statCheckBoxes;
    	private HashMap<String, TextStatistic> stats;
    	private List<IdentifyOutput> lastNumDev;
    	
    	public IdentifyAction() {
    		super("Identify Cipher", "image/page_white_find.png");
    		this.statCheckBoxes = new HashMap<String, JCheckBox>();
    		
    		this.dialog.setMinimumSize(new Dimension(500, 700));

    		JPanel panel = new JPanel();

	        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
	        JPanel optionPanel = new JPanel();
	        optionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
	        optionPanel.setLayout(new GridLayout(0, 4));
	        for(String key : StatisticHandler.map.keySet()) {
	        	JCheckBox checkBox = new JCheckBox(StatisticHandler.shortNameMap.get(key));
	        	statCheckBoxes.put(key, checkBox);
	        	checkBox.setFont(checkBox.getFont().deriveFont(Font.BOLD));
	        	checkBox.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent event) {
						updateDialog();
					}
	        	});
	            checkBox.setSelected(true);
	        	optionPanel.add(checkBox);
	        }
	        JButton refreshButton = new JButton("Refresh");
	        refreshButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					((JButton)event.getSource()).setEnabled(false);
					for(JCheckBox checkBox : statCheckBoxes.values())
						checkBox.setEnabled(false);
					
					optionPanel.remove(refreshButton);
					JProgressBar progress = new JProgressBar();
					progress.setStringPainted(true);
					optionPanel.add(progress);
					optionPanel.repaint();
					optionPanel.revalidate();
					cipherInfoPanel.removeAll();
		    		cipherScorePanel.removeAll();
				    cipherScorePanel.revalidate();
		    		cipherScorePanel.repaint();	
				    cipherInfoPanel.revalidate();
				    cipherInfoPanel.repaint();	

					Threads.runTask(new Runnable() {

						@Override
						public void run() {
							stats = StatisticHandler.createTextStatistics(getInputText(), progress);
				     		lastNumDev = null;
				     		updateDialog();
				     		optionPanel.remove(progress);
				     		optionPanel.add(refreshButton);
							((JButton)event.getSource()).setEnabled(true);
							for(JCheckBox checkBox : statCheckBoxes.values())
								checkBox.setEnabled(true);
						}
					});
				}
	        });
	        optionPanel.add(refreshButton);
	        panel.add(optionPanel);
	        
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
		   
		    panel.add(scrollPane);
	         
    		this.dialog.add(panel);
    		this.dialog.pack();
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
     		addDialog(this.dialog);
		}
    	
	    public Comparator<IdentifyOutput> comparator = new Comparator<IdentifyOutput>() {
	    	@Override
	        public int compare(IdentifyOutput c1, IdentifyOutput c2) {
	        	double diff = c1.score - c2.score;
	        	return diff == 0.0D ? 0 : diff > 0 ? 1 : -1; 
	        }
	    };
	    
    	public void openDialog(String title, List<IdentifyOutput> num_dev) {
    		JDialog dialog = new JDialog();
  
  
    		JPanel panel = new JPanel();
    		JPanel cipherInfoPanel = new JPanel();
        	JPanel cipherScorePanel = new JPanel();
        	JPanel basePanel = new JPanel();
        	JScrollPane scrollPane = new JScrollPane(basePanel);
		    scrollPane.getVerticalScrollBar().setUnitIncrement(12);
	        basePanel.setLayout(new BoxLayout(basePanel, BoxLayout.X_AXIS));
	        scrollPane = new JScrollPane(basePanel);
		    scrollPane.getVerticalScrollBar().setUnitIncrement(12);
		    cipherInfoPanel.setLayout(new BoxLayout(cipherInfoPanel, BoxLayout.Y_AXIS));
		    basePanel.add(cipherInfoPanel);
		    cipherScorePanel.setLayout(new BoxLayout(cipherScorePanel, BoxLayout.Y_AXIS));
		    basePanel.add(cipherScorePanel);
		    panel.add(scrollPane);
		    
		    Collections.sort(num_dev, this.comparator);
	
		    
		    
		    JLabel titleLabel = new JLabel("Cipher" + StringTransformer.repeat(" ", 15));
		    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
		    cipherInfoPanel.add(titleLabel);
		    
		    JLabel titleScoreLabel = new JLabel("Likelyhood");
		    titleScoreLabel.setFont(titleScoreLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
		    cipherScorePanel.add(titleScoreLabel);
		    
		    for(int i = 0; i < num_dev.size(); i++) {
		    	IdentifyOutput identifyOutput = num_dev.get(i);
		    	JPanel infoPanel = new JPanel();
		    	infoPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		    	infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
		    	JLabel cipherInfoLabel = new JLabel(identifyOutput.id + " ");
		    	
		    	cipherInfoLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
		    	infoPanel.add(cipherInfoLabel);
		    	
		    	if(!identifyOutput.subOutput.isEmpty()) {
		    		JButton zoomIcon = new JButton();
		    		zoomIcon.setIcon(ImageUtil.createImageIcon("/image/page_white_magnify.png", "Sublist"));
		    		zoomIcon.setBorderPainted(false);
		    		zoomIcon.setFocusPainted(false);
		    		ButtonUtil.setButtonSizeToIconSize(zoomIcon, zoomIcon.getIcon(), 4);
		    		zoomIcon.addMouseListener(new MouseAdapter() {
			    		@Override
			    	    public void mouseClicked(MouseEvent event) {  
			    			openDialog(dialog.getTitle() + " " + identifyOutput.id, identifyOutput.subOutput);
			    	    }  
			    	});
		    		infoPanel.add(zoomIcon);
		    	}
		    	
		    	cipherInfoPanel.add(infoPanel);
		    	
		    	String valueStr = String.format("%.2f", identifyOutput.score, 2);
		    	
		    	JLabel cipherScoreLabel = new JLabel(valueStr);
		    	cipherScoreLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
		    	cipherScorePanel.add(cipherScoreLabel);
		    }
		    
		    
    		dialog.add(panel);
    		
    		dialog.addWindowListener(new JDialogCloseEvent(dialog));
    		dialog.setTitle(title);
    		dialog.setAlwaysOnTop(true);
    		dialog.setModal(false);
    		dialog.setResizable(false);
    		dialog.setMinimumSize(new Dimension(320, 200));
    		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/page_white_find.png")));
    		dialog.setFocusableWindowState(false);
    		dialog.setVisible(true);
    		dialog.revalidate();
    		dialog.repaint();	
    		dialog.setLocationRelativeTo(this.dialog);
    		dialogs.add(dialog);
    		addDialog(dialog);
    		

    	}
    	
    	public void updateDialog() {
    		cipherInfoPanel.removeAll();
    		cipherScorePanel.removeAll();
    		
    		if(this.stats != null) {
    			ArrayList<String> doOnly = new ArrayList<String>();
    			for(String id : this.statCheckBoxes.keySet()) {
    				if(this.statCheckBoxes.get(id).isSelected())
    					doOnly.add(id);
    			}
    			
				List<IdentifyOutput> num_dev = StatisticHandler.orderCipherProbibitly(this.stats, doOnly);
				System.out.println(num_dev);

			    Collections.sort(num_dev, this.comparator);
		
			    
			    
			    JLabel titleLabel = new JLabel("Cipher" + StringTransformer.repeat(" ", 40));
			    titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
			    this.cipherInfoPanel.add(titleLabel);
			    
			    JLabel titleScoreLabel = new JLabel("Likelyhood");
			    titleScoreLabel.setFont(titleScoreLabel.getFont().deriveFont(Font.BOLD).deriveFont(20F));
			    this.cipherScorePanel.add(titleScoreLabel);
			    
			    for(int i = 0; i < num_dev.size(); i++) {
			    	IdentifyOutput identifyOutput = num_dev.get(i);
			    	
			    	JPanel infoPanel = new JPanel();
			    	infoPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
			    	infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
			    	JLabel cipherInfoLabel = new JLabel(identifyOutput.id + " ");
			    	
			    	cipherInfoLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	infoPanel.add(cipherInfoLabel);
			    	
			    	if(!identifyOutput.subOutput.isEmpty()) {
			    		JButton zoomIcon = new JButton();
			    		zoomIcon.setIcon(ImageUtil.createImageIcon("/image/page_white_magnify.png", "Sublist"));
			    		zoomIcon.setBorderPainted(false);
			    		zoomIcon.setFocusPainted(false);
			    		ButtonUtil.setButtonSizeToIconSize(zoomIcon, zoomIcon.getIcon(), 4);
			    		zoomIcon.addMouseListener(new MouseAdapter() {
				    		@Override
				    	    public void mouseClicked(MouseEvent event) {  
				    			openDialog(identifyOutput.id, identifyOutput.subOutput);
				    	    }  
				    	});
			    		infoPanel.add(zoomIcon);
			    	}
			    	
			    	this.cipherInfoPanel.add(infoPanel);
			    	
			    	String valueStr = String.format("%.2f", identifyOutput.score, 2);
			    	if(this.lastNumDev != null) {
			    		for(int l = 0; l < this.lastNumDev.size(); l++) {
			    			if(this.lastNumDev.get(l).id.equals(identifyOutput.id)) {
			    				valueStr += " ";
			    				
			    				if(i != l) {
				    				if(i < l)
				    					valueStr += (char)8593;
				    				else if(i > l)
				    					valueStr += (char)8595;
				    				valueStr += " " + Math.abs(i - l);
			    				}
			    				else
			    					valueStr += (char)8592;
			    				
			    				break;
			    			}
			    		}
			    	}
			    	
			    	JLabel cipherScoreLabel = new JLabel(valueStr);
			    	cipherScoreLabel.setFont(cipherInfoLabel.getFont().deriveFont(20F));
			    	this.cipherScorePanel.add(cipherScoreLabel);
			    }
			    
			    this.lastNumDev = num_dev;
			    this.cipherScorePanel.revalidate();
	    		this.cipherScorePanel.repaint();	
			    this.cipherInfoPanel.revalidate();
	    		this.cipherInfoPanel.repaint();	
    		}
    	}
    }
     
    private class TextInformationAction extends NCCDialog implements ActionListener {
    	
    	public JTextArea output;
    	
    	public TextInformationAction() {
    		super("Text Statistics", "image/lock_break.png");
    		this.dialog.setMinimumSize(new Dimension(375, 600));
	       
	        this.output = new JTextArea();
			JScrollPane outputScrollPanel = new JScrollPane(this.output);
			outputScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			outputScrollPanel.setPreferredSize(new Dimension(1000, 200));
			this.output.setEditable(false);
			this.output.setLineWrap(true);
		
	
    		this.dialog.add(outputScrollPanel);
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		this.dialog.setVisible(true);
    		UINew.this.addDialog(this.dialog);
     		
    		this.updateOnWithTextArea();
		}
    	
    	@Override
    	public void updateOnWithTextArea() {
    		String text = getInputTextOnlyAlpha();
    		int length = text.length();
    		
    		String outputText = "Length: " + length;
			outputText += "\nEstimated Fitness for length: " + Rounder.round(TextFitness.getEstimatedFitness(text, UINew.this.getLanguage()), 4);
    		
    		if(!text.isEmpty()) {

    			outputText += "\n IC: " + StatCalculator.calculateIC(text, 1, true) * 1000.0D;
    			outputText += "\n MIC: " + StatCalculator.calculateMaxIC(text, 1, 15) * 1000.0D;
    		    outputText += "\n MKA: " + StatCalculator.calculateMaxKappaIC(text, 1, 15);
    		    outputText += "\n DIC: " + StatCalculator.calculateIC(text, 2, true) * 10000.0D;
    		    outputText += "\n EDI: " + StatCalculator.calculateIC(text, 2, false) * 10000;
    		    outputText += "\n LR: " + StatCalculator.calculateLR(text);
    		    outputText += "\n ROD: " + StatCalculator.calculateROD(text);
    		    outputText += "\n LDI: " + PolyalphabeticIdentifier.calculateLDI(text);
    		    outputText += "\n SDD: " + StatCalculator.calculateSDD(text);

    		    outputText += "\n A_LDI: " + PolyalphabeticIdentifier.calculateAutokeyPortaLDI(text);
    		    outputText += "\n B_LDI: " + PolyalphabeticIdentifier.calculateBeaufortLDI(text);
    		    outputText += "\n P_LDI: " + PolyalphabeticIdentifier.calculatePortaLDI(text);
    		    outputText += "\n S_LDI: " + Math.max(PolyalphabeticIdentifier.calculateSlidefairBeaufortLDI(text), PolyalphabeticIdentifier.calculateSlidefairVigenereLDI(text));
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
    			List<String> possible = RandomEncrypter.getAllWithDifficulty(UINew.this.encodingDiffSlider.getValue());
    			IRandEncrypter randomEncrypt = RandomEncrypter.getFromName(RandomUtil.pickRandomElement(possible));
    			String cipherText = randomEncrypt.randomlyEncrypt(text);
    			UINew.this.output.println(randomEncrypt.getClass().getSimpleName());
    			UINew.this.output.println(StringTransformer.repeat("\n", 25));
    			StringSelection selection = new StringSelection(cipherText);
	    		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    			
	    		UINew.this.output.println(cipherText);
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
    		UINew.this.getSettings().setLanguage(this.language);
    		UINew.this.menuItemCurrentLanguage.setText("Current: " + this.language.getName());
		}
    }
    
    private class KeywordCreationAction implements ActionListener {
    	
    	public int id;
    	
    	public KeywordCreationAction(int id) {
    		this.id = id;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		UINew.this.getSettings().setKeywordCreationId(this.id);
		}
    }

    private class CheckShiftAction implements ActionListener {
    	
    	public JCheckBoxMenuItem menuItem;
    	
    	public CheckShiftAction(JCheckBoxMenuItem menuItem) {
    		this.menuItem = menuItem;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		UINew.this.getSettings().checkShift = this.menuItem.isSelected();
		}
    }
    
    private class CheckReverseAction implements ActionListener {
    	
    	public JCheckBoxMenuItem menuItem;
    	
    	public CheckReverseAction(JCheckBoxMenuItem menuItem) {
    		this.menuItem = menuItem;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		UINew.this.getSettings().checkReverse = this.menuItem.isSelected();
		}
    }
    
    private class CheckRoutesAction implements ActionListener {
    	
    	public JCheckBoxMenuItem menuItem;
    	
    	public CheckRoutesAction(JCheckBoxMenuItem menuItem) {
    		this.menuItem = menuItem;
    	}
    	
    	@Override
		public void actionPerformed(ActionEvent event) {
    		UINew.this.getSettings().checkRoutes = this.menuItem.isSelected();
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
			UINew.this.getSettings().getSA().set(this.id, Double.valueOf(this.textComponent.getText()));
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
			UINew.this.getSettings().getSA().set(0, this.tempStart);
			UINew.this.getSettings().getSA().set(1, this.tempStep);
			UINew.this.getSettings().getSA().set(2, (double)this.count);
			this.tempSetting.setText(ValueFormat.getNumber(this.tempStart));

			this.tempStepSetting.setText(ValueFormat.getNumber(this.tempStep));

			this.countSetting.setText(ValueFormat.getNumber(this.count));	
		}
    }
    
    public class UpdateProgressAction implements ActionListener {

    	public JCheckBoxMenuItem checkBox;
    	
    	public UpdateProgressAction(JCheckBoxMenuItem checkBox) {
    		this.checkBox = checkBox;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent event) {
			if(this.checkBox.isSelected()) {
				UINew.this.getSettings().updateProgressBars = true;
				UINew.this.progressBar.setString("0.0%");
			}
			else {
				UINew.this.progressBar.setValue(0);
				UINew.this.progressBar.setString("Inactive");
				UINew.this.settings.updateProgressBars = false;
			}

			UINew.this.keyPanel.setIterationUnsed();
		}
    }
    
    public class CollectSolutionsAction implements ActionListener {

    	public JCheckBoxMenuItem checkBox;
    	
    	public CollectSolutionsAction(JCheckBoxMenuItem checkBox) {
    		this.checkBox = checkBox;
    	}
    	
		@Override
		public void actionPerformed(ActionEvent event) {
			UINew.this.settings.collectSolutions = this.checkBox.isSelected();
		}
    }
    
    public class CribInputAction implements ActionListener {

    	@Override
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
    
    public class WordAddAction implements ActionListener {

    	@Override
    	public void actionPerformed(ActionEvent event) {
    		JDialog dialog = new JDialog(UINew.this);
    		
    		dialog.setTitle("Add words to Dictonary");
    		
    		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/book_add.png")));
    		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    		dialog.setResizable(false);
    		dialog.setMinimumSize(new Dimension(400, 200));
    		dialog.setModal(true);
   
    		ActionListener escListener = new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent event) {
    	        	dialog.dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
    	        }
    	    };

    	    dialog.getRootPane().registerKeyboardAction(escListener, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
    		
    		JPanel panel = new JPanel();
    		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createCompoundBorder(new EmptyBorder(2, 2, 2, 2), BorderFactory.createEtchedBorder()));
    	    dialog.add(panel);
    	    JTextArea wordInput = new JTextArea();
    	    JScrollPane scrollArea = new JScrollPane(wordInput);
    	    wordInput.setLineWrap(true);
    	    JButton button = new JButton("Compile words");
    	    button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent event) {
					String text = wordInput.getText();
					String[] words = text.split("[\\s+]+");
					System.out.println(Arrays.toString(words));
					DefaultTableModel model = new DefaultTableModel(new String[] {"Word", "Valid"}, words.length) {
						private static final long serialVersionUID = 1L;

						@Override
						public boolean isCellEditable(int row, int col) {
							return false;
						}
					};
					JTable table = new JTable(model);
					JScrollPane tableScroll = new JScrollPane(table);
					panel.removeAll();
					panel.add(tableScroll);
					int i = 0;
					for(String word : words) {
						word = word.toUpperCase();
						word = word.replaceAll("[^a-zA-Z]+", "");
						if(word.length() == 0) continue;
						
						if(Dictionary.containsWord(word)) {
							//model.setValueAt(word, i, 0);
							//model.setValueAt("Already a word", i++, 1);
						}
						else {
							model.setValueAt(word, i, 0);
							model.setValueAt("Found new word", i++, 1);
						}
					}
					model.setRowCount(i);
					panel.revalidate();
					panel.repaint();
				}
    	    	
    	    });
    	    panel.add(scrollArea);
    	    panel.add(button);
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
    		UINew.this.menuItemCurrentAttack.setText("Target: " + this.menuItem.getText());
    		
    		int index = 0;
    		for(String name : AttackRegistry.getNames()) {
    			if(this.menuItem.getText().equals(name)) {
    				UINew.this.cipherSelect.setSelectedIndex(index);
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
	

	@Override
	public JDialog openGraph(JBarChart barChart) {
		JDialog dialog = new JDialog();
		dialog.addWindowListener(new JDialogCloseEvent(dialog));
		dialog.setAlwaysOnTop(true);
		dialog.setModal(false);
		dialog.setResizable(false);
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("image/chart_bar.png")));
		dialog.setFocusableWindowState(false);
		dialog.setMinimumSize(new Dimension(800, 400));
		
		JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(barChart);
        dialog.add(panel);
        
        dialog.setVisible(true);
		dialogs.add(dialog);
		addDialog(dialog);
		
		return dialog;
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
	private JMenuItem menuItemClearOutput;
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
    private JMenuItem menuItemIoCHill;
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
    private JSlider encodingDiffSlider;
    private JMenuItem menuItemEncode;
    private JMenu menuItemEncodeChose;
    private JMenu menuItemSettings;
    private JMenu menuItemLanguage;
    private JMenuItem menuItemCurrentLanguage;
    private JMenu menuItemKeyword;
    private JMenuItem menuItemKeywordNormal;
    private JMenuItem menuItemKeywordHalf;
    private JMenuItem menuItemKeywordReverse;
    private JCheckBoxMenuItem menuItemCheckShift;
    private JCheckBoxMenuItem menuItemCheckReverse;
    private JCheckBoxMenuItem menuItemCheckRoutes;
    private JMenu menuItemSimulatedAnnealing;
    private JMenu menuItemSAPreset;
    private JCheckBoxMenuItem menuItemUpdateProgress;
    private JCheckBoxMenuItem menuItemCollectSolutions;
    private JMenu menuCipherAttack;
    private JMenuItem menuCribInput;
    private JMenuItem menuItemCurrentAttack;
    private JMenu menuDataFiles;
    private JMenuItem addWordAddDictonary;
}