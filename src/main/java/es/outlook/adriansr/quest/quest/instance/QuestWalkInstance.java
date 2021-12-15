package es.outlook.adriansr.quest.quest.instance;

import es.outlook.adriansr.quest.quest.Quest;
import es.outlook.adriansr.quest.quest.type.QuestType;
import es.outlook.adriansr.quest.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

/**
 * @author AdrianSR / 14/12/2021 / 10:11 p. m.
 */
public class QuestWalkInstance extends QuestInstance {
	
	protected Location starting_point;
	
	public QuestWalkInstance ( Player player , Quest configuration ) {
		super ( player , configuration , QuestType.WALK );
	}
	
	@Override
	protected void start ( ) {
		super.start ( );
		register ( );
		
		// starting point
		this.starting_point = player.getLocation ( );
	}
	
	@Override
	protected void finish ( ) {
		super.finish ( );
		unregister ( );
	}
	
	@Override
	protected void cancel ( ) {
		super.cancel ( );
		unregister ( );
	}
	
	@EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
	public void onWalk ( PlayerMoveEvent event ) {
		Player   player   = event.getPlayer ( );
		Location location = event.getTo ( );
		
		if ( !isFinished ( ) && Objects.equals ( player.getUniqueId ( ) , this.player.getUniqueId ( ) )
				&& location != null /* location can be null in some spigot forks */ ) {
			double distance = location.distance ( starting_point );
			double left     = Math.max ( configuration.getAmount ( ) - distance , 0.0D );
			
			if ( distance >= configuration.getAmount ( ) ) {
				finish ( );
			} else {
				String bar_format = configuration.getBarFormat ( );
				
				if ( StringUtil.isNotBlank ( bar_format ) ) {
					player.spigot ( ).sendMessage (
							ChatMessageType.ACTION_BAR ,
							new TextComponent ( String.format ( bar_format , ( int ) left + 1 ) ) );
				}
			}
		}
	}
}