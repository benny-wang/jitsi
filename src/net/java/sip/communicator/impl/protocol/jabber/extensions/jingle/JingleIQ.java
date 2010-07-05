/*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.jabber.extensions.jingle;

import java.util.*;

import org.jivesoftware.smack.packet.*;
import org.jivesoftware.smackx.packet.*;

import net.java.sip.communicator.util.*;

/**
 * A straightforward extension of the IQ. A <tt>JingleIQ</tt> object is created
 * by smack via the {@link JingleIQProvider}. It contains all the information
 * extracted from a <tt>jingle</tt> IQ.
 *
 * @author Emil Ivov
 */
public class JingleIQ extends IQ
{
    /**
     * Logger for this class
     */
    private static final Logger logger =
        Logger.getLogger(JingleIQ.class);

    /**
     * The name space that jingle belongs to.
     */
    public static final String NAMESPACE = "urn:xmpp:tmp:jingle";

    /**
     * The name of the element that contains the jingle data.
     */
    public static final String ELEMENT_NAME = "jingle";

    /**
     * The <tt>JingleAction</tt> that describes the purpose of this
     * <tt>jingle</tt> element.
     */
    private JingleAction action;

    /**
     * The full JID of the entity that has initiated the session flow. Only
     * present when the <tt>JingleAction</tt> is <tt>session-accept</tt>.
     */
    private String initiator;

    /**
     * The full JID of the entity that replies to a Jingle initiation. The
     * <tt>responder</tt> can be different from the 'to' address on the IQ-set.
     * Only present when the <tt>JingleAction</tt> is <tt>session-accept</tt>.
     */
    private String responder;

    /**
     * The ID of the Jingle session that this IQ belongs to. XEP-0167: A sid is
     * a random session identifier generated by the initiator, which
     * effectively maps to the local-part of a SIP "Call-ID" parameter
     */
    private String sid;







    // Sub-elements of a Jingle object.

    private final List<JingleContent> contentList = new ArrayList<JingleContent>();

    private JingleContentInfo contentInfo;

    /* (non-Javadoc)
     * @see org.jivesoftware.smack.packet.IQ#getChildElementXML()
     */
    @Override
    public String getChildElementXML()
    {
        // TODO Auto-generated method stub
        return null;
    }

}