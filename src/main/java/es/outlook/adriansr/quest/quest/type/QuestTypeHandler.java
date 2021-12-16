package es.outlook.adriansr.quest.quest.type;

import es.outlook.adriansr.quest.enums.EnumFile;
import es.outlook.adriansr.quest.handler.PluginHandler;
import es.outlook.adriansr.quest.main.QuestPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author AdrianSR / 14/12/2021 / 09:16 p. m.
 */
public final class QuestTypeHandler extends PluginHandler {
	
	/**
	 * Constructs the plugin handler.
	 *
	 * @param plugin the plugin to handle.
	 */
	public QuestTypeHandler ( QuestPlugin plugin ) {
		super ( plugin );
		
		saveDefaults ( );
		loadConfiguration ( );
	}
	
	private void saveDefaults ( ) {
		File file = EnumFile.QUEST_TYPE_CONFIGURATION_FILE.getFile ( );
		
		if ( !file.exists ( ) ) {
			file.getParentFile ( ).mkdirs ( );
			
			try {
				file.createNewFile ( );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
			
			YamlConfiguration yaml = YamlConfiguration.loadConfiguration ( file );
			
			for ( QuestType type : QuestType.values ( ) ) {
				type.save ( yaml.createSection ( type.name ( ) ) );
			}
			
			try {
				yaml.save ( file );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
		}
	}
	
	private void loadConfiguration ( ) {
		File              file = EnumFile.QUEST_TYPE_CONFIGURATION_FILE.getFile ( );
		YamlConfiguration yaml = file.exists ( ) ? YamlConfiguration.loadConfiguration ( file ) : null;
		
		if ( yaml != null ) {
			for ( QuestType type : QuestType.values ( ) ) {
				ConfigurationSection section = yaml.getConfigurationSection ( type.name ( ) );
				
				if ( section != null ) {
					type.load ( section );
				}
			}
		}
	}
}