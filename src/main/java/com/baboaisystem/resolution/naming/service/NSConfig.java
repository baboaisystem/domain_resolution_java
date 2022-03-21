package com.baboaisystem.resolution.naming.service;

import com.baboaisystem.config.network.model.Network;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NSConfig {
    private Network chainId;
    private String blockchainProviderUrl;
    private String contractAddress;
}
