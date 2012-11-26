import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.scilor.grooveshark.API.Base.GroovesharkClient;
import com.scilor.grooveshark.API.Base.Utilities;

public final class GSInterface {
	private static GroovesharkClient client;
	private static final Logger LOGGER = Logger.getLogger(GSInterface.class);

	public GSInterface() {
		try {
			init();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static GroovesharkClient getInstance() {
		if (client == null) {
			try {
				init();
			} catch (Exception e) {
				LOGGER.fatal(e.getMessage());
			}
		}
		return client;
	}

	private static void exportClientToXml() throws IOException {
		String filename = new File(Utilities.GetAppPath(), "session.tmp")
				.getPath();

		FileOutputStream out = new FileOutputStream(filename);
		ObjectOutputStream obj = new ObjectOutputStream(out);

		obj.writeObject(client);
		obj.close();
		out.flush();
		out.close();
	}

	private static void loadClientFromXml() throws Exception {
		try {
			String filename = new File(Utilities.GetAppPath(), "session.tmp")
					.getPath();
			FileInputStream in = new FileInputStream(filename);
			ObjectInputStream obj = new ObjectInputStream(in);

			client = (GroovesharkClient) obj.readObject();
			obj.close();
		} catch (Exception ex) {
			client = new GroovesharkClient(true);
		}
	}

	private static void init() throws Exception {
		if (new File(Utilities.GetAppPath(), "session.tmp").exists()) {
			loadClientFromXml();
		} else {
			client = new GroovesharkClient(true);
		}
	}

	public static void refreshSession() throws Exception {
		if (!client.isConnected()) {
			client = new GroovesharkClient(true);
			exportClientToXml();
		}
	}
	public static void downloadList(List<Song> songList) {
		long startTime = System.currentTimeMillis();
		int success = 0;
		SongThreadPoolExecutor executor = new SongThreadPoolExecutor();
		List<Callable<Song>> sCallable = new ArrayList<Callable<Song>>(songList);
		try {
			List<Future<Song>> results = executor.downloadSongs(sCallable);
			for (Future<Song> f : results) {
				if (f.isDone()) {
					success++;
					songList.remove(f.get());
				}
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage());
		} catch (ExecutionException e) {
			LOGGER.error(e.getMessage());
		}
		// On demande l'arret de l'executor
		// (arret doux, donc il cesse ˆ la fin de toutes ses taches soumises).
		executor.shutDown();
		LOGGER.info("Download time : "
				+ TimeUnit.MINUTES.convert(
						(System.currentTimeMillis() - startTime),
						TimeUnit.MILLISECONDS) + "minute(s)");
		LOGGER.debug("Failed downloads: " + songList);
		LOGGER.debug("Nb OK: " + success);
		LOGGER.debug("Nb KO: " + songList.size());

	}
}
