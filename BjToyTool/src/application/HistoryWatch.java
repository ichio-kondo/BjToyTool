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

/**
 * 実行履歴フォルダを監視するクラス。実行履歴一覧オープン時に別スレッドとして実行されます。
 * フォルダにアクションがあった場合は、TableView（HistoryTable）のリフレッシュを行います。
 * @author kreis
 *
 */
public class HistoryWatch  implements Runnable{

	private WatchService watcher;
	public String watchPath = "C:\\TK\\EZ_業務\\EzAvater実行履歴";
	public HistoryController controller;

	@Override
	public void run() {
		try {
			System.out.println("HistoryWatchStart");
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
				controller.refreshOpen();
			}
			if (!watchKey.reset()) {
				System.out.println("WatchKey が無効になりました");
				return;
			}
		}

	}

}
