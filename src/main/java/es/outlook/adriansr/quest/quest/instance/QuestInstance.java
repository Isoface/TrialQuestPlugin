package es.outlook.adriansr.quest.quest.instance;

import es.outlook.adriansr.quest.main.QuestPlugin;
import es.outlook.adriansr.quest.quest.Quest;
import es.outlook.adriansr.quest.quest.type.QuestType;
import es.outlook.adriansr.quest.util.Constants;
import es.outlook.adriansr.quest.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author AdrianSR / 14/12/2021 / 10:12 p. m.
 */
public abstract class QuestInstance implements Listener {
	
	protected final Player player;
	protected final Quest  configuration;
	
	// state
	protected boolean started;
	protected boolean finished;
	protected boolean cancelled;
	
	protected QuestInstance ( Player player , Quest configuration , QuestType... supported_types ) {
		this.player        = Objects.requireNonNull ( player , "player cannot be null" );
		this.configuration = Objects.requireNonNull ( configuration , "configuration cannot be null" );
		
		Validate.isTrue ( configuration.isValid ( ) , "configuration must be valid" );
		Validate.notNull ( supported_types , "supported types cannot be null" );
		Validate.notEmpty ( supported_types , "supported types cannot be empty" );
		Validate.notNull ( configuration.getType ( ) , "configuration returned a null type" );
		
		// checking quest type is supported
		boolean supported = false;
		
		for ( QuestType supported_type : supported_types ) {
			if ( supported_type == configuration.getType ( ) ) {
				supported = true;
				break;
			}
		}
		
		if ( !supported ) {
			throw new IllegalArgumentException (
					"quest type '" + configuration.getType ( ).getClass ( ).getSimpleName ( )
							+ " not supported. supported types: [" + Arrays.stream ( supported_types )
							.map ( QuestType :: name )
							.collect ( Collectors.joining ( ", " ) ) + "]" );
		}
	}
	
	public Player getPlayer ( ) {
		return player;
	}
	
	public Quest getConfiguration ( ) {
		return configuration;
	}
	
	public boolean isStarted ( ) {
		return started;
	}
	
	public boolean isFinished ( ) {
		return finished;
	}
	
	public boolean isCancelled ( ) {
		return cancelled;
	}
	
	protected void start ( ) {
		Validate.isTrue ( !started , "already started" );
		Validate.isTrue ( !finished , "already finished" );
		Validate.isTrue ( !cancelled , "already cancelled" );
		
		this.started = true;
		
		// start message
		String start_message = configuration.getStartMessage ( );
		
		if ( StringUtil.isNotBlank ( start_message ) ) {
			player.sendMessage ( start_message );
		}
	}
	
	protected void finish ( ) {
		Validate.isTrue ( started , "never started" );
		Validate.isTrue ( !finished , "already finished" );
		Validate.isTrue ( !cancelled , "already cancelled" );
		
		this.finished = true;
		
		// executing command
		if ( player.isOnline ( ) ) {
			Bukkit.dispatchCommand ( Bukkit.getConsoleSender ( ) , configuration.getCommand ( )
					.replace ( Constants.QUEST_COMMAND_PLAYER_PLACEHOLDER , player.getName ( ) ) );
		}
		
		// finish message
		String finish_message = configuration.getFinishMessage ( );
		
		if ( StringUtil.isNotBlank ( finish_message ) ) {
			player.sendMessage ( finish_message );
		}
		
		// clearing action bar. in case the bar format
		// is not blank, we will clear the action bar as is
		// might be still displaying the bar of the quest.
		if ( StringUtil.isNotBlank ( configuration.getBarFormat ( ) ) ) {
			player.spigot ( ).sendMessage (
					ChatMessageType.ACTION_BAR , new TextComponent ( " " ) );
		}
	}
	
	protected void cancel ( ) {
		Validate.isTrue ( started , "never started" );
		
		if ( !finished ) {
			this.cancelled = true;
		}
	}
	
	// ------- utils
	
	protected void register ( ) {
		Bukkit.getPluginManager ( ).registerEvents ( this , QuestPlugin.getInstance ( ) );
	}
	
	protected void unregister ( ) {
		HandlerList.unregisterAll ( this );
	}
}
