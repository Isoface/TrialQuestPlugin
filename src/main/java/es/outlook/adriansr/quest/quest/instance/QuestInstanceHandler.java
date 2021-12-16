package es.outlook.adriansr.quest.quest.instance;

import es.outlook.adriansr.quest.handler.PluginHandler;
import es.outlook.adriansr.quest.main.QuestPlugin;
import org.apache.commons.lang.Validate;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author AdrianSR / 14/12/2021 / 10:17 p. m.
 */
public final class QuestInstanceHandler extends PluginHandler {
	
	public static QuestInstanceHandler getInstance ( ) {
		return getPluginHandler ( QuestInstanceHandler.class );
	}
	
	private final Map < UUID, QuestInstance > quest_map = new HashMap <> ( );
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public QuestInstanceHandler ( QuestPlugin plugin ) {
		super ( plugin );
	}
	
	public QuestInstance getQuest ( Player player ) {
		return quest_map.get ( player.getUniqueId ( ) );
	}
	
	public void startQuest ( QuestInstance instance ) {
		Player player = instance.getPlayer ( );
		
		Validate.notNull ( player , "quest returned a null player" );
		Validate.isTrue ( !instance.isStarted ( ) , "quest already started" );
		
		// cancelling current quest
		cancelQuest ( player );
		
		// then starting and registering
		// the new one.
		instance.start ( );
		quest_map.put ( player.getUniqueId ( ) , instance );
	}
	
	public void finishQuest ( QuestInstance instance ) {
		Player player = Objects.requireNonNull (
				instance.getPlayer ( ) , "quest returned a null player" );
		
		// unregistering
		quest_map.remove ( player.getUniqueId ( ) );
		
		// then finishing
		if ( instance.isStarted ( ) && !instance.isFinished ( ) ) {
			instance.finish ( );
		}
	}
	
	public void finishQuest ( Player player ) {
		QuestInstance instance = quest_map.get ( player.getUniqueId ( ) );
		
		if ( instance != null ) {
			finishQuest ( instance );
		}
	}
	
	public void cancelQuest ( QuestInstance instance ) {
		Player player = Objects.requireNonNull (
				instance.getPlayer ( ) , "quest returned a null player" );
		
		// unregistering
		quest_map.remove ( player.getUniqueId ( ) );
		
		// then cancelling
		if ( instance.isStarted ( ) && !instance.isCancelled ( ) ) {
			instance.cancel ( );
		}
	}
	
	public void cancelQuest ( Player player ) {
		QuestInstance instance = quest_map.get ( player.getUniqueId ( ) );
		
		if ( instance != null ) {
			cancelQuest ( instance );
		}
	}
}