package com.baboaisystem.resolution.naming.service.uns;
import com.baboaisystem.resolution.naming.service.NSConfig;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UNSConfig {
  private NSConfig layer1;
  private NSConfig layer2;
}

