/*
 * Coypright (c) 2006 Mindquarry GmbH, Potsdam, Germany 
 */
package com.mindquarry.search.cocoon;

import java.io.File;
import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.FileTypeMap;
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
import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceNotFoundException;
import org.apache.excalibur.source.impl.FileSource;
import org.xml.sax.SAXException;

import com.mindquarry.search.cocoon.filters.FilterException;
import com.mindquarry.search.cocoon.filters.TextFilter;

/**
 * <p>
 * A cocoon sitemap generator that filters the given input source to extract
 * text content(s). This is intended for any kind of files, but especially for
 * binary files like word processing documents or pdfs to be able to display a
 * preview of the text content or to index the files for a search engine.
 * </p>
 * 
 * <p>
 * The process of extracting is done by finding out the mime type via an
 * advanced guessing mechanism (using the jmimemagic lib) and then using
 * predefined filtering components to extract the content. Those filtering
 * components must implement the
 * <code>com.mindquarry.search.cocoon.filters.TextFilter</code> interface and
 * be registered as Spring beans with a bean id that includes the mime type:
 * <code>com.mindquarry.search.TextFilter/mime-type</code>, where "mime-type"
 * must be replaced by the one that filter is handling. There can only be one
 * TextFilter for a specific mime type, no priority or similar mechanism exists.
 * </p>
 * 
 * <p>
 * The generated XML will vary on the number of fields the concrete TextFilter
 * will produce (eg. content, author, title, subject, etc.). The most basic
 * field is <code>&lt;content&gt;</code>, containing the full text content of
 * the document, which should always be available. Additionaly the field
 * <code>&lt;mime-type&gt;</code> is always present, indicating the mime type
 * which was actually used for filtering. A typical output looks like this:
 * </p>
 * 
 * <pre>
 *   &lt;document&gt;
 *       &lt;mime-type&gt;application/msword&lt;/mime-type&gt;
 *       &lt;content&gt;Lorem Impsum ... &lt;/content&gt;
 *   &lt;/document&gt;
 * </pre>
 * 
 * @author <a href="mailto:lars(dot)trieloff(at)mindquarry(dot)com"> Lars
 *         Trieloff</a>
 * @author <a href="mailto:alexander(dot)klimetschek(at)mindquarry(dot)com">
 *         Alexander Klimetschek</a>
 * 
 */
public class TextFilterGenerator extends FileGenerator implements Generator {
    /**
     * The root element of the XML document that is returned by this generator.
     */
    public static final String DOCUMENT_ROOT_ELEMENT = "document";

    /**
     * Additional field that contains the actual mime type used for filtering.
     */
    public static final String MIME_TYPE_ELEMENT = "mime-type";

    /**
     * "Stupid" mime type which only says that the content is a binary stream.
     * Often used when the actual mime type is unknown.
     */
    private static final String GENERIC_BINARY_MIMETYPE = "application/octet-stream";

    /**
     * Cached map of mime type to according TextFilter component.
     */
    private Map<String, TextFilter> textFilters;

    public void setup(SourceResolver resolver, Map objectModel, String src,
            Parameters par) throws ProcessingException, SAXException,
            IOException {
        super.setup(resolver, objectModel, src, par);

        this.textFilters = new HashMap<String, TextFilter>();
        
        MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
        // add basic file types that are not correctly handled by jmimemage
        fileTypeMap.addMimeTypes("application/mspowerpoint ppt");
        fileTypeMap.addMimeTypes("application/msexcel xls");
        FileTypeMap.setDefaultFileTypeMap(fileTypeMap);
    }

    public void dispose() {
        super.dispose();

        // we have to release all TextFilter components we have aggregated
        for (Iterator<TextFilter> i = this.textFilters.values().iterator(); i
                .hasNext();) {
            this.manager.release(i.next());
        }
        this.textFilters.clear();
    }

    public void generate() throws IOException, SAXException,
            ProcessingException {
        String mimeType = guessMimetype(this.inputSource);
        Map<String, Reader> fields = filter(this.inputSource, mimeType);

        this.contentHandler.startDocument();

        XMLUtils.startElement(this.contentHandler, DOCUMENT_ROOT_ELEMENT);

        XMLUtils.createElement(this.contentHandler, MIME_TYPE_ELEMENT, mimeType);

        // copy each field returned by the TextFilter as a single XML element
        // with the filtered content stream as text content of the element
        for (Iterator i = fields.keySet().iterator(); i.hasNext();) {
            Object key = i.next();

            XMLUtils.startElement(this.contentHandler, key.toString());

            // since we put the filtered text stream into an XML stream, we
            // must ensure that all characters are valid XML chars; any invalid
            // characters are filtered out by the inner class XMLFilterReader
            Reader fieldValue = new XMLFilterReader((Reader) fields.get(key));

            // copy the filtered stream in 1K chunks into the XML stream
            int read = 0;
            char[] cont = new char[1024];

            while ((read = fieldValue.read(cont, 0, 1024)) != -1) {
                this.contentHandler.characters(cont, 0, read);
            }

            XMLUtils.endElement(this.contentHandler, key.toString());
        }

        XMLUtils.endElement(this.contentHandler, DOCUMENT_ROOT_ELEMENT);

        this.contentHandler.endDocument();
    }

