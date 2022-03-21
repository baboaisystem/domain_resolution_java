package com.baboaisystem.resolution.naming.service;

import com.baboaisystem.config.network.model.Network;
import com.baboaisystem.resolution.contracts.interfaces.IProvider;

public abstract class BaseNamingService implements NamingService {
    protected Network chainId;
    protected String blockchainProviderUrl;
    protected String contractAddress;
    protected IProvider provider;

    protected BaseNamingService(NSConfig nsConfig, IProvider provider) {
        this.chainId = nsConfig.getChainId();
        this.blockchainProviderUrl = nsConfig.getBlockchainProviderUrl();
        this.contractAddress = nsConfig.getContractAddress();
        this.provider = provider;
    }

    public Network getNetwork() {
        return chainId;
    }

    public String getProviderUrl() {
        return blockchainProviderUrl;
    }

    public String getContractAddress() {
        return contractAddress;
    }
}
