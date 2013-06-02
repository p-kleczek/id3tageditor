package id3editor.mvc;

/**
 * @author Pawel, Florian, Sebastian (Gruppe 4)
 */

import id3editor.MP3Player;
import id3editor.data.MP3File;
import id3editor.data.tag.MP3TagFrameTypes;
import id3editor.parser.Writer;
import id3editor.toolbox.ImageOpperations;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import net.iharder.dnd.FileDrop;

/*
 * To provide highly qualified GUI layer, an internal NetBeans GUI builder was used.
 */

@SuppressWarnings("serial")
public class Application extends javax.swing.JFrame implements Observer {

	public final ImageIcon noCoverImageIcon = new ImageIcon(getClass()
			.getResource("/images/noCover.png")); //$NON-NLS-1$

	private static Application application = new Application();

	public static Application getApplication() {
		return application;
	}

	/**
	 * Creates new form Application
	 */
	public Application() {
		initLookAndFeel();
		initComponents();
	}

	public static void initialize() {
		Application.getApplication().setVisible(true);
		Control.getControl().addObserver(Application.getApplication());
		Writer.getWriter().addObserver(Application.getApplication());
		MP3Player.getInstance().addObserver(Application.getApplication());
	}

	private void initComponents() {

		// panels
		splitPane = new javax.swing.JSplitPane();
		treeScrollPane = new javax.swing.JScrollPane();

		hierarchyTree = new javax.swing.JTree();
		new FileDrop(null, hierarchyTree, new FileDrop.Listener() {

			public void filesDropped(java.io.File[] files) {
				for (int i = 0; i < files.length; i++) {
					Control.getControl().addWatchedFolder(files[i]);
				}
			}
		});

		tabPanel = new javax.swing.JTabbedPane();
		fileTab = new javax.swing.JPanel();
		folderTab = new javax.swing.JScrollPane();
		folderTable = new javax.swing.JTable();
		playerTab = new javax.swing.JPanel();
		// labels
		titleLabel = new javax.swing.JLabel();
		artistLabel = new javax.swing.JLabel();
		albumLabel = new javax.swing.JLabel();
		yearLabel = new javax.swing.JLabel();
		coverLabel = new javax.swing.JLabel();
		coverImageLabel = new javax.swing.JLabel();
		playerSongLabel = new javax.swing.JLabel();
		// textfields
		titleTextField = new javax.swing.JTextField();
		artistTextField = new javax.swing.JTextField();
		albumTextField = new javax.swing.JTextField();
		yearTextField = new javax.swing.JTextField();
		// buttons
		changeCoverButton = new javax.swing.JButton();
		deleteCoverButton = new javax.swing.JButton();
		startPlayerButton = new javax.swing.JButton();
		stopPlayerButton = new javax.swing.JButton();

		// menubar
		menuBar = new javax.swing.JMenuBar();
		firstMenu = new javax.swing.JMenu();
		addFolderMenuItem = new javax.swing.JMenuItem();
		saveChangesMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("ID3Tag Editor   (by Florian, Sebastian and Pawel)       MPGI4 WiSe 2011-12"); //$NON-NLS-1$
		setIconImages(null);

		// TreePanel
		treeScrollPane.setMinimumSize(new java.awt.Dimension(150, 23));
		hierarchyTree.setModel(Model.getModel().getTreeModel());
		hierarchyTree.setRootVisible(true);
		hierarchyTree
				.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {

					public void valueChanged(
							javax.swing.event.TreeSelectionEvent evt) {
						treePanelValueChanged(evt);
					}
				});
		treeScrollPane.setViewportView(hierarchyTree);

		splitPane.setLeftComponent(treeScrollPane);

		// setup labels
		titleLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		titleLabel.setText(""); //$NON-NLS-1$
		artistLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		artistLabel.setText(""); //$NON-NLS-1$
		albumLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		albumLabel.setText(""); //$NON-NLS-1$
		yearLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		yearLabel.setText(""); //$NON-NLS-1$
		yearLabel.setToolTipText(""); //$NON-NLS-1$
		coverLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		coverLabel.setText(""); //$NON-NLS-1$
		coverImageLabel.setIcon(noCoverImageIcon);

