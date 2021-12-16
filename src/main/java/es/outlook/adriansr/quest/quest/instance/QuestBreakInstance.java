package es.outlook.adriansr.quest.quest.instance;

import es.outlook.adriansr.quest.data.DataStorage;
import es.outlook.adriansr.quest.data.DataStorageHandler;
import es.outlook.adriansr.quest.enums.EnumStat;
import es.outlook.adriansr.quest.quest.Quest;
import es.outlook.adriansr.quest.quest.type.QuestType;
import es.outlook.adriansr.quest.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Objects;

/**
 * @author AdrianSR / 14/12/2021 / 10:11 p. m.
 */
public class QuestBreakInstance extends QuestInstance {
	
	protected int count;
	
	public QuestBreakInstance ( Player player , Quest configuration ) {
		super ( player , configuration , QuestType.BREAK );
	}
	
	@Override
	protected void start ( ) {
		super.start ( );
		register ( );
	}
	
	@Override
	protected void finish ( ) {
		super.finish ( );
		unregister ( );
		
		// updating stats
		DataStorage database = DataStorageHandler.getInstance ( ).getDataStorage ( );
		
		if ( database != null ) {
			try {
				database.setStatValue (
						player , EnumStat.BROKEN_BLOCKS ,
						count + database.getStatValue (
								player.getUniqueId ( ) , EnumStat.BROKEN_BLOCKS ) );
			} catch ( Exception e ) {
				e.printStackTrace ( );
			}
		}
	}
	
	@Override
	protected void cancel ( ) {
		super.cancel ( );
		unregister ( );
	}
	
	@EventHandler ( priority = EventPriority.MONITOR, ignoreCancelled = true )
	public void onBreak ( BlockBreakEvent event ) {
		Player player = event.getPlayer ( );
		
		if ( !isFinished ( ) && Objects.equals ( player.getUniqueId ( ) , this.player.getUniqueId ( ) ) ) {
			// increasing
			count++;
			
			// checking is finished
			int left = Math.max ( configuration.getAmount ( ) - count , 0 );
			
			if ( count >= configuration.getAmount ( ) ) {
				finish ( );
			} else {
				String bar_format = configuration.getBarFormat ( );
				
				if ( StringUtil.isNotBlank ( bar_format ) ) {
					player.spigot ( ).sendMessage (
							ChatMessageType.ACTION_BAR ,
							new TextComponent ( String.format ( bar_format , left ) ) );
				}
			}
		}
	}
}