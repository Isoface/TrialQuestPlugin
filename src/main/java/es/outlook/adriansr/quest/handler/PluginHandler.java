package es.outlook.adriansr.quest.handler;

import es.outlook.adriansr.quest.main.QuestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AdrianSR / 14/12/2021 / 09:10 p. m.
 */
public abstract class PluginHandler implements Listener {
	
	protected static final Map < Class < ? extends PluginHandler >, PluginHandler > INSTANCES = new HashMap <> ( );
	
	public static < T extends PluginHandler > T getPluginHandler ( Class < T > clazz ) {
		return clazz.cast ( INSTANCES.get ( clazz ) );
	}
	
	// handling plugin instance
	protected final QuestPlugin plugin;
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	protected PluginHandler ( QuestPlugin plugin ) {
		if ( INSTANCES.containsKey ( getClass ( ) ) ) {
			throw new IllegalStateException ( "cannot create more than one instance of this handler!" );
		}
		
		this.plugin = plugin;
		PluginHandler.INSTANCES.put ( getClass ( ) , this );
	}
	
	/**
	 * Gets the plugin this handler handles.
	 * <p>
	 * @return the handling plugin.
	 */
	public QuestPlugin getPlugin ( ) {
		return plugin;
	}
	
	/**
	 * Registers events in this class.
	 */
	protected void register ( ) {
		HandlerList.unregisterAll ( this );
		Bukkit.getPluginManager ( ).registerEvents ( this , plugin );
	}
	
	/**
	 * Unregisters events in this class.
	 */
	protected void unregister ( ) {
		HandlerList.unregisterAll ( this );
	}
}
