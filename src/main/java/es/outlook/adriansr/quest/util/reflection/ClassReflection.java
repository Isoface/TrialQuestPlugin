package es.outlook.adriansr.quest.util.reflection;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author AdrianSR / 15/12/2021 / 06:20 a. m.
 */
public class ClassReflection {
	
	public static boolean isPrimitiveWrapper ( Object input ) {
		return input instanceof Integer || input instanceof Boolean || input instanceof Character
				|| input instanceof Byte || input instanceof Short || input instanceof Double || input instanceof Long
				|| input instanceof Float;
	}
	
	public static boolean compatibleTypes ( Class < ? > class_a , Class < ? > class_b ) {
		if ( primitiveTypeCheck ( class_a , class_b ) ) {
			return true;
		} else {
			return ( isNumericType ( class_a ) && isNumericType ( class_b ) )
					|| ( class_a.isAssignableFrom ( class_b ) || class_b.isAssignableFrom ( class_a ) );
		}
	}
	
	public static boolean compatibleTypes ( Class < ? > clazz , Object to_check ) {
		if ( primitiveTypeCheck ( clazz , ( to_check != null ? to_check.getClass ( ) : null ) ) ) {
			return true;
		} else {
			return to_check != null && ( ( isNumericType ( clazz ) && to_check instanceof Number )
					|| clazz.isAssignableFrom ( to_check.getClass ( ) ) );
		}
	}
	
	protected static boolean primitiveTypeCheck ( Class < ? > class_a , Class < ? > class_b ) {
		DataType type_a = DataType.fromClass ( class_a );
		DataType type_b = DataType.fromClass ( class_b );
		
		return type_a != null && type_b != null && Objects.equals ( type_a , type_b );
	}
	
	public static boolean isNumericType ( Class < ? > clazz ) {
		if ( Number.class.isAssignableFrom ( clazz ) ) {
			return true;
		} else {
			DataType type = DataType.fromClass ( clazz );
			
			if ( type != null ) {
				switch ( type ) {
					case BYTE:
					case DOUBLE:
					case FLOAT:
					case INTEGER:
					case LONG:
					case SHORT:
						return true;
					
					default:
						break;
				}
			}
		}
		return false;
	}
	
	/**
	 * Scans the names of all the classes within a package contained by the provided
	 * <strong>{@code .jar}</strong>.
	 * <p>
	 *
	 * @param jarFile     the file that represents the .jar
	 * @param packageName the name of the desired package that contains the classes to get, or null to get all the
	 *                    classes contained by the .jar
	 *
	 * @return a set with the name of the classes.
	 */
	public static Set < String > getClassNames ( File jarFile , String packageName ) {
		Set < String > names = new HashSet <> ( );
		
		try ( JarFile file = new JarFile ( jarFile ) ) {
			for ( Enumeration < JarEntry > entry = file.entries ( ) ; entry.hasMoreElements ( ) ; ) {
				JarEntry jarEntry = entry.nextElement ( );
				String   name     = jarEntry.getName ( ).replace ( "/" , "." );
				if ( ( packageName == null || packageName.trim ( ).isEmpty ( ) || name.startsWith (
						packageName.trim ( ) ) )
						&& name.endsWith ( ".class" ) ) {
					names.add ( name.substring ( 0 , name.lastIndexOf ( ".class" ) ) );
				}
			}
		} catch ( Exception e ) {
			e.printStackTrace ( );
		}
		return names;
	}
	
	/**
	 * Scans all the classes within a package contained by the provided
	 * <strong>{@code .jar}</strong>.
	 * <p>
	 *
	 * @param jarFile     the file that represents the .jar
	 * @param packageName the name of the desired package that contains the classes to get, or null to get all the
	 *                    classes contained by the .jar
	 *
	 * @return a set with the scanned classes.
	 */
	public static Set < Class < ? > > getClasses ( File jarFile , String packageName ) {
		Set < Class < ? > > classes = new HashSet <> ( );
		
		getClassNames ( jarFile , packageName ).forEach ( class_name -> {
			try {
				classes.add ( Class.forName ( class_name ) );
			} catch ( ClassNotFoundException e ) {
				e.printStackTrace ( );
			}
		} );
		return classes;
	}
}
