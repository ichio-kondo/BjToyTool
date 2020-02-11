package application;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.Watchable;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class LogWatch implements Runnable {

	private WatchService watcher;
	public String watchPath;
	@FXML
	public TextArea message;
	@FXML
	public TextField nowdateDir;

	public LogFileOpen logWrite;

	public void run() {

		logWrite = new LogFileOpen(this.message, this.nowdateDir, this.watchPath);
		//WatchService watcher;
		try {
			System.out.println("WatchStart");
			watcher = FileSystems.getDefault().newWatchService();

			Watchable path = Paths.get(this.watchPath);
			path.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		while (true) {
			WatchKey watchKey;
			try {
				watchKey = watcher.take();
			} catch (InterruptedException e) {
				System.err.println(e.getMessage());
				return;
			}

			for (WatchEvent<?> event : watchKey.pollEvents()) {
				Kind<?> kind = event.kind();
				Object context = event.context();
				System.out.println("kind=" + kind + ", context=" + context);
				logWrite.loadLogFile(kind.toString(), context.toString());

			}
			if (!watchKey.reset()) {
				System.out.println("WatchKey が無効になりました");
				return;
			}
		}
	}

	public void close() {
		try {
			this.watcher.close();
			System.out.println("WatchEnd");
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}




