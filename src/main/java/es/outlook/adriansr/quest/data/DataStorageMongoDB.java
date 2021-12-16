package es.outlook.adriansr.quest.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import es.outlook.adriansr.quest.enums.EnumMainConfiguration;
import es.outlook.adriansr.quest.enums.EnumStat;
import es.outlook.adriansr.quest.main.QuestPlugin;
import es.outlook.adriansr.quest.util.StringUtil;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.*;

/**
 * @author AdrianSR / 16/12/2021 / 06:21 p. m.
 */
public class DataStorageMongoDB implements DataStorage {
	
	protected static final String COLLECTION_NAME = "quest_players";
	protected static final String ID_FIELD_NAME   = "_id";
	protected static final String NAME_FIELD_NAME = "name";
	
	protected MongoClient   connection;
	protected MongoDatabase database;
	
	public DataStorageMongoDB ( QuestPlugin plugin ) {
		// good to go
	}
	
	@Override
	public boolean setUp ( ) {
		String connection_uri = EnumMainConfiguration.DATABASE_MONGODB_URI.getAsString ( );
		String database_name  = EnumMainConfiguration.DATABASE_MONGODB_DATABASE.getAsString ( );
		
		if ( StringUtil.isNotBlank ( database_name ) ) {
			if ( StringUtil.isNotBlank ( connection_uri ) ) {
				this.connection = MongoClients.create (
						MongoClientSettings.builder ( )
								.applyConnectionString ( new ConnectionString ( connection_uri ) )
								.uuidRepresentation ( UuidRepresentation.STANDARD )
								.build ( ) );
			} else {
				String host = EnumMainConfiguration.DATABASE_MONGODB_HOST.getAsString ( );
				int    port = EnumMainConfiguration.DATABASE_MONGODB_PORT.getAsInteger ( );
				
				if ( StringUtil.isNotBlank ( host ) ) {
					this.connection = MongoClients.create (
							MongoClientSettings.builder ( )
									.applyToClusterSettings ( builder -> builder.hosts (
											Arrays.asList ( new ServerAddress ( host , port ) ) ) )
									.uuidRepresentation ( UuidRepresentation.STANDARD )
									.build ( ) );
				}
			}
			
			if ( connection != null ) {
				this.database = connection.getDatabase ( database_name );
				return true;
			} else {
				return false;
			}
		} else {
			Bukkit.getConsoleSender ( ).sendMessage (
					ChatColor.RED + "[Quest] Invalid MongoDB database name!" );
			return false;
		}
	}
	
	// stats
	
	@Override
	public Map < UUID, Map < EnumStat, Integer > > getStoredStatValues ( ) {
		Map < UUID, Map < EnumStat, Integer > > result = new HashMap <> ( );
		
		for ( Document document : getCollection ( ).find ( ) ) {
			UUID uuid;
			
			// extracting uuid
			if ( document.containsKey ( ID_FIELD_NAME ) ) {
				uuid = document.get ( ID_FIELD_NAME , UUID.class );
			} else {
				continue;
			}
			
			// extracting stat values
			result.put ( uuid , extractStatValues ( document ) );
		}
		return result;
	}
	
	@Override
	public Map < EnumStat, Integer > getStatValues ( UUID uuid ) {
		Document result = getCollection ( ).find ( Filters.eq ( ID_FIELD_NAME , uuid ) ).first ( );
		
		return result != null ? extractStatValues ( result ) : new EnumMap <> ( EnumStat.class );
	}
	
	@Override
	public int getStatValue ( UUID uuid , EnumStat stat_type ) {
		Document result = getCollection ( ).find ( Filters.eq ( ID_FIELD_NAME , uuid ) ).first ( );
		String   key    = stat_type.name ( ).toLowerCase ( );
		
		if ( result != null && result.containsKey ( key ) ) {
			return result.getInteger ( key );
		} else {
			return 0;
		}
	}
	
	@Override
	public void setStatValue ( UUID uuid , String name , EnumStat stat_type , int value ) {
		set0 ( getCollection ( ) , uuid , name , stat_type , value );
	}
	
	// -----
	
	protected Map < EnumStat, Integer > extractStatValues ( Document document ) {
		Map < EnumStat, Integer > values = new EnumMap <> ( EnumStat.class );
		
		for ( EnumStat stat_type : EnumStat.values ( ) ) {
			String key = stat_type.name ( ).toLowerCase ( );
			
			if ( document.containsKey ( key ) ) {
				values.put ( stat_type , document.getInteger ( key ) );
			}
		}
		return values;
	}
	
	protected void set0 ( MongoCollection < Document > collection , UUID uuid , String name ,
			Enum < ? > key , Object value ) {
		set0 ( collection , uuid , name , key.name ( ).toLowerCase ( ) , value );
	}
	
	protected void set0 ( MongoCollection < Document > collection , UUID uuid , String name ,
			String key , Object value ) {
		collection.updateOne (
				// filter
				new Document ( ID_FIELD_NAME , uuid ) ,
				// the update
				new Document ( "$set" , new Document ( )
						.append ( ID_FIELD_NAME , uuid )
						.append ( NAME_FIELD_NAME , name )
						.append ( key , value ) ) ,
				// upsert must be enabled
				new UpdateOptions ( ).upsert ( true ) );
	}
	
	protected MongoCollection < Document > getCollection ( ) {
		return database.getCollection ( COLLECTION_NAME );
	}
	
	// -----
	
	@Override
	public void dispose ( ) {
		if ( connection != null ) {
			connection.close ( );
		}
	}
}
