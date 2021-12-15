package es.outlook.adriansr.quest.util.reflection;

import java.io.File;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author AdrianSR / 15/12/2021 / 06:20 a. m.
 */
public class ClassReflection {
	
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
