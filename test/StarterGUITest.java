import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import org.junit.*;
import data.*;

public class StarterGUITest
{
    @Before
    public void setup()
    {
        // Set up the test environment.
        setBaseUrl(WebConfig.GetUrl());
    }

    @Test
    public void testStartProject()
    {
        // Test GoToFundingRequestPage.
        beginAt("StarterGUI?action=GoToFundingRequestPage");
        assertFormPresent();
        assertFormElementPresent("name");
        assertFormElementPresent("desc");
        assertFormElementPresent("target");
        assertFormElementPresent("endDate");
        assertFormElementPresent("email");
        assertFormElementPresent("paymentInfo");
        assertButtonPresentWithText("Submit");
        assertButtonPresentWithText("Home");
        
        // Test StartProject
        setTextField("name", "");
        setTextField("desc", "aaaaaaaa");
        setTextField("target", "5000.0");
        setTextField("endDate", "2020-06-01");
        setTextField("email", "aaa@email.com");
        setTextField("paymentInfo", "#aaa");
        clickButtonWithText("Submit");
        assertTextNotPresent("Funding request submitted.");
        assertTextPresent("Funding request could not be submitted.");
    }
}