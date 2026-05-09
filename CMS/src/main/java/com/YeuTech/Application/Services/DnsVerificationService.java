package com.YeuTech.Application.Services;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

import org.springframework.stereotype.Service;

/**
 * Real DNS verification using JNDI directory lookups.
 */
@Service
public class DnsVerificationService implements IDnsVerificationService {

    @Override
    public List<String> lookupTxtRecords(String domainName) {
        List<String> records = new ArrayList<>();
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            Attributes attrs = ctx.getAttributes(domainName, new String[]{"TXT"});
            Attribute txtAttr = attrs.get("TXT");
            if (txtAttr != null) {
                NamingEnumeration<?> values = txtAttr.getAll();
                while (values.hasMore()) {
                    String value = values.next().toString().replace("\"", "").trim();
                    records.add(value);
                }
            }
            ctx.close();
        } catch (NamingException ignored) {
            // DNS lookup failed — return empty list
        }
        return records;
    }

    @Override
    public boolean hasDnsRecords(String domainName) {
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            // Try A records first
            Attributes aAttrs = ctx.getAttributes(domainName, new String[]{"A"});
            if (aAttrs.get("A") != null) {
                ctx.close();
                return true;
            }
            // Try CNAME records
            Attributes cnameAttrs = ctx.getAttributes(domainName, new String[]{"CNAME"});
            if (cnameAttrs.get("CNAME") != null) {
                ctx.close();
                return true;
            }
            ctx.close();
        } catch (NamingException ignored) {
            // DNS lookup failed
        }
        return false;
    }
}
