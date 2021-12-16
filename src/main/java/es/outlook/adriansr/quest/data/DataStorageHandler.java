package es.outlook.adriansr.quest.data;

import es.outlook.adriansr.quest.enums.EnumMainConfiguration;
import es.outlook.adriansr.quest.handler.PluginHandler;
import es.outlook.adriansr.quest.main.QuestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author AdrianSR / 16/12/2021 / 06:02 p. m.
 */
public final class DataStorageHandler extends PluginHandler {
	
	public static DataStorageHandler getInstance ( ) {
		return getPluginHandler ( DataStorageHandler.class );
	}
	
	private final DataStorage data_storage;
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public DataStorageHandler ( QuestPlugin plugin ) {
		super ( plugin );
		
		// connecting to database
		DataStorage data_storage = null;
		
		if ( EnumMainConfiguration.ENABLE_DATABASE.getAsBoolean ( ) ) {
			data_storage = new DataStorageMongoDB ( plugin );
		}
		
		if ( data_storage != null ) {
			Exception error        = null;
			boolean   successfully = false;
			
			try {
				if ( data_storage.setUp ( ) ) {
					successfully = true;
				}
			} catch ( Exception ex ) {
				error = ex;
			}
			
			if ( successfully ) {
				Bukkit.getConsoleSender ( ).sendMessage (
						ChatColor.GREEN + "[Quest] Connected to database" );
			} else {
				data_storage.dispose ( );
				data_storage = null;
				
				Bukkit.getConsoleSender ( ).sendMessage (
						ChatColor.RED + "[Quest] Couldn't connect to database!" );
				
				if ( error != null ) {
					error.printStackTrace ( );
				}
			}
		}
		
		this.data_storage = data_storage;
	}
	
	public DataStorage getDataStorage ( ) {
		return data_storage;
	}
}
