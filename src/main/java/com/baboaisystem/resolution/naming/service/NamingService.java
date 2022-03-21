package com.baboaisystem.resolution.naming.service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.baboaisystem.config.network.model.Location;
import com.baboaisystem.config.network.model.Network;
import com.baboaisystem.exceptions.dns.DnsException;
import com.baboaisystem.exceptions.ns.NamingServiceException;
import com.baboaisystem.resolution.dns.DnsRecord;
import com.baboaisystem.resolution.dns.DnsRecordsType;

public interface NamingService {
    NamingServiceType getType();

    Boolean isSupported(String domain) throws NamingServiceException;

    Map<String, String> getAllRecords(String domain) throws NamingServiceException;
    String getRecord(String domain, String recordKey) throws NamingServiceException;
    Map<String, String> getRecords(String domain, List<String> recordsKeys) throws NamingServiceException;

    String getOwner(String domain) throws NamingServiceException;
    Map<String, String> batchOwners(List<String> domain) throws NamingServiceException;

    String getNamehash(String domain) throws NamingServiceException;

    List<DnsRecord> getDns(String domain, List<DnsRecordsType> types) throws NamingServiceException, DnsException;
    
    Network getNetwork();

    String getTokenUri(BigInteger tokenID) throws NamingServiceException;

    String getDomainName(BigInteger tokenID) throws NamingServiceException;

    String getProviderUrl();

    String getContractAddress();

    Map<String, Location> getLocations(String... domains) throws NamingServiceException;
}
