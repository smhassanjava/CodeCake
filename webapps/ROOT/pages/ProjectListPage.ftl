<!DOCTYPE HTML>
<html>

<head>
    <title>Project Funding</title>
    <style>
        button {
            font-family: "'Microsoft YaHei','Helvetica Neue',Helvetica,Arial,sans-serif";
            font-size: 25px !important;
            line-height: 18px !important;
            padding: 3px 18px;
            display: inline-block;
            vertical-align: middle;
            font-weight: normal;
            border-radius: 3px;
            margin: 0 8px 0 3px;
            border: 1px solid #3383da;
            color: #ffffff;
            background-color: #3383da;
            cursor: pointer;
        }

        button:hover {
            background-color: #1ABC9C;
        }

        input {
            outline-style: none;
            border: 0px;
            border-radius: 3px;
            padding: auto;
            width: 300px;
            font-size: 25px;
            background-color: #181a1b;
        }
    </style>
</head>

<body topmargin="50" style="background-color: #282828;">
    <div style="background-image: linear-gradient(to right, #ff9569 0%, #e92758 100%);">
        <h1 style="text-align:center;font-size:60px;">Project Funding</h1>
        <form style="text-align:center;" action="StarterGUI?action=GoToFundingRequestPage" method="POST">
            <button style="height: 60px;">Start Funding Request</button>
		    <br/><br/>
        </form>
    </div>
    <br /><br /><br /><br />
    <p style="text-align:center;color:#FFB442;font-size:40px;font-family:Microsoft YaHei">Search projects</p>
    <form style="text-align:center;" action="SupporterGUI?action=GetProjectList" method="POST">
        <select name="status" style="color:#FFFFFF;font-family:Microsoft YaHei;background-color: #181a1b;height:30;">
            <option value="Open" selected>Open</option>
            <option value="Successful">Successful</option>
            <option value="Failed">Failed</option>
            <option value="Cancelled">Cancelled</option>
        </select>
        <button style="height: 25px;color:#FFFFFF;">Search</button>
    </form>
    <br />
    <#if projects??>
        <#list projects>
            <table cellpadding="10" style="color:#FFFFFF;font-size: 20px;">
                <tr style="color: #cccccc;">
                    <th style="text-align:left;">Name</th>
                    <th style="text-align:left;">Description</th>
                    <th style="text-align:left;">Current/Target</th>
                    <th style="text-align:left;">&nbsp;</th>
                </tr>
                <#items as p>
                    <tr>
                        <td>${p.name}</td>
                        <td>${p.desc?truncate(30)}</td>
                        <td>${p.current}&euro;/${p.target}&euro;</td>
                        <td>
                            <form action="SupporterGUI?action=GetProjectDescription" method="POST">
                                <button name="pId" value="${p.pid}">Details</button>
                            </form>
                        </td>
                    </tr>
                </#items>
            </table>
        </#list>
    </#if>
</body>

</html>