<html>

<head>
    <title>Project Funding</title>
    <style>
        button {
            font-family: "'Microsoft YaHei','Helvetica Neue',Helvetica,Arial,sans-serif";
            font-size: 25px !important;
            height: 60px;
            width: 300px;
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

        input, textarea {
            outline-style: none;
            border: 0px;
            border-radius: 3px;
            padding: auto;
            width: 620px;
            font-size: 25px;
            background-color: #181a1b;
            font-family: Microsoft YaHei;
        }
    </style>
</head>

<body topmargin="50" style="background-color: #282828;">
    <div style="background-image: linear-gradient(-90deg, #29bdd9 0%, #276ace 100%);">
        <h1 style="text-align:center;font-size:60px;color:#FFFFFF;font-family:Microsoft YaHei">Project Funding</h1>
        <h2 style="font-size:40px;color:#FFFFFF;font-family:Microsoft YaHei">Funding Request</h2>
    </div>

    <form action="StarterGUI?action=StartProject" method="POST">
        <span style="font-size:30px;text-align:center;color:#6365B0;font-family:Microsoft YaHei">
            Project Name:&nbsp;&nbsp;<input type="text" name="name" style="color:#FFFFFF;" required />
        </span>
        <br />
        <span style="font-size:30px;color:#B06380;font-family:Microsoft YaHei">
            Project Description:<br />
            <textarea name="desc" style="color:#FFFFFF;width: 600px;height: 100px;" required></textarea>
        </span>
        <br />
        <span style="color:#E27D6E;font-size:30px;font-family:Microsoft YaHei">
            Target Amount:&nbsp;&nbsp;<input type="number" name="target" min="0" step="0.01" style="text-align:end;color:#FFFFFF;" required />&euro;
        </span>
        <br />
        <span style="color:#DDA299;font-size:30px;font-family:Microsoft YaHei">
            End Date:&nbsp;&nbsp;<input type="date" name="endDate" style="color:#FFFFFF;" required />
        </span>
        <br />
        <span style="color:#FCBF3D;font-size:30px;font-family:Microsoft YaHei">
            Rewards(optional):
            <br />
            &diams;<input type="number" name="tier0" style="text-align:end;color:#FFFFFF;" />&euro;:&nbsp;
            <input type="text" name="reward0" style="color:#FFFFFF;" /><br />
            &diams;<input type="number" name="tier1" style="text-align:end;color:#FFFFFF;" />&euro;:&nbsp;
            <input type="text" name="reward1" style="color:#FFFFFF;" /><br />
            &diams;<input type="number" name="tier2" style="text-align:end;color:#FFFFFF;" />&euro;:&nbsp;
            <input type="text" name="reward2" style="color:#FFFFFF;" /><br />
            &diams;<input type="number" name="tier3" style="text-align:end;color:#FFFFFF;" />&euro;:&nbsp;
            <input type="text" name="reward3" style="color:#FFFFFF;" /><br />
            &diams;<input type="number" name="tier4" style="text-align:end;color:#FFFFFF;" />&euro;:&nbsp;
            <input type="text" name="reward4" style="color:#FFFFFF;" /><br />
        </span>
        <span style="color:#63AAB0;font-size:30px;font-family:Microsoft YaHei">
            Email:&nbsp;&nbsp;<input type="email" name="email" style="color:#FFFFFF;" required />
        </span>
        <br />
        <span style="color:#6389B0;font-size:30px;font-family:Microsoft YaHei">
            Payment Information:&nbsp;&nbsp;<input type="text" name="paymentInfo" style="color:#FFFFFF;" required />
        </span>
        <br />
        <button style="width:300px;color:#FFFFFF;">Submit</button>
    </form>
    <br />
    <form action="StarterGUI?action=back" method="POST">
        <button>Home</button>
    </form>
</body>

</html>