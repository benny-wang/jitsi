/*
 * SIP Communicator, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.protocol.jabber.extensions.jingle;

import java.util.*;

import net.java.sip.communicator.util.*;

import org.jivesoftware.smack.packet.*;

/**
 * The Jingle "content" element contains the entire description of the session
 * being initiated. Among other things it contains details such as transport,
 * or (in the case of RTP) payload type list.
 *
 * @author Emil Ivov
 */
public class ContentPacketExtension implements PacketExtension
{
    /**
     * Logger for this class
     */
    private static final Logger logger =
        Logger.getLogger(ContentPacketExtension.class);

    /**
     * The name space (or rather lack thereof ) that the content element
     * belongs to.
     */
    public static final String NAMESPACE = "";

    /**
     * The name of the "content" element.
     */
    public static final String ELEMENT_NAME = "content";

    /**
     * The name of the "creator" argument.
     */
    public static final String CREATOR_ARG_NAME = "creator";

    /**
     * The name of the "disposition" argument.
     */
    public static final String DISPOSITION_ARG_NAME = "disposition";

    /**
     * The name of the "name" argument.
     */
    public static final String NAME_ARG_NAME = "name";

    /**
     * The name of the "senders" argument.
     */
    public static final String SENDERS_ARG_NAME = "senders";

    /**
     * The values we currently support for the creator field.
     */
    public static enum CreatorEnum
    {
        /**
         * Indicates that content type was originally generated by the session
         * initiator
         */
        initiator,

        /**
         * Indicates that content type was originally generated by the session
         * addressee
         */
        responder
    };

    /**
     * The creator field indicates which party originally generated the content
     * type and is used to prevent race conditions regarding modifications;
     * the defined values are "initiator" and "responder" (where the
     * default is "initiator"). The value of the 'creator' attribute for a
     * given content type MUST always match the party that originally generated
     * the content type, even for Jingle actions that are sent by the other
     * party in relation to that content type (e.g., subsequent content-modify
     * or transport-info messages). The combination of the 'creator'
     * attribute and the 'name' attribute is unique among both parties to a
     * Jingle session.
     */
    private CreatorEnum creator;

    /**
     * Indicates how the content definition is to be interpreted by the
     * recipient. The meaning of this attribute matches the
     * "Content-Disposition" header as defined in RFC 2183 and applied to SIP
     * by RFC 3261. The value of this attribute SHOULD be one of the values
     * registered in the IANA Mail Content Disposition Values and Parameters
     * Registry. The default value of this attribute is "session".
     */
    private String disposition;

    /**
     * A unique name or identifier for the content type according to the
     * creator, which MAY have meaning to a human user in order to differentiate
     * this content type from other content types (e.g., two content types
     * containing video media could differentiate between "room-pan" and
     * "slides"). If there are two content types with the same value for the
     * 'name' attribute, they shall understood as alternative definitions for
     * the same purpose (e.g., a legacy method and a standards-based method for
     * establishing a voice call), typically to smooth the transition from an
     * older technology to Jingle.
     */
    private String name;

    /**
     * Contains a list of packet extensions that are part of the jingle content.
     * Most often, the extensions we find in here would be <tt>description</tt>
     * and <tt>transport</tt>.
     */
    private final List<PacketExtension> childExtensions
                                = new ArrayList<PacketExtension>();

    /**
     * The values we currently support for the <tt>senders</tt> field.
     */
    public static enum SendersEnum
    {
        /**
         * Indicates that only the initiator will be generating content
         */
        initiator,

        /**
         * Indicates that no one in this session will be generating content
         */
        none,

        /**
         * Indicates that only the responder will be generating content
         */
        responder,

        /**
         * Indicates that both parties in this session will be generating
         * content
         */
        both
    };

    /**
     * Indicates which parties in the session will be generating content;
     * the allowable values are defined in the <tt>SendersEnum</tt>.
     */
    private SendersEnum senders = SendersEnum.both;

    /**
     * Creates a new <tt>ContentPacketExtension</tt> instance with only required
     * parameters.
     *
     * @param creator indicates which party originally generated the content
     * type
     * @param disposition indicates how the content definition is to be
     * interpreted by the recipient
     * @param name the content type according to the creator
     * @param senders the parties in the session will be generating content.
     */
    public ContentPacketExtension(CreatorEnum creator,
                                  String disposition,
                                  String name,
                                  SendersEnum senders)
    {
        this.creator = creator;
        this.disposition = disposition;
        this.name = name;
        this.senders = senders;
    }

