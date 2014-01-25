package de.chino.annotation;
import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import de.chino.resources.Resources;
 
 
public class AndroidResourceLoader extends FileResourceLoader {
	private Resources resources;
	private String packageName;
 
	public void commonInit(RuntimeServices rs, ExtendedProperties configuration) {
		super.commonInit(rs,configuration);
		this.resources = (Resources)rs.getProperty("android.content.res.Resources");
		this.packageName = (String)rs.getProperty("packageName");
	}
 
	public long getLastModified(Resource resource) {
		return 0;
	}
 
	public InputStream getResourceStream(String templateName) {
		return resources.openResource(templateName);
	}
 
	public boolean	isSourceModified(Resource resource) {
		return false;
	}
 
	public boolean	resourceExists(String templateName) {
		return resources.exists(templateName);
	}
}