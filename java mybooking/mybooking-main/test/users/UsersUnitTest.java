package users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsersUnitTest
{

    Users Default;

    @BeforeEach
    void setUp()
    {
        Default = new Users();
    }

    @Test
    void roleVerifier()
    {
        assertEquals(Default.RoleVerifier("admin"), Users.Role.ADMIN);
        assertEquals(Default.RoleVerifier("provider"), Users.Role.PROVIDER);
        assertEquals(Default.RoleVerifier("customer"), Users.Role.CUSTOMER);
        assertEquals(Default.RoleVerifier(""), Users.Role.NOROLE);
    }

    @Test
    void setGetMessage()
    {
        Default.setMessages("This is a message");
        assertEquals("This is a message", Default.getMessages());
    }

    @Test
    void register()
    {
        assertFalse(Default.Register("George", "password", "admin", "male", ""));
        assertTrue(Default.Register("Nikos", "password", "customer", "male", ""));
        assertTrue(Default.Register("Sofia", "password", "provider", "female", "Sofia Apartments"));
        assertTrue(Default.Register("George", "password", "provider", "male", "Golden Star Hotel"));
        assertFalse(Default.Register("Isabella", "password", "provide", "female", "Luxury Suits"));
        assertTrue(Default.Register("Isabella", "password", "provider", "female", "Luxury Suits"));
        assertTrue(Default.Register("George", "password", "provider", "male", "Golden Star Hotel"));
    }

    @Test
    void checkActivation()
    {
        Providers provider = new Providers("Maria", "Slender", "provider", "female", "Acropolis Palace");
        assertFalse(provider.accountStatus());
        provider.activate();
        assertTrue(provider.accountStatus());
    }

}