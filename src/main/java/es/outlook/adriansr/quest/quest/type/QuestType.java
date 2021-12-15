package es.outlook.adriansr.quest.quest.type;

import es.outlook.adriansr.quest.quest.Quest;
import es.outlook.adriansr.quest.quest.instance.QuestBreakInstance;
import es.outlook.adriansr.quest.quest.instance.QuestInstance;
import es.outlook.adriansr.quest.quest.instance.QuestWalkInstance;
import es.outlook.adriansr.quest.util.Constants;
import es.outlook.adriansr.quest.util.YamlUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author AdrianSR / 14/12/2021 / 09:02 p. m.
 */
public enum QuestType {
	
	WALK {
		@Override
		public QuestInstance createInstance ( Player player , Quest configuration ) {
			return new QuestWalkInstance ( player , configuration );
		}
	},
	
	BREAK {
		@Override
		public QuestInstance createInstance ( Player player , Quest configuration ) {
			return new QuestBreakInstance ( player , configuration );
		}
	},
	;
	
	private String display_name;
	
	QuestType ( ) {
		this.display_name = StringUtils.capitalize ( name ( ).toLowerCase ( ) );
	}
	
	public abstract QuestInstance createInstance ( Player player , Quest configuration );
	
	public String getDisplayName ( ) {
		return display_name;
	}
	
	public boolean isValid ( ) {
		return StringUtils.isNotBlank ( display_name );
	}
	
	public void load ( ConfigurationSection section ) {
		this.display_name = section.getString ( Constants.DISPLAY_NAME_KEY );
	}
	
	public int save ( ConfigurationSection section ) {
		int save = 0;
		
		if ( StringUtils.isNotBlank ( display_name ) ) {
			save += YamlUtil.setNotEqual ( section , Constants.DISPLAY_NAME_KEY , display_name ) ? 1 : 0;
		}
		
		return save;
	}
}
