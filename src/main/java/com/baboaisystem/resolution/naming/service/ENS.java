package com.baboaisystem.resolution.naming.service;

import java.math.BigInteger;

import com.baboaisystem.config.network.model.Location;
import com.baboaisystem.exceptions.ns.NSExceptionCode;
import com.baboaisystem.exceptions.ns.NSExceptionParams;
import com.baboaisystem.exceptions.ns.NamingServiceException;
import com.baboaisystem.resolution.Namehash;
import com.baboaisystem.resolution.artifacts.Numeric;
import com.baboaisystem.resolution.contracts.BaseContract;
import com.baboaisystem.resolution.contracts.ens.EnsContractType;
import com.baboaisystem.resolution.contracts.ens.Registry;
import com.baboaisystem.resolution.contracts.ens.Resolver;
import com.baboaisystem.resolution.contracts.interfaces.IProvider;
import com.baboaisystem.resolution.dns.DnsRecord;
import com.baboaisystem.resolution.dns.DnsRecordsType;
import com.baboaisystem.util.Utilities;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ENS extends BaseNamingService {

  private final Registry registryContract;

  public ENS(NSConfig config, IProvider provider) {
    super(config, provider);
    this.registryContract = (Registry) buildContract(config.getContractAddress(), EnsContractType.Registry, provider);
  }

  @Override
  public NamingServiceType getType() {
    return NamingServiceType.ENS;
  }

  @Override
  public Boolean isSupported(String domain) {
    String[] ensTLDs = { "eth", "kred", "luxe", "xyz" };
    String[] split = domain.split("\\.");
    String tld = split[split.length - 1];
    return (split.length != 0 && Arrays.asList(ensTLDs).contains(tld));
  }

  @Override
  public String getTokenUri(BigInteger tokenID) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getTokenUri", getType().toString()));
  }

  @Override
  public Map<String, String> getAllRecords(String domain) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getAllRecords", getType().toString()));
  }

  @Override
  public String getRecord(String domain, String recordKey) throws NamingServiceException {
    if (recordKey.startsWith("crypto.")) {
      String ticker = recordKey.split("\\.")[1];
      return getAddress(domain, ticker);
    }
    if (recordKey.equals("ipfs.html.value") || recordKey.equals("dweb.ipfs.hash")) {
      return getIpfsHash(domain);
    }
    Resolver resolver = getResolverContract(domain);
    byte[] tokenId = tokenId(domain);
    String record = resolver.getTextRecord(tokenId, recordKey);
    if (Utilities.isEmptyResponse(record)) {
      throw new NamingServiceException(NSExceptionCode.RecordNotFound,
        new NSExceptionParams("d|r", domain, recordKey));
    }
    return record;
  }

  @Override
  public Map<String, String> getRecords(String domain, List<String> recordKeys) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getRecords", "ENS"));
  }

  @Override
  public String getOwner(String domain) throws NamingServiceException {
   byte[] tokenId = tokenId(domain);
   String owner = registryContract.getOwner(tokenId);
    if (Utilities.isEmptyResponse(owner)) {
      throw new NamingServiceException(NSExceptionCode.UnregisteredDomain, new NSExceptionParams("d", domain));
    }
    return owner;
  }

  @Override
  public Map<String, String> batchOwners(List<String> domains) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "batchOwners", getType().toString()));
  }

  @Override
  public List<DnsRecord> getDns(String domain, List<DnsRecordsType> types) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getDns", getType().toString()));
  }

  @Override
  public Map<String, Location> getLocations(String... domains) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getLocations", getType().toString()));
  }

  private String getAddress(String domain, String ticker) throws NamingServiceException {
    if (!ticker.equalsIgnoreCase("ETH")) {
      throw new NamingServiceException(NSExceptionCode.UnsupportedCurrency,
        new NSExceptionParams("c", ticker.toUpperCase()));
    }
    Resolver resolver = getResolverContract(domain);
    byte[] tokenId = tokenId(domain);
    return resolver.addr(tokenId, ticker.toUpperCase());
  }

  private Resolver getResolverContract(String domain) throws NamingServiceException {
    String resolverAddress = getResolverAddress(domain);
    if (Boolean.TRUE.equals(Utilities.isEmptyResponse(resolverAddress))) {
      String owner = registryContract.getOwner(tokenId(domain));
      if (Utilities.isEmptyResponse(owner)) {
        throw new NamingServiceException(NSExceptionCode.UnregisteredDomain, new NSExceptionParams("d", domain));
      }
      throw new NamingServiceException(NSExceptionCode.UnspecifiedResolver, new NSExceptionParams("d", domain));
    }
    return (Resolver) buildContract(resolverAddress, EnsContractType.Resolver, provider);
  }

  private byte[] tokenId(String domain) throws NamingServiceException {
    String hash = getNamehash(domain);
    return Numeric.hexStringToByteArray(hash);
  }

  private String getResolverAddress(String domain) throws NamingServiceException {
    byte[] tokenId = tokenId(domain);
    return registryContract.getResolverAddress(tokenId);
  }

  private BaseContract buildContract(String address, EnsContractType type, IProvider provider) {
    if (type.equals(EnsContractType.Resolver)) {
      return new Resolver(blockchainProviderUrl, address, provider);
    }
    return new Registry(blockchainProviderUrl, address, provider);
  }

  private String getIpfsHash(String domain) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getIpfsHash" ,"ENS"));
  }

  @Override
  public String getNamehash(String domain) throws NamingServiceException {
    return Namehash.nameHash(domain);
  }

  public String getDomainName(BigInteger tokenID) throws NamingServiceException {
    throw new NamingServiceException(NSExceptionCode.NotImplemented, new NSExceptionParams("m|n", "getDomainName", "ENS"));
  }
}
