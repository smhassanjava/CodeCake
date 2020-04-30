import static org.mockito.Mockito.*;
import java.util.*;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.*;
import org.powermock.core.classloader.annotations.*;
import org.powermock.modules.junit4.*;
import components.*;
import data.*;
import junit.framework.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PF_Application.class)
public class PF_ApplicationTest extends TestCase
{
    private static Project p;
    private static Donation d;
    private EmailAdapter ea;
    private DBFacade db;
    private PaymentService ps;

    @BeforeClass
    public static void init()
    {
        // Initialize static variables.
        p = new Project(0,
            "aaa@email.com",
            new PaymentInfo("#aaa"),
            "Aaa",
            null,
            0.0,
            5000.0,
            new DateTime(new Date().getTime()),
            Status.Open,
            new Reward[0]);
        d = new Donation(0,
            0,
            "bbb@email.net",
            new PaymentInfo("#bbb"),
            300.0);
    }

    @Before
    public void setup()
    {
        // Set up the test environment.
        ea = mock(EmailAdapter.class);
        db = mock(DBFacade.class);
        ps = mock(PaymentService.class);
        EmailAdapter.SetInstance(ea);
        DBFacade.SetInstance(db);
        PaymentService.SetInstance(ps);
    }

    @Test
    public void testSubmitProject()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();
        app.tempProjects.clear();

        // Test for invalid input.
        assertFalse(app.SubmitProject(null, p.paymentInfo, p.name, p.desc, p.target, p.endDate, p.rewards));
        verify(ea, never()).SendProjectConfirmLink(eq(p.email), anyString(), anyInt());
        assertEquals(0, app.tempProjects.size());

        // Test for valid input.
        assertTrue(app.SubmitProject(p.email, p.paymentInfo, p.name, p.desc, p.target, p.endDate, p.rewards));
        verify(ea, times(1)).SendProjectConfirmLink(eq(p.email), anyString(), anyInt());
        assertEquals(1, app.tempProjects.size());
    }

    @Test
    public void testSubmitDonation()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();
        app.tempDonations.clear();

        // Test for invalid input.
        assertFalse(app.SubmitDonation(d.pId, null, d.paymentInfo, d.amount));
        verify(ea, never()).SendDonationConfirmLink(eq(d.email), anyString(), anyInt());
        assertEquals(0, app.tempDonations.size());

        // Test for valid input.
        assertTrue(app.SubmitDonation(d.pId, d.email, d.paymentInfo, d.amount));
        verify(ea, times(1)).SendDonationConfirmLink(eq(d.email), anyString(), anyInt());
        assertEquals(1, app.tempDonations.size());
    }

    @Test
    public void testForwardConfirmProject()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();
        app.tempProjects.clear();
        app.tempProjects.add(p);

        // Test ForwardConfirmProject.
        app.ForwardConfirmProject(p.pId);
        verify(db, times(1)).AddProject(p);
        assertEquals(0, app.tempProjects.size());
    }

    @Test
    public void testForwardConfirmDonation()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();
        app.tempDonations.clear();
        app.tempDonations.add(d);

        // Test ForwardConfirmDonation.
        app.ForwardConfirmDonation(d.dId);
        verify(db, times(1)).AddDonator(d);
        assertEquals(0, app.tempDonations.size());
    }

    @Test
    public void testRequestProjectList()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();

        // Set up return values.
        Project[] expected =
        {
            p,
        };
        when(db.GetProjects(Status.Open)).thenReturn(expected);

        // Test RequestProjectList.
        Project[] result = app.RequestProjectList(Status.Open);
        verify(db, times(1)).GetProjects(Status.Open);
        assertEquals(expected, result);
    }

    @Test
    public void testRequestProjectDescription()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();

        // Set up return values.
        when(db.GetProject(p.pId)).thenReturn(p);

        // Test RequestProjectDescription.
        Project result = app.RequestProjectDescription(p.pId);
        verify(db, times(1)).GetProject(p.pId);
        assertEquals(p, result);
    }

    public void testCheckEndDates()
    {
        // Set up initial state.
        PF_Application app = PF_Application.GetInstance();

        // Set up return values.
        when(db.GetProjectsExpired()).thenReturn(new Project[]
        {
            p
        });
        when(db.GetDonations(p.pId)).thenReturn(new Donation[]
        {
            d
        });

        // Test no changes.
        p.current = 0.0;
        app.CheckEndDates();
        verify(db, times(1)).SetStatus(p.pId, Status.Failed);
        verify(ea, times(1)).NotifyStatusStarter(p.email, p.name, Status.Failed);
        verify(ea, times(1)).NotifyStatusSupporter(d.email, p.name, Status.Failed);
        verify(ps, never()).ChargeDonation(any(), any(), anyDouble());

        // Test one update.
        p.current = p.target;
        app.CheckEndDates();
        verify(db, times(1)).SetStatus(p.pId, Status.Successful);
        verify(ea, times(1)).NotifyStatusStarter(p.email, p.name, Status.Successful);
        verify(ea, times(1)).NotifyStatusSupporter(d.email, p.name, Status.Successful);
        verify(ps, times(1)).ChargeDonation(d.paymentInfo, p.paymentInfo, d.amount);
    }
}
