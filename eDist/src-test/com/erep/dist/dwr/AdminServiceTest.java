package com.erep.dist.dwr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.jdo.PersistenceManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.erep.dist.PMF;
import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.servlets.InstallHelper;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

public class AdminServiceTest {

	private final LocalServiceTestHelper	helper	= new LocalServiceTestHelper(
															new LocalDatastoreServiceTestConfig());

	private final AdminService				service	= new AdminService();

	@Before
	public void setUp() throws Exception {
		helper.setUp();
		PersistenceManager pm = PMF.getNewPersistenceManager();
		pm.makePersistent(InstallHelper.newDefaultConfig());
		pm.makePersistent(InstallHelper.newDefaultGroup());
	}

	@After
	public void tearDown() throws Exception {
		helper.tearDown();
	}

	@Test
	public void testSaveDistributor() {

		assertFalse(service.saveDistributor(new Distributor()));

		assertEquals(1, service.listDistributors().size());

		Distributor dist = new Distributor();
		dist.setEmail("test@test.com");
		dist.setName("Test Distributor");
		dist.setPassword("123456789");

		assertTrue(service.saveDistributor(dist));

		assertEquals(2, service.listDistributors().size());

		dist = service.listDistributors().get(0);
		dist.setName("Edited Test Distributor");
		assertTrue(service.saveDistributor(dist));

		assertEquals(2, service.listDistributors().size());
		assertEquals("Edited Test Distributor",
				service.listDistributors().get(0).getName());

	}

	@Test
	public void testListDistributors() {
		assertEquals(1, service.listDistributors().size());

		for (int i = 0; i < 21; i++) {
			Distributor dist = new Distributor();
			dist.setEmail("123");
			dist.setPassword("123");
			assertTrue(service.saveDistributor(dist));
		}

		assertEquals(22, service.listDistributors().size());

		Distributor dist = new Distributor();
		dist.setEmail("test@test.com");
		dist.setName("Test Distributor");
		dist.setPassword("123456789");

		assertTrue(service.saveDistributor(dist));

		dist = service.listDistributors().get(22);
		assertEquals("test@test.com", dist.getEmail());
		assertEquals("Test Distributor", dist.getName());
		assertNull(dist.getPassword());

	}

	@Test
	public void testSaveGroup() {

		PlayerGroup group = new PlayerGroup();
		group.setGroupName("Test Group");
		DistConfig config = new DistConfig();
		config.setAutoSend(true);
		config.setCurrentMaxDailyFF(160);
		group.setConfig(config);

		assertTrue(service.saveGroup(group));

		assertEquals(1, service.listGroups().size());
		DistConfig config2 = service.listGroups().get(0).getConfig();
		assertNotNull(config2);
		assertEquals(new Integer(160), config2.getCurrentMaxDailyFF());
		assertNotNull(config2.getAutoSend());
		assertTrue(config2.getAutoSend());

		group = service.listGroups().get(0);
		group.setGroupName("Edited Test Group");

		DistConfig config3 = new DistConfig();
		config2.setAutoSend(false);
		group.setConfig(config3);

		assertTrue(service.saveGroup(group));

		assertEquals(1, service.listGroups().size());
		assertEquals("Edited Test Group", service.listGroups().get(0)
				.getGroupName());
		assertFalse(service.listGroups().get(0).getConfig().getAutoSend());

	}

	@Test
	public void testListGroups() {
		for (int i = 0; i < 30; i++) {
			PlayerGroup group = new PlayerGroup();
			DistConfig config = new DistConfig();
			config.setCurrentMaxDailyFF(160);
			group.setConfig(config);
			group.setGroupName("Test Group");
			assertTrue(service.saveGroup(group));
		}

		assertEquals(30, service.listGroups().size());

		PlayerGroup group = service.listGroups().get(0);
		assertNotNull(group);
		assertNotNull(group.getConfig());
		assertEquals(new Integer(160), group.getConfig().getCurrentMaxDailyFF());
		assertNull(group.getPlayers());

	}

	@Test
	public void testAddPlayer() {
		Player player = new Player();
		player.setId(5313713l);

		assertTrue(service.addPlayer(player));
		assertEquals(1, service.listPlayers(null).getResults().size());

	}

	@Test
	public void testEditPlayer() {
		fail("Not yet implemented");
	}

	@Test
	public void testListPlayers() {
		fail("Not yet implemented");
	}

}
