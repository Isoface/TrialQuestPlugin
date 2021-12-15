package es.outlook.adriansr.quest.enums;

import es.outlook.adriansr.quest.handler.PluginHandler;
import es.outlook.adriansr.quest.quest.QuestHandler;
import es.outlook.adriansr.quest.quest.instance.QuestInstanceHandler;
import es.outlook.adriansr.quest.quest.type.QuestTypeHandler;

import java.util.concurrent.Callable;

/**
 * @author AdrianSR / 14/12/2021 / 09:57 p. m.
 */
public enum EnumPluginHandler {
	
	// order is vital
	
	QUEST_TYPE_HANDLER ( QuestTypeHandler.class ),
	QUEST_INSTANCE_HANDLER ( QuestInstanceHandler.class ),
	QUEST_HANDLER ( QuestHandler.class ),
	
	;
	
	private final Class < ? extends PluginHandler > clazz;
	private final Callable < Boolean >              init_flag;
	
	EnumPluginHandler ( Class < ? extends PluginHandler > clazz , Callable < Boolean > init_flag ) {
		this.clazz     = clazz;
		this.init_flag = init_flag;
	}
	
	EnumPluginHandler ( Class < ? extends PluginHandler > clazz ) {
		this ( clazz , ( ) -> Boolean.TRUE /* no special checks are required */ );
	}
	
	public Class < ? extends PluginHandler > getHandlerClass ( ) {
		return clazz;
	}
	
	public boolean canInitialize ( ) throws Exception {
		return init_flag.call ( );
	}
}