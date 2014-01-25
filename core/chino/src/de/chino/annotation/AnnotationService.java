package de.chino.annotation;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.*; 

import de.chino.resources.ResourceService;
import de.chino.text.Text;

public interface AnnotationService {

	String annotate (Text text) ;
	
	AnnotationService INSTANCE = new AnnotationServiceImpl();
	
}
class AnnotationServiceImpl implements AnnotationService {
     
	static Template template;
	static {
		Velocity.setProperty(Velocity.RUNTIME_LOG_LOGSYSTEM_CLASS, "de.chino.annotation.VelocityLogger");
		Velocity.setProperty("resource.loader", "android");
		Velocity.setProperty("android.resource.loader.class", "de.chino.annotation.AndroidResourceLoader");
		Velocity.setProperty("android.content.res.Resources",ResourceService.INSTANCE.getResources());
		Velocity.setProperty("packageName", "com.cereslogic.myapplication");
		Velocity.init();
		template = Velocity.getTemplate("reader.vm");
	}
	
	@Override
	public String annotate(Text text) {
		VelocityContext context = new VelocityContext();
		context.put("text", text);
		StringWriter sw = new StringWriter();
		template.merge(context, sw);
		return sw.toString();
	}


}