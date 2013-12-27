/**
 * Copyright (c) 2011, 2013, Jonathan Giles, Johan Vos, Hendrik Ebbers
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of DataFX, the website javafxdata.org, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.datafx.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
	
//	public void testReadSingleXML() throws FileNotFoundException, URISyntaxException {
//		URL url = this.getClass().getResource("singlebook.xml");
//		File f = new File(url.toURI());
//		assertTrue(f.exists());
//		FileSource<Book> fs = new FileSource(f);
////		fs.setSingle(true);
//		Book data = fs.get();
//		assertNotNull(data);
//	}
	
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
