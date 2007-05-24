package com.mindquarry.jcr.xml.source.xenodot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Stack;

import javax.jcr.Session;

import org.apache.excalibur.source.SourceException;
import org.apache.jackrabbit.core.NodeImpl;
import org.apache.jackrabbit.uuid.UUID;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import com.mindquarry.common.index.IndexClient;
import com.mindquarry.jcr.xenodot.XenodotClient;
import com.mindquarry.jcr.xml.source.JCRConstants;
import com.mindquarry.jcr.xml.source.JCRNodeSource;
import com.mindquarry.jcr.xml.source.JCRSourceFactory;

public class XenodotSource extends JCRNodeSource {
    private XenodotClient xenodot;

    public XenodotSource(JCRSourceFactory factory, Session session,
            String path, IndexClient client, String myrevision,
            XenodotClient xenodot) throws SourceException {
        super(factory, session, path, client, myrevision);
        this.xenodot = xenodot;
    }

    private final static String[] NODE_ELEMENTS = { "id", "node_name_ns",
            "node_name_local", "node_name_prefix", "node_hierarchy" };

    private String qname(String prefix, String local) {
        if (prefix == null || "".equals(prefix)) {
            return local;
        } else {
            return prefix + ":" + local;
        }
    }

    private void writeEvent(ContentHandler handler,
            Stack<HashMap<String, String>> stack,
            HashMap<String, String> currentRow, AttributesImpl atts)
            throws SAXException {
        if (currentRow.isEmpty()) {
            return;
        }
        if (atts.getValue("http://mindquarry.com/ns/cnd/xt", "characters") != null) {
            String stringValue = atts.getValue(
                    "http://mindquarry.com/ns/cnd/xt", "characters");
            char[] chars = stringValue.toCharArray();
            handler.characters(chars, 0, chars.length);
        } else {
            handler.startElement(currentRow.get("node_name_ns"), currentRow
                    .get("node_name_local"),
                    qname(currentRow.get("node_name_prefix"), currentRow
                            .get("node_name_local")), atts);
            stack.push((HashMap<String, String>) currentRow.clone());
        }
        currentRow.clear();
        atts.clear();
    }

    public void toSAX(ContentHandler handler) throws SAXException {

        Connection connection = xenodot.getConnection();
        try {
            UUID uuid = ((NodeImpl) node.getNode(JCRConstants.JCR_CONTENT))
                    .getNodeId().getUUID();
            PreparedStatement sql = connection
                    .prepareStatement("select content.* from xenodot.content_nodes as content, "
                            + "jackrabbit.node(?, ?) as root "
                            + "where content.node_hierarchy like (root.hierarchy || '/%')"
                            + "order by content.node_hierarchy, content.node_position, content.property_position;");
            sql.setLong(1, uuid.getMostSignificantBits());
            sql.setLong(2, uuid.getLeastSignificantBits());
            ResultSet result = sql.executeQuery();

            handler.startDocument();

            HashMap<String, String> currentRow = new HashMap<String, String>();
            Stack<HashMap<String, String>> stack = new Stack<HashMap<String, String>>();
            AttributesImpl atts = new AttributesImpl();

            while (result.next()) {
                String currentId = currentRow.get("id");

                while (currentId != null
                        && !stack.empty()
                        && !currentRow.get("node_hierarchy").startsWith(
                                stack.peek().get("node_hierarchy"))) {
                    HashMap<String, String> endNode = stack.pop();
                    handler.endElement(endNode.get("node_name_ns"), endNode
                            .get("node_name_local"), qname(endNode
                            .get("node_name_prefix"), endNode
                            .get("node_name_local")));
                }

                if (!result.getString("id").equals(currentId)) {
                    writeEvent(handler, stack, currentRow, atts);
                }

                for (String name : NODE_ELEMENTS) {
                    currentRow.put(name, result.getString(name));
                }

                if (result.getString("value_string") != null
                        && result.getInt("property_position") == 0) {
                    String ns = result.getString("property_name_ns");
                    String prefix = result.getString("property_name_prefix");
                    String local = result.getString("property_name_local");
                    String qname = qname(prefix, local);
                    atts.addAttribute(ns, local, qname, "CDATA", result
                            .getString("value_string"));
                }
            }
            writeEvent(handler, stack, currentRow, atts);
            while (!stack.empty()) {
                HashMap<String, String> endNode = stack.pop();
                handler.endElement(endNode.get("node_name_ns"), endNode
                        .get("node_name_local"), qname(endNode
                        .get("node_name_prefix"), endNode
                        .get("node_name_local")));
            }

            handler.endDocument();
        } catch (Exception exp) {
            exp.printStackTrace();
            throw new SAXException(exp);
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