    /**
     * Calls the TextFilter for the source with the given (correct) mime type
     * and returns a map with all fields -- Reader the filter has read.
     */
    private Map<String, Reader> filter(Source source, String mimeType) {
        // lookup for an already installed filter for mimeType
        TextFilter filter = this.textFilters.get(mimeType);

        // if the filter for mimeType was not yet installed, do that now
        if (filter == null) {
            try {
                filter = (TextFilter) this.manager
                        .lookup("com.mindquarry.search.TextFilter/" + mimeType);
                                
                this.textFilters.put(mimeType, filter);
            } catch (ServiceException e) {
                // if no filter was installed, simply return an empty mapping
                return new HashMap<String, Reader>();
            }
        }

        // filter the input stream
        try {
            InputStream inputStream = source.getInputStream();
            return filter.doFilter(inputStream);
        } catch (SourceNotFoundException e) {
            getLogger().error("Document not found", e);
        } catch (IOException e) {
            getLogger().error("Could not read document", e);
        } catch (FilterException e) {
            getLogger().error("Extracing content failed", e.getCause());
        }

        // nothing was filtered, return empty map
        return new HashMap<String, Reader>();
    }

    /**
     * The "application/octet-stream" mime type is generic and does not give any
     * information just like the null String.
     */
    public static boolean isUndefined(String mimeType) {
        return (mimeType == null || GENERIC_BINARY_MIMETYPE.equals(mimeType));
    }

    /**
     * <p>
     * Tries to find out the mime type. This is done by (1) looking at the mime
     * type of the inputSource. If this one is not usable, we (2) use the
     * information from the MimetypesFileTypeMap provided by java. The last
     * ressort (3) is the jmimemagic parser which uses the magic bytes data
     * stored at the beginning of most files to obtain the actual mime type.
     * </p>
     * 
     * <p>
     * Since we are using excalibur Sources, we must handle the generic case,
     * where we read the header (first 1K bytes of the stream) to pass it to the
     * jmimemagic parser. For the simpler case with an actual file (FileSource)
     * we can use the provided methods working with java.io.File.
     * </p>
     */
    public static String guessMimetype(Source source) {
        // (1) this mimetype is rarely defined...
        String mimeType = source.getMimeType();

        // check for the simpler java.io.File case
        if (source instanceof FileSource) {
            FileSource fileSource = (FileSource) source;
            File srcFile = fileSource.getFile();

            // (2) check for the file types table with the File object
            if (isUndefined(mimeType)) {
                mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap()
                        .getContentType(srcFile);
            }

            // (3) call the jmimemagic parser with the File object
            if (isUndefined(mimeType)) {
                Magic parser = new Magic();
                try {
                    mimeType = parser.getMagicMatch(srcFile).getMimeType();
                } catch (MagicParseException e) {
                } catch (MagicMatchNotFoundException e) {
                } catch (MagicException e) {
                    // ignore all exceptions since we are guessing only
                }
            }
        }

        // (2) check for the file types table with the URI
        if (isUndefined(mimeType)) {
            mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap()
                    .getContentType(source.getURI());
        }

        // (3) call the jmimemagic parser with the first 1K bytes of the stream
        if (isUndefined(mimeType)) {
            byte[] header = new byte[1024];
            try {
                source.getInputStream().read(header);
                Magic parser = new Magic();
                mimeType = parser.getMagicMatch(header).getMimeType();
            } catch (SourceNotFoundException e) {
            } catch (IOException e) {
            } catch (MagicParseException e) {
            } catch (MagicMatchNotFoundException e) {
            } catch (MagicException e) {
                // ignore all exceptions since we are guessing only
            }
        }

        return mimeType;
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
            return Character.isIdentifierIgnorable((int) i) || i < 17;
        }

        public int read(char[] cbuf, int off, int len) throws IOException {
            int result = in.read(cbuf, off, len);

            for (int i = off; i < len; i++) {
                if (isIgnorable((int) cbuf[i])) {
                    cbuf[i] = ' ';
                }
            }

            return result;
        }
    }
}
