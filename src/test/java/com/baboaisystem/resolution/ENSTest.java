package com.baboaisystem.resolution;

import com.baboaisystem.TestUtils;
import com.baboaisystem.exceptions.ns.NSExceptionCode;
import com.baboaisystem.exceptions.ns.NamingServiceException;
import com.baboaisystem.resolution.naming.service.NamingServiceType;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

public class ENSTest {
  private static DomainResolution resolution;

  @BeforeAll
  public static void init() {
    resolution = new Resolution();
  }

  @Test
  public void ownerTest() throws NamingServiceException {
    String owner = resolution.getOwner("monkybrain.eth");
    assertEquals("0x842f373409191cff2988a6f19ab9f605308ee462", owner);
  }

  @Test
  public void getRecordsEns() throws Exception {
      TestUtils.expectError(() -> resolution.getRecords("someens.eth", Arrays.asList("crypto.ETH.address")), NSExceptionCode.NotImplemented);
  }

  @Test
  public void errorTest() throws Exception {
    TestUtils.expectError(() -> resolution.getEmail("monkybrain.eth"), NSExceptionCode.RecordNotFound);
    TestUtils.expectError(() -> resolution.getAddress("brad.eth", "btc"), NSExceptionCode.UnsupportedCurrency);
    TestUtils.expectError(() -> resolution.getAddress("unregistered23.eth", "eth"), NSExceptionCode.UnregisteredDomain);
    TestUtils.expectError(() -> resolution.getTokenURI("brad.eth"), NSExceptionCode.NotImplemented);
    TestUtils.expectError(() -> resolution.unhash("0x062c59dccddeb7f5b0f32f3a0ded53b33f90b5ae8ddcc681f4ac5048ab5045da", NamingServiceType.ENS), NSExceptionCode.NotImplemented);
  }

  @Test
  public void addressTest() throws NamingServiceException {
    String addr = resolution.getAddress("brad.eth", "eth");
    assertEquals("0x1af001667bb945d1bdbb05145eea7c21d86737f7", addr);

    addr = resolution.getAddress("monkybrain.eth", "EtH");
    assertEquals("0x842f373409191cff2988a6f19ab9f605308ee462", addr);
  }

  @Test
  public void getAllRecords() throws Exception {
    TestUtils.expectError(() -> resolution.getAllRecords("monkybrain.eth"), NSExceptionCode.NotImplemented);
  }
}