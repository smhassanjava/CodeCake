<html>

<head>
    <title>Project Funding</title>
    <style>
        button {
            font-family: "'Microsoft YaHei','Helvetica Neue',Helvetica,Arial,sans-serif";
            font-size: 25px !important;
            height: 30px;
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
            border: 1px solid #ccc;
            border-radius: 3px;
            padding: auto;
            width: 620px;
            font-size: 25px;
            background-color: #181a1b
        }
    </style>
</head>

<body topmargin="50" style="background-color: #282828;">
    <div style="background-image: linear-gradient(-90deg, #29bdd9 0%, #276ace 100%);">
        <h1 style="text-align:center;font-size:60px;">Project Funding</h1>
        <h2 style="font-size:40px;">${name}</h2>
    </div>
    <span style='font-size:30px;font-family:Microsoft YaHei'>
        <p style="color: #99C4DD;">&diams; Status: ${status}</p>
        <p style="color: #B06380;">&diams; Description: ${desc}</p>
        <p style="color: #DDA299;">&diams; End Date: ${endDate?string("dd-MM-yyyy")}</p>
        <p style="color: #FFB786;">&diams; Current Amount: ${current?string(",##0.00")}&euro;</p>
        <p style="color: #FF7328;">&diams; Target Amount: ${target?string(",##0.00")}&euro;</p>
    </span>
    <#if rewards??>
        <#list rewards>
            <fieldset>
                <legend>Rewards:</legend>
                <ul>
                    <#items as r>
                        <li>${r.tier}&euro;: ${r.reward}</li>
                    </#items>
                </ul>
            </fieldset>
        </#list>
    </#if>
    <form action="SupporterGUI?action=GoToProjectDonationPage" method="POST">
        <button name="pId" value="${pId}" style="width:300px;color:#FFFFFF;">Donate</button>
    </form>
    <form action="StarterGUI?action=back" method="POST">
        <button>Home</button>
    </form>
</body>

</html>