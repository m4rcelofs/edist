package com.erep.dist.cache;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;

import org.aspectj.lang.ProceedingJoinPoint;

import com.erep.dist.ServerConstants;

/**
 * Fa�ade to help access to AppEngine cache, since you can't get it by name
 * 
 * @author Marcelo
 * @since 19/09/2012
 */
public class CacheFacade {

	private static final Logger	logger	= Logger.getLogger(CacheFacade.class
												.getName());
	private static Cache		cache;

	static {
		CacheManager manager = CacheManager.getInstance();

		try { // TODO google doesn't use manager?
			if (manager.getCache(ServerConstants.CACHE_NAME) == null) {
				logger.fine("Cache is null, creating one");
				cache = manager.getCacheFactory().createCache(
						Collections.EMPTY_MAP);
				manager.registerCache(ServerConstants.CACHE_NAME, cache);
			}
		} catch (CacheException e) {
			logger.severe(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public static Object put(String key, Object value) {
		try {
			return cache.put(key, value);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception on cache put", e);
			return null;
		}
	}

	public static Object get(String key) {
		try {
			return cache.get(key);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception on cache get", e);
			return null;
		}
	}

	public static boolean containsKey(String key) {
		try {
			return cache.containsKey(key);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception on cache containsKey", e);
			return false;
		}
	}

	public static Object remove(String key) {
		try {
			return cache.remove(key);
		} catch (Exception e) {
			logger.log(Level.WARNING, "Exception on cache remove", e);
			return null;
		}
	}

	public static String createKey(final ProceedingJoinPoint thisJoinPoint) {
		// generate the key under which cached value is stored
		// will look like caching.aspectj.Calculator.sum(Integer=1;Integer=2;)
		StringBuilder keyBuff = new StringBuilder();

		// append name of the class
		keyBuff.append(thisJoinPoint.getTarget().getClass().getName());

		// append name of the method
		keyBuff.append(".").append(thisJoinPoint.getSignature().getName());

		keyBuff.append("(");
		// loop through cacheable method arguments
		for (final Object arg : thisJoinPoint.getArgs())
			if (arg != null)
				// append argument type and value
				keyBuff.append(arg.getClass().getSimpleName() + "=" + arg + ";");
		keyBuff.append(")");

		return keyBuff.toString();
	}

}
