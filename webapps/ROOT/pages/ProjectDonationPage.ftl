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
        <h2 style="font-size:40px;">Donation</h2>
    </div>
    <h3 style="font-size:20px;">${name}</h3>
    <form action="SupporterGUI?action=StartDonation" method="POST">
        <span style="font-size:30px;font-family:Microsoft YaHei">
            <span style="color:#E27D6E;">
                Email:&nbsp;&nbsp;<input type="email" name="email" style="color:#FFFFFF;" required />
            </span>
            <br />
            <span style="color:#DDA299;">
                Payment Information:&nbsp;&nbsp;<input type="text" name="paymentInfo" style="color:#FFFFFF;" required />
            </span>
            <br />
            <span style="color:#FCBF3D;">
                Donation Amount:&nbsp;&nbsp;<input type="number" name="amount" min="0" step="0.01" style="text-align:end;color:#FFFFFF;" required />&euro;
            </span>
            <br />
        </span>
        <button name="pId" value="${pId}" style="width:300px;color:#FFFFFF;">Submit</button>
    </form>
    <form action="StarterGUI?action=back" method="POST">
        <button>Home</button>
    </form>
</body>

</html>