package com.ufoscout.coreutils.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class RoleEncoderTest extends BaseTest{

    @Test
    public void shouldEncode01() {
        RolesEncoder service = service(() -> Arrays.asList(
                new Role(0, "ADMIN", new String[0]),
                new Role(1, "USER", new String[0])
        ));

        assertEquals(1l, service.encode("ADMIN"));
        assertEquals(2l, service.encode("USER"));
        assertEquals(3l, service.encode("ADMIN", "USER"));
        assertEquals(3l, service.encode("ADMIN", "USER", "ADMIN", "USER"));
        assertEquals(0l, service.encode("ADMIN_WRONG"));
    }

    @Test
    public void shouldEncode02() {
        RolesEncoder service = service(() -> Arrays.asList(
                new Role(0, "ADMIN", new String[0]),
                new Role(2, "USER", new String[0])
        ));

        assertEquals(1l, service.encode("ADMIN"));
        assertEquals(4l, service.encode("USER"));
        assertEquals(5l, service.encode("ADMIN", "USER"));
    }

    @Test
    public void shouldDecode01() {
        RolesEncoder service = service(() -> Arrays.asList(
                new Role(0, "ADMIN", new String[0]),
                new Role(1, "USER", new String[0])
        ));
        assertEquivalent(new String[]{"ADMIN"}, service.decode(1l));
        assertEquivalent(new String[]{"USER"}, service.decode(2l));
        assertEquivalent(new String[]{"ADMIN","USER"}, service.decode(3l));
        assertEquivalent(new String[0], service.decode(0l));
        assertEquivalent(new String[0], service.decode(1_000_000l));
    }

    private RolesEncoder service(RolesProvider provider) {
        AuthService service = new AuthServiceImpl(provider);
        service.start();
        return service.encoder();
    }

    private void assertEquivalent(String[] expected, List<Role> found) {
        assertEquals(expected.length, found.size());
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], found.get(i).getName());
        }
    }

}