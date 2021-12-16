package es.outlook.adriansr.quest.enums;

import es.outlook.adriansr.quest.util.configuration.ConfigurationEntry;
import es.outlook.adriansr.quest.util.reflection.ClassReflection;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author AdrianSR / 16/12/2021 / 06:03 p. m.
 */
public enum EnumMainConfiguration implements ConfigurationEntry {
	
	// database enable
	ENABLE_DATABASE ( "database.enable" , false ),
	
	// mongodb database
	DATABASE_MONGODB_URI ( "database.mongodb.uri" , "" ),
	DATABASE_MONGODB_HOST ( "database.mongodb.host" , "host name" ),
	DATABASE_MONGODB_PORT ( "database.mongodb.port" , 27017 ),
	DATABASE_MONGODB_DATABASE ( "database.mongodb.database" , "database name" ),
	
	;
	
	private final String      key;
	private final Object      default_value;
	private final Class < ? > type;
	private       Object      value;
	
	EnumMainConfiguration ( String key , Object default_value , Class < ? > type ) {
		this.key           = key.trim ( );
		this.default_value = default_value;
		this.value         = default_value;
		this.type          = type;
	}
	
	EnumMainConfiguration ( String key , Object default_value ) {
		this ( key , default_value , default_value.getClass ( ) );
	}
	
	@Override
	public String getKey ( ) {
		return key;
	}
	
	@Override
	public Object getDefaultValue ( ) {
		return default_value;
	}
	
	@Override
	public Object getValue ( ) {
		return value;
	}
	
	@Override
	public Class < ? > getValueType ( ) {
		return type;
	}
	
	@Override
	public void load ( ConfigurationSection section ) {
		Object raw = section.get ( getKey ( ) );
		
		if ( raw != null && ClassReflection.compatibleTypes ( this.type , raw ) ) {
			this.value = raw;
		}
	}
}
