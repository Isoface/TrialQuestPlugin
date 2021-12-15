package es.outlook.adriansr.quest.quest;

import es.outlook.adriansr.quest.quest.type.QuestType;
import es.outlook.adriansr.quest.util.Constants;
import es.outlook.adriansr.quest.util.StringUtil;
import es.outlook.adriansr.quest.util.YamlUtil;
import es.outlook.adriansr.quest.util.reflection.EnumReflection;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author AdrianSR / 15/12/2021 / 06:46 a. m.
 */
public class Quest {
	
	protected String    name;
	protected QuestType type;
	protected String    command;
	// amount data
	protected int       amount;
	// messages
	protected String    start_message;
	protected String    bar_format;
	protected String    finish_message;
	
	public Quest ( String name , QuestType type , String command , int amount ,
			String start_message , String bar_format , String finish_message ) {
		this.name           = name;
		this.type           = type;
		this.command        = command;
		this.amount         = amount;
		this.start_message  = start_message;
		this.bar_format     = bar_format;
		this.finish_message = finish_message;
	}
	
	public Quest ( ) {
		// to be loaded
	}
	
	public String getName ( ) {
		return name;
	}
	
	public QuestType getType ( ) {
		return type;
	}
	
	public String getCommand ( ) {
		return command;
	}
	
	public int getAmount ( ) {
		return amount;
	}
	
	public String getStartMessage ( ) {
		return start_message;
	}
	
	public String getBarFormat ( ) {
		return bar_format;
	}
	
	public String getFinishMessage ( ) {
		return finish_message;
	}
	
	public boolean isValid ( ) {
		return StringUtil.isNotBlank ( name ) && type != null && StringUtil.isNotBlank ( command );
	}
	
	public Quest load ( ConfigurationSection section ) {
		this.name           = section.getString ( Constants.NAME_KEY , section.getName ( ) );
		this.type           = EnumReflection.getEnumConstant ( QuestType.class , section.getString (
				Constants.TYPE_KEY , StringUtil.EMPTY ).toUpperCase ( ) );
		this.command        = section.getString ( Constants.COMMAND_KEY , StringUtil.EMPTY );
		this.amount         = section.getInt ( Constants.AMOUNT_KEY , 0 );
		this.start_message  = StringUtil.translateAlternateColorCodes (
				section.getString ( Constants.START_MESSAGE_KEY , StringUtil.EMPTY ) );
		this.bar_format     = StringUtil.translateAlternateColorCodes (
				section.getString ( Constants.BAR_FORMAT_KEY , StringUtil.EMPTY ) );
		this.finish_message = StringUtil.translateAlternateColorCodes (
				section.getString ( Constants.FINISH_MESSAGE_KEY , StringUtil.EMPTY ) );
		
		return this;
	}
	
	public int save ( ConfigurationSection section ) {
		int save = 0;
		
		if ( StringUtil.isNotBlank ( name ) ) {
			save += YamlUtil.setNotEqual ( section , Constants.NAME_KEY , name ) ? 1 : 0;
		}
		
		if ( type != null ) {
			save += YamlUtil.setNotEqual ( section , Constants.TYPE_KEY , type.name ( ) ) ? 1 : 0;
		}
		
		if ( StringUtil.isNotBlank ( command ) ) {
			save += YamlUtil.setNotEqual ( section , Constants.COMMAND_KEY , command ) ? 1 : 0;
		}
		
		if ( amount > 0 ) {
			save += YamlUtil.setNotEqual ( section , Constants.AMOUNT_KEY , amount ) ? 1 : 0;
		}
		
		if ( StringUtil.isNotBlank ( start_message ) ) {
			save += YamlUtil.setNotEqual (
					section , Constants.START_MESSAGE_KEY ,
					StringUtil.untranslateAlternateColorCodes ( start_message ) ) ? 1 : 0;
		}
		
		if ( StringUtil.isNotBlank ( bar_format ) ) {
			save += YamlUtil.setNotEqual (
					section , Constants.BAR_FORMAT_KEY ,
					StringUtil.untranslateAlternateColorCodes ( bar_format ) ) ? 1 : 0;
		}
		
		if ( StringUtil.isNotBlank ( finish_message ) ) {
			save += YamlUtil.setNotEqual (
					section , Constants.FINISH_MESSAGE_KEY ,
					StringUtil.untranslateAlternateColorCodes ( finish_message ) ) ? 1 : 0;
		}
		
		return save;
	}
}
