/*
 * Jitsi, the OpenSource Java VoIP and Instant Messaging client.
 *
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package net.java.sip.communicator.impl.gui.main.login;

import net.java.sip.communicator.impl.gui.*;
import net.java.sip.communicator.impl.gui.customcontrols.*;
import net.java.sip.communicator.impl.gui.main.*;
import net.java.sip.communicator.impl.gui.main.authorization.*;
import net.java.sip.communicator.service.protocol.*;
import net.java.sip.communicator.util.account.*;
import net.java.sip.communicator.util.account.LoginManager;

/**
 * The <tt>LoginRendererSwingImpl</tt> provides a Swing base implementation of
 * the <tt>LoginRenderer</tt> interface.
 *
 * @author Yana Stamcheva
 */
public class LoginRendererSwingImpl
    implements LoginRenderer
{
    private final MainFrame mainFrame
        = GuiActivator.getUIService().getMainFrame();

    /**
     * Adds the user interface related to the given protocol provider.
     *
     * @param protocolProvider the protocol provider for which we add the user
     * interface
     */
    public void addProtocolProviderUI(ProtocolProviderService protocolProvider)
    {
        GuiActivator.getUIService()
            .getMainFrame().addProtocolProvider(protocolProvider);
    }

    /**
     * Removes the user interface related to the given protocol provider.
     *
     * @param protocolProvider the protocol provider to remove
     */
    public void removeProtocolProviderUI(
        ProtocolProviderService protocolProvider)
    {
        this.mainFrame.removeProtocolProvider(protocolProvider);
    }

    /**
     * Starts the connecting user interface for the given protocol provider.
     *
     * @param protocolProvider the protocol provider for which we add the
     * connecting user interface
     */
    public void startConnectingUI(ProtocolProviderService protocolProvider)
    {
        mainFrame.getAccountStatusPanel().startConnecting(protocolProvider);
    }

    /**
     * Stops the connecting user interface for the given protocol provider.
     *
     * @param protocolProvider the protocol provider for which we remove the
     * connecting user interface
     */
    public void stopConnectingUI(ProtocolProviderService protocolProvider)
    {
        mainFrame.getAccountStatusPanel().stopConnecting(protocolProvider);
    }

    /**
     * Indicates that the given protocol provider is now connected.
     *
     * @param protocolProvider the <tt>ProtocolProviderService</tt> that is
     * connected
     * @param date the date on which the event occured
     */
    public void protocolProviderConnected(
        ProtocolProviderService protocolProvider, long date)
    {
        OperationSetPresence presence
            = AccountStatusUtils.getProtocolPresenceOpSet(protocolProvider);

        OperationSetMultiUserChat multiUserChat =
            mainFrame.getMultiUserChatOpSet(protocolProvider);

        if (presence != null)
        {
            presence.setAuthorizationHandler(new AuthorizationHandlerImpl(
                mainFrame));
        }

        if(multiUserChat != null)
        {
            GuiActivator.getUIService().getConferenceChatManager()
                .getChatRoomList().synchronizeOpSetWithLocalContactList(
                    protocolProvider, multiUserChat);
        }
    }

    /**
     * Indicates that a protocol provider connection has failed.
     *
     * @param protocolProvider the <tt>ProtocolProviderService</tt>, which
     * connection failed
     * @param loginManagerCallback the <tt>LoginManager</tt> implementation,
     * which is managing the process
     */
    public void protocolProviderConnectionFailed(
        ProtocolProviderService protocolProvider,
        LoginManager loginManagerCallback)
    {
        AccountID accountID = protocolProvider.getAccountID();
        String errorMessage = GuiActivator.getResources().getI18NString(
            "service.gui.LOGIN_NETWORK_ERROR",
            new String[]
               { accountID.getUserID(), accountID.getService() });

        int result =
            new MessageDialog(
                null,
                GuiActivator.getResources()
                    .getI18NString("service.gui.ERROR"),
                errorMessage,
                GuiActivator.getResources()
                    .getI18NString("service.gui.RETRY"), false)
            .showDialog();

        if (result == MessageDialog.OK_RETURN_CODE)
        {
            loginManagerCallback.login(protocolProvider);
        }
    }

    /**
     * Returns the <tt>SecurityAuthority</tt> implementation related to this
     * login renderer.
     *
     * @param protocolProvider the specific <tt>ProtocolProviderService</tt>,
     * for which we're obtaining a security authority
     * @return the <tt>SecurityAuthority</tt> implementation related to this
     * login renderer
     */
    public SecurityAuthority getSecurityAuthorityImpl(
        ProtocolProviderService protocolProvider)
    {
        return GuiActivator.getUIService()
                .getDefaultSecurityAuthority(protocolProvider);
    }

    /**
     * Indicates if the given <tt>protocolProvider</tt> related user interface
     * is already rendered.
     *
     * @param protocolProvider the <tt>ProtocolProviderService</tt>, which
     * related user interface we're looking for
     * @return <tt>true</tt> if the given <tt>protocolProvider</tt> related user
     * interface is already rendered
     */
    public boolean containsProtocolProviderUI(
        ProtocolProviderService protocolProvider)
    {
        return mainFrame.hasProtocolProvider(protocolProvider);
    }
}
