package main.java.acs.configuration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

import net.sourceforge.yamlbeans.YamlException;
import net.sourceforge.yamlbeans.YamlReader;

public class YAML {
			
	@SuppressWarnings("rawtypes")
	public static void setParamsFromFile(String filePath) {
		try {
			YamlReader reader = new YamlReader(new FileReader(filePath));
			Object object = reader.read();
			System.out.println(object);
			Map map = (Map)object;
			System.out.println(map.get("suricata"));
		} catch (FileNotFoundException | YamlException e) {
			e.printStackTrace();
		}
	}
}
