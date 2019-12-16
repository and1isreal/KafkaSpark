package ru.kafkaspark.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class PackageServiceTest {

    private PackageService packageService;

    @Before
    public void init() {
        packageService = new PackageService();
        packageService.getNetworkDevice();
    }

    @After
    public void tearDown() {
        packageService = null;
    }


    @Test
    public void getNetworkDeviceTest() {
        assertTrue(packageService.getPcapNetworkInterfaceList().size() > 0);
        assertNotNull(packageService.getDevice());
    }
}
