package es.outlook.adriansr.quest.main;

import es.outlook.adriansr.quest.data.DataStorage;
import es.outlook.adriansr.quest.data.DataStorageHandler;
import es.outlook.adriansr.quest.enums.EnumPluginHandler;
import es.outlook.adriansr.quest.quest.Quest;
import es.outlook.adriansr.quest.quest.QuestHandler;
import es.outlook.adriansr.quest.quest.instance.QuestInstanceHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author AdrianSR / 14/12/2021 / 10:03 p. m.
 */
public final class QuestPlugin extends JavaPlugin {
	
	public static QuestPlugin getInstance ( ) {
		return JavaPlugin.getPlugin ( QuestPlugin.class );
	}
	
	@Override
	public void onEnable ( ) {
		// initializing handlers
		for ( EnumPluginHandler handler : EnumPluginHandler.values ( ) ) {
			try {
				if ( handler.canInitialize ( ) ) {
					handler.getHandlerClass ( ).getConstructor ( QuestPlugin.class )
							.newInstance ( this );
				}
			} catch ( Exception e ) {
				e.printStackTrace ( );
			}
		}
		
		// test quest command
		Objects.requireNonNull ( getCommand ( "quest" ) ).setExecutor ( this );
		Objects.requireNonNull ( getCommand ( "quest" ) ).setTabCompleter ( this );
	}
	
	@Override
	public void onDisable ( ) {
		// disposing database connection
		DataStorage connection = DataStorageHandler.getInstance ( ).getDataStorage ( );
		
		if ( connection != null ) {
			connection.dispose ( );
		}
	}
	
	@Override
	public @NotNull File getFile ( ) {
		return super.getFile ( );
	}
	
	@Override
	public boolean onCommand ( @NotNull CommandSender sender , @NotNull Command command ,
			@NotNull String label , @NotNull String[] args ) {
		boolean send_help = false;
		
		if ( args.length > 1 ) {
			Player player = Bukkit.getPlayerExact ( args[ 0 ] );
			Quest  quest  = QuestHandler.getInstance ( ).getQuest ( args[ 1 ] );
			
			if ( player != null && quest != null ) {
				// then starting the quest
				QuestInstanceHandler.getInstance ( ).startQuest (
						quest.getType ( ).createInstance ( player , quest ) );
			} else {
				if ( player == null ) {
					sender.sendMessage ( ChatColor.RED + "Unknown player '" + args[ 0 ] + "'!" );
				}
				
				if ( quest == null ) {
					sender.sendMessage ( ChatColor.RED + "Unknown quest '" + args[ 1 ] + "'!" );
				}
			}
		} else {
			send_help = true;
		}
		
		if ( send_help ) {
			sender.sendMessage ( ChatColor.RED + "Usage: " + command.getUsage ( ) );
		}
		
		return true;
	}
	
	@Override
	public @Nullable List < String > onTabComplete ( @NotNull CommandSender sender , @NotNull Command command ,
			@NotNull String alias , @NotNull String[] args ) {
		if ( args.length > 1 ) {
			List < String > result = new ArrayList <> ( );
			
			QuestHandler.getInstance ( ).getQuests ( )
					.stream ( ).map ( Quest :: getName ).forEach ( result :: add );
			
			return result;
		} else {
			return super.onTabComplete ( sender , command , alias , args );
		}
	}
}