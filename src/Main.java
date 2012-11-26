import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {

	private static final Logger LOGGER = Logger.getLogger(Main.class);
	public static Properties prop;

	public static void main(String[] args) {
		BasicConfigurator.configure();
		LOGGER.debug("Application start");
		LOGGER.getParent().setLevel(Level.TRACE);
		prop = new Properties();
		try {
			// Demarrage de l'IHM
			LOGGER.debug("Loading props");
			prop.load(new FileInputStream("cfg/GSdl.properties"));
			LOGGER.debug("props loaded : " + prop);
			UserInterface userInterface = new UserInterface();
			userInterface.initComponents();
			// Tests
			// Playlist Funk 57499563
			// Playlist "Latino" 52085516

			// SearchArtistResult[] playlist = GSInterface.getInstance()
			// .GetPlaylistSongs(52085516).result.Songs;
			// List<Song> salsaPlaylistSongs = new ArrayList<Song>();
			// int i = 0;
			// for (SearchArtistResult result : playlist) {
			// Song theSong = new Song(result.SongName, result.ArtistName,
			// result.SongID, result.AlbumName);
			// if (i < 0) {
			// i++;
			// LOGGER.debug("Skipping " + theSong);
			//
			// } else {
			// LOGGER.debug("Adding to queue: " + theSong);
			// salsaPlaylistSongs.add(theSong);
			// }
			// } // Telecharge l'ensemble de la playlist salsa
			// downloadList(salsaPlaylistSongs);

		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		}

	}
}
