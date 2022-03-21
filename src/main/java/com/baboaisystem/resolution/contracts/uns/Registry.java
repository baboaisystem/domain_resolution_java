package com.baboaisystem.resolution.contracts.uns;

import com.baboaisystem.resolution.contracts.BaseContract;
import com.baboaisystem.resolution.contracts.interfaces.IProvider;

public class Registry extends BaseContract {
    private static final String ABI_FILE = "uns/registry_abi.json";
    private static final String NAMING_SERVICE_NAME = "UNS";

    public Registry(String url, String address, IProvider provider) {
        super(NAMING_SERVICE_NAME, url, address, provider);
    }

    @Override
    protected String getAbiPath() {
      return ABI_FILE;
    }
}
