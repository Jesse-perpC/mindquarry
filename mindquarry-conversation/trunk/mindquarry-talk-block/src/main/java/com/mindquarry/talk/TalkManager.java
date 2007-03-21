package com.mindquarry.talk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.ModifiableTraversableSource;

import com.mindquarry.jcr.id.JCRUniqueIDGenerator;
import com.mindquarry.teamspace.Teamspace;
import com.mindquarry.teamspace.TeamspaceListener;
import com.mindquarry.teamspace.TeamspaceListenerRegistry;

public class TalkManager implements TeamspaceListener {

	private static final String JCR_SCHEME = "jcr://";
	
    private SourceResolver sourceResolver;
	
    private JCRUniqueIDGenerator uniqueIDGenerator;
	
    public String getUniqueTalkId(String path, String title) throws Exception {
        String teamspace = extractTeamspaceFromURI(path);
        initializeTalk(teamspace, teamspace);
        
        final String normalizedTitle = normalizeTitle(title);
        ModifiableTraversableSource source;
        source = (ModifiableTraversableSource) this.sourceResolver
                .resolveURI(path + normalizedTitle + "/meta.xml");
        if (source.exists()) {
            long id = this.uniqueIDGenerator.getNextID(path);
            return normalizedTitle + "-" + id;
        } else {
            return normalizedTitle;
        }
    }
    
    private String extractTeamspaceFromURI(String jcrURI) throws Exception {
        // simple regular expression that looks for the teamspace name...
        Pattern p = Pattern.compile("jcr:///teamspaces/([^/]*)/(.*)");
        Matcher m = p.matcher(jcrURI);
        if (m.matches()) {
            return m.group(1); // "mindquarry";
        }
        
        throw new Exception("Invalid JCR URI.");
    }
    
    /**
     * Initialization is only done, if the things to initialize are not yet
     * existing, so it can be called anytime to ensure the existence.
     */
    private void initializeTalk(String teamspace, String teamspaceName) throws Exception {
        String talkDirPath = "/teamspaces/" + teamspace + "/talk/conversations";
        
        // create sub directory
        ModifiableTraversableSource source;
        source = (ModifiableTraversableSource) this.sourceResolver
                .resolveURI(JCR_SCHEME + talkDirPath);
        if (!source.exists()) {
            source.makeCollection();
        }
        
        // create id subnode
        this.uniqueIDGenerator.initializePath(talkDirPath);
    }
    
    /**
     * characters to keep for normalizeTitle()
     */
    private final static String legal = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private final static char normalizeGapFiller = '-';
    
    public String normalizeTitle(String title) {
        StringBuffer result = new StringBuffer();
        
        char lastCh = normalizeGapFiller;
        for (int i = 0; i < title.length(); i++) {
            char ch = title.charAt(i);
            if (legal.indexOf(ch) == -1) {
            	ch = normalizeGapFiller;
            }
            if (ch == normalizeGapFiller && lastCh == normalizeGapFiller) {
            	continue;
            }

            result.append(ch);
            lastCh = ch;
        }
        
        if (result.charAt(result.length() - 1) == normalizeGapFiller)
        	result.deleteCharAt(result.length() - 1);
        
        return result.toString();
    }
    
	public void afterTeamspaceRemoved(Teamspace arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	public void beforeTeamspaceCreated(Teamspace teamspace) throws Exception {
		// TODO Auto-generated method stub

		initializeTalk(teamspace.getId(), teamspace.getName());
	}

    public void setTeamspaceListenerRegistry(TeamspaceListenerRegistry registry) {
        registry.addListener(this);
    }

    public void setSourceResolver(SourceResolver resolver) {
        this.sourceResolver = resolver;
    }

    public void setUniqueIDGenerator(JCRUniqueIDGenerator uniqueIDGenerator) {
        this.uniqueIDGenerator = uniqueIDGenerator;
    }
	
}
