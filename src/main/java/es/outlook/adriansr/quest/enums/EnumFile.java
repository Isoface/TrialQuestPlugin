package es.outlook.adriansr.quest.enums;

import es.outlook.adriansr.quest.main.QuestPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @author AdrianSR / 14/12/2021 / 09:23 p. m.
 */
public enum EnumFile {
	
	QUEST_TYPE_CONFIGURATION_FILE ( "QuestTypeConfiguration.yml" ),
	QUEST_CONFIGURATION_FILE ( "QuestConfiguration.yml" ),
	
	;
	
	private final String name;
	
	private EnumFile ( String name ) {
		this.name = name;
	}
	
	public String getName ( ) {
		return name;
	}
	
	public File getFile ( ) {
		return new File ( QuestPlugin.getInstance ( ).getDataFolder ( ) , name );
	}
	
	/**
	 * Gets the file, and creates it if not exists.
	 *
	 * @return the file.
	 */
	public File safeGetFile ( ) {
		File file = getFile ( );
		
		if ( !file.getParentFile ( ).exists ( ) ) {
			file.getParentFile ( ).mkdirs ( );
		}
		
		if ( !file.exists ( ) ) {
			try {
				file.createNewFile ( );
			} catch ( IOException e ) {
				e.printStackTrace ( );
			}
		}
		return file;
	}
}