    /**
     * Creates a new <tt>ContentPacketExtension</tt> instance with the specified
     * parameters.
     *
     * @param creator indicates which party originally generated the content
     * type
     * interpreted by the recipient
     * @param name the content type according to the creator
     */
    public ContentPacketExtension(CreatorEnum creator, String name)
    {
        this.creator = creator;
        this.name = name;
    }

    /**
     * Returns the name of the <tt>content</tt> element.
     *
     * @return the name of the <tt>content</tt> element.
     */
    public String getElementName()
    {
        return ELEMENT_NAME;
    }

    /**
     * Returns an empty <tt>String</tt> as the <tt>content</tt> element does
     * not have a namespace.
     *
     * @return an empty <tt>String</tt> as the <tt>content</tt> element does
     * not have a namespace.
     */
    public String getNamespace()
    {
        return NAMESPACE;
    }

    /**
     * Returns the value of the creator argument. The creator argument indicates
     * which party originally generated the content type and is used to prevent
     * race conditions regarding modifications; the defined values are
     * "initiator" and "responder" (where the default is "initiator"). The value
     * of the 'creator' attribute for a given content type MUST always match
     * the party that originally generated the content type, even for Jingle
     * actions that are sent by the other party in relation to that content
     * type (e.g., subsequent content-modify or transport-info messages). The
     * combination of the 'creator' attribute and the 'name' attribute is
     * unique among both parties to a Jingle session.
     *
     * @return the value of this content's creator argument.
     */
    public CreatorEnum getCreator()
    {
        return creator;
    }

    /**
     * Returns the value of the disposition argument if it exists or
     * <tt>null</tt> if it doesn't. The disposition argument indicates how the
     * content definition is to be interpreted by the recipient. The meaning of
     * this attribute matches the <tt>"Content-Disposition"</tt> header as
     * defined in RFC 2183 and applied to SIP by RFC 3261. The value of this
     * attribute SHOULD be one of the values registered in the IANA Mail
     * Content Disposition Values and Parameters Registry. The default value of
     * this attribute is "session".
     *
     * @return the value of this content's disposition argument.
     */
    public String getDisposition()
    {
        return disposition;
    }

    /**
     * Returns the value of the name argument if it exists or
     * <tt>null</tt> if it doesn't. The name argument is a unique identifier
     * for the content type according to the creator, which MAY have meaning to
     * a human user in order to differentiate this content type from other
     * content types (e.g., two content types containing video media could
     * differentiate between "room-pan" and "slides"). If there are two content
     * types with the same value for the 'name' attribute, they shall understood
     * as alternative definitions for the same purpose (e.g., a legacy method
     * and a standards-based method for establishing a voice call), typically
     * to smooth the transition from an older technology to Jingle.
     *
     * @return the value of this content's <tt>name</tt> argument.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the value of the senders argument which indicates the parties
     * that will be generating content in this session; the allowable values
     * are defined in the <tt>SendersEnum</tt>.
     *
     * @return a {@link SendersEnum} instance indicating the the parties that
     * will be generating content in this session.
     */
    public SendersEnum getSenders()
    {
        return senders;
    }

    /**
     * Returns a reference to the list of <tt>PacketExtension</tt>s that this
     * element contains.
     *
     * @return  a reference to the list of <tt>PacketExtension</tt>s that this
     * element contains.
     */
    public List<PacketExtension> getExtensions()
    {
        return childExtensions;
    }

    /**
     * Adds <tt>extension</tt> to the list of <tt>PacketExtension</tt>s that
     * this element contains.
     *
     * @param extension the new <tt>PacketExtension</tt>s that we need to
     * add to this element.
     */
    public void addExtensions(PacketExtension extension)
    {
        childExtensions.add(extension);
    }

    /**
     * Returns the XML representation of the jingle content packet extension
     * including all child elements.
     *
     * @return this packet extension as an XML <tt>String</tt>.
     */
    public String toXML()
    {
        StringBuilder bldr = new StringBuilder("<").append(getElementName());


        //add the arguments that we have
        bldr.append(" " + CREATOR_ARG_NAME +"='"+ getCreator() + "'");

        //disposition
        if( getDisposition() != null )
        {
            bldr.append(" " + DISPOSITION_ARG_NAME
                            +"='"+ getDisposition() + "'");
        }

        //name
        bldr.append(" " + NAME_ARG_NAME +"='"+ getName() + "'");

        //senders
        if( getSenders() != null )
        {
            bldr.append(" " + SENDERS_ARG_NAME
                            +"='"+ getSenders() + "'");
        }

        bldr.append(">");


        //other child elements
        synchronized (childExtensions)
        {
            for(PacketExtension ext : childExtensions)
            {
                bldr.append(ext.toXML());
            }
        }

        bldr.append("</"+getElementName()+">");
        return bldr.toString();
    }

}