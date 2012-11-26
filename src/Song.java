import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.scilor.grooveshark.API.Base.GroovesharkAudioStream;

public class Song implements Callable<Song> {
	private static final Logger LOGGER = Logger.getLogger(Song.class);
	private final String title;
	private final String artist;
	private final int songId;
	private final String album;

	public Song(String name, String artist, int songId, String album) {
		this.title = name;
		this.artist = artist;
		this.songId = songId;
		this.album = album;
	}

	/**
	 * @return the name
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * @return the album name
	 */
	public String getAlbum() {
		return this.album;
	}

	/**
	 * @return the artist
	 */
	public String getArtist() {
		return this.artist;
	}

	/**
	 * @return the songId
	 */
	public int getSongId() {
		return this.songId;
	}

	/**
	 * @param filename
	 * @return
	 */
	private static String fixFilename(String filename) {
		String tmpString = "";

		for (int i = 0; i < filename.length(); i++) {
			try {
				new File(filename.charAt(i) + "").getCanonicalFile();
				if (filename.charAt(i) != '/' && filename.charAt(i) != '\\') {
					tmpString += filename.charAt(i);
				}
			} catch (Exception ex) {
			}
		}

		return tmpString;
	}

	/**
	 * @param song
	 */
	@Override
	public Song call() {
		long start = System.currentTimeMillis();
		LOGGER.debug("Downloading: " + this.toString());
		try {
			GroovesharkAudioStream stream;
			LOGGER.trace("Getting MusicStream");
			stream = GSInterface.getInstance().GetMusicStream(this.getSongId());

			String filename = Main.prop.getProperty("downloadfolder") + fixFilename(this.getArtist() + " - "
					+ this.getTitle() + " - " + this.getAlbum())
					+ ".mp3";

			FileOutputStream writer = new FileOutputStream(filename);
			int readBytes = 0;
			do {
				byte[] buffer = new byte[4096];
				readBytes = stream.Stream().read(buffer);
				if (readBytes > 0)
					writer.write(buffer, 0, readBytes);
			} while (readBytes > 0);

			LOGGER.debug("Download completed: " + this.toString() + " in "
					+ ((System.currentTimeMillis() - start) / 1000)
					+ " seconds");
			stream.MarkSongAsDownloaded();
			writer.flush();
			writer.close();
			stream.Stream().close();
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
		} finally {
			try {
				GSInterface.getInstance().GetMusicStream(this.getSongId())
						.MarkSongAsDownloaded();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		return this;
	}

	public String toString() {
		StringBuilder presentation = new StringBuilder();
		presentation.append(this.getArtist());
		presentation.append(" - ");
		presentation.append(this.getTitle());
		return presentation.toString();

	}

}
