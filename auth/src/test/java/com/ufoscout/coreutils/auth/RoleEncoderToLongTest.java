package com.ufoscout.coreutils.auth;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class RoleEncoderToLongTest extends BaseTest{

    @Test
    public void shouldEncode01() {
        AuthService<Long, Auth<Long>> service = service(() -> Arrays.asList(
                new Role(0, "ADMIN", new String[0]),
                new Role(1, "USER", new String[0])
        ));

        assertEquals(1l, service.encode("ADMIN").longValue());
        assertEquals(2l, service.encode("USER").longValue());
        assertEquals(3l, service.encode("ADMIN", "USER").longValue());
        assertEquals(3l, service.encode("ADMIN", "USER", "ADMIN", "USER").longValue());
        assertEquals(0l, service.encode("ADMIN_WRONG").longValue());
    }

    @Test
    public void shouldEncode02() {
        AuthService<Long, Auth<Long>> service = service(() -> Arrays.asList(
                new Role(0, "ADMIN", new String[0]),
                new Role(2, "USER", new String[0])
        ));

        assertEquals(1l, service.encode("ADMIN").longValue());
        assertEquals(4l, service.encode("USER").longValue());
        assertEquals(5l, service.encode("ADMIN", "USER").longValue());
    }

    @Test
    public void shouldDecode01() {
        AuthService<Long, Auth<Long>> service = service(() -> Arrays.asList(
                new Role(0, "ADMIN", new String[0]),
                new Role(1, "USER", new String[0])
        ));
        assertEquivalent(new String[]{"ADMIN"}, service.decode(1l));
        assertEquivalent(new String[]{"USER"}, service.decode(2l));
        assertEquivalent(new String[]{"ADMIN","USER"}, service.decode(3l));
        assertEquivalent(new String[0], service.decode(0l));
        assertEquivalent(new String[0], service.decode(1_000_000l));
    }

    private AuthService<Long, Auth<Long>> service(RolesProvider provider) {
        AuthService<Long, Auth<Long>> service = new AuthServiceImpl(provider, new RolesEncoderToLong());
        service.start();
        return service;
    }

    private void assertEquivalent(String[] expected, List<Role> found) {
        assertEquals(expected.length, found.size());
        for (int i=0; i<expected.length; i++) {
            assertEquals(expected[i], found.get(i).getName());
        }
    }

}