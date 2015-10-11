package com.simplebank.supersimplestocks.sd;

import com.simplebank.supersimplestocks.fix.SecurityType;

/**
 * Interface to access stock static data
 * 
 * @author Francesc Montserrat
 *
 */
public interface StaticDataService {
  
  /**
   * Obtain the security type for a certain stock
   * 
   * @return SecurityType
   */
  public SecurityType securityType(String ticker);

}
