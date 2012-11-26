import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.scilor.grooveshark.API.Functions.SearchArtist.SearchArtistResult;

public class UserInterface extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7908899439554784938L;

	private static final Logger LOGGER = Logger.getLogger(Main.class);

	public UserInterface() {
	}

	public void initComponents() {
		final JTextField searchField = new JTextField("Your search", 20);
		final JButton searchButton = new JButton("Search");
		final JButton downloadButton = new JButton("Download");
		final JList resultList = new JList();

		final JMenuBar menuBar = new JMenuBar();
		final JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
		final JMenuItem changePropItem = new JMenuItem("Properties");
		changePropItem.addActionListener(new ChangePropertiesActionListener(this));	
		fileMenu.add(changePropItem);
		this.setJMenuBar(menuBar);

		this.setTitle("GroovesharkDownloader UI");
		this.setSize(400, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		JPanel searchPanel = new JPanel();
		searchPanel.add(searchField);

		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final String search = searchField.getText();
				LOGGER.debug(search);
				try {
					SearchArtistResult[] result = GSInterface.getInstance()
							.SearchArtist(search).result.result;
					List<Song> songs = new ArrayList<Song>();
					for (SearchArtistResult song : result) {
						LOGGER.debug(song.Name);
						songs.add(new Song(song.SongName, song.ArtistName,
								song.SongID, song.AlbumName));
					}
					resultList.setListData(songs.toArray());
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			}
		});
		searchPanel.add(searchButton, BorderLayout.EAST);
		this.add(searchPanel, BorderLayout.NORTH);
		downloadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Object[] dlList = resultList.getSelectedValues();
				LOGGER.debug("Preparing to Download: ");
				List<Song> songList = new ArrayList<Song>();
				for (Object song : dlList) {
					LOGGER.debug((Song) song);
					songList.add((Song) song);
				}
				GSInterface.downloadList(songList);

			}
		});
		this.add(downloadButton, BorderLayout.SOUTH);

		resultList.setAutoscrolls(true);
		resultList.setLayoutOrientation(JList.VERTICAL);
		JScrollPane scrollPane = new JScrollPane(resultList);
		this.add(scrollPane, BorderLayout.CENTER);

		this.setVisible(true);
	}

}
