package com.mindquarry.teamspace.transformation;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
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
public class TeamspaceTransformer extends AbstractTransformer {
	private static final String SURNAME_ATTRIBUTE_VALUE = "surname";
	private static final String FIRSTNAME_ATTRIBUTE_VALUE = "firstname";
	private static final String ONLY_ATTRIBUTE = "only";
	private StringBuffer teamid;
	private StringBuffer userid;
	private String only;
	
	public static String TEAM_NS ="http://mindquarry.com/ns/schema/teamtransform";
	
	public static String TEAM_ELEMENT = "team";
	public static String USER_ELEMENT = "user";
	private TeamspaceQuery teamquery;
	private UserQuery userquery;
	
	public void setup(SourceResolver resolver, Map objectModel, String src,
			Parameters par) throws ProcessingException, SAXException,
			IOException {
		//nothing to do here
	}

	/**
	 * Characters are always piped through, ony when a <team> or <user> element has been
	 * seen, they will be recorded.
	 */
	@Override
	public void characters(char[] c, int start, int len) throws SAXException {
		if (this.teamid!=null) {
			this.teamid.append(c, start, len);
		} else if (this.userid!=null) {
			this.userid.append(c, start, len);
		} else {
			super.characters(c, start, len);
		}
	}

	/**
	 * At end of an <team> or <user> element, team and username will be resolved, and written
	 * to the output stream.
	 */
	@Override
	public void endElement(String uri, String loc, String raw)
			throws SAXException {
		//if it is a teamtransformer element
		if (uri.equals(TEAM_NS)) {
			//if it is a team element
			if (loc.equals(TEAM_ELEMENT)) {
				//get the team object
				TeamspaceRO team = teamquery.teamspaceById(this.teamid.toString().trim());
				if (team==null) {
					//if there is no team object, pipe the original team id throgh.
					char[] chars = this.teamid.toString().toCharArray();
					this.contentHandler.characters(chars, 0, chars.length);
				} else {
					//else write the team's name
					this.contentHandler.characters(team.getName().toCharArray(),0 , team.getName().length());
				}
			//if it is a user element
			} else if (loc.equals(USER_ELEMENT)) {
				//get the user object
				UserRO user = userquery.userById(this.userid.toString().trim());
				if (user==null) {
					//no user found, use the user id
					char[] chars = this.userid.toString().toCharArray();
					this.contentHandler.characters(chars, 0, chars.length);
				} else {
					//show the first name
					if (only==null||only.equals(FIRSTNAME_ATTRIBUTE_VALUE)) {
						this.contentHandler.characters(user.getName().toCharArray(),0 , user.getName().length());
					}
					//show delimiter only when both first and surname are selected
					if (only==null) {
						this.contentHandler.characters(" ".toCharArray(), 0, 1);
					}
					//show the surname
					if (only==null||only.equals(SURNAME_ATTRIBUTE_VALUE)) {
						this.contentHandler.characters(user.getSurname().toCharArray(),0 , user.getSurname().length());
					}
				}
			} else {
				super.endElement(uri, loc, raw);
			}
		} else {
			super.endElement(uri, loc, raw);
		}
		this.userid = null;
		this.teamid = null;
	}


	@Override
	public void startElement(String uri, String loc, String raw, Attributes a)
			throws SAXException {
		if (uri.equals(TEAM_NS)) {
			if (loc.equals(TEAM_ELEMENT)) {
				this.teamid = new StringBuffer();
			} else if (loc.equals(USER_ELEMENT)) {
				this.only = a.getValue("", ONLY_ATTRIBUTE);
				this.userid = new StringBuffer();
			} else {
				super.startElement(uri, loc, raw, a);
			}
		} else {
			super.startElement(uri, loc, raw, a);
		}
	}

	public void setTeamquery(TeamspaceQuery teamquery) {
		this.teamquery = teamquery;
	}

	public void setUserquery(UserQuery userquery) {
		this.userquery = userquery;
	}

}
