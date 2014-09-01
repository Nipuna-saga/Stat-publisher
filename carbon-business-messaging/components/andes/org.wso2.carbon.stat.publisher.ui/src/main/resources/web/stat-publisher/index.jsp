<!--
~ Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
~
~ WSO2 Inc. licenses this file to you under the Apache License,
~ Version 2.0 (the "License"); you may not use this file except
~ in compliance with the License.
~ You may obtain a copy of the License at
~
~ http://www.apache.org/licenses/LICENSE-2.0
~
~ Unless required by applicable law or agreed to in writing,
~ software distributed under the License is distributed on an
~ "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
~ KIND, either express or implied. See the License for the
~ specific language governing permissions and limitations
~ under the License.
-->

<%@ page import="org.apache.axis2.context.ConfigurationContext" %>
<%@ page import="org.wso2.carbon.CarbonConstants" %>
<%@ page import="org.wso2.carbon.andes.ui.Constants" %>
<%@ page import="org.wso2.carbon.stat.publisher.internal.data.xsd.StatConfiguration" %>
<%@ page import="org.wso2.carbon.stat.publisher.ui.StatPublisherClient" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIMessage" %>
<%@ page import="org.wso2.carbon.ui.CarbonUIUtil" %>
<%@ page import="org.wso2.carbon.utils.ServerConstants" %>





<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://wso2.org/projects/carbon/taglibs/carbontags.jar" prefix="carbon" %>
<script type="text/javascript" src="js/toggle.js"></script>


<fmt:bundle basename="org.wso2.carbon.stat.publisher.ui.i18n.Resources">

<%
    String serverURL = CarbonUIUtil.getServerURL(config.getServletContext(), session);
    ConfigurationContext configContext =
            (ConfigurationContext) config.getServletContext().getAttribute(CarbonConstants.CONFIGURATION_CONTEXT);
    String cookie = (String) session.getAttribute(ServerConstants.ADMIN_SERVICE_COOKIE);

    StatPublisherClient client;
    StatConfiguration statConfigurationGetObject;


    try {
        client = new StatPublisherClient(configContext, serverURL, cookie);
        statConfigurationGetObject = client.getStatConfiguration();

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
    String enable_checked_value = "";

    String get_username_value = "";
    String get_password_value = "";
    String get_URL_value = "";

    String message_stat_check_value = "";
    String system_stat_check_value = "";
    String MB_stat_check_value = "";


    if (statConfigurationGetObject != null) {


        if (statConfigurationGetObject.getEnableStatPublisher()) {
            enable_checked_value = "checked";
        }

        get_username_value = statConfigurationGetObject.getUsername();
        get_password_value = statConfigurationGetObject.getPassword();
        get_URL_value = statConfigurationGetObject.getURL();


        if (statConfigurationGetObject.getMB_statEnable()) {
            MB_stat_check_value = "checked";

        }

        if (statConfigurationGetObject.getMessage_statEnable()) {
            message_stat_check_value = "checked";

        }
        if (statConfigurationGetObject.getSystem_statEnable()) {
            system_stat_check_value = "checked";

        }


    }

    if (!(request.getAttribute("servlet_resp") == null)) {

        response_message = (String) request.getAttribute("servlet_resp");

%>
<script type="text/javascript">
    alertMessage("<%=response_message%>");

</script>

<%


    }

%>

<div id="middle">
    <h2><fmt:message key="mb.stat.publisher"/></h2>

    <div id="workArea">

        <form id="details_form" action="/carbon/stat-publisher/statConfigurationServlet" method="POST"
              onsubmit="return DoValidation();">
            <table class="styledLeft" style="width: 20%">

                <tbody>
                <tr>
                    <td colspan="5">Enable Publisher</td>
                    <td><input type="checkbox" id="enable_check" name="enable_check" value="true"
                               onclick="toggleTable();" <%=enable_checked_value%>/></td>
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
                        <td>&nbsp<input type="text" id="username" name="username" value="<%=get_username_value%>"/></td>
                    </tr>
                    <tr>
                        <td class="formRaw"><fmt:message key="password"/><span
                                class="required">*</span></td>
                        <td>&nbsp<input type="password" id="password" name="password" value="<%=get_password_value%>"/>
                        </td>
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
                        <td><input type="text" id="url_address" name="url_address" value="<%=get_URL_value%>"/>
                            <input type="button" class="button" id="testBut" value="<fmt:message key="test"/>"
                                   onclick="validateURL()"/>
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
                                   value="true" <%=message_stat_check_value%>/>
                        </td>
                        <td colspan="3">System</td>
                        <td>
                            <input type="checkbox" id="system_stat_check" name="system_stat_check"
                                   value="true" <%=system_stat_check_value%>/>
                        </td>
                        <td colspan="3">MB</td>
                        <td>
                            <input type="checkbox" id="MB_stat_check" name="MB_stat_check" value="true"
                                   value="true" <%=MB_stat_check_value%>/>
                        </td>

                    </tr>

                </table>
                <br>
            </div>
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
<script type="text/javascript" src="js/toggle.js"></script>
</fmt:bundle>