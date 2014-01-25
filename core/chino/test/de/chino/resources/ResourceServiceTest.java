package de.chino.resources;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.After;
import org.junit.Test;
import org.junit.AfterClass;

public class ResourceServiceTest {

	InputStream inputStream;
	
	@After
	public void closeResources() throws Exception {
		if(inputStream != null) {
			inputStream.close();
		}
	}
	
	@Test
	public void testGetResources() {
		assertNotNull(ResourceService.INSTANCE.getResources());
	}

	@Test
	public void testOpenResource() {
		inputStream = ResourceService.INSTANCE.getResources().openResource("reader.vm");
		assertNotNull(inputStream);
	}
	

	@Test
	public void testExists() {
		assertTrue(ResourceService.INSTANCE.getResources().exists("reader.vm"));
	}

}
