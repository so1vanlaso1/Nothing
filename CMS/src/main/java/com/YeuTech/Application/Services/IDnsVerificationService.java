package com.YeuTech.Application.Services;

import java.util.List;

/**
 * Abstraction for DNS record lookups, allowing real JNDI lookups in production
 * and mock implementations in tests.
 */
public interface IDnsVerificationService {

    /**
     * Look up TXT records for the given domain.
     *
     * @param domainName the domain to query
     * @return list of TXT record values, empty if none found or lookup fails
     */
    List<String> lookupTxtRecords(String domainName);

    /**
     * Check whether the given domain has A or CNAME records resolving to any address.
     *
     * @param domainName the domain to query
     * @return true if A or CNAME records exist
     */
    boolean hasDnsRecords(String domainName);
}
