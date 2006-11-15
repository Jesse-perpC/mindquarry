package com.mindquarry.search.cocoon;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.generation.FileGenerator;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.AttributesImpl;
import org.xml.sax.SAXException;

import com.mindquarry.search.cocoon.filters.TextFilter;

public class TextFilterGenerator extends FileGenerator implements Generator {
	private List textFilters;
	
	public void generate() throws IOException, SAXException,
			ProcessingException {
		Map contents = filter();
		this.contentHandler.startDocument();
		this.contentHandler.startElement(null, "document", "document", new AttributesImpl());
		for (Iterator i=contents.keySet().iterator();i.hasNext();) {
			Object key = i.next();
			Reader value = (Reader) contents.get(key);
			this.contentHandler.startElement(null, key.toString(), key.toString(), new AttributesImpl());
			
			int read = 0;
			char[] cont = {};
			
			while((read = value.read(cont, 0, 1024))!=-1) {
				this.contentHandler.characters(cont, 0, read);
			}
			
			this.contentHandler.endElement(null, key.toString(), key.toString());
		}
		this.contentHandler.endElement(null, "document", "document");
		this.contentHandler.endDocument();
	}
	
	private Map filter() {
		for (final Iterator i=this.textFilters.iterator();i.hasNext();) {
			final TextFilter filter = (TextFilter) i.next();
			if (filter.canFilter(this.inputSource.getMimeType())) {
				try {
					return filter.doFilter(this.inputSource.getInputStream());
				} catch (final Exception e) {
					getLogger().error("Failed to filter source", e);
				}
			}
		}
		return new HashMap();
	}

}
