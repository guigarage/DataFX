/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.datafx.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.JAXB;
import junit.framework.TestCase;
import org.datafx.reader.FileSource;
import org.datafx.reader.converter.XmlConverter;

/**
 *
 * @author johan
 */
public class FileSourceTest extends TestCase {
	
	public FileSourceTest(String testName) {
		super(testName);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}
	
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testReadSingleXML() throws FileNotFoundException {
		URL url = this.getClass().getResource("singlebook.xml");
		File f = new File(url.getFile());
		assertTrue(f.exists());
		FileSource<Book> fs = new FileSource(f);
//		fs.setSingle(true);
		Book data = fs.get();
		assertNotNull(data);
	}
	
	public void testReadManyXML() throws FileNotFoundException, IOException {
		//URL url = this.getClass().getResource("manybooks.xml");
		//File f = new File(url.getFile());
		String fn = createManyBookFile();
		System.out.println("filename = "+fn);
		File f = new File(fn);
		assertTrue(f.exists());
        XmlConverter<Book> converter = new XmlConverter<Book>("entries", Book.class);
		FileSource<Book> fs = new FileSource<Book>(f, converter);
		
		List<Book> data = new LinkedList<Book>();
		while (fs.next()) {
			data.add(fs.get());
		}
		assertNotNull(data);
		assertEquals(data.size(),2);
	}
	
	private String createManyBookFile () throws IOException {
		Books books = new Books();
		Book book1 = new Book();
		book1.setTitle("Title 1");
		Book book2 = new Book();
		book2.setTitle("Tile 2");
		books.getEntries().add(book1);
		books.getEntries().add(book2);
		File createTempFile = File.createTempFile("test", ".xml");
		JAXB.marshal(books, createTempFile);
		return createTempFile.getAbsolutePath();
	}
	// TODO add test methods here. The name must begin with 'test'. For example:
	// public void testHello() {}
}
