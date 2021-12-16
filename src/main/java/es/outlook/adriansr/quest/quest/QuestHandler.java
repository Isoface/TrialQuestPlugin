package es.outlook.adriansr.quest.quest;

import es.outlook.adriansr.quest.enums.EnumFile;
import es.outlook.adriansr.quest.handler.PluginHandler;
import es.outlook.adriansr.quest.main.QuestPlugin;
import es.outlook.adriansr.quest.quest.type.QuestType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * @author AdrianSR / 15/12/2021 / 07:16 a. m.
 */
public final class QuestHandler extends PluginHandler {
	
	private static final Quest[] EXAMPLE_QUESTS = {
			// walk
			new Quest ( "example-walk-quest" , QuestType.WALK ,
						"give @player minecraft:stone 64" ,
						10 ,
						ChatColor.GOLD + "Walk 10 blocks from here" ,
						ChatColor.GOLD + "%d blocks left" ,
						ChatColor.GREEN + "Quest finished! Congratulations." ) ,
			// break
			new Quest ( "example-break-quest" , QuestType.BREAK ,
						"give @player minecraft:apple 1" ,
						5 ,
						ChatColor.GOLD + "Break 5 blocks" ,
						ChatColor.GOLD + "%d blocks left" ,
						ChatColor.GREEN + "Quest finished! Congratulations." )
	};
	
	public static QuestHandler getInstance ( ) {
		return getPluginHandler ( QuestHandler.class );
	}
	
	private final Set < Quest > quests = new HashSet <> ( );
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public QuestHandler ( QuestPlugin plugin ) {
		super ( plugin );
		
		saveDefaults ( );
		loadConfiguration ( );
	}
	
	public Quest getQuest ( String name ) {
		return quests.stream ( ).filter ( quest -> Objects.equals ( name , quest.getName ( ) ) )
				.findAny ( ).orElse ( null );
	}
	
	public Collection < Quest > getQuests ( ) {
		return Collections.unmodifiableCollection ( quests );
	}
	
	private void saveDefaults ( ) {
		File file = EnumFile.QUEST_CONFIGURATION_FILE.getFile ( );
		
		if ( !file.exists ( ) ) {
			file.getParentFile ( ).mkdirs ( );
			
			try {
				file.createNewFile ( );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
			
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
			
			for ( Quest example : EXAMPLE_QUESTS ) {
				example.save ( yaml.createSection ( example.getName ( ) ) );
			}
			
			try {
				yaml.save ( file );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
		}
	}
	
	private void loadConfiguration ( ) {
		File file = EnumFile.QUEST_CONFIGURATION_FILE.getFile ( );
		
		if ( file.exists ( ) ) {
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
			
			for ( String key : yaml.getKeys ( false ) ) {
				ConfigurationSection section = yaml.isConfigurationSection ( key )
						? yaml.getConfigurationSection ( key ) : null;
				
				if ( section != null ) {
					Quest quest = new Quest ( ).load ( section );
					
					if ( quest.isValid ( ) ) {
						quests.add ( quest );
					} else {
						Bukkit.getConsoleSender ( ).sendMessage (
								ChatColor.RED + "Quest '" + section.getName ( )
										+ "' has an invalid configuration!" );
					}
				}
			}
		}
	}
}
