package servlets;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import components.*;
import data.*;
import utils.*;

@WebServlet("/SupporterGUI")
public class SupporterGUI extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            // Process GET and POST messages.
            ProcessMessage(request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            // Process GET and POST messages.
            ProcessMessage(request, response);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static void ProcessMessage(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        // Get the requested action.
        switch (StringUtils.NonNull(request.getParameter("action")))
        {
            case "StartDonation":
                // Submit the entered data and check for validity.
                if (PF_Application.GetInstance().SubmitDonation(
                    Integer.parseInt(request.getParameter("pId")),
                    request.getParameter("email"),
                    new PaymentInfo(request.getParameter("paymentInfo")),
                    Double.parseDouble(request.getParameter("amount"))))
                {
                    // Input valid.
                    request.getRequestDispatcher(WebConfig.GetWebpage("DonationStarted")).forward(request, response);
                }
                else
                {
                    // Input invalid.
                    request.getRequestDispatcher(WebConfig.GetWebpage("DonationStartFail")).forward(request, response);
                }
                break;

            case "GetProjectList":
                // Show the list of projects with the specified status.
                request.setAttribute("projects",
                    PF_Application.GetInstance().RequestProjectList(Status.valueOf(request.getParameter("status"))));
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectListPage")).forward(request, response);
                break;

            case "GetProjectDescription":
                // Show the project description.
                Project p = PF_Application.GetInstance().RequestProjectDescription(Integer.parseInt(request.getParameter("pId")));
                request.setAttribute("pId", p.pId);
                request.setAttribute("name", p.name);
                request.setAttribute("desc", p.desc);
                request.setAttribute("current", p.current);
                request.setAttribute("target", p.target);
                request.setAttribute("endDate", new Date(p.endDate.value));
                request.setAttribute("status", p.status.name());
                request.setAttribute("rewards", p.rewards);
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectDescriptionPage")).forward(request, response);
                break;

            case "GoToProjectDonationPage":
                // Show the project donation page.
                p = PF_Application.GetInstance().RequestProjectDescription(Integer.parseInt(request.getParameter("pId")));
                request.setAttribute("pId", p.pId);
                request.setAttribute("name", p.name);
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectDonationPage")).forward(request, response);
                break;

            case "ConfirmDonation":
                // Forward donation confirmation and show the default page.
                PF_Application.GetInstance().ForwardConfirmDonation(Integer.parseInt(request.getParameter("dId")));
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectListPage")).forward(request, response);
                break;

            case "":
            case "back":
                // Show the default page.
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectListPage")).forward(request, response);
                break;
        }
    }
}
