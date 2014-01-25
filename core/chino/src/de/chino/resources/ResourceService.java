package de.chino.resources;

import java.io.InputStream;

public interface ResourceService {
	
	ResourceService INSTANCE = new ResourceServiceImpl();

	Resources getResources();

}

class ResourceServiceImpl implements ResourceService {

	@Override
	public Resources getResources() {

		return new Resources() {

			@Override
			public InputStream openResource(String templateName) {
				return ClassLoader.getSystemResourceAsStream("templates/".concat(templateName));
			}
			
			public boolean exists(String templateName) {
				return ClassLoader.getSystemResource("templates/".concat(templateName)) != null;
			}
		};
	}
	
}
