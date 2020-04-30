import static net.sourceforge.jwebunit.junit.JWebUnit.*;
import org.junit.*;
import data.*;

public class SupporterGUITest
{
    @Before
    public void setup()
    {
        setBaseUrl(WebConfig.GetUrl());
    }

    @Test
    public void testGetProjectList()
    {
        // Test default page.
        beginAt("SupporterGUI");
        assertFormPresent();
        assertButtonPresentWithText("Start Funding Request");
        assertTextPresent("Search projects");
        assertSelectOptionValuesEqual("status", new String[]
        {
            "Open",
            "Successful",
            "Failed",
            "Cancelled",
        });
        assertSelectedOptionValueEquals("status", "Open");
        assertButtonPresentWithText("Search");
        clickButtonWithText("Search");
        assertTextPresent("Search projects");
    }
}