// A very basic mock of an asynchronous call, 
// using Java's Future.

import java.net.*;
import java.io.*;
import java.util.concurrent.*;

/**
 * Demo class - makes an asynchronous call.
 */
public class Demo {
	private static ExecutorService pool;
	
	/**
	 *	starts program execution.
     *	@param args - command-line arguments.
	 *	@throws Exception - if 'InterruptedException' is thrown while invoking
	 *					   Future.get().
	 */
	public static void main(String[] args) throws Exception {
		if (args.length > 0) {
			try {
				Future<String> content = getContent(args[0]);
                
				while(!content.isDone()) {
					//TODO: do something else as content is being retrieved.
				}
                
                String html = content.get(); // result of asynchronous call.
                
                if (!html.isEmpty()) {
                    System.out.println("\n>HTML content:\n\n" + html + "\n");
                } else {
                    System.out.println("\nIt appears that the " 
                                       + "retrieved content is empty.\n");
                }
				pool.shutdown(); // kill thread-pool.
			}catch(IOException ex) {
				System.err.println("An exception occurred - "
                                   + ex.getMessage());
			}
		} else {
			System.err.println("\nUsage:\n\n> java Demo http://google.com\n");
			System.exit(1);
		}
	}
	
	/**
	 * retrieves 'html' content from a given url.
	 * @param url - the site to retrieve content from.
	 * @return a Future.
	 */
	private static Future<String> getContent(String url) throws IOException {
		pool = Executors.newSingleThreadExecutor();
		
		return pool.submit( new Callable<String>() {
			@Override
			public String call() throws Exception {
				StringBuilder content = new StringBuilder();
				URLConnection connection  = new URL(url).openConnection();
				// obtain input stream read from the open connection
				BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
				
				String line;
				while ((line = br.readLine()) != null) {
                    content.append(line + "\n");
				}
				br.close();
				return content.toString();
			}
		});
	}
}

