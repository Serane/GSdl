import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SongThreadPoolExecutor {
	private static final Logger LOGGER = Logger
			.getLogger(SongThreadPoolExecutor.class);
	int poolSize = 20;

	int maxPoolSize = 20;

	long keepAliveTime = 10000;

	ThreadPoolExecutor threadPool = null;

	final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(
			2000);

	public SongThreadPoolExecutor() {
		threadPool = new ThreadPoolExecutor(poolSize, maxPoolSize,
				keepAliveTime, TimeUnit.MINUTES, queue);
	}

	public Future<Song> downloadSong(Callable<Song> song) {
		LOGGER.trace("onBeforeSubmit : " + threadPool.getQueue().size());
		Future<Song> submitted =  threadPool.submit(song);
		LOGGER.trace("onPostSubmit : " + threadPool.getQueue().size());
		return submitted;

	}
	public List<Future<Song>> downloadSongs(List<Callable<Song>> songList) throws InterruptedException {
		List<Future<Song>> submitted =threadPool.invokeAll(songList);
		return submitted;
	}

	public void shutDown() {
		LOGGER.trace("Asking for Shutdown. Status :" + " \n \t QueueSize: "
				+ threadPool.getQueue().size() + "\n\t CompleteTasks: "
				+ threadPool.getCompletedTaskCount());
		threadPool.shutdown();
	}

}
