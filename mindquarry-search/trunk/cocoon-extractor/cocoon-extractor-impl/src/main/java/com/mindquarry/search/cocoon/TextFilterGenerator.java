package com.mindquarry.search.cocoon;

import java.io.File;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.FileGenerator;
import org.apache.cocoon.generation.Generator;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.impl.FileSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.search.cocoon.filters.TextFilter;

public class TextFilterGenerator extends FileGenerator implements Generator {
	private static final String GENERIC_BINARY_MIMETYPE = "application/octet-stream";

	private Map<String, TextFilter> textFilters;

	public void generate() throws IOException, SAXException,
			ProcessingException {
		Map contents = filter();
		this.contentHandler.startDocument();
		AttributesImpl atts = new AttributesImpl();
		atts.addAttribute(null, "mime-type", "mime-type", "CDATA",
				this.inputSource.getMimeType());
		this.contentHandler.startElement(null, "document", "document",
				new AttributesImpl());
		for (Iterator i = contents.keySet().iterator(); i.hasNext();) {
			Object key = i.next();
			Reader value = new XMLFilterReader((Reader) contents.get(key));
			atts = new AttributesImpl();
			// atts.addAttribute(null, "text-filter", "text-filter", "CDATA",
			// value)
			this.contentHandler.startElement(null, key.toString(), key
					.toString(), atts);

			int read = 0;
			char[] cont = new char[1024];

			while ((read = value.read(cont, 0, 1024)) != -1) {
				this.contentHandler.characters(cont, 0, read);
			}

			this.contentHandler
					.endElement(null, key.toString(), key.toString());
		}
		this.contentHandler.endElement(null, "document", "document");
		this.contentHandler.endDocument();
	}

	private Map filter() {
		String mimeType = guessMimetype();
		TextFilter filter = this.textFilters.get(mimeType);
		if (filter == null) {
			try {
				filter = (TextFilter) this.manager
						.lookup("com.mindquarry.search.TextFilter/" + mimeType);
				this.textFilters.put(mimeType, filter);
				InputStream inputStream = this.inputSource.getInputStream();
				return filter.doFilter(inputStream);
			} catch (ServiceException e) {
				getLogger().error("Could not lookup filter", e);
			} catch (SourceNotFoundException e) {
				getLogger().error("Document not found", e);
			} catch (IOException e) {
				getLogger().error("Could not read document", e);
			} catch (Exception e) {
				getLogger().error("Extracing content failed", e);
			}
		}
		return new HashMap();
	}

	private String guessMimetype() {
		String mimeType = this.inputSource.getMimeType();
		if (this.inputSource instanceof FileSource) {
			FileSource fileSource = (FileSource) this.inputSource;
			File srcFile = fileSource.getFile();
			if ((mimeType == null)
					|| (GENERIC_BINARY_MIMETYPE.equals(mimeType))) {
				mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap()
						.getContentType(srcFile);
			}
			if ((mimeType == null)
					|| (GENERIC_BINARY_MIMETYPE.equals(mimeType))) {
				Magic parser = new Magic();
				try {
					mimeType = parser.getMagicMatch(srcFile).getMimeType();
				} catch (MagicParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MagicMatchNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MagicException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if ((mimeType == null) || (GENERIC_BINARY_MIMETYPE.equals(mimeType))) {
			mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap()
					.getContentType(this.inputSource.getURI());
		}
		if ((mimeType == null) || (GENERIC_BINARY_MIMETYPE.equals(mimeType))) {
			byte[] header = new byte[1024];
			try {
				this.inputSource.getInputStream().read(header);
				Magic parser = new Magic();
				mimeType = parser.getMagicMatch(header).getMimeType();
			} catch (SourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MagicParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MagicMatchNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MagicException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mimeType;
	}

	public void dispose() {
		super.dispose();
		for (Iterator<TextFilter> i = this.textFilters.values().iterator(); i
				.hasNext();) {
			this.manager.release(i.next());
		}
		this.textFilters.clear();
	}

	public void setup(SourceResolver arg0, Map arg1, String arg2,
			Parameters arg3) throws ProcessingException, SAXException,
			IOException {
		super.setup(arg0, arg1, arg2, arg3);
		this.textFilters = new HashMap<String, TextFilter>();
	}

	/**
	 * This class filters identifier ignorable characters from a Reader. Some of
	 * those characters are invalid in XML and throw exceptions when transformed
	 * (for example 0x8, backspace). This filter replaces those characters with
	 * spaces.
	 * 
	 * @author Stefan Kanev
	 * 
	 */
	private class XMLFilterReader extends FilterReader {

		public XMLFilterReader(Reader in) {
			super(in);
		}

		public int read() throws IOException {
			int i = in.read();
			if (isIgnorable(i)) {
				i = (int) ' ';
			}
			return i;
		}

		private boolean isIgnorable(int i) {
			return Character.isIdentifierIgnorable((int) i)||i<17;
		}

		public int read(char[] cbuf, int off, int len) throws IOException {
			int result = in.read(cbuf, off, len);

			// TODO: This might not work that well. Check exactly what it does
			for (int i = off; i < len; i++) {
				if (isIgnorable((int) cbuf[i])) {
					cbuf[i] = ' ';
				}
			}

			return result;
		}
	}
}
