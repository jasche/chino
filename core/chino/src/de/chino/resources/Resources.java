package de.chino.resources;

import java.io.InputStream;

public interface Resources {

	InputStream openResource(String templateName);
	boolean exists(String templateName);
}
