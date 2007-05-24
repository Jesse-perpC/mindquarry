package com.mindquarry.teamspace.transformation;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractSAXTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import com.mindquarry.teamspace.TeamspaceQuery;
import com.mindquarry.teamspace.TeamspaceRO;
import com.mindquarry.user.UserQuery;
import com.mindquarry.user.UserRO;
/**
 * This transformer eases working with userids and teamspaceids that are ubiquitous in Mindquarry.
 * The transformer can replace a user id or teamspace id with the corresponding human readable
 * name, e.g. userid 'lars' will be replaced with user name 'Lars Trieloff'.
 * 
 * The transformer will replace following elements:
 * <pre>
 * &lt;team xmlns="http://mindquarry.com/ns/schema/teamtransform"&gt;$teamid&lt;/team&gt;
 * &lt;user xmlns="http://mindquarry.com/ns/schema/teamtransform"&gt;$userid&lt;/user&gt;
 * </pre>
 * The user id takes the attribute <tt>only</tt> to show only the <tt>firstname</tt>
 * or <tt>surname</tt>.
 * <pre>
 * &lt;user xmlns="http://mindquarry.com/ns/schema/teamtransform" only="firstname"&gt;$userid</user>
 * &lt;!-- returns 'Lars' --&gt;
 * &lt;user xmlns="http://mindquarry.com/ns/schema/teamtransform" only="surname"&gt;$userid</user>
 * &lt;!-- returns 'Trieloff' --&gt;
 * </pre>
 * @author Lars Trieloff
 *
 */
public class TeamspaceTransformer extends AbstractSAXTransformer {
	@Override
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters params) throws ProcessingException, SAXException,
			IOException {
		super.setup(resolver, objectModel, src, params);
	}

	private static final String SURNAME_ATTRIBUTE_VALUE = "surname";
	private static final String FIRSTNAME_ATTRIBUTE_VALUE = "firstname";
	private static final String ONLY_ATTRIBUTE = "only";
	private String only;
	public static String TEAM_NS ="http://mindquarry.com/ns/schema/teamtransform";
	public static String TEAM_ELEMENT = "team";
	public static String USER_ELEMENT = "user";
	private TeamspaceQuery teamquery;
	private UserQuery userquery;
	public TeamspaceTransformer() {
		super();
		this.defaultNamespaceURI = TEAM_NS;
		this.removeOurNamespacePrefixes = true;
	}
	@Override
	public void endTransformingElement(String uri, String name, String raw)
			throws ProcessingException, IOException, SAXException {
		String text = this.endTextRecording().trim();
		if (text.equals("")) {
			return;
		}
		if (name.equals(TEAM_ELEMENT)) {
			TeamspaceRO team = teamquery.teamspaceById(text);
			if (team==null) {
				this.sendTextEvent(text);
			} else {
				this.sendTextEvent(team.getName());
			}
		} else if (name.equals(USER_ELEMENT)) {
			UserRO user = userquery.userById(text);
			if (user==null) {
				this.sendTextEvent(text);
			} else {
				if (only==null||only.equals(FIRSTNAME_ATTRIBUTE_VALUE)) {
					this.sendTextEvent(user.getName());
				}
				//show delimiter only when both first and surname are selected
				if (only==null) {
					this.sendTextEvent(" ");
				}
				//show the surname
				if (only==null||only.equals(SURNAME_ATTRIBUTE_VALUE)) {
					this.sendTextEvent(user.getSurname());
				}
			}
		}
	}
	@Override
	public void startTransformingElement(String uri, String name, String raw,
			Attributes attr) throws ProcessingException, IOException,
			SAXException {
		if (name.equals(USER_ELEMENT)) {
			this.only = attr.getValue("", ONLY_ATTRIBUTE);
		}
		this.startTextRecording();
	}

	@Override
	public void service(ServiceManager manager) throws ServiceException {
		super.service(manager);
		this.userquery = (UserQuery) manager.lookup("com.mindquarry.user.UserQuery");
		this.teamquery = (TeamspaceQuery) manager.lookup("com.mindquarry.teamspace.TeamspaceQuery");
	}
}