		// setup textfields
		titleTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		titleTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				textFieldKeyReleased(evt);
			}
		});

		artistTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		artistTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				textFieldKeyReleased(evt);
			}
		});

		albumTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		albumTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				textFieldKeyReleased(evt);
			}
		});

		yearTextField.setFont(new java.awt.Font("Tahoma", 0, 14)); //$NON-NLS-1$
		yearTextField.addKeyListener(new java.awt.event.KeyAdapter() {

			@Override
			public void keyReleased(java.awt.event.KeyEvent evt) {
				textFieldKeyReleased(evt);
			}
		});

		changeCoverButton.setText(Captions.getString("App.change")); //$NON-NLS-1$
		changeCoverButton.setActionCommand("CHANGE_COVER"); //$NON-NLS-1$
		changeCoverButton
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						changeCoverButtonActionPerformed(evt);
					}
				});

		deleteCoverButton.setText(Captions.getString("App.clear")); //$NON-NLS-1$
		deleteCoverButton.setActionCommand("DELETE_COVER"); //$NON-NLS-1$
		deleteCoverButton
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						changeCoverButtonActionPerformed(evt);

						deleteCoverButton.setEnabled(false);
						coverImageLabel.setIcon(noCoverImageIcon);
					}
				});

		javax.swing.GroupLayout fileTabLayout = new javax.swing.GroupLayout(
				fileTab);
		fileTab.setLayout(fileTabLayout);
		fileTabLayout
				.setHorizontalGroup(fileTabLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								fileTabLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																artistLabel)
														.addComponent(
																titleLabel)
														.addComponent(
																albumLabel)
														.addComponent(yearLabel)
														.addComponent(
																coverLabel))
										.addGap(18, 18, 18)
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																yearTextField)
														.addComponent(
																albumTextField)
														.addComponent(
																titleTextField)
														.addComponent(
																artistTextField,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																338,
																Short.MAX_VALUE)
														.addGroup(
																fileTabLayout
																		.createSequentialGroup()
																		.addComponent(
																				coverImageLabel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				150,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addGroup(
																				fileTabLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								changeCoverButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(
																								deleteCoverButton,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								78,
																								Short.MAX_VALUE))
																		.addGap(0,
																				0,
																				Short.MAX_VALUE)))
										.addContainerGap()));
		fileTabLayout
				.setVerticalGroup(fileTabLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								fileTabLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																titleLabel)
														.addComponent(
																titleTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																artistLabel)
														.addComponent(
																artistTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																albumLabel)
														.addComponent(
																albumTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(yearLabel)
														.addComponent(
																yearTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(18, 18, 18)
										.addGroup(
												fileTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																fileTabLayout
																		.createSequentialGroup()
																		.addComponent(
																				changeCoverButton)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				deleteCoverButton)
																		.addGap(8,
																				8,
																				8)
																		.addComponent(
																				coverLabel))
														.addComponent(
																coverImageLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																150,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(12, Short.MAX_VALUE)));

		tabPanel.addTab(Captions.getString("App.dataView"), fileTab); //$NON-NLS-1$

		folderTable.setAutoCreateRowSorter(true);
		folderTable.setModel(Model.getModel().getTableModel());
		folderTable.setEnabled(false);
		folderTable.setAutoscrolls(false);
		folderTable.setCursor(new java.awt.Cursor(
				java.awt.Cursor.DEFAULT_CURSOR));
		folderTab.setViewportView(folderTable);

		tabPanel.addTab(Captions.getString("App.folderView"), folderTab); //$NON-NLS-1$

		startPlayerButton.setText(Captions.getString("App.play")); //$NON-NLS-1$
		startPlayerButton.setActionCommand("START_PLAYER"); //$NON-NLS-1$
		startPlayerButton
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						playerActionHandler(evt);
					}
				});

		stopPlayerButton.setText(Captions.getString("App.stop")); //$NON-NLS-1$
		stopPlayerButton.setActionCommand("STOP_PLAYER"); //$NON-NLS-1$
		stopPlayerButton.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				playerActionHandler(evt);
			}
		});

		playerSongLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N //$NON-NLS-1$
		playerSongLabel.setText(Captions.getString("App.nothingIsPlayer")); //$NON-NLS-1$

		javax.swing.GroupLayout playerTabLayout = new javax.swing.GroupLayout(
				playerTab);
		playerTab.setLayout(playerTabLayout);
		playerTabLayout
				.setHorizontalGroup(playerTabLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								playerTabLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												playerTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																playerTabLayout
																		.createSequentialGroup()
																		.addComponent(
																				startPlayerButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				90,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				stopPlayerButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				99,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																playerSongLabel))
										.addContainerGap(230, Short.MAX_VALUE)));
		playerTabLayout
				.setVerticalGroup(playerTabLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								playerTabLayout
										.createSequentialGroup()
										.addContainerGap(280, Short.MAX_VALUE)
										.addComponent(playerSongLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												playerTabLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																startPlayerButton)
														.addComponent(
																stopPlayerButton))
										.addContainerGap()));

		startPlayerButton.getAccessibleContext().setAccessibleName(
				"startButton"); //$NON-NLS-1$
		stopPlayerButton.getAccessibleContext().setAccessibleName("stopButton"); //$NON-NLS-1$
		playerSongLabel.getAccessibleContext().setAccessibleName(""); //$NON-NLS-1$

		tabPanel.addTab(Captions.getString("App.player"), playerTab); //$NON-NLS-1$

		splitPane.setRightComponent(tabPanel);

		firstMenu.setText(Captions.getString("App.plik")); //$NON-NLS-1$

		addFolderMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_PLUS,
				java.awt.event.InputEvent.CTRL_MASK));
		addFolderMenuItem.setText(Captions.getString("App.addFolder")); //$NON-NLS-1$
		addFolderMenuItem.setActionCommand("ADD_FOLDER_MENU"); //$NON-NLS-1$
		addFolderMenuItem
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						menuActionHandler(evt);
					}
				});
		firstMenu.add(addFolderMenuItem);

		saveChangesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_MASK));
		saveChangesMenuItem.setText(Captions.getString("App.saveChanges")); //$NON-NLS-1$
		saveChangesMenuItem.setActionCommand("SAVE_MENU"); //$NON-NLS-1$
		saveChangesMenuItem
				.addActionListener(new java.awt.event.ActionListener() {

					public void actionPerformed(java.awt.event.ActionEvent evt) {
						menuActionHandler(evt);
					}
				});
		firstMenu.add(saveChangesMenuItem);

		firstMenu.add(new JSeparator());

		exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(
				java.awt.event.KeyEvent.VK_ESCAPE,
				java.awt.event.InputEvent.CTRL_MASK));
		exitMenuItem.setText(Captions.getString("App.exit")); //$NON-NLS-1$
		exitMenuItem.setActionCommand("EXIT_MENU"); //$NON-NLS-1$
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {

			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuActionHandler(evt);
			}
		});
		firstMenu.add(exitMenuItem);

		menuBar.add(firstMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 597,
				Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 367,
				Short.MAX_VALUE));

		setControlsEnabled(false);

		addWindowListener(Control.getControl().mainWindowListener);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void changeCoverButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_changeCoverButtonActionPerformed
		Control.getControl().doCoverAction(evt.getActionCommand());
	}// GEN-LAST:event_changeCoverButtonActionPerformed

	private void menuActionHandler(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_menuActionHandler
		Control.getControl().doMenuGuiAction(evt.getActionCommand());
	}// GEN-LAST:event_menuActionHandler

	private void treePanelValueChanged(javax.swing.event.TreeSelectionEvent evt) {// GEN-FIRST:event_treePanelValueChanged
		Control.getControl().valueChanged(evt);
	}// GEN-LAST:event_treePanelValueChanged

	private void textFieldKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_textFieldKeyReleased
		if (evt.getSource() instanceof JTextField) {
			JTextField tf = (JTextField) evt.getSource();
			String text = tf.getText();

			if (titleTextField == tf) {
				Control.getControl().modifyCurrentNodeTextTag(
						MP3TagFrameTypes.SONGNAME, text);
			}

			if (artistTextField == tf) {
				Control.getControl().modifyCurrentNodeTextTag(
						MP3TagFrameTypes.COMPOSER, text);
			}

			if (albumTextField == tf) {
				Control.getControl().modifyCurrentNodeTextTag(
						MP3TagFrameTypes.ALBUM, text);
			}
			if (yearTextField == tf) {
				String year = yearTextField.getText();
				String id3YearPattern = "([1-9][0-9]{3})|(^$)"; //$NON-NLS-1$

				if (Pattern.matches(id3YearPattern, year)) {
					yearTextField.setBackground(Color.white);
					Control.getControl().modifyCurrentNodeTextTag(
							MP3TagFrameTypes.YEAR, year);
				} else {
					yearTextField.setBackground(Color.red);
				}
			}
		}
	}// GEN-LAST:event_textFieldKeyReleased

	private void playerActionHandler(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_playerActionHandler
		Control.getControl().doPlayerAction(evt.getActionCommand());
	}// GEN-LAST:event_playerActionHandler

	public static void initLookAndFeel() {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
					.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) { //$NON-NLS-1$
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;

				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName())
					.log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>
	}

	private javax.swing.JLabel coverImageLabel;
	private javax.swing.JLabel coverLabel;
	private javax.swing.JMenuItem addFolderMenuItem;
	private javax.swing.JLabel albumLabel;
	private javax.swing.JTextField albumTextField;
	private javax.swing.JLabel artistLabel;
	private javax.swing.JTextField artistTextField;
	private javax.swing.JButton changeCoverButton;
	private javax.swing.JButton deleteCoverButton;
	private javax.swing.JPanel fileTab;
	private javax.swing.JMenu firstMenu;
	private javax.swing.JScrollPane folderTab;
	private javax.swing.JTable folderTable;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenuItem saveChangesMenuItem;
	private javax.swing.JLabel playerSongLabel;
	private javax.swing.JPanel playerTab;
	private javax.swing.JSplitPane splitPane;
	private javax.swing.JButton startPlayerButton;
	private javax.swing.JButton stopPlayerButton;
	private javax.swing.JTabbedPane tabPanel;
	private javax.swing.JLabel titleLabel;
	private javax.swing.JTextField titleTextField;
	private javax.swing.JTree hierarchyTree;
	private javax.swing.JScrollPane treeScrollPane;
	private javax.swing.JLabel yearLabel;
	private javax.swing.JTextField yearTextField;

	@Override
	public void update(Observable o, Object arg) {
		updateShownFile();
		updatePlayer();

		hierarchyTree.repaint(); // sets up modification marker
	}

	/**
	 * Fills controls with content of selected file's tags.
	 */
	public void updateShownFile() {
		MP3File file = Model.getModel().getFileInWork();

		if (file != null) {
			setControlsEnabled(true);
			
			titleTextField.setText(file
					.getTextContentById(MP3TagFrameTypes.SONGNAME));
			artistTextField.setText(file
					.getTextContentById(MP3TagFrameTypes.COMPOSER));
			albumTextField.setText(file
					.getTextContentById(MP3TagFrameTypes.ALBUM));
			yearTextField.setBackground(Color.white);
			yearTextField.setText(file
					.getTextContentById(MP3TagFrameTypes.YEAR));

			ImageIcon icon = ImageOpperations.ByteArrayToIcon(
					file.getCoverPicture(), coverImageLabel.getWidth(),
					coverImageLabel.getHeight());

			coverImageLabel.setIcon(icon);

			// Delete button only enabled if file contains cover image.
			if (icon.getImage() == null)
				deleteCoverButton.setEnabled(false);
			else
				deleteCoverButton.setEnabled(true);

		} else {
			setControlsEnabled(false);
			
			titleTextField.setText(""); //$NON-NLS-1$
			artistTextField.setText(""); //$NON-NLS-1$
			albumTextField.setText(""); //$NON-NLS-1$
			yearTextField.setText(""); //$NON-NLS-1$
			yearTextField.setBackground(Color.white);

			coverImageLabel.setIcon(noCoverImageIcon);
		}
		
		stopPlayerButton.setEnabled(MP3Player.getInstance().isPlaying());
	}

	public void updatePlayer() {
		if (MP3Player.getInstance().isPlaying()) {
			playerSongLabel.setText(Captions.getString("App.nowPlaying", MP3Player.getInstance().getSong())); //$NON-NLS-1$
		} else {
			playerSongLabel.setText(Captions.getString("App.nothingIsPlayer")); //$NON-NLS-1$
		}
	}

	/**
	 * Enables or disables all controls.
	 * 
	 * @param enabled
	 */
	private void setControlsEnabled(boolean enabled) {
		titleTextField.setEnabled(enabled);
		artistTextField.setEnabled(enabled);
		albumTextField.setEnabled(enabled);
		yearTextField.setEnabled(enabled);
		changeCoverButton.setEnabled(enabled);
		deleteCoverButton.setEnabled(enabled);
		startPlayerButton.setEnabled(enabled);
		stopPlayerButton.setEnabled(enabled);
	}
}
