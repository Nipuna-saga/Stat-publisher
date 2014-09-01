<%@ page import="org.wso2.carbon.andes.ui.Constants" %>
<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>
<%@ page import="org.wso2.carbon.andes.stub.AndesAdminServiceStub" %>
<%@ page import="org.wso2.carbon.andes.stub.admin.types.QueueRolePermission" %>
<%@ page import="org.wso2.carbon.andes.ui.UIUtils" %>
<%@ page import="org.wso2.carbon.andes.stub.AndesAdminServiceBrokerManagerAdminException" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>




<%@ page import="org.wso2.carbon.stat.publisher.ui.StatPublisherClient" %>
<%@ page import="org.wso2.carbon.stat.publisher.data.xsd.StatConfiguration" %>
<script type="text/javascript" src="../ajax/js/prototype.js"></script>
<script type="text/javascript" src="../resources/js/resource_util.js"></script>
<!--Yahoo includes for dom event handling-->
<script src="../yui/build/yahoo-dom-event/yahoo-dom-event.js" type="text/javascript"></script>

<script src='//dgdsbygo8mp3h.cloudfront.net/sites/default/files/blank.gif'
        data-original="pathToBuild/yahoo/yahoo-min.js"
        type="text/javascript"></script>
<script src='//dgdsbygo8mp3h.cloudfront.net/sites/default/files/blank.gif'
        data-original="pathToBuild/event/event-min.js"
        type="text/javascript"></script>
<script src='//dgdsbygo8mp3h.cloudfront.net/sites/default/files/blank.gif'
        data-original="pathToBuild/connection/connection_core-min.js"
        type="text/javascript"></script>


<script src='//dgdsbygo8mp3h.cloudfront.net/sites/default/files/blank.gif'
        data-original="pathToBuild/connection/connection-min.js"
        type="text/javascript"></script>

<!--Yahoo includes for animations-->
<script src="../yui/build/animation/animation-min.js" type="text/javascript"></script>

<!--Yahoo includes for menus-->
<link rel="stylesheet" type="text/css" href="../yui/build/menu/assets/skins/sam/menu.css"/>
<script type="text/javascript" src="../yui/build/container/container_core-min.js"></script>
<script type="text/javascript" src="../yui/build/menu/menu-min.js"></script>

<!--EditArea javascript syntax hylighter -->
<script language="javascript" type="text/javascript" src="../editarea/edit_area_full.js"></script>

<!--Local js includes-->

<script type="text/javascript" src="js/toggle.js"></script>

<link href="css/tree-styles.css" media="all" rel="stylesheet"/>
<link href="css/dsxmleditor.css" media="all" rel="stylesheet"/>
<fmt:bundle basename="org.wso2.carbon.andes.dataAgent.ui.i18n.Resources">

<%
    String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

    StatPublisherClient client;
    boolean status;

    StatConfiguration statConfiguration;

    String ip_address = "";
    int port_num = 0;
    try {
        client = new StatPublisherClient(configContext, serverURL, cookie);

    } catch (Exception e) {
        CarbonUIMessage.sendCarbonUIMessage(e.getMessage(), CarbonUIMessage.ERROR, request, e);
%>
<script type="text/javascript">
    location.href = "../admin/error.jsp";
</script>


<%
        return;
    }

%>

<%

    String response_message = "";
    String enable_checked = "";
    String get_username = "";
    String get_password = "";
    //String get_IP = "";
    //String get_port = "";
    String message_stat_check = "";
    String system_stat_check = "";
    String MB_stat_check = "";


    if (true) {

/*
               enable_checked = "checked";
               get_username = client.getUsername(publisher_name);
               get_password = client.getPassword(publisher_name);
               get_IP = client.getIP(publisher_name);
               get_port = client.getPort(publisher_name);

               if (client.getMBStatConfig(publisher_name)) {
                   MB_stat_check = "checked";

               }

               if (client.getMessageStatConfig(publisher_name)) {
                   message_stat_check = "checked";

               }
               if (client.getSystemStatConfig(publisher_name)) {
                   system_stat_check = "checked";

               }

*/
    }
/*
       if (!(request.getAttribute("servlet_resp") == null)) {

           response_message = (String) request.getAttribute("servlet_resp");

   %>
<script type="text/javascript">
    alertMessage("<%=response_message%>");

</script>

<%


    }
    */
%>
<div id="middle">
    <div id="workArea">
        <h2>
            <fmt:message key="mb.stat.publisher"/>
        </h2>

        <form id="details_form" action="/carbon/publisher/dataAgentServlet" method="POST" onsubmit="">
            <table class="styledLeft" style="width: 20%">

                <tbody>
                <tr>
                    <td colspan="5">Enable Publisher</td>
                    <td><input type="checkbox" id="enable_check" name="enable_check" value="true"
                               onclick="toggleTable();"  ></td>
                </tr>
                </tbody>
            </table>


            <br><br>

            <div id="toggle">
                <table class="styledLeft" style="width: 100%" id="authentication_table">
                    <col width=20%>
                    <col width=30%>
                    <col width=50%>
                    <thead>
                    <tr>
                        <th colspan="2">Authentication</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="formRaw"><fmt:message key="username"/><span
                                class="required">*</span></td>
                        <td>&nbsp<input type="text" id="username" name="username" value=""/></td>
                    </tr>
                    <tr>
                        <td class="formRaw"><fmt:message key="password"/><span
                                class="required">*</span></td>
                        <td>&nbsp<input type="password" id="password" name="password" value=""/></td>
                    </tr>
                </table>

                <br><br>

                <table class="styledLeft" style="width: 100%" id="transport_table">
                    <col width=20%>
                    <col width=30%>
                    <col width=50%>
                    <thead>
                    <tr>
                        <th colspan="2">Transport</th>
                    </tr>
                    </thead>
                    <tbody>

                    <tr>
                        <td class="formRaw"><fmt:message key="url"/><span
                                class="required">*</span></td>
                        <td><input type="text" id="url_address" name="url_address" value="<%=%>"/>
                            <input type="button" class="button" id="testBut" value="<fmt:message key="test"/>" onclick="validateURL()"/>
                        </td>

                    </tr>

                </table>
                <br><br>
                <table class="styledLeft" style="width: 50%" id="stat_table">
                    <col width=10%>
                    <col width=10%>
                    <col width=10%>

                    <thead>
                    <tr>
                        <th colspan="12">Statistic Configuration</th>
                    </tr>


                    </thead>
                    <tr>
                        <td colspan="3">Message</td>
                        <td>
                            <input type="checkbox" id="message_stat_check" name="message_stat_check"
                                   value="true" />
                        </td>
                        <td colspan="3">System</td>
                        <td>
                            <input type="checkbox" id="system_stat_check" name="system_stat_check"
                                   value="true" />
                        </td>
                        <td colspan="3">MB</td>
                        <td>
                            <input type="checkbox" id="MB_stat_check" name="MB_stat_check" value="true"
                                   value="true" />
                        </td>

                    </tr>

                </table>
                <br>

                <table class="styledLeft" style="width: 50%" id="button_table">
                    <tr>

                        <td class="buttonRow"><input type="submit" class="button" id="saveButton"
                                                     name="saveButton"
                                                     value="<fmt:message key="save"/>"
                                />
                            <input type="submit" class="button" id="resetButton"
                                   name="saveButton"
                                   value="<fmt:message key="reset"/>"
                                    />
                        </td>

                    </tr>
                </table>

        </form>
    </div>
</div>
</div>



<script type="text/javascript" src="js/toggle.js"></script>
</fmt:bundle>