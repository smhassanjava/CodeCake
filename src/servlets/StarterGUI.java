package servlets;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import components.*;
import data.*;
import utils.*;

@WebServlet("/StarterGUI")
public class StarterGUI extends HttpServlet
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
        throws ServletException, IOException, NumberFormatException, ParseException
    {
        // Get the requested action.
        switch (StringUtils.NonNull(request.getParameter("action")))
        {
            case "":
            case "GoToFundingRequestPage":
                // Show the funding request page.
                request.getRequestDispatcher(WebConfig.GetWebpage("FundingRequestPage")).forward(request, response);
                break;

            case "StartProject":
                // Retrieve all non-empty tuples of reward field data.
                ArrayList<Reward> rewards = new ArrayList<>();
                for (int i = 0; i < 5; i++)
                {
                    String tier = request.getParameter("tier" + i);
                    String reward = request.getParameter("reward" + i);
                    if (!StringUtils.IsNullOrEmpty(tier) && !StringUtils.IsNullOrWhitespace(reward))
                    {
                        rewards.add(new Reward(Double.parseDouble(tier), reward));
                    }
                }
                // Submit the entered data and check for validity.
                if (PF_Application.GetInstance().SubmitProject(
                    request.getParameter("email"),
                    new PaymentInfo(request.getParameter("paymentInfo")),
                    request.getParameter("name"),
                    request.getParameter("desc"),
                    Double.parseDouble(request.getParameter("target")),
                    new DateTime(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endDate")).getTime()),
                    rewards.toArray(new Reward[rewards.size()])))
                {
                    // Input valid.
                    request.getRequestDispatcher(WebConfig.GetWebpage("ProjectStarted")).forward(request, response);
                }
                else
                {
                    // Input invalid.
                    request.getRequestDispatcher(WebConfig.GetWebpage("ProjectStartFail")).forward(request, response);
                }
                break;

            case "ConfirmProject":
                // Forward project confirmation and show the default page.
                PF_Application.GetInstance().ForwardConfirmProject(Integer.parseInt(request.getParameter("pId")));
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectListPage")).forward(request, response);
                break;

            case "back":
                // Show the default page.
                request.getRequestDispatcher(WebConfig.GetWebpage("ProjectListPage")).forward(request, response);
                break;
        }
    }
}
