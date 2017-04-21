/* Created Apr 20, 2017
 *
 * @(#)DeveloperRepo.java
 *
 * (C)2017 - Emmanuel Akolly
 */
package com.example.dao;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.security.MessageDigest;
import java.util.List;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
//import sun.misc.BASE64Encoder;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.example.model.Developer;

/**
 * @author efoeakolly
 *
 */
@Repository
public class DeveloperDao implements IDeveloperDao {
	
	@Autowired
	private LdapTemplate ldap;

	/* (non-Javadoc)
	 * @see com.example.dao.DeveloperDao#getAllDevelopers()
	 */
	@Override
	public List<Developer> getAllDevelopers() {
		return ldap.search(query().base("ou=devs").where("objectclass").is("inetOrgPerson"),new DeveloperAttributesMapper());
	}


    @Override
    public List<Developer> getDeveloperByLastName(String lastName) {
        LdapQuery query = query().base("ou=devs")
                .attributes("uid", "userPassword", "givenName", "sn", "cn")
                .where("objectclass").is("inetOrgPerson")
                .and("sn").is(lastName);

        return ldap.search(query, new DeveloperAttributesMapper());

    }

    @Override
    public Developer getDeveloperByUserName(String userName) {
        LdapQuery query = query()
                .attributes("uid", "userPassword", "givenName", "sn", "cn")
                .where("objectclass").is("inetOrgPerson")
                .and("uid").is(userName);

        List<Developer> devs = ldap.search(query, new DeveloperAttributesMapper());

        return devs.get(0);
        //return getLdapTemplate().lookup(buildDn(userName), new DeveloperAttributesMapper());
    }

    @Override
    public boolean createDeveloper(Developer dev) {
        DirContextAdapter context = new DirContextAdapter(buildDn(dev));
        mapToContext(dev, context);
        ldap.bind(context);
        return true;
    }

    @Override
    public boolean updateDeveloper(Developer dev) {
        Name dn = buildDn(dev);
        /* DirContextOperations context = getLdapTemplate().lookupContext(dn);
      mapToContext(dev, context);
      getLdapTemplate().modifyAttributes(context);
      Attribute attr1 = new BasicAttribute("sn", dev.getLastName());
      ModificationItem item1 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr1);
      Attribute attr2 = new BasicAttribute("cn", dev.getFullName());
      ModificationItem item2 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr2);
      Attribute attr3 = new BasicAttribute("uid", dev.getUserName());
      ModificationItem item3 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr3);
      Attribute attr4 = new BasicAttribute("givenName", dev.getFirstName());
      ModificationItem item4 = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr4);
      getLdapTemplate().modifyAttributes(dn, new ModificationItem[] {item1,item2,item3,item4});*/

      DirContextAdapter context = (DirContextAdapter) ldap.lookup(dn);
      mapToContext(dev, context);
      ldap.modifyAttributes(dn, context.getModificationItems());

      return true;
    }

    @Override
    public boolean deleteDeveloper(Developer dev) {
        ldap.unbind(buildDn(dev));
        return true;
    }


    private class DeveloperAttributesMapper implements AttributesMapper<Developer> {

        @Override
        public Developer mapFromAttributes(Attributes attrs) throws NamingException {
            Developer dev = new Developer();
            dev.setUserName((String) attrs.get("uid").get());
            //dev.setPassword((String)attrs.get("userPassword").get());
            Object obj = (Object) attrs.get("userPassword").get();
            byte[] bytes = (byte[]) obj;
            String hash = new String(bytes);
            dev.setPassword(hash);
            dev.setFirstName((String) attrs.get("givenName").get());
            dev.setLastName((String) attrs.get("sn").get());
            dev.setFullName((String) attrs.get("cn").get());
            return dev;
        }

    }

    protected Name buildDn(Developer dev) {
        return LdapNameBuilder.newInstance()
                //.add("uid", dev.getUserName())
                .add("ou", "devs")
                .add("cn", dev.getFullName())
                //.add("sn", dev.getLastName())
                //.add("userPassword", dev.getPassword())
                //.add("givenName", dev.getFirstName())
                .build();
    }

    protected Name buildDn(String userName) {
        return LdapNameBuilder.newInstance()
                .add("uid", userName)
                .add("ou", "devs")
                .build();
    }

    protected void mapToContext(Developer dev, DirContextOperations context) {
        //context.setAttributeValues("dc", new String[]{"applications", "com"});
        context.setAttributeValues("objectclass", new String[]{"top", "person", "organizationalPerson", "inetOrgPerson"});
        context.setAttributeValue("cn", dev.getFullName());
        context.setAttributeValue("sn", dev.getLastName());
        context.setAttributeValue("uid", dev.getUserName());
        context.setAttributeValue("userPassword", dev.getPassword());
        context.setAttributeValue("givenName", dev.getFirstName());
    }
    
    protected String encryptPassword(String algorithm, String password) {
    	String encryptedPassword = null;
    	if(password != null && password.length() > 0) {
    		boolean md5Alg = "MD5".equalsIgnoreCase(algorithm);
    		boolean shaAlg = ("SHA".equalsIgnoreCase(algorithm) || "SHA1".equalsIgnoreCase(algorithm) || "SHA-1".equalsIgnoreCase(algorithm));
    		if(md5Alg || shaAlg) {
    			String algo = "MD5";
    			if(shaAlg) {
    				algo = "SHA";
    			}
    			try {
    				MessageDigest md = MessageDigest.getInstance(algo);
                    md.update(password.getBytes("UTF-8"));
                    //sEncrypted = "{" + sAlgorithm + "}" + (new Base64Encoder()).encode(md.digest());
                    encryptedPassword = new LdapShaPasswordEncoder().encodePassword(password, null);
    			} catch(Exception e) {
    				encryptedPassword = null;
    			}
    		}
    	}
    	return encryptedPassword;
    }

    /*public String encryptLdapPassword(String algorithm, String _password) {
        String sEncrypted = _password;
        if ((_password != null) && (_password.length() > 0)) {
            boolean bMD5 = algorithm.equalsIgnoreCase("MD5");
            boolean bSHA = algorithm.equalsIgnoreCase("SHA")
                    || algorithm.equalsIgnoreCase("SHA1")
                    || algorithm.equalsIgnoreCase("SHA-1");
            if (bSHA || bMD5) {
                String sAlgorithm = "MD5";
                if (bSHA) {
                    sAlgorithm = "SHA";
                }
                try {
                    MessageDigest md = MessageDigest.getInstance(sAlgorithm);
                    md.update(_password.getBytes("UTF-8"));
                    sEncrypted = "{" + sAlgorithm + "}" + (new Base64Encoder()).encode(md.digest());
                    new LdapShaPasswordEncoder().
                } catch (Exception e) {
                    sEncrypted = null;
                    //logger.error(e, e);
                }
            }
        }
        return sEncrypted;
    }*/

}